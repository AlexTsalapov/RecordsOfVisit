package main.java.com.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.com.app.dto.FIleDTO;
import main.java.com.app.dto.GroupDTO;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditGroupController implements Initializable
{
    ObservableList<String> groups = FXCollections.observableArrayList();

    @FXML
    private Button btn_cancel;
    @FXML
    private ComboBox cb_group;
    @FXML
    private TextField tf_new_group_name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        FIleDTO file = new FIleDTO();
        groups = FXCollections.observableArrayList(file.getGroups());
        cb_group.setItems(groups);
    }

    @FXML
    private void edit()
    {
        String new_group = tf_new_group_name.getText().trim();
        if (cb_group.getValue() == null) // если не выбран объект для редактирования
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно изменить группу");
            war.setContentText("Выберите название группы.");
            war.showAndWait();
        }
        else if ((new_group.equals("Введите название")) || (new_group.equals("")))
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно изменить группу");
            war.setContentText("Введите название группы.");
            war.showAndWait();
        }
        else if (groups.contains(new_group) && (!new_group.equals(cb_group.getValue()))) // если название занято
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно изменить группу");
            war.setContentText("Группа с данным именем уже существует. Введите другое название.");
            war.showAndWait();
        }
        else
        {
            Alert con = new Alert(Alert.AlertType.CONFIRMATION);
            con.setTitle("Подтверждение действия");
            con.setHeaderText("Вы уверены что хотите изменить название группы \""+cb_group.getValue()+"\" на \""+new_group+"\"?");
            ButtonType yes_btn = new ButtonType("Изменить");
            ButtonType no_btn = new ButtonType("Отмена");
            con.getButtonTypes().clear();
            con.getButtonTypes().addAll(yes_btn, no_btn);
            Optional<ButtonType> option = con.showAndWait();
            if (option.get() == yes_btn)
            {
                GroupDTO group=new GroupDTO();
                FIleDTO file=new FIleDTO();
               ArrayList<String> rename=file.getAllDates((String) cb_group.getValue());
                FIleDTO.renameDir("data\\"+cb_group.getValue(), "data\\"+new_group);
               for(String str:rename)
               {
                 group= FIleDTO.serReadFile("data\\"+new_group+"\\"+str);
                 group.setName(new_group);
                 FIleDTO.serWriteFile("data\\"+new_group+"\\"+str,group);
               }


                Alert inf = new Alert(Alert.AlertType.INFORMATION);
                inf.setTitle("Сообщение");
                inf.setHeaderText("Группа \""+new_group+"\" переименована успешно");
                inf.setContentText(null);
                inf.showAndWait();
                cancel();
            }
        }
    }

    @FXML
    private void cancel()
    {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
}