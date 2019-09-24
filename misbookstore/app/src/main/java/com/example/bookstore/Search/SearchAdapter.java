package com.example.bookstore.Search;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchLinearViewHolder>{

    //private ListData[] listData;
    private ArrayList listData;
    private ListData data;
    private Context mContext;
    private OnItemClickListener mlistener;
    private FirebaseUser user;
    private String uid;

    public SearchAdapter(Context context, ArrayList list, OnItemClickListener listener){
        this.mContext = context;
        this.listData = list;
        this.mlistener = listener;
    }

    public SearchAdapter.SearchLinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        return new SearchLinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.search_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder( SearchAdapter.SearchLinearViewHolder viewHolder,final int position) {
        data = (ListData) listData.get(position);
        Glide.with(mContext)
                .load(data.getUrl())
                .into(viewHolder.photo);
        viewHolder.title.setText(data.getTitle());
        viewHolder.price.setText("$ "+ data.getPrice());
        DatabaseReference favorite_book = FirebaseDatabase.getInstance().getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/favorite_book/" + uid);
        favorite_book.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.unlike.setChecked(false);
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String isbn = ((ListData) listData.get(position)).getIsbn();
                    if(isbn.equals(ds.getValue().toString())){
                        viewHolder.unlike.setChecked(true);
                        viewHolder.unlike.setEnabled(false);
                    }
                    //System.out.println("我的最愛: " + ds.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        viewHolder.unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn = ((ListData) listData.get(position)).getIsbn();
                DatabaseReference add = FirebaseDatabase.getInstance().getReference("favorite_book").child(uid);
                add.child(isbn).setValue(isbn);
                Toast.makeText(mContext,"已加入我的最愛",Toast.LENGTH_LONG).show();
                //System.out.println( bookInfo.getTitle() + "加入我的最愛");
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onClick(position);
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
    class SearchLinearViewHolder extends RecyclerView.ViewHolder{

        private TextView title,price;
        private CheckBox unlike;
        private ImageView photo;

        public SearchLinearViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.sli_title);
            price = itemView.findViewById(R.id.sli_price);
            unlike = itemView.findViewById(R.id.sl_like_btn);
            photo = itemView.findViewById(R.id.sli_img);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
