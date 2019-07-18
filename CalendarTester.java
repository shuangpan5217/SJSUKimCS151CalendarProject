package calendarProject;

public class CalendarTester {
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
