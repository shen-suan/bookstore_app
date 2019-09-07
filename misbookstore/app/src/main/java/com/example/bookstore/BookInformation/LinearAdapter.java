package com.example.bookstore.BookInformation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstore.R;
import com.example.bookstore.fragments.BookFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder>{

    private ArrayList listData;
    private ListData data;
    private Context mContext;
    private OnItemClickListener mlistener;
    private FirebaseUser user;
    private String uid;
    public String[] fav_list ;
    public boolean a = false;


    public LinearAdapter(Context context, ArrayList list, OnItemClickListener listener){
        this.mContext = context;
        this.listData = list;
        this.mlistener = listener;
    }

    public LinearAdapter.LinearViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.book_list_item,parent,false));
    }


    @Override
    public void onBindViewHolder( LinearAdapter.LinearViewHolder viewHolder,final int position) {
        data = (ListData) listData.get(position);
        Glide.with(mContext)
                .load(data.getUrl())
                .into(viewHolder.photoUrl);
        viewHolder.title.setText(data.getTitle());
        viewHolder.price.setText("$ "+ data.getPrice());
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        DatabaseReference favorite_book = FirebaseDatabase.getInstance().getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/favorite_book/" + uid);
        favorite_book.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String isbn = ((ListData) listData.get(position)).getIsbn();
//                    System.out.println("比對的書: " + isbn);
//                    System.out.println("ISBN: " + ds.getValue());
//                    System.out.println(isbn.equals(ds.getValue().toString()));
                    if(isbn.equals(ds.getValue().toString())){
                        viewHolder.favorite.setChecked(true);
                        viewHolder.favorite.setEnabled(false);
                    }
                    //System.out.println("我的最愛: " + ds.getValue());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //viewHolder.title.setText(listData[position].getTitle());
        //viewHolder.price.setText("$ "+listData[position].getPrice());
//        viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //viewHolder.favorite.setChecked(true);
//                DatabaseReference favorite_book = FirebaseDatabase.getInstance().getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/favorite_book/" + uid);
//                favorite_book.child(data.getIsbn()).setValue(data.getIsbn());
//                System.out.println("點擊" + position);
//            }
//        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView title,price;
        private ImageView photoUrl;
        private CheckBox favorite;

        public LinearViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.bli_title);
            price = itemView.findViewById(R.id.bli_price);
            photoUrl = itemView.findViewById(R.id.bli_img);
            favorite = itemView.findViewById(R.id.bli_checkbox);

        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
