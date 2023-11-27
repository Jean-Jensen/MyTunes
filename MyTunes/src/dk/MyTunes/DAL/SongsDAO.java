package dk.MyTunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyTunes.BE.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsDAO implements ISongsDAO{
    private final ConnectionManager cm = new ConnectionManager();

    @Override
    public Song getSong(int id) {
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
                String album = rs.getString("album");
                String comment = rs.getString("comment");
                int year = rs.getInt("year");
                String genre = rs.getString("genre");
                String fileType = rs.getString("fileType");
                String filePath = rs.getString("filePath");



                Song s = new Song(sid, name, artist, length,album,comment,year,genre,fileType,filePath);
                System.out.println(s);
                System.out.println("Connection Established");
                return s;

            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSong(int id) {

    }

    @Override
    public void updateSong(Song s) {
        try(Connection con = cm.getConnection())
        {
            String sql = "UPDATE songs SET name=?, artist=?, length=?, album=?, comment=?, year=?, genre=?, fileType=?, filePath=? WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getArtist());
            pstmt.setString(3, s.getLength());
            pstmt.setString(4, s.getAlbum());
            pstmt.setString(5, s.getComment());
            pstmt.setInt(6, s.getYear());
            pstmt.setString(7, s.getGenre());
            pstmt.setString(8, s.getFileType());
            pstmt.setString(9, s.getFilePath());
            pstmt.setInt(10, s.getId());
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

    }

    @Override
    public List<Song> getAllSongs() {
        List<Song> songs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT id, name, artist, album, length, fileType, comment FROM songs";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String artist = rs.getString("artist");
                String album = rs.getString("album");
                String length = rs.getString("length");
                String fileType = rs.getString("fileType");
                //String comment = rs.getString("comment");
                Song song = new Song(id, name, artist, album, length, fileType);
                songs.add(song);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return songs;
    }
}