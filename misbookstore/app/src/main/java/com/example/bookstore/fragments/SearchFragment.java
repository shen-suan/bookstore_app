package com.example.bookstore.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import com.example.bookstore.BookInfoActivity;
import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.R;
import com.example.bookstore.Search.SearchAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String QUERY_TAG = "query";
    private String query;
    private RecyclerView sl_main;
    private CheckBox sl_like_btn;
    private FirebaseUser user;
    private String uid;

    public static SearchFragment newInstance(String query) {
        SearchFragment search = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(QUERY_TAG, query);
        search.setArguments(args);
        return search;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        if (getArguments() != null) {
            query = getArguments().getString(QUERY_TAG);
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
//        ArrayList<ListData> listData = new ArrayList<>();
        //listData.add(new ListData("沉默的遊行",1,"44","t","t","t","t","t","t","t",0));

        readData(new FavoriteFragment.MyCallback() {
            @Override
            public void onCallback(ArrayList value) {
                sl_main = view.findViewById(R.id.sl_main);
                sl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
                Bookrecyclerview(value);
            }

        });



//        fl_main = view.findViewById(R.id.fl_main);
//        fl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
//        fl_main.setAdapter(new FavBookAdapter(getActivity(), listData ,new FavBookAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int pos) {
//                String isbn=listData.get(pos).getIsbn();
//                String book_name=listData.get(pos).getTitle();
//                int book_price=listData.get(pos).getPrice();
//                Intent intent = new Intent(getActivity(), BookInfoActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("isbn",isbn);//傳遞String
//                bundle.putString("title",book_name);
//                bundle.putInt("price",book_price);
//                intent.putExtras(bundle);
//                startActivity(intent);
////                Toast.makeText(getActivity(),"click..."+pos,Toast.LENGTH_SHORT).show();
//            }
//        }));
//
        return view;
    }
    public void readData(FavoriteFragment.MyCallback myCallback) {
        ArrayList<ListData> listData = new ArrayList<>();
//        listData.clear();
        //連資料庫
        DatabaseReference ISBN_list = FirebaseDatabase.getInstance().getReference("book_info");
        //抓書籍資訊
        ISBN_list.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    ListData bookList = ds.getValue(ListData.class);
                    if(bookList.getTitle().indexOf(query) != -1){
                        listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                bookList.getClassification(), bookList.getIndex()));
                    }
                    myCallback.onCallback(listData);
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

    public void Bookrecyclerview (ArrayList listData) {
        sl_main.setAdapter(new SearchAdapter(getActivity(), listData ,new SearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                ListData data = (ListData) listData.get(pos);
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
