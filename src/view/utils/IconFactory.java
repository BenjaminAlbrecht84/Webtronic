package view.utils;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class IconFactory {

    public static ImageView createImageView(String name, ReadOnlyDoubleProperty size) {
        ImageView imageView = new ImageView(new Image(IconFactory.class.getResourceAsStream(File.separator + "icons" + File.separator + name)));
        imageView.fitHeightProperty().bind(size.multiply(0.6));
        imageView.fitWidthProperty().bind(size.multiply(0.6));
        return imageView;
    }

}
