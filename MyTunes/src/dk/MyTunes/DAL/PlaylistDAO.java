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
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM playlist";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");

                // Get the songs for this playlist
                List<PlaylistConnection> connections = getPlaylistConnections(id);

                // Calculate the number of songs and their total length
                int songCount = connections.size();
                //Using connections.stream to convert to a Stream(collection of data)
                //This gives us access to map so we can .mapToInt to help us get a sum
                int totalLengthInSeconds = connections.stream()
                        .mapToInt(connection -> {
                            //We receive the song here as HH:MM:SS and add .split so
                            //when the string is received it knows where the breaks are
                            //and gives us 3 elements that we can use as ints
                            String[] hms = connection.getLength().split(":");
                            int hours = Integer.parseInt(hms[0]);
                            int minutes = Integer.parseInt(hms[1]);
                            int seconds = Integer.parseInt(hms[2]);
                            return hours * 3600 + minutes * 60 + seconds;
                        })
                        .sum();

                // Convert total length to "HH:mm:ss" format to match the AddSongWindow
                int hours = totalLengthInSeconds / 3600;
                int minutes = (totalLengthInSeconds % 3600) / 60;
                int seconds = totalLengthInSeconds % 60;
                String length = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                Playlist play = new Playlist(id, name, length, songCount);
                playlists.add(play);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting all Playlists", e);
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
