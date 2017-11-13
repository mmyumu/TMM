package fr.mmyumu.tmm.view;

import java.io.IOException;
import java.util.Optional;

import org.controlsfx.control.Notifications;

import fr.mmyumu.tmm.MainApp;
import fr.mmyumu.tmm.model.Event;
import fr.mmyumu.tmm.model.Person;
import fr.mmyumu.tmm.util.DateUtil;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * The Class EventOverviewController.
 */
public class EventOverviewController {

	/** The name label. */
	@FXML
	private Label nameLabel;

	/** The date label. */
	@FXML
	private Label dateLabel;

	@FXML
	private TableView<Person> peopleTable;
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	@FXML
	private TableColumn<Person, String> lastNameColumn;
	@FXML
	private TableColumn<Person, Number> totalColumn;

	/** The tmm overview controller. */
	private TMMOverviewController tmmOverviewController;

	/** The event. */
	private Event event;

	private BorderPane eventOverview;

	/**
	 * Sets the parent controller.
	 *
	 * @param tmmOverviewController
	 *            the new parent controller
	 */
	public void setParentController(TMMOverviewController tmmOverviewController) {
		this.tmmOverviewController = tmmOverviewController;
	}

	public TMMOverviewController getTmmOverviewController() {
		return tmmOverviewController;
	}

	public void setParent(BorderPane eventDetails) {
		this.eventOverview = eventDetails;
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		showPersonDetails(null);

		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

		peopleTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));

		initContextMenu();
	}

	private void initContextMenu() {
		peopleTable.setRowFactory(new Callback<TableView<Person>, TableRow<Person>>() {
			@Override
			public TableRow<Person> call(TableView<Person> tableView) {
				final TableRow<Person> row = new TableRow<>();

				final ContextMenu contextMenu = new ContextMenu();

				contextMenu.getItems().add(createAddPersonMenuItem());

				final MenuItem editMenuItem = new MenuItem("Edit");
				editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						Person editedPerson = row.getItem();
						boolean okClicked = tmmOverviewController.getMainApp().showPersonEditDialog(editedPerson, event);
						if(okClicked) {
							Notifications.create()
							.title("Edit")
							.text("Person " + editedPerson.getFullName() + " edited.")
							.hideAfter(Duration.seconds(3))
							.show();
						}
					}
				});
				contextMenu.getItems().add(editMenuItem);

				final MenuItem removeMenuItem = new MenuItem("Remove");
				removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						peopleTable.getItems().remove(row.getItem());
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

		peopleTable.setContextMenu(createPeopleTableViewContextMenu());
	}

	private ContextMenu createPeopleTableViewContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().add(createAddPersonMenuItem());
		return contextMenu;
	}

	private MenuItem createAddPersonMenuItem() {
		final MenuItem addMenuItem = new MenuItem("Add");

		addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddPerson();
			}
		});

		return addMenuItem;
	}

	/**
	 * Fills all text fields to show details about the event. If the specified
	 * event is null, all text fields are cleared.
	 *
	 * @param event
	 *            the event or null
	 */
	public void showEventDetails(Event event) {
		this.event = event;

		peopleTable.setItems((ObservableList<Person>) event.getPeople());

		nameLabel.setText(event.getName());
		dateLabel.setText(DateUtil.format(event.getDate()));
	}

	/**
	 * Show person details.
	 *
	 * @param person
	 *            the person
	 */
	public void showPersonDetails(Person person) {
		if (person != null) {
			showPersonOverview(person);
		} else {
			closePersonOverview();
		}

	}

	private void showPersonOverview(Person person) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));

		try {
			AnchorPane personOverview = (AnchorPane) loader.load();

			BorderPane personDetails = (BorderPane) eventOverview.lookup("#personDetails");
			personDetails.setCenter(personOverview);

			PersonOverviewController personOverviewController = loader.getController();
			personOverviewController.setParentController(this);
			personOverviewController.setParent(personDetails);
			personOverviewController.showPersonDetails(person);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void closePersonOverview() {
		if(eventOverview != null) {
			BorderPane personDetails = (BorderPane) eventOverview.lookup("#personDetails");
			personDetails.setCenter(null);
		}
	}

	// /**
	// * Sets the expenses visible.
	// *
	// * @param visible
	// * the new expenses visible
	// */
	// private void setExpensesVisible(boolean visible) {
	// if (eventDetails != null) {
	// Node expenses = eventDetails.lookup("#expenses");
	// expenses.setVisible(visible);
	// }
	// }

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeleteEvent() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete event");
		alert.setHeaderText("Are you sure you want to delete the event " + event.getName() + "?");
		alert.setContentText("This action cannot be undone");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			tmmOverviewController.delete(event);
		}
	}

	/**
	 * Called when the user clicks the edit button. Opens a dialog to edit
	 * details for the selected person.
	 */
	@FXML
	private void handleEditEvent() {
		tmmOverviewController.edit(event);
	}

	@FXML
	private void handleAddPerson() {
		Person tempPerson = new Person();

		boolean okClicked = tmmOverviewController.getMainApp().showPersonEditDialog(tempPerson, event);
		if (okClicked) {
			event.getPeople().add(tempPerson);
		}
	}

	@FXML
	private void compute() {
		tmmOverviewController.getMainApp().showResultDialog(event);
	}

	public void refresh() {
		peopleTable.refresh();
	}
}
