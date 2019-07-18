package calendarProject;

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

public class EventFrame extends JFrame implements ChangeListener{
	private DataModel dataModel;
	private CalendarFrame calendarFrame;
	private EventFormatter formatter;
	private final JTextArea textArea;
	private int days;
	private String view;
	private LocalDate current;
	private String result;
    private JButton dayButton;
    private JButton weekButton;
    private JButton monthButton;
    private JButton agendaButton;
    private JButton fileButton;
	
	private static final long serialVersionUID = 1L;
	
	public EventFrame(DataModel dataModel, EventFormatter formatter) {
		this.dataModel = dataModel;
		this.formatter = formatter;
		days = 0;
		dayButton = new JButton("Day");
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
	    
	    dayButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calendarFrame.setView(view);
				stateChanged(null);
				setTextArea();
				result = "";
			}
	    	
	    });
	    
	    weekButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calendarFrame.setView("week");
			}
	    	
	    });
	    
	    monthButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				calendarFrame.setView("month");
				textArea.setText("");
			}
	    	
	    });
	    
	/*    fileButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (Desktop.isDesktopSupported()) {
	    		    try {
	    		    	if(System.getProperty("os.name").toUpperCase().contains("MAC"))
						Desktop.getDesktop().open(new File("/users/"));
	    		    	else
	    		    		Desktop.getDesktop().open(new File("C:\\"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	    		}
	    	}
	    });  */
	    
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
	    panel3.add(textArea);
	    panel3.add(scroll);
	    
	    add(panel1);
	    add(panel2);
	    add(panel3);
	    
	    setLocation(0, 500);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    pack();
	    setVisible(true);
	}
	
	public void processFile(Scanner fileScanner) {
		while(fileScanner.hasNextLine()) {
			String events[] = fileScanner.nextLine().split(";");
			dataModel.addEvent(new Event(events[0], Integer.parseInt(events[1]), Integer.parseInt(events[2]), Integer.parseInt(events[3])
					   				   , events[4], Integer.parseInt(events[5]), Integer.parseInt(events[6])));
			textArea.setText(result);
			setResult();
		}
	}
	
	public void setCalendarFrame(CalendarFrame frame) {
		this.calendarFrame = frame;
		current = calendarFrame.getCurrentClick();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		System.out.println();
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
		result = result.trim() + '\n';
	}
	
	public void setTextArea() {
		textArea.setText(result);
	}
	
	public void setResult() {
		result = "";
	}
	
	public String getResult() {
		return result;
	}
	
	public int getDays() {
		return days;
	}
}
