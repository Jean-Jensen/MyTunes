package dk.MyTunes.BE;

import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private String length;
    private int songCount;
    private List<Song> songs;

    public Playlist(int id, String name, String length, int songCount) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.songCount = songCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }

}