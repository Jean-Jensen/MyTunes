package dk.MyTunes.Exceptions;

import java.sql.SQLException;

public class MyTunesExceptions extends SQLException {

    public MyTunesExceptions(String message){
        super();
    }
    public MyTunesExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public MyTunesExceptions(Throwable cause) {
        super(cause);
    }

}
