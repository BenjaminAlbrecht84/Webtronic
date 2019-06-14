package view;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;

public class ViewController {

    @FXML
    protected TabPane tabPane;

    @FXML
    public MenuBar menuBar;
    @FXML
    protected MenuItem openMenuItem;
    @FXML
    protected MenuItem closeMenuItem;
    @FXML
    protected MenuItem quitMenuItem;
}
