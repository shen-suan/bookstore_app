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
import android.widget.Toast;

import com.example.bookstore.BookInfoActivity;
import com.example.bookstore.BookInformation.LinearAdapter;
import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookFragment extends Fragment implements RadioGroup.OnCheckedChangeListener{

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
//        ListData[] listData = {
//                new ListData("沉默的遊行", 180, "11"),
//                new ListData("訂閱經濟", 250, "22"),
//                new ListData("不賣東西賣體驗", 175, "33"),
//        };
        ArrayList<ListData> listData = new ArrayList<>();
        listData.add(new ListData("沉默的遊行",1,"44"));
        listData.add(new ListData("訂閱經濟",2,"55"));
        listData.add(new ListData("不賣東西賣體驗",3,"66"));
        listData.add(new ListData("沉默的遊行",4,"77"));
        listData.add(new ListData("訂閱經濟",5,"88"));
        listData.add(new ListData("不賣東西賣體驗",6,"99"));
        // Recyclerview的設定
        bl_main = view.findViewById(R.id.bl_main);
        bl_main.setLayoutManager(new LinearLayoutManager(getActivity()));
        bl_main.setAdapter(new LinearAdapter(getActivity(), listData ,new LinearAdapter.OnItemClickListener() {
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
//                Toast.makeText(getActivity(),"click..."+isbn,Toast.LENGTH_SHORT).show();
            }
        }));

        //依分類的下拉選單
        bl_category = view.findViewById(R.id.bl_category);

        //Drawable size setting
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
            }
        });
        //menu設置改變監聽
        bl_menu = view.findViewById(R.id.bl_menu);
        bl_menu.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.bl_hot:
                Toast.makeText(getActivity().getApplicationContext(),"熱銷排行",Toast.LENGTH_LONG).show();
                break;
            case R.id.bl_new:
                Toast.makeText(getActivity().getApplicationContext(),"新書榜",Toast.LENGTH_LONG).show();
                break;

        }
    }

}
