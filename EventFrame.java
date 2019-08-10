import java.awt.Container;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The interface of the event frame.
 * @author Shuang Pan, Yunru Chen, Nada Elzeini
 * @version 1.0 07/23/2019
 */
public class EventFrame extends JFrame implements ChangeListener{
	private DataModel dataModel;
	private CalendarFrame calendarFrame;
	private EventFormatter formatter;
	private final JTextArea textArea;
	private String view;
	private LocalDate current;
	private String result;
	private LocalDate startingDate;
	private LocalDate endingDate;
    private JButton dayButton;
    private JButton weekButton;
    private JButton monthButton;
    private JButton agendaButton;
    private JButton fileButton;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Initialize the interface of the event frame.
	 * @param dataModel the DataModel object
	 * @param formatter the EventFormatter object
	 */
	public EventFrame(DataModel dataModel, EventFormatter formatter) {
		this.dataModel = dataModel;
		this.formatter = formatter;
		dayButton = new JButton("Day");
		dayButton.setSelected(true);
		weekButton = new JButton("Week");
		monthButton = new JButton("Month");
		agendaButton = new JButton("Agenda");
		fileButton = new JButton("From File");
		result = "";
		view = "day";
	    final Container contentPane = getContentPane();
	    setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
	    
	    ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fileDialog = new FileDialog(new JFrame());
				fileDialog.setVisible(true);
				File[] files = fileDialog.getFiles();
				Scanner scanner = null;
				if(files.length > 0) {
					try {
				    	scanner = new Scanner(files[0]);
				    	processFile(scanner);
					}
					catch (FileNotFoundException exception) {
					    exception.printStackTrace();
					} 
					finally {
					    if (scanner != null) {
					        scanner.close();
					    }
					}
					fileButton.setBorderPainted(false);
					fileButton.removeActionListener(fileButton.getActionListeners()[0]);
				}
			}
	    };
	    fileButton.addActionListener(listener);
	    
	    JButton viewButtons[] = new JButton[] {dayButton,  weekButton, monthButton, agendaButton};
	    ActionListener viewListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton button = (JButton) e.getSource();
				if(button == viewButtons[0]) {
					setDayView();
				}
				else if(button == viewButtons[1]) {
					setWeekView();
				}
				else if(button == viewButtons[2]) {
					setMonthView();
				}
				else {
					@SuppressWarnings("unused")
					AgendaFrame af = new AgendaFrame(calendarFrame.getCurrentClick(), calendarFrame.getEventFrame());
				}
				if(button != viewButtons[3]) {
					for(int i = 0; i < viewButtons.length; i++) {
						viewButtons[i].setSelected(false);
					}
					button.setSelected(true);
				}
			}
	    	
	    };
	    for(int i = 0; i < viewButtons.length; i++) {
	    	viewButtons[i].addActionListener(viewListener);
	    }
	    
	    JPanel panel1 = new JPanel();
	    panel1.add(dayButton);
	    panel1.add(weekButton);
	    panel1.add(monthButton);
	    panel1.add(agendaButton);
	    
	    JPanel panel2 = new JPanel();
	    panel2.add(fileButton);
	    
	    final int row = 30;
	    final int column = 50;
	    textArea = new JTextArea(row, column);
	    textArea.setEditable(false);
	    JScrollPane scroll = new JScrollPane(textArea);
	    
	    
	    JPanel panel3 = new JPanel();
	    panel3.add(scroll);
	    
	    add(panel1);
	    add(panel2);
	    add(panel3);
	    
	    setTitle("Events");
	    setLocation(0, 500);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack();
	    setVisible(true);
	}
	
	/**
	 * Read events from the file and add them to the data model.
	 * @param fileScanner the file scanner to be used to read file
	 */
	public void processFile(Scanner fileScanner) {
		while(fileScanner.hasNextLine()) {
			String events[] = fileScanner.nextLine().split(";");
			for(int i = 0; i < events.length; i++)
				events[i] = events[i].trim();
			dataModel.addEvent(new Event(events[0], Integer.parseInt(events[1]), Integer.parseInt(events[2]), Integer.parseInt(events[3])
					   				   , events[4], Integer.parseInt(events[5]), Integer.parseInt(events[6])));
			if(view.equals("day")) {
				setDayView();
			}
			else if(view.equals("week")) {
				setWeekView();
			}
			else if(view.equals("month")){
				setMonthView();
			}
			else {
				setAgendaView();
			}
		}
	}
	
	/**
	 * Set the calendar frame reference and current click.
	 * @param frame the calendar frame
	 */
	public void setCalendarFrame(CalendarFrame frame) {
		this.calendarFrame = frame;
		current = calendarFrame.getCurrentClick();
	}

	/**
	 * Do reaction when the new event is added to the dataModel or set new views.
	 * @param e the ChangeEvent object
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		ArrayList<Event> events = dataModel.getEvents();
		current = calendarFrame.getCurrentClick();
		ArrayList<Event> eventsOnCurrentDay = new ArrayList<>();
		for(int i = 0; i < events.size(); i++) {
			Event event = events.get(i);
			if(current.getYear() == event.getYear()){
				if(event.getEndingMonth() == 0) {
					if(event.getStartingMonth() == current.getMonthValue() && (int) event.getDays().get(0) == current.getDayOfMonth())
						eventsOnCurrentDay.add(event);
				}
				else {
					if((current.getMonthValue() >= event.getStartingMonth() && current.getMonthValue() <= event.getEndingMonth())
					   && event.getDays().contains(current.getDayOfWeek().getValue()))
						eventsOnCurrentDay.add(event);
				}
			}
				
		}
		result = dataModel.format(eventsOnCurrentDay, formatter, current);
	}
	
	/**
	 * Set the text area with new results.
	 */
	public void setTextArea() {
		textArea.setText(result);
	}
	
	/**
	 * Set the result to empty for other useage. 
	 */
	public void setResult() {
		result = "";
	}
	
	/**
	 * Get latest result in the textArea.
	 * @return the latest result.
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Get current view
	 * @return the current view
	 */
	public String getView() {
		return view;
	}
	
	/**
	 * Set the day view to the text area and calendar frame.
	 */
	public void setDayView() {
		view = "day";
		calendarFrame.setView(view);
		stateChanged(null);
		setTextArea();
		setResult();
	}
	
	/**
	 * Set the week view to the text area and calendar frame.
	 */
	public void setWeekView() {
		view = "week";
		calendarFrame.setView(view);
		current = calendarFrame.getCurrentClick();
		LocalDate click = LocalDate.of(current.getYear(), current.getMonth(), current.getDayOfMonth());
		int dayOfWeek = current.getDayOfWeek().getValue();
		String weekResult = "";
		if(dayOfWeek != 7) {
			for(int i = dayOfWeek; i > 1; i--) {
				current = current.minusDays(1);
			}
			current = current.minusDays(1);
		}
		for(int i = 0; i <= 6; i++) {
			calendarFrame.setCurrentClick(current);
			stateChanged(null);
			weekResult += result;
			current = current.plusDays(1);
		}
		current = click;
		result = weekResult;
		setTextArea();
		setResult();
		calendarFrame.setCurrentClick(click);
	}
	
	/**
	 * Set the month view to the text area and calendar frame.
	 */
	public void setMonthView() {
		view = "month";
		calendarFrame.setView(view);
		current = calendarFrame.getCurrentClick();
		LocalDate firstDay = LocalDate.of(current.getYear(), current.getMonth(), 1);
		LocalDate click = LocalDate.of(current.getYear(), current.getMonth(), current.getDayOfMonth());
		String monthResult = "";
		int length = firstDay.getMonth().length(firstDay.isLeapYear());
		for(int i = 0; i < length; i++) {
			calendarFrame.setCurrentClick(firstDay);
			stateChanged(null);
			monthResult += result;
			firstDay = firstDay.plusDays(1);
		}
		result = monthResult;
		setTextArea();
		setResult();
		calendarFrame.setCurrentClick(click);
	}
	
	/**
	 * Set the agenda view to the text area and calendar frame.
	 */
	public void setAgendaView() {
		LocalDate start = LocalDate.of(startingDate.getYear(), startingDate.getMonth(), startingDate.getDayOfMonth());
		view = "agenda";
		current = calendarFrame.getCurrentClick();
		LocalDate click = LocalDate.of(current.getYear(), current.getMonth(), current.getDayOfMonth());
		String agendaResult = "";
		while(startingDate.compareTo(endingDate) <= 0) {
			calendarFrame.setCurrentClick(startingDate);
			stateChanged(null);
			agendaResult += result;
			startingDate = startingDate.plusDays(1);
		}
		result = agendaResult;
		setTextArea();
		setResult();
		calendarFrame.setCurrentClick(click);
		startingDate = start;
	}
	
	/**
	 * Set the starting date of the agenda
	 * @param startingDate the starting date of the agenda
	 */
	public void setStartingDate(LocalDate startingDate) {
		this.startingDate = startingDate;
	}
	
	/**
	 * Set the ending date of the agenda
	 * @param endingDate the ending date of the agenda
	 */
	public void setEndingDate(LocalDate endingDate) {
		this.endingDate = endingDate;
	}
}