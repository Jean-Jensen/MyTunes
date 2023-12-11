package dk.MyTunes.BE;

public class PlaylistConnection extends Song { //Had to create this as a subclass to carry the orderID to the table display
    private int orderId;
    private int connectionID;
   private int playlistId;
   private Song song;

    public PlaylistConnection(int id, String name, String artist, String length, String fileType, int orderId,String filePath, int connectionID) {
        super(id, name, artist, String.valueOf(length), fileType,filePath);
        this.orderId = orderId;
        this.connectionID = connectionID;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getConnectionID() {
        return connectionID;
    }
}