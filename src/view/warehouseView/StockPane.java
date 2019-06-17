package view.warehouseView;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.warehouse.OrderBins;
import model.warehouse.Warehouse;

import java.util.ArrayList;

public class StockPane {

    private GridPane topGridPane = new GridPane(), leftGridPane = new GridPane(), centerGridPane = new GridPane();
    private ScrollPane topScrollPane, centerScrollPane, leftScrollPane;
    private ScrollBar bottomScrollBar = new ScrollBar(), rightScrollBar = new ScrollBar();
    private HBox fillBox = new HBox();

    public StockPane(Warehouse warehouse) {

        centerGridPane.setHgap(5);
        centerGridPane.setVgap(5);
        topGridPane.setHgap(5);
        topGridPane.setVgap(5);
        leftGridPane.setHgap(5);
        leftGridPane.setVgap(5);

        topScrollPane = new ScrollPane(topGridPane);
        centerScrollPane = new ScrollPane(centerGridPane);
        leftScrollPane = new ScrollPane(leftGridPane);

        topScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        centerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        centerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        leftScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        topScrollPane.hvalueProperty().bindBidirectional(centerScrollPane.hvalueProperty());
        leftScrollPane.vvalueProperty().bindBidirectional(centerScrollPane.vvalueProperty());

        topScrollPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        leftScrollPane.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        centerScrollPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        rightScrollBar.minProperty().bindBidirectional(centerScrollPane.vminProperty());
        rightScrollBar.maxProperty().bindBidirectional(centerScrollPane.vmaxProperty());
        rightScrollBar.valueProperty().bindBidirectional(centerScrollPane.vvalueProperty());
        rightScrollBar.setOrientation(Orientation.VERTICAL);
        bottomScrollBar.minProperty().bindBidirectional(centerScrollPane.hminProperty());
        bottomScrollBar.maxProperty().bindBidirectional(centerScrollPane.hmaxProperty());
        bottomScrollBar.valueProperty().bindBidirectional(centerScrollPane.hvalueProperty());
        bottomScrollBar.setOrientation(Orientation.HORIZONTAL);

        fillBox.prefWidthProperty().bind(leftScrollPane.widthProperty());

        setUpListener(warehouse);
    }

    public void setUpListener(Warehouse warehouse) {
        warehouse.binsProperty().addListener((a, b, c) ->
                setupGridPane(c)
        );
    }

    private void setupGridPane(ArrayList<OrderBins.TimeBin> bins) {

        ArrayList<String> itemIdentifiers = bins.get(0).getItemIdentifier();
        ArrayList<ReadOnlyDoubleProperty> columnWidths = new ArrayList<>();
        Font defaultFont = Font.getDefault();
        Font boldFont = Font.font(defaultFont.getName(), FontWeight.BOLD, defaultFont.getSize());

        topGridPane.getChildren().clear();
        Label label = new Label("BG-Nummer");
        label.setFont(boldFont);
        columnWidths.add(label.widthProperty());
        topGridPane.add(label, 0, 0);
        GridPane.setHalignment(label, HPos.LEFT);
        for (int i = 1; i < bins.size(); i++) {
            label = new Label(bins.get(i).getTimeStamp());
            label.setFont(boldFont);
            topGridPane.add(label, i, 0);
            GridPane.setHalignment(label, HPos.LEFT);
            columnWidths.add(label.widthProperty());
        }

        centerGridPane.getChildren().clear();
        int rowIndex = 1;
        for (int j = 0; j < itemIdentifiers.size(); j++) {
            String itemIdentifier = itemIdentifiers.get(j);
            label = new Label(itemIdentifier);
            label.setFont(boldFont);
            label.prefWidthProperty().bind(columnWidths.get(0));
            ReadOnlyDoubleProperty prefHeight = label.prefHeightProperty();
            leftGridPane.add(label, 0, rowIndex);
            GridPane.setHalignment(label, HPos.LEFT);
            for (int i = 1; i < bins.size(); i++) {
                label = new Label(String.valueOf(bins.get(i).getStock(itemIdentifier)));
                label.prefWidthProperty().bind(columnWidths.get(i));
                label.prefHeightProperty().bind(prefHeight);
                centerGridPane.add(label, i - 1, rowIndex);
                GridPane.setHalignment(label, HPos.LEFT);
            }
            rowIndex++;
        }
    }

    public void setContent(BorderPane borderPane, SplitPane parentPane) {
        borderPane.setTop(topScrollPane);
        borderPane.setCenter(centerScrollPane);
        borderPane.setLeft(leftScrollPane);
        borderPane.setBottom(new HBox(fillBox, bottomScrollBar));
        borderPane.setRight(rightScrollBar);
        bottomScrollBar.prefWidthProperty().bind(borderPane.widthProperty().subtract(fillBox.widthProperty()));
        borderPane.maxHeightProperty().bind(parentPane.heightProperty().subtract(100));
        topScrollPane.maxWidthProperty().bind(borderPane.widthProperty().subtract(rightScrollBar.widthProperty()));
    }
}
