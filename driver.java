import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class driver {

    static String filePath = "settings.config";

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

        for (Process process : list) {
            while (fit.add(process.name, process.size) < 0) {
                // timer.waitForRemoval();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Thread p = new Thread(process);
            p.start();
        }
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
//                                                     ██                '
