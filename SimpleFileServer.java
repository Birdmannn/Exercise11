import java.net.*;
import java.util.Scanner;
import java.io.*;


public class SimpleFileServer {
    static final int DEFAULT_PORT = 1728;

    public static void main(String[] args) {
        int port;

        ServerSocket listener;
        Socket connection;

        Scanner readFromClient;
        PrintWriter writeToClient;
        File userHomeDirectory = new File(System.getProperty("user.home"));
        File outputFile;

        if(args.length == 0) {
            port = DEFAULT_PORT;
            outputFile = new File(userHomeDirectory, "Documents");
        }
        else {
            char ch = args[0].charAt(0);
            if(args.length == 1) {
                if(Character.isDigit(ch)) {
                    try {
                        port = Integer.parseInt(args[0]);
                        outputFile = new File(userHomeDirectory, "Documents");
                        if(port < 0 || port > 65535)
                            throw new NumberFormatException();
                    }
                    catch(NumberFormatException e) {
                        System.out.println("Illegal port number, " + args[0]);
                        return;
                    }
                }
                else {
                    outputFile = new File(userHomeDirectory, args[0]);
                    port = DEFAULT_PORT;
                }
            }
            else {
                outputFile = new File(userHomeDirectory, args[1]);
                port = DEFAULT_PORT;
            }
        }

        //Wait for a connection request.
        try {
            listener = new ServerSocket(port);
            System.out.println("File Server: " + outputFile);
            connection = listener.accept();
            listener.close();
            readFromClient = new Scanner(new InputStreamReader(connection.getInputStream()));
            writeToClient = new PrintWriter(connection.getOutputStream());

            writeToClient.println("Connected.");
            writeToClient.flush();

            try {
                String clientInput;
                if (!outputFile.exists()) {
                    if (!outputFile.isFile())
                        System.out.println(outputFile + " is not a file.");
                    else
                        System.out.println(outputFile + " does not exist.");
                } else {
                    File[] fileList;
                    writeToClient.println("Enter 'quit' to end the program.");
                    while (true) {
                        clientInput = readFromClient.next();
                        if (clientInput.equalsIgnoreCase("QUIT")) {
                            writeToClient.println("The connection has been closed.");
                            writeToClient.flush();
                            writeToClient.close();
                            System.out.println("Connection closed.");
                            break;
                        } else if (clientInput.equalsIgnoreCase("INDEX")) {
                            fileList = outputFile.listFiles();
                            writeToClient.println("Files in " + outputFile + ": ");
                            assert fileList != null;
                            for (File file : fileList) {
                                writeToClient.println("    " + file);
                            }
                            writeToClient.println("Finished.");
                        } else if (clientInput.equalsIgnoreCase("GET")) {
                            if (readFromClient.hasNext()) {
                                clientInput = readFromClient.next();
                                File checkFile = new File(outputFile, clientInput);
                                if (!checkFile.exists() && !checkFile.isFile()) {
                                    writeToClient.println("ERROR.");
                                    break;
                                }

                                writeToClient.println("OK.");
                                writeToClient.println("Files in " + checkFile + ": ");
                                fileList = checkFile.listFiles();
                                assert fileList != null;
                                for (File file : fileList) {
                                    writeToClient.println("    " + file);
                                }
                            }
                        } else {
                            writeToClient.println("\"" + clientInput + "\" is not a command.");
                        }
                        writeToClient.flush();

                        if(writeToClient.checkError())
                            throw new IOException("Error occurred while transmitting message.");
                    } // end of while.
                }
            }
            catch (Exception e) {
                System.out.println("An Error has occurred.");
                System.out.println("Error: " + e);
            }
            finally {
                connection.close();
            }
        }
        catch (Exception e) {
            System.out.println("An error occurred while opening connection.");
            System.out.println("Error: " + e);
        }

    }
}
