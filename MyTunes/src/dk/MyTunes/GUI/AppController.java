package dk.MyTunes.GUI;


import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.BLL.BLLPlaylist;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.nio.file.Paths;
import dk.MyTunes.BE.PlaylistConnection;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;
import java.util.logging.Logger;


public class AppController {

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView tablePlaylists;
    @FXML
    private TableColumn colPlaylistName;
    @FXML
    private TableColumn colSongCount;
    @FXML
    private TableColumn colLength;
    @FXML
    private Slider volumeSlider;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TableView<PlaylistConnection> tableSongsFromPlayList;
    @FXML
    private TableColumn<PlaylistConnection, String> columnSongs;
    @FXML
    private TableColumn<PlaylistConnection, String> columnArtists;
    @FXML
    private TableColumn<PlaylistConnection, String> columnLength;
    @FXML
    private TableColumn<PlaylistConnection, String> columnFileType;
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
    @FXML
    private Button addSongToPlaylistButton;
    @FXML
    private Button removeSongFromPlaylistButton;
    @FXML
    private ToggleButton togglePlayPause;
    private TableView<PlaylistConnection> currentTableView;
    private BLLManager bllManager;  //The only thing the GUI talks to is the bllManager
    private BLLPlaylist bllPlaylist = new BLLPlaylist();
    private MediaPlayer mediaPlayer;
    private Song selectedSong;
    private int currentSong;
    private int previousSong = -1;


    public AppController() {
        this.bllManager = new BLLManager();
    }

    public void initialize() throws MyTunesExceptions {
        songSelector();
        toolTips();
        // windowCenterBar(); //Keeps the middle of the splitpane centered relative to window(maybe not needed)
        coloumnSizes(); //This makes it so the header for the table (Columns) readjust to the window size
        showSongs();
        showPlayLists();
        setVolumeSlider();
    }
public void toolTips(){
    Tooltip tooltipAddSong = new Tooltip("Add selected song to the selected playlist");
        Tooltip.install(addSongToPlaylistButton, tooltipAddSong);

    Tooltip tooltipRemoveSong = new Tooltip("Remove selected song from the selected playlist");
    Tooltip.install(removeSongFromPlaylistButton, tooltipRemoveSong);

}
    private void windowCenterBar() {
        splitPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Set the divider position to the middle of the SplitPane
            splitPane.setDividerPositions(0.5);
        });
    }

    private void coloumnSizes() {
        int numberOfColumns = 4;        //To adjust columns sizing with window size of playlist list
        columnSongs.prefWidthProperty().bind(tableSongsFromPlayList.widthProperty().divide(numberOfColumns));
        columnArtists.prefWidthProperty().bind(tableSongsFromPlayList.widthProperty().divide(numberOfColumns));
        columnLength.prefWidthProperty().bind(tableSongsFromPlayList.widthProperty().divide(numberOfColumns));
        columnFileType.prefWidthProperty().bind(tableSongsFromPlayList.widthProperty().divide(numberOfColumns));

        int numberOfColumnsDB = 4;      //To adjust columns sizing with window size of Database list
        columnSongsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnArtistsDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnLengthDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));
        columnFileTypeDB.prefWidthProperty().bind(tableViewDB.widthProperty().divide(numberOfColumnsDB));

        int numberOfColumnsPlaylist = 3;
        colPlaylistName.prefWidthProperty().bind(tablePlaylists.widthProperty().divide(numberOfColumnsPlaylist));
        colLength.prefWidthProperty().bind(tablePlaylists.widthProperty().divide(numberOfColumnsPlaylist));
        colSongCount.prefWidthProperty().bind(tablePlaylists.widthProperty().divide(numberOfColumnsPlaylist));
    }

