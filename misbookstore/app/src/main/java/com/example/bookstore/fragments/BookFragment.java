package com.example.bookstore.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bookstore.BookInfoActivity;
import com.example.bookstore.BookInformation.LinearAdapter;
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
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment{

    private RecyclerView bl_main;
    private RadioGroup bl_menu;
    private RadioButton bl_category;
    private PopupWindow bl_pop;
    private FirebaseUser user;
    private String uid;


    public BookFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);


        // 設定要給 Adapter 的陣列為 listData
//        ArrayList<ListData> listData1 = new ArrayList<>();
//        listData1.add(new ListData("熱銷排行",1,"44"));
//        listData1.add(new ListData("熱銷排行",2,"55"));
//        listData1.add(new ListData("熱銷排行",3,"66"));
//        listData1.add(new ListData("熱銷排行",4,"77"));
//        listData1.add(new ListData("熱銷排行",5,"88"));
//        listData1.add(new ListData("熱銷排行",6,"99"));

//        ArrayList<ListData> listData2 = new ArrayList<>();
//        listData2.add(new ListData("依分類",1,"44"));
//        listData2.add(new ListData("依分類",2,"55"));
//        listData2.add(new ListData("依分類",3,"66"));
//        listData2.add(new ListData("依分類",4,"77"));
//        listData2.add(new ListData("依分類",5,"88"));
//        listData2.add(new ListData("依分類",6,"99"));
//
//        ArrayList<ListData> listData3 = new ArrayList<>();
//        listData3.add(new ListData("新書榜",1,"44"));
//        listData3.add(new ListData("新書榜",2,"55"));
//        listData3.add(new ListData("新書榜",3,"66"));
//        listData3.add(new ListData("新書榜",4,"77"));
//        listData3.add(new ListData("新書榜",5,"88"));
//        listData3.add(new ListData("新書榜",6,"99"));

        // Recyclerview的初始設定(熱銷排行)
        bl_main = view.findViewById(R.id.bl_main);
        bl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
        BookCreate_hot(view);

        //依分類的下拉選單
        bl_category = view.findViewById(R.id.bl_category);
        //Drawable 大小設定
        Drawable twoarrow = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            twoarrow = getResources().getDrawable(R.drawable.twoarrow,null);
        }
        if (twoarrow != null) {
            twoarrow.setBounds(0, 0, twoarrow.getIntrinsicHeight() / 4, twoarrow.getIntrinsicHeight() / 4);
        }
        bl_category.setCompoundDrawables(null, null, twoarrow, null);
        bl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewlist = getLayoutInflater().inflate(R.layout.book_list_category,null);

                bl_pop = new PopupWindow(viewlist,bl_menu.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                bl_pop.setFocusable(true);
                bl_pop.showAsDropDown(bl_menu);
                //取消popwindow後更新書籍資料
                bl_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    CheckBox b1 = viewlist.findViewById(R.id.B1);
                    CheckBox b2 = viewlist.findViewById(R.id.B2);
                    CheckBox b3 = viewlist.findViewById(R.id.B3);
                    CheckBox b4 = viewlist.findViewById(R.id.B4);
                    CheckBox b5 = viewlist.findViewById(R.id.B5);
                    CheckBox b6 = viewlist.findViewById(R.id.B6);
                    CheckBox b7 = viewlist.findViewById(R.id.B7);
                    CheckBox b8 = viewlist.findViewById(R.id.B8);
                    StringBuilder category_list = new StringBuilder();
                    @Override
                    public void onDismiss() {
                        if(b1.isChecked()){
                            category_list.append(b1.getText()).append(" ");
                        }
                        if(b2.isChecked()){
                            category_list.append(b2.getText()).append(" ");
                        }
                        if(b3.isChecked()){
                            category_list.append(b3.getText()).append(" ");
                        }
                        if(b4.isChecked()){
                            category_list.append(b4.getText()).append(" ");
                        }
                        if(b5.isChecked()){
                            category_list.append(b5.getText()).append(" ");
                        }
                        if(b6.isChecked()){
                            category_list.append(b6.getText()).append(" ");
                        }
                        if(b7.isChecked()){
                            category_list.append(b7.getText()).append(" ");
                        }
                        if(b8.isChecked()){
                            category_list.append(b8.getText()).append(" ");
                        }//end for
                        System.out.println("選擇: " + category_list);
                        BookCreate_category(view,category_list);
                    }
                });
            }
        });

        //menu設置改變監聽
        bl_menu = view.findViewById(R.id.bl_menu);
        bl_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.bl_hot:
                        BookCreate_hot(view);
                        break;
                    case R.id.bl_new:
                        BookCreate_new(view);
                        break;
                }
            }
        });

        return view;
    }
    //熱銷排行
    public void BookCreate_hot (View view){
        //用於存放Firebase取回的資料，限定型態為ListData。
        ArrayList<ListData> listData = new ArrayList<>();
        //連資料庫
        DatabaseReference Book_list = FirebaseDatabase.getInstance().getReference("bookList_hot");
        //抓書本資料
        Book_list.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    ListData bookList = ds.getValue(ListData.class);
                    listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                            bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                            bookList.getClassification(), bookList.getIndex()));
                }
                //System.out.println("Url"  +  listData.get(0).getUrl());

                // Recyclerview的設定
                bl_main = view.findViewById(R.id.bl_main);
                bl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
                Bookrecyclerview(listData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });
    }
    //新書排行
    public void BookCreate_new (View view){
        //用於存放Firebase取回的資料，限定型態為ListData。
        ArrayList<ListData> listData = new ArrayList<>();
        //連資料庫
        DatabaseReference Book_list = FirebaseDatabase.getInstance().getReference("book_info");
        Book_list.orderByChild("PublishDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    ListData bookList = ds.getValue(ListData.class);
                    listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                            bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                            bookList.getClassification(), bookList.getIndex()));
                    //System.out.println("title"  + listData.get(0).getTitle());

                }
                Collections.reverse(listData);

                // Recyclerview的設定
                bl_main = view.findViewById(R.id.bl_main);
                bl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
                Bookrecyclerview(listData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });
    }
    //分類書籍
    public void BookCreate_category (View view, StringBuilder choose_list){
        //分割choose_list
        String choose = choose_list.toString();
        String[] tokens = choose.split(" ");

        //用於存放Firebase取回的資料，限定型態為ListData。
        ArrayList<ListData> listData = new ArrayList<>();
        //連資料庫
        DatabaseReference Book_list = FirebaseDatabase.getInstance().getReference("book_info");
        Book_list.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    ListData bookList = ds.getValue(ListData.class);
                    if(choose.length() == 0){
                        listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                bookList.getClassification(), bookList.getIndex()));
                    }else{
                        for(String c : tokens){
                            if(bookList.getClassification().equals(c)){
                                listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                        bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                        bookList.getClassification(), bookList.getIndex()));
                            }
                        }
                    }
                    //System.out.println("title"  + listData.get(0).getTitle());
                }

                // Recyclerview的設定
                bl_main = view.findViewById(R.id.bl_main);
                bl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
                Bookrecyclerview(listData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });
    }

    public void Bookrecyclerview (ArrayList listData) {
        bl_main.setAdapter(new LinearAdapter(getActivity(), listData ,new LinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                ListData data = (ListData) listData.get(pos);
                String isbn= data.getIsbn();
                String book_name= data.getTitle();
                String book_price= data.getPrice();
                Intent intent = new Intent(getActivity(), BookInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isbn",isbn);//傳遞String
                bundle.putString("title",book_name);
                bundle.putString("price",book_price);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));
    }

}