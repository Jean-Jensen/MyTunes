package dk.MyTunes.BE;

public class PlaylistConnection extends Song { //Had to create this as a subclass to carry the orderID to the table display
    private int orderId;
    private int playlistId;

    public PlaylistConnection(int id, String name, String artist, String length, String fileType, int orderId, int playlistId) {
        super(id, name, artist, String.valueOf(length), fileType);
        this.orderId = orderId;
        this.playlistId = playlistId;
    }
    public int getOrderId() {
        return orderId;
    }
}