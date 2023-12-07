package dk.MyTunes.DAL;

import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.util.List;

public interface ISongsDAO {
   Song getSong(int id) throws MyTunesExceptions;
    void deleteSong(int id) throws MyTunesExceptions;
    void updateSong(Song s) throws MyTunesExceptions;
    void createSong(Song s) throws MyTunesExceptions;
    List<Song> getAllSongs() throws MyTunesExceptions;
    int getLastID() throws MyTunesExceptions;
}
