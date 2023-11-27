package dk.MyTunes.Exceptions;

public class MyTunesExceptions extends Exception{

    public MyTunesExceptions(String message){
        super();
    }
    public MyTunesExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public MyTunesExceptions(Throwable cause) {
        super(cause);
    }

    protected MyTunesExceptions(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
