public class Review implements State{

    String priorOutput;
    String prompt = "Enter [D] to go back to Patient Menu";

    public Review(String priorOutput) {
        this.priorOutput = priorOutput;
    }

    @Override
    public State executeInput(String input) {
        if (input.equals("D")) return new PopState();
        return null;
    }

    @Override
    public String getOutput() {
        return priorOutput + "\n" +  prompt;
    }
}
