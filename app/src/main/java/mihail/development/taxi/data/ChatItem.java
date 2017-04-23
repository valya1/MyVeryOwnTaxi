package mihail.development.taxi.data;

/**
 * Created by mihail on 23.04.2017.
 */

public class ChatItem {

    private String login_user;
    private String login_driver;
    private String message;
    private boolean from_driver;

    public String getLogin_user() {
        return login_user;
    }

    public void setLogin_user(String login_user) {
        this.login_user = login_user;
    }

    public String getLogin_driver() {
        return login_driver;
    }

    public void setLogin_driver(String login_driver) {
        this.login_driver = login_driver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFrom_driver() {
        return from_driver;
    }

    public void setFrom_driver(boolean from_driver) {
        this.from_driver = from_driver;
    }

    public ChatItem(String login_user, String login_driver, String message, boolean from_driver) {
        this.login_user = login_user;
        this.login_driver = login_driver;
        this.message = message;

        this.from_driver = from_driver;
    }
}
