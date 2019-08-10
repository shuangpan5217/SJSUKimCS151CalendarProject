import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * The interface of the creating event.
 * @author Shuang Pan, Yunru Chen, Nada Elzeini
 * @version 1.0 07/23/2019
 */
public class CreateEventFrame extends JFrame{
	private LocalDate firstDay;
	private LocalDate click;
	private CalendarFrame cf;
	private DataModel dataModel;
	public static final String DAY_OF_WEEK = "SMTWTFA";
	
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize the interface of the creating-event interface.
	 * @param click the current click on the calendar frame
	 * @param cf the calendar frame reference
	 * @param dataModel the DataModel object
	 * @param createButton the CreateEvent Button from the calendar frame
	 * @param mouseListeners the mouse listeners on the CreateEvent button
	 */
	public CreateEventFrame(LocalDate previousClick, CalendarFrame calendarFrame, DataModel dm, JButton createButton, MouseListener[] mouseListeners) {
		this.cf = calendarFrame;
		this.click = previousClick;
		firstDay = LocalDate.of(click.getYear(), click.getMonth(), 1);
		this.dataModel = dm;
		
		
		setTitle("Create Event");
		addWindowListener(new WindowAdapter() {
			
			@Override
            public void windowClosing(WindowEvent e)
            {
				for(MouseListener listener: mouseListeners)
					createButton.addMouseListener(listener);
            }
		});
		final Container contentPane2 = getContentPane();
		setLayout(new BoxLayout(contentPane2, BoxLayout.Y_AXIS));
		JPanel namePanel = new JPanel();
		JPanel datePanel = new JPanel();
		JPanel errorPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JLabel nameLabel = new JLabel("Add title: ");
		JTextField nameField = new JTextField(10);
		JButton date = new JButton();
		JTextArea errorTextArea = new JTextArea();
		errorTextArea.setForeground(Color.RED);
		errorTextArea.setEditable(false);
		date.setText(click.toString());
		date.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frame = new JFrame();
				frame.addWindowFocusListener(new WindowFocusListener() {

					@Override
					public void windowGainedFocus(WindowEvent e) {
						
					}

					@Override
					public void windowLostFocus(WindowEvent e) {
						frame.dispose();
					}
					
				});
				frame.setTitle("Select date");
				JButton dateButton = new JButton();
				JButton nextMonthButton = new JButton("Next Month");
				JButton previousMonthButton = new JButton("Previous Month");
				
			    final Container contentPane = frame.getContentPane();
			    frame.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
				
				JPanel panel3 = new JPanel();
				dateButton.setText(firstDay.getMonth().toString() + " " + Integer.toString(firstDay.getYear()));
				dateButton.setBorderPainted(false);
				panel3.add(dateButton);
				panel3.add(previousMonthButton);
				panel3.add(nextMonthButton);
				
				JPanel panel4 = new JPanel();
				panel4.setLayout(new GridLayout(7, 7));
				
				ActionListener eventListener = new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if(((JButton)e.getSource()) == previousMonthButton) {
							firstDay = firstDay.minusMonths(1);
							firstDay = LocalDate.of(firstDay.getYear(), firstDay.getMonth(), 1);
						}
						else if(((JButton)e.getSource()) == nextMonthButton) {
							firstDay = firstDay.plusMonths(1);
							firstDay = LocalDate.of(firstDay.getYear(), firstDay.getMonth(), 1);
						}
						setDate(panel4, frame, dateButton, date);
					}
					
				};
				previousMonthButton.addActionListener(eventListener);
				nextMonthButton.addActionListener(eventListener);
				setDate(panel4, frame, dateButton, date);
				firstDay = LocalDate.of(firstDay.getYear(), firstDay.getMonth(), 1);
				
				frame.add(panel3);
				frame.add(panel4);
				
				frame.setLocation(650, 350);
			    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			    frame.pack();
			    frame.setVisible(true);
			}
			
		});
		Integer hour[] = new Integer[24];
		for(int i = 0; i < hour.length; i++) {
			hour[i] = i;
		}
		JLabel startingTimeLabel = new JLabel("Staring Time:");
		JComboBox<Integer> startingTimeBox = new JComboBox<>(hour);
		JLabel endingTimeLabel = new JLabel("Ending Time:");
		JComboBox<Integer> endingTimeBox = new JComboBox<>(hour);
		JButton addButton = new JButton("Add");
		
		//check time conflict and add event.
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int start = (Integer) startingTimeBox.getSelectedItem();
				int end = (Integer) endingTimeBox.getSelectedItem();
				
				String name = nameField.getText();
				if(name.length() == 0) {
					errorTextArea.setText("Please enter a event name.");
					errorPanel.setVisible(true);
				}
				else if(start == end) {
					errorTextArea.setText("Starting time and ending time can't be equal.");
					errorPanel.setVisible(true);
				}
				else if(start > end && end != 0){
					errorTextArea.setText("Starting time can't be greater than ending time if ending time is not zero.");
					errorPanel.setVisible(true);
				}
				else {
					Event newEvent = new Event(name, click.getYear(), click.getMonthValue(), 0
							                 , Integer.toString(click.getDayOfMonth()), start, end);
					if(dataModel.checkConflict(newEvent)) {
						errorTextArea.setText("Time conflict, please reenter the time.");
						errorPanel.setVisible(true);
					}
					else {
						dataModel.addEvent(newEvent);
						EventFrame ef = cf.getEventFrame();
						if(ef.getView().equals("day")) {
							ef.setDayView();
						}
						else if(ef.getView().equals("week")) {
							ef.setWeekView();
						}
						else if(ef.getView().equals("month")){
							ef.setMonthView();
						}
						else {
							ef.setAgendaView();
						}
						errorPanel.setVisible(false);
						for(MouseListener listener: mouseListeners)
							createButton.addMouseListener(listener);
						dispose();
					}
				}
			}
		});
		
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		datePanel.add(date);
		datePanel.add(startingTimeLabel);
		datePanel.add(startingTimeBox);
		datePanel.add(endingTimeLabel);
		datePanel.add(endingTimeBox);
		errorPanel.add(errorTextArea);
		errorPanel.setVisible(false);
		buttonPanel.add(addButton);
		
		setPreferredSize(new Dimension(500, 250));
		add(namePanel);
		add(datePanel);
		add(errorPanel);
		add(buttonPanel);
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    pack();
	    setVisible(true);
	
	}

	/**
	 * Open a date frame and let users select the date.
	 * @param panel4 the JPanel which holds the date interface
	 * @param frame the frame which holds the date panel
	 * @param dateButton set the date about month and year on the date frame
	 * @param date set the selected date to the creating-event interface
	 */
	public void setDate(JPanel panel4, JFrame frame, JButton dateButton, JButton date) {
		panel4.removeAll();
		for(int i = 0; i < DAY_OF_WEEK.length(); i++) {
			JButton a = new JButton(DAY_OF_WEEK.substring(i, i + 1));
			a.setBorderPainted(false);
			panel4.add(a);
		}
		ArrayList<JButton> buttons = new ArrayList<>();
		int firstDayOfMonth = firstDay.getDayOfWeek().getValue();
		if(firstDayOfMonth != 7) {
			for(int i = 0; i < firstDayOfMonth; i++) {
				firstDay = firstDay.minusDays(1);
			}
			for(int i = 0; i < firstDayOfMonth; i++) {
				JButton a = new JButton(Integer.toString(firstDay.getDayOfMonth()));
				if(click.equals(firstDay))
					a.setBorderPainted(true);
				else
					a.setBorderPainted(false);
				a.setForeground(Color.GRAY);
				panel4.add(a);
				firstDay = firstDay.plusDays(1);
				buttons.add(a);
			}
		}
		int currentMonthLength = firstDay.getMonth().length(firstDay.isLeapYear());
		for(int i = 0; i < currentMonthLength; i++) {
			JButton a = new JButton(Integer.toString(firstDay.getDayOfMonth()));
			if(firstDay.equals(LocalDate.now())) {
				a.setForeground(Color.RED);
				a.setFont(new Font("ROMAN_BASELINE", Font.ITALIC, 12));
			}
			else {
				a.setBorderPainted(false);
			}
			panel4.add(a);
			firstDay = firstDay.plusDays(1);
			buttons.add(a);
		}
		int leftDays = 0;
		if(firstDayOfMonth != 7)
			leftDays = 49 - currentMonthLength - firstDayOfMonth - 7;
		else
			leftDays = 49 - currentMonthLength - 7;
		for(int i = 0; i < leftDays; i++) {
			JButton a = new JButton(Integer.toString(firstDay.getDayOfMonth()));
			a.setForeground(Color.GRAY);
			if(click.equals(firstDay))
				a.setBorderPainted(true);
			else
				a.setBorderPainted(false);
			panel4.add(a);
			firstDay = firstDay.plusDays(1);
			buttons.add(a);
		}
		
		firstDay = firstDay.minusMonths(1);
		dateButton.setText(firstDay.getMonth().toString() + " " + Integer.toString(firstDay.getYear()));
		if(!click.equals(LocalDate.now())) {
			if(firstDay.getYear() == click.getYear() 
			&& firstDay.getMonthValue() == click.getMonthValue()) {
				for(JButton button: buttons) {
					if(button.getForeground().equals(Color.BLACK) 
					&& Integer.parseInt(button.getText()) == click.getDayOfMonth()) {
						button.setForeground(Color.BLUE);
						button.setBorderPainted(true);
						break;
					}
				}
			}
		}
		
		for(JButton button: buttons) {
			button.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(!button.getForeground().equals(Color.GRAY)) {
						click = LocalDate.of(firstDay.getYear(), firstDay.getMonth(), Integer.parseInt(button.getText()));
					}
					else {
						int day = Integer.parseInt(button.getText());
						if(day > 22) {
							firstDay = firstDay.minusMonths(1);
						}
						else {
							firstDay = firstDay.plusMonths(1);
						}
						click = LocalDate.of(firstDay.getYear(), firstDay.getMonth(), Integer.parseInt(button.getText()));
					}
					firstDay = LocalDate.of(firstDay.getYear(), firstDay.getMonth(), 1);
					LocalDate previousClick = cf.getCurrentClick();
					cf.setCurrentClick(click);
					cf.setFirstDay(firstDay);
					cf.setDate();
					EventFrame ef = cf.getEventFrame();
					if(ef.getView().equals("day")) {
						ef.setDayView();
					}
					else if(ef.getView().equals("week")) {
						int x = previousClick.getDayOfWeek().getValue();
						LocalDate p = null;
						if(x != 7) {
							p = previousClick.minusDays(x);
						}
						else
							p = previousClick;
						LocalDate n = p.plusDays(6);
						if(click.compareTo(p) < 0 || click.compareTo(n) > 0)
							ef.setWeekView();
					}
					else if(ef.getView().equals("month")){
						if(!(previousClick.getYear() == click.getYear() && previousClick.getMonthValue() == click.getMonthValue()))
							ef.setMonthView();
					}
					else {
						ef.setAgendaView();
					}
					
					date.setText(click.toString());
					frame.dispose();
				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					button.setBorderPainted(true);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					LocalDate local = null;
					if(button.getForeground().equals(Color.GRAY)) {
						if(Integer.parseInt(button.getText()) > 22)
							local = LocalDate.of(firstDay.minusMonths(1).getYear(), firstDay.minusMonths(1).getMonth(), Integer.parseInt(button.getText()));
						else
							local = LocalDate.of(firstDay.plusMonths(1).getYear(), firstDay.plusMonths(1).getMonth(), Integer.parseInt(button.getText()));
						if(!local.equals(click))
							button.setBorderPainted(false);
					}
					else if(!((firstDay.getYear() == LocalDate.now().getYear() 
					   && firstDay.getMonthValue() == LocalDate.now().getMonthValue() 
					   && Integer.parseInt(button.getText()) == LocalDate.now().getDayOfMonth()) 
				     || ((firstDay.getYear() == click.getYear() 
					   && firstDay.getMonthValue() == click.getMonthValue() 
					   && Integer.parseInt(button.getText()) == click.getDayOfMonth()))))
						button.setBorderPainted(false);
				}
				
			});
		}
		panel4.revalidate();
		panel4.repaint();
	}
}