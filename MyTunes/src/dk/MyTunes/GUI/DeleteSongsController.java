package dk.MyTunes.GUI;

import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteSongsController implements Initializable {

    @FXML
    private Label lblSong;


    private String name;
    private int ID;
    private BLLManager bll = new BLLManager();
    private AppController appController;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void setData(int id, String name){
        this.ID = id;
        this.name = name;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSong.setText(name);
    }

    public void deleteSong(ActionEvent actionEvent) throws MyTunesExceptions {
        bll.deleteSong(ID);
        //refresh all the tables after delete
        appController.showSongs();
        appController.displaySongsInPlaylist(null);
        appController.showPlayLists();
        closeWindow(actionEvent);
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stg = (Stage) lblSong.getScene().getWindow(); //fetching the current stage by getting it from the label (can be done with any child node)
        stg.close();
    }
}
