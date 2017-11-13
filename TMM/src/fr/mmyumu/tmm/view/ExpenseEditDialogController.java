package fr.mmyumu.tmm.view;

import fr.mmyumu.tmm.model.Expense;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExpenseEditDialogController {

	@FXML
	private TextField labelField;
	@FXML
	private TextField amountField;

	private Stage dialogStage;
	private Expense expense;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	/**
	 * Sets the stage of this dialog.
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the expense to be edited in the dialog.
	 *
	 * @param expense
	 */
	public void setExpense(Expense expense) {
		this.expense = expense;

		labelField.setText(expense.getLabel());
		amountField.setText(expense.getAmount().toString());
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 *
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			expense.setLabel(labelField.getText());
			expense.setAmount(Double.parseDouble(amountField.getText()));

			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 *
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (labelField.getText() == null || labelField.getText().length() == 0) {
			errorMessage += "No valid label!\n";
		}

		if (amountField.getText() == null || amountField.getText().length() == 0) {
			errorMessage += "No valid amount!\n";
		} else {
			try {
				Double.parseDouble(amountField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "No vaild amount!\n";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}