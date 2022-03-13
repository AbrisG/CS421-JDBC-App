import java.sql.SQLException;

public class WriteNote implements State {

    private int appointmentID;
    private String prompt = "Please type your observation: ";

    public WriteNote(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    @Override
    public State executeInput(String input) {
        String sqlInsert = constructInsert(input);
        runSqlInsert(sqlInsert);
        return new PopState();
    }

    private String constructInsert(String input) {

        String sqlInsert = "INSERT INTO note (noteId, timestp, content, appointment) VALUES (" +
                "(SELECT MAX(noteId) + 1 FROM note), " +
                "(SELECT TIME (current timestamp) FROM sysibm.sysdummy1), " +
                Prompt.formatEscape(input) + ", " +
                appointmentID + ")";
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
