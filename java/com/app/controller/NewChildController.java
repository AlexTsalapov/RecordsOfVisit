package main.java.com.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.com.app.dto.ChildDTO;
import main.java.com.app.dto.FIleDTO;
import main.java.com.app.dto.GroupDTO;
import main.java.com.app.dto.TimeDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class NewChildController implements Initializable
{
    String path = "";
    GroupDTO group = new GroupDTO();
    @FXML
    private Button btn_cancel;
    @FXML
    private TextField tf_number;
    @FXML
    private TextField tf_surname;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_second_name;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        path = FIleDTO.readFile("temp");
        group = FIleDTO.serReadFile(path.split("@")[0]);
    }

    @FXML
    public void create()
    {
        if ((tf_number.getText().trim().equals("Введите номер")) || (tf_number.getText().trim().length() == 0) ||(tf_surname.getText().trim().equals("Введите фамилию")) || (tf_surname.getText().trim().length() == 0) || (tf_name.getText().trim().equals("Введите имя")) || (tf_name.getText().trim().length() == 0) || (tf_second_name.getText().trim().equals("Введите отчество")) || (tf_second_name.getText().trim().length() == 0))
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно добавить ребенка");
            war.setContentText("Заполните все поля.");
            war.showAndWait();
        }
        else if (group.getNumbers().contains(Integer.parseInt(tf_number.getText()))) // если данный номер уже есть в группе
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить номер ребенка");
            war.setContentText("Ребенок с данным номером уже существует. Введите другой номер.");
            war.showAndWait();
        }
        else if ((!tf_number.getText().matches("[0-9]+")) || (tf_number.getText().length() > 3))
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить номер ребенка");
            war.setContentText("Используйте для ввода номера цифры. Число не должно превышать 999.");
            war.showAndWait();
        }
        else if (group.getFIO().contains((tf_surname.getText().trim()+" "+tf_name.getText().trim()+" "+tf_second_name.getText().trim()).toLowerCase()))
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить имя ребенка");
            war.setContentText("Ребенок с данным именем уже существует. Введите другое имя.");
            war.showAndWait();
        }
        else if ((tf_surname.getText()+" "+tf_name.getText()+" "+tf_second_name.getText()).split(" ").length != 3)
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить имя ребенка");
            war.setContentText("Введите фамилию, имя и отчество ребенка (3 слова)");
            war.showAndWait();
        }
        else if (!(tf_surname.getText()+" "+tf_name.getText()+" "+tf_second_name.getText()).matches("[а-яА-Я -]+")) {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно установить имя ребенка");
            war.setContentText("Используйте для ввода буквы русского алфавита, символы \"-\" и \" \"");
            war.showAndWait();
        }
        else
        {
            TimeDTO time = new TimeDTO();
            ChildDTO child = new ChildDTO(tf_number.getText().trim(), tf_surname.getText().trim(), tf_name.getText().trim(), tf_second_name.getText().trim(),0, time.getDaysInChosseMonth(path.split("@")[1]), time.getWeekends(path.split("@")[1]));
            System.out.println(path);
            group.getKids().add(child);
            FIleDTO.serWriteFile(path.split("@")[0], group);
            Alert inf = new Alert(Alert.AlertType.INFORMATION);
            inf.setTitle("Сообщение");
            inf.setHeaderText(child.getFio() + " добавлен(а) успешно.");
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