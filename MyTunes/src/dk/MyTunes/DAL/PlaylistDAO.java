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
            String sql = "DELETE FROM connection WHERE OrderID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error removing song from playlist", e);
        }
    }

    public List<PlaylistConnection> getPlaylistConnections(int playlistId) throws MyTunesExceptions {
        List<Integer> songIDs = new ArrayList<>();
        List<Integer> orderIDs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM connection WHERE PlaylistID = " + playlistId;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int songId = rs.getInt("SongID");
                int orderId = rs.getInt("OrderID");
                songIDs.add(songId);
                orderIDs.add(orderId);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting all Connections", e);
        }

        List<PlaylistConnection> connections = new ArrayList<>();
        for(int i = 0; i < songIDs.size(); i++){
            Song song = songsDAO.getSong(songIDs.get(i));
            PlaylistConnection connection = new PlaylistConnection(song.getId(), song.getName(), song.getArtist(), song.getLength(), song.getFileType(), orderIDs.get(i), playlistId);
            connections.add(connection);
        }
        return connections;
    }


    /*@Override
    public List<Song> getSongsInPlaylist(int playlistId) throws MyTunesExceptions {
        List<Integer> IDs = getIdsOfAllSongsInPlaylist(playlistId);
        List<Song> songs = new ArrayList<>();
        for(int ID : IDs){
          //  System.out.println("addedSong" + ID);
            songs.add(songsDAO.getSong(ID));
        }
        return songs;
    }

    /*private List<Integer> getIdsOfAllSongsInPlaylist(int playlistID) throws MyTunesExceptions {
        List<Integer> IDs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM connection WHERE PlaylistID = " + playlistID;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("SongID");
                IDs.add(id);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting all Connections", e);
        }
        for(int ID : IDs){
          //  System.out.println(ID);
        }
        return IDs;
    }*/
}
