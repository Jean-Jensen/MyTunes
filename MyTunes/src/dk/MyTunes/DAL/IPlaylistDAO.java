package dk.MyTunes.DAL;


import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import java.util.List;

public interface IPlaylistDAO {
    void createPlaylist(String name) throws MyTunesExceptions;
    List<Playlist> getAllPlaylists() throws MyTunesExceptions;
    void updatePlaylist(Playlist p) throws MyTunesExceptions;
    void deletePlaylist(int id) throws MyTunesExceptions;
    void addSongToPlaylist(int songId, int playlistId) throws MyTunesExceptions;
    void removeSongFromPlaylist(int songId, int playlistId) throws MyTunesExceptions;
    List<Song> getSongsInPlaylist(int playlistId) throws MyTunesExceptions;
}