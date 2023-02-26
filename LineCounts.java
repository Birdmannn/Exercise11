import java.io.File;
import java.util.Scanner;

public class LineCounts {

    public static void main(String[] args) {
        Scanner in;
        File[] inputFile;

        File userHomeDirectory = new File(System.getProperty("user.home"));
        if(args.length == 0) {
            System.out.println("No file was in the input");
            return;
        }

        inputFile = new File[args.length];
        int lineCount;

        for(int i = 0; i < inputFile.length; i++) {
            try {
                inputFile[i] = new File(userHomeDirectory, args[i]);
                lineCount = 0;
                if (!inputFile[i].isFile()) {
                    if (!inputFile[i].exists())
                        System.out.println("The file" + inputFile[i] + " does not exist.");
                    else
                        System.out.println(inputFile[i] + " is not a file.");
                }
                else {
                    in = new Scanner(inputFile[i]);
                    while(in.hasNextLine()) {
                        in.nextLine();
                        lineCount++;
                    }
                    System.out.println("The file " + inputFile[i] + " has " + String.format("%22d lines.", lineCount));
                }
            }
            catch(Exception e) {              //Should catch the error, print the error message and move on to the next args, if any.
                System.out.print("An error occurred while accessing file " + inputFile[i]);
                System.out.print(": " + e);
            }
        }
    }  // end of main.
}   //end of LineCounts class, smiles..
