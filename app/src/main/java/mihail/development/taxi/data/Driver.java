package mihail.development.taxi.data;

/**
 * Created by mihail on 18.04.2017.
 */

public class Driver {
    public Driver(String f_name, String l_name, String car, String login, double rating) {
        this.f_name = f_name;
        this.l_name = l_name;
        this.car = car;
        this.login = login;
        this.rating = rating;
    }

    private String f_name;

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    private String l_name;
    private String car;
    private String login;
    private double rating;
}
