import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ReservationQueries{
    private static Connection connection;
    private static PreparedStatement addReservation;
    private static PreparedStatement getReservationList;
    private static PreparedStatement getReservationByRoom;
    private static PreparedStatement getRoomList;
    private static ResultSet resultSet;
    
    public static boolean addReservationEntry(String faculty, Date date, int seats){
        boolean addedToReservation = false;     // This is the variable we get in the main GUI which decides what the status button is going to say(reservation/waitlist)
        boolean resMade = false;                // so we do not put the same request in multiple rooms on one button press
        connection = DBConnection.getConnection();  // get connection to database
        ArrayList<RoomEntry> rooms = RoomQueries.getRoomList();   // makes an array of room entries.
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()); // timestamp
                                                   //LOOPS THROUGH EACH ROOM 
        for(int i = 0; i < rooms.size(); i++){     // I IS THE INDEX OF THE ROOM
            
            RoomEntry room = rooms.get(i);         //making up a room entry
            if(seats <= room.getNumSeats()){       // requested seats must be smaller than available
                ArrayList<RoomEntry> roomsReserved = getRoomsReservedByDate(date); // list of all rooms by date
                boolean roomUsed = false;          // if the room we are looking at is available or not
                for(int j = 0; j < roomsReserved.size(); j++){    // loop through to see if the room is available
                    if(roomsReserved.get(j).getRoomName().equals(room.getRoomName())){ 
                        roomUsed = true;    // if it is used set 'roomUsed' to true so we do not use it
                    }
                }
                if(!resMade){  // if there is not a reservation made already
                    if(roomsReserved.size() == 0 || !roomUsed){ // if there are no reservation we will obviously go through or if room is not used
                        try{
                            addReservation = connection.prepareStatement("insert into reservations (faculty, room, date, seats, timestamp) values (?,?,?,?,?)");
                            addReservation.setString(1, faculty);
                            addReservation.setString(2, room.getRoomName());
                            addReservation.setDate(3, date);                        // insert all of our data into the reservation table
                            addReservation.setInt(4, seats);
                            addReservation.setTimestamp(5, currentTimestamp);
                            addReservation.executeUpdate();
                            resMade = true;                                         // set our reservationMade to true cause we just made one
                            addedToReservation = true;                              // also the one to tell us which one we did
                        }
                        catch(SQLException sqlException){
                            sqlException.printStackTrace();
                        }
                    }
                    else{
                        resMade = true;
                    }
                }
            }
        }
        if(!addedToReservation){
            WaitlistQueries.addWaitlistEntry(faculty, date, seats);
        }
        return addedToReservation;   // return our decision on reservation or waitlist
    }//ends addReservationStatus
    
    public static ArrayList<ReservationEntry> getReservationsByRoom(String room){
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservation = new ArrayList<>();
        try{
            getReservationByRoom = connection.prepareStatement("select faculty, date, seats, timestamp from reservations where room = ?");
            getReservationByRoom.setString(1, room);
            resultSet = getReservationByRoom.executeQuery();
            while(resultSet.next()){
                ReservationEntry list = new ReservationEntry(resultSet.getString(1), room, resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4));
                reservation.add(list);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return reservation;
    }
    
    public static ArrayList<ReservationEntry> getReservationsByDate(Date date){
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservation = new ArrayList<>();
        try{
            getReservationList = connection.prepareStatement("select faculty, room, seats, timestamp from reservations where date = ?");
            getReservationList.setDate(1, date);
            resultSet = getReservationList.executeQuery();              // this one just gets the reservation by the date provided
            
            while(resultSet.next()){
                ReservationEntry list = new ReservationEntry(resultSet.getString(1), resultSet.getString(2), date, resultSet.getInt(3), resultSet.getTimestamp(4));
                reservation.add(list);
            }
            
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return reservation;
    }
    
    public static ArrayList<RoomEntry> getRoomsReservedByDate(Date date){
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> rooms = new ArrayList<RoomEntry>();      // this one gets the rooms by what date they want to see      
        try{
            getRoomList = connection.prepareStatement("select room, seats from reservations where date = ?");
            getRoomList.setDate(1, date);
            resultSet = getRoomList.executeQuery();
            
            while(resultSet.next()){
                RoomEntry room = new RoomEntry(resultSet.getString(1), resultSet.getInt(2));
                rooms.add(room);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return rooms;
    }
    
    public static void deleteReservation(String room){            // do not know why we need this yet but it was in the Room Scheduler Design Layout so I put it in
        connection = DBConnection.getConnection();
        try{
            getRoomList = connection.prepareStatement("delete from reservations where room = ?");
            getRoomList.setString(1, room);
            getRoomList.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static ArrayList<ReservationEntry> getReservationByFaculty(String faculty){   // gets reservations the faculty made
        connection = DBConnection.getConnection();
        ArrayList<ReservationEntry> reservation = new ArrayList<ReservationEntry>();
        try{
            getReservationList = connection.prepareStatement("select room, date, seats, timestamp from reservations where faculty = ?");
            getReservationList.setString(1, faculty);
            resultSet =getReservationList.executeQuery();
            
            while(resultSet.next()){
                ReservationEntry reserve = new ReservationEntry(faculty, resultSet.getString(1), resultSet.getDate(2), resultSet.getInt(3), resultSet.getTimestamp(4));
                reservation.add(reserve);
            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return reservation;
    } 
    
    public static boolean cancelReservation(String faculty, Date date){
        connection = DBConnection.getConnection();
        boolean canceled = false;
        try{
            getRoomList = connection.prepareStatement("delete from reservations where faculty = ? and date = ?");
            getRoomList.setString(1, faculty);
            getRoomList.setDate(2, date);
            getRoomList.executeUpdate();
            canceled = true;
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
        return canceled;
    }
}// edns class
