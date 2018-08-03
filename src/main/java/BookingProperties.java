import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public abstract class BookingProperties {

    protected Properties bookProperties;
    protected static Scanner userInput;
    private String propertyFileName;

    public BookingProperties(String propertyName) throws IOException {
        this.propertyFileName = propertyName + ".properties";
        bookProperties = new Properties();

        if (propertiesFileExists()) {
            loadPropertiesFromFile();
        } else {
            userInput = new Scanner(System.in);
            System.out.printf("The property file %s does not yet exist, please fill in the following form to create it%n", propertyFileName);

            getPropertiesFromUser();
            createPropertiesFile();

            userInput.close();
        }
    }

    protected abstract void getPropertiesFromUser();

    private boolean propertiesFileExists() {
        Path defaultPropertiesPath = Paths.get(propertyFileName);
        return Files.exists(defaultPropertiesPath);
    }

    private void loadPropertiesFromFile() throws IOException {
        InputStream credentialsStream = Files.newInputStream(Paths.get(propertyFileName));

        bookProperties.load(credentialsStream);
    }

    private void createPropertiesFile() throws IOException {
        FileOutputStream output = new FileOutputStream(propertyFileName);

        bookProperties.store(output, null);
    }
}
