package main.java.com.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.com.app.dto.ChildDTO;
import main.java.com.app.dto.FIleDTO;
import main.java.com.app.dto.GroupDTO;
import main.java.com.app.dto.TimeDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class MoveChildController implements Initializable
{
    ObservableList<String> groups = FXCollections.observableArrayList();
    String path = "";
    GroupDTO group = new GroupDTO();
    ChildDTO child = new ChildDTO();
    @FXML
    private Button btn_cancel, btn_move;
    @FXML
    private TextField tf_fio;
    @FXML
    private ComboBox cb_group;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        FIleDTO file = new FIleDTO();
        groups = FXCollections.observableArrayList(file.getGroups());
        cb_group.setItems(groups);
        /* Чтение выбранной группы */
        path = FIleDTO.readFile("temp");
        group = FIleDTO.serReadFile(path.split("@")[0]);
        group.setName(path.split("\\\\")[2]);
        System.out.println(group.getName());
        /* Получение выбранного ребенка */
        child = group.getKid(path.split("@")[1]);

        tf_fio.setText(child.getFio());
        cb_group.getSelectionModel().select(group.getName());
    }

    @FXML
    private void move()
    {
        TimeDTO time=new TimeDTO();
        /* Добавить ребенка в другую группу */
        /* В этой группе его исключить */


        /* Удаление выбранного ребенка */
        String str=path.split("@")[1];
        group.getKid(str).editInfo1(Integer.parseInt(time.getDay()), time.getDaysInChosseMonth(time.getMonth()+" "+time.getYear()));
        group.getKid(str).setStatus(1);
        FIleDTO.serWriteFile(path.split("@")[0], group);
        group=FIleDTO.serReadFile("data\\" + cb_group.getValue() + "\\" + time.getMonth()+" "+time.getYear());
        child.refresh();
        child.editInfo2(Integer.parseInt(time.getDay()));
        child.setStatus(0);
        group.getKids().add(child);
        FIleDTO.serWriteFile( "data\\" + cb_group.getValue() + "\\" + time.getMonth()+" "+time.getYear(),group);

      //  child.editInfo1(Integer.parseInt(time.getDay()), Integer.parseInt(time.getDaysInChosseMonth(time.getMonth())+" "+time.getYear()));
        /* Изменение данных выбранного ребенка */
        //child.setNumber(tf_number.getText().trim());
        //child.setFio(tf_surname.getText()+" "+tf_name.getText()+" "+tf_second_name.getText());
        /* Добавление выбранного ребенка */
        //group.getKids().add(child);



        Alert inf = new Alert(Alert.AlertType.INFORMATION);
        inf.setTitle("Сообщение");
        inf.setHeaderText("Данные изменены успешно.");
        inf.setContentText(null);
        inf.showAndWait();
        cancel();

    }

    @FXML
    private void cancel()
    {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }
}