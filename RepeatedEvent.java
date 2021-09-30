public class RepeatedEvent extends CalendarEvent {
    private int repeatNumber;
    private String repeatUnit;

    public RepeatedEvent(String title, String description, int firstStartMonth, int firstStartDay,
                            int firstStartYear, int firstStartHour, int firstStartMinute,
                            int firstEndMonth, int firstEndDay, int firstEndYear,
                            int firstEndHour, int firstEndMinute, int repeatNumber,
                            String repeatUnit) {

        super(title, firstStartMonth, firstStartDay, firstStartYear, firstStartHour, firstStartMinute, firstEndMonth,
                firstEndDay, firstEndYear, firstEndHour, firstEndMinute, description);
        this.repeatNumber = repeatNumber;
        this.repeatUnit = repeatUnit;
    }

    public int compareTo(RepeatedEvent other) {
        
    }
}
