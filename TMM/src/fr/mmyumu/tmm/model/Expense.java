package fr.mmyumu.tmm.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Expense {
	private final StringProperty label;

	private final DoubleProperty amount;

	public Expense(String label, Double amount) {
		super();
		this.label = new SimpleStringProperty(label);
		this.amount = new SimpleDoubleProperty(amount);
	}

	public Expense() {
		this(null, 0.0);
	}

	/**
	 * Gets the label.
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label.get();
	}

	/**
	 * Sets the label.
	 *
	 * @param label
	 *            the new label
	 */
	public void setLabel(String label) {
		this.label.set(label);
	}

	/**
	 * Label property.
	 *
	 * @return the string property
	 */
	public StringProperty labelProperty() {
		return label;
	}

	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public Double getAmount() {
		return amount.get();
	}

	/**
	 * Sets the amount.
	 *
	 * @param amount
	 *            the new amount
	 */
	public void setAmount(Double amount) {
		this.amount.set(amount);
	}

	/**
	 * Amount property.
	 *
	 * @return the double property
	 */
	public DoubleProperty amountProperty() {
		return amount;
	}
}
