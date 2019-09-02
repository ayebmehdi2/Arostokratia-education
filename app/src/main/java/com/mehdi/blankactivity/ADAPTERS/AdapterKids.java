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
import com.mehdi.blankactivity.DATAS.CHILD;
import com.mehdi.blankactivity.R;

import java.util.ArrayList;

public class AdapterKids extends RecyclerView.Adapter<AdapterKids.holder> {


    private Context context;

    private ArrayList<CHILD> dataPerson = null;

    public interface clickPrson{
        void select(String uid);
    }

    private final clickPrson clickPrson;

    public AdapterKids(clickPrson clickPrson){
        this.clickPrson = clickPrson;
    }

    public void swapAdapter(ArrayList<CHILD> data){
        if (dataPerson == data) return;
        this.dataPerson = data;
        if (data != null){
            this.notifyDataSetChanged();
        }
    }

    class holder extends RecyclerView.ViewHolder{

        ImageView userImage;
        TextView userName;
        ImageView qrCode;
        public holder(@NonNull View itemView) {
            super(itemView);


            userImage = itemView.findViewById(R.id.kid_photo);
            userName = itemView.findViewById(R.id.kid_name);
            qrCode = itemView.findViewById(R.id.kid_qr_code);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPrson.select(dataPerson.get(getAdapterPosition()).getName());
                }
            });




        }
    }

    @NonNull
    @Override
    public  holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new  holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kid, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull  holder holder, int i) {
        CHILD data = dataPerson.get(i);

        if (data == null) return;

        holder.userName.setText(data.getName());
        try {
            Glide.with(context).load(data.getPhoto()).into(holder.userImage);
            Glide.with(context).load(data.getQRcode()).into(holder.qrCode);
        }catch (Exception e){ }

    }




    @Override
    public int getItemCount() {
        if (dataPerson == null){
            return 0;
        }
        return dataPerson.size();
    }


}