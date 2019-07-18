package calendarProject;

import java.time.LocalDate;

public interface EventFormatter {
	String formatHeader(LocalDate click);
	String formatEvent(Event event);
	String formatFooter();
}
