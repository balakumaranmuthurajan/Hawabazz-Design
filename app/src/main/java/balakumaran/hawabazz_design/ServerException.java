package balakumaran.hawabazz_design;

/**
 * Created by Praveen kumar on 13/10/2015.
 */
public class ServerException extends Throwable {

    private int errorCode=0;
    public ServerException(int code){
        errorCode=code;
    }

    public int getErrorCode(){
        return errorCode;
    }

    public String toString(){
        return "Server Error Code: "+errorCode;
    }

}
