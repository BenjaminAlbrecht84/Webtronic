package model;

import io.OrderParser;
import io.WarehouseWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.warehouse.Item;
import model.warehouse.Order;
import model.warehouse.OrderBins;
import model.warehouse.Warehouse;
import presenter.Presenter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Model {

    private Presenter presenter;
    private ObservableMap<String, Warehouse> warehouseMap = FXCollections.observableHashMap();

    public void parseOrders(String orderFile) {
        ArrayList<Object[]> result = OrderParser.run(orderFile);
        for (Object[] o : result) {
            Item item = new Item((String) o[0]);
            String warehouseId = (String) o[1];
            Date deliveryDate = (Date) o[2];
            int itemNumber = (int) o[3];
            Order order = new Order(item, deliveryDate, itemNumber);
            warehouseMap.putIfAbsent(warehouseId, new Warehouse(warehouseId));
            warehouseMap.get(warehouseId).addOrder(item, order);
        }
    }

    public ObservableMap<String, Warehouse> getWarehouseMap() {
        return warehouseMap;
    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public void updateWarehouseBins(String warehouseId, OrderBins.TIME_INTERVAL timeInterval) {
        warehouseMap.get(warehouseId).setUpBins(timeInterval);
    }

    public void exportStocks(File file, OrderBins.TIME_INTERVAL timeInterval) {
        WarehouseWriter.run(file.getAbsolutePath(), new HashMap<>(warehouseMap), timeInterval);
    }

}
