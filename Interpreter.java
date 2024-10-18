import java.util.ArrayList;

public class Interpreter {
    // Constant for floating point rounding 
    final double EPLSION = 0.000001d;
    // Opcode is the operation to be done
    private char opcode;
    // Code for the program
    private ArrayList<String> program;
    // Single ArrayList to hold the "history." Controls the flow of the program and stores the results of the operations
    private ArrayList<Double> total;
    // Result of the operation
    private double result;
    
    // Condition for the IF statement
    private boolean condition;

    // Loop variables
    private boolean loop = false;
    private int loopIndex = 0;
    private int lineNumber = 0;
    int LoopStart;
    int LoopEnd;
    int LoopStep;

    // Constructor for when program is given.
    public Interpreter(ArrayList<String> program ) {
        // Loads the program into the interpreter
        this.program = program;
        this.total = new ArrayList<Double>();
        this.condition = false;
        this.opcode = ' ';
    }
    //Constructor for when program is not given.
    public Interpreter() {
        this.total = new ArrayList<Double>();
        this.condition = false;
        this.opcode = ' ';
    }

    // Load a program into the program variable
    public void loadProgram(ArrayList<String> program) {
        this.program = program;
    }
    // Execute the program
    public void execProgram(){
        // If there is no program, print error
        if(program == null){
            System.out.print("Program not loaded");
            return;
        }
        exec();
    }
    private void exec(){
        // For each line in the program
        for(int statement = 0; statement < program.size(); statement++){
            // If it is a comment or blank line continue
            if(program.get(statement).contains("//")) continue;
            if(program.get(statement).equals("")) continue;
            // Set the line number
            lineNumber = statement + 1;
            // Split the line into an array
            String[] line = program.get(statement).trim().split(" ");
            
            // Flow control for loops
            if(loop){
                int statementStart = -1;
                int statementEnd = -1;
                // Find the start and end of the loop
                while(true){
                    if(program.get(statement).contains("{")) {
                        statementStart = statement+1;
                    }
                    if(program.get(statement).contains("}")){
                        statementEnd = statement-1;
                        break;
                    }
                    statement++;
                }
                // Error handling
                if(statementStart == -1 || statementEnd == -1){
                    System.out.print("\nError: Line " + lineNumber + " Invalid loop statement\n");
                    return;
                }
                // Specified loop
                for(loopIndex = LoopStart; (LoopStep > 0 ? loopIndex <= LoopEnd : loopIndex >= LoopEnd); loopIndex+=LoopStep){
                    // Loop through the lines within the loop
                    for(int i = statementStart; i <= statementEnd; i++){
                        if(program.get(i).contains("//")) continue;
                        if(program.get(i).equals("")) continue;
                        lineNumber = i+1;
                        String[] lineLoop = program.get(i).trim().split(" ");
                        evalLine(lineLoop);
                    }
                    statement++;
                }
                statement = statementEnd+1;
                loop = false;
                continue;
            }
            // Evaluate the line
            evalLine(line);
        }
    }

    private void evalLine(String[] line){
        for(int i=0; i<line.length; i++){
            line[i] = line[i].trim();
            // Check for keywords
            if(line[i].equals("IF")){
                if(condition){
                    continue;
                }
                else{
                    break;
                }
            }
            if(line[i].equals("PUSH")){
                opcode = ' ';
            }
            if(line[i].equals("PRINT")){
                PRINT();
                continue;
            }
            if(line[i].equals("PRINTC")){
                PRINTC();
                continue;
            }
            if(line[i].equals("PRINTLN")){
                System.out.println();
                continue;
            }
            if(line[i].equals("CLEAR")){
                CLEAR();
                continue;
            }
            if(line[i].equals("MEMCLEAR")){
                MEMCLEAR();
                continue;
            }
            if(line[i].equals("INDEX")){
                total.add(Double.valueOf(loopIndex));
                continue;
            }
            if(line[i].equals("FOR")){
                LoopStart = Integer.parseInt(line[i+1]);
                LoopEnd = Integer.parseInt(line[i+2]);
                LoopStep = Integer.parseInt(line[i+3]);
                if(loop)
                {
                    System.out.print("\nError: Line " + lineNumber + " Nested loops are not allowed\n");
                    return;
                }
                loop = true;
                break;
            }

            // If it is not a keyword, we check if it is a number, or if it is an operation. If it is neither we print a warning and continue
            if(isDouble(line[i])){
                total.add(Double.valueOf(line[i]));
            }
            else{
                getOpcode(line[i]);
                continue;
            }
            // If there are enough values to perform an operation we do so
            int arraySize = total.size();
            if(arraySize > 1){
                if(opcode == '+'){
                    result = total.get(arraySize-2) + total.get(arraySize-1);
                }
                else if(opcode == '-'){
                    result = total.get(arraySize-2) - total.get(arraySize-1);
                }
                else if(opcode == '*'){
                    result = total.get(arraySize-2) * total.get(arraySize-1);
                }
                else if(opcode == '/'){
                    result = total.get(arraySize-2) / total.get(arraySize-1);
                }
                else if(opcode == '%'){
                    result = total.get(arraySize-2) % total.get(arraySize-1);
                }
                else if(opcode == '='){
                    condition = Math.abs(total.get(arraySize-2) - total.get(arraySize-1)) < EPLSION;
                    opcode = ' ';
                }
                else {
                    continue;
                }
                total.add(result);
            }
        }
    }
    private void PRINT(){
        // Control for printing integers or doubles
        if(total.get(total.size()-1) % 1 == 0){
            System.out.print(total.get(total.size()-1).intValue());
        }
        else{
            System.out.print(total.get(total.size()-1).doubleValue());
        }
    }
    private void PRINTC(){
        int total_i = (int) total.get(total.size()-1).doubleValue();
        System.out.print(Character.toString((char) total_i));
    }
    private void CLEAR(){
        total.clear();
    }
    private void MEMCLEAR(){
        CLEAR();
        opcode = ' ';
        result = 0;
        condition = false;
    }
    private boolean getOpcode(String line){
        if(line.equals("+")){
            opcode = '+';
            return true;
        }
        else if(line.equals("-")){
            opcode = '-';
            return true;
        }
        else if(line.equals("*")){
            opcode = '*';
            return true;
        }
        else if(line.equals("/")){
            opcode = '/';
            return true;
        }
        else if(line.equals("%")){
            opcode = '%';
            return true;
        }
        else if(line.equals("=")){
            opcode = '=';
            return true;
        }
        else if(line.equals("!=")){
            opcode = '!';
            return true;
        }
        else{
            //System.out.println("\nText="+line.toString());
            System.out.print("\nWarning: Line " + lineNumber +  " Invalid opperation - Results may not be logically valid\n");
            return false;
        }
    }

    // Utility function to check if a string is a double
    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}