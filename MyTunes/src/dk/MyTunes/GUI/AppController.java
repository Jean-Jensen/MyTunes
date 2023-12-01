package dk.MyTunes.GUI;


import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private BLLManager bllManager;  //The only thing the GUI talks to is the bllManager
    private MediaPlayer mediaPlayer;

    public AppController(){
        this.bllManager = new BLLManager();
    }
    public void initialize() {
        // windowCenterBar(); //Keeps the middle of the splitpane centered relative to window(maybe not needed)
        coloumnSizes(); //This makes it so the header for the table (Columns) readjust to the window size
        showDBtable();
        setVolumeSlider();
    }

    private void windowCenterBar() {
        splitPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Set the divider position to the middle of the SplitPane
            splitPane.setDividerPositions(0.5);
        });
    }

    private void coloumnSizes() {
        int numberOfColumns = 4;        //To adjust columns sizing with window size of playlist list
        columnSongs.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnArtists.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnLength.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));
        columnFileType.prefWidthProperty().bind(tableView.widthProperty().divide(numberOfColumns));

        int numberOfColumnsDB = 4;      //To adjust columns sizing with window size of Database list
        columnSongsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnArtistsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnLengthDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnFileTypeDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
    }

    public void play(ActionEvent actionEvent) {
        Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();
        playSong(selectedSong);
    }

    public void pause(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } else if (mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            }
        }
    }

    public void stop() {
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
        if(mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
            stop();
        }
        Media media = new Media(Paths.get(song.getFilePath()).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    public void setVolume(){
    }
    public void setVolumeSlider(){  //observable (the property that was changed[not used but needed for .addListener]), oldValue (the previous value of the property), and newValue (the new value of the property).
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                double sliderValue = newValue.doubleValue(); //This value is normally between 0 and 100 but next line makes this more precise, so we need a double
                double volume = (Math.log10(sliderValue) - 2) / -2; // Convert slider value to logarithmic scale
                mediaPlayer.setVolume(volume);
            }
        });
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

        List<Song> songs = bllManager.getAllSongs(); //Get all songs from the BLL layer through the getAllSongs method
        tableViewDB.getItems().setAll(songs);          //that talks to the DAL layer and returns a list of songs

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

