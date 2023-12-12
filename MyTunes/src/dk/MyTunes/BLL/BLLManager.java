package dk.MyTunes.BLL;

import dk.MyTunes.BE.Song;
import dk.MyTunes.DAL.SongsDAO;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.scene.control.Alert;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BLLManager {
    SongsDAO songsDAO = new SongsDAO();

    public void createSong(Song s) throws MyTunesExceptions {
        if(s.getArtist().isEmpty() || s.getName().isEmpty() || s.getLength().isEmpty() || s.getFilePath().isEmpty()) {
            errorAlert("Please check that none of the following fields are empty" +
                    "\nName\nArtist\nLength\nFilepath");
            throw new MyTunesExceptions("Empty fields");
        }
        if(!s.getFileType().equals(".wav") && !s.getFileType().equals(".mp3")) {
            errorAlert("Invalid file format");
            throw new MyTunesExceptions("Invalid file format");
        }
        try{
            songsDAO.createSong(s);
        } catch (SQLException e) {
            errorAlert(e);
            throw new MyTunesExceptions("Error creating song", e);
        }
    }

    public List<Song> searchForSong(String searchWord) throws MyTunesExceptions {
        try{
            return songsDAO.searchForSong(searchWord);
        }catch (SQLException e) {
            errorAlert(e);
            throw new MyTunesExceptions("Error getting all songs where name/artist is " + searchWord, e);
        }
    }

    public void deleteSong(int id) throws MyTunesExceptions {
        try{
            songsDAO.deleteSong(id);
        }catch (SQLException e) {
            errorAlert(e);
            throw new MyTunesExceptions("Error deleting song with ID " + id, e);
        }
    }

    public List<Song> getAllSongs() throws MyTunesExceptions {
        try{
            return songsDAO.getAllSongs();
        }  catch (SQLException e) {
            errorAlert(e);
            throw new MyTunesExceptions("Error getting all songs", e);
        }
    }

    public void updateSong(Song s) throws MyTunesExceptions {
        try{
            songsDAO.updateSong(s);
        }catch (SQLException e) {
            errorAlert(e);
            throw new MyTunesExceptions("Error updating song with ID " + s.getId(), e);
        }
    }

    public Song getSongById(int id) throws MyTunesExceptions {
        try{
            return songsDAO.getSong(id);
        }catch (SQLException e) {
            errorAlert(e);
            throw new MyTunesExceptions("Error getting song with ID " + id, e);
        }
    }

    public int getLastID() throws MyTunesExceptions {
        try{
            return songsDAO.getLastID();
        } catch (SQLException e) {
            errorAlert(e);
            throw new MyTunesExceptions("Error getting last song ID", e);
        }

    }

    public void errorAlert(Exception e){
        String message = e.getMessage() + "\nCheck console for errors and please try again";

        Logger.getLogger(SongsDAO.class.getName()).log(Level.SEVERE, null, e);

        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.setTitle("Error");
        alert.show();
    }
    public void errorAlert(String message){
        String messageDisplay = message + "\nPlease try again";

        Alert alert = new Alert(Alert.AlertType.ERROR, messageDisplay);
        alert.setTitle("Error");
        alert.show();
    }

}

