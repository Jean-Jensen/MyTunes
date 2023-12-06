package dk.MyTunes.GUI;

import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileReader;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;

public class AddSongWindow {
    @FXML
    private TextField artistField;
    @FXML
    private TextField lengthField;
    @FXML
    private TextField fileTypeField;
    @FXML
    private TextField filePathField;
    @FXML
    private TextField nameField;

    private BLLManager bll = new BLLManager();
    private String lengthString = "";

    private AppController appController;
    //filters so users can only select .mp3 or .wav files
    private FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(".mp3 files", "*.mp3");
    private FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter(".wav files", "*.wav");



    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void AddSong(ActionEvent actionEvent) throws MyTunesExceptions, SQLException {
        if(nameField.getText() != null && artistField.getText() != null && filePathField.getText() != null
                && fileTypeField.getText() != null && lengthField.getText() != null){
            Song s = new Song(bll.getLastID()+1, nameField.getText(), artistField.getText(), lengthField.getText(), fileTypeField.getText());
            //creates a new song object and with a temporary ID that uses the last ID of the table +1
            //since that's how the database would decide its ID
            s.setFilePath(filePathField.getText());


            bll.createSong(s);
            appController.showSongs();
        }


    }

    public void findSong(ActionEvent actionEvent) throws UnsupportedAudioFileException, IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Song");
        chooser.getExtensionFilters().addAll(filter1, filter2);
        File selected = chooser.showOpenDialog(nameField.getScene().getWindow());

        if(selected != null){
            setDataOfFile(selected);
        }
    }

    private void setDataOfFile(File file) { //sets the textfields to the correct value from the file
        nameField.setText(file.getName()); //gets filename
        String filepath = file.getPath(); //string value for the filepath (since we'll be reusing it)
        filePathField.setText(filepath); //sets filepath
        fileTypeField.setText(filepath.substring(filepath.lastIndexOf('.'))); //gets filetype by getting everything after the last instance of "."


        Media media = new Media(Paths.get(file.getPath()).toUri().toString()); //gets media from filepath
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            //the media.getDuration method can only work properly once
            //the mediaPlayer status is "Ready", thus why we need to wait. this is also why I had to make a mediaplayer
            lengthString = String.valueOf(media.getDuration().toSeconds()); //obtains duration of song in seconds
            //lengthString needs to be a variable outside of any method in order to be able to be changed here and used elsewhere
            lengthString = ((int) (Double.parseDouble(lengthString) / 60)) + ":" + String.valueOf(Double.parseDouble(lengthString) % 60).substring(0,5);
            //converting to time in seconds to seconds and minutes
            //the minutes are typecasted into an integer so that there are no decimal values (which would make it hard to read)

            lengthField.setText(lengthString);//sets the textfield to the songs length
            artistField.setText((String) media.getMetadata().get("artist")); //sets the "artist" field

        });

    }

}
