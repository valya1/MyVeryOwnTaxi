package mihail.development.taxi.data;

import com.google.gson.annotations.Expose;

/**
 * Created by mihail on 26.04.2017.
 */

public class Tariff {
    @Expose
    private double price_minute;
    @Expose
    private double price_km;

    public Tariff(double price_minute, double price_km) {
        this.price_minute = price_minute;
        this.price_km = price_km;
    }

    public double getPrice_minute() {
        return price_minute;
    }

    public void setPrice_minute(double price_minute) {
        this.price_minute = price_minute;
    }

    public double getPrice_km() {
        return price_km;
    }

    public void setPrice_km(double price_km) {
        this.price_km = price_km;
    }
}
