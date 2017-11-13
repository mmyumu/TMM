package fr.mmyumu.tmm.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Helper class to wrap a list of persons. This is used for saving the list of
 * persons to XML.
 *
 * @author Marco Jakob
 */
@XmlRootElement(name = "events")
public class EventListWrapper {

	private List<Event> events;

	@XmlElement(name = "event")
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
}
