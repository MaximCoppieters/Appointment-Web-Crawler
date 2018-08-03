import java.io.IOException;

public class Credentials extends BookingProperties {

    public Credentials() throws IOException {
       super("login");
    }

    @Override
    protected void getPropertiesFromUser() {
        String url = askCredential("Site URL");
        String username = askCredential("Username");
        String pw = askCredential("Password");

        bookProperties.setProperty("url", url);
        bookProperties.setProperty("username", username);
        bookProperties.setProperty("pass", pw);
    }

    private String askCredential(String credential) {
        System.out.printf("%s: %n", credential);
        return userInput.next();
    }

    public String getUrl() {
        return  bookProperties.getProperty("url");
    }

    public String getUsername() {
        return bookProperties.getProperty("username");
    }

    public String getPw() {
        return bookProperties.getProperty("pass");
    }
}
