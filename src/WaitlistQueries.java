import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class WaitlistQueries{
    private static Connection connection;
    private static ResultSet resultSet;
    private static PreparedStatement addWaitlist;
    private static PreparedStatement getWaitlist;
    
    public static ArrayList<WaitlistEntry> getWaitlist(){
        connection = DBConnection.getConnection();
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        try{
            getWaitlist = connection.prepareStatement("select faculty, date, seats, timestamp from waitlist order by date, timestamp");
            
            resultSet = getWaitlist.executeQuery();                                     // returns the waitlist in order by date first then timestamp
            
            while(resultSet.next()){
                WaitlistEntry entry = new WaitlistEntry(resultSet.getString(1), resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4));
                waitlist.add(entry);
            }
        }    
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return waitlist;
    } // ends get waitlist
    
    public static ArrayList<WaitlistEntry> getWaitlistByDate(Date date){
        connection = DBConnection.getConnection();                                  // gets the waitlist by date
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        try{
            getWaitlist = connection.prepareStatement("select faculty, seats, timestamp from waitlist where date = ?");
            getWaitlist.setDate(1, date);
            resultSet = getWaitlist.executeQuery();
            
            while(resultSet.next()){
                WaitlistEntry entry = new WaitlistEntry(resultSet.getString(1), date, resultSet.getInt(2), resultSet.getTimestamp(3));
                waitlist.add(entry);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return waitlist;
    }
    
    public static ArrayList<WaitlistEntry> getWaitlistByFaculty(String faculty){
        connection = DBConnection.getConnection();                                              // gets the waitlist by faculty
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();
        try{
            getWaitlist = connection.prepareStatement("select faculty, seats, timestamp from waitlist where faculty = ?");
            getWaitlist.setString(1, faculty);
            resultSet = getWaitlist.executeQuery();
            
            while(resultSet.next()){
                WaitlistEntry entry = new WaitlistEntry(faculty, resultSet.getDate(1), resultSet.getInt(2), resultSet.getTimestamp(3));
                waitlist.add(entry);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return waitlist;
    }    
    
    public static void addWaitlistEntry(String faculty, Date date, int seats){                                                  // adds to waitlist
        java.sql.Timestamp currentTimeStamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());               // timestamp
        connection = DBConnection.getConnection();
        try{
            addWaitlist = connection.prepareStatement("insert into waitlist (faculty, date, seats, timestamp) values (?,?,?,?)");
            addWaitlist.setString(1, faculty);
            addWaitlist.setDate(2, date);                       // adds to the waitlist table
            addWaitlist.setInt(3, seats);
            addWaitlist.setTimestamp(4, currentTimeStamp);
            addWaitlist.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static boolean deleteWaitlist(String faculty){
        connection = DBConnection.getConnection();
        boolean canceled = false;
        ArrayList<WaitlistEntry> waitlist = new ArrayList<>();              // have not used yet but explained in new video  
        try{
            getWaitlist = connection.prepareStatement("delete from waitlist where faculty = ?");
            getWaitlist.setString(1, faculty);
            getWaitlist.executeUpdate();
            canceled = true;
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return canceled;        
    }
}//ends class
