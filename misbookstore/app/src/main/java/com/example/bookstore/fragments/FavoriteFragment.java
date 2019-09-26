package com.example.bookstore.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.bookstore.BookInfoActivity;
import com.example.bookstore.BookInformation.LinearAdapter;
import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.Favorate.FavBookAdapter;
import com.example.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {
    private RecyclerView fl_main;
    private RelativeLayout ff_empty;
    private CheckBox fl_like_btn;
    private FirebaseUser user;
    private String uid;
    ArrayList<ListData> listData = new ArrayList<>();

    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
//        ArrayList<ListData> listData = new ArrayList<>();
        //listData.add(new ListData("沉默的遊行",1,"44","t","t","t","t","t","t","t",0));

        readUid(new UidCallback() {
            @Override
            public void onCallback(boolean value) {
                System.out.println("value____________________________________________ : " + value);
                if(value){
                    ff_empty = view.findViewById(R.id.ff_empty);
                    ff_empty.setVisibility(View.GONE);
                    readData(new MyCallback() {
                        @Override
                        public void onCallback(ArrayList value) {
                            fl_main = view.findViewById(R.id.fl_main);
                            fl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
                            Bookrecyclerview(value);
                        }

                    });
                }else{
                    ff_empty = view.findViewById(R.id.ff_empty);
                    ff_empty.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }
    public void readUid(UidCallback myCallback){
        DatabaseReference search_uid = FirebaseDatabase.getInstance().getReference("favorite_book");
        search_uid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean search = false;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String UID = ds.getKey().toString();
                    if(UID.equals(uid)){
                        search = true;
                    }

                }
                System.out.println("我的最愛搜尋____________________________________________ : " + search);
                myCallback.onCallback(search);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void readData(MyCallback myCallback) {

        ArrayList<ListData> listData = new ArrayList<>();
//        listData.clear();
        //連資料庫
        DatabaseReference ISBN_list = FirebaseDatabase.getInstance().getReference("favorite_book").child(uid);
        //抓ISBN
        ISBN_list.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData.clear();
                int count = 0;
                //根據ISBN抓書籍資訊
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    DatabaseReference book_info = FirebaseDatabase.getInstance().getReference("book_info").child(ds.getValue().toString());
                    book_info.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ListData bookList = dataSnapshot.getValue(ListData.class);
                            listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                    bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                    bookList.getClassification(), bookList.getIndex()));
                            myCallback.onCallback(listData);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("onCancelled",databaseError.toException());
                        }
                    });//end book_info ValueEventListener
                    count++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });//end  ISBN_list ValueEventListener
    }

    public interface MyCallback{
        void onCallback(ArrayList value);
    }

    public interface UidCallback{
        void onCallback(boolean value);
    }

    public void Bookrecyclerview (ArrayList listdata) {
        fl_main.setAdapter(new FavBookAdapter(getActivity(), listdata ,new FavBookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                ListData data = (ListData) listdata.get(pos);
                String isbn=data.getIsbn();
                String book_name=data.getTitle();
                String book_price=data.getPrice();
                Intent intent = new Intent(getActivity(), BookInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isbn",isbn);//傳遞String
                bundle.putString("title",book_name);
                bundle.putString("price",book_price);
                intent.putExtras(bundle);
                startActivity(intent);
//                Toast.makeText(getActivity(),"click..."+pos,Toast.LENGTH_SHORT).show();
            }
        }));
    }
}