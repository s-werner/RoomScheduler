import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomQueries {
    private static Connection connection;
    private static ArrayList<String> rooms = new ArrayList<String>();
    private static PreparedStatement getRoomList;
    private static PreparedStatement addRoom;
    private static PreparedStatement dropRoom;
    private static ResultSet resultSet;

    public static ArrayList<RoomEntry> getRoomList(){
        connection = DBConnection.getConnection();
        ArrayList<RoomEntry> rooms = new ArrayList<RoomEntry>();
        try{
            getRoomList = connection.prepareStatement("select name, seats from rooms order by seats");     // gets the list of rooms 
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
    
    public static void addRoom(String name, int seats){
        connection = DBConnection.getConnection();
        try{
            addRoom = connection.prepareStatement("insert into rooms (name, seats) values (?,?)"); // adds a room. Have not used this either
            addRoom.setString(1, name);
            addRoom.setInt(2, seats);
            addRoom.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
    
    public static void dropRoom(String name){
        connection = DBConnection.getConnection();
        try{
            dropRoom = connection.prepareStatement("delete from rooms where name = ?");     // nor this one
            dropRoom.setString(1, name);
            dropRoom.executeUpdate();
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }
}
    