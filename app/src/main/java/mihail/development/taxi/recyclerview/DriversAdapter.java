package mihail.development.taxi.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mihail.development.taxi.data.Driver;


public class DriversAdapter extends RecyclerView.Adapter<DriversViewHolder> {

    private ArrayList<Driver> drivers;

    public DriversAdapter() {
        super();
        drivers = new ArrayList<>();
    }

    public void addData(ArrayList<Driver> newlist)
    {
        drivers.clear();
        drivers.addAll(newlist);
        this.notifyDataSetChanged();
    }

    public void clear()
    {
        drivers.clear();
        notifyDataSetChanged();
    }

    @Override
    public DriversViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(mihail.development.taxi.R.layout.item_driver, parent, false);
        DriversViewHolder driversViewHolder = new DriversViewHolder(view);
        return driversViewHolder;
    }

    @Override
    public void onBindViewHolder(DriversViewHolder holder, int position) {
        Driver driver = drivers.get(position);
        holder.tvDriverFirstName.setText(driver.getF_name());
        holder.tvDriverLastName.setText(driver.getL_name());
        holder.tvDriverCar.setText(driver.getCar());
        holder.tvDriverRatingValue.setText(String.valueOf(driver.getRating()));
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }
}
