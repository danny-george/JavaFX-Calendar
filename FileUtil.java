import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileUtil {

    public static boolean saveEventToFile(CalendarEvent event, String name) throws IOException {
        try {
            Files.write(Paths.get(name), event.fileDisplay().getBytes(), StandardOpenOption.APPEND);
        } catch (NoSuchFileException nsfe) {
            Files.write(Paths.get(name), event.fileDisplay().getBytes(), StandardOpenOption.CREATE);
        }
        return true;
    }

    public static boolean saveEventsToFile(List<CalendarEvent> events, File file) {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
            return false;
        }
        for (int i = 0; i < events.size(); i++) {
            printWriter.print(events.get(i).fileDisplay());
        }
        printWriter.close();
        return true;
    }

    public static void sortEvents() {
        ArrayList<CalendarEvent> events = getEvents();
        File eventFile = new File("event-lookup.txt");
        Collections.sort(events);
        saveEventsToFile(events, eventFile);
    }

    public static ArrayList<CalendarEvent> getEvents() {
        File eventFile = new File("event-lookup.txt");
        Scanner fileScan = null;
        ArrayList<CalendarEvent> events = new ArrayList<>();

        try {
            fileScan = new Scanner(eventFile);
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                String[] x = line.split("\\|");
                
                int startMonth = Integer.parseInt(x[1]);
                int startDay = Integer.parseInt(x[2]);
                int startYear = Integer.parseInt(x[0]);
                int startHour = Integer.parseInt(x[3]);
                int startMin = Integer.parseInt(x[4]);

                int endMonth = Integer.parseInt(x[6]);
                int endDay = Integer.parseInt(x[7]);
                int endYear = Integer.parseInt(x[5]);
                int endHour = Integer.parseInt(x[8]);
                int endMin = Integer.parseInt(x[9]);
                CalendarEvent newEvent = new CalendarEvent(x[10], startMonth, startDay, startYear, startHour, startMin,
                                                            endMonth, endDay, endYear, endHour, endMin, x[11]);
                
                events.add(newEvent);
            }
            fileScan.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.toString());
        }

        return events;
    }

    public static void removeEvent(CalendarEvent toBeRemoved) throws IOException {
        ArrayList<CalendarEvent> eventsBeforeRemoval = getEvents();
        try {
            for (CalendarEvent calEv : eventsBeforeRemoval) {
                if (calEv.compareTo(toBeRemoved) != 0) {
                    saveEventToFile(calEv, "event-lookup-temp.txt");
                }
            }

            File old = new File("event-lookup.txt");
            old.delete();

            File newFile = new File("event-lookup.txt");
            File temp = new File("event-lookup-temp.txt");
            temp.renameTo(newFile);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.toString());
        }
    }
}