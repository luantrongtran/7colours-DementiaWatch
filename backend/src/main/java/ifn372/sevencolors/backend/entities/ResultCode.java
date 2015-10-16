package ifn372.sevencolors.backend.entities;

/**
 * Created by lua on 26/09/2015.
 */
public class ResultCode {
    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    boolean result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
}
