import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ChildrenBookingProperties extends BookingProperties {

    public ChildrenBookingProperties() throws IOException {
        super("child");
    }

    @Override
    protected void getPropertiesFromUser() {
        Set<String> names = new HashSet<>();
        Set<String> birthYears = new HashSet<>();

        addChildrenFromUserInput(names, birthYears);

        bookProperties.setProperty("names", splitIntoCsv(names));
        bookProperties.setProperty("birthyears", splitIntoCsv(birthYears));
    }

    private String splitIntoCsv(Iterable<String> toSplit) {
        return String.join(",", toSplit);
    }

    private void addChildrenFromUserInput(Set<String> names, Set<String> birthYears) {
        String registerNewChildQuestion = "Do you want to register a child? (y/n)";

        String registerAnswer = askForInputWithQuestion(registerNewChildQuestion);
        while (registerAnswer.contains("y")) {
            String childName = askForInputWithQuestion("Name of child: ");
            String birthYear = askForInputWithQuestion(String.format("Birthyear of %s%n: "));

            names.add(childName);
            birthYears.add(birthYear);

            registerAnswer = askForInputWithQuestion(registerNewChildQuestion);
        }
    }

    private String askForInputWithQuestion(String question) {
        System.out.println(question);
        return userInput.next();
    }
}
