public class RoomEntry {
    private String roomName;
    private int numSeats;
    
    public RoomEntry(String roomName, int numSeats){        // Another entry file that just makes the objects
        this.roomName = roomName;
        this.numSeats = numSeats;
    }
    
    public String getRoomName(){
        return this.roomName;
    }
    public int getNumSeats(){
        return this.numSeats;
    }
}
