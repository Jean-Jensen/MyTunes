package dk.MyTunes.BLL;

import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.PlaylistConnection;
import dk.MyTunes.DAL.IPlaylistDAO;
import dk.MyTunes.DAL.PlaylistDAO;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.sql.SQLException;
import java.util.List;


public class BLLPlaylist {

    private BLLManager bll = new BLLManager();
    IPlaylistDAO playlistDAO = new PlaylistDAO();

    public void createPlaylist(String name) throws MyTunesExceptions {
        try {
            playlistDAO.createPlaylist(name);
        } catch (SQLException e) {
            bll.errorAlert(e);
            throw new MyTunesExceptions("Error creating playlist", e);
        }
    }

    public List<Playlist> getAllPlaylists() throws MyTunesExceptions {
        try {
            return playlistDAO.getAllPlaylists();
        } catch (SQLException e) {
            bll.errorAlert(e);
            throw new MyTunesExceptions("Error getting all Playlists", e);
        }
    }

    public void updatePlaylist(int id, String newName) throws MyTunesExceptions {
        playlistDAO.updatePlaylist(id, newName);
    }

    public void deletePlaylist(int id) throws MyTunesExceptions {
        try {
            playlistDAO.deletePlaylist(id);
        } catch (SQLException e) {
            bll.errorAlert(e);
            throw new MyTunesExceptions("Error deleting playlist with ID " + id, e);
        }
    }

    public void addSongToPlaylist(int songId, int playlistId) throws MyTunesExceptions {
        try {
            playlistDAO.addSongToPlaylist(songId, playlistId);
        } catch (SQLException e) {
            bll.errorAlert(e);
            throw new MyTunesExceptions("Error adding song to playlist", e);
        }
    }

    public void removeSongFromPlaylist(int connectionID) throws MyTunesExceptions {
        try {
            playlistDAO.removeSongFromPlaylist(connectionID);
        } catch (SQLException e) {
            bll.errorAlert(e);
            throw new MyTunesExceptions("Error removing song from playlist", e);
        }
    }
    public void moveSongUpPlaylist(int orderID, int playlistID) throws MyTunesExceptions {
     try {
        playlistDAO.moveSongUpPlaylist(orderID, playlistID);
    } catch (SQLException e) {
        bll.errorAlert(e);
        throw new MyTunesExceptions("Error moving song up playlist", e);
    }
}
    public void moveSongDownPlaylist(int orderID, int playlistID) throws MyTunesExceptions {
        try {
            playlistDAO.moveSongDownPlaylist(orderID, playlistID);
        } catch (SQLException e) {
            bll.errorAlert(e);
            throw new MyTunesExceptions("Error moving song up playlist", e);
        }
    }

    public List<PlaylistConnection> getPlaylistConnections(int playlistId) throws MyTunesExceptions {
        try {
            return playlistDAO.getPlaylistConnections(playlistId);
        } catch (SQLException e) {
            bll.errorAlert(e);
            throw new MyTunesExceptions("Error getting orderID", e);
        }
    }
}