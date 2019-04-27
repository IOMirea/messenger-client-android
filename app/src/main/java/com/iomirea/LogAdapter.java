package com.iomirea;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


//временный адптер, при окончательных контейнерах классов надо менять
public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ItemHolder>{
    private Context context;
    List<String> logs = new ArrayList<String>();

    public LogAdapter(Context context) {

        this.context=context;
    }

    public void addEntity(String entity){
        logs.add(entity);
        notifyItemInserted(logs.size()-1);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.log_item, parent, false);

        return new ItemHolder(view);
    }
    //создает только один элемент в указанном количестве
    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        holder.text.setText(logs.get(position));
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        private final TextView text;

        ItemHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.log_text);
        }

        public void setText(String log){
            text.setText(log);
        }

    }
}