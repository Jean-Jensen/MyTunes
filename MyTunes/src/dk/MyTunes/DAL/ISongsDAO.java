package dk.MyTunes.DAL;

import dk.MyTunes.BE.Song;

import java.util.List;

public interface ISongsDAO {
    public Song getSong(int id);
    public void deleteSong(int id);
    public void updateSong(Song s);
    public void createSong(Song s);
    public List<Song> getAllSongs();
}
