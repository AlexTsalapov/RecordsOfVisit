package main.java.com.app.dto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;

public class ChildDTO implements Externalizable {
    private String number;
    private String surname;
    private String name;
    private String second_name;
    private String fio;
    private int status;
    private int index;
    private ObservableList<String> info;


    public ChildDTO() {

    }

    public ChildDTO(String number, String surname, String name, String second_name, int status, ObservableList<String> info) {
        setNumber(number);
        setName(name);
        setSurname(surname);
        setSecond_name(second_name);
        setFio(this.surname + " " + this.name + " " + this.second_name);
        this.status = status;
        this.index = -1;
        this.info = info;
    }

    public ChildDTO(String number, String surname, String name, String second_name, int status, int info_size, ArrayList<Integer> weekends) {
        setNumber(number);
        setName(name);
        setSurname(surname);
        setSecond_name(second_name);
        setFio(this.surname + " " + this.name + " " + this.second_name);
        this.status = status;
        this.index = -1;
        this.info = FXCollections.observableArrayList();
        for (int i = 0; i < info_size; i++) {
            if (weekends.contains(i+1)){
                this.info.add("В");
            }
            else{
                this.info.add("");
            }
        }
    }

    public ChildDTO(int info_size) {
        number = " ";
        surname = "";
        name = "";
        second_name = "";
        fio = "";
        status = -1;
        info = FXCollections.observableArrayList();for (int i = 0; i < info_size; i++) {
            this.info.add(" ");
        }
        index = -1;
    }

    public ChildDTO(ObservableList<String> info)
    {
        number = " ";
        surname = "";
        name = "";
        second_name = "";
        fio = " Всего присутствует";
        status = -1;
        this.info = info;
        index = -1;
    }


    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getSurname() {
        return surname;
    }

    public String getInfo() {
        if (info.size() == 0) {
            return "";
        }
        index++;
        if (index == info.size()) {
            index = 0;
        }
        return info.get(index);
    }

    public String getInfo(int index) {
        return info.get(index);
    }

    public ObservableList<String> getAllInfo()
    {
        return info;
    }

    public String getFio() {
        return fio;
    }

    public int getStatus() {
        return status;
    }


    public void setInfo(ObservableList<String> info) {
        this.info = info;
    }

    public void setNumber(String number) {
        if (number.length() != 3) {
            String temp = "";
            for (int i = 0; i < 3 - number.length(); i++) {
                temp += "0";
            }
            temp += number;
            this.number = temp;
            return;
        }
        this.number = number;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setSurname(String surname) {
        this.surname = surname.trim();
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name.trim();
    }

    public void setFio(String fio) {
        this.surname = fio.split(" ", 3)[0].substring(0, 1).toUpperCase() + fio.split(" ", 3)[0].substring(1).toLowerCase();
        this.name = fio.split(" ", 3)[1].substring(0, 1).toUpperCase() + fio.split(" ", 3)[1].substring(1).toLowerCase();
        this.second_name = fio.split(" ", 3)[2].substring(0, 1).toUpperCase() + fio.split(" ", 3)[2].substring(1).toLowerCase();
        this.fio = this.surname + " " + this.name + " " + this.second_name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfo(int index, String info) {
        this.info.set(index, info);
    }

    public int getSumInfo(boolean a){
        int sum = 0,days=0;
        if(a)
        {
          days= Integer.parseInt(new TimeDTO().getDay());
        }
        else {
            days=info.size();

        }
        for (int i = 0; i <days; i++){
            if (info.get(i).equals("")){
                sum++;
            }
        }

        return sum;
    }
    public void editInfo2(int dayOfMonth)
    {

        for (int i = 0; i <dayOfMonth; i++) {
            info.set(i,"И");
        }


    }
    public void editInfo1(int day,int dayOfMonth)
    {

        for (int i = day-1; i <info.size(); i++) {
            info.set(i,"И");
        }


    }

    public void refresh()
    {
        TimeDTO time=new TimeDTO();
       ArrayList<Integer> weekend= time.getWeekends(time.getMonth()+" "+time.getYear());
        for (int i = 0; i < info.size(); i++) {
            if(info.get(i).equals("И"))
            {
                info.set(i,"");
            }
            if (weekend.contains(i+1)){
                info.set(i,"В");
            }


        }
    }

    public void nextInfo(int index) {
        if (this.info.get(index).equals("")) {
            this.info.set(index, "Б");
        } else if (this.info.get(index).equals("Б")) {
            this.info.set(index, "Н");
        } else if (this.info.get(index).equals("Н")) {
            this.info.set(index, "");
        }
    }


    public void writeExternal(ObjectOutput out) {
        try {
            out.writeObject(number);
            out.writeObject(name);
            out.writeObject(surname);
            out.writeObject(second_name);
            out.writeObject(status);

            out.writeObject(info.toArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        index = -1;
    }

    public void readExternal(ObjectInput in) {
        try {

            setNumber((String) in.readObject());
            setName((String) in.readObject());
            setSurname((String) in.readObject());
            setSecond_name((String) in.readObject());


            status = (int) in.readObject();
            Object[] temp = (Object[]) in.readObject();
            info = FXCollections.observableArrayList();
            fio = surname + " " + name + " " + second_name;
            for (int i = 0; i < temp.length; i++) {
                info.add(i, (String) temp[i]);
            }
            index = -1;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        index = -1;
    }
}
