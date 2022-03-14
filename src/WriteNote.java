import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WriteNote implements State {

    private int appointmentID;
    private String prompt = "Please type your observation: ";

    public WriteNote(int appointmentID) {

        this.appointmentID = appointmentID;

        if (preparedStatement == null) preparedStatement = getPreparedStatement();
    }

    @Override
    public State executeInput(String input) {
        try {
            preparedStatement.setString(1, input);
            preparedStatement.setInt(2, appointmentID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
        }
        return new PopState();
    }

    @Override
    public String getOutput() {
        return prompt;
    }

    private static PreparedStatement preparedStatement;
    private static PreparedStatement getPreparedStatement() {
        String statementString = "INSERT INTO note (noteId, timestp, content, appointment) VALUES (" +
                "(SELECT MAX(noteId) + 1 FROM note), " +
                "(SELECT TIME (current timestamp) FROM sysibm.sysdummy1), " +
                "?, " +
                "?)";
        PreparedStatement statement = null;
        try {
            statement = Prompt.GLOBAL_CON.prepareStatement(statementString);
        } catch (SQLException e) {
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
        }
        return statement;
    }
}
