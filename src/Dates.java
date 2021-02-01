import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Dates {
    private static Connection connection;
    private static ArrayList<String> date = new ArrayList<String>();
    private static PreparedStatement addDate;
    private static PreparedStatement checkDate;
    private static PreparedStatement getDateList;
    private static ResultSet resultSet;
    
    public static boolean addDates(Date date){
        connection = DBConnection.getConnection();
        try{
            boolean dateUsed = checkDate(date);
            if(!dateUsed){
                addDate = connection.prepareStatement("insert into dates (date) values (?)");  // inserting the given date into date table
                addDate.setDate(1, date);
                addDate.executeUpdate();
                return true;
            }
            else return false;
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return false;
    }//ends addDates
    
    public static boolean checkDate(Date date){
        connection = DBConnection.getConnection();
        int size = 0;
        try{
            checkDate = connection.prepareStatement("SELECT * FROM dates WHERE date = ?");
            checkDate.setDate(1, date);
            ResultSet resultSet = checkDate.executeQuery();
            while(resultSet.next()){
                size++;
            }
            if(size == 0) return false;
            else return true;
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return false;
    }
    
    public static ArrayList<Date> getDatesList(){
        connection = DBConnection.getConnection();
        ArrayList<Date> date = new ArrayList<Date>();
        try{
            getDateList = connection.prepareStatement("select date from dates order by date"); // returns the selected date that they want and it is ordered by date
            resultSet = getDateList.executeQuery();
            while(resultSet.next()){
                date.add(resultSet.getDate(1)); // this helps format the return so I can make a list 
            }                                    // of dates to return if there are multiple things in the database with the same date
            
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return date; // returns the list we made of dates
    }
}//ends class
