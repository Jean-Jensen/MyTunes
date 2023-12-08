package dk.MyTunes.GUI;

import dk.MyTunes.BLL.BLLPlaylist;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPlaylistController {
    @FXML
    private TextField txtName;
    private BLLPlaylist bllPlaylist = new BLLPlaylist();

    private AppController appController;
    public void setAppController(AppController appController) {
        this.appController = appController;
    }
    public void createPlaylist(ActionEvent actionEvent) throws MyTunesExceptions {
        if(txtName.getText() != null){
            bllPlaylist.createPlaylist(txtName.getText());
            appController.showPlayLists();
            Stage stage = (Stage) txtName.getScene().getWindow();
            stage.close();
        }
    }
}
