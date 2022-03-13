import java.sql.Statement;

public interface State {
    public State executeInput(String input);

    public String getOutput();
}
