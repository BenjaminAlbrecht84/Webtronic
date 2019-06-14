package view.warehouseView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import model.warehouse.Warehouse;
import view.utils.MyFilterTableView;

public class WarehouseTable extends MyFilterTableView {

    private ObservableList<WarehouseInfo> warehouseIDs = FXCollections.observableArrayList();
    private SimpleStringProperty selectedID = new SimpleStringProperty();

    public WarehouseTable(Pane parentPane, WarehouseView warehouseView) {
        super(new TableView<WarehouseInfo>(), parentPane);

        TableColumn<WarehouseInfo, String> idColumn = new TableColumn<>("Lager");
        tableView.getColumns().setAll(idColumn);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("identifier"));
        idColumn.setStyle("-fx-alignment: CENTER;");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getSelectionModel().selectedItemProperty().addListener((ChangeListener<WarehouseInfo>) (observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedID.set(newValue.getIdentifier());
            }
        });

        FilteredList<WarehouseInfo> filteredList = new FilteredList<>(warehouseIDs, p -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(info -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String keyword = newValue.toLowerCase();
                if (info.getIdentifier().toLowerCase().contains(keyword))
                    return true;
                return false;
            });
        });
        SortedList<WarehouseInfo> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);


    }

    public String getSelectedID() {
        return selectedID.get();
    }

    public SimpleStringProperty selectedIDProperty() {
        return selectedID;
    }

    public void addWarehouseID(String id) {
        warehouseIDs.add(new WarehouseInfo(id));
    }

    public void removeWarehouseID(String id) {
        warehouseIDs.remove(new WarehouseInfo(id));
    }

    public class WarehouseInfo {

        private String identifier;

        public WarehouseInfo(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;

        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof WarehouseInfo)
                return ((WarehouseInfo) obj).getIdentifier().equals(identifier);
            return false;
        }
    }
}
