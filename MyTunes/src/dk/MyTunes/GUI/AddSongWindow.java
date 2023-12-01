package dk.MyTunes.GUI;

import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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

    private AppController appController;

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
}
