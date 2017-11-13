package fr.mmyumu.tmm.model;

import java.util.Collections;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Person {
	private final StringProperty firstName;

	private final StringProperty lastName;

	private final ListProperty<Expense> expenses;

	// private DoubleBinding total;

	public Person(String firstName, String lastName, List<Expense> expenses) {
		super();
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);

		ObservableList<Expense> observableList = FXCollections.observableArrayList(expenses);
		this.expenses = new SimpleListProperty<Expense>(observableList);

		// this.total = Bindings.createDoubleBinding(() ->
		// expenses.stream().mapToDouble(Double::doubleValue).sum());
	}

	public Person() {
		this(null, null, Collections.emptyList());
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName.get();
	}

	/**
	 * Sets the first name.
	 *
	 * @param name
	 *            the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	/**
	 * First name property.
	 *
	 * @return the string property
	 */
	public StringProperty firstNameProperty() {
		return firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName.get();
	}

	/**
	 * Sets the last name.
	 *
	 * @param name
	 *            the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	/**
	 * Last name property.
	 *
	 * @return the string property
	 */
	public StringProperty lastNameProperty() {
		return lastName;
	}

	/**
	 * Gets the expenses.
	 *
	 * @return the expenses
	 */
	public List<Expense> getExpenses() {
		return expenses.get();
	}

	/**
	 * Sets the expenses.
	 *
	 * @param name
	 *            the new expenses
	 */
	public void setExpenses(List<Expense> expenses) {
		ObservableList<Expense> observableList = FXCollections.observableArrayList(expenses);
		this.expenses.set(observableList);
	}

	/**
	 * Expenses property.
	 *
	 * @return the string property
	 */
	public ListProperty<Expense> expensesProperty() {
		return expenses;
	}

	public DoubleProperty totalProperty() {
		return new SimpleDoubleProperty(expenses.stream().mapToDouble(expense -> expense.getAmount()).sum());
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
