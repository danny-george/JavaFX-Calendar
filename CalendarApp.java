import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextField;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class CalendarApp extends Application {

    public int ACTUAL_MONTH = Calendar.getInstance().get(Calendar.MONTH);
    public int ACTUAL_DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    public int ACTUAL_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private Calendar currentMonthCal = new GregorianCalendar();
    private int currentYear = ACTUAL_YEAR;
    private int currentMonth = ACTUAL_MONTH;
    private Font textFont = new Font(24);
    private BorderStroke gpStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.THIN);
    private Border gridPaneBorder = new Border(gpStroke);
    private GridPane calBody;
    private VBox root;
    private HBox header;
    private Label month;
    private Label yearShow;
    private String eventsMasterList = "event-lookup.txt";
    private Stage eventStage;
    private Stage editStage;
    private ListView<EditEvent> editViewer;

    public void start(Stage stage) {

        currentMonthCal.set(Calendar.DAY_OF_MONTH, 1);
        calBody = makeCalendarPane(currentMonthCal);

        month = new Label(getMonthName(currentMonthCal.get(Calendar.MONTH)));
        month.setFont(textFont);

        yearShow = new Label("" + currentYear);
        yearShow.setFont(textFont);

        Button next = new Button("Next");
        next.setFont(textFont);
        next.setOnAction(event -> {
            prevNext(1);
        });
        
        Button prev = new Button("Previous");
        prev.setFont(textFont);
        prev.setOnAction(event -> {
            prevNext(-1);
        });

        Button thisMonth = new Button("This Month");
        thisMonth.setFont(textFont);
        thisMonth.setOnAction(event -> {
            prevNext(0);
        });

        Button addEvent = new Button("Add Event");
        addEvent.setFont(textFont);
        addEvent.setOnAction(event -> {
            addAnEvent(getMonthName(currentMonth), getMonthName(currentMonth),
                        ACTUAL_DAY - 1, ACTUAL_DAY - 1, 10, 10, "00", "01", "00", "00",
                        "", "");
        });

        Button editEvents = new Button("Edit / Remove an Event");
        editEvents.setFont(textFont);
        editEvents.setOnAction(event -> {
            try {
                if (FileUtil.getEvents().size() == 0) {
                    throw new MalformedURLException("No events to edit");
                }
                GridPane editRoot = new GridPane();
                editRoot.setVgap(15);
                editRoot.setHgap(5);
                editRoot.setAlignment(Pos.CENTER);

                Label selectL = new Label("Select an event to edit:");
                selectL.setFont(textFont);
                editViewer = new ListView<>();
                Button editB = new Button("Edit");
                editB.setFont(textFont);
                Button removeB = new Button("Remove");
                removeB.setFont(textFont);

                ArrayList<CalendarEvent> eventsOnFile = FileUtil.getEvents();
                for (CalendarEvent ce : eventsOnFile) {
                    if ((ce.getMonth() == currentMonth) && (ce.getYear() == currentYear)) {
                        EditEvent ev = new EditEvent(ce);
                        editViewer.getItems().add(ev);
                    }
                }

                editB.setOnAction(event2 -> {
                    editAnEvent("edit", eventsOnFile);
                });

                removeB.setOnAction(event3 -> {
                    editAnEvent("remove", eventsOnFile);
                });

                HBox editButtons = new HBox();
                editButtons.getChildren().addAll(editB, removeB);

                editRoot.add(selectL, 0, 0);
                editRoot.add(editViewer, 0, 1);
                editRoot.add(editButtons, 0, 2);

                Scene editScene = new Scene(editRoot, 750, 650);
                editStage = new Stage();
                editStage.setTitle("Event Editor");
                editStage.setScene(editScene);
                editStage.show();
            } catch (MalformedURLException murle) {
                Alert a = new Alert(Alert.AlertType.ERROR, "There are no events to edit.");
                a.setTitle("Error");
                a.setHeaderText("No Events to Edit");
                a.showAndWait();
            }
        });

        header = new HBox(15);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(month, yearShow, addEvent, editEvents);

        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(prev, next);

        VBox buttonHolder = new VBox(10);
        buttonHolder.setAlignment(Pos.CENTER);
        buttonHolder.getChildren().addAll(buttons, thisMonth);

        root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(header, calBody, buttonHolder);

        Scene scene = new Scene(root, 1000, 1000);
        stage.setTitle("Calendar.v1");
        stage.setScene(scene);
        stage.show();
    }

    private void addAnEvent(String selectSM, String selectEM, int selectSD, int selectED,
                            int selectSY, int selectEY, String selectSH, String selectEH,
                            String selectSMin, String selectEMin, String selectTitle,
                            String selectDescription) {
        GridPane eventRoot = new GridPane();
        eventRoot.setVgap(15);
        eventRoot.setHgap(5);
        eventRoot.setAlignment(Pos.CENTER);

        ComboBox<String> startMonthI = new ComboBox<>();
        ComboBox<String> endMonthI = new ComboBox<>();
        for (int i = 0; i < 12; i++) {
            startMonthI.getItems().add(getMonthName(i));
            endMonthI.getItems().add(getMonthName(i));
        }
        startMonthI.getSelectionModel().select(selectSM);
        endMonthI.getSelectionModel().select(selectEM);

        ComboBox<Integer> startDayI = new ComboBox<>();
        ComboBox<Integer> endDayI = new ComboBox<>();
        for (int i = 1; i < 32; i++) {
            startDayI.getItems().add(i);
            endDayI.getItems().add(i);
        }
        startDayI.getSelectionModel().select(selectSD);
        endDayI.getSelectionModel().select(selectED);

        ComboBox<Integer> startYearI = new ComboBox<>();
        ComboBox<Integer> endYearI = new ComboBox<>();
        for (int i = currentYear - 10; i < currentYear + 11; i++) {
            startYearI.getItems().add(i);
            endYearI.getItems().add(i);
        }
        startYearI.getSelectionModel().select(selectSY);
        endYearI.getSelectionModel().select(selectEY);

        ComboBox<String> startHourI = new ComboBox<>();
        ComboBox<String> endHourI = new ComboBox<>();
        setMilitaryComboBoxes(startHourI, endHourI, 24);
        startHourI.getSelectionModel().select(selectSH);
        endHourI.getSelectionModel().select(selectEH);

        ComboBox<String> startMinI = new ComboBox<>();
        ComboBox<String> endMinI = new ComboBox<>();
        setMilitaryComboBoxes(startMinI, endMinI, 60);
        startMinI.getSelectionModel().select(selectSMin);
        endMinI.getSelectionModel().select(selectEMin);

        Label headL = new Label("Add an Event");
        headL.setUnderline(true);
        Label titleL = new Label("Title: ");
        TextField titleI = new TextField();
        titleI.setText(selectTitle);
        Label startL = new Label("Start time: ");
        Label endL = new Label("End time: ");
        Label colon1L = new Label(":");
        Label colon2L = new Label(":");
        Label comma1L = new Label(",");
        Label comma2L = new Label(",");
        Label atSign1L = new Label("@");
        Label atSign2L = new Label("@");
        Label descriptionL = new Label("Description: ");
        TextField descriptionI = new TextField();
        descriptionI.setText(selectDescription);

        Node[] starts = {startL, startMonthI, startDayI, comma1L, startYearI,
            atSign1L, startHourI, colon1L, startMinI};
        Node[] ends = {endL, endMonthI, endDayI, comma2L, endYearI,
            atSign2L, endHourI, colon2L, endMinI};
        eventRoot.add(headL, 0, 0);
        eventRoot.add(titleL, 0, 1);
        eventRoot.add(titleI, 1, 1);
        for (int i = 0; i < 9; i++) {
            eventRoot.add(starts[i], i, 2);
            eventRoot.add(ends[i], i, 3);
        }
        eventRoot.add(descriptionL, 0, 4);
        eventRoot.add(descriptionI, 1, 4);

        Labeled[] displayItems = {headL, titleL, startL, endL, descriptionL};
        for (Labeled item : displayItems) {
            item.setFont(textFont);
        }

        Button confirmEvent = new Button("Add to Calendar");
        confirmEvent.setFont(textFont);
        confirmEvent.setOnAction(event1 -> {
            String title = titleI.getCharacters().toString();
            ArrayList<ComboBoxBase<Integer>> startsI = new ArrayList<>(Arrays.asList(startDayI, startYearI));
            ArrayList<ComboBoxBase<Integer>> endsI = new ArrayList<>(Arrays.asList(endDayI, endYearI));
            ArrayList<ComboBoxBase<String>> startsII = new ArrayList<>(Arrays.asList(startHourI, startMinI));
            ArrayList<ComboBoxBase<String>> endsII = new ArrayList<>(Arrays.asList(endHourI, endMinI));
            int[] startTime = new int[5];
            int[] endTime = new int[5];
            startTime[0] = getMonthNumber(startMonthI.getValue());
            endTime[0] = getMonthNumber(endMonthI.getValue());
            for (int i = 1; i < 3; i++) {
                startTime[i] = startsI.get(i - 1).getValue();
                endTime[i] = endsI.get(i - 1).getValue();
            }
            for (int i = 3; i < 5; i++) {
                startTime[i] = Integer.parseInt(startsII.get(i - 3).getValue());
                endTime[i] = Integer.parseInt(endsII.get(i - 3).getValue());
            }

            try{
                Calendar startCal = new GregorianCalendar(startTime[2], startTime[0], startTime[1], startTime[3], startTime[4]);
                Calendar endCal = new GregorianCalendar(endTime[2], endTime[0], endTime[1], endTime[3], endTime[4]);
                if (startCal.compareTo(endCal) >= 0) {
                    throw new InvalidParameterException("Invalid end time.");
                }
                if (title.length() == 0) {
                    throw new IllegalArgumentException("Invalid title.");
                }
                String desc = descriptionI.getCharacters().toString();

                CalendarEvent newEvent = new CalendarEvent(title, startTime[0], startTime[1], startTime[2],
                                                        startTime[3], startTime[4], endTime[0], endTime[1],
                                                        endTime[2], endTime[3], endTime[4], desc);

                FileUtil.saveEventToFile(newEvent, eventsMasterList);
                FileUtil.sortEvents();
    
                root.getChildren().remove(calBody);
                calBody = makeCalendarPane(currentMonthCal);
                root.getChildren().add(1, calBody);
                eventStage.hide();
            } catch (InvalidParameterException ipe) {
                Alert a = new Alert(Alert.AlertType.ERROR, "You have entered an invalid end-time.");
                a.setTitle("Error");
                a.setHeaderText("Invalid End-Time");
                a.showAndWait();
            } catch (IllegalArgumentException iae) {
                Alert a = new Alert(Alert.AlertType.ERROR, "You have not entered a title.");
                a.setTitle("Error");
                a.setHeaderText("No title");
                a.showAndWait();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        });
        eventRoot.add(confirmEvent, 0, 5);

        Scene eventScene = new Scene(eventRoot, 750, 650);
        eventStage = new Stage();
        eventStage.setTitle("Event Editor");
        eventStage.setScene(eventScene);
        eventStage.show();
    }

    private void editAnEvent(String option, ArrayList<CalendarEvent> eventsOnFile) {
        int index = editViewer.getFocusModel().getFocusedIndex();
        CalendarEvent currentEdit = eventsOnFile.get(index);

        if (option.equals("edit")) {
            int sDifference = currentEdit.getYear() - ACTUAL_YEAR;
            int eDifference = currentEdit.getEndYear() - ACTUAL_YEAR;
            String milSH = currentEdit.getHour() < 10 ? "0" + currentEdit.getHour() : currentEdit.getHour() + "";
            String milEH = currentEdit.getEndHour() < 10 ? "0" + currentEdit.getEndHour() : currentEdit.getEndHour() + "";
            String milSM = currentEdit.getMinute() < 10 ? "0" + currentEdit.getMinute() : currentEdit.getMinute() + "";
            String milEM = currentEdit.getEndMinute() < 10 ? "0" + currentEdit.getEndMinute() : currentEdit.getEndMinute() + "";

            addAnEvent(getMonthName(currentEdit.getMonth()), getMonthName(currentEdit.getEndMonth()),
                        currentEdit.getDay() - 1, currentEdit.getEndDay() - 1, 10 + sDifference, 10 + eDifference,
                        milSH, milEH, milSM, milEM, currentEdit.getTitle(), currentEdit.getDescription());
        }   

        try {
            FileUtil.removeEvent(currentEdit);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        root.getChildren().remove(calBody);
        calBody = makeCalendarPane(currentMonthCal);
        root.getChildren().add(1, calBody);

        editStage.hide();
    }

    private String getDayName(int n) {
        String[] days = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
        return days[n];
    }

    private int getMonthNumber(String monthName) {
        String[] months = {"January", "February", "March", "April", "May",
                            "June", "July", "August", "September", "October",
                            "November", "December"};
        ArrayList<String> monthsList = new ArrayList<>();
        for (String month : months) {
            monthsList.add(month);
        }
        return monthsList.indexOf(monthName);
    }

    private String getMonthName(int n) {
        String[] months = {"January", "February", "March", "April", "May",
                            "June", "July", "August", "September", "October",
                            "November", "December"};
        return months[n];
    }

    private void setMilitaryComboBoxes(ComboBox<String> start, ComboBox<String> end, int n) {
        for (int i = 0; i < n; i++) {
            String str = "" + i;
            if (str.length() == 1) {
                start.getItems().add("0" + str);
                end.getItems().add("0" + str);
            } else {
                start.getItems().add(str);
                end.getItems().add(str);
            }
        }
    }

    private GregorianCalendar getPreviousMonth(Calendar cal) {
        int currMonth = cal.get(Calendar.MONTH);
        int prevMonth = currMonth == 0 ? 11 : currMonth - 1;
        currentYear = currMonth == 0 ? cal.get(Calendar.YEAR) - 1 : cal.get(Calendar.YEAR);
        return new GregorianCalendar(currentYear, prevMonth, 1);
    }

    private GregorianCalendar getNextMonth(Calendar cal) {
        currentMonth = cal.get(Calendar.MONTH);
        int nextMonth = currentMonth == 11 ? 0 : currentMonth + 1;
        currentYear = currentMonth == 11 ? cal.get(Calendar.YEAR) + 1 : cal.get(Calendar.YEAR);
        return new GregorianCalendar(currentYear, nextMonth, 1);
    }

    private void prevNext(int n) {
        header.getChildren().remove(0);
        root.getChildren().remove(1);

        boolean changeCheck = currentMonthCal.get(Calendar.YEAR) != getPreviousMonth(currentMonthCal).get(Calendar.YEAR);
        if (changeCheck && (n != 0)) {
            currentYear += n > 0 ? 1 : -1;
            currentMonth += n == 2 ? 1 : 0;
            currentMonth = currentMonth == 12 ? 0 : currentMonth;
        } else if (n == 0) {
            // add an actual year/month updater method
            currentYear = ACTUAL_YEAR;
            currentMonth = ACTUAL_MONTH;
        }

        if (n == -1) {
            currentMonthCal = getPreviousMonth(currentMonthCal);
        } else if (n == 1) {
            currentMonthCal = getNextMonth(currentMonthCal);
        } else if (n == 0) {
            currentMonthCal = new GregorianCalendar(currentYear, currentMonth, 1);
        } else if (n == 2) {

        }
        calBody = makeCalendarPane(currentMonthCal);
        currentMonth = currentMonthCal.get(Calendar.MONTH);
        month = new Label(getMonthName(currentMonthCal.get(Calendar.MONTH)));
        month.setFont(textFont);
        yearShow.setText("" + currentYear);
        header.getChildren().add(0, month);
        root.getChildren().add(1, calBody);
    }
    
    private GridPane makeCalendarPane(Calendar cal) {

        GridPane calBody = new GridPane();
        calBody.setAlignment(Pos.CENTER);
        calBody.setBorder(gridPaneBorder);
        calBody.setMinHeight(800);
        
        for (int i = 0; i < 7; i++) { // adding day headers
            Label dayName = new Label(getDayName(i));
            dayName.setFont(textFont);
            calBody.add(dayName, i, 0);
        }

        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentM = cal.get(Calendar.MONTH);
        int currentY = cal.get(Calendar.YEAR);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int row = 1;
        
        ListView<CalendarEvent> tempViewer;
        ArrayList<CalendarEvent> eventList = FileUtil.getEvents();

        for (int i = currentDay; i <= daysInMonth; i++) { // add each day of month
            if (dayOfWeek == 8) {
                dayOfWeek = 1;
                row++;
            }

            Label date = new Label(String.valueOf(currentDay));
            date.setFont(textFont);
            
            if ((currentDay == ACTUAL_DAY) && (currentM == ACTUAL_MONTH) && (currentY == ACTUAL_YEAR)) {
                date.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            }

            tempViewer = new ListView<>();
            for (int j = 0; j < eventList.size(); j++) {
                if ((eventList.get(j).getDay() == i) && (eventList.get(j).getMonth() == currentM) && (eventList.get(j).getYear() == currentY)) {
                    tempViewer.getItems().add(eventList.get(j));
                }
            }
            VBox dayView = new VBox();
            dayView.getChildren().addAll(date, tempViewer);

            calBody.add(dayView, dayOfWeek - 1, row);
            currentDay++;
            dayOfWeek++;
        }

        calBody.setGridLinesVisible(true);

        return calBody;
    }
}