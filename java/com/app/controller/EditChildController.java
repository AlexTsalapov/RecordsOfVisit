package main.java.com.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.com.app.dto.ChildDTO;
import main.java.com.app.dto.FIleDTO;
import main.java.com.app.dto.GroupDTO;

import java.net.URL;
import java.util.ResourceBundle;

public class EditChildController implements Initializable
{
    String path = "";
    GroupDTO group = new GroupDTO();
    ChildDTO child = new ChildDTO();
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
        /* Чтение выбранной группы */
        path = FIleDTO.readFile("temp");
        group = FIleDTO.serReadFile(path.split("@")[0]);
        /* Получение выбранного ребенка */
        child = group.getKid(path.split("@")[1]);
        tf_number.setText(child.getNumber());
        tf_name.setText(child.getName());
        tf_second_name.setText(child.getSecond_name());
        tf_surname.setText(child.getSurname());
    }

    @FXML
    private void create()
    {
        if ((tf_number.getText().trim().length() == 0) || (tf_surname.getText().trim().length() == 0) || (tf_name.getText().trim().length() == 0) || (tf_second_name.getText().trim().length() == 0))
        {
            Alert war = new Alert(Alert.AlertType.WARNING);
            war.setTitle("Ошибка");
            war.setHeaderText("Невозможно сохранить изменения.");
            war.setContentText("Заполните все поля.");
            war.showAndWait();
        }
        else if (group.getNumbers().contains(Integer.parseInt(tf_number.getText())) && !(tf_number.getText().equals(child.getNumber())))
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
        else if ((group.getFIO().contains((tf_surname.getText().trim()+" "+tf_name.getText().trim()+" "+tf_second_name.getText().trim()).toLowerCase())) && (!child.getFio().toLowerCase().equals((tf_surname.getText().trim()+" "+tf_name.getText().trim()+" "+tf_second_name.getText().trim()).toLowerCase())))
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
            /* Удаление выбранного ребенка */
            group.remove(group.getKids().indexOf(child));
            /* Изменение данных выбранного ребенка */
            child.setNumber(tf_number.getText().trim());
            child.setFio(tf_surname.getText()+" "+tf_name.getText()+" "+tf_second_name.getText());
            /* Добавление выбранного ребенка */
            group.getKids().add(child);
            FIleDTO.serWriteFile(path.split("@")[0], group);
            Alert inf = new Alert(Alert.AlertType.INFORMATION);
            inf.setTitle("Сообщение");
            inf.setHeaderText("Данные изменены успешно.");
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