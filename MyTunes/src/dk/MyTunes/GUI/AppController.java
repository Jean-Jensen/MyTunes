package dk.MyTunes.GUI;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;


public class AppController {
    @FXML
    private Slider volumeSlider;
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
        //columnAlbum.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnLength.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnFileType.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));

        int numberOfColumnsDB = 5;      //To adjust columns sizing with window size of Database list
        columnSongsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnArtistsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        //columnAlbumDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnLengthDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnFileTypeDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
    }

    private MediaPlayer mediaPlayer;

    public void play(ActionEvent actionEvent) {
        Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();
        playSong(selectedSong);
    }

    public void pause(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void next(ActionEvent actionEvent) {
        // Get the index of the selected song
        int selectedIndex = tableViewDB.getSelectionModel().getSelectedIndex();
        // Get the next song
        Song nextSong = tableViewDB.getItems().get(selectedIndex + 1);
        // Play the next song
        playSong(nextSong);
    }

    public void prev(ActionEvent actionEvent) {
        // Get the index of the selected song
        int selectedIndex = tableViewDB.getSelectionModel().getSelectedIndex();
        // Get the previous song
        Song prevSong = tableViewDB.getItems().get(selectedIndex - 1);
        // Play the previous song
        playSong(prevSong);
    }

    private void playSong(Song song) {
        Media media = new Media(Paths.get("src/dk/MyTunes/DAL/Songs/" + song.getFilePath()).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void setVolume(DragEvent dragEvent) {
        // Get the value from the volume slider
        double volume = volumeSlider.getValue();
        // Set the volume of the media player
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }


    public void renamePlaylist(ActionEvent actionEvent) {
    }

    public void deletePlaylist(ActionEvent actionEvent) {
    }

    public void savePlaylist(ActionEvent actionEvent) {
    }

    public void addSong(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/AddSongs.fxml"));
        Parent root = loader.load();
        AddSongWindow controller = loader.getController();
        controller.setAppController(this);
        openNewScene(root, "Add Song");
    }

    public void removeSong(ActionEvent actionEvent) throws IOException {
        Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();
        if(selectedSong != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/DeleteSongs.fxml"));
            Parent root = loader.load();
            DeleteSongsController controller = loader.getController();
            controller.setData(selectedSong.getId(),selectedSong.getName());
            controller.setAppController(this);
            openNewScene(root,"RemoveSong");
        }
    }
    @FXML
    public void openUpdateWindow(ActionEvent actionEvent) throws IOException {
        try {
            Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();
            if(selectedSong != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/UpdateSongs.fxml"));
                int id = selectedSong.getId();
                Parent root = loader.load();
                UpdateSongWindow controller = loader.getController();
                controller.setAppController(this);
                controller.setID(id);
                openNewScene(root,"Update Song");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //in order to avoid repeating the same lines of code over and over
    private void openNewScene(Parent root, String title) throws IOException {
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }
    public void showDBtable() {
        columnSongsDB.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArtistsDB.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnLengthDB.setCellValueFactory(new PropertyValueFactory<>("length"));
        columnFileTypeDB.setCellValueFactory(new PropertyValueFactory<>("fileType"));

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

