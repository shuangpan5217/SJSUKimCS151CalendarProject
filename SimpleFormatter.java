package calendarProject;

import java.time.LocalDate;

public class SimpleFormatter implements EventFormatter{
	@Override
	public String formatHeader(LocalDate click) {
		return click.getDayOfWeek().toString() + " " + click.toString() + '\n';
	}
	
	@Override
	public String formatEvent(Event event) {
		return event.getStartingTime() + ":00 - "
			 + event.getEndingTime() + ":00: " + event.getEventName() + '\n';
	}

	@Override
	public String formatFooter() {
		return "\n";
	}
}


