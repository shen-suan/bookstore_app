package com.example.bookstore.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookstore.BookInfoActivity;
import com.example.bookstore.BookInformation.LinearAdapter;
import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.R;
import com.example.bookstore.ViewPagerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomepageFragment extends Fragment {


    TextView mTextMessage;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    //recycleview
    private RecyclerView home_new,home_search,home_hot,home_like;

    public HomepageFragment() {
        // Required empty public constructor

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.activity_home_page, container, false);
        View v=inflater.inflate(R.layout.activity_home_page, container, false);

        // Recyclerview的設定
//        ListData[] listData = {
//                new ListData("沉默的遊行", 180, "11","t","t","t","t","t","t","t",0),
//
//        };
        //新書
        //用於存放Firebase取回的資料，限定型態為ListData。
        ArrayList<ListData> listData = new ArrayList<>();
        //連資料庫
        DatabaseReference Book_new= FirebaseDatabase.getInstance().getReference("bookList_new");
        Book_new.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    ListData bookList = ds.getValue(ListData.class);
                    listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                            bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                            bookList.getClassification(), bookList.getIndex()));
                }
                home_search = v.findViewById(R.id.home_search);
                home_hot = v.findViewById(R.id.home_hot);
                home_like = v.findViewById(R.id.home_like);
                home_new = v.findViewById(R.id.home_new);
                BookItem(home_new,listData);
                BookItem(home_search,listData);
                BookItem(home_hot,listData);
                BookItem(home_like,listData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });
        //抓書本資料
        //最近搜尋
        //熱門
        //猜你喜歡
        //home_new = v.findViewById(R.id.home_new);
//        home_search = v.findViewById(R.id.home_search);
//        home_hot = v.findViewById(R.id.home_hot);
//        home_like = v.findViewById(R.id.home_like);
        //BookItem(home_new,listData);
//        BookItem(home_search,listData);
//        BookItem(home_hot,listData);
//        BookItem(home_like,listData);


       /* home_new.setLayoutManager(linearLayoutManager);
        home_new.setAdapter(new HorAdapter(getActivity(), listData ,new HorAdapter.OnItemClickListener(){
            @Override
            public void onClick(int pos) {
                String isbn=listData[pos].getIsbn();
                String book_name=listData[pos].getTitle();
                int book_price=listData[pos].getPrice();
                Intent intent = new Intent(getActivity(), BookInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isbn",isbn);//傳遞String
                bundle.putString("title",book_name);
                bundle.putInt("price",book_price);
                intent.putExtras(bundle);
                startActivity(intent);*/
    //                Toast.makeText(getActivity(),"click..."+isbn,Toast.LENGTH_SHORT).show();
      /*      }
        }));*/


        mTextMessage = v.findViewById(R.id.message);
        viewPager = v.findViewById(R.id.viewPager);
        sliderDotspanel = v.findViewById(R.id .SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8,0 ,8 , 0);

            sliderDotspanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0; i<dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return v;
    }

    private void BookItem(RecyclerView r, ArrayList listData){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        r.setLayoutManager(linearLayoutManager);
        r.setAdapter(new HorAdapter(getActivity(), listData ,new HorAdapter.OnItemClickListener(){
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
                //                Toast.makeText(getActivity(),"click..."+isbn,Toast.LENGTH_SHORT).show();
            }
        }));
    }


}

class HorAdapter extends RecyclerView.Adapter<HorAdapter.HorViewHolder>{

    private ArrayList listData;
    private ListData data;
    private Context mContext;
    private HorAdapter.OnItemClickListener mlistener;

    public HorAdapter(Context context, ArrayList list, HorAdapter.OnItemClickListener listener){
        this.mContext = context;
        this.listData = list;
        this.mlistener = listener;
    }

    public HorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HorViewHolder(LayoutInflater.from(mContext).inflate(R.layout.home_book_item,parent,false));
    }

    @Override
    public void onBindViewHolder(HorAdapter.HorViewHolder viewHolder, final int position) {
        data = (ListData) listData.get(position);
        Glide.with(mContext)
                .load(data.getUrl())
                .into(viewHolder.photo);
        viewHolder.title.setText(data.getTitle());
        viewHolder.price.setText("$ "+data.getPrice());
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
    class HorViewHolder extends RecyclerView.ViewHolder{

        private TextView title,price;
        private ImageView photo;

        public HorViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.hb_title);
            price = itemView.findViewById(R.id.hb_price);
            photo = itemView.findViewById(R.id.hb_image);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}
