package dk.MyTunes.DAL;

import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.util.List;

public interface ISongsDAO {
    public Song getSong(int id) throws MyTunesExceptions;
    public void deleteSong(int id) throws MyTunesExceptions;
    public void updateSong(Song s) throws MyTunesExceptions;
    public void createSong(Song s) throws MyTunesExceptions;
    public List<Song> getAllSongs() throws MyTunesExceptions;
    public int getLastID() throws MyTunesExceptions;
}
