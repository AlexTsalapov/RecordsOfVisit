package main.java.com.app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import main.java.com.app.Main;
import main.java.com.app.dto.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.FileNotFoundException;
import java.net.URL;

import javafx.event.EventHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class AppController implements Initializable {
    GroupDTO group;
    ObservableList<String> all_groups;
    ObservableList<String> group_months;
    final String info_path = "data\\info";
    @FXML
    private ComboBox<String> cb_month;
    @FXML
    private ComboBox<String> cb_group;
    @FXML
    private TableView<ChildDTO> tv_childrens;
    @FXML
    private Button btn_print_group;
    ContextMenu cm_fio;
    ContextMenu cm_create;
    private boolean flag = false;

    @FXML
    private ComboBox<String> cb_report_month;
    @FXML
    private TableView<GroupDTO> tv_report_groups;
    @FXML
    private TableView<TypeDTO> tv_report_categories;
    @FXML
    private Button btn_print_total;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Создание выпадающего списка с группами */
        createCBGroup();
        createCBReportMonth();
        /* Если группы есть */
        if (all_groups.size() > 0) {
            createCBMonth();
            createTVChildrens();
            createTVReportGroups();
            createTVReportCategories();
        }
    }


    @FXML
    private void createCBGroup() /* Заполнение списка групп */ {
        /* Получение списка групп */
        FIleDTO file = new FIleDTO();
        all_groups = FXCollections.observableArrayList(file.getGroups());
        ObservableList<String> items = FXCollections.observableArrayList(all_groups);
        /* Если группы есть */
        if (all_groups.size() > 0) {
            /* Добавление пунктов в меню */
            items.add("Редактировать группу");
            items.add("Удалить группу");
        }
        items.add("Создать группу");
        cb_group.setItems(items);
        /* Если группы есть */
        if (all_groups.size() > 0) {
            /* Выбрать группу согласно номеру, записанному в файле info */
            cb_group.getSelectionModel().select(Integer.parseInt(FIleDTO.readFile(info_path)));
        } else {
            /* Оставить выбор группы пустым */
            cb_group.getSelectionModel().select(null);
        }
    }

    @FXML
    private void actionCBGroup(ActionEvent actionEvent) /* Действие при выборе группы */ {
        if (cb_group.getValue() == null) {
            /* Установить список месяцев на пустой */
            cb_month.setItems(FXCollections.observableArrayList());
            cb_month.getSelectionModel().select(null);
        } else if (cb_group.getValue().equals("Редактировать группу")) {
            /* Установить список месяцев на пустой */
            cb_month.setItems(FXCollections.observableArrayList());
            cb_month.getSelectionModel().select(null);
            /* Открытие окна редактирования группы */
            Main.createStage("edit_group");
            recreateWindow();
        } else if (cb_group.getValue().equals("Удалить группу")) {
            /* Установить список месяцев на пустой */
            cb_month.setItems(FXCollections.observableArrayList());
            cb_month.getSelectionModel().select(null);
            /* Открытие окна удаления группы */
            Main.createStage("del_group");
            recreateWindow();
        } else if (cb_group.getValue().equals("Создать группу")) {
            /* Установить список месяцев на пустой */
            cb_month.setItems(FXCollections.observableArrayList());
            cb_month.getSelectionModel().select(null);
            /* Открытие окна создания группы */
            Main.createStage("new_group");
            recreateWindow();
        } else {
            /* Запись в файл info номер выбранной группы */
            FIleDTO.writeFile(info_path, "" + cb_group.getItems().indexOf(cb_group.getValue()));
            recreateWindow();
        }
    }


    @FXML
    private void createCBMonth() /* Заполнение списка месяцев */ {
        TimeDTO time = new TimeDTO();
        FIleDTO file = new FIleDTO();
        /* Получение списка месяцев для выбранной группы */
        group_months = FXCollections.observableArrayList(file.getDates(cb_group.getValue()));
        ObservableList<String> months = FXCollections.observableArrayList(group_months);
        cb_month.setItems(months);
        cb_month.getSelectionModel().select(time.getMonth() + " " + time.getYear());
    }

    @FXML
    private void actionCBMonth(ActionEvent actionEvent) /* Действие при выборе месяца */ {
        if (cb_month.getValue() != null) {
            createTVChildrens();
        }
    }
    @FXML
    private  void refreshGroups() /* Заполнение таблицы колонками */ {
        GroupDTO temp=new GroupDTO();

        FIleDTO file=new FIleDTO();
        file.deleteOldFile();
        all_groups= FXCollections.observableArrayList(file.getGroups());
        for (int i = 0; i < all_groups.size(); i++) {
            TimeDTO time = new TimeDTO();
            group = FIleDTO.serReadFile("data\\" + all_groups.get(i) + "\\" + cb_month.getValue());
            if ( (Files.exists(Path.of("data\\" + all_groups.get(i) + "\\" + time.getPrevMonthWithYear())))) {
                /* Получение группы прошлого месяца */
               temp = FIleDTO.serReadFile("data\\" + all_groups.get(i) + "\\" + time.getPrevMonthWithYear());
                /* Запись актуальных детей в выбранный месяц */
                 temp.setKids(temp.getActualKids(time.getDaysInChosseMonth(cb_month.getValue()), time.getWeekends(cb_month.getValue())));
                temp.setManger(temp.isManger());
                /* Запись группы выбранного месяца в файл */
                FIleDTO.serWriteFile("data\\" + all_groups.get(i) + "\\" + cb_month.getValue(), temp);

            }
            else
            {
                String s= FIleDTO.getDirContent("data\\"+all_groups.get(i)).get(1);
                temp = FIleDTO.serReadFile(  s);

               temp.setKids(temp.getActualKids(time.getDaysInChosseMonth(cb_month.getValue()), time.getWeekends(cb_month.getValue())));
                temp.setManger(temp.isManger());
                /* Запись группы выбранного месяца в файл */
                FIleDTO.serWriteFile("data\\" + all_groups.get(i) + "\\" + cb_month.getValue(), temp);

            }
        }
        group = FIleDTO.serReadFile("data\\"+ cb_group.getValue() + "\\" + cb_month.getValue());

    }
    @FXML
    private void createTVChildrens() /* Заполнение таблицы колонками */ {
        /* Чтение списка детей выбранной группы выбранного месяца */
        TimeDTO time = new TimeDTO();
        FIleDTO fIleDTO=new FIleDTO();
        group = FIleDTO.serReadFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue());
        /* Если дети отсутствуют и при этом есть файл с предшествующим месяцем то*/

        if ((group.getKids().size() == 0) &&( (Files.exists(Path.of("data\\" + cb_group.getValue() + "\\" + time.getPrevMonthWithYear())))||fIleDTO.getAllDates(group.getName()).size()!=1)) {
            /* Получение группы прошлого месяца */
         refreshGroups();
            group = FIleDTO.serReadFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue());
        }

        /* Очистка таблицы и ее повторное заполнение */
        tv_childrens.getColumns().clear();
        tv_childrens.getColumns().addAll(createNumberColumn(), createFIOColumn());
        createDateColumns();
        createContextMenu();
        actionTVChildrens();
        reloadTVChildren();
    }

    @FXML
    private void reloadTVChildren() /* Заполнение таблицы данными */ {
        /* Создание копии детей*/
        TimeDTO time = new TimeDTO();
        ObservableList<ChildDTO> copy_kids = FXCollections.observableArrayList(group.getKids());
        /* Если дети есть то */
        if (copy_kids.size() > 0) {
            /* Добавить пустую строку и строку с результатом */
            copy_kids.add(new ChildDTO(time.getDaysInChosseMonth(cb_month.getValue())));
            copy_kids.add(new ChildDTO(group.getAllTotal(cb_month.getValue())));
        }
        /* Очистка таблицы и запись копии детей*/
        tv_childrens.getItems().clear();
        tv_childrens.setItems(copy_kids);
        // P.S. копия детей используется для того чтобы не было "двойного" доступа к памяти
    }

    @FXML
    private TableColumn<ChildDTO, String> createNumberColumn() /* Создание колонки "№" */ {
        TableColumn<ChildDTO, String> number_column = new TableColumn<>();
        number_column.setCellValueFactory(new PropertyValueFactory<ChildDTO, String>("number"));
        number_column.setText("№");
        number_column.getStyleClass().add("table-column-center");
        return number_column;
    }

    @FXML
    private TableColumn<ChildDTO, String> createFIOColumn() /* Создание колонки "ФИО" */ {
        TableColumn<ChildDTO, String> fio_column = new TableColumn<>();
        fio_column.setCellValueFactory(new PropertyValueFactory<ChildDTO, String>("fio"));
        fio_column.setText("ФИО");
        return fio_column;
    }

    @FXML
    private void createDateColumns() /* Создание колонок с датами */ {
        TimeDTO time = new TimeDTO();
        /* Перебор дней в выбранном месяце */
        for (int i = 0; i < time.getDaysInChosseMonth(cb_month.getValue()); i++) {
            TableColumn<ChildDTO, String> temp = new TableColumn<>();
            temp.setText((i + 1) + "");
            if (time.getWeekends(cb_month.getValue()).contains(i + 1)) {
                temp.setStyle("-fx-text-fill: red");
            }
            temp.setSortable(false);
            temp.getStyleClass().add("table-column-center");
            temp.setCellValueFactory(new PropertyValueFactory<ChildDTO, String>("info"));
            tv_childrens.getColumns().add(temp);
        }
    }

    @FXML
    private void createContextMenu() /* Создание контекстного меню таблицы */ {
        cm_fio = new ContextMenu();
        cm_fio.getItems().addAll(menuItemEdit(), menuItemMove(), menuItemDelete(), menuItemCreate());
        cm_create = new ContextMenu();
        cm_create.getItems().add(menuItemCreate());
    }

    @FXML
    private MenuItem menuItemEdit() /* Создание пункта "Изменить" контекстного меню */ {
        MenuItem edit = new MenuItem("Изменить");
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /* Cоздание временного файла для переноса данных в другой контроллер */
                FIleDTO.createFile("temp");
                /* Запись номера выбранного ребенка во временный файл */
                FIleDTO.writeFile("temp", "data" + "\\" + "\\" + cb_group.getValue() + "\\" + "\\" + cb_month.getValue() + "@" + tv_childrens.getSelectionModel().getSelectedItem().getNumber());
                /* Открытие окна редактирования ребенка */
                Main.createStage("edit_child");
                /* Чтение списка детей выбранной группы выбранного месяца */
                group.setKids((FIleDTO.serReadFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue())).getKids());
                reloadTVChildren();
            }
        });
        return edit;
    }

    @FXML
    private MenuItem menuItemMove() /* Создание пункта "Переместить" контекстного меню */{
        MenuItem move = new MenuItem("Переместить");
        move.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /* Cоздание временного файла для переноса данных в другой контроллер */
                FIleDTO.createFile("temp");
                /* Запись номера выбранного ребенка во временный файл */
                FIleDTO.writeFile("temp", "data" + "\\" + "\\" + cb_group.getValue() + "\\" + "\\" + cb_month.getValue() + "@" + tv_childrens.getSelectionModel().getSelectedItem().getNumber());
                /* Открытие окна перемещения ребенка */
                Main.createStage("move_child");
                /* Чтение списка детей выбранной группы выбранного месяца */
                group.setKids((FIleDTO.serReadFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue())).getKids());
                reloadTVChildren();
            }
        });
        return move;
    }

    @FXML
    private MenuItem menuItemDelete() /* Создание пункта "Удалить" контекстного меню */ {
        MenuItem delete = new MenuItem("Удалить");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /* Создание Alert-а с подтверждением удаления ребенка из группы */
                Alert con = new Alert(Alert.AlertType.CONFIRMATION);
                con.setTitle("Подтверждение действия");
                con.setContentText("При удалении ребенка из группы данные о его посещаемости НЕ сохранятся.");
                con.setHeaderText("Вы уверены что хотите удалить " + tv_childrens.getSelectionModel().getSelectedItem().getFio() + " из группы \"" + cb_group.getValue() + "\"?");
                ButtonType yes_btn = new ButtonType("Удалить");
                ButtonType no_btn = new ButtonType("Отмена");
                con.getButtonTypes().clear();
                con.getButtonTypes().addAll(yes_btn, no_btn);
                Optional<ButtonType> option = con.showAndWait();
                /* Если нажато "Удалить" */
                if (option.get() == yes_btn) {
                    /* Удаление выбранного ребенка из списка детей */
                    group.remove(group.getKids().indexOf(tv_childrens.getSelectionModel().getSelectedItem()));
                    /* Запись группы выбранного месяца в файл */
                    FIleDTO.serWriteFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue(), group);
                    reloadTVChildren();
                    reloadTVReportGroups();
                    reloadTvReportCategories();
                    Alert inf = new Alert(Alert.AlertType.INFORMATION);
                    inf.setTitle("Сообщение");
                    inf.setHeaderText("Удаление из группы прошло успешно");
                    inf.setContentText(null);
                    inf.showAndWait();
                }
            }
        });
        return delete;
    }

    @FXML
    private MenuItem menuItemRemove() /* Создание пункта "Исключить" контекстного меню */ {
        MenuItem remove = new MenuItem("Исключить");
        remove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /* Создание Alert-а с подтверждением исключения ребенка из группы */
                Alert con = new Alert(Alert.AlertType.CONFIRMATION);
                con.setTitle("Подтверждение действия");
                con.setHeaderText("Вы уверены что хотите исключить " + tv_childrens.getSelectionModel().getSelectedItem().getFio() + " из группы \"" + cb_group.getValue() + "\"?");
                con.setContentText("При исключении ребенка из группы данные о его посещаемости сохранятся.");
                ButtonType yes_btn = new ButtonType("Исключить");
                ButtonType no_btn = new ButtonType("Отмена");
                con.getButtonTypes().clear();
                con.getButtonTypes().addAll(yes_btn, no_btn);
                Optional<ButtonType> option = con.showAndWait();
                /* Если нажато "Исключить" */
                if (option.get() == yes_btn) {
                    /* Смена статуса выбранного ребенка */
                    TimeDTO time = new TimeDTO();
                    group.getKids().get(group.getKids().indexOf(tv_childrens.getSelectionModel().getSelectedItem())).setStatus(1);
                    group.getKids().get(group.getKids().indexOf(tv_childrens.getSelectionModel().getSelectedItem())).editInfo1(Integer.parseInt(time.getDay()), time.getDaysInChosseMonth(cb_report_month.getValue()));
                    /* Запись группы выбранного месяца в файл */
                    FIleDTO.serWriteFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue(), group);
                    reloadTVChildren();
                    Alert inf = new Alert(Alert.AlertType.INFORMATION);
                    inf.setTitle("Сообщение");
                    inf.setHeaderText("Исключение из группы прошло успешно");
                    inf.setContentText(null);
                    inf.showAndWait();
                    reloadTVReportGroups();
                    reloadTvReportCategories();
                }
            }
        });
        return remove;
    }

    @FXML
    private MenuItem menuItemRestore() /* Создание пункта "Восстановить" контекстного меню */ {
        MenuItem restore = new MenuItem("Восстановить");
        restore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /* Смена статуса выбранного ребенка */
                group.getKids().get(group.getKids().indexOf(tv_childrens.getSelectionModel().getSelectedItem())).setStatus(0);
                group.getKids().get(group.getKids().indexOf(tv_childrens.getSelectionModel().getSelectedItem())).refresh();
                /* Запись группы выбранного месяца в файл */
                FIleDTO.serWriteFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue(), group);
                reloadTVChildren();
                Alert inf = new Alert(Alert.AlertType.INFORMATION);
                inf.setTitle("Сообщение");
                inf.setHeaderText("Восстановление прошло успешно.");
                inf.setContentText(null);
                inf.showAndWait();
                reloadTVReportGroups();
                reloadTvReportCategories();
            }
        });
        return restore;
    }

    @FXML
    private MenuItem menuItemCreate() /* Создание пункта "Новый ребенок" контекстного меню */ {
        MenuItem create = new MenuItem("Новый ребенок");
        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /* Cоздание временного файла для переноса данных в другой контроллер */
                FIleDTO.createFile("temp");
                /* Запись номера выбранного ребенка во временный файл */
                FIleDTO.writeFile("temp", "data" + "\\" + "\\" + cb_group.getValue() + "\\" + "\\" + cb_month.getValue() + "@" + cb_month.getValue());
                /* Открытие окна добавления ребенка */
                Main.createStage("new_child");
                /* Чтение списка детей выбранной группы выбранного месяца */
                group = FIleDTO.serReadFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue());
                reloadTVChildren();
                reloadTVReportGroups();
                reloadTvReportCategories();
            }
        });
        return create;
    }

    @FXML
    private synchronized void actionTVChildrens() /* Действие при нажатии на таблицу */ {
        tv_childrens.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                int count = 0;
                if (cm_fio.isShowing()) {
                    /* Скрыть контекстное меню при нажатии в другую точку */
                    cm_fio.hide();
                }
                if (cm_create.isShowing()) {
                    /* Скрыть контекстное меню при нажатии в другую точку */
                    cm_create.hide();
                }
                /* Если нажата правая кнопка мыши */
                if (event.getButton() == MouseButton.SECONDARY) {
                    /* Если выбрана ячейка таблицы и выбран ребенок (не "Итого") */
                    if (tv_childrens.getSelectionModel().getSelectedItem() != null && tv_childrens.getSelectionModel().getSelectedItem().getStatus() != -1) {
                        /* Если нажатие "итоговое" */
                        if (flag) {
                            /* Если выбрано ФИО/номер ребенка */
                            if ((((TablePosition) ((tv_childrens.getSelectionModel()).getSelectedCells()).get(0)).getTableColumn().getText().equals("ФИО")) || (((TablePosition) ((tv_childrens.getSelectionModel()).getSelectedCells()).get(0)).getTableColumn().getText().equals("№"))) {
                                if (tv_childrens.getSelectionModel().getSelectedItem().getStatus() == 0) {
                                    /* Добавить в контекстное меню с полем "Исключить" */
                                    cm_fio.getItems().add(1, menuItemRemove());
                                } else {
                                    /* Добавить в контекстное меню с полем "Восстановить" */
                                    cm_fio.getItems().add(1, menuItemRestore());
                                }
                                if (cm_fio.getItems().size() > 5) {
                                    cm_fio.getItems().remove(2);
                                }
                                cm_fio.show(tv_childrens, event.getScreenX(), event.getScreenY());
                            }
                            /* Следующее нажатие - не "итоговое" */
                            flag = false;
                        } else {
                            /* Следующее нажатие - "итоговое" */
                            flag = true;
                            try {
                                /* Модуляция нажатия левой и правой кнопкой мыши */


                                Robot robot = null;

                                robot = new Robot();
                                robot.mouseMove((int) event.getScreenX(), (int) event.getScreenY());
                                robot.mousePress(InputEvent.BUTTON1_MASK);
                                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                                if ((((TablePosition) ((tv_childrens.getSelectionModel()).getSelectedCells()).get(0)).getTableColumn().getText().equals("ФИО"))) {
                                    robot.mousePress(InputEvent.BUTTON3_MASK);
                                    robot.mouseRelease(InputEvent.BUTTON3_MASK);
                                }




                            } catch (AWTException e) {
                                reloadTVChildren();


                            }
                        }
                    } else {
                        /* Показать контекстное меню */
                        cm_create.show(tv_childrens, event.getScreenX(), event.getScreenY());
                    }

                }
                /* Если нажата левая кнопка мыши */
                if (event.getButton() == MouseButton.PRIMARY) {
                    /* Если выбрана ячейка таблицы */
                    if (tv_childrens.getSelectionModel().getSelectedItem() != null) {
                        TimeDTO time = new TimeDTO();
                        /* Если выбрана дата */
                        if ((!(((TablePosition) ((tv_childrens.getSelectionModel()).getSelectedCells()).get(0)).getTableColumn().getText().equals("ФИО"))) && (!(((TablePosition) ((tv_childrens.getSelectionModel()).getSelectedCells()).get(0)).getTableColumn().getText().equals("№")))) {
                            /* Если дата до текущей либо текущая */
                            if (!((cb_month.getValue().equals(time.getMonth() + " " + time.getYear())) && (Integer.parseInt(((tv_childrens.getSelectionModel()).getSelectedCells().get(0).getTableColumn()).getText()) > Integer.parseInt(time.getDay())))) {
                                /* Если выбран ребенок (не "Итого") */
                                if (tv_childrens.getSelectionModel().getSelectedItem().getStatus() != -1) {
                                    /* Смена статуса посещения выбранного ребенка в выбранную дату */
                                    group.getKids().get(group.getKids().indexOf(tv_childrens.getSelectionModel().getSelectedItem())).nextInfo(Integer.parseInt(((tv_childrens.getSelectionModel()).getSelectedCells().get(0).getTableColumn()).getText()) - 1);
                                    /* Запись группы выбранного месяца в файл */
                                    FIleDTO.serWriteFile("data\\" + cb_group.getValue() + "\\" + cb_month.getValue(), group);
                                    reloadTVChildren();
                                    reloadTVReportGroups();
                                    reloadTvReportCategories();

                                }
                            }
                        }
                    }
                }

            }

        });
    }


    @FXML
    private void actionBtnPrint() {
    }


    @FXML
    private void recreateWindow() /* Пересоздание списков и таблицы */ {
        createCBGroup();
        createCBMonth();
        createTVChildrens();
    }


    @FXML
    private void createCBReportMonth() /* Заполнение списка отчетных месяцев */ {
        TimeDTO timeDTO = new TimeDTO();
        cb_report_month.setItems(FXCollections.observableArrayList(timeDTO.getMonthWithYearToOneYear()));
        cb_report_month.getSelectionModel().select(timeDTO.getMonth() + " " + timeDTO.getYear());
    }

    @FXML
    private void actionCBReportMonth() /* Действие при выборе отчетного месяца */ {
        tv_report_groups.getColumns().clear();
        tv_report_categories.getColumns().clear();
        createTVReportGroups();
        createTVReportCategories();

    }

    @FXML
    private void createTVReportGroups() /* Заполнение таблицы колонками */ {
        tv_report_groups.getColumns().addAll(createGroupColumn(), new AppController().<GroupDTO>createKidsNumberColumn());
        createReportDateColumns();
        tv_report_groups.getColumns().add(new AppController().<GroupDTO>createTotalColumn());
        reloadTVReportGroups();
    }

    @FXML
    private void createTVReportCategories() /* Заполнение таблицы колонками  для категорий*/ {
        tv_report_categories.getColumns().addAll(createCategoryColumn(), new AppController().<TypeDTO>createKidsNumberColumn());
        createReportDateColumnsCategories();
        tv_report_categories.getColumns().add(new AppController().<TypeDTO>createTotalColumn());
        reloadTvReportCategories();
    }


    @FXML
    private TableColumn<GroupDTO, String> createGroupColumn() /* Создание колонки "Группа" */ {
        TableColumn<GroupDTO, String> group_column = new TableColumn<>();
        group_column.setCellValueFactory(new PropertyValueFactory<GroupDTO, String>("name"));
        group_column.setText("Название группы");
        group_column.setSortable(false);
        return group_column;
    }

    @FXML
    private <T> TableColumn<T, String> createKidsNumberColumn() /* Создание колонки "Состав" */ {
        TableColumn<T, String> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<T, String>("kids_number"));
        column.setText("Состав");
        column.setSortable(false);
        column.getStyleClass().add("table-column-center");
        return column;
    }

    @FXML
    private <T> TableColumn<T, String> createTotalColumn() /* Создание колонки "Всего посещений" */ {
        TableColumn<T, String> column = new TableColumn<>();
        column.setCellValueFactory(new PropertyValueFactory<T, String>("sum_total"));
        column.setText("Всего посещений");
        column.setSortable(false);
        column.getStyleClass().add("table-column-right");
        return column;
    }

    @FXML
    private void reloadTVReportGroups() /* Заполнение таблицы данными */ {

        FIleDTO file = new FIleDTO();

        TimeDTO time = new TimeDTO();
        GroupDTO groupDTO = new GroupDTO();
        ObservableList<GroupDTO> groups = FXCollections.observableArrayList();
        ObservableList<String> presence = FXCollections.observableArrayList();
        ObservableList<String> temp = FXCollections.observableArrayList();
        int days = 0, check = 0;
        boolean flag;
        /* Если у группы есть файл с выбранным месяцем то добавить ее */
        int kids_number = 0;
        int sum_total = 0;

        if (cb_report_month.getValue().equals(new TimeDTO().getMonth() + " " + new TimeDTO().getYear())) {
            days = Integer.parseInt(time.getDay());
            flag = true;
        } else {

            days = time.getDaysInChosseMonth(cb_report_month.getValue());
            flag = false;
        }
        for (int i = 0; i < all_groups.size(); i++) {
            if (file.getAllDates(String.valueOf(all_groups.get(i))).contains(cb_report_month.getValue())) {
                groupDTO = FIleDTO.serReadFile("data\\" + all_groups.get(i) + "\\" + cb_report_month.getValue());
                groupDTO.setName(all_groups.get(i));
                groupDTO.setFlag(flag);
                groups.add(groupDTO);
                kids_number += Integer.parseInt(groupDTO.getKids_number());
                // groupDTO = FIleDTO.serReadFile("data\\" + all_groups.get(i) + "\\" + cb_report_month.getValue());
                //groupDTO.setFlag(flag);
                sum_total += Integer.parseInt(groupDTO.getSum_total());
                check++;
            }


        }
        if (check != 0) {
            check = 0;

            presence = ((FIleDTO.serReadFile("data\\" + all_groups.get(0) + "\\" + cb_report_month.getValue())).getAllTotal(cb_report_month.getValue()));
            for (int i = 1; i < all_groups.size(); i++) {
                if ((file.getAllDates(all_groups.get(i))).contains(cb_report_month.getValue())) {

                    temp = ((FIleDTO.serReadFile("data\\" + all_groups.get(i) + "\\" + cb_report_month.getValue())).getAllTotal(cb_report_month.getValue()));

                    for (int j = 0; j < days; j++) {
                        presence.set(j, String.valueOf(Integer.parseInt(temp.get(j)) + Integer.parseInt(presence.get(j))));

                    }
                }
            }

            if (groups.size() != 0) {
                GroupDTO result_group = new GroupDTO(time.getDaysInChosseMonth(cb_report_month.getValue()));
                result_group.setName("Всего присутствует");
                result_group.setKids_number(String.valueOf(kids_number));

                result_group.setTotal(presence);
                result_group.setSum_total(String.valueOf(sum_total));
                groups.addAll(new GroupDTO(time.getDaysInChosseMonth(cb_report_month.getValue())), result_group);
            }

            /* Очистка таблицы и запись групп */
            tv_report_groups.getItems().clear();
            tv_report_groups.setItems(groups);

        } else {
            //System.out.println("я очистился!!!");
            tv_report_groups.getItems().clear();
        }
    }


    @FXML
    private void createReportDateColumns() /* Создание колонок с датами */ {
        TimeDTO time = new TimeDTO();
        /* Перебор дней в выбранном месяце */

        for (int i = 0; i < time.getDaysInChosseMonth(cb_report_month.getValue()); i++) {
            TableColumn<GroupDTO, String> temp_group = new TableColumn<>();
            TableColumn<GroupDTO, String> temp_categories = new TableColumn<>();
            temp_group.setText((i + 1) + "");
            temp_categories.setText((i + 1) + "");
            if (time.getWeekends(cb_report_month.getValue()).contains(i + 1)) {
                temp_group.setStyle("-fx-text-fill: red");
                temp_categories.setStyle("-fx-text-fill: red");
            }
            temp_group.setSortable(false);
            temp_categories.setSortable(false);
            temp_group.getStyleClass().add("table-column-center");
            temp_categories.getStyleClass().add("table-column-center");

            temp_group.setCellValueFactory(new PropertyValueFactory<GroupDTO, String>("total"));
            //temp_categories.setCellValueFactory(new PropertyValueFactory<TypeDTO, String>("info"));

            tv_report_groups.getColumns().add(temp_group);
            //tv_report_categories.getColumns().add(temp_group);
        }
    }

    @FXML
    private TypeDTO score(ObservableList<String> groups, String name) {
        TimeDTO time = new TimeDTO();
        FIleDTO file = new FIleDTO();
        GroupDTO groupDTO = new GroupDTO();
        ObservableList<String> presence = FXCollections.observableArrayList();
        ObservableList<String> temp = FXCollections.observableArrayList();
        boolean flag;

        int days = 0, check = 0, kids_number = 0, sum_total = 0;
        if (cb_report_month.getValue().equals(new TimeDTO().getMonth() + " " + new TimeDTO().getYear())) {
            days = Integer.parseInt(time.getDay());
            flag = true;
        } else {

            days = time.getDaysInChosseMonth(cb_report_month.getValue());
            flag = false;
        }
        TypeDTO type = new TypeDTO(name, days);
        type.setFlag(flag);
        if (groups.size() == 1 && groups.get(0) == null) {
            return type;
        }
        for (int i = 0; i < groups.size(); i++) {

            if ((file.getAllDates(String.valueOf(groups.get(i)))).contains(cb_report_month.getValue())) {

                groupDTO = FIleDTO.serReadFile("data\\" + groups.get(i) + "\\" + cb_report_month.getValue());
                kids_number += Integer.parseInt(groupDTO.getKids_number());

                groupDTO.setFlag(flag);
                sum_total += Integer.parseInt(groupDTO.getSum_total());
                check++;
            }

        }
        if (check != 0) {
            check = 0;
            presence = ((FIleDTO.serReadFile("data\\" + groups.get(0) + "\\" + cb_report_month.getValue())).getAllTotal(cb_report_month.getValue()));
            for (int i = 1; i < groups.size(); i++) {
                if ((file.getAllDates(all_groups.get(i))).contains(cb_report_month.getValue())) {

                    temp = ((FIleDTO.serReadFile("data\\" + groups.get(i) + "\\" + cb_report_month.getValue())).getAllTotal(cb_report_month.getValue()));

                    for (int j = 0; j < days; j++) {
                        presence.set(j, String.valueOf(Integer.parseInt(temp.get(j)) + Integer.parseInt(presence.get(j))));

                    }

                }
                //type.setDay(0);

            }
            type.setKids_number(String.valueOf(kids_number));
            temp.clear();
            for (int i = 0; i < days; i++) {
                temp.add(i, presence.get(i));
            }
            // temp=(ObservableList<String>) presence.subList(0,days);
            type.setTotal(temp);
            type.setSum_total(String.valueOf(sum_total));
//type.setDay(0);
        }
        return type;
    }

    @FXML
    private void reloadTvReportCategories() {
        FIleDTO file = new FIleDTO();
        TimeDTO time = new TimeDTO();

        GroupDTO groupDTO = new GroupDTO();
        ObservableList<TypeDTO> categories = FXCollections.observableArrayList();
        ObservableList<String> groupsManger = FXCollections.observableArrayList();
        ObservableList<String> groupsGarten = FXCollections.observableArrayList();
        TypeDTO resultGroups;
        ObservableList<String> presence = FXCollections.observableArrayList();
        ObservableList<String> temp = FXCollections.observableArrayList();
        int days = 0, check = 0;
        boolean flag, isNotNull = false;
        /* Если у группы есть файл с выбранным месяцем то добавить ее */
        int kids_number = 0;
        int sum_total = 0;
        if (cb_report_month.getValue().equals(new TimeDTO().getMonth() + " " + new TimeDTO().getYear())) {
            days = Integer.parseInt(time.getDay());
            flag = true;
        } else {

            days = time.getDaysInChosseMonth(cb_report_month.getValue());
            flag = false;
        }
        TypeDTO typeGartenDTO = new TypeDTO("Ясли", time.getDaysInChosseMonth(cb_report_month.getValue()));
        TypeDTO typeMangerDTO = new TypeDTO("Сад", time.getDaysInChosseMonth(cb_report_month.getValue()));
        for (int i = 0; i < all_groups.size(); i++) {
            if (file.getAllDates(String.valueOf(all_groups.get(i))).contains(cb_report_month.getValue())) {
                groupDTO = FIleDTO.serReadFile("data\\" + all_groups.get(i) + "\\" + cb_report_month.getValue());


                if (groupDTO.isManger()) {
                    groupsManger.add(groupDTO.getName());
                } else {
                    groupsGarten.add(groupDTO.getName());
                }
                isNotNull = true;

            }
        }
        if (isNotNull) {
            typeMangerDTO = score(groupsManger, "ЯСЛИ");
            typeGartenDTO = score(groupsGarten, "САД");


            kids_number = Integer.parseInt(typeGartenDTO.getKids_number()) + Integer.parseInt(typeMangerDTO.getKids_number());
            sum_total = Integer.parseInt(typeGartenDTO.getSum_total()) + Integer.parseInt(typeMangerDTO.getSum_total());


            // System.out.println(typeMangerDTO.getTotal().get(i));
            //System.out.println(typeGartenDTO.getTotal().get(i));
            for (int i = 0; i < days; i++) {

                presence.add(String.valueOf(Integer.parseInt(typeMangerDTO.getTotal()) + Integer.parseInt(typeGartenDTO.getTotal())));

            }
            //   System.out.println(cb_report_month.getValue());


            typeMangerDTO.setDay(-1);
            typeGartenDTO.setDay(-1);

            typeMangerDTO.addTotal(time.getDaysInChosseMonth(cb_report_month.getValue()) - days);
            typeGartenDTO.addTotal(time.getDaysInChosseMonth(cb_report_month.getValue()) - days);
            categories.addAll(typeGartenDTO, typeMangerDTO);

            if (Integer.parseInt(typeMangerDTO.getKids_number()) != 0 || Integer.parseInt(typeGartenDTO.getKids_number()) != 0) {
                resultGroups = new TypeDTO(time.getDaysInChosseMonth(cb_report_month.getValue()));
                resultGroups.setName("Всего присутствует");
                resultGroups.setKids_number(String.valueOf(kids_number));
                //System.out.println(presence);
                resultGroups.addTotal(time.getDaysInChosseMonth(cb_report_month.getValue()) - days);

                resultGroups.setTotal(presence);
                resultGroups.addTotal(time.getDaysInChosseMonth(cb_report_month.getValue()) - days);
                // System.out.println(resultGroups.getTotal());
                resultGroups.setSum_total(String.valueOf(sum_total));
                resultGroups.setDay(-1);
                categories.addAll(new TypeDTO(time.getDaysInChosseMonth(cb_report_month.getValue())), resultGroups);
            }
            //      System.out.println(categories.get(0).getTotal());
            /* Очистка таблицы и запись групп */


            tv_report_categories.getItems().clear();
            tv_report_categories.setItems(categories);
        } else {
            tv_report_categories.getItems().clear();

        }
        isNotNull = false;
    }

    @FXML
    private void createReportDateColumnsCategories() /*Создание колонок с датами  для категорий*/ {
        TimeDTO time = new TimeDTO();
        /* Перебор дней в выбранном месяце */

        for (int i = 0; i < time.getDaysInChosseMonth(cb_report_month.getValue()); i++) {
            TableColumn<TypeDTO, String> temp_group = new TableColumn<>();
            TableColumn<TypeDTO, String> temp_categories = new TableColumn<>();
            temp_group.setText((i + 1) + "");
            temp_categories.setText((i + 1) + "");
            if (time.getWeekends(cb_report_month.getValue()).contains(i + 1)) {
                temp_group.setStyle("-fx-text-fill: red");
                temp_categories.setStyle("-fx-text-fill: red");
            }
            temp_group.setSortable(false);
            temp_categories.setSortable(false);
            temp_group.getStyleClass().add("table-column-center");
            temp_categories.getStyleClass().add("table-column-center");

            temp_group.setCellValueFactory(new PropertyValueFactory<TypeDTO, String>("total"));
            //temp_categories.setCellValueFactory(new PropertyValueFactory<TypeDTO, String>("info"));

            tv_report_categories.getColumns().add(temp_group);
            //tv_report_categories.getColumns().add(temp_group);
        }

    }

    @FXML
    private TableColumn<TypeDTO, String> createCategoryColumn() /* Создание колонки "Группа" */ {
        TableColumn<TypeDTO, String> category_column = new TableColumn<>();
        category_column.setCellValueFactory(new PropertyValueFactory<TypeDTO, String>("name"));
        category_column.setText("Название категории");
        category_column.setSortable(false);
        return category_column;
    }


}