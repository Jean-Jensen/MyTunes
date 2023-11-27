package dk.MyTunes.GUI;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.GUI.UpdateWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class AppController {
    @FXML
    private SplitPane splitPane;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<Song, String> columnSongs;
    @FXML
    private TableColumn<Song, String> columnArtists;
    @FXML
    private TableColumn<Song, String> columnAlbum;
    @FXML
    private TableColumn<Song, String> columnLength;
    @FXML
    private TableColumn<Song, String> columnFileType;
    @FXML
    private TableView<Song> tableViewDB;
    @FXML
    private TableColumn<Song, String> columnSongsDB;
    @FXML
    private TableColumn<Song, String> columnArtistsDB;
    @FXML
    private TableColumn<Song, String> columnAlbumDB;
    @FXML
    private TableColumn<Song, String> columnLengthDB;
    @FXML
    private TableColumn<Song, String> columnFileTypeDB;
    private Button openUpdateWindow;
    private BLLManager bllManager;

    public AppController() {
        this.bllManager = new BLLManager();
    }
    public void initialize() {
        // windowCenterBar(); //Keeps the middle of the splitpane centered relative to window(maybe not needed)
        coloumnSizes(); //This makes it so the header for the table (Coloumns) readjust to the window size
        showDBtable();
    }

    private void windowCenterBar() {
        splitPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Set the divider position to the middle of the SplitPane
            splitPane.setDividerPositions(0.5);
        });
    }

    private void coloumnSizes() {
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
    @FXML
    public void openUpdateWindow(ActionEvent actionEvent) throws IOException {
        try {
            Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();
            if(selectedSong != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/UpdateSongs.fxml"));
                int id = selectedSong.getId();
                UpdateWindow updatewindow = new UpdateWindow(id);
                loader.setController(updatewindow);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage primaryStage = new Stage();
                primaryStage.setTitle("Update Songs");
                primaryStage.setScene(scene);
                primaryStage.initModality(Modality.APPLICATION_MODAL);
                primaryStage.show();
                updatewindow.setAppController(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        }
    private void showDBtable() {
        columnSongsDB.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArtistsDB.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnAlbumDB.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnLengthDB.setCellValueFactory(new PropertyValueFactory<>("length"));
        columnFileTypeDB.setCellValueFactory(new PropertyValueFactory<>("fileType"));
        //commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        List<Song> songs = bllManager.getAllSongs();
        tableViewDB.getItems().setAll(songs);

    }
    public void updateSongInTableView(Song updatedSong) {
        // Find the index of the song in the TableView's items
        int index = tableViewDB.getItems().indexOf(updatedSong);

        // Replace the song in the TableView's items with the updated song
        if (index != -1) {
            tableViewDB.getItems().set(index, updatedSong);
        }
    }


}

