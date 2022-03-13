import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class PatientMenu implements State {

    private String mname;
    private String healthcareID;
    private int coupleID;
    private int pregNo;
    private int appointmentID;
    private String priorResult;

    public PatientMenu(String mname, String healthcareID, int coupleID, int pregNo, int appointmentID, String priorResult) {
        this.mname = mname;
        this.healthcareID = healthcareID;
        this.coupleID = coupleID;
        this.pregNo = pregNo;
        this.appointmentID = appointmentID;
        this.priorResult = priorResult;
    }


    @Override
    public State executeInput(String input) {
        int choiceNumber = Integer.parseInt(input);
        switch(choiceNumber) {
            case 1:
                String sqlQueryRN = constructReviewNotesQuery(coupleID, pregNo);
                return runReviewNotesQuery(sqlQueryRN);
            case 2:
                String sqlQueryRT = constructReviewTestsQuery(coupleID, pregNo);
                return runReviewTestsQuery(sqlQueryRT);
            case 3:
                return new WriteNote(appointmentID);
            case 4:
                return new PrescribeTest(appointmentID);
            case 5:
                return new PopState();
            default:
                System.out.println("Error: Command cannot be recognized, please enter an integer between 1 and 5.");
                return null;
        }
    }

    @Override
    public String getOutput() {
        System.out.println("PatientMenu");
        return priorResult;
    }

    private State runReviewNotesQuery(String sqlQueryRT) {
        try {
            ResultSet rs = Prompt.GLOBAL_STATEMENT.executeQuery(sqlQueryRT);
            String out = "";
            while (rs.next ())
            {
                String content = rs.getString ("content");
                Timestamp appTime = rs.getTimestamp("appTime") ;
                Timestamp timestp = rs.getTimestamp("timestp") ;


                String formattedAppTime = new SimpleDateFormat("yyyy-MM-dd").format(appTime);
                String formattedTimestp = new SimpleDateFormat("HH:mm:ss").format(timestp);
                String trimmedContent = content.substring(0, Math.min(content.length(), 50));

                out += String.format("%s\t%s\t%s\n", formattedAppTime, formattedTimestp, trimmedContent);
            }
            if (out.isEmpty()) return new Review("No notes for this pregnancy");

            return new Review(out);

        } catch (SQLException e) {
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);

            return null;
        }
    }

    private String constructReviewTestsQuery(int coupleID, int pregNo) {
        String sqlQuery =
                "SELECT * FROM " +
                        "test JOIN appointment " +
                        "ON appointment.appointmentId = test.appointment " +
                        "AND appointment.pcouple = " + coupleID +
                        " AND appointment.pnum = " + pregNo +
                        " ORDER BY test.prescriptionDate DESC";
        return sqlQuery;
    }

    private String constructReviewNotesQuery(int coupleID, int pregNo) {
        String sqlQuery =
                "SELECT * FROM " +
                        "note JOIN appointment " +
                        "ON appointment.appointmentId = note.appointment " +
                        "AND appointment.pcouple = " + coupleID +
                        " AND appointment.pnum = " + pregNo +
                        " ORDER BY appointment.appTime DESC, note.timestp DESC";
        return sqlQuery;
    }

    private Review runReviewTestsQuery(String query) {
        try {
            ResultSet rs = Prompt.GLOBAL_STATEMENT.executeQuery(query);
            String out = "";
            while (rs.next ())
            {
                String ttype = rs.getString ("ttype");
                String result = rs.getString("result");
                Timestamp prescriptionDate = rs.getTimestamp("prescriptionDate");

                String formattedPresDate = new SimpleDateFormat("yyyy-MM-dd").format(prescriptionDate);
                String resultFinal;
                if (result == null) resultFinal = "PENDING";
                else {
                    String trimmedContent = result.substring(0, Math.min(result.length(), 50));
                    resultFinal = trimmedContent;
                }

                out += String.format("%s\t[%s]\t%s\n", formattedPresDate, ttype, resultFinal);
            }
            if (out.isEmpty()) return new Review("No tests prescribed for this pregnancy");

            return new Review(out);

        } catch (SQLException e) {
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);

            return null;
        }
    }
}
