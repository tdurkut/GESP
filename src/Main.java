import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        if(args.length != 4 || !args[0].equals("-i") || !args[2].equals("-m") ||
                (!args[3].toUpperCase().equals("GESP") && !args[3].toUpperCase().equals("JCB"))) {
            System.out.println("Parameters should be: -i filePath -m methodName(JCB/GESP)");
            return;
        }
        try
        {
           if(args[3].toUpperCase().equals("GESP"))
           {
               GESP gesp = new GESP();
               gesp.readFile(args[1]);
               gesp.solveTheSystem();
           }
           else if(args[3].toUpperCase().equals("JCB"))
           {
               System.out.println("We're sorry that The Jacobi’s method has not been implemented yet.");
           }
        }
        catch(Exception ex)
        {
            String message = ex.getMessage();
        }
    }
}
