package com.mehdi.blankactivity.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mehdi.blankactivity.DATAS.MESSAGE;
import com.mehdi.blankactivity.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.holder> {


    private Context context;
    private ArrayList<MESSAGE> data = null;

    public void swapAdapter(ArrayList<MESSAGE> da){
        if (da == data) return;
        this.data = da;
        if (da != null){
            this.notifyDataSetChanged();
        }
    }

    class holder extends RecyclerView.ViewHolder{

        ImageView com_user_photo;
        TextView com_time;
        TextView com_user_name;
        TextView com;
        holder(@NonNull View itemView) {
            super(itemView);

            com_user_photo = itemView.findViewById(R.id.com_user_tof);
            com_user_name = itemView.findViewById(R.id.com_user_name);
            com = itemView.findViewById(R.id.com);
            com_time = itemView.findViewById(R.id.com_time);

        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int i) {
        MESSAGE da = data.get(i);

        if (da == null) return;


        try {
            Glide.with(context).load(da.getImage()).into(holder.com_user_photo);
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.com_user_name.setText(da.getName());
        holder.com.setText(da.getMessage());
        Date date = new Date(da.getTime());
        if (System.currentTimeMillis() - da.getTime() > 86400000){
            holder.com_time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
        }else {
            holder.com_time.setText(DateFormat.getDateInstance().format(date));
        }
    }




    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }


}