package presenter;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Model;
import model.warehouse.OrderBins;
import view.View;

import java.io.File;

public class Presenter {

    private View view;
    private Model model;
    private Stage mainStage;

    public Presenter(View view, Model model) {
        this.view = view;
        this.model = model;
        setUpActions();
        setUpBindings();
    }

    private void setUpBindings() {
        view.bindWarehouseMap(model.getWarehouseMap());
    }


    private void setUpActions() {
        view.getQuitMenuItem().setOnAction(a -> Platform.exit());
        view.getOpenMenuItem().setOnAction(a -> openFileDialog());
    }

    private void openFileDialog() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(mainStage);
        if (file != null)
            model.parseOrders(file.getAbsolutePath());
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public void updateWarehouseBins(String warehouseId, int time) {
        model.updateWarehouseBins(warehouseId, getTimeInterval(time));
    }

    public void exportStocks(int time) {
        File file = setUpSaveDialog("Sicherung Lagerbestand");
        if (file != null)
            model.exportStocks(file, getTimeInterval(time));
    }

    public File setUpSaveDialog(String title) {
        String extension = "xlsx";
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel file(*." + extension + ")", "*." + extension));
        File file = chooser.showSaveDialog(mainStage);
        if (file != null && !file.getName().endsWith("." + extension)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filename Exception");
            alert.setContentText("Filename has to end with ." + extension + "!");
            return null;
        }
        return file;
    }

    private OrderBins.TIME_INTERVAL getTimeInterval(int time) {
        switch (time) {
            case 0:
                return OrderBins.TIME_INTERVAL.DAY;
            case 1:
                return OrderBins.TIME_INTERVAL.WEEK;
            case 2:
                return OrderBins.TIME_INTERVAL.MONTH;
        }
        return OrderBins.TIME_INTERVAL.YEAR;
    }

}
