import java.util.concurrent.Semaphore;
public class SmokersThread extends Thread{
		private Semaphore s; 
		private String n;
		private int[] cntr;
		private Semaphore r;
		
		public SmokersThread (Semaphore s, Semaphore[] array, int[] cntr,Semaphore r, String n){
			this.s = s;
			this.cntr = cntr;
			this.n = n;
			this.r = r;
		}
		
		public void run (){
			for(;;){
				try {
					r.acquire();
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				// repeat the process again
				printConsole(n +" completes his cigarette");
				cntr[0] = 0;
				printConsole("Reset Counter");
				printConsole(n + " smokes");
				printConsole(n + " done");
				printConsole("********************************************************");
				s.release();
				try {
					sleep(1000);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		}
		
		public void printConsole(Object o) {
			System.out.println(o);
		}


}
