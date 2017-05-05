package mihail.development.taxi.driversrecycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import mihail.development.taxi.R;
import mihail.development.taxi.activities.ChatActivity;
import mihail.development.taxi.activities.MapActivity;

class DriversViewHolder extends RecyclerView.ViewHolder {


    final TextView tvDriverFirstName;
    final TextView tvDriverLastName;
    final TextView tvDriverCar;
    final TextView tvDriverRatingValue;
    final ImageView ivDriverAvatar;
    final AppCompatButton btnChat;
    final TextView tvRating;
    final CardView cardView;


    public DriversViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cv);
        tvDriverFirstName = (TextView) itemView.findViewById(R.id.tvDriverFirstName);
        tvDriverLastName = (TextView) itemView.findViewById(R.id.tvDriverLastName);
        tvDriverCar = (TextView) itemView.findViewById(R.id.tvDriverCar);
        tvRating = (TextView) itemView.findViewById(R.id.tvRating);
        tvDriverRatingValue = (TextView) itemView.findViewById(R.id.tvDriverRatingValue);
        ivDriverAvatar = (ImageView) itemView.findViewById(R.id.person_photo);
        btnChat = (AppCompatButton) itemView.findViewById(R.id.btnChat);

    }
}

