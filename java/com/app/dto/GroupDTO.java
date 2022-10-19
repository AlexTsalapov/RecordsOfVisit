package main.java.com.app.dto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class GroupDTO implements Externalizable {
    private String name;
    private boolean isManger;
    private ObservableList<ChildDTO> kids;
    private String kids_number = "";
    private ObservableList<String> total;
    private String sum_total = "";
    private int day;
    private boolean flag = false;
    //private String total;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public GroupDTO() {

/*
        name = "1";
        kids = FXCollections.observableArrayList();
        kids.add(new ChildDTO("01", "Галабурда", "Валерия", "Сергеевна", 0, FXCollections.observableArrayList("","", "", "В", "В", "", "", "", "", "", "В", "В", "", "", "", "", "", "В", "В", "", "", "", "", "", "В", "В", "", "", "", "", "")));
        kids.add(new ChildDTO("02", "Цалапов", "Алексей", "Дмитриевич", 0, FXCollections.observableArrayList("","", "", "В", "В", "", "", "", "", "", "В", "В", "", "", "", "", "", "В", "В", "", "", "", "", "", "В", "В", "", "", "", "", "")));
        isManger = false;
*/
        total = FXCollections.observableArrayList();
        day = -1;
    }

    public GroupDTO(int days) {
        name = "";
        kids_number = "";
        kids = FXCollections.observableArrayList();
        total = FXCollections.observableArrayList();
        for (int i = 0; i < days; i++) {
            total.add("");
        }
        sum_total = "";
        day = -1;
    }

    public GroupDTO(String name, ObservableList<ChildDTO> kids, boolean isManger, int days) {
        this.name = name;
        this.isManger = isManger;
        this.kids = kids;
        this.kids_number = String.valueOf(kids.size());
        total = FXCollections.observableArrayList();
        for (int i = 0; i < days; i++) {
            total.add("");
        }
        sum_total = "";
        this.day = -1;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isManger() {
        return isManger;
    }

    public void setManger(boolean manger) {
        isManger = manger;
    }

    public ChildDTO getKid(String num) {
        for (int i = 0; i < kids.size(); i++) {
            if (kids.get(i).getNumber().equals(num)) {
                return kids.get(i);
            }
        }
        return kids.get(0);
    }

    public ObservableList<ChildDTO> getActualKids(int info_size, ArrayList<Integer> weekends) /* создание массива */ {
        ObservableList<ChildDTO> temp = FXCollections.observableArrayList();
        ObservableList<String> info = FXCollections.observableArrayList();
        for (int i = 0; i < info_size; i++) {
            if (weekends.contains(i + 1)) {
                info.add("В");
            } else {
                info.add("");
            }
        }
        ChildDTO child = new ChildDTO();
        for (int i = 0; i < kids.size(); i++) {
            if (kids.get(i).getStatus() == 0) {
                child = kids.get(i);
                child.setInfo(info);
                temp.add(child);
            }
        }
        return temp;
    }

    public ObservableList<String> getFIO() {
        ObservableList<String> fio = FXCollections.observableArrayList();
        for (int i = 0; i < kids.size(); i++) {
            fio.add(kids.get(i).getFio().toLowerCase());
        }
        return fio;
    }

    public ObservableList<Integer> getNumbers() {
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        for (int i = 0; i < kids.size(); i++) {
            numbers.add(Integer.parseInt(kids.get(i).getNumber()));
        }
        return numbers;
    }

    public ObservableList<ChildDTO> getKids() {
        if (kids == null) {
            kids = FXCollections.observableArrayList();
        }

        return kids;
    }

    public void setKids(ObservableList<ChildDTO> kids) {
        this.kids = kids;
        this.kids_number = String.valueOf(kids.size());
    }

    public String getKids_number() {

        if (name.equals("Всего присутствует")) {
            return kids_number;
        }
        if (name.equals("")) {
            return "";
        }

        kids_number = String.valueOf(kids.size());

        return kids_number;
    }

    public void setKids_number(String kids_number) {
        this.kids_number = kids_number;
    }

    public String getTotal() {

        /* Если это пустая строка */
        if (kids.size() == 0 && name.equals("")) {
            return "";
        }
        /* Если это строка итого */
        if (kids.size() == 0 && name.equals("Всего присутствует")) {
            day++;
            if (day == total.size()) {
                day = 0;
            }
            return total.get(day);
        }
        /* Если это обычная группа */
        day++;
        if (kids.size() != 0 && day == kids.get(0).getAllInfo().size()) {
            day = 0;
            total = FXCollections.observableArrayList();
        }
        int count = 0;
        for (int j = 0; j < kids.size(); j++) {
            if (kids.get(j).getInfo(day).equals(""))
                count++;
        }
        total.add(String.valueOf(count));
        if (flag) {
            if (day >= Integer.parseInt(new TimeDTO().getDay())) {

                return "";
            }
        }
        return total.get(day);
    }

    public void setTotal(ObservableList<String> total) {
        this.total = total;
    }

    public ObservableList<String> getAllTotal(String date) {
        TimeDTO time = new TimeDTO();
        int count = 0, days = 0, i = 0;
        ObservableList<String> total = FXCollections.observableArrayList();
        if (date.equals(time.getMonth() + " " + time.getYear())) /*Если выбран текущий месяц*/ {
            days = Integer.parseInt(time.getDay());
        } else {
            days = time.getDaysInChosseMonth(date);
        }
        for (i = i; i < days; i++) {
            total.add("0");
            for (int j = 0; j < kids.size(); j++) {//дети
                if (kids.get(j).getInfo(i).equals(""))
                    count++;
            }
            total.set(i, count + "");
            count = 0;
        }
        for (i = i; i < time.getDaysInChosseMonth(date); i++) {
            total.add("");
        }

        return total;
    }

    public ObservableList<String> getTotalList() {
        return total;
    }

    public String getSum_total() {
        if (name.equals("")) {
            return "";
        }
        if ((kids.size() == 0) && (!sum_total.equals(""))) {
            return sum_total;
        }
        if (kids.size() == 0) {
            return "0";
        }
        int sum = 0;
        for (int i = 0; i < kids.size(); i++) {
            sum += kids.get(i).getSumInfo(flag);
        }
        sum_total = String.valueOf(sum);
        return sum_total;
    }

    public void setSum_total(String sum_total) {
        this.sum_total = sum_total;
    }


    public void addToTotal(ObservableList<String> other) {
        ObservableList<String> new_total = FXCollections.observableArrayList();
        for (int i = 0; i < total.size(); i++) {
            new_total.add(String.valueOf(i));
        }
        total = new_total;
    }

    public void remove(int index) {
        kids.remove(index);
    }


    public void readExternal(ObjectInput in) {
        try {
            name = (String) in.readObject();
            isManger = (boolean) in.readObject();

            Object[] temp = (Object[]) in.readObject();
            kids = FXCollections.observableArrayList();
            for (int i = 0; i < temp.length; i++) {
                kids.add(i, (ChildDTO) temp[i]);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        day = -1;
    }

    public void writeExternal(ObjectOutput out) {
        try {
            out.writeObject(name);
            out.writeObject(isManger);

            out.writeObject(kids.toArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        day = -1;
    }
}