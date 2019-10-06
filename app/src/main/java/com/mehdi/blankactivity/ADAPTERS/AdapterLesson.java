package com.mehdi.blankactivity.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehdi.blankactivity.DATAS.NOTE;
import com.mehdi.blankactivity.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterLesson extends RecyclerView.Adapter<AdapterLesson.holder> {


    private Context context;
    private ArrayList<NOTE> data = null;

    public void swapAdapter(ArrayList<NOTE> da){
        if (da == data) return;
        this.data = da;
        if (da != null){
            this.notifyDataSetChanged();
        }
    }

    class holder extends RecyclerView.ViewHolder{

        ImageView lesson_image;
        TextView lessonDetail;
        TextView lessonDate;
        TextView lessonDesk;
        holder(@NonNull View itemView) {
            super(itemView);

            lesson_image = itemView.findViewById(R.id.lesson_img);
            lessonDetail = itemView.findViewById(R.id.lesson_detail);
            lessonDesk = itemView.findViewById(R.id.lesson_desk);
            lessonDate = itemView.findViewById(R.id.lesson_date);

        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lesson, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int i) {
        NOTE da = data.get(i);

        if (da == null) return;


        try {
            if (da.getImageDesk() == 1){
                holder.lesson_image.setImageDrawable(context.getDrawable(R.drawable.ic_video_lesson));
            }else if (da.getImageDesk() == 2){
                holder.lesson_image.setImageDrawable(context.getDrawable(R.drawable.ic_dm_manager));
            }else if (da.getImageDesk() == 3){
                holder.lesson_image.setImageDrawable(context.getDrawable(R.drawable.ic_techaerabsent));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.lessonDetail.setText(da.getDetail());
        holder.lessonDesk.setText(da.getDesk());
        Date date = new Date(da.getTime());
        if (System.currentTimeMillis() - da.getTime() > 86400000){
            holder.lessonDate.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
        }else {
            holder.lessonDate.setText(DateFormat.getDateInstance().format(date));
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
