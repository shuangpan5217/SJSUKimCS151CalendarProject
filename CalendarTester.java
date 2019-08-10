/**
 * A calendar program.
 * @author Shuang Pan, Yunru Chen, Nada Elzeini
 * @version 1.0 07/23/2019
 */
public class CalendarTester {
	/**
	 * Initialize the calendar and event frame.
	 * @param args unused
	 */
	public static void main(String[] args) {
		EventFormatter formatter = new SimpleFormatter(); 
		
		DataModel dataModel = new DataModel();
		
		EventFrame eventFrame = new EventFrame(dataModel, formatter);
		CalendarFrame calendarFrame = new CalendarFrame(dataModel);
		
		eventFrame.setCalendarFrame(calendarFrame);
		calendarFrame.setEventFrame(eventFrame);
		calendarFrame.setCalendarReference(calendarFrame);
		
		dataModel.addChangeListener(eventFrame);
	}
}