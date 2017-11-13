package fr.mmyumu.tmm.view;

import org.controlsfx.control.Notifications;

import fr.mmyumu.tmm.model.Expense;
import fr.mmyumu.tmm.model.Person;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * The Class EventOverviewController.
 */
public class PersonOverviewController {
	@FXML
	private TableView<Expense> expenseTable;
	@FXML
	private TableColumn<Expense, String> labelColumn;
	@FXML
	private TableColumn<Expense, Number> amountColumn;

	private EventOverviewController eventOverviewController;

	private Person person;

	private BorderPane personOverview;

	/**
	 * Sets the parent controller.
	 *
	 * @param tmmOverviewController
	 *            the new parent controller
	 */
	public void setParentController(EventOverviewController eventOverviewController) {
		this.eventOverviewController = eventOverviewController;
	}

	public void setParent(BorderPane personDetails) {
		this.personOverview = personDetails;
	}

	public void showPersonDetails(Person person) {
		this.person = person;
		expenseTable.setItems((ObservableList<Expense>) person.getExpenses());
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		labelColumn.setCellValueFactory(cellData -> cellData.getValue().labelProperty());
		amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty());

		initContextMenu();
	}

	private void initContextMenu() {
		expenseTable.setRowFactory(new Callback<TableView<Expense>, TableRow<Expense>>() {
			@Override
			public TableRow<Expense> call(TableView<Expense> tableView) {
				final TableRow<Expense> row = new TableRow<>();

				final ContextMenu contextMenu = new ContextMenu();

				contextMenu.getItems().add(createAddExpenseMenuItem());

				final MenuItem editMenuItem = new MenuItem("Edit");
				editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						boolean okClicked = eventOverviewController.getTmmOverviewController().getMainApp().showExpenseEditDialog(row.getItem());
						if(okClicked) {
							Notifications.create()
							.title("Edit")
							.text("Expense " + row.getItem().getLabel() + " edited.")
							.hideAfter(Duration.seconds(3))
							.show();

							eventOverviewController.refresh();
						}
					}
				});
				contextMenu.getItems().add(editMenuItem);

				final MenuItem removeMenuItem = new MenuItem("Remove");
				removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						expenseTable.getItems().remove(row.getItem());
						eventOverviewController.refresh();
					}
				});
				contextMenu.getItems().add(removeMenuItem);

				// Set context menu on row, but use a binding to make it only
				// show for non-empty rows:
				// Bindings.
				row.contextMenuProperty().bind(
						Bindings.when(row.emptyProperty())
								.then((ContextMenu) null)
								.otherwise(contextMenu));
				return row;
			}
		});

		expenseTable.setContextMenu(createExpenseTableViewContextMenu());
	}

	private ContextMenu createExpenseTableViewContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().add(createAddExpenseMenuItem());
		return contextMenu;
	}

	private MenuItem createAddExpenseMenuItem() {
		final MenuItem addMenuItem = new MenuItem("Add");

		addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddExpense();
			}
		});

		return addMenuItem;
	}

	@FXML
	private void handleAddExpense() {
		Expense tempExpense = new Expense();
		boolean okClicked = eventOverviewController.getTmmOverviewController().getMainApp().showExpenseEditDialog(tempExpense);
		if (okClicked) {
			person.getExpenses().add(tempExpense);
		}
		eventOverviewController.refresh();
	}
}
