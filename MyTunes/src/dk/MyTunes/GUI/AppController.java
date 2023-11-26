package dk.MyTunes.GUI;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.DragEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AppController {
    @FXML
    private SplitPane splitPane;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> columnSongs;
    @FXML
    private TableColumn<?, ?> columnArtists;
    @FXML
    private TableColumn<?, ?> columnAlbum;
    @FXML
    private TableColumn<?, ?> columnLength;
    @FXML
    private TableColumn<?, ?> columnFileType;
    @FXML
    private TableView<?> tableViewDB;
    @FXML
    private TableColumn<?, ?> columnSongsDB;
    @FXML
    private TableColumn<?, ?> columnArtistsDB;
    @FXML
    private TableColumn<?, ?> columnAlbumDB;
    @FXML
    private TableColumn<?, ?> columnLengthDB;
    @FXML
    private TableColumn<?, ?> columnFileTypeDB;



    public void initialize() {

        windowCenterBar();
        coloumnSizes();

}

    private void windowCenterBar(){ // Keeps the middle of the splitpane centered relative to window
    splitPane.widthProperty().addListener((obs, oldVal, newVal) -> {
        // Set the divider position to the middle of the SplitPane
        splitPane.setDividerPositions(0.5);
    });
}

private void coloumnSizes(){
        int numberOfColumns = 5;        //To adjust columns sizing with window size of playlist list
        columnSongs.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnArtists.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnAlbum.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnLength.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnFileType.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));

        int numberOfColumnsDB = 5;      //To adjust columns sizing with window size of Database list
        columnSongsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnArtistsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnAlbumDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnLengthDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnFileTypeDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
    }
    public void prev(ActionEvent actionEvent) {
    }

    public void play(ActionEvent actionEvent) {
    }

    public void pause(ActionEvent actionEvent) {
    }

    public void stop(ActionEvent actionEvent) {
    }

    public void next(ActionEvent actionEvent) {
    }

    public void setVolume(DragEvent dragEvent) {
    }


    public void renamePlaylist(ActionEvent actionEvent) {
    }

    public void deletePlaylist(ActionEvent actionEvent) {
    }

    public void savePlaylist(ActionEvent actionEvent) {
    }

    public void addSong(ActionEvent actionEvent) {
    }

    public void removeSong(ActionEvent actionEvent) {
    }

}

