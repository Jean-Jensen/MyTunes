package dk.MyTunes.GUI;

import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.BLL.BLLPlaylist;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeletePlaylistController implements Initializable {

    @FXML
    private Label lblSong;


    private String name;
    private int ID;
    private BLLPlaylist bll = new BLLPlaylist();
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

    public void deletePlaylist(ActionEvent actionEvent) throws MyTunesExceptions {
        bll.deletePlaylist(ID);
        //refresh all the tables after delete
        appController.showPlayLists();
        appController.displaySongsInPlaylist(null);
        closeWindow(actionEvent);
    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stg = (Stage) lblSong.getScene().getWindow(); //fetching the current stage by getting it from the label (can be done with any child node)
        stg.close();
    }
}
