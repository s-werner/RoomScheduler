import java.sql.Date;
import java.sql.Timestamp;

public class ReservationEntry{
    private String faculty;
    private String room;
    private Date date;
    private int seats;
    private Timestamp timestamp;
    
    public ReservationEntry(String faculty, String room, Date date, int seats, Timestamp timestamp){
        this.faculty = faculty;
        this.room = room;
        this.date = date;               // updates all of the object info into the private variables
        this.seats = seats;
        this.timestamp = timestamp;
    }
    
    public String getFaculty(){
        return this.faculty;
    }
    public String getRoom(){
        return this.room;
    }
    public Date getDate(){              // getters for all the info. Used in the main GUI so we can access the info
        return this.date;
    }
    public int getSeats(){
        return this.seats;
    }
    public Timestamp getTimestamp(){
        return this.timestamp;
    }
}