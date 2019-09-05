package com.example.bookstore.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.bookstore.BookInfoActivity;
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
    private CheckBox fl_like_btn;
    private FirebaseUser user;
    private String uid;
    public String[] isbn_list;

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
        ArrayList<ListData> listData = new ArrayList<>();
        listData.add(new ListData("沉默的遊行",1,"44","t","t","t","t","t","t","t",0));
//        listData.add(new ListData("訂閱經濟",2,"55","t"));
//        listData.add(new ListData("不賣東西賣體驗",3,"66","t"));
//        listData.add(new ListData("沉默的遊行",4,"77","t"));
//        listData.add(new ListData("訂閱經濟",5,"88","t"));
//        listData.add(new ListData("不賣東西賣體驗",6,"99","t"));
        //連資料庫
        DatabaseReference fav_list = FirebaseDatabase.getInstance().getReference("favorite_book").child(uid);
        fav_list.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                isbn_list = new String[(int)dataSnapshot.getChildrenCount()];
                for(DataSnapshot ds :dataSnapshot.getChildren()){
                    isbn_list[i] = ds.getValue().toString();
                    i++;
                }
                for(String a : isbn_list){
                    System.out.println("陣列:" + a);
                    DatabaseReference bookInfo = FirebaseDatabase.getInstance().getReference("").child(uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fl_main = view.findViewById(R.id.fl_main);
        fl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
        fl_main.setAdapter(new FavBookAdapter(getActivity(), listData ,new FavBookAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                String isbn=listData.get(pos).getIsbn();
                String book_name=listData.get(pos).getTitle();
                int book_price=listData.get(pos).getPrice();
                Intent intent = new Intent(getActivity(), BookInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isbn",isbn);//傳遞String
                bundle.putString("title",book_name);
                bundle.putInt("price",book_price);
                intent.putExtras(bundle);
                startActivity(intent);
//                Toast.makeText(getActivity(),"click..."+pos,Toast.LENGTH_SHORT).show();
            }
        }));

        return view;
    }

}
