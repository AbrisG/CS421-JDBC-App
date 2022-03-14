import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Prompt {

    private static Prompt INSTANCE;

    private Prompt() {}

    private static Stack<State> actionStack = new Stack();

    public static Statement GLOBAL_STATEMENT;
    public static Connection GLOBAL_CON;

    public static int GLOBAL_MIDWIFE;

    public static void start() {
        Scanner command = new Scanner(System.in);
        actionStack.add(new StartState());

        while(true){
            System.out.println(actionStack.peek().getOutput());
            String input = command.nextLine();

            //Check if the user wants to exit
            if (input.equals("E")) {
                System.out.println("E: Exiting application");
                break;
            }

            //Run the input in the context of the current state
            State newState = actionStack.peek().executeInput(input);

            //Check if you need to go back one state
            if (newState instanceof PopState) {
                actionStack.pop();
                continue;
            }

            //If the new state is null, then the query was incorrect; do nothing
            if (newState == null) continue;

            //Add new state to stack
            actionStack.add(newState);

        }
        command.close();
    }


    public static Prompt getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Prompt();
        }
        return INSTANCE;
    }

    public static String formatEscape(String in) {
        return "\'" + in +"\'";
    }

    public static String formatEscape(int in) {
        return "\'" + in +"\'";
    }

}
