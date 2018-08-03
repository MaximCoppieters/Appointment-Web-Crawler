import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static ChildrenBookingProperties infoAboutChildren;
    private static Credentials loginCredentials;
    private static BookingDates bookingDateInfo;
    private static HtmlPage activePage;
    private static WebClient webClient;

    static {
        readOrWriteLocalBookingProperties();
    }

    private static void readOrWriteLocalBookingProperties() {
        try {
            loginCredentials = new Credentials();
            infoAboutChildren = new ChildrenBookingProperties();
            bookingDateInfo = new BookingDates();
        } catch (IOException ioe) {
            System.out.println("Something went wrong creating new booking properties or reading old ones");
        }
    }

    public static void main(String[] args) {
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);

        initializeClient();

        Authenticator loginAuthenticator = new Authenticator(webClient, loginCredentials);

        authenticate(loginAuthenticator);
        activePage = loginAuthenticator.getPageBeingViewed();

        Navigator siteNavigator = new Navigator(webClient, activePage);
        HtmlAnchor bookingPageLink = HtmlElementFinder.findLinkToBookingPage(activePage);

        navigateToBookingPage(siteNavigator, bookingPageLink);
        siteNavigator.runJavascript();

        activePage = siteNavigator.getPageBeingViewed();

        HtmlButton nextMonthButton = HtmlElementFinder.findNextMonthButton(activePage);
        navigateToNextMonth(siteNavigator, nextMonthButton);
        navigateToNextMonth(siteNavigator, nextMonthButton);

        List<HtmlAnchor> bookingFormLinks = HtmlElementFinder.findBookFormLinks(activePage);

        Booker bookingFormFiller =
                new Booker(infoAboutChildren, bookingDateInfo, webClient);

        for (HtmlAnchor bookingFormLink : bookingFormLinks) {
            try {
                siteNavigator.executeJS(bookingFormLink);
                activePage = siteNavigator.getPageBeingViewed();
                activePage = bookingFormFiller.book(activePage);
                dump(activePage);
                HtmlButton formClosingButton = HtmlElementFinder.findBookingFormClosingButton(activePage);
                activePage = formClosingButton.click();
                webClient.waitForBackgroundJavaScript(10000);
            } catch (IOException|NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initializeClient() {
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }

    private static void authenticate(Authenticator loginAuthenticator) {
        try {
            loginAuthenticator.authenticate();
        } catch (IOException e) {
            System.out.println("Something went wrong trying to authenticate");
        }
    }

    private static void navigateToBookingPage(Navigator siteNavigator, HtmlAnchor bookingPageLink) {
        try{
            siteNavigator.navigateTo(bookingPageLink);
        } catch (IOException e) {
            System.out.println("Could not find booking page link");
        }
    }

    private static void navigateToNextMonth(Navigator siteNavigator, HtmlButton nextMonthButton) {
        try {
            siteNavigator.navigateTo(nextMonthButton);
        } catch (IOException ioe) {
            System.out.println("Couldn't click the button to navigate to the next month");
        }
    }

    private static void dump(HtmlPage page) {
        try(BufferedWriter os = Files.newBufferedWriter(Paths.get("dump.html"))) {
            os.write(page.asXml());
        } catch (IOException ioe) {
            System.out.println("Couldn't dump page");
        }
    }
}
