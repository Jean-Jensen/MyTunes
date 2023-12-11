package dk.MyTunes.GUI;

//imports
import dk.MyTunes.BE.Playlist;
import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import dk.MyTunes.BLL.BLLPlaylist;
import dk.MyTunes.Exceptions.MyTunesExceptions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import dk.MyTunes.BE.PlaylistConnection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;


import javax.sound.sampled.*;
import javax.sound.sampled.spi.AudioFileReader;
import java.util.List;
import java.util.Map;

public class AppController {

    //UI Elements
    @FXML
    private Label lblSongName;
    @FXML
    private Label lblArtist;
    @FXML
    private ProgressBar songProgress;
    @FXML
    private Slider songProgressSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private TextField txtSearch;
    private ImageView playView;
    private ImageView pauseView;
//Buttons
    @FXML
    private Button updateButton;
    @FXML
    private Button removeSongButton;
    @FXML
    private Button addSongButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button addSongToPlaylistButton;
    @FXML
    private Button removeSongFromPlaylistButton;
    @FXML
    private ToggleButton togglePlayPause;
//Tables
    @FXML
    private TableView<Playlist> tablePlaylists;
    @FXML
    private TableColumn<Playlist, String> colPlaylistName;
    @FXML
    private TableColumn<Playlist, Integer> colSongCount;
    @FXML
    private TableColumn<Playlist, String> colLength;
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
//Non-UI related
    private TableView<?> currentTableView;
    private BLLManager bllManager;  //The only thing the GUI talks to is the bllManager
    private BLLPlaylist bllPlaylist = new BLLPlaylist();

    private MediaPlayer mediaPlayer;
    private Song selectedSong;
    private int currentSong;
    private int previousSong = -1;
    //filters so users can only select .mp3 or .wav files
    private FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(".mp3 files", "*.mp3");
    private FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter(".wav files", "*.wav");


    public AppController() {
        this.bllManager = new BLLManager();
    }

    public void initialize() throws MyTunesExceptions {
        songSelector(); //This figured out which song in which table you have selected and plays that one
       toolTips(); //Lets you add notes to buttons when you hover them with your mouse
        coloumnSizes(); //This makes it so the header for the table (Columns) readjust to the window size
        showSongs(); //Shows the songs in the Database on the Database Table
        showPlayLists(); //Shows the songs in the Playlist Database on the Playlist Table
        setVolumeSlider(); //Initializes the volume slider
        contextMenu(); //Gives us the ability to right click things
        tableViewDB.setVisible(true);
        tableSongsFromPlayList.setVisible(false);
        updateButtonState(); //This method works with the playToggle below so you cannot toggle if a song isnt selected
        playToggle();  //toggles the play/pause button image
    }


    ///////////////////Quality of Life/////////////////////////
    public void toolTips() {
        Tooltip tooltipAddSong = new Tooltip("Add selected song to the selected playlist");
        Tooltip.install(addSongToPlaylistButton, tooltipAddSong);

        Tooltip tooltipRemoveSong = new Tooltip("Remove selected song from the selected playlist");
        Tooltip.install(removeSongFromPlaylistButton, tooltipRemoveSong);

        Tooltip tooltipSearch = new Tooltip("Search for song that contains what you typed in its name/artist name");
        Tooltip.install(searchButton, tooltipSearch);

        Tooltip tooltipAdd = new Tooltip("Add song to songs list");
        Tooltip.install(addSongButton, tooltipAdd);

        Tooltip tooltipDelete = new Tooltip("Delete currently selected song. Will do nothing if no song selected");
        Tooltip.install(removeSongButton, tooltipDelete);

        Tooltip tooltipUpdate = new Tooltip("Update values of currently selected song. Will do nothing if no song selected");
        Tooltip.install(updateButton, tooltipUpdate);

    }

