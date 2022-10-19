package main.java.com.app.dto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.app.controller.AppController;

import java.time.LocalDate;

public class TypeDTO {
    private String name;
    private String kids_number = "0";

    private ObservableList<String> total;
    private String sum_total ="0";
    private int day;
    boolean flag;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setName(String name) {
        this.name = name;
        sum_total = "0";


    }

    public void setSum_total(String sum_total) {
        if (sum_total != null)
            this.sum_total = sum_total;
    }

    public String getSum_total() {
        if(name.equals(""))
        {
            return "";
        }

        return sum_total;
    }

    public TypeDTO(int days) {
        name = "";
        kids_number = "";

        total = FXCollections.observableArrayList();
        for (int i = 0; i < days; i++) {
            total.add("");
        }
        //sum_total = "0";
        day = -1;
     //   kids_number = "0";
    }

    //private ObservableList<> groups;
    public TypeDTO(String name, int days) {

        this.name = name;
        total = FXCollections.observableArrayList();
        for (int i = 0; i < days; i++) {
            total.add("0");
        }
        //sum_total = "0";
        day = -1;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getKids_number() {
        return kids_number;
    }

    public String getName() {
        return name;
    }
    public void addTotal(int day)
    {
        for (int i = 0; i < day; i++) {
            total.add("");
        }
       // System.out.println(total);
    }
    //private S

    public String getTotal() {
        /* Если это пустая строка */

        if (day >= total.size()-1) {
            int temp=day;
            day=0;
            return total.get(day);
        }
        if(flag) {
            if (day >= Integer.parseInt(new TimeDTO().getDay())) {
                day++;
                return "";
            }
        }
        day++;




        return total.get(day);
    }

    public void setKids_number(String kids_number) {
        if (kids_number != null)
            this.kids_number = kids_number;
    }

    public void setTotal(ObservableList<String> total) {
        if (total != null)
            this.total = total;
    }
}
