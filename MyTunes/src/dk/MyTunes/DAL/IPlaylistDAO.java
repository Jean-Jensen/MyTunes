package dk.MyTunes.DAL;


import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.PlaylistConnection;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IPlaylistDAO {
    void createPlaylist(String name) throws SQLException;
    List<Playlist> getAllPlaylists() throws SQLException;
    void updatePlaylist(Playlist p) throws MyTunesExceptions;
    void deletePlaylist(int id) throws SQLException;
    void addSongToPlaylist(int songId, int playlistId) throws SQLException;
    public void removeSongFromPlaylist(int orderId) throws SQLException;
    /*List<Song> getSongsInPlaylist(int playlistId) throws MyTunesExceptions;*/
    List<PlaylistConnection> getPlaylistConnections(int playlistId) throws SQLException;

}