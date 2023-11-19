import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class driver {

        static int partSize = 1024; // TODO: Read from config
        static int maxProcessSize = 100;
        static int numProcesses = 10;
        static int maxProcessTime = 1000;

    public static void main(String[] args) {

        GenericFit fit = null;
        System.out.print("Options are: BF, WF, NF: ");
        Scanner sc = new Scanner(System.in);
        String target = sc.next();
        if( target.equalsIgnoreCase("BF")){
            fit = new BestFit(partSize);
        }
        else if (target.equalsIgnoreCase("WF")){
            fit = new WorstFit(partSize);
        }
        else if (target.equalsIgnoreCase("NF")) {
            fit = new NextFit(partSize);
        }
        else {
            System.out.println("Invaild input");
        }
        sc.close();

        List<Object[]> list = generateProcesses();
        driver app = new driver();
        app.handleProcesses(list, fit);

    }

    public static List<Object[]> generateProcesses() {
        ArrayList<Object[]> list = new ArrayList<Object[]>();
        Random rand = new Random();
        for (int i = 0; i < numProcesses; i++) {
            Object[] newProcess = {"P"+i, rand.nextInt(maxProcessSize), rand.nextInt(maxProcessTime)}; // Format: name, size, time
            list.add(newProcess);
        }

        return list;
    }

    synchronized void handleProcesses(List<Object[]> list, GenericFit fit) {
        for ( Object[] process : list) {
            while (fit.add((String)process[0], (int)process[1]) < 0) {
                try {
                    wait();
                } catch (InterruptedException e) { e.printStackTrace(); }
            };

            Thread t = new Thread(new RemovalTimer(fit, (String)process[0], (int)process[2]));
            t.start();
        }
    }
}
