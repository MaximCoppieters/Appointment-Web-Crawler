import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;

public class Navigator {

    private WebClient client;
    private HtmlPage pageBeingViewed;

    public Navigator(WebClient client, HtmlPage pageBeingViewed) {
        this.client = client;
        this.pageBeingViewed = pageBeingViewed;
    }

    public void navigateTo(HtmlAnchor linkToGoTo) throws IOException {
        if (linkToGoTo.getHrefAttribute() != null) {
            pageBeingViewed = (HtmlPage) linkToGoTo.openLinkInNewWindow();
        } else {
            linkToGoTo.click();
            runJavascript();
        }
    }

    public void executeJS(HtmlAnchor link) {
        pageBeingViewed = (HtmlPage) pageBeingViewed.executeJavaScript(link.getOnClickAttribute()).getNewPage();

    }

    public void navigateTo(HtmlButton buttonToClick) throws IOException {
        buttonToClick.click();
        runJavascript();
    }

    public void openBookingWindow(HtmlAnchor bookingLink) throws MalformedURLException {
        pageBeingViewed = (HtmlPage) bookingLink.openLinkInNewWindow();
    }

    public void runJavascript() {
        client.waitForBackgroundJavaScript(30000);
    }

    public WebClient getClient() {
        return client;
    }

    public HtmlPage getPageBeingViewed() {
        return pageBeingViewed;
    }
}
