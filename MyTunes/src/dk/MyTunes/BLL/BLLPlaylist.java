package dk.MyTunes.BLL;

import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.Song;
import dk.MyTunes.DAL.IPlaylistDAO;
import dk.MyTunes.DAL.PlaylistDAO;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.util.List;


public class BLLPlaylist {
    private IPlaylistDAO playlistDAO = new PlaylistDAO();

    public void createPlaylist(String name) throws MyTunesExceptions {
        playlistDAO.createPlaylist(name);
    }

    public List<Playlist> getAllPlaylists() throws MyTunesExceptions {
        return playlistDAO.getAllPlaylists();
    }

    public void updatePlaylist(Playlist p) throws MyTunesExceptions {
        playlistDAO.updatePlaylist(p);
    }

    public void deletePlaylist(int id) throws MyTunesExceptions {
        playlistDAO.deletePlaylist(id);
    }

    public void addSongToPlaylist(int songId, int playlistId) throws MyTunesExceptions {
        playlistDAO.addSongToPlaylist(songId, playlistId);
    }

    public void removeSongFromPlaylist(int songId, int playlistId) throws MyTunesExceptions {
        playlistDAO.removeSongFromPlaylist(songId, playlistId);
    }

    public List<Song> getSongsInPlaylist(int playlistId) throws MyTunesExceptions {
        return playlistDAO.getSongsInPlaylist(playlistId);
    }
}
