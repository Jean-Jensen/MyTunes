package dk.MyTunes.BE;

import java.util.Objects;

public class Song {
    private int id;
    private String name;
    private String artist;
    private String length;
    private String fileType;
    private String filePath;
    

    public Song(int id, String name, String artist, String length, String fileType, String filePath) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.length = length;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public Song(int id, String name, String artist, String length, String fileType) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.length = length;
        this.fileType = fileType;
    }

// The equals and hashcode needed to be set up this way to auto-update our tables in real time when using the update button/method
//Jeppe if you read this one and you know why I would love a proper explanation because I read
//alot about it but was still confused outside of "It just works"
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id == song.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
