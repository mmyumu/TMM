package fr.mmyumu.tmm.view;

import java.io.IOException;
import java.time.LocalDate;

import org.controlsfx.control.Notifications;

import fr.mmyumu.tmm.MainApp;
import fr.mmyumu.tmm.model.Event;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Duration;

public class TMMOverviewController {
	@FXML
	private TableView<Event> eventTable;
	@FXML
	private TableColumn<Event, String> nameColumn;
	@FXML
	private TableColumn<Event, LocalDate> dateColumn;

	// Reference to the main application.
	private MainApp mainApp;
	private AnchorPane tmmOverview;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public TMMOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

		// Listen for selection changes and show the person details when
		// changed.
		eventTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showEventDetails(newValue));

		initContextMenu();
	}

	private void initContextMenu() {
		eventTable.setRowFactory(new Callback<TableView<Event>, TableRow<Event>>() {
			@Override
			public TableRow<Event> call(TableView<Event> tableView) {
				final TableRow<Event> row = new TableRow<>();

				final ContextMenu contextMenu = new ContextMenu();

				final MenuItem computeMenuItem = new MenuItem("Compute");
				computeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						mainApp.showResultDialog(row.getItem());
					}
				});
				contextMenu.getItems().add(computeMenuItem);

				contextMenu.getItems().add(createAddEventMenuItem());

				final MenuItem editMenuItem = new MenuItem("Edit");
				editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Event editedEvent = row.getItem();
						boolean okClicked = mainApp.showEventEditDialog(editedEvent);
						if(okClicked) {
							Notifications.create()
							.title("Edit")
							.text("Event " + editedEvent.getName() + " edited.")
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
						eventTable.getItems().remove(row.getItem());
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

		eventTable.setContextMenu(createEventTableViewContextMenu());
	}

	private ContextMenu createEventTableViewContextMenu() {
		final ContextMenu contextMenu = new ContextMenu();
		contextMenu.getItems().add(createAddEventMenuItem());
		return contextMenu;
	}

	private MenuItem createAddEventMenuItem() {
		final MenuItem addMenuItem = new MenuItem("Add");

		addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleAddEvent();
			}
		});

		return addMenuItem;
	}

	/**
	 * Gets the main app.
	 *
	 * @return the main app
	 */
	public MainApp getMainApp() {
		return mainApp;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

		// Add observable list data to the table
		eventTable.setItems(mainApp.getEventData());
	}

	/**
	 * Sets the parent.
	 *
	 * @param tmmOverview
	 *            the new parent
	 */
	public void setParent(AnchorPane tmmOverview) {
		this.tmmOverview = tmmOverview;
	}

	/**
	 * Fills all text fields to show details about the event. If the specified
	 * event is null, all text fields are cleared.
	 *
	 * @param event
	 *            the event or null
	 */
	private void showEventDetails(Event event) {
		if (event != null) {
			showEventOverview(event);
		} else {
			closeEventOverview();
		}
	}

	private void showEventOverview(Event event) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/EventOverview.fxml"));

		try {
			AnchorPane eventOverview = (AnchorPane) loader.load();

			BorderPane eventDetails = (BorderPane) tmmOverview.lookup("#eventDetails");
			eventDetails.setCenter(eventOverview);

			EventOverviewController eventOverviewController = loader.getController();
			eventOverviewController.setParentController(this);
			eventOverviewController.setParent(eventDetails);
			eventOverviewController.showEventDetails(event);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void closeEventOverview() {
		BorderPane eventDetails = (BorderPane) tmmOverview.lookup("#eventDetails");
		eventDetails.setCenter(null);
	}

//	public void handleDeleteEvent() {
//		int selectedIndex = eventTable.getSelectionModel().getSelectedIndex();
//		eventTable.getItems().remove(selectedIndex);
//	}

	/**
	 * Called when the user clicks the new button. Opens a dialog to edit
	 * details for a new event.
	 */
	@FXML
	private void handleAddEvent() {
		Event tempEvent = new Event();

		boolean okClicked = mainApp.showEventEditDialog(tempEvent);
		if (okClicked) {
			mainApp.getEventData().add(tempEvent);
		}
	}

	public void delete(Event event) {
		eventTable.getItems().remove(event);
	}

	public void edit(Event event) {
		Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
		if (selectedEvent != null) {
			boolean okClicked = mainApp.showEventEditDialog(selectedEvent);
			if (okClicked) {
				showEventDetails(selectedEvent);
			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Event Selected");
			alert.setContentText("Please select an event in the table.");

			alert.showAndWait();
		}
	}
}
