package calendarProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Event implements Comparable<Event>{
	public static Map<String, Integer> DAYS_AND_WEEK = new HashMap<>() {
		private static final long serialVersionUID = 1L;
	{
		put("M", 1);
		put("T", 2);
		put("W", 3);
		put("R", 4);
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
		put(4, "R");
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
	
	public Event(String eventName, int year, int startingMonth, int endingMonth,
				 String dayOfWeek, int startingTime, int endingTime) {
		days = new ArrayList<>();
		this.eventName = eventName;
		this.year = year;
		this.startingMonth = startingMonth;
		this.endingMonth = endingMonth;
		if(endingMonth == 0) {
			days.add(Integer.parseInt(dayOfWeek));
		}
		else {
			for(int i = 0; i < dayOfWeek.length(); i++) {
				days.add(DAYS_AND_WEEK.get(dayOfWeek.substring(i, i + 1)));
			}
		}
		this.startingTime = startingTime;
		this.endingTime = endingTime;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getStartingMonth() {
		return startingMonth;
	}
	
	public int getEndingMonth() {
		return endingMonth;
	}
	
	public ArrayList<Integer> getDays() {
		return days;
	}
	
	public int getStartingTime() {
		return startingTime;
	}
	
	public int getEndingTime() {
		return endingTime;
	}

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
