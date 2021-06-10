import java.util.concurrent.Semaphore;
public class IngredientThread extends Thread{ 
	private Semaphore r; 
	private Semaphore[] ca;
	private Semaphore mx;
	private int[] cntr;
	 
	private int ri;
	private String n; 
	
	public IngredientThread (Semaphore r, Semaphore[] ca, Semaphore mx, int ri, String n, int[] cntr){
		this.r = r;
		this.ca = ca;
		this.mx = mx;
		this.ri = ri;
		this.n = n;
		this.cntr = cntr;
	}
	
	public void run(){
		for(;;){
			try {
				r.acquire();
				DrainArray(ca);
				printConsole(n + " is being taken");
				mx.acquire();
				cntr[0] = cntr[0] + ri;
				ca[cntr[0]-1].release();
				mx.release();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
	
	public static void DrainArray(Semaphore[] sa){
		int m = 0;
		while(m < sa.length){
			int n = sa[m].availablePermits();
			if (n > 2){
				sa[m].drainPermits();
			}
			m++;
		}
	}
	
	public void printConsole(Object o) {
		System.out.println(o);
	}

}
