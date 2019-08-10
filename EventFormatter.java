import java.time.LocalDate;

/**
 * The interface to format events.
 * @author Shuang Pan, Yunru Chen, Nada Elzeini
 * @version 1.0 07/23/2019
 */
public interface EventFormatter {
	/**
	 * Return the string representation of the header of the event.
	 * @param click the current date
	 * @return the header for the current event
	 */
	String formatHeader(LocalDate click);
	
	/**
	 * Return the string representation of the body of the event.
	 * @param event the current event
	 * @return the string representation of the current event
	 */
	String formatEvent(Event event);
	
	/**
	 * Return the string representation of the footer of the event.
	 * @return the footer of the current event
	 */
	String formatFooter();
}