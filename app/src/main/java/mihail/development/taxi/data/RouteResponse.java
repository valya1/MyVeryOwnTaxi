package mihail.development.taxi.data;

import java.util.List;

/**
 * Created by mihail on 22.04.2017.
 */

public class RouteResponse {
    private List<Route> routes;

    public String getPoints() {
        return routes.get(0).overview_polyline.points;
    }

    class Route {
        OverviewPolyline overview_polyline;
    }

    class OverviewPolyline {
        String points;
    }
}
