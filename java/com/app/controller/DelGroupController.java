package main.java.com.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.com.app.dto.FIleDTO;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DelGroupController implements Initializable
{
    ObservableList<String> groups = FXCollections.observableArrayList();

    @FXML
    private Button btn_cancel;
    @FXML
    private ComboBox cb_group;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        FIleDTO file = new FIleDTO();
        groups = FXCollections.observableArrayList(file.getGroups());
        cb_group.setItems(groups);
    }

    @FXML
    private void delete()
    {
        if (cb_group.getValue() == null) // если не выбран объект для удаления
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно удалить группу");
            war.setContentText("Выберите название группы.");
            war.showAndWait();
        }
        else
        {
            Alert con = new Alert(Alert.AlertType.CONFIRMATION);
            con.setTitle("Подтверждение действия");
            con.setHeaderText("При удалении сотрется вся информация об этой группе во всех месяцах, вы уверены что хотите удалить группу \""+cb_group.getValue()+"\"?");

            ButtonType yes_btn = new ButtonType("Удалить");
            ButtonType no_btn = new ButtonType("Отмена");
            con.getButtonTypes().clear();
            con.getButtonTypes().addAll(yes_btn, no_btn);
            Optional<ButtonType> option = con.showAndWait();
            if (option.get() == yes_btn)
            {

               if( FIleDTO.deleteDir("data\\"+cb_group.getValue())) {


                   FIleDTO.writeFile("data\\info", "0");
                   Alert inf = new Alert(Alert.AlertType.INFORMATION);
                   inf.setTitle("Сообщение");
                   inf.setHeaderText("Группа \"" + cb_group.getValue() + "\" удалена успешно");
                   inf.setContentText(null);
                   inf.showAndWait();
                   cancel();
               }
               else {
                   notDelete();
               }
            }
        }
    }

    @FXML
    private void cancel()
    {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
    private void notDelete()
    {

            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно удалить группу");
            war.setContentText("Выйдите из диалоговых окон и попробуйте снова");
            war.showAndWait();
        }
}