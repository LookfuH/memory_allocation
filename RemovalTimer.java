public class RemovalTimer implements Runnable{

    GenericFit fit;
    String process;
    int time;

    public RemovalTimer(GenericFit fit, String process, int time){
        this.fit = fit;
        this.process = process;
        this.time = time;
    }

    public synchronized void run() {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) { e.printStackTrace(); }

        fit.remove(process);
        notify();
    }
}
