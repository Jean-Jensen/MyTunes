package dk.MyTunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.PlaylistConnection;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import javax.naming.Name;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IPlaylistDAO {

    ConnectionManager cm = new ConnectionManager();
    SongsDAO songsDAO = new SongsDAO();

    @Override
    public void createPlaylist(String name) throws SQLException {
        Connection con = cm.getConnection();
        String sql = "INSERT INTO Playlist(Name, Length, SongCount)" +
                " VALUES(?, ?, ?)"; //command in SQL to add a new Song
        PreparedStatement prStmt = con.prepareStatement(sql);

        //setting the values based on the song object we're adding (replacing the "?"s)
        prStmt.setString(1, name);
        prStmt.setString(2, "00:00:00");
        prStmt.setString(2, "0");

        prStmt.executeUpdate(); //execute command in the database
    }

    @Override
    public List<Playlist> getAllPlaylists() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        Connection con = cm.getConnection();
        String sql = "SELECT * FROM playlist";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String length = rs.getString("length");
            String songCount = rs.getString("songcount");
            Playlist play = new Playlist(id, name, length, Integer.parseInt(songCount));
            playlists.add(play);
        }

        return playlists;
    }

    @Override
    public void updatePlaylist(Playlist p) throws MyTunesExceptions {

    }

    @Override
    public void deletePlaylist(int id) throws SQLException {
        Connection con = cm.getConnection();
        String sql = "DELETE FROM Playlist WHERE ID = ?";
        PreparedStatement prStmt = con.prepareStatement(sql);
        prStmt.setString(1, String.valueOf(id));
        prStmt.executeUpdate(); //execute command in the database

    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) throws SQLException {
        Connection con = cm.getConnection();
        String sql = "INSERT INTO connection (PlaylistID, SongID) VALUES (?, ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, playlistId);
        pstmt.setInt(2, songId);
        pstmt.executeUpdate();

    }


    @Override
    public void removeSongFromPlaylist(int orderId) throws SQLException {
        Connection con = cm.getConnection();
        String sql = "DELETE FROM connection WHERE OrderID = ?"; //uses the order ID below to delete a song
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, orderId);
        stmt.executeUpdate();
    }

    //Display a list to the table on GUI that carries over the order ID to make it possible to remove
    //duplicate songs.
    public List<PlaylistConnection> getPlaylistConnections(int playlistId) throws SQLException {
        List<PlaylistConnection> connectionToDB = new ArrayList<>();
        Connection con = cm.getConnection();
        String sql = "SELECT * FROM connection WHERE PlaylistID = ?"; //Checks what playlist I am on and then query's that
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, playlistId);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int songId = rs.getInt("SongID");
            int orderId = rs.getInt("OrderID"); //Gets the ID and assigns it to the constructed connection
            Song song = songsDAO.getSong(songId);
            PlaylistConnection connection = new PlaylistConnection(song.getId(), song.getName(), song.getArtist(), song.getLength(), song.getFileType(), orderId, song.getFilePath());
            connectionToDB.add(connection);
        }

        return connectionToDB;
    }
}
