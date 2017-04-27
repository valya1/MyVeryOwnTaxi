package mihail.development.taxi.driversrecycler;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mihail.development.taxi.activities.ConfirmActivity;
import mihail.development.taxi.R;
import mihail.development.taxi.activities.ChatActivity;
import mihail.development.taxi.data.Driver;


public class DriversAdapter extends RecyclerView.Adapter<DriversViewHolder> {

    private ArrayList<Driver> drivers;
    private Context context;
    private FragmentManager fragmentManager;

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
    public void onBindViewHolder(final DriversViewHolder holder, int position) {
        final Driver driver = drivers.get(position);
        holder.tvDriverFirstName.setText(driver.getF_name());
        holder.tvDriverLastName.setText(driver.getL_name());
        holder.tvDriverCar.setText(driver.getCar());
        holder.tvDriverRatingValue.setText(String.valueOf(driver.getRating()));
        holder.ivDriverAvatar.setImageResource(R.drawable.no_avatar);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                context.startActivity(new Intent(context, ConfirmActivity.class)
                        .putExtra("avatar", holder.ivDriverAvatar.getResources().getIdentifier("no_avatar", "drawable", context.getPackageName()))
                        .putExtra("driver_first_name", driver.getF_name())
                        .putExtra("driver_last_name", driver.getL_name()));
            }
        });

        holder.btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context = v.getContext();
                context.startActivity(new Intent(context, ChatActivity.class).putExtra("driver_login", driver.getLogin()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }
}
