import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

public class Authenticator {

    private Credentials siteCredentials;
    private HtmlPage pageBeingViewed;
    private WebClient client;

    public Authenticator(WebClient client, Credentials siteCredentials) {
        this.client = client;
        this.siteCredentials = siteCredentials;
    }

    public void authenticate() throws IOException {
        pageBeingViewed = client.getPage(siteCredentials.getUrl());

        HtmlInput userField = HtmlElementFinder.findInputFieldById(pageBeingViewed, "P101_USERNAME");
        HtmlInput passwordField = HtmlElementFinder.findInputFieldById(pageBeingViewed, "P101_PASSWORD");

        HtmlForm authenticationForm = userField.getEnclosingForm();

        userField.setValueAttribute(siteCredentials.getUsername());
        passwordField.setValueAttribute(siteCredentials.getPw());

        pageBeingViewed = client.getPage(authenticationForm.getWebRequest(null));
    }

    public HtmlPage getPageBeingViewed() {
        return pageBeingViewed;
    }
}
