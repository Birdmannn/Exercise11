import java.io.File;
import java.util.Scanner;

public class DirectoryList2 {

    public static void main(String[] args) {
        String directoryName;
        File directory;
        File userHomeDirectory = new File(System.getProperty("user.home"));

        System.out.println(userHomeDirectory);
        Scanner in = new Scanner(System.in);

        System.out.print("Enter a directory name:   ");
        directoryName = in.nextLine().trim();

        directory = new File(userHomeDirectory, directoryName);

        if(!directory.isDirectory()) {
            if(!directory.exists())
                System.out.println("There is no such directory.");
            else
                System.out.println("That file is not a directory.");
        }
        else {
            System.out.println("\nFiles in directory \"" + directoryName + "\":\n");
            printFiles(directory);
        }
    }

    static void printFiles(File dir) {
        File[] files = dir.listFiles();

        assert files != null;
        for(File file : files) {
            if(!file.isDirectory())
                System.out.println( "       " + file.getName() );
            else
                printFiles(file);
        }
    }
}
