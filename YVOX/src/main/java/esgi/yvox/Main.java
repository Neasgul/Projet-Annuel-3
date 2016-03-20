package esgi.yvox;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Created by Benoit on 17/03/2016.
 */
public class Main extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("YVOX Voice Controller : start state");
        stage.getIcons().add(new Image("file:img/logo_icone.png"));
        Rectangle2D mainscreen = Screen.getPrimary().getVisualBounds();
        stage.setWidth(mainscreen.getWidth() * 0.75);
        stage.setHeight(mainscreen.getHeight() * 0.75);

        StackPane sp = new StackPane();
        Image imgmic = new Image("file:img/mic.png");
        ImageView imgview = new ImageView(imgmic);
        sp.getChildren().add(imgview);
        Scene scene = new Scene(sp,imgmic.getWidth(),imgmic.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    void sphinxConfiguration(){

    }
}
