import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PrescribeTest implements State {

    private int appointmentID;
    private String prompt = "Please enter the type of test: ";

    public PrescribeTest(int appointmentID) {

        this.appointmentID = appointmentID;

        if (preparedStatement == null) preparedStatement = getPreparedStatement();
    }

    @Override
    public State executeInput(String input) {
        try {
            preparedStatement.setInt(1, appointmentID);
            preparedStatement.setString(2, input);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
        }
        return new PopState();
    }

    private static PreparedStatement preparedStatement;
    private static PreparedStatement getPreparedStatement() {
        String statementString = "INSERT INTO test (testId, appointment, ttype, prescriptionDate) VALUES (" +
                "(SELECT MAX(testId) + 1 FROM test),  " +
                "?, " +
                "?, " +
                "(SELECT DATE (current timestamp) FROM sysibm.sysdummy1))";
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

    @Override
    public String getOutput() {
        return prompt;
    }
}
