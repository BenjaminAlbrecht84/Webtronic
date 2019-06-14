package view;

import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import model.warehouse.OrderBins;
import model.warehouse.Warehouse;
import presenter.Presenter;
import view.warehouseView.WarehouseView;

public class View {

    private BorderPane root;
    private Presenter presenter;
    private TabPane tabPane;
    private WarehouseView warehouseView;
    private MenuBar menuBar;
    private MenuItem openMenuItem, closeMenuItem, quitMenuItem;

    public View() {

        try {

            FXMLLoader loader = new FXMLLoader(ViewController.class.getResource("View.fxml"));
            loader.load();
            root = loader.getRoot();
            ViewController controller = loader.getController();

            setUpMenu(controller);
            setUpView(controller);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpMenu(ViewController controller) {
        menuBar = controller.menuBar;
        openMenuItem = controller.openMenuItem;
        closeMenuItem = controller.closeMenuItem;
        quitMenuItem = controller.quitMenuItem;
    }

    private void setUpView(ViewController controller) {

        tabPane = controller.tabPane;
        tabPane.prefHeightProperty().bind(root.heightProperty().subtract(menuBar.heightProperty()));
        tabPane.prefWidthProperty().bind(root.widthProperty());

        Tab warehouseTab = new Tab("Lager");
        warehouseView = new WarehouseView(this, tabPane);
        warehouseTab.setContent(warehouseView.getRoot());
        tabPane.getTabs().add(warehouseTab);

    }

    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    public Parent getRoot() {
        System.out.println(root);
        return root;
    }

    public void bindWarehouseMap(ObservableMap<String, Warehouse> warehouseMap) {
        warehouseView.bindWarehouseMap(warehouseMap);
    }

    public MenuItem getOpenMenuItem() {
        return openMenuItem;
    }

    public MenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    public MenuItem getQuitMenuItem() {
        return quitMenuItem;
    }

    public void updateWarehouseBins(String warehouseId, int time) {
        presenter.updateWarehouseBins(warehouseId, time);
    }

    public void exportStocks(int time) {
        presenter.exportStocks(time);
    }
}
