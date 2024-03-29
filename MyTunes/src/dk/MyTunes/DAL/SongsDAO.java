package dk.MyTunes.DAL;

import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsDAO implements ISongsDAO {
    private final ConnectionManager cm = new ConnectionManager();

    @Override
   public Song getSong(int id) throws MyTunesExceptions {
        try(Connection con = cm.getConnection())
       {
            String sql = "SELECT * FROM Songs WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int sid = rs.getInt("id");
                String name = rs.getString("name");
                String artist = rs.getString("artist");
                String length = rs.getString("length");
                String fileType = rs.getString("fileType");
                String filePath = rs.getString("filePath");

                return new Song(sid, name, artist, length, fileType,filePath);

            }
            return null;
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting song with ID " + id, e);
        }
   }

   public List<Song> searchForSong(String searchword) throws MyTunesExceptions {
        if(searchword.isEmpty()){
            return getAllSongs();
        }
       List<Song> songs = new ArrayList<>();
       try (Connection con = cm.getConnection()) {
           String sql = "SELECT * FROM songs a WHERE Name LIKE ?" +
                   "UNION SELECT * FROM songs WHERE Artist LIKE ?"; //"UNION" is so that there are no duplicate values 
           PreparedStatement pstmt = con.prepareStatement(sql);
           pstmt.setString(1, "%" + searchword + "%");
           pstmt.setString(2, "%" + searchword + "%");
           //the "%" are to indicate that we're searching for something
           //that CONTAINS this string and is not 1-to-1 equal to it
           ResultSet rs = pstmt.executeQuery();
           while (rs.next()) {
               int id = rs.getInt("id");
               String name = rs.getString("name");
               String artist = rs.getString("artist");
               String length = rs.getString("length");
               String fileType = rs.getString("fileType");
               String filePath = rs.getString("filePath");
               Song song = new Song(id, name, artist, length, fileType, filePath);
               songs.add(song);
           }
       } catch (SQLException e) {
           throw new MyTunesExceptions("Error getting all songs where name/artist is " + searchword, e);
       }
       return songs;
   }

    @Override
    public void deleteSong(int id) throws MyTunesExceptions{
        try(Connection con = cm.getConnection()){
            String sql = "DELETE FROM SONGS WHERE ID = ?"; //command in SQL to delete a song
            PreparedStatement prStmt = con.prepareStatement(sql);

            //setting the values based on the song object we're adding (replacing the "?"s)
            prStmt.setString(1, String.valueOf(id));

            prStmt.executeUpdate(); //execute command in the database
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error deleting song with ID " + id, e);
        }
    }

    @Override
    public void updateSong(int id, String newName, String newArtist) throws MyTunesExceptions{
        try(Connection con = cm.getConnection())
        {
            String sql = "UPDATE songs SET Name=?, artist=? WHERE id=?";
            PreparedStatement prStmt = con.prepareStatement(sql);
            prStmt.setString(1, newName);
            prStmt.setString(2, newArtist);
            prStmt.setInt(3, id);
            prStmt.executeUpdate();
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error changing name/artist", e);
        }
    }

    @Override
    public void createSong(Song s) throws MyTunesExceptions{
        try(Connection con = cm.getConnection()){
            String sql = "INSERT INTO songs(Name, Artist, Length, FileType, FilePath)" +
                    " VALUES(?, ?, ?, ?, ?)"; //command in SQL to add a new Song
            PreparedStatement prStmt = con.prepareStatement(sql);

            //setting the values based on the song object we're adding (replacing the "?"s)
            prStmt.setString(1, s.getName());
            prStmt.setString(2, s.getArtist());
            prStmt.setString(3, s.getLength());
            prStmt.setString(4, s.getFileType());
            prStmt.setString(5, s.getFilePath());

            prStmt.executeUpdate(); //execute command in the database
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error creating song", e);
        }
    }

    @Override
    public List<Song> getAllSongs() throws MyTunesExceptions{
        List<Song> songs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM songs";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String artist = rs.getString("artist");
                String length = rs.getString("length");
                String fileType = rs.getString("fileType");
                String filePath = rs.getString("filePath");
                Song song = new Song(id, name, artist, length, fileType, filePath);
                songs.add(song);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting all songs", e);
        }
        return songs;
    }

    public int getLastID() throws MyTunesExceptions{
        int ID = 0;
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM Songs WHERE ID = (SELECT IDENT_CURRENT('Songs'))";
            //"INDENT_CURRENT" finds the last identity value created for a table, which will always be the highest ID since it auto-increments
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                ID = rs.getInt("id");
            }
            return ID;
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting last song ID", e);
        }
    }


}
