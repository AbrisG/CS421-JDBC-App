import java.util.ArrayList;
import java.util.Collection;

public class AppointmentMenu implements State {

    private ArrayList<String> mnames;
    private ArrayList<String> healthcareIDs;
    private ArrayList<Integer> coupleIDs;
    private ArrayList<Integer> pregNos;
    private ArrayList<Integer> appointmentIDs;
    private String result;
    private String priorResult;
    private String prompt = "Enter the appointment number that you would like to work on.\n" +
            "[E] to exit [D] to go back to another date :";

    AppointmentMenu(Collection<String> mnames, Collection<String> healthcareIDs, ArrayList<Integer> coupleIDs,
                    ArrayList<Integer> pregNos, ArrayList<Integer> appointmentIDs, String priorResult) {
        this.mnames = new ArrayList(mnames);
        this.healthcareIDs = new ArrayList(healthcareIDs);
        this.coupleIDs = new ArrayList(coupleIDs);
        this.pregNos = new ArrayList(pregNos);
        this.appointmentIDs = new ArrayList<>(appointmentIDs);
        this.priorResult = priorResult;
    }

    @Override
    public State executeInput(String input) {
        if (input.equals("D")) return new PopState();
        int parentIndex = Integer.parseInt(input) - 1;
        if (parentIndex >= mnames.size() || parentIndex < 0) return null;
        String mname = mnames.get(parentIndex);
        String healthcareID = healthcareIDs.get(parentIndex);
        int coupleID = coupleIDs.get(parentIndex);
        int pregNo = pregNos.get(parentIndex);
        int appointmentID = appointmentIDs.get(parentIndex);

        result = String.format("For %s %s: \n\n" +
                        "1. Review Notes\n" +
                        "2. Review Tests\n" +
                        "3. Add a note\n" +
                        "4. Prescribe a test\n" +
                        "5. Go back to appointments.\n\n" +
                        "Enter your choice: "
                , mname, healthcareID);

        return new PatientMenu(mname, healthcareID, coupleID, pregNo, appointmentID, result);
    }

    @Override
    public String getOutput() {
        return priorResult + prompt;
    }
}
