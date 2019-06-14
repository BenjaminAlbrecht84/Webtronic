package model.warehouse;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class Warehouse {

    private String identifier;
    private HashMap<Item, ArrayList<Order>> item2order = new HashMap<>();
    private OrderBins.TIME_INTERVAL timeInterval;
    private SimpleObjectProperty<ArrayList<OrderBins.TimeBin>> bins = new SimpleObjectProperty<>();

    public Warehouse(String identifier) {
        this.identifier = identifier;
    }

    public void addOrder(Item item, Order order) {
        item2order.putIfAbsent(item, new ArrayList<>());
        ArrayList<Order> itemOrders = item2order.get(item);
        int index = Collections.binarySearch(itemOrders, order);
        index = index < 0 ? -index - 1 : index;
        itemOrders.add(index, order);
    }

    public void setUpBins(OrderBins.TIME_INTERVAL t) {
        if (timeInterval != t) {
            timeInterval = t;
            bins.set(OrderBins.binItemStocks(this, t));
        }
    }

    public ArrayList<OrderBins.TimeBin> getBins() {
        return bins.get();
    }

    public SimpleObjectProperty<ArrayList<OrderBins.TimeBin>> binsProperty() {
        return bins;
    }

    public String getIdentifier() {
        return identifier;
    }

    public OrderBins.TIME_INTERVAL getTimeInterval() {
        return timeInterval;
    }

    public HashMap<Item, ArrayList<Order>> getItem2order() {
        return item2order;
    }

}
