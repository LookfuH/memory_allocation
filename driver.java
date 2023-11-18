import java.util.Scanner;

public class driver {
    public static void main(String[] args) {

        int maxSize = 1024; // TODO: Read from config
        
        Object fit = null;
        System.out.print("Options are: BF, WF, NF: ");
        Scanner sc = new Scanner(System.in);
        String target = sc.next();
        if( target.equalsIgnoreCase("BF")){
            //needs list?
            fit = new BestFit(maxSize);
        }
        else if (target.equalsIgnoreCase("WF")){
            fit = new WorstFit(maxSize);
        }
        else if (target.equalsIgnoreCase("NF")) {
            fit = new NextFit(maxSize);
        }
        else {
           fit = new FirstFit(maxSize);
            System.out.println("Invaild input");
        }
        sc.close();
    }
}

