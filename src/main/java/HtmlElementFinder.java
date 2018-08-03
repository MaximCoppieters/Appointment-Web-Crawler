import com.gargoylesoftware.htmlunit.html.*;

import java.util.List;

public class HtmlElementFinder {

    public static HtmlAnchor findLinkToBookingPage(HtmlPage pageBeingViewed) {
        return pageBeingViewed.getFirstByXPath("//li/span[contains(text(), 'Inschrijven')]/following-sibling::span/a");
    }

    public static List<HtmlAnchor> findBookFormLinks(HtmlPage pageBeingViewed) {
        String linkXpathExpression =
                "//a[contains(@class, 'fc-day-grid-event') and not(contains(@class, 'vaag5')) " +
                        "and not(contains(@title, 'beëindigd')) " +
                        "and not(contains(@title, 'Ingeschreven')) " +
                        "and not(contains(@class,'calendaralert'))]";
        return pageBeingViewed.getByXPath(linkXpathExpression);
    }

    public static HtmlButton findNextMonthButton(HtmlPage pageBeingViewed) {
        return pageBeingViewed.getFirstByXPath("//button[contains(@class, 'fc-next-button')]");
    }

    public static HtmlInput findInputFieldById(HtmlPage pageBeingViewed, String inputId) {
        return pageBeingViewed.getFirstByXPath("//input[@id='" + inputId + "']");
    }

    public static HtmlElement findChildCareDateLabel(HtmlPage pageBeingViewed) {
        return pageBeingViewed.getFirstByXPath("//span[contains(@id, 'DATUM')]/b");
    }

    public static Iterable<HtmlInput> findChildBookCheckBoxes(HtmlPage pageBeingViewed) {
        return pageBeingViewed.getByXPath("//fieldset[contains(@class, 'checkbox')]/input");
    }

    public static HtmlButton findRegisterBookingButton(HtmlPage pageBeingViewed) {
        return pageBeingViewed.getFirstByXPath("//button[contains(@class, 'save')]");
    }

    public static HtmlButton findBookingFormClosingButton(HtmlPage pageBeingViewed) {
        return pageBeingViewed.getFirstByXPath("//div[contains(@class, 'ui-dialog') and contains(@style,'display: block')]/div/button");
    }
}
