import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CalculatorLang {
    public static void main(String[] args) {
        // The array defining the program. Contains each line of code.
        ArrayList<String> program = new ArrayList<String>();

        // We will init the scanner here and create an option to chose how you'd like to input the program
        System.out.println("CalcLang Interpreter:\n 1.) Load program from file\n 2.) Type out program manually\n 3.) Exit");
        Scanner scannerInput = new Scanner(System.in);
        int choice = scannerInput.nextInt();
        switch (choice) {
            // Load program from file
            case 1:
                // We will grab the file name of the program
                System.out.println("Enter the name of the program file: ");
                scannerInput.nextLine();
                String programName = scannerInput.nextLine();
                // We will then create a file object and read the program into the program array
                File file = new File(programName);
                // There is a try catch to check if the file can be read
                try {
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();
                        program.add(line);
                    }
                    sc.close();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                // We will then create an interpreter object and execute the program
                Interpreter interpreter = new Interpreter(program);
                interpreter.execProgram();
                break;
            // Type out program manually
            case 2:
                // Let the user input lines of code then once they are done, they can type "end" to finish
                System.out.println("Enter your program, when you are finished type \"end\": ");
                String line = scannerInput.nextLine();
                while (!line.equals("end")) {
                    program.add(line);
                    line = scannerInput.nextLine();
                }
                Interpreter userProgram = new Interpreter(program);
                userProgram.execProgram();
                break;
            // Exit
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
        // Close the scanner
        scannerInput.close();
    }

}
