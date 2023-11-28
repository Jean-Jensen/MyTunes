package dk.MyTunes.BLL;

import dk.MyTunes.BE.Song;
import dk.MyTunes.DAL.ISongsDAO;
import dk.MyTunes.DAL.SongsDAO;
import dk.MyTunes.Exceptions.MyTunesExceptions;

import java.util.List;

public class BLLManager {
    SongsDAO songsDAO = new SongsDAO();

    public void createSong(Song s) throws MyTunesExceptions {
        if(!s.getFileType().equals(".wav") && !s.getFileType().equals(".mp3")) {
            throw new MyTunesExceptions("Invalid file format");
        }
        songsDAO.createSong(s);
    }

    public List<Song> getAllSongs() {
        List<Song> songs = songsDAO.getAllSongs();
        for (Song song : songs) {
            System.out.println(song);
            System.out.println("This is the BLL talking");
        }
        return songs;
    }

    public void updateSong(Song s){
        songsDAO.updateSong(s);
    }

    public Song getSongById(int id) {
        List<Song> songs = songsDAO.getAllSongs();
        for (Song song : songs) {
            if (song.getId() == id) {
                return song;
            }
        }
        return null;
    }

    public int getLastID(){
        return songsDAO.getLastID();
    }

}

