package dk.MyTunes.DAL;

import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.PlaylistConnection;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IPlaylistDAO {

    ConnectionManager cm = new ConnectionManager();
    SongsDAO songsDAO = new SongsDAO();
    @Override
    public void createPlaylist(String name) throws MyTunesExceptions {

    }

    @Override
    public List<Playlist> getAllPlaylists() throws MyTunesExceptions {
        List<Playlist> playlists = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
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
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting all Playlists", e);
        }
        return playlists;
    }

    @Override
    public void updatePlaylist(Playlist p) throws MyTunesExceptions {

    }

    @Override
    public void deletePlaylist(int id) throws MyTunesExceptions {
        try(Connection con = cm.getConnection()){
            String sql = "DELETE FROM Playlist WHERE ID = ?";
            PreparedStatement prStmt = con.prepareStatement(sql);
            prStmt.setString(1, String.valueOf(id));
            prStmt.executeUpdate(); //execute command in the database
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error deleting playlist with ID " + id, e);
        }
    }

    @Override
    public void addSongToPlaylist(int playlistId, int songId) throws MyTunesExceptions {
        try (Connection con = cm.getConnection()) {
            String sql = "INSERT INTO connection (PlaylistID, SongID) VALUES (?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, songId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error adding song to playlist", e);
        }
    }


    @Override
    public void removeSongFromPlaylist(int orderId) throws MyTunesExceptions {
        try (Connection con = cm.getConnection()) {
            String sql = "DELETE FROM connection WHERE OrderID = ?"; //uses the order ID below to delete a song
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error removing song from playlist", e);
        }
    }

    //Display a list to the table on GUI that carries over the order ID to make it possible to remove
    //duplicate songs.
    public List<PlaylistConnection> getPlaylistConnections(int playlistId) throws MyTunesExceptions {
        List<PlaylistConnection> connectionToDB = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
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
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting orderID", e);
        }
        return connectionToDB;
    }
}
