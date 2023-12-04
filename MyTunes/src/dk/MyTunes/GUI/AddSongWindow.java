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
    private double length = 0;
    private String lengthString = "";

    private AppController appController;
    //filters so users can only select .mp3 or .wav files
    private FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(".mp3 files", "*.mp3");
    private FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter(".wav files", "*.wav");



    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void AddSong(ActionEvent actionEvent) throws MyTunesExceptions {
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
            String filepath = selected.getPath();
            filePathField.setText(filepath);
            fileTypeField.setText(filepath.substring(filepath.lastIndexOf('.')));
            setAudioFileLength(selected);
        }
    }

    private void setAudioFileLength(File file) { //sets the textfield "lengthField" to the current length
        Media media = new Media(Paths.get(file.getPath()).toUri().toString()); //gets media from filepath
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setOnReady(() -> {
            //the media.getDuration method can only work properly once
            //the mediaPlayer status is "Ready", thus why we need to wait. this is also why I had to make a mediaplayer
            lengthString = media.getDuration().toString(); //obtains duration of song
            //lengthString needs to be a variable outside of any method in order to be able to be changed here and used elsewhere
            lengthField.setText(lengthString);//sets the textfield to the songs length
        });

    }

}
