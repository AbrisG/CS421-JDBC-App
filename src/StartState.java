import java.nio.file.ProviderNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StartState implements State {

    private String prompt = "Please enter your practitioner id [E] to exit";
    private String priorQuery = null;

    @Override
    public State executeInput(String input) {
        int midwifeID = Integer.parseInt(input);
        String sqlQuery =
                "SELECT * FROM " +
                        "midwife WHERE practitionerId = " + midwifeID;

        try {
            ResultSet rs = Prompt.GLOBAL_STATEMENT.executeQuery(sqlQuery);
            int counter = 1;
            while ( rs.next ( ) )
            {
                counter++;
            }

            if (counter == 1) {
                System.out.println("Error: practitioner id is invalid, please try again");
                return null;
            }

            Prompt.GLOBAL_MIDWIFE = midwifeID;
            return new AppointmentDate();

        } catch (SQLException e) {

            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
            return null;
        }
    }

    @Override
    public String getOutput() {
        return prompt;
    }
}
