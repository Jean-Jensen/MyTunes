package dk.MyTunes.BE;

import java.util.Objects;

public class Song {
    private int id;
    private String name;
    private String artist;
    private String length;
    private String album;
    private String comment;
    private int year;
    private String genre;
    private String fileType;
    private String filePath;

    public Song(int id, String name, String artist, String length, String album, String comment, int year, String genre, String fileType, String filePath) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.length = length;
        this.album = album;
        this.comment = comment;
        this.year = year;
        this.genre = genre;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    public Song(int id, String name, String artist) {
        this.id = id;
        this.name = name;
        this.artist = artist;
    }


    public Song(int id, String name, String artist, String album, String length, String fileType) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.length = length;
        this.album = album;
        this.fileType = fileType;
    }

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

    @Override
    public String toString() { //Java automatically calls toString in System.out.println so this overrides that and
        return "Song{" +       //prints it out in something readable.
                "id: " + id +
                ", name: '" + name + '\'' +
                ", artist: '" + artist + '\'' +
                ", length: '" + length + '\'' +
                ", album: '" + album + '\'' +
                ", comment: '" + comment + '\'' +
                ", year: " + year +
                ", genre: '" + genre + '\'' +
                ", fileType: '" + fileType + '\'' +
                ", filePath: '" + filePath + '\'' +
                '}';
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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
