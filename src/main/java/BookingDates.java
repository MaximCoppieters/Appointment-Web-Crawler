import java.io.IOException;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.*;

public class BookingDates extends BookingProperties {

    private static final String[] daynamesAbbreviated;
    private List<String> bookableDays;
    private static final Locale DUTCH_DAY_NOTATION = Locale.forLanguageTag("nl-BE");
    private static final int DAYS_IN_WEEK = 7;

    static {
        daynamesAbbreviated = new String[DAYS_IN_WEEK];
        for(DayOfWeek dayOfWeek : DayOfWeek.values()) {
            daynamesAbbreviated[dayOfWeek.getValue()-1] = getAbbreviatedDayName(dayOfWeek);
        }
    }

    private static String getAbbreviatedDayName(DayOfWeek dayOfWeek) {
        return dayOfWeek.getDisplayName(TextStyle.SHORT, DUTCH_DAY_NOTATION).toLowerCase();
    }

    public BookingDates() throws IOException {
        super("date");

        setBookableDays();
    }

    private void setBookableDays() {
        bookableDays = new ArrayList<>();

        for (int i=0; i < DAYS_IN_WEEK; i++) {
            if (bookProperties.getProperty(daynamesAbbreviated[i]).equals("true")) {
                bookableDays.add(daynamesAbbreviated[i]);
            }
        }
    }

    @Override
    protected void getPropertiesFromUser() {
        String startTime = askBookTime("starting time");
        String endTime = askBookTime("ending time");

        bookProperties.setProperty("startTime", startTime);
        bookProperties.setProperty("endTime", endTime);

        System.out.println("By default, this program will only book child care on days of the week.");
        for (int day=0; day < daynamesAbbreviated.length; day++) {
            boolean shouldBookOnDay = day < 5;
            bookProperties.setProperty(daynamesAbbreviated[day], shouldBookOnDay ? "true" : "false");
        }
    }

    private String askBookTime(String bookTimeInstant) {
        System.out.printf("What would you like the %s to be? (hh:mm)%n", bookTimeInstant);
        return userInput.next();
    }


    public boolean dayIsBookable(String dayAbbreviation) {
        return bookableDays.stream()
                .anyMatch(bookableDay -> bookableDay.equals(dayAbbreviation.toLowerCase()));
    }

    private boolean isBookable(String abbrDays) {
        return bookProperties.getProperty(abbrDays).equals("true");
    }
}
