package com.iomirea;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;


//временный адптер, при окончательных контейнерах классов надо менять
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ItemHolder>{
    private final int itemNumber;
    private Context context;

    public ChatAdapter(int counter, Context context) {
        this.itemNumber=counter;
        this.context=context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.chats_list, parent, false);

        return new ItemHolder(view);
    }
    //создает только один элемент в указанном количестве
    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        holder.username.setText(R.string.username);
        holder.time.setText(R.string.time);
        holder.last_message.setText(R.string.last_message);
        holder.avatar.setImageResource(R.drawable.logo);
        holder.setItemCLickListener(new ItemCLickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemNumber;
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView username;
        private final TextView time;
        private final TextView last_message;
        private final CircleImageView avatar;
        private ItemCLickListener itemCLickListener;


        ItemHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            time = itemView.findViewById(R.id.time);
            last_message = itemView.findViewById((R.id.last_message));
            avatar = itemView.findViewById(R.id.avatar);
            itemView.setOnClickListener(this);
        }

        public void setItemCLickListener(ItemCLickListener itemCLickListener) {
            this.itemCLickListener = itemCLickListener;
        }

        @Override
        public void onClick(View v) {
            itemCLickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
