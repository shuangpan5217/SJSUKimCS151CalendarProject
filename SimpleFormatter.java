import java.time.LocalDate;

/**
 * A program to format events.
 * @author Shuang Pan, Yunru Chen, Nada Elzeini
 * @version 1.0 07/23/2019
 */
public class SimpleFormatter implements EventFormatter{
	
	/**
	 * Return the string representation of the header of the event.
	 * @param click the current date
	 * @return the header for the current event
	 */
	@Override
	public String formatHeader(LocalDate click) {
		return click.getDayOfWeek().toString() + " " + click.toString() + '\n';
	}
	
	/**
	 * Return the string representation of the body of the event.
	 * @param event the current event
	 * @return the string representation of the current event
	 */
	@Override
	public String formatEvent(Event event) {
		return event.getStartingTime() + ":00 - "
			 + event.getEndingTime() + ":00: " + event.getEventName() + '\n';
	}

	/**
	 * Return the string representation of the footer of the event.
	 * @return the footer of the current event
	 */
	@Override
	public String formatFooter() {
		return "\n";
	}
}