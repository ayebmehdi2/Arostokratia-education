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
import com.mehdi.blankactivity.DATAS.NOTE_FOR_PARENT;
import com.mehdi.blankactivity.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterNote   extends RecyclerView.Adapter<AdapterNote.holder> {


    private Context context;
    private ArrayList<NOTE_FOR_PARENT> data = null;

    public void swapAdapter(ArrayList<NOTE_FOR_PARENT> da){
        if (da == data) return;
        this.data = da;
        if (da != null){
            this.notifyDataSetChanged();
        }
    }

    class holder extends RecyclerView.ViewHolder{

        ImageView ima;
        TextView nameAndDetail;
        TextView dateAndDesk;
        ImageView deskImage;
        holder(@NonNull View itemView) {
            super(itemView);

            ima = itemView.findViewById(R.id.ima);
            nameAndDetail = itemView.findViewById(R.id.name_and_detail);
            dateAndDesk = itemView.findViewById(R.id.date_and_desk);
            deskImage = itemView.findViewById(R.id.note_desk_image);

        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_note_for_parent, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int i) {
        NOTE_FOR_PARENT da = data.get(i);

        if (da == null) return;


        try {
            if (da.getNoteDeskImage() == 1){
                holder.deskImage.setImageDrawable(context.getDrawable(R.drawable.ic_video_lesson));
            }else if (da.getNoteDeskImage() == 2){
                holder.deskImage.setImageDrawable(context.getDrawable(R.drawable.ic_dm_manager));
            }else if (da.getNoteDeskImage() == 3){
                holder.deskImage.setImageDrawable(context.getDrawable(R.drawable.ic_techaerabsent));
            }

            Glide.with(context).load(da.getKidImage()).into(holder.ima);
        }catch (Exception e) {
            e.printStackTrace();
        }


        String datt;
        Date date = new Date(da.getTime());
        if (System.currentTimeMillis() - da.getTime() > 86400000){
            datt = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
        }else {
            datt = DateFormat.getDateInstance().format(date);
        }

        String s1 = da.getKidName() + " - " + da.getNoteDetail();
        holder.nameAndDetail.setText(s1);

        String s2 = datt + " - " + da.getNoteDesk();
        holder.dateAndDesk.setText(s2);

    }




    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
    }

}
