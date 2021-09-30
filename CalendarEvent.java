import java.util.Calendar;
import java.util.GregorianCalendar;
import java.time.LocalTime;

public class CalendarEvent implements Comparable<CalendarEvent> {
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMinute;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int endHour;
    private int endMinute;
    private String title;
    private String description;
    
    public CalendarEvent(String title, int startMonth, int startDay, int startYear, 
                            int startHour, int startMinute, int endMonth, int endDay,
                            int endYear, int endHour, int endMinute, String description) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.title = title;
        this.description = description;
    }

    public int compareTo(CalendarEvent other) {
        Calendar self = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMinute);
        Calendar otherC = new GregorianCalendar(other.getYear(), other.getMonth(), other.getDay(), 
                                                    other.getHour(), other.getMinute());
        return self.compareTo(otherC);
    }

    public String toString() {
        String sH = "" + startHour;
        sH = sH.length() == 1 ? "0" + sH : sH;
        String sM = "" + startMinute;
        sM = sM.length() == 1 ? "0" + sM : sM;
        String eH = "" + endHour;
        eH = eH.length() == 1 ? "0" + eH : eH;
        String eM = "" + endMinute;
        eM = eM.length() == 1 ? "0" + eM : eM;

        String str = sH + ":" + sM + " - " + eH + ":" + eM;
        str += "\n" + title;
        return str;
    }

    public String fileDisplay() {
        String str = startYear + "|" + startMonth + "|" + startDay + "|" + startHour;
        str += "|" + startMinute + "|" + endYear + "|" + endMonth + "|" + endDay + "|";
        str += endHour + "|" + endMinute + "|" + title + "|" + description + "\n";
        return str;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return startYear;
    }
    public void setYear(int startYear) {
        this.startYear = startYear;
    }
    public int getEndYear() {
        return endYear;
    }
    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getMonth() {
        return startMonth;
    }
    public void setMonth(int startMonth) {
        this.startMonth = startMonth;
    }
    public int getEndMonth() {
        return endMonth;
    }
    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }
    
    public int getDay() {
        return startDay;
    }
    public void setDay(int startDay) {
        this.startDay = startDay;
    }
    public int getEndDay() {
        return endDay;
    }
    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getHour() {
        return startHour;
    }
    public void setHour(int startHour) {
        this.startHour = startHour;
    }
    public int getEndHour() {
        return endHour;
    }
    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getMinute() {
        return startMinute;
    }
    public void setMinute(int startMinute) {
        this.startMinute = startMinute;
    }
    public int getEndMinute() {
        return endMinute;
    }
    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }
}