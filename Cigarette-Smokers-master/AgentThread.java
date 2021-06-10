import java.util.Random;
import java.util.concurrent.Semaphore;
public class AgentThread extends Thread{
	private Semaphore ps;
	private Semaphore tb;
	private Semaphore ms;
	private Semaphore ppr;
	private Random r = new Random();
	private int rnd; 
	
	public AgentThread() {
		
	}
	
	public AgentThread (Semaphore a, Semaphore b, Semaphore c, Semaphore d){
		ps = a;
		tb = b;
		ppr = c;
		ms = d;
	}
	
	public void run(){
		for(;;){
			try {
				//start the process by picking a random number
				sleep(1000);
				printConsole("**************************************************");
				rnd = r.nextInt(3);
				printConsole("Agent is putting"); 
				if (getRound() == 0){
					roundZero(ps,ppr,tb);
				}
				else if (getRound() == 1){
					roundOne(ps,ppr,tb);
				}
				else if (getRound() == 2){
					roundTwo(ps,ppr,tb);
				}
				
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public void roundOne(Semaphore ps, Semaphore ppr, Semaphore tb) throws Exception {
		ps.acquire();
		ms.release(); 
		printConsole("Matches");
		tb.release();
		printConsole("and Tobacco");
	}
	
	public void roundTwo(Semaphore ps, Semaphore ppr, Semaphore tb) throws Exception {
		ps.acquire();
		ms.release();
		printConsole("Matches");
		ppr.release(); 
		printConsole("and Paper");
	}
	
	public void roundZero(Semaphore ps, Semaphore ppr, Semaphore tb) throws Exception {
		ps.acquire();
		ppr.release(); 
		printConsole("Paper");
		tb.release(); 
		printConsole("and Tobacco");
	}
	
	public void printConsole(Object o) {
		System.out.println(o);
	}
	
	public int getRound() {
		return rnd;
	}

}
