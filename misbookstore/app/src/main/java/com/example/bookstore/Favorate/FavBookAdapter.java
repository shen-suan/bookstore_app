package com.example.bookstore.Favorate;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FavBookAdapter extends RecyclerView.Adapter<FavBookAdapter.FavLinearViewHolder>{

    //private ListData[] listData;
    private ArrayList listData;
    private ListData data;
    private Context mContext;
    private OnItemClickListener mlistener;
    private FirebaseUser user;
    private String uid;

    public FavBookAdapter(Context context, ArrayList list, OnItemClickListener listener){
        this.mContext = context;
        this.listData = list;
        this.mlistener = listener;
    }

    public FavBookAdapter.FavLinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        return new FavLinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fav_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder( FavBookAdapter.FavLinearViewHolder viewHolder,final int position) {
        data = (ListData) listData.get(position);
        Glide.with(mContext)
                .load(data.getUrl())
                .into(viewHolder.photo);
        viewHolder.title.setText(data.getTitle());
        viewHolder.price.setText("$ "+ data.getPrice());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onClick(position);
            }
        });
        viewHolder.unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext,"刪除" + ((ListData) listData.get(position)).getTitle(),Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("確定要從我的收藏中移除")
                        .setIcon(R.drawable.alert)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String isbn = ((ListData) listData.get(position)).getIsbn();
                                System.out.println("選擇: " + isbn);
                                DatabaseReference delete = FirebaseDatabase.getInstance().getReference("favorite_book").child(uid).child(isbn);
                                delete.removeValue();
                                Toast.makeText(mContext,"已移除",Toast.LENGTH_SHORT).show();
                                removeData(position);
                                listData.clear();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        }).show();
            }
        });
    }
    //  删除数据
    public void removeData(int position) {
        listData.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
        //notifyItemRemoved(position);
        //notifyItemRangeChanged(position, getItemCount());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    class FavLinearViewHolder extends RecyclerView.ViewHolder{

        private TextView title,price;
        private CheckBox unlike;
        private ImageView photo;

        public FavLinearViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.fli_title);
            price = itemView.findViewById(R.id.fli_price);
            unlike = itemView.findViewById(R.id.fl_like_btn);
            photo = itemView.findViewById(R.id.fli_img);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
