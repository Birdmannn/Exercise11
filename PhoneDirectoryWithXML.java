import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.Scanner;

public class PhoneDirectoryWithXML {

    private static final String DATA_FILE_NAME = ".phone_book_demo3";
    private static final File userHomeDirectory = new File(System.getProperty("user.home"));
    private static TreeMap<String,String> phoneBook;

    public static void main(String[] args) {
        String name, number;
        phoneBook = new TreeMap<>();

        File dataFile = new File(userHomeDirectory, DATA_FILE_NAME);

        if(! dataFile.exists()) {
            System.out.println("No phone book data file found. A new one");
            System.out.println("Will be created, if you add any entries.");
            System.out.println("File name: " + dataFile.getAbsolutePath());
        }
        else {
            //This is where you read phone book from data by parsing the XML
            System.out.println("Reading phone book data...");
            loadPhoneBook(dataFile);
        }

        /*
        Read commands from the user and carry them out, until the
        user gives the "Exit from program" command.
         */

        Scanner in = new Scanner(System.in);
        boolean changed = false;

        mainLoop: while(true) {
            System.out.println("\nSelect the action that you want to perform:");
            System.out.println("   1.  Look up a phone number.");
            System.out.println("   2.  Add or change a phone number.");
            System.out.println("   3.  Remove an entry from your phone directory");
            System.out.println("   4.  List the entire phone directory.");
            System.out.println("   5.  Exit from the program.");
            System.out.println("Enter action number (1-5):  ");

            int command;
            if(in.hasNextInt()) {
                command = in.nextInt();
                in.nextLine();
            }
            else {
                System.out.println("\nILLEGAL RESPONSE. YOU MUST ENTER A NUMBER.");
                in.nextLine();
                continue;
            }

            switch(command) {
                case 1 -> {
                    System.out.print("\nEnter the name whose number you wish to look up:  ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if(number == null)
                        System.out.println("\nSORRY, NO NUMBER FOUND FOR " + name);
                    else
                        System.out.println("\nNUMBER FOR " + name + ":  " + number);
                }
                case 2 -> {
                    System.out.print("\nEnter the name: ");
                    name = in.nextLine().trim().toLowerCase();
                    if(name.length() == 0)
                        System.out.println("\nNAME CANNOT BE BLANK.");
                    else if(name.indexOf('%') >= 0)
                        System.out.println("\nNAME CANNOT CONTAIN THE CHARACTER \"%\"");
                    else {
                        System.out.print("Enter phone number: ");
                        number = in.nextLine().trim();
                        if(number.length() == 0)
                            System.out.println("\nPHONE NUMBER CANNOT BE BLANK.");
                        else {
                            phoneBook.put(name, number);
                            changed = true;
                        }
                    }
                }
                case 3 -> {
                    System.out.print("\nEnter the name whose entry you wish to remove: ");
                    name = in.nextLine().trim().toLowerCase();
                    number = phoneBook.get(name);
                    if(number == null)
                        System.out.println("\nSORRY, THERE IS NO ENTRY FOR " + name);
                    else {
                        phoneBook.remove(name);
                        changed = true;
                        System.out.println("\nDIRECTORY ENTRY REMOVED FOR " + name);
                    }
                }
                case 4 -> {
                    System.out.println("\nLIST OF ENTRIES IN YOUR PHONE BOOK:\n");
                    for(Map.Entry<String,String> entry : phoneBook.entrySet())
                        System.out.println("  " + entry.getKey() + ": " + entry.getValue());
                }
                case 5 -> {
                    System.out.println("\nExiting program.");
                    break mainLoop;
                }
                default -> System.out.println("\nILLEGAL ACTION NUMBER.");
            }
        } // end of mainLoop.

        if(changed) {
            System.out.println("Saving phone directory to file " + dataFile.getAbsolutePath() + " ...");
            savePhoneBook(dataFile);
        }
    }

    static void loadPhoneBook(File dataFile) {
        DocumentBuilder docReader;
        Document xmlDoc;
        Element element;
        try {
            docReader = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmlDoc = docReader.parse(dataFile);
            element = xmlDoc.getDocumentElement();
            NodeList children = element.getChildNodes();

            for(int i = 0; i < children.getLength(); i++) {
               Node child = children.item(i);
               if(child instanceof Element nameElement) {
                   String name = nameElement.getAttribute("name");
                   String number = nameElement.getAttribute("number");
                   phoneBook.put(name,number);
               }
            }
        }
        catch (Exception ignored) {}
    }

    static void savePhoneBook(File savedFile) {
        try {
            PrintWriter writeFile = new PrintWriter(savedFile);
            writeFile.println("<?xml version=\"1.0\"?>");
            writeFile.println("<phone_directory>");

            for(Map.Entry<String,String> entry : phoneBook.entrySet())
                writeFile.println("  <entry name='" + entry.getKey() + "' number='" + entry.getValue() + "'/>");

            writeFile.println("</phone_directory>");
            if(writeFile.checkError())
                throw new IOException();
            writeFile.flush();
            writeFile.close();
        }
        catch (IOException e) {
            System.out.println("Error encountered while writing file " + savedFile.getAbsolutePath());
            System.out.println("Error: " + e);
        }
    }
}
