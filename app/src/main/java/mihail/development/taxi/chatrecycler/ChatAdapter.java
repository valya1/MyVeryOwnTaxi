package mihail.development.taxi.chatrecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import mihail.development.taxi.R;
import mihail.development.taxi.data.ChatItem;

/**
 * Created by mihail on 23.04.2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<MessageViewHolder> {


    private ArrayList<ChatItem> chatItems;

    public ChatAdapter() {
        super();
        chatItems = new ArrayList<>();
    }

    public void update(ChatItem item)
    {
        chatItems.add(item);
        notifyItemInserted(chatItems.size());
    }

    public void update(ArrayList<ChatItem> items)
    {
        chatItems.clear();
        chatItems.addAll(items);
        notifyDataSetChanged();
    }


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        ChatItem chatItem = chatItems.get(position);
        if(chatItem.isFrom_driver())
            holder.pesonName.setText(chatItem.getLogin_driver());
        else
            holder.pesonName.setText(chatItem.getLogin_user());
        holder.message.setText(chatItem.getMessage());
        holder.avatar.setImageResource(R.drawable.no_avatar);
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }
}
