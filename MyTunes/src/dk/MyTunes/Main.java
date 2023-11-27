package dk.MyTunes;

import dk.MyTunes.DAL.SongsDAO;
import dk.MyTunes.GUI.FXML.GraphicalGUI;


public class Main {
    public static void main(String[] args) {
        SongsDAO dao = new SongsDAO();
        dao.test();
        GraphicalGUI.run();
    }

}