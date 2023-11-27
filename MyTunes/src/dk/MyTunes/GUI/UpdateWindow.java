package dk.MyTunes.GUI;

import dk.MyTunes.BE.Song;
import dk.MyTunes.BLL.BLLManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class UpdateWindow {

    @FXML
    private TextField nameField;
    @FXML
    private TextField artistField;
    @FXML
    private TextField albumField;
    @FXML
    private TextField lengthField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField fileTypeField;
    @FXML
    private TextField filePathField;

    private BLLManager bllManager;
    private int id;
    private AppController appController;

    public UpdateWindow(int songId) {
        this.bllManager = new BLLManager();
        this.id = songId;
    }
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void updateSong(ActionEvent actionEvent) {
        Song s = bllManager.getSongById(id);

        if (!nameField.getText().isEmpty()) {
            s.setName(nameField.getText());
        }
        if (!artistField.getText().isEmpty()) {
            s.setArtist(artistField.getText());
        }
        if (!albumField.getText().isEmpty()) {
            s.setAlbum(albumField.getText());
        }
        if (!lengthField.getText().isEmpty()) {
            s.setLength(lengthField.getText());
        }
        if (!yearField.getText().isEmpty()) {
            s.setYear(Integer.parseInt(yearField.getText()));
        }
        if (!genreField.getText().isEmpty()) {
            s.setGenre(genreField.getText());
        }
        if (!fileTypeField.getText().isEmpty()) {
            s.setFileType(fileTypeField.getText());
        }
        if (!filePathField.getText().isEmpty()) {
            s.setFilePath(filePathField.getText());
        } else {
            s.setFilePath("NULL");
        }
        bllManager.updateSong(s);
        appController.updateSongInTableView(s);
    }

}
