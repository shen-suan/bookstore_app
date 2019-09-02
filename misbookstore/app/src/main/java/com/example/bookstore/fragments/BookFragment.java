package com.example.bookstore.fragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bookstore.BookInfoActivity;
import com.example.bookstore.BookInformation.LinearAdapter;
import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment{

    private RecyclerView bl_main;
    private RadioGroup bl_menu;
    private RadioButton bl_category;
    private PopupWindow bl_pop;


    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        // 設定要給 Adapter 的陣列為 listData
        ArrayList<ListData> listData1 = new ArrayList<>();
        listData1.add(new ListData("熱銷排行",1,"44"));
        listData1.add(new ListData("熱銷排行",2,"55"));
        listData1.add(new ListData("熱銷排行",3,"66"));
        listData1.add(new ListData("熱銷排行",4,"77"));
        listData1.add(new ListData("熱銷排行",5,"88"));
        listData1.add(new ListData("熱銷排行",6,"99"));

        ArrayList<ListData> listData2 = new ArrayList<>();
        listData2.add(new ListData("依分類",1,"44"));
        listData2.add(new ListData("依分類",2,"55"));
        listData2.add(new ListData("依分類",3,"66"));
        listData2.add(new ListData("依分類",4,"77"));
        listData2.add(new ListData("依分類",5,"88"));
        listData2.add(new ListData("依分類",6,"99"));

        ArrayList<ListData> listData3 = new ArrayList<>();
        listData3.add(new ListData("新書榜",1,"44"));
        listData3.add(new ListData("新書榜",2,"55"));
        listData3.add(new ListData("新書榜",3,"66"));
        listData3.add(new ListData("新書榜",4,"77"));
        listData3.add(new ListData("新書榜",5,"88"));
        listData3.add(new ListData("新書榜",6,"99"));

        // Recyclerview的初始設定(熱銷排行)
        bl_main = view.findViewById(R.id.bl_main);
        bl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
        BookCreate(listData1);

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
                    @Override
                    public void onDismiss() {
                        BookCreate(listData2);
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
                        BookCreate(listData1);
                        break;
                    case R.id.bl_new:
                        BookCreate(listData3);
                        break;
                }
            }
        });

        return view;
    }

    public void BookCreate (ArrayList listData){
        bl_main.setAdapter(new LinearAdapter(getActivity(), listData ,new LinearAdapter.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                ListData data = (ListData) listData.get(pos);
                String isbn= data.getIsbn();
                String book_name= data.getTitle();
                int book_price= data.getPrice();
                Intent intent = new Intent(getActivity(), BookInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isbn",isbn);//傳遞String
                bundle.putString("title",book_name);
                bundle.putInt("price",book_price);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }));
    }

}