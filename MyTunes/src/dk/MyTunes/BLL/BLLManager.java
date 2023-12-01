package dk.MyTunes.BLL;

import dk.MyTunes.BE.Song;
import dk.MyTunes.DAL.ISongsDAO;
import dk.MyTunes.DAL.SongsDAO;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.util.List;

public class BLLManager {
    ISongsDAO songsDAO = new SongsDAO();

    public void createSong(Song s) throws MyTunesExceptions {
        if(!s.getFileType().equals(".wav") && !s.getFileType().equals(".mp3")) {
            throw new MyTunesExceptions("Invalid file format");
        }
        songsDAO.createSong(s);
    }

    public void deleteSong(int id) throws MyTunesExceptions {
        songsDAO.deleteSong(id);
    }

    public List<Song> getAllSongs() throws MyTunesExceptions {
        List<Song> songs = songsDAO.getAllSongs();
        return songs;
    }

    public void updateSong(Song s) throws MyTunesExceptions {
        songsDAO.updateSong(s);
    }

    public Song getSongById(int id) throws MyTunesExceptions {
                return songsDAO.getSong(id);
            }

    public int getLastID() throws MyTunesExceptions {
        return songsDAO.getLastID();
    }

}

