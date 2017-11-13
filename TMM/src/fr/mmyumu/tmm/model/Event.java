package fr.mmyumu.tmm.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import fr.mmyumu.tmm.LocalDateAdapter;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The Class Event.
 */
public class Event {

	/** The name. */
	private final StringProperty name;

	/** The date. */
	private final ObjectProperty<LocalDate> date;

	private final ListProperty<Person> people;

	/**
	 * Instantiates a new event.
	 *
	 * @param name
	 *            the name
	 * @param date
	 *            the date
	 */
	public Event(String name, LocalDate date, List<Person> people) {
		super();
		this.name = new SimpleStringProperty(name);
		this.date = new SimpleObjectProperty<LocalDate>(date);

		ObservableList<Person> observableList = FXCollections.observableArrayList(people);
		this.people = new SimpleListProperty<Person>(observableList);
	}

	public Event() {
		this(null, LocalDate.now(), Collections.emptyList());
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name.get();
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name.set(name);
	}

	/**
	 * Name property.
	 *
	 * @return the string property
	 */
	public StringProperty nameProperty() {
		return name;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	public LocalDate getDate() {
		return date.get();
	}

	/**
	 * Sets the date.
	 *
	 * @param date
	 *            the new date
	 */
	public void setDate(LocalDate date) {
		this.date.set(date);
	}

	/**
	 * Date property.
	 *
	 * @return the local date property
	 */
	public ObjectProperty<LocalDate> dateProperty() {
		return date;
	}

	/**
	 * Gets the people.
	 *
	 * @return the people
	 */
	public List<Person> getPeople() {
		return people.get();
	}

	/**
	 * Sets the people.
	 *
	 * @param people
	 *            the new people
	 */
	public void setPeople(List<Person> people) {
		ObservableList<Person> observableList = FXCollections.observableArrayList(people);
		this.people.set(observableList);
	}

	/**
	 * People property.
	 *
	 * @return the list property
	 */
	public ListProperty<Person> peopleProperty() {
		return people;
	}

}
