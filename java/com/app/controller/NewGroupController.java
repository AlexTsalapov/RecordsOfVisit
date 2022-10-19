package main.java.com.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.com.app.dto.FIleDTO;
import main.java.com.app.dto.GroupDTO;
import main.java.com.app.dto.TimeDTO;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class NewGroupController implements Initializable
{
    ArrayList<String> groups = new ArrayList<>();
    @FXML
    private Button btn_cancel;
    @FXML
    private TextField tf_group_name;
    @FXML
    private RadioButton rbtn_garten;
    @FXML
    private RadioButton rbtn_manger;
    ToggleGroup group;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        group = new ToggleGroup();
        rbtn_manger.setToggleGroup(group);
        rbtn_garten.setToggleGroup(group);

        FIleDTO file = new FIleDTO();
        groups = file.getGroups();
        if (groups.size() == 0)
        {
            btn_cancel.setVisible(false);
        }
    }

    @FXML
    private void create()
    {
        String new_group = tf_group_name.getText().trim();
        String type = ((RadioButton)group.getSelectedToggle()).getText();
        TimeDTO date = new TimeDTO();
        if ((new_group.equals("Введите название")) || (new_group.equals(""))) // если название не введено
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить имя группы");
            war.setContentText("Введите название группы.");
            war.showAndWait();
        }
        else if (type == null){
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить тип группы");
            war.setContentText("Выберите тип группы.");
            war.showAndWait();
        }
        else if (groups.contains(new_group)) // если название занято
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить имя группы");
            war.setContentText("Группа с данным именем уже существует. Введите другое название.");
            war.showAndWait();
        }
        else
        {
            FIleDTO.createDir("data\\"+new_group);
            FIleDTO.createFile("data\\"+new_group+"\\"+date.getMonth()+" "+date.getYear());
            /* Запись группы выбранного месяца в файл */
            if (type.equals("Ясли")){
                FIleDTO.serWriteFile("data\\"+new_group+"\\"+date.getMonth()+" "+date.getYear(), new GroupDTO(new_group, FXCollections.observableArrayList(), true, TimeDTO.getDaysInMonth()));
            }
            else {
                FIleDTO.serWriteFile("data\\"+new_group+"\\"+date.getMonth()+" "+date.getYear(), new GroupDTO(new_group, FXCollections.observableArrayList(), false, TimeDTO.getDaysInMonth()));
            }
            Alert inf = new Alert(Alert.AlertType.INFORMATION);
            inf.setTitle("Сообщение");
            inf.setHeaderText("Группа \""+new_group+"\" создана успешно");
            inf.setContentText(null);
            inf.showAndWait();
            cancel();

        }
    }

    @FXML
    private void cancel()
    {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
}