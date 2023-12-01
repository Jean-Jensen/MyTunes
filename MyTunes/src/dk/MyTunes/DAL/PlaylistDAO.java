package dk.MyTunes.DAL;

import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.util.List;

public class PlaylistDAO implements IPlaylistDAO {
    @Override
    public void createPlaylist(String name) throws MyTunesExceptions {

    }

    @Override
    public List<Playlist> getAllPlaylists() throws MyTunesExceptions {
        return null;
    }

    @Override
    public void updatePlaylist(Playlist p) throws MyTunesExceptions {

    }

    @Override
    public void deletePlaylist(int id) throws MyTunesExceptions {

    }

    @Override
    public void addSongToPlaylist(int songId, int playlistId) throws MyTunesExceptions {

    }

    @Override
    public void removeSongFromPlaylist(int songId, int playlistId) throws MyTunesExceptions {

    }

    @Override
    public List<Song> getSongsInPlaylist(int playlistId) throws MyTunesExceptions {
        return null;
    }
    // Your existing code here...
}
