import java.sql.Date;
import java.sql.Timestamp;

public class WaitlistEntry {
    private String faculty;
    private Date date;
    private int seats;
    private Timestamp timestamp;
 
    public WaitlistEntry(String faculty, Date date, int seats, Timestamp timestamp){
        this.faculty = faculty;
        this.date = date;
        this.seats = seats;
        this.timestamp = timestamp;         // makes waitlist objects
    }
    public Timestamp getTimestamp(){
        return this.timestamp;
    }
    public String getFaculty(){
        return this.faculty;
    }
    public Date getDate(){
        return this.date;
    }
    public int getSeats(){
        return this.seats;
    }
}
