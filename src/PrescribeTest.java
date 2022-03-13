import java.sql.SQLException;

public class PrescribeTest implements State {

    private int appointmentID;
    private String prompt = "Please enter the type of test: ";

    public PrescribeTest(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    @Override
    public State executeInput(String input) {
        String sqlInsert = constructInsert(input);
        runSqlInsert(sqlInsert);
        return new PopState();
    }

    private String constructInsert(String input) {

        String sqlInsert = "INSERT INTO test (testId, appointment, ttype, prescriptionDate)" +
                " VALUES (" +
                "(SELECT MAX(testId) + 1 FROM test), " +
                appointmentID + ", " +
                Prompt.formatEscape(input) + ", " +
                "(SELECT DATE (current timestamp) FROM sysibm.sysdummy1))";
        return sqlInsert;
    }

    private void runSqlInsert(String sqlInsert) {
        try
        {
            Prompt.GLOBAL_STATEMENT.executeUpdate(sqlInsert);
        }
        catch (SQLException e)
        {
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
        }
    }

    @Override
    public String getOutput() {
        return prompt;
    }
}
