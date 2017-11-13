package fr.mmyumu.tmm.view;

import java.math.BigDecimal;

import fr.mmyumu.tmm.model.Event;
import fr.mmyumu.tmm.model.Expense;
import fr.mmyumu.tmm.model.ResultComputer;
import fr.mmyumu.tmm.util.Util;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ResultDialogController {

	@FXML
	private Label eventName;

	private Stage dialogStage;
	private Event event;
	private boolean okClicked = false;

	private ResultComputer resultComputer;

	private StringBuilder resultAsString = new StringBuilder();

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

	public void setEvent(Event event) {
		this.event = event;
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
		okClicked = true;
		dialogStage.close();
	}

	public void shown() {
		Parent parent = dialogStage.getScene().getRoot();
		createPeopleListContent(parent);
		createResultsContent(parent);
	}

	private void createPeopleListContent(Parent parent) {
		Pane peopleListParent = (Pane) parent.lookup("#peopleList");

		eventName.setText("Results for " + event.getName());
		resultAsString.append("Results for ");
		resultAsString.append(event.getName());
		resultAsString.append("\n\n");

		event.getPeople().forEach(person -> {
			double total = person.getExpenses().stream().mapToDouble(expense -> expense.getAmount()).sum();
			TitledPane titledPane = new TitledPane();
			titledPane.setText(person.getFullName() + ": " + total);
			resultAsString.append(person.getFullName());
			resultAsString.append(": ");
			resultAsString.append(total);
			resultAsString.append("\n");

			person.getExpenses().forEach(expense -> {
				resultAsString.append(expense.getLabel());
				resultAsString.append(": ");
				resultAsString.append(expense.getAmount());
				resultAsString.append("\n");
			});
			resultAsString.append("\n");

			TableView<Expense> expenses = new TableView<>();
			expenses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

			TableColumn<Expense, String> expenseLabel = new TableColumn<>();
			expenseLabel.setText("Label");
			expenseLabel.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
			expenses.getColumns().add(expenseLabel);

			TableColumn<Expense, Number> expenseAmount = new TableColumn<>();
			expenseAmount.setText("Amount");
			expenseAmount.setCellValueFactory(cellData -> cellData.getValue().amountProperty());
			expenses.getColumns().add(expenseAmount);

			expenses.setItems((ObservableList<Expense>) person.getExpenses());


			ObservableList<Expense> expensesObservableList = expenses.getItems().isEmpty()
					? FXCollections.observableArrayList(new Expense()) : expenses.getItems();
			expenses.setFixedCellSize(25);
			expenses.prefHeightProperty()
					.bind(expenses.fixedCellSizeProperty().multiply(Bindings.size(expensesObservableList).add(1.01)));
			expenses.minHeightProperty().bind(expenses.prefHeightProperty());
			expenses.maxHeightProperty().bind(expenses.prefHeightProperty());

			titledPane.setContent(expenses);
			peopleListParent.getChildren().add(titledPane);
		});
		resultAsString.append("\n\n");
	}

	private void createResultsContent(Parent parent) {
		Pane resultsParent = (Pane) parent.lookup("#results");

		resultComputer = new ResultComputer(event);
		resultComputer.compute();
		String resultTrace = resultComputer.getTrace();
		String oweTrace = resultComputer.getOweTrace();

		createLabel(resultsParent,
				"Average payment per person: " + Util.AMOUNT_FORMATTER.format(resultComputer.getAverage()));
		resultAsString.append("Average payment per person: ");
		resultAsString.append(Util.AMOUNT_FORMATTER.format(resultComputer.getAverage()));
		resultAsString.append("\n\n");

		createLabel(resultsParent, "Balance:");
		resultAsString.append("Balance:\n");

		resultComputer.getPersonToBalance().forEach((person, balance) -> {
			Label balanceField = createLabel(resultsParent,
					person.getFullName() + ": " + Util.AMOUNT_FORMATTER.format(balance));
			resultAsString.append(person.getFullName());
			resultAsString.append(": ");
			resultAsString.append(Util.AMOUNT_FORMATTER.format(balance));
			resultAsString.append("\n");

			if (balance.compareTo(BigDecimal.ZERO) > 0) {
				balanceField.getStyleClass().add("creditor");
			} else if (balance.compareTo(BigDecimal.ZERO) < 0) {
				balanceField.getStyleClass().add("debtor");
			}
		});
		createLabel(resultsParent, "");
		resultAsString.append("\n");
		createLabel(resultsParent, "Transactions:");
		resultAsString.append("Transactions:\n");
		createLabel(resultsParent, resultTrace);
		resultAsString.append(resultTrace);

		createLabel(resultsParent, "");
		resultAsString.append("\n\n");

		createLabel(resultsParent, "Who owes what?");
		resultAsString.append("Who owes what?\n");
		createLabel(resultsParent, oweTrace);
		resultAsString.append(oweTrace);
	}

	private Label createLabel(Pane parent, String text) {
		Label label = new Label();
		label.setText(text);

		parent.getChildren().add(label);
		return label;
	}

	@FXML
	private void handleCopyClipboard() {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();
		content.putString(resultAsString.toString());
		clipboard.setContent(content);
	}
}