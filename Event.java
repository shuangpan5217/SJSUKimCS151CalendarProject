import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Event class which represents events.
 * @author Shuang Pan, Yunru Chen, Nada Elzeini
 * @version 1.0 07/23/2019
 */
public class Event implements Comparable<Event>{
	public static Map<String, Integer> DAYS_AND_WEEK = new HashMap<>() {
		private static final long serialVersionUID = 1L;
	{
		put("M", 1);
		put("T", 2);
		put("W", 3);
		put("H", 4);
		put("F", 5);
		put("A", 6);
		put("S", 7);
	}};
	
	public static Map<Integer, String> WEEK_AND_DAYS  = new HashMap<>() {
		private static final long serialVersionUID = 1L;
	{
		put(1, "M");
		put(2, "T");
		put(3, "W");
		put(4, "H");
		put(5, "F");
		put(6, "A");
		put(7, "S");
	}};
	
	private String eventName;
	private int year;
	private int startingMonth;
	private int endingMonth;
	private ArrayList<Integer> days;
	private int startingTime;
	private int endingTime;
	
	/**
	 * Initialize the current event.
	 * @param eventName the event name
	 * @param year the year of the event
	 * @param startingMonth the starting month of the event
	 * @param endingMonth the ending month of the event (0 for one time event)
	 * @param day day of week for regular event and day of month for the event
	 * @param startingTime the starting time of the event
	 * @param endingTime the ending time of the event
	 */
	public Event(String eventName, int year, int startingMonth, int endingMonth,
				 String day, int startingTime, int endingTime) {
		days = new ArrayList<>();
		this.eventName = eventName;
		this.year = year;
		this.startingMonth = startingMonth;
		this.endingMonth = endingMonth;
		if(endingMonth == 0) {
			days.add(Integer.parseInt(day));
		}
		else {
			for(int i = 0; i < day.length(); i++) {
				days.add(DAYS_AND_WEEK.get(day.substring(i, i + 1)));
			}
		}
		this.startingTime = startingTime;
		this.endingTime = endingTime;
	}
	
	/**
	 * Get the event name.
	 * @return the event name
	 */
	public String getEventName() {
		return eventName;
	}
	
	/**
	 * Get the year of the current event.
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * Get the starting month of the current event.
	 * @return the starting month
	 */
	public int getStartingMonth() {
		return startingMonth;
	}
	
	/**
	 * Get the ending month of the current event.
	 * @return the ending month
	 */
	public int getEndingMonth() {
		return endingMonth;
	}
	
	/**
	 * Get the days of the event
	 * @return days array list
	 */
	public ArrayList<Integer> getDays() {
		return days;
	}
	
	/**
	 * Get the starting time of the current time.
	 * @return the starting time
	 */
	public int getStartingTime() {
		return startingTime;
	}
	
	/**
	 * Get the ending time of the current event.
	 * @return the ending time
	 */
	public int getEndingTime() {
		return endingTime;
	}

	/**
	 * Compare the hour between two events for sorting.
	 * @param event the event object
	 */
	@Override
	public int compareTo(Event event) {
		if(startingTime < event.startingTime)
			return -1;
		else if(startingTime > event.startingTime)
			return 1;
		else {
			if(endingTime < event.endingTime)
				return -1;
			else if(endingTime > event.endingTime)
				return 1;
			else
				return 0;
		}
	}
}