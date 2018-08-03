import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class Booker {

    private ChildrenBookingProperties childInfo;
    private BookingDates bookingDates;
    private WebClient client;

    public Booker(ChildrenBookingProperties childInfo, BookingDates bookingDates, WebClient client) {
        this.childInfo = childInfo;
        this.bookingDates = bookingDates;
        this.client = client;
    }

    public HtmlPage book(HtmlPage bookingFormPage) throws IOException {
        HtmlElement childCareDateLabel = HtmlElementFinder.findChildCareDateLabel(bookingFormPage);
        String abbreviatedDay = childCareDateLabel.asText().substring(0,2);

        if (bookingDates.dayIsBookable(abbreviatedDay)) {
            for (HtmlInput childBookCheckBox : HtmlElementFinder.findChildBookCheckBoxes(bookingFormPage)) {
                childBookCheckBox.setChecked(true);
            }

            HtmlButton registerBookingButton = HtmlElementFinder.findRegisterBookingButton(bookingFormPage);

            bookingFormPage = registerBookingButton.click();
            client.waitForBackgroundJavaScript(30000);
            return bookingFormPage;
        }

        return bookingFormPage;
    }
}
