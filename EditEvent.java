public class EditEvent implements Comparable<EditEvent> {

    private CalendarEvent eventItself;

    public EditEvent(CalendarEvent eventItself) {
        this.eventItself = eventItself;
    }

    public int compareTo(EditEvent other) {
        CalendarEvent otherEvent = other.getEventItself();
        return eventItself.compareTo(otherEvent);
    }

    public String toString() {
        String str = getMonthName(eventItself.getMonth()) + " " + eventItself.getDay() + ", ";
        str += eventItself.getYear();
        if ((eventItself.getDay() != eventItself.getEndDay())
            || (eventItself.getMonth() != eventItself.getEndMonth())
            || (eventItself.getYear() != eventItself.getEndYear())) {
                str += " - " + getMonthName(eventItself.getEndMonth()) + " " + eventItself.getEndDay();
                str += ", " + eventItself.getEndYear();
        }
        str += "\n" + eventItself.toString();
        return str;
    }

    public CalendarEvent getEventItself() {
        return eventItself;
    }

    public void setEventItself(CalendarEvent eventItself) {
        this.eventItself = eventItself;
    }

    private String getMonthName(int n) {
        String[] months = {"January", "February", "March", "April", "May",
                            "June", "July", "August", "September", "October",
                            "November", "December"};
        return months[n];
    }
}