public void songSelector(){
    // Add a listener to the selection model of tableSongsFromPlayList
    tableSongsFromPlayList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            selectedSong = newSelection;
            currentTableView = tableSongsFromPlayList;
        }
    });
    // Add a listener to the selection model of tableViewDB
    tableViewDB.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
        if (newSelection != null) {
            selectedSong = newSelection;
            currentTableView = tableSongsFromPlayList;
        }
    });
}


    public void togglePlayPause(ActionEvent actionEvent) {
        if (selectedSong == null) {
            return;
        }

        boolean isMediaPlayerDefined = mediaPlayer != null;
        // Check if mediaPlayer is currently playing a song
        boolean isMediaPlayerPlaying = isMediaPlayerDefined && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
        // Check if the same song is already loaded in the mediaPlayer
        boolean isSameSongPlaying = isMediaPlayerDefined && mediaPlayer.getMedia().getSource().equals(Paths.get(selectedSong.getFilePath()).toUri().toString());

        if (isMediaPlayerPlaying) {
            mediaPlayer.pause();
            // If a different song is loaded, play the selected song
        } else if (!isMediaPlayerDefined || !isSameSongPlaying) {
            playSong(selectedSong);
            // If mediaPlayer is paused, resume
        } else {
            mediaPlayer.play();
        }
    }

    private void playSong(Song song) {
        //Setting previousSong for previous song button
        if (currentSong != -1) {
            previousSong = currentSong;
        }
        if (mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            stop();
        }

        Media media = new Media(Paths.get(song.getFilePath()).toUri().toString());
        mediaPlayer = new MediaPlayer(media);

        // Set the volume to the current value of the volumeSlider
        double sliderValue = volumeSlider.getValue();
        double volume = (Math.log10(sliderValue) - 2) / -2; // using the same scale as our slider
        mediaPlayer.setVolume(volume);

        mediaPlayer.play();

        // Update currentSong so the Next Button works
        currentSong = currentTableView.getItems().indexOf(song);

        mediaPlayer.setOnEndOfMedia(() -> {
            // Get the index of the current song
            int currentSong = currentTableView.getItems().indexOf(song);
            // Check if there is a next song
            if (currentSong + 1 < currentTableView.getItems().size()) {
                // Get the next song
                Song nextSong = currentTableView.getItems().get(currentSong + 1);
                playSong(nextSong);
            } else {
                // If there is no next song, play the first song in the table
                if (!currentTableView.getItems().isEmpty()) {
                    Song firstSong = currentTableView.getItems().get(0);
                    playSong(firstSong);
                }
            }
        });
    }


    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void next(ActionEvent actionEvent) {
        // Check if currentSong + 1 is a valid index
        if (currentSong + 1 < currentTableView.getItems().size()) {
            Song nextSong = currentTableView.getItems().get(currentSong + 1);
            playSong(nextSong);
        } else {
            // If there is no next song, play the first song in the table
            if (!currentTableView.getItems().isEmpty()) {
                Song firstSong = currentTableView.getItems().get(0);
                playSong(firstSong);
            }
        }
    }

    public void prev(ActionEvent actionEvent) {
        if (previousSong != -1) {
            Song prevSong = currentTableView.getItems().get(previousSong);
            playSong(prevSong);
        }
    }


    public void setVolume() {
    }

    public void setVolumeSlider() {  //observable (the property that was changed[not used but needed for .addListener]), oldValue (the previous value of the property), and newValue (the new value of the property).
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
        if (selectedSong != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/DeleteSongs.fxml"));
            Parent root = loader.load();
            DeleteSongsController controller = loader.getController();
            controller.setData(selectedSong.getId(), selectedSong.getName());
            controller.setAppController(this);
            openNewScene(root, "RemoveSong");
        }
    }

    @FXML
    public void openUpdateWindow(ActionEvent actionEvent) throws IOException {
        try {
            Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();
            if (selectedSong != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/UpdateSongs.fxml"));
                int id = selectedSong.getId();
                Parent root = loader.load();
                UpdateSongWindow controller = loader.getController();
                controller.setAppController(this);
                controller.setID(id);
                openNewScene(root, "Update Song");

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

    public void showSongs() throws MyTunesExceptions {
        columnSongsDB.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArtistsDB.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnLengthDB.setCellValueFactory(new PropertyValueFactory<>("length"));
        columnFileTypeDB.setCellValueFactory(new PropertyValueFactory<>("fileType"));

        List<Song> songs = bllManager.getAllSongs(); //Get all songs from the BLL layer through the getAllSongs method
        tableViewDB.getItems().setAll(songs);          //that talks to the DAL layer and returns a list of songs

    }

    public void showPlayLists() throws MyTunesExceptions {
        colPlaylistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSongCount.setCellValueFactory(new PropertyValueFactory<>("songCount"));
        colLength.setCellValueFactory(new PropertyValueFactory<>("length"));

        List<Playlist> playlists = bllPlaylist.getAllPlaylists();
        tablePlaylists.getItems().setAll(playlists);

    }

    public void updateSongInTableView(Song updatedSong) {
        // Find the index of the song in the TableView's items
        int index = tableViewDB.getItems().indexOf(updatedSong);
        // Replace the song in the TableView's items with the updated song
        if (index != -1) {
            tableViewDB.getItems().set(index, updatedSong);
        }
    }


    public void displaySongs(MouseEvent mouseEvent) throws MyTunesExceptions {
        Playlist selected = (Playlist) tablePlaylists.getSelectionModel().getSelectedItem();
        if (selected != null) {
            columnSongs.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnArtists.setCellValueFactory(new PropertyValueFactory<>("artist"));
            columnLength.setCellValueFactory(new PropertyValueFactory<>("length"));
            columnFileType.setCellValueFactory(new PropertyValueFactory<>("fileType"));
            List<PlaylistConnection> connections = bllPlaylist.getPlaylistConnections(selected.getId());
            tableSongsFromPlayList.getItems().setAll(connections);
        }
    }

    public void addSongToPlaylist(ActionEvent actionEvent) throws MyTunesExceptions {
        Playlist selectedPlaylist = (Playlist) tablePlaylists.getSelectionModel().getSelectedItem();
        Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null && selectedSong != null) {
            bllPlaylist.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            displaySongs(null);  // Refresh the song list in the selected playlist
        }
    }


    public void removeSongFromPlaylist(ActionEvent actionEvent) throws MyTunesExceptions {
        Playlist selectedPlaylist = (Playlist) tablePlaylists.getSelectionModel().getSelectedItem();
        PlaylistConnection selectedSong = tableSongsFromPlayList.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null && selectedSong != null) {
            int selectedIndex = tableSongsFromPlayList.getSelectionModel().getSelectedIndex();
            bllPlaylist.removeSongFromPlaylist(selectedSong.getOrderId());
            displaySongs(null);  // Refresh the song list in the selected playlist

            // Stops cursor from jumping to the top
            if (tableSongsFromPlayList.getItems().size() > selectedIndex) {
                tableSongsFromPlayList.getSelectionModel().select(selectedIndex);
            } else if (selectedIndex > 0) {  // If at bottom of list, move up 1
                tableSongsFromPlayList.getSelectionModel().select(selectedIndex - 1);
            }
        }
    }

    public void Search(ActionEvent actionEvent) throws MyTunesExceptions {
        columnSongsDB.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArtistsDB.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnLengthDB.setCellValueFactory(new PropertyValueFactory<>("length"));
        columnFileTypeDB.setCellValueFactory(new PropertyValueFactory<>("fileType"));

        List<Song> songs = bllManager.searchForSong(txtSearch.getText()); //Get all songs from the BLL layer through the getAllSongs method
        tableViewDB.getItems().setAll(songs);          //that talks to the DAL layer and returns a list of songs

    }
}

