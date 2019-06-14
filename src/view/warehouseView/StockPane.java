package view.warehouseView;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.warehouse.OrderBins;
import model.warehouse.Warehouse;

import java.util.ArrayList;

public class StockPane extends GridPane {

    public StockPane(Warehouse warehouse) {
        setHgap(5);
        setVgap(5);
        setUpListener(warehouse);
    }

    public void setUpListener(Warehouse warehouse) {
        warehouse.binsProperty().addListener((a, b, c) -> {

            getChildren().clear();
            ArrayList<OrderBins.TimeBin> bins = c;
            ArrayList<String> itemIdentifer = c.get(0).getItemIdentifier();
            Font defaultFont = Font.getDefault();
            Font boldFont = Font.font(defaultFont.getName(), FontWeight.BOLD, defaultFont.getSize());

            Label label = new Label("BG-Nummer");
            label.setFont(boldFont);
            add(label, 0, 0);
            GridPane.setHalignment(label, HPos.CENTER);
            for (int i = 1; i < bins.size(); i++) {
                label = new Label(bins.get(i).getTimeStamp());
                label.setFont(boldFont);
                add(label, i, 0);
                GridPane.setHalignment(label, HPos.CENTER);
            }

            int rowIndex = 1;
            for (String itemIdentifier : itemIdentifer) {
                label = new Label(itemIdentifier);
                label.setFont(boldFont);
                add(label, 0, rowIndex);
                GridPane.setHalignment(label, HPos.CENTER);
                for (int i = 1; i < bins.size(); i++) {
                    label = new Label(String.valueOf(bins.get(i).getStock(itemIdentifier)));
                    add(label, i, rowIndex);
                    GridPane.setHalignment(label, HPos.CENTER);
                }
                rowIndex++;
            }

        });
    }

}
