package com.mehdi.blankactivity.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mehdi.blankactivity.DATAS.POST;
import com.mehdi.blankactivity.R;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterCommunity extends RecyclerView.Adapter<AdapterCommunity.holder> {



    private ArrayList<POST> data = null;

    public AdapterCommunity(CLICK click) {
        this.click = click;
    }

    public interface CLICK{
        void select(String uid, String sub);
    }

    private final CLICK click;



    public void swapAdapter(ArrayList<POST> da){
        if (da == data) return;
        this.data = da;
        if (da != null){
            this.notifyDataSetChanged();
        }
    }

    class holder extends RecyclerView.ViewHolder{

        View color;
        TextView subject;
        TextView desc;
        TextView time;
        TextView numComment;
        holder(@NonNull View itemView) {
            super(itemView);

            color = itemView.findViewById(R.id.color);
            subject = itemView.findViewById(R.id.subject);
            desc = itemView.findViewById(R.id.desc_community);
            time = itemView.findViewById(R.id.time);
            numComment = itemView.findViewById(R.id.num_comment);


            itemView.setOnClickListener(v -> click.select(data.get(getAdapterPosition()).getUid(), data.get(getAdapterPosition()).getSubject()));




        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.community_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int i) {
        POST da = data.get(i);

        if (da == null) return;


        try {
            holder.color.setBackgroundColor(da.getColor());

            holder.subject.setText(da.getSubject());
            holder.desc.setText(da.getDesc());

            Date date = new Date(da.getTime());
            if (System.currentTimeMillis() - da.getTime() > 86400000){
                holder.time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
            }else {
                holder.time.setText(DateFormat.getDateInstance().format(date));
            }
            holder.numComment.setText(String.valueOf(da.getNumComment()));
        }catch (Exception e){
            e.printStackTrace();
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