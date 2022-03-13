import java.sql.Statement;

public class PopState implements State {

    @Override
    public State executeInput(String input) {
        return null;
    }

    @Override
    public String getOutput() {
        return "PopState: This should never run";
    }
}