    public void contextMenu(){
        // Create the context menu for tableSongsFromPlaylist
        ContextMenu contextMenuForPlaylist = new ContextMenu();
        ContextMenu contextMenuForDB = new ContextMenu();

        // Add menuitem for Updatesong that runs openUpdateWindow method
        MenuItem updateSongItem = new MenuItem("Update song");
        updateSongItem.setOnAction(mouseClick -> {
            try {
                openUpdateWindow();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        MenuItem addSong = new MenuItem("Add Song");
        addSong.setOnAction(mouseClick -> {
            try {
                addSong(mouseClick);
            } catch (MyTunesExceptions | IOException | UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            }
        });

        MenuItem deleteSong = new MenuItem("Delete Song");
        deleteSong.setOnAction(mouseClick -> {
            try {
                removeSong(mouseClick);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        contextMenuForPlaylist.getItems().add(removePlaylistSong());
        contextMenuForDB.getItems().add(updateSongItem);
        contextMenuForDB.getItems().add(addSong);
        contextMenuForDB.getItems().add(deleteSong);
        contextMenuForDB.getItems().add(addPlaylistSong());


        // Set the context menu on the tableViewDB
        tableViewDB.setContextMenu(contextMenuForDB);
        tableSongsFromPlayList.setContextMenu(contextMenuForPlaylist);

    }

    private Menu addPlaylistSong() {
        Menu addToPlaylistMenu = new Menu("Add to playlist");
        // Create local list for playlists
        List<Playlist> playlists = null;
        try {
            playlists = bllPlaylist.getAllPlaylists();
        } catch (MyTunesExceptions e) {
            e.printStackTrace();
        }
        //Makes sure there is playlists
        if (playlists != null) {
            for (Playlist playlist : playlists) {
                //Loop to display all playlists
                MenuItem playlistItem = new MenuItem(playlist.getName());
                playlistItem.setOnAction(e -> {
                    try {
                        //to target the playlist you click
                        tablePlaylists.getSelectionModel().select(playlist);
                        Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();
                        if (selectedSong != null) {
                            //uses our addsongtoplaylist method
                            bllPlaylist.addSongToPlaylist(playlist.getId(), selectedSong.getId());
                            //updates lists
                            displaySongs(null);
                            showPlayLists();
                        }
                    } catch (MyTunesExceptions ex) {
                        ex.printStackTrace();
                    }
                });
                // Add playlistItem to the addToPlaylistMenu menu
                addToPlaylistMenu.getItems().add(playlistItem);
            }
        }
        return addToPlaylistMenu;
    }

    private MenuItem removePlaylistSong() {
        MenuItem removeFromPlaylist = new MenuItem("Remove from playlist");
        removeFromPlaylist.setOnAction(e -> {
            try {
                // Get the selected song
                PlaylistConnection selectedSong = tableSongsFromPlayList.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    // Use the removeSongFromPlaylist method
                    bllPlaylist.removeSongFromPlaylist(selectedSong.getOrderId());
                    // Update lists
                    displaySongs(null);
                    showPlayLists();
                }
            } catch (MyTunesExceptions ex) {
                ex.printStackTrace();
            }
        });
        return removeFromPlaylist;
    }

    /////////////////Media-player Functions//////////////////
    public void songSelector() {
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
                System.out.println(tableViewDB.getSelectionModel().getSelectedItem().getName());
                currentTableView = tableViewDB;
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
            setProgressBar();
        }
    }

    private void playSong(Song song) {
        //Setting previousSong for previous song button
        if (currentSong != -1) {
            previousSong = currentSong;
        }

        //checking if the media player is already playing something, prevents the same song playing twice
        if (mediaPlayer != null && mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            stop();
        }

        Media media = new Media(Paths.get(song.getFilePath()).toUri().toString());
        mediaPlayer = new MediaPlayer(media);
        setProgressBar();
        setSongLabels(song);
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
                Song nextSong = (Song) currentTableView.getItems().get(currentSong + 1);
                playSong(nextSong);
            } else {
                // If there is no next song, play the first song in the table
                if (!currentTableView.getItems().isEmpty()) {
                    Song firstSong = (Song) currentTableView.getItems().get(0);
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
            Song nextSong = (Song) currentTableView.getItems().get(currentSong + 1);
            playSong(nextSong);
        } else {
            // If there is no next song, play the first song in the table
            if (!currentTableView.getItems().isEmpty()) {
                Song firstSong = (Song) currentTableView.getItems().get(0);
                playSong(firstSong);
            }
        }
    }

    public void prev(ActionEvent actionEvent) {
        if (previousSong != -1) {
            Song prevSong = (Song) currentTableView.getItems().get(previousSong);
            playSong(prevSong);
        }
    }

    private void setVolumeSlider() {  //observable (the property that was changed[not used but needed for .addListener]), oldValue (the previous value of the property), and newValue (the new value of the property).
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                double sliderValue = newValue.doubleValue(); //This value is normally between 0 and 100 but next line makes this more precise, so we need a double
                double volume = (Math.log10(sliderValue) - 2) / -2; // Convert slider value to logarithmic scale
                mediaPlayer.setVolume(volume);
            }
        });
    }

    private void setProgressBar(){ //adjusts the progressbar and slider value everytime a new song plays
        if(mediaPlayer != null){
            //add a listener for whenever the current time changes
            //(so we can set the slider to be the same as the current duration)
            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    songProgressSlider.setValue(newValue.toSeconds() / 100);
                    songProgress.progressProperty().set(newValue.toSeconds() / 100);
                }
            });

        }
    }

    //setting it so that any adjustment of the slider will change the current time in the song
    public void adjustSongTime(MouseEvent mouseEvent) {
        if(mediaPlayer != null){
            mediaPlayer.seek(Duration.seconds(songProgressSlider.getValue() * 100));
            songProgress.progressProperty().set(songProgressSlider.getValue());
        }
    }

    ///////////////////////////UI + Buttons//////////////////////////
    public void Search(KeyEvent keyEvent) throws MyTunesExceptions {
        columnSongsDB.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnArtistsDB.setCellValueFactory(new PropertyValueFactory<>("artist"));
        columnLengthDB.setCellValueFactory(new PropertyValueFactory<>("length"));
        columnFileTypeDB.setCellValueFactory(new PropertyValueFactory<>("fileType"));

        List<Song> songs = bllManager.searchForSong(txtSearch.getText()); //Get all songs from the BLL layer through the getAllSongs method
        tableViewDB.getItems().setAll(songs);          //that talks to the DAL layer and returns a list of songs
    }

    private void setSongLabels(Song song){
        lblArtist.setText(song.getArtist());
        lblSongName.setText(song.getName());
    }

    public void renamePlaylist(ActionEvent actionEvent) {
    }

    public void deletePlaylist(ActionEvent actionEvent) {
    }

    public void savePlaylist(ActionEvent actionEvent) {
    }

    public void addSongToPlaylist(ActionEvent actionEvent) throws MyTunesExceptions {
        Playlist selectedPlaylist = tablePlaylists.getSelectionModel().getSelectedItem();
        Song selectedSong = tableViewDB.getSelectionModel().getSelectedItem();

        if (selectedPlaylist != null && selectedSong != null) {
            bllPlaylist.addSongToPlaylist(selectedPlaylist.getId(), selectedSong.getId());
            displaySongs(null);  // Refresh the song list in the selected playlist
            showPlayLists(); //Refresh display everytime new song is added
        }
    }

    public void removeSongFromPlaylist(ActionEvent actionEvent) throws MyTunesExceptions {
        Playlist selectedPlaylist = (Playlist) tablePlaylists.getSelectionModel().getSelectedItem();
        PlaylistConnection selectedSong = tableSongsFromPlayList.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null && selectedSong != null) {
            int selectedIndex = tableSongsFromPlayList.getSelectionModel().getSelectedIndex();
            bllPlaylist.removeSongFromPlaylist(selectedSong.getOrderId());
            displaySongs(null);  // Refresh the song list in the selected playlist
            showPlayLists(); //Refresh display everytime new song is added
            // Stops cursor from jumping to the top
            if (tableSongsFromPlayList.getItems().size() > selectedIndex) {
                tableSongsFromPlayList.getSelectionModel().select(selectedIndex);
            } else if (selectedIndex > 0) {  // If at bottom of list, move up 1
                tableSongsFromPlayList.getSelectionModel().select(selectedIndex - 1);
            }
        }
    }

    private Song getSongFromFile(File file) throws MyTunesExceptions, UnsupportedAudioFileException, IOException { //creates Song with correct values from the file
        String name = file.getName(); //gets filename
        String filepath = file.getPath(); //string value for the filepath (since we'll be reusing it)
        String fileType = filepath.substring(filepath.lastIndexOf('.')); //gets filetype by getting everything after the last instance of "."

        //AudioFileFormat fileFormat = null;
        double duration = 0.0;
        String artist = "";
        if(fileType.equals(".mp3")){
            AudioFileFormat fileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
            //the MP3SPI library allows us to read MP3 info
            Map properties = fileFormat.properties();
            Long durationLong = (Long) properties.get("duration");
            duration = durationLong.doubleValue() / 1000;
            duration = (duration / 1000) ;
            artist = (String) properties.get("artist");
        } else { //assuming it's a .wav file
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(file);
            AudioFormat format = audioInput.getFormat();
            long totalFrames = audioInput.getFrameLength();
            duration = totalFrames / format.getFrameRate(); //gets duration in seconds
            if(format.getProperty("artist") != null){ //it most likely will, I've heard .wav files have poor tagging support
                artist = format.getProperty("artist").toString();
            }
        }

        int hours = (int) (duration / 3600);
        int minutes = (int) ((duration % 3600) / 60);
        int seconds = (int) (duration % 60);
        //using %02d with String.format will ensure that 2 digits always show up, padding with 0s
        String lengthString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        if(artist == null || artist.isEmpty()){ //so that we don't run into any errors
            artist = "Unknown";
        }
        //System.out.println("Song: " + (bllManager.getLastID()+1) + name + " " + artist + " " + lengthString + " " + fileType + " " + filepath);

        return new Song(bllManager.getLastID()+1, name, artist, lengthString, fileType, filepath);
    }

    public void showSongTable(ActionEvent event) {
        tableViewDB.setVisible(true);
        tableSongsFromPlayList.setVisible(false);
    }

    private void updateButtonState() {
        if (selectedSong != null && togglePlayPause.isSelected()) {
            togglePlayPause.setGraphic(pauseView);
        } else {
            togglePlayPause.setGraphic(playView);
        }
    }
    private void playToggle(){
        Image playImage = new Image("dk/MyTunes/GUI/FXML/Icons/cyan-play-icon.png");
        Image pauseImage = new Image("dk/MyTunes/GUI/FXML/Icons/cyan-pause-icon2.png");

        playView = new ImageView(playImage);
        playView.setFitWidth(28);
        playView.setFitHeight(28);

        pauseView = new ImageView(pauseImage);
        pauseView.setFitWidth(28);
        pauseView.setFitHeight(28);

    togglePlayPause.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
        if(selectedSong != null) {
            if (isNowSelected) {
                togglePlayPause.setGraphic(pauseView);
            } else {
                togglePlayPause.setGraphic(playView);
            }
        }
    });

    // Set initial graphic
    togglePlayPause.setGraphic(playView);
    //use css after this
    togglePlayPause.getStyleClass().add("playPauseButton");

    }

    ///////////////////////New Window Buttons////////////////////////
    @FXML
    public void openUpdateWindow() throws IOException {
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

    public void addSong(ActionEvent actionEvent) throws MyTunesExceptions, IOException, UnsupportedAudioFileException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Song");
        chooser.getExtensionFilters().addAll(filter1, filter2);
        File selected = chooser.showOpenDialog(lblSongName.getScene().getWindow());

        if(selected != null){
            bllManager.createSong(getSongFromFile(selected));
            showSongs(); //refreshing table after adding song
        }
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

    //in order to avoid repeating the same lines of code over and over
    private void openNewScene(Parent root, String title) throws IOException {
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.show();
    }

    ///////////////////////Table Displays/////////////////////////
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

    public void displaySongs(MouseEvent mouseEvent) throws MyTunesExceptions {
        Playlist selected = (Playlist) tablePlaylists.getSelectionModel().getSelectedItem();
        if (selected != null) {
            columnSongs.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnArtists.setCellValueFactory(new PropertyValueFactory<>("artist"));
            columnLength.setCellValueFactory(new PropertyValueFactory<>("length"));
            columnFileType.setCellValueFactory(new PropertyValueFactory<>("fileType"));
            List<PlaylistConnection> connections = bllPlaylist.getPlaylistConnections(selected.getId());
            tableSongsFromPlayList.getItems().setAll(connections);
            tableViewDB.setVisible(false);
            tableSongsFromPlayList.setVisible(true);
        }
    }

    public void updateSongInTableView(Song updatedSong) {
        // Find the index of the song in the TableView's items
        int index = tableViewDB.getItems().indexOf(updatedSong);
        // Replace the song in the TableView's items with the updated song
        if (index != -1) {
            tableViewDB.getItems().set(index, updatedSong);
        }
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
}

