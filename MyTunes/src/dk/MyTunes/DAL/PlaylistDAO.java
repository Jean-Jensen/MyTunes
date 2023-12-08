package dk.MyTunes.DAL;

import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.PlaylistConnection;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlaylistDAO implements IPlaylistDAO {

    ConnectionManager cm = new ConnectionManager();
    SongsDAO songsDAO = new SongsDAO();

    @Override
    public void createPlaylist(String name) throws MyTunesExceptions {
        try (Connection con = cm.getConnection()) {
            String sql = "INSERT INTO Playlist(Name, Length, SongCount)" +
                    " VALUES(?, ?, ?)"; //command in SQL to add a new Song
            PreparedStatement prStmt = con.prepareStatement(sql);

            //setting the values based on the song object we're adding (replacing the "?"s)
            prStmt.setString(1, name);
            prStmt.setString(2, "00:00:00");
            prStmt.setString(3, "0");

            prStmt.executeUpdate(); //execute command in the database
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error creating song", e);
        }
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
    public void deletePlaylist(int id) throws MyTunesExceptions {
        try (Connection con = cm.getConnection()) {
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
            String sql = "INSERT INTO connection (PlaylistID, SongID, orderID2) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, songId);
            if(isPlaylistEmpty(playlistId)){
                pstmt.setInt(3, 0);
            } else{
                pstmt.setInt(3, getLastID(playlistId) + 1);
            }
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error adding song to playlist", e);
        }
    }

    public boolean isPlaylistEmpty(int playlistId) throws MyTunesExceptions {
        int count = 0;
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM playlist";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error adding song to playlist", e);
        }

        return count == 0;
    }


    @Override
    public void removeSongFromPlaylist(int orderId, int playlistID) throws MyTunesExceptions {
        try (Connection con = cm.getConnection()) {
            String sql = "DELETE FROM connection WHERE OrderID2 = ? and PlaylistID = ?"; //uses the order ID below to delete a song
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, orderId);
            stmt.setInt(2, playlistID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error removing song from playlist", e);
        }
    }

    public void moveSongUpPlaylist(int orderID, int playlistID) throws MyTunesExceptions {


        //Getting all the ID's from the playlist, so we know which one comes after the one we're looking for (so we can swap)
        List<Integer> IDs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM connection WHERE PlaylistID = ?"; //Checks what playlist I am on and then query's that
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, playlistID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int order = rs.getInt("orderID2");
                IDs.add(order);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting orderID", e);
        }
        for(int id: IDs){
            System.out.println(id);
        }
        Collections.sort(IDs);
        System.out.println("----------------------------------------");
        int nextID = IDs.get(IDs.indexOf(orderID) - 1); //getting the ID after the one we're looking to move up/swap
        int lastID = IDs.getLast();
        System.out.println("ID to swap: " + orderID);
        System.out.println("ID above it: " + nextID);
        System.out.println("Last ID: " + lastID);
        System.out.println();
        System.out.println();

        swapSongs(orderID, nextID, lastID);
    }

    public void swapSongs(int firstID, int secondID, int lastID) throws MyTunesExceptions {

        try (Connection con = cm.getConnection()) {

            //setting the 2nd ID to be a temp value
            String sql = "UPDATE connection SET orderID2=? WHERE orderID2=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(lastID + 60));
            //the "lastID + 30" will act as a temporary value since we can't have 2 connections with the same orderID and same playlistID
            //since that would mean if we search for that orderID we'll get 2 results.
            //I made it "lastID + 30" so that I know the value isn't taken
            pstmt.setString(2, String.valueOf(secondID));
            pstmt.execute();
            con.commit();

            //setting the first ID to be the same as the 2nd
            String sql2 = "UPDATE connection SET orderID2=? WHERE orderID2=?";
            pstmt = con.prepareStatement(sql2);
            pstmt.setString(1, String.valueOf(secondID));
            pstmt.setString(2, String.valueOf(firstID));
            pstmt.execute();
            con.commit();

            //setting the 2nd ID to be the same as the first
            String sql3 = "UPDATE connection SET orderID2=? WHERE orderID2=?";
            pstmt = con.prepareStatement(sql3);
            pstmt.setString(1, String.valueOf(firstID));
            pstmt.setString(2, String.valueOf(lastID + 60));
            pstmt.execute();
            con.commit();

        } catch (SQLException e) {
            throw new MyTunesExceptions("Error updating song with ID " + secondID, e);
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
                int orderId = rs.getInt("OrderID2"); //Gets the ID and assigns it to the constructed connection
                Song song = songsDAO.getSong(songId);
                PlaylistConnection connection = new PlaylistConnection(song.getId(), song.getName(), song.getArtist(), song.getLength(), song.getFileType(), orderId, song.getFilePath());
                connectionToDB.add(connection);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting orderID", e);
        }
        connectionToDB.sort(Comparator.comparing(PlaylistConnection::getOrderId)); //sort the list by order since I'm not sure if it will do that automatically
        return connectionToDB;
    }
    public int getLastID(int playlistID) throws MyTunesExceptions{
        int ID = 0;
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT MAX(OrderID2) FROM connection WHERE PlaylistID = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, playlistID);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                ID = rs.getInt(1);
            }
            return ID;
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting last song ID", e);
        }
    }


}
