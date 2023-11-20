import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class driver {

    static String filePath = "settings.config";
    //parameters
    static int partSize;// = 1024;
    static int maxProcessSize;// = 256;
    static int numProcesses;// = 10;
    static int maxProcessTime;// = 10000;

    public static void main(String[] args) {

        try {
           readFile();
        } catch (FileNotFoundException e) {
            System.err.println("File '" + filePath + "' does not exist. Please locate this file and try again." );
            System.exit(1);
        }
        //Option select
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

        Process[] list = generateProcesses(fit);
        driver app = new driver();
        app.handleProcesses(list, fit);

    }
    //filereader
    public static void readFile() throws FileNotFoundException {
        Scanner read = new Scanner(new File(filePath));

        while (read.hasNextLine()) {
            String[] line = read.nextLine().split("=");
            String key = line[0].strip();
            int value = Integer.parseInt(line[1].strip());

            switch (key) {
                case "MEMORY_MAX":
                    partSize = value;
                    break;
                case "PROC_SIZE_MAX":
                    maxProcessSize = value;
                    break;
                case "NUM_PROC":
                    numProcesses = value;
                    break;
                case "MAX_PROC_TIME":
                    maxProcessTime = value;
                    break;
                default:
                    System.err.println("ERROR: Invalid key '" + key + "' in " + filePath);
                    System.exit(1);
                    break;
            }
        }


    }

    public static Process[] generateProcesses(GenericFit fit) {
        Random rand = new Random();
        Process[] list = new Process[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            list[i] = new Process("P"+i, rand.nextInt(maxProcessSize), rand.nextInt(maxProcessTime), fit);
        }
        return list;
    }

    public void handleProcesses(Process[] list, GenericFit fit) {
        ArrayList<Thread> threadList = new ArrayList<Thread>();
        double startTime = System.currentTimeMillis();
        int delayedProcesses = 0;
        int delayTime = 0;
        for (Process process : list) {
            boolean delayed = false;
            while (fit.add(process.name, process.size) < 0) {
                // timer.waitForRemoval();
                try {
                    delayTime++;
                    delayed = true;
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            delayedProcesses += delayed ? 1 : 0;
            Thread p = new Thread(process);
            p.start();
            threadList.add(p);
        }

        threadList.forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        System.out.println("\n-----RESULTS-----");
        System.out.println("Time taken: " + (int)(System.currentTimeMillis() - startTime) + "ms");
        System.out.println("Delayed Processes: " + delayedProcesses);
        System.out.println("Total delayed time: " + delayTime + "ms");
    }
}


//                 ██        ██
//               ██▒▒██    ██▒▒██
//             ██▒▒▒▒▒▒████▒▒▒▒▒▒██
//         ▓▓████▒▒▒▒▒▒▓▓▒▒▒▒▒▒▒▒▓▓██
//       ▓▓▒▒▒▒██▒▒▒▒██████▒▒▒▒▒▒▒▒▓▓
//       ██▒▒▒▒▒▒▒▒██▒▒▒▒▒▒██▒▒▒▒▒▒██
//     ▓▓████▒▒▒▒██▒▒░░▒▒░░▒▒████▒▒██
//     ██▒▒▒▒▒▒██▒▒░░░░░░░░░░░░░░████
//     ██████████▒▒██░░▒▒░░░░░░░░░░██
//   ██░░░░░░██▒▒░░░░░░░░░░░░░░░░░░░░██
// ██░░░░░░░░██▒▒░░▒▒░░▒▒░░░░░░░░░░░░██
//   ██░░░░░░██▒▒▒▒░░░░░░░░░░░░░░░░░░░░██
//     ████░░██▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░██
//         ████▒▒▒▒░░▒▒░░░░░░░░░░░░░░░░░░██
//           ██▒▒▒▒░░░░░░░░░░░░░░░░░░░░░░██
//             ██▒▒▒▒░░▒▒░░░░░░░░░░░░░░░░░░██
//             ██▒▒░░░░░░░░░░░░░░░░░░▒▒░░░░██
//             ██▒▒░░▒▒░░░░░░▒▒░░░░░░░░▒▒░░▒▒▒▒                                  ▓▓
//             ██▒▒▒▒░░▒▒░░░░░░▒▒░░░░░░░░▒▒░░░░▓▓██                            ▓▓░░██
//             ██▒▒▒▒░░░░▒▒░░░░▒▒▒▒░░░░▒▒██▒▒▒▒░░░░██                        ██░░░░██
//               ██▒▒░░░░░░▒▒░░▒▒██▒▒░░▒▒██████▒▒▒▒▒▒██                    ██░░░░░░██
//               ██▒▒▒▒░░░░▒▒░░▒▒██▒▒░░▒▒██░░░░██████░░██████          ████░░░░░░██
//             ▓▓▒▒▒▒▓▓▒▒▒▒▓▓▒▒▒▒██▓▓▒▒▒▒██░░░░▒▒░░▓▓░░▓▓▓▓▒▒░░▓▓▓▓░░▓▓▓▓▒▒░░░░▓▓██▓▓
//             ██▒▒▓▓██▒▒▓▓██▒▒▒▒██▒▒▓▓▒▒██░░▓▓▓▓▓▓████▓▓██▓▓░░░░░░▒▒░░░░░░░░░░░░░░▒▒▓▓
//             ██▓▓▒▒██▒▒██▒▒▓▓▒▒██░░▒▒▓▓░░▓▓▓▓▓▓▒▒▒▒▒▒▓▓▓▓▓▓▓▓▓▓░░░░░░░░░░░░░░░░░░▓▓░░
//           ▓▓▒▒▒▒░░▒▒▓▓▒▒░░▒▒██▒▒░░░░▒▒▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓▒▒▓▓░░░░░░░░░░░░░░░░██
//           ██░░░░░░░░▒▒░░░░░░▒▒░░░░░░░░██▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██░░░░░░░░░░░░▓▓▓▓
//           ██░░░░░░░░░░░░░░░░░░░░░░░░░░██▓▓▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██░░░░░░░░░░░░░░▓▓
//           ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░██▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒██░░░░░░░░░░░░░░██
//             ██░░░░░░░░░░░░░░░░░░░░░░░░░░██▓▓▒▒▒▒▒▒▒▒▒▒▒▒▒▒▓▓▓▓▓▓██░░░░░░░░░░██
//             ██░░░░░░░░░░░░░░░░░░░░░░░░░░██▓▓▓▓▓▓▓▓▓▓▒▒▒▒▒▒▓▓████░░░░░░░░░░██
//             ██░░░░░░░░░░░░░░░░░░░░░░░░░░██▓▓▓▓▓▓████▓▓▓▓▒▒▓▓▓▓██░░░░░░░░██
//             ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░██▓▓▓▓██░░██▓▓▓▓████░░░░░░▒▒░░██
//             ▒▒▓▓░░░░░░░░░░░░░░░░░░░░░░░░░░░░████░░░░░░████░░░░░░░░░░░░░░██
//               ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██
//                 ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██
//                 ██░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░██
//                   ████░░░░░░░░░░░░░░░░░░░░██░░░░░░░░░░░░░░░░░░░░░░██
//                     ██░░░░░░░░░░░░░░░░░░░░░░██░░░░░░░░░░░░░░░░░░██
//                       ████░░░░░░░░░░░░░░░░░░░░██░░░░░░░░░░░░░░██
//                         ██░░░░░░░░░░░░░░░░░░░░░░██░░░░░░██████
//                           ██████████░░░░░░░░░░░░░░██░░██
//                             ██░░██  ████████████████████
//                         ██████░░██                ██░░██
//                       ██░░░░░░░░░░██          ██████░░██
//                         ████░░░░██░░▓▓      ██░░░░░░░░░░██
//                         ██▒▒██░░██▓▓          ████░░░░██░░▓▓
//                         ██████░░██            ██░░██░░████
//                               ██              ██████░░██
//                                                     ██
