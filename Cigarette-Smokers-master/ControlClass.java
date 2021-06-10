import java.util.concurrent.Semaphore;
public class ControlClass {
	//declaring the variables
	private Semaphore[] arr = new Semaphore[6];
	private Semaphore mx;
	private Semaphore ps;
	private Semaphore tb;
	private Semaphore ppr;
	private Semaphore ms;
	
	//Smokers having infinite at the table 
	//Needs the other two resources 
	//for cigar places on table
	private SmokersThread smoker1;
	private SmokersThread smoker2;
	private SmokersThread smoker3;
	
	//Resources are cached to increment the appropriate resources
	private IngredientThread Cached_tb;
	private IngredientThread Cached_ppr;
	private IngredientThread Cached_m;
	
	//To control index 
	private int[] cntr;
	
	//Agent needed to place the element 
	private AgentThread agnt;
	
	//Start of the program 
	public void startSolution(){
		//Initializing all the declared variables 
		
		cntr = new int[1];
		mx = new Semaphore(1);
		cntr[0] = 0;
		int y = 0;
		while(y < arr.length){
			arr[y] = new Semaphore(0);
			y++;
		}
		tb = new Semaphore(0);
		ms = new Semaphore(0);
		ps = new Semaphore(1);
		ppr = new Semaphore(0);
		
		//One agent thread 
		agnt = new AgentThread(ps, tb, ppr, ms);
		
		//Three resources threads
		Cached_tb = new IngredientThread (tb, arr, mx, 4, "Tobacco", cntr);
		Cached_ppr = new IngredientThread (ppr, arr, mx, 2, "Paper",cntr);
		Cached_m = new IngredientThread (ms, arr, mx, 1, "Matches",cntr);
		
		
		//Three smokers threads
		smoker1 = new SmokersThread(ps, arr, cntr, arr[2], "smoker1");
		smoker2 = new SmokersThread(ps, arr, cntr, arr[5], "smoker2");
		smoker3 = new SmokersThread(ps, arr, cntr, arr[4], "smoker3");
		
		//starting agent thread
		agnt.start();
		
		//starting resources threads
		Cached_tb.start();
		Cached_ppr.start();
		Cached_m.start();
		
		//starting smokers threads
		smoker1.start();	
		smoker2.start();
		smoker3.start();
	}
}
