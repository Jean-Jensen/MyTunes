package dk.MyTunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyTunes.BE.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsDAO implements ISongsDAO {
    private final ConnectionManager cm = new ConnectionManager();

    @Override
  /*  public Song getSong(int id) {
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

                Song s = new Song(sid, name, artist, length, fileType,filePath);
                System.out.println(s);
                System.out.println("Connection Established");
                return s;

            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/


    public Song getSong(int id) {
        return null;
    }

    @Override
    public void deleteSong(int id) {
        try(Connection con = cm.getConnection()){
            String sql = "DELETE FROM SONGS WHERE ID = ?"; //command in SQL to delete a song
            PreparedStatement prStmt = con.prepareStatement(sql);

            //setting the values based on the song object we're adding (replacing the "?"s)
            prStmt.setString(1, String.valueOf(id));

            prStmt.executeUpdate(); //execute command in the database
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateSong(Song s) {
        try(Connection con = cm.getConnection())
        {
            String sql = "UPDATE songs SET name=?, artist=?, length=?, fileType=?, filePath=? WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getArtist());
            pstmt.setString(3, s.getLength());
            pstmt.setString(4, s.getFileType());
            pstmt.setString(5, s.getFilePath());
            pstmt.setInt(6, s.getId());
            pstmt.execute();

            boolean isResultSet = pstmt.execute();
            System.out.println("Is the first result a ResultSet? " + isResultSet);

            int updateCount = pstmt.getUpdateCount();
            System.out.println("Number of rows updated: " + updateCount);

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSong(Song s) {
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Song> getAllSongs() {
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
            throw new RuntimeException(e);
        }
        return songs;
    }

    public int getLastID(){
        //SELECT * FROM Songs
        //WHERE ID = (
        //    SELECT IDENT_CURRENT('Songs'))
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
            throw new RuntimeException(e);
        }
    }


}
