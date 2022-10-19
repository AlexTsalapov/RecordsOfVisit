package main.java.com.app.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeDTO {
    private HashMap<Month, String> month_to_stringmonth = new HashMap<Month, String>(){{
        put(Month.JANUARY, "Январь");
        put(Month.FEBRUARY, "Февраль");
        put(Month.MARCH, "Март");
        put(Month.APRIL, "Апрель");
        put(Month.MAY, "Май");
        put(Month.JUNE, "Июнь");
        put(Month.JULY, "Июль");
        put(Month.AUGUST, "Август");
        put(Month.SEPTEMBER, "Сентябрь");
        put(Month.OCTOBER, "Октябрь");
        put(Month.NOVEMBER, "Ноябрь");
        put(Month.DECEMBER, "Декабрь");
    }};
    private HashMap<String, Integer> stringmonth_to_intmonth = new HashMap<String, Integer>(){{
        put("Январь", 1);
        put("Февраль", 2);
        put("Март", 3);
        put("Апрель", 4);
        put("Май", 5);
        put("Июнь", 6);
        put("Июль", 7);
        put("Август", 8);
        put("Сентябрь", 9);
        put("Октябрь", 10);
        put("Ноябрь", 11);
        put("Декабрь", 12);
    }};
    private HashMap<String, Month> stringmonth_to_month = new HashMap<String, Month>() {{
        put("Январь", Month.JANUARY);
        put("Февраль", Month.FEBRUARY);
        put("Март", Month.MARCH);
        put("Апрель", Month.APRIL);
        put("Май", Month.MAY);
        put("Июнь", Month.JUNE);
        put("Июль", Month.JULY);
        put("Август", Month.AUGUST);
        put("Сентябрь", Month.SEPTEMBER);
        put("Октябрь", Month.OCTOBER);
        put("Ноябрь", Month.NOVEMBER);
        put("Декабрь", Month.DECEMBER);
    }};

    private String current_day;
    private String current_month;
    private String current_year;
    private static LocalDate today = LocalDate.now();

    public TimeDTO()
    {
        today = LocalDate.now();
        current_day = getDay();
        current_month = getMonth();
        current_year = getYear();
    }

    public static LocalDate getToday() {
        return today;
    }

    public String getDay()
    {
        return String.valueOf(LocalDate.now().getDayOfMonth());
    }
    public String getMonth()
    {
        return String.valueOf(month_to_stringmonth.get(LocalDate.now().getMonth()));
    }
    public String getYear()
    {
        return String.valueOf(today.getYear());
    }

    public DayOfWeek getDayWeek()
    {
        return  today.getDayOfWeek();
    }
    public static int getDaysInMonth()
    {
        return today.getMonth().maxLength();
    }
    public  int getDaysInChosseMonth(String path)
    {
        String month=path.split(" ")[0];

        String year=path.split(" ")[1];
        int tempInt= stringmonth_to_intmonth.get(month);
        LocalDate temp= LocalDate.of(Integer.parseInt(year),tempInt,1);
        return temp.getMonth().maxLength();
    }
    public ArrayList<String> getLastMonths(ArrayList<String> dates)
    {
        for (int i = 0; i < dates.size(); i++)
        {
            if (!((dates.get(i).equals(getMonth()+" "+getYear())) || (dates.get(i).equals(getPrevMonthWithYear()))))
            {
                dates.remove(i);
                i--;
            }
        }
        return dates;
    }
    public String getPrevMonthWithYear()
    {
        LocalDate month=today.minusMonths(1);
        String year = String.valueOf(today.getYear());
        return month_to_stringmonth.get(month.getMonth())+" "+year;
    }
    public ArrayList<String> getMonthWithYearToOneYear()
    {
        ArrayList<String> monthWithYearToOneYear = new ArrayList<>();
        monthWithYearToOneYear.add(month_to_stringmonth.get(today.getMonth()) + " " + current_year);
        for (int i = 0; i < 14; i++)
        {
            today = today.minusMonths(1);
            String year = String.valueOf(today.getYear());
            monthWithYearToOneYear.add(month_to_stringmonth.get(today.getMonth()) + " " + year);
        }
        today = LocalDate.now();
        return monthWithYearToOneYear;
    }
    public ArrayList<Integer> getWeekends(String path)
    {
        ArrayList<Integer> weekendDays = new ArrayList<>();
        String year = path.substring(path.length() - 4, path.length());
        String month_in_string = path.substring(0, path.length() - 5);
        Month month = stringmonth_to_month.get(month_in_string);
        LocalDate temp = LocalDate.of(Integer.parseInt(year), month, 1);
        for (int i = 0; i < temp.getMonth().maxLength(); i++)
        {
            String dayInWeek = String.valueOf(temp.getDayOfWeek());
            if (dayInWeek.equals("SUNDAY") || dayInWeek.equals("SATURDAY"))
            {
                weekendDays.add(i + 1);
            }
            temp = temp.plusDays(1);
        }
        return weekendDays;
    }
    public ArrayList<String> sortMonth(ArrayList<String> months)
    {
        String month_i, month_j;
        for (int i = 0; i < months.size() - 1; i++)
        {
            for (int j = i + 1; j <months.size(); j++)
            {
                month_j = months.get(j);
                month_i = months.get(i);

                if (Integer.parseInt(months.get(i).substring(month_i.length()-4, month_i.length())) < Integer.parseInt(months.get(j).substring(month_j.length()-4, month_j.length())))
                {
                    String temp_month = months.get(i);
                    months.set(i, months.get(j));
                    months.set(j, temp_month);
                }
                else if (Integer.parseInt(months.get(i).substring(month_i.length()-4, month_i.length())) == Integer.parseInt(months.get(j).substring(month_j.length()-4, month_j.length())))
                {
                    if (stringmonth_to_intmonth.get(months.get(i).substring(0, month_i.length()-5)) < stringmonth_to_intmonth.get(months.get(j).substring(0, month_j.length()-5)))
                    {
                        String temp_month = months.get(i);
                        months.set(i, months.get(j));
                        months.set(j, temp_month);
                    }
                }
            }
        }
        return months;
    }
}