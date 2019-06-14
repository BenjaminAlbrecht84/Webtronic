package view.utils;

import javafx.geometry.Dimension2D;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public abstract class MyFilterTableView extends VBox {

    private final static int SPACE = 5;
    private final static int PADDING = 5;

    protected TableView tableView;
    protected TextField filterField = new TextField();
    protected Label filterLabel = new Label();

    public MyFilterTableView(TableView tableView, Pane parentPane) {
        this.tableView = tableView;

        HBox hBox = new HBox(filterField, filterLabel);
        hBox.setSpacing(SPACE);
        getChildren().addAll(hBox, tableView);
        setSpacing(2.5);
        setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));

        filterField.setPromptText("Enter keyword to filter table...");
        filterLabel.setMinWidth(getTextDimension(filterLabel.getText(), Font.getDefault()).getWidth());
        filterLabel.setTextAlignment(TextAlignment.CENTER);
        filterLabel.prefHeightProperty().bind(filterField.heightProperty());
        filterField.prefWidthProperty().bind(widthProperty().subtract(filterLabel.widthProperty().add(SPACE * 2).add(PADDING * 2)));
        filterLabel.setGraphic(IconFactory.createImageView("icons8-filter-filled-50.png", filterField.heightProperty()));

        prefHeightProperty().bind(parentPane.heightProperty());
        prefWidthProperty().bind(parentPane.widthProperty());
        tableView.prefWidthProperty().bind(widthProperty());
        tableView.prefHeightProperty().bind(heightProperty().subtract(filterField.heightProperty().add(SPACE).add(2 * PADDING)));
    }

    private Dimension2D getTextDimension(String s, Font font) {
        Text text = new Text(s);
        text.setFont(font == null ? Font.getDefault() : font);
        return new Dimension2D(text.getBoundsInLocal().getWidth(), text.getBoundsInLocal().getHeight());
    }

}
