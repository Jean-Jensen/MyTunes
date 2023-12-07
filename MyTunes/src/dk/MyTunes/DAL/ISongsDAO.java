package dk.MyTunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.sql.SQLException;
import java.util.List;

public interface ISongsDAO {
   Song getSong(int id) throws MyTunesExceptions, SQLException;
    void deleteSong(int id) throws MyTunesExceptions, SQLException;
    void updateSong(Song s) throws MyTunesExceptions, SQLException;
    void createSong(Song s) throws MyTunesExceptions, SQLException;
    List<Song> getAllSongs() throws MyTunesExceptions, SQLException;
    int getLastID() throws MyTunesExceptions, SQLException;
}
