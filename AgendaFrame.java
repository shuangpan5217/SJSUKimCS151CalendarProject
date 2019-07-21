package calendarProject;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class AgendaFrame extends JFrame{
	private LocalDate firstDay;
	private LocalDate click;
	public static final String DAY_OF_WEEK = "SMTWTFA";
	private JButton date;
	@SuppressWarnings("unused")
	private EventFrame ef;
	
	private static final long serialVersionUID = 1L;
	public AgendaFrame(LocalDate click, EventFrame ef) {
		this.click = LocalDate.of(click.getYear(), click.getMonth(), click.getDayOfMonth());
		firstDay = LocalDate.of(click.getYear(), click.getMonth(), 1);
		date = new JButton(click.toString());
		setTitle("Agenda");
		this.ef = ef;
		
		final Container contentPane2 = getContentPane();
		setLayout(new BoxLayout(contentPane2, BoxLayout.Y_AXIS));
		JLabel startingDateLabel = new JLabel("Starting date");
		JButton startingDate = new JButton(click.toString());
		JLabel endingDateLabel = new JLabel("Ending date:");
		JButton endingDate = new JButton(click.toString());
		JButton agendaButton = new JButton("Show agenda");
		JTextArea errorTextArea = new JTextArea();
		
		JPanel datePanel = new JPanel();
		JPanel errorPanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		errorTextArea.setForeground(Color.RED);
		errorTextArea.setEditable(false);
		datePanel.add(startingDateLabel);
		datePanel.add(startingDate);
		datePanel.add(endingDateLabel);
		datePanel.add(endingDate);
		errorPanel.add(errorTextArea);
		errorPanel.setVisible(false);
		buttonPanel.add(agendaButton);
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
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
						if((JButton) event.getSource() == startingDate)
							date = startingDate;
						else
							date = endingDate;
						setDate(panel4, frame, dateButton, date);
					}
					
				};
				if((JButton) event.getSource() == startingDate)
					date = startingDate;
				else
					date = endingDate;
				previousMonthButton.addActionListener(eventListener);
				nextMonthButton.addActionListener(eventListener);
				setDate(panel4, frame, dateButton, date);
				
				
				frame.add(panel3);
				frame.add(panel4);
				
				frame.setLocation(650, 350);
			    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			    frame.pack();
			    frame.setVisible(true);
			}
			
		};
		startingDate.addActionListener(listener);
		endingDate.addActionListener(listener);
		
		agendaButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String start = startingDate.getText();
				String end = endingDate.getText();
				if(start.compareTo(end) > 0) {
					errorTextArea.setText("Starting date can't be greater than ending date!");
					errorPanel.setVisible(true);
				}
				else {
					errorPanel.setVisible(false);
					String startArray[] = start.split("-");
					String endArray[] = end.split("-"); 
					ef.setStartingDate(LocalDate.of(Integer.parseInt(startArray[0]), Integer.parseInt(startArray[1]), Integer.parseInt(startArray[2])));
					ef.setEndingDate( LocalDate.of(Integer.parseInt(endArray[0]), Integer.parseInt(endArray[1]), Integer.parseInt(endArray[2])));
					ef.setAgendaView();
					dispose();
				}
			}
		});
		
		add(datePanel);
		add(errorPanel);
		add(buttonPanel);
	    setLocation(0, 500);
	    setPreferredSize(new Dimension(500, 150));
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    pack();
	    setVisible(true);
	}
	
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
