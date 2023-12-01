package dk.MyTunes.DAL;

import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.Song;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements IPlaylistDAO {

    ConnectionManager cm = new ConnectionManager();
    SongsDAO songsDAO = new SongsDAO();
    @Override
    public void createPlaylist(String name) throws MyTunesExceptions {

    }

    @Override
    public List<Playlist> getAllPlaylists() throws MyTunesExceptions {
        List<Playlist> playlists = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM playlist";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("playlist added");
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String length = rs.getString("length");
                String songCount = rs.getString("songcount");
                Playlist play = new Playlist(id, name, length, Integer.parseInt(songCount));
                playlists.add(play);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting all Playlists", e);
        }
        return playlists;
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
        List<Integer> IDs = getIdsOfAllSongsInPlaylist(playlistId);
        List<Song> songs = new ArrayList<>();
        for(int ID : IDs){
            System.out.println("addedSong" + ID);
            songs.add(songsDAO.getSong(ID));
        }
        return songs;
    }

    private List<Integer> getIdsOfAllSongsInPlaylist(int playlistID) throws MyTunesExceptions {
        List<Integer> IDs = new ArrayList<>();
        try (Connection con = cm.getConnection()) {
            String sql = "SELECT * FROM connection WHERE PlaylistID = " + playlistID;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("SongID");
                IDs.add(id);
            }
        } catch (SQLException e) {
            throw new MyTunesExceptions("Error getting all Connections", e);
        }
        for(int ID : IDs){
            System.out.println(ID);
        }
        return IDs;
    }
}
