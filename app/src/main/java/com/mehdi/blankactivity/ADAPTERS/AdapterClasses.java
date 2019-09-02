package com.mehdi.blankactivity.ADAPTERS;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mehdi.blankactivity.DATAS.CLASS;
import com.mehdi.blankactivity.DATAS.MESSAGE;
import com.mehdi.blankactivity.R;

import java.util.ArrayList;

public class AdapterClasses extends RecyclerView.Adapter<AdapterClasses.holder> {


    ArrayList<CLASS> data = new ArrayList<>();

    public void swapAdapter(ArrayList<CLASS> da){
        if (da == data) return;
        this.data = da;
        if (da != null){
            this.notifyDataSetChanged();
        }
    }

    class holder extends RecyclerView.ViewHolder{

        TextView view;
        public holder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.c_n);
        }
    }

    @NonNull
    @Override
    public AdapterClasses.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClasses.holder holder, int position) {
        CLASS dd = data.get(position);
        if (dd == null) return;
        holder.view.setText(dd.getName());
    }


    @Override
    public int getItemCount() {
            if (data == null){
                return 0;
            }
            return data.size();
    }
}
