package main.java.com.app.dto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FIleDTO {
    String dir_path = "";
    String info_file_name = "";

    public FIleDTO() {
        this.dir_path = "data";
        this.info_file_name = "info";
        createMainDir();
    }

    public FIleDTO(String main_dir, String info_file_name) {
        this.dir_path = main_dir;
        this.info_file_name = info_file_name;
        createMainDir();
    }

    private void createMainDir() // создание начальной директории и файла с информацией
    {
        if (!Files.exists(Path.of(dir_path))) {
            createDir(dir_path);
            createFile(dir_path + "\\" + info_file_name);
            writeFile(dir_path + "\\" + info_file_name, "0");
        }
    }

    public static ArrayList<String> getDirContent(String path) // возвращает все элементы хранящиеся в директории
    {
        ArrayList<String> content = new ArrayList<String>();
        DirectoryStream<Path> stream = null;
        try {
            stream = Files.newDirectoryStream(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Path dir : stream) {
            content.add(String.valueOf(dir));
        }
        return content;
    }

    public ArrayList<String> getGroups() // возвращает список групп (пустой список если групп нет)
    {
        ArrayList<String> groups = new ArrayList<String>();
        if (getDirContent(dir_path).size() > 1) //
        {
            groups = getDirContent(dir_path);

            for (int i = 0; i < groups.size(); i++) {
                groups.set(i, groups.get(i).substring(dir_path.length() + 1));
                if (groups.get(i).equals(info_file_name)) {
                    groups.remove(i);
                    i--;
                }
            }
        }
        return groups;
    }

    public ArrayList<String> getDates(String group)// возвращает список из двух последних (если нет - будут)
    {
        ArrayList<String> dates = new ArrayList<String>();
        TimeDTO date = new TimeDTO();

        return date.getLastMonths(getAllDates(group));
    }

    public ArrayList<String> getAllDates(String group) // возвращает список месяцов в группе, создает тек. мес. если их нет
    {

        ArrayList<String> dates = new ArrayList<String>();
        TimeDTO date = new TimeDTO();
        try {
            if (getDirContent(dir_path + "\\" + group).size() == 0) // если в группе нет записей
            {
                Files.createFile(Path.of(dir_path + "\\" + group + "\\" + date.getMonth() + " " + date.getYear())); // создать файл текущего месяца
                dates.add(date.getMonth() + " " + date.getYear()); // добавить текущий месяц в список
            } else {
                if (!getDirContent(dir_path + "\\" + group).contains(dir_path + "\\" + group + "\\" + date.getMonth() + " " + date.getYear())) {

                    Files.createFile(Path.of(dir_path + "\\" + group + "\\" + date.getMonth() + " " + date.getYear())); // создать файл текущего месяца
                    dates.add(date.getMonth() + " " + date.getYear()); // добавить текущий месяц в список
                }
                dates = getDirContent(dir_path + "\\" + group);
                for (int i = 0; i < dates.size(); i++) {
                    dates.set(i, dates.get(i).substring(dir_path.length() + group.length() + 2));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return date.sortMonth(dates); // возвращает отсортированный список месяцев (за все время)
    }

    public static void createFile(String path) {
        if (!Files.exists(Path.of(path))) {
            try {
                Files.createFile(Path.of(path));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createDir(String path) {
        try {
            Files.createDirectory(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean deleteFile(String path) {
        try {
            if(Files.deleteIfExists(Path.of(path)))
            {
                return true;

            }
            else{
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
public  void deleteOldFile()
{
ArrayList<String>groups=getGroups();
TimeDTO time=new TimeDTO();
ArrayList<String>dates_year=time.getMonthWithYearToOneYear();
ArrayList<String> dates;
for(String str:groups)
{
   dates= getAllDates(str);
    for(String temp:dates)
    {
        if(!dates_year.contains(temp))
        {
            deleteFile("data\\" + str + "\\" + temp);
        }
    }
}
}
    public static boolean deleteDir(String path)  {
        ArrayList<String> files = null;
        files = getDirContent(path);
       GroupDTO groupDTO=new GroupDTO();

        for (int i = 0; i < files.size(); i++) {


            try {
                if(!Files.deleteIfExists(Path.of(files.get(i))))
                {
                    return false;
                }

            } catch (IOException e) {
                return false;
            }


        }
            try {
            //Files.exists(Path.of(path));
           if( Files.deleteIfExists(Path.of(path)))
           {
               return true;
           }
           else {
               return false;
           }
        } catch (IOException e) {
            return false;
        }
    }

    public static void renameDir(String path, String new_path) {
        java.io.File dir = new java.io.File(path);
        java.io.File newDir = new java.io.File(new_path);
        dir.renameTo(newDir);
    }

    public static void writeFile(String path, String data) {
        Charset w1251 = Charset.forName("Windows-1251");
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            stream.write(data.getBytes(w1251));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(String path) {
        Charset w1251 = Charset.forName("Windows-1251");
        String data = "";
        int i = -1, n = 0;
        BufferedReader stream = null;
        try {
            stream = new BufferedReader(new InputStreamReader(new FileInputStream(path), "cp1251"));
            while ((i = stream.read()) != -1) {
                data += Character.toString((char) i);
            }
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static GroupDTO serReadFile(String filename) {
        GroupDTO groupDTO = new GroupDTO();
        ObservableList<ChildDTO> kids = FXCollections.observableArrayList();
        FileInputStream fiStream = null;
        try {
            TimeDTO timeDTO = new TimeDTO();
            fiStream = new FileInputStream(filename);
            groupDTO.setName(filename.split("\\\\")[2]);
            if (fiStream.available() != 0) {
                ObjectInputStream objectStream = new ObjectInputStream(fiStream);
                int i = 0;

                while (true) {

                    if (fiStream.available() != 0) {

                        // ChildDTO temp = new ChildDTO();
                        groupDTO = (GroupDTO) objectStream.readObject();

                    } else {
                      //  groupDTO.setManger((Boolean) objectStream.readObject());
                      //  groupDTO.setKids(kids);
                        return groupDTO;
                    }
                }
            } else {
             //   groupDTO.setKids(kids);
                return groupDTO;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void serWriteFile(String filename, GroupDTO group) {
        ObservableList<ChildDTO> kids = group.getKids();
        FileOutputStream fileOutput = null;
        Collections.sort(kids, new Comparator<ChildDTO>() {
            @Override
            public int compare(ChildDTO o1, ChildDTO o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });
        try {
            createFile(filename);
            fileOutput = new FileOutputStream(filename);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);

           /* for (int i = 0; i < kids.size(); i++) {
                outputStream.writeObject(kids.get(i));
            }*/

            outputStream.writeObject(group);
            fileOutput.close();
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не создан");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}