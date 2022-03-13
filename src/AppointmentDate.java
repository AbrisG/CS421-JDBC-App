import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;

public class AppointmentDate implements State {

    private String prompt = "Please enter the date for appointment list [E] to exit:";
    private String result;
    private String priorResult;

    @Override
    public State executeInput(String input) {
        if (input.equals("D")) return new PopState();
        String sqlQuery = constructQuery(input);
        return runQuery(sqlQuery);
    }

    @Override
    public String getOutput() {
        System.out.println("AppointmentDate");
        return prompt;
    }

    private String constructQuery(String inputDate) {
        String sqlQuery =
                "SELECT * FROM " +
                        "pregnancy JOIN couple " +
                        "ON pregnancy.coupleId = couple.coupleId " +
                        "AND (pregnancy.backupMidwife = " + Prompt.GLOBAL_MIDWIFE  +
                        " OR pregnancy.primaryMidwife = " + Prompt.GLOBAL_MIDWIFE +
                        ") JOIN mother " +
                        "ON couple.motherId = mother.id " +
                        "JOIN parent " +
                        "ON parent.id = mother.id " +
                        "JOIN appointment " +
                        "ON appointment.pcouple = pregnancy.coupleId " +
                        "AND appointment.pnum = pregnancy.pregNo " +
                        "WHERE date(appTime) = \'" + inputDate + "\'" +
                        " ORDER BY appTime";
        return sqlQuery;
    }

    private State runQuery(String query) {
        try {
            ResultSet rs = Prompt.GLOBAL_STATEMENT.executeQuery(query);
            int counter = 1;
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> healthcareIDs = new ArrayList<>();
            ArrayList<Integer> coupleIDs = new ArrayList<>();
            ArrayList<Integer> pregNos = new ArrayList<>();
            ArrayList<Integer> appointmentIDs = new ArrayList<>();

            String out = "";
            while (rs.next ())
            {
                String mname = rs.getString("pname");
                int primaryMidwife = rs.getInt ("primaryMidwife");
                int coupleID = rs.getInt ("coupleID");
                int pregNo = rs.getInt ("pregNo");
                int appointmentId = rs.getInt ("appointmentId");
                String healthcareid = rs.getString ("healthcareid");
                Timestamp appTime = rs.getTimestamp("appTime") ;

                String p;
                if (primaryMidwife == Prompt.GLOBAL_MIDWIFE) p = "P";
                else p = "B";

                String formattedDate = new SimpleDateFormat("HH:mm").format(appTime);

                out += String.format("%d:\t%s\t%s\t%s\t%s\n", counter, formattedDate, p, mname, healthcareid);
                counter++;

                names.add(mname);
                healthcareIDs.add(healthcareid);
                coupleIDs.add(coupleID);
                pregNos.add(pregNo);
                appointmentIDs.add(appointmentId);
            }
            result = out;

            if (counter == 1) return null;

            return new AppointmentMenu(names, healthcareIDs, coupleIDs, pregNos, appointmentIDs, result);

        } catch (SQLException e) {
            int sqlCode = e.getErrorCode();
            String sqlState = e.getSQLState();

            System.out.println("Code: " + sqlCode + "  sqlState: " + sqlState);
            System.out.println(e);
            return null;
        }
    }

}
