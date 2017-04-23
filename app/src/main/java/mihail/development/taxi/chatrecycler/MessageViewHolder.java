package mihail.development.taxi.chatrecycler;

import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import mihail.development.taxi.R;

/**
 * Created by mihail on 23.04.2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    final TextView message;
    final ImageView avatar;
    final TextView pesonName;

    public MessageViewHolder(View itemView) {
        super(itemView);
        message =  (TextView) itemView.findViewById(R.id.tvMessage);
        avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
        pesonName = (TextView) itemView.findViewById(R.id.tvSenderName);
    }
}
