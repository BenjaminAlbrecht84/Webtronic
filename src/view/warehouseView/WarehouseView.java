package view.warehouseView;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.warehouse.Warehouse;
import view.View;
import view.utils.IconFactory;

import java.util.HashMap;

public class WarehouseView {

    private BorderPane root, stockPane;
    private HBox toolbar;
    private SplitPane splitPane;
    private Pane warehousePane;
    private WarehouseTable warehouseTable;
    private HashMap<String, StockPane> warehouse2stockPane = new HashMap<>();
    private View view;
    private Button exportButton;
    private ComboBox<String> timeBox;
    private ObservableList<String> timeList = FXCollections.observableArrayList("Tage", "Wochen", "Monate", "Jahre");

    public WarehouseView(View view, TabPane tabPane) {

        this.view = view;

        try {

            FXMLLoader loader = new FXMLLoader(WarehouseController.class.getResource("Warehouse.fxml"));
            loader.load();
            root = loader.getRoot();
            WarehouseController controller = loader.getController();

            toolbar = controller.toolbar;
            splitPane = controller.splitPane;
            root.prefHeightProperty().bind(tabPane.heightProperty());
            root.prefWidthProperty().bind(tabPane.widthProperty());

            setUpView();
            setUpActions();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpActions() {
        warehouseTable.selectedIDProperty().addListener((observable, oldValue, newValue) -> showWarehouseStock());
        exportButton.setOnAction(a -> view.exportStocks(getTimeInterval()));
        timeBox.setOnAction(a -> showWarehouseStock());
    }

    public void showWarehouseStock() {
        String identifier = warehouseTable.getSelectedID();
        if (identifier != null) {
            view.updateWarehouseBins(identifier, getTimeInterval());
            warehouse2stockPane.get(identifier).setContent(stockPane, splitPane);
        }
    }

    public void bindWarehouseMap(ObservableMap<String, Warehouse> warehouseMap) {
        warehouseMap.addListener((MapChangeListener<String, Warehouse>) change -> {
            if (change.wasAdded()) {
                String id = change.getKey();
                warehouseTable.addWarehouseID(id);
                warehouse2stockPane.putIfAbsent(id, new StockPane(change.getValueAdded()));
            } else if (change.wasRemoved()) {
                String id = change.getKey();
                warehouseTable.removeWarehouseID(change.getKey());
                warehouse2stockPane.remove(id);
            }
        });
    }

    private void setUpView() {

        warehousePane = new Pane();
        warehouseTable = new WarehouseTable(warehousePane, this);
        warehousePane.getChildren().add(warehouseTable);

        stockPane = new BorderPane();
        stockPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        splitPane.getItems().setAll(warehousePane, stockPane);
        splitPane.setDividerPositions(0.1);

        toolbar.setPadding(new Insets(5, 5, 5, 5));
        toolbar.setSpacing(5);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        exportButton = new Button("");
        exportButton.setPrefHeight(30);
        exportButton.setGraphic(IconFactory.createImageView("icons8-save-filled-50.png", exportButton.heightProperty()));
        toolbar.getChildren().add(exportButton);

        timeBox = new ComboBox<>();
        timeBox.setItems(timeList);
        timeBox.getSelectionModel().select(1);
        toolbar.getChildren().add(timeBox);

    }

    public Parent getRoot() {
        return root;
    }

    private int getTimeInterval() {
        return timeBox.getSelectionModel().getSelectedIndex();
    }

}
