package com.iomirea;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemHolder>{
    private final int itemNumber;

    public ChatAdapter(int counter) {
        this.itemNumber=counter;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.chats_list, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        holder.username.setText(R.string.username);
        holder.time.setText(R.string.time);
        holder.last_message.setText(R.string.last_message);
        holder.avatar.setImageResource(R.drawable.logo);

    }

    @Override
    public int getItemCount() {
        return itemNumber;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private final TextView username;
        private final TextView time;
        private final TextView last_message;
        private final CircleImageView avatar;



        ItemHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.time);
            last_message = itemView.findViewById((R.id.last_message));
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
