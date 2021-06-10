package crawler2;

import java.io.File;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SleepingBarber{
	
public static void main (String a[]) throws InterruptedException {	
		
		int noOfBarbers=0, customerId=1;
		int noOfCustomers=10, noOfChairs;	
		
		Scanner sc = new Scanner(System.in);
		
    	noOfBarbers=1; // Since it is single barber problem.
    	
    	System.out.println("waiting room with"+ " no of chairs:");	
    			
    	noOfChairs=sc.nextInt();
    	

    	
		ExecutorService exec = Executors.newFixedThreadPool(12);	//12 thread pool
    	Shop shop = new Shop(noOfBarbers, noOfChairs);				//Shop with given number of Barbers and chairs
    	Random r = new Random();  									//delays for customer arrival.
       	    	
        System.out.println("Number of barbers are  "+noOfBarbers+"\n");
        
      
        for(int i=1; i<=noOfBarbers;i++) {			//barber threads.
        	
        	Barber barber = new Barber(shop, i);	
        	Thread thbarber = new Thread(barber);
            exec.execute(thbarber);
        }
        
        for(int i=0;i<noOfCustomers;i++) {			//customer threads
        
            Customer customer = new Customer(shop);
            Thread thcustomer = new Thread(customer);
            customer.setcustomerId(customerId++);
            exec.execute(thcustomer);
            
            try {
            	
            	double val = r.nextGaussian() * 2000 + 2000;			
            	int millisDelay = Math.abs((int) Math.round(val));		
            	Thread.sleep(millisDelay);								
            }
            catch(InterruptedException iex) {
            
                iex.printStackTrace();
            }
            
        }
        
        exec.shutdown();												
        exec.awaitTermination(10, SECONDS);						//wait time.
 
      	//Summary.
        
        System.out.println("\n Done, Shop closed.");
        System.out.println("\n Summary:");
        System.out.println("\nTotal customers who came in: "+noOfCustomers+"\nTotal customers served: "+shop.getTotalHairCuts()+"\nTotal customers lost: "+shop.getCustomerLost());
               
        sc.close();
    }
}

//Barber class
 
class Barber implements Runnable {									

    Shop shop;
    int barberId;
 
    public Barber(Shop shop, int barberId) {
    
        this.shop = shop;
        this.barberId = barberId;
    }
    
    public void run() {
    
        while(true) {
        
            shop.cutHair(barberId);
        }
    }
}

//Customer class
class Customer implements Runnable {

    int customerId;
    Shop shop;
 
    public Customer(Shop shop) {
    
        this.shop = shop;
    }
 
    public int getCustomerId() {										
        return customerId;
    }
 
    public void setcustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void run() {													
    
        goForHairCut();
    }
    private synchronized void goForHairCut() {			//customer added
    
        shop.add(this);
    }
}
 
class Shop {

	private final AtomicInteger totalHairCuts = new AtomicInteger(0);
	private final AtomicInteger customersLost = new AtomicInteger(0);
	int nchair, noOfBarbers, availableBarbers;
    List<Customer> customers;
    
    Random r = new Random();	 
    
    public Shop(int noOfBarbers, int noOfChairs){
    
        this.nchair = noOfChairs;												
        customers = new LinkedList<Customer>();					//customers coming in.
        this.noOfBarbers = noOfBarbers;								
        availableBarbers = noOfBarbers;
    }
 
    public AtomicInteger getTotalHairCuts() {
    	
    	totalHairCuts.get();
    	return totalHairCuts;
    }
    
    public AtomicInteger getCustomerLost() {
    	
    	customersLost.get();
    	return customersLost;
    }
    
    public void cutHair(int barberId)
    {
        Customer customer;
        synchronized (customers) {									
        															 	
            while(customers.size()==0) {
            
                System.out.println("\nNo customers. Barber "+barberId+" is sleeping now!");
                
                try {
                
                    customers.wait();								//sleeping barber
                }
                catch(InterruptedException iex) {
                
                    iex.printStackTrace();
                }
            }
            
            customer = (Customer)((LinkedList<?>)customers).poll();	
            
            System.out.println("\nBarber "+barberId +" is woken up by customer "+customer.getCustomerId());
        }
        
        int millisDelay=0;
                
        try {
        	
        	availableBarbers--; 						//barber engaged
            System.out.println("\nBarber "+barberId+" is working. "+
            		customer.getCustomerId()+ " so customer sleeps");
        	
            double val = r.nextGaussian() * 1000 + 8000;		// time to cut cut
        	millisDelay = Math.abs((int) Math.round(val));				
        	Thread.sleep(millisDelay);
        	
        	System.out.println("\nCompleted Cutting hair of "+customer.getCustomerId()+" by barber " + barberId +" in "+millisDelay+ " milliseconds.");
        
        	totalHairCuts.incrementAndGet();
            															
            if(customers.size()>0) {									
            	System.out.println("\nBarber "+barberId+" picks up a customer from the "+ " the waiting room");		
            }
            
            availableBarbers++;					
        }
        catch(InterruptedException iex) {
        
            iex.printStackTrace();
        }
        
    }
 
    public void add(Customer customer) {
    
        System.out.println("\nNew Customer "+customer.getCustomerId()+" has come to the shop.");
 
        synchronized (customers) {
        
            if(customers.size() == nchair) {							
            
                System.out.println("\nAll chairs are occupied already! "+ "So customer "+customer.getCustomerId()+" is leaving");
                
              customersLost.incrementAndGet();
                
                return;
            }
            else if (availableBarbers > 0) {							
            	((LinkedList<Customer>)customers).offer(customer);
				customers.notify();
			}
            else {														
            	((LinkedList<Customer>)customers).offer(customer);
                
            	System.out.println("All barber(s) are busy so "+customer.getCustomerId()+" takes a chair in the waiting room");
                 
                if(customers.size()==1)
                    customers.notify();
            }
        }
    }

}
