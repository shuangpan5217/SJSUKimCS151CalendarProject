import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The date model which holds all events.
 * @author Shuang Pan, Yunru Chen, Nada Elzeini
 * @version 1.0 07/23/2019
 */
public class DataModel{
	private ArrayList<Event> events;
	private ArrayList<ChangeListener> listeners;
	
	/**
	 * Initialize the date model and change listeners.
	 */
	public DataModel() {
		events = new ArrayList<Event>();
		listeners = new ArrayList<ChangeListener>();
	}
	
	/**
	 * Add the event to the data model and call state change.
	 * @param event the event object
	 */
	public void addEvent(Event event) {
		events.add(event);
		Collections.sort(events);
		ChangeEvent e = new ChangeEvent(this);
		for(ChangeListener listener: listeners) {
			listener.stateChanged(e);
		}
	}
	
	/**
	 * Add the ChangeListener to the data model.
	 * @param listener ChangeListener object
	 */
	public void addChangeListener(ChangeListener listener) {
		listeners.add(listener);
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Return the copy of all events in the data model.
	 * @return the copy of all events
	 */
	public ArrayList<Event> getEvents(){
		return (ArrayList<Event>) events.clone();
	}
	
	/**
	 * Return the string representation of all events on current day.
	 * @param events all events which will be displayed 
	 * @param formatter the EventFormatter formatter
	 * @param currentDay the certain day
	 * @return the string representation of all events
	 */
	public String format(ArrayList<Event> events, EventFormatter formatter, LocalDate currentDay) {
		String result = "";
		result += formatter.formatHeader(currentDay);
		if(events.size() != 0) {
			for(Event event: events) {
				result += formatter.formatEvent(event);
			}
		}
		result += formatter.formatFooter();
		return result;
	}
	
	/**
	 * Check whether two events have time conflicts. Return true if there are; otherwise, false.
	 * @param event the new event to be checked
	 * @return true if there are conflicts between two events; otherwise, false
	 */
	public boolean checkConflict(Event event) {
		boolean hasConflict = false;
		for(Event e: events) {
			if(e.getYear() == event.getYear()) {
				if(e.getEndingMonth() == 0) {
					if(e.getStartingMonth() == event.getStartingMonth() && (int) e.getDays().get(0) == (int) event.getDays().get(0)) {
						if(checkTime(e, event)) {
							hasConflict = true;
							break;
						}
					}
				}
				else {
					if((event.getStartingMonth() >= e.getStartingMonth() && event.getStartingMonth() <= e.getEndingMonth()) 
					 && e.getDays().contains(LocalDate.of(event.getYear(), event.getStartingMonth(), event.getDays().get(0)).getDayOfWeek().getValue())) {
						if(checkTime(e, event)) {
							hasConflict = true;
							break;
						}
					}
				}
			}
		}
		return hasConflict;
	}
	
	/**
	 * Only check the time for hours on the certain day.
	 * @param e the event in the array list
	 * @param event the new event to be checked
	 * @return true if there is no hour conflict; otherwise, false.
	 */
	private boolean checkTime(Event e, Event event) {
		if(e.getEndingTime() == 0 && event.getEndingTime() == 0) {
			return true;
		}
		else if(e.getEndingTime() == 0 && event.getEndingTime() != 0) {
			if(e.getStartingTime() >= event.getEndingTime())
				return false;
			else
				return true;
		}
		else if(e.getEndingTime() != 0 && event.getEndingTime() == 0) {
			if(e.getEndingTime() <= event.getStartingTime())
				return false;
			else
				return true;
		}
		else {
			if(event.getEndingTime() <= e.getStartingTime() || event.getStartingTime() >= e.getEndingTime())
				return false;
			else 
				return true;
		}
	}
}