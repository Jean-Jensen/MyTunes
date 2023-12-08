package dk.MyTunes.GUI;

import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UpdateSongWindow {

    @FXML
    private TextField nameField;
    @FXML
    private TextField artistField;


    private BLLManager bllManager = new BLLManager();
    private int id;
    private AppController appController;

    public void setID(int songId){
        this.id = songId;
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    public void updateSong(ActionEvent actionEvent) throws MyTunesExceptions {
        Song s = bllManager.getSongById(id);

        if (!nameField.getText().isEmpty()) {
            s.setName(nameField.getText());
        }
        if (!artistField.getText().isEmpty()) {
            s.setArtist(artistField.getText());
        }

        bllManager.updateSong(s);

        appController.updateSongInTableView(s);
    }

}
