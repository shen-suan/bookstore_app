package com.example.bookstore.BookInformation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookstore.R;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder>{

    private ListData[] listData;
    private Context mContext;
    private OnItemClickListener mlistener;

    public LinearAdapter(Context context, ListData[] list, OnItemClickListener listener){
        this.mContext = context;
        this.listData = list;
        this.mlistener = listener;
    }

    public LinearAdapter.LinearViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.book_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder( LinearAdapter.LinearViewHolder viewHolder,final int position) {
        viewHolder.title.setText(listData[position].getTitle());
        viewHolder.price.setText("$ "+listData[position].getPrice());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mlistener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }
    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView title,price;

        public LinearViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.bli_title);
            price = itemView.findViewById(R.id.bli_price);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
