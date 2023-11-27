package dk.MyTunes;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyTunes.BE.Song;
import dk.MyTunes.DAL.ConnectionManager;
import dk.MyTunes.DAL.SongsDAO;
import dk.MyTunes.GUI.FXML.GraphicalGUI;


public class Main {
    public static void main(String[] args) throws SQLServerException {
       GraphicalGUI.run();
    }

}