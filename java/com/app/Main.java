package main.java.com.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.com.app.dto.FIleDTO;
import main.java.com.app.dto.GroupDTO;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage new_stage) throws IOException {
    //    FIleDTO.serWriteFile("data\\1\\Сентябрь 2022", (new GroupDTO()));
        Stage stage = new_stage;
        stage.setTitle("A-vit");
        stage.getIcons().add(new Image("main/resources/com/image/app.png"));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setMaximized(true);
        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/com/app/start.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        root = FXMLLoader.load(getClass().getResource("../../../resources/com/app/application.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void createStage(String fxml) {
        Stage stage = new Stage();
        if (fxml.equals("new_group")) {
            stage.setTitle("Создание группы");
            stage.getIcons().add(new Image("main/resources/com/image/plus.png"));
        } else if (fxml.equals("edit_group")) {
            stage.setTitle("Редактирование группы");
            stage.getIcons().add(new Image("main/resources/com/image/edit.png"));
        } else if (fxml.equals("del_group")) {
            stage.setTitle("Удаление группыjava - ");
            stage.getIcons().add(new Image("main/resources/com/image/cancel.png"));
        } else if (fxml.equals("new_child")) {
            stage.setTitle("Создание ребенка");
            stage.getIcons().add(new Image("main/resources/com/image/plus.png"));
        } else if (fxml.equals("edit_child")) {
            stage.setTitle("Редактирование информации о ребенке");
            stage.getIcons().add(new Image("main/resources/com/image/edit.png"));
        }
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("../../../resources/com/app/" + fxml + ".fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }
}