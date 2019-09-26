package com.example.bookstore.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookstore.BookInfoActivity;
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
public class HomepageFragment extends Fragment {

    private View v;
    //recycleview
    private RecyclerView home_new,home_search,home_hot,home_like,home_rank;
    boolean isRunning = false;
    private ViewPager viewPager;
    private int[] imageResIds;
    private ArrayList<ImageView> imageViewList;
    private LinearLayout ll_point_container;
    private int previousSelectedPosition = 0;
    private Handler handler;
    private FirebaseUser user;
    private String uid;
    private boolean search = false;
    private boolean like = false;


    public HomepageFragment() {
        // Required empty public constructor

    }

    public interface MyCallback{
        void onCallback(ArrayList value);
    }

    public interface TypeCallback{
        void onCallback(String value);
    }

    //最近搜尋
    public void readData(MyCallback myCallback) {
        ArrayList<ListData> listData = new ArrayList<>();
        //連資料庫
        DatabaseReference ISBN_list = FirebaseDatabase.getInstance().getReference("bookList_search").child(uid);
        //抓ISBN
        ISBN_list.orderByValue().limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData.clear();
                //根據ISBN抓書籍資訊
                for (DataSnapshot ds : dataSnapshot.getChildren() ){
                    DatabaseReference book_info = FirebaseDatabase.getInstance().getReference("book_info").child(ds.getKey());
                    book_info.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ListData bookList = dataSnapshot.getValue(ListData.class);
                            listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                    bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                    bookList.getClassification(), bookList.getIndex()));
                            Collections.reverse(listData);
                            myCallback.onCallback(listData);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("onCancelled",databaseError.toException());
                        }
                    });//end book_info ValueEventListener
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });//end  ISBN_list ValueEventListener
    }

    //取的使用者喜歡書籍別
    public void readType(TypeCallback myCallback){
        DatabaseReference user_like= FirebaseDatabase.getInstance().getReference("user_profile").child(uid).child("books");
        user_like.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String type = dataSnapshot.getValue().toString();
                myCallback.onCallback(type);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.activity_home_page, container, false);
        v=inflater.inflate(R.layout.activity_home_page, container, false);

        handler = new Handler();
        handler.postDelayed(new TimerRunnable(),3000);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        //新書排行
        //用於存放Firebase取回的資料，限定型態為ListData。
        ArrayList<ListData> listData = new ArrayList<>();
        //連資料庫
        DatabaseReference Book_new= FirebaseDatabase.getInstance().getReference("book_info");
        Book_new.orderByChild("PublishDate").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ListData bookList = ds.getValue(ListData.class);
                    listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                            bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                            bookList.getClassification(), bookList.getIndex()));
                }
                Collections.reverse(listData);
                home_new = v.findViewById(R.id.home_new);
                BookItem(home_new,listData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });

        //最近搜尋
        DatabaseReference search_uid = FirebaseDatabase.getInstance().getReference("bookList_search");
        search_uid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getKey() == uid){
                        search = true;
                    }else {
                        search = false;
                    }
                }
                if(search){
                    System.out.println("有最近搜尋____________________________________________");
                    readData(new MyCallback() {
                        @Override
                        public void onCallback(ArrayList value) {
                            home_search = v.findViewById(R.id.home_search);
                            BookItem(home_search,value);
                        }
                    });
                }else {
                    System.out.println("沒有最近搜尋____________________________________________");
                    ArrayList<ListData> listData_search = new ArrayList<>();
                    DatabaseReference Book_search= FirebaseDatabase.getInstance().getReference("book_info");
                    Book_search.orderByChild("Price").limitToLast(10).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                ListData bookList = ds.getValue(ListData.class);
                                //System.out.println("日期: " + bookList.getPublishDate());
                                listData_search.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                        bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                        bookList.getClassification(), bookList.getIndex()));
                            }
                            Collections.reverse(listData_search);
                            home_search = v.findViewById(R.id.home_search);
                            BookItem(home_search,listData_search);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("onCancelled",databaseError.toException());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //熱門書籍
        ArrayList<ListData> listData_hot = new ArrayList<>();
        //連資料庫
        DatabaseReference Book_hot= FirebaseDatabase.getInstance().getReference("bookList_hot");
        Book_hot.limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ListData bookList = ds.getValue(ListData.class);
                    //System.out.println("日期: " + bookList.getPublishDate());
                    listData_hot.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                            bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                            bookList.getClassification(), bookList.getIndex()));
                }
                home_hot = v.findViewById(R.id.home_hot);
                BookItem(home_hot,listData_hot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });


        //猜你喜歡
        ArrayList<ListData> listData_like = new ArrayList<>();
        DatabaseReference user_like= FirebaseDatabase.getInstance().getReference("user_profile").child(uid).child("books");
        user_like.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listData_like.clear();
                String type = dataSnapshot.getValue().toString();
                if(type.equals("尚未選擇")){
                    like = false;
                }else {
                    like = true;
                }
                System.out.println("like -----------------------------------------------------------" + like);
                if(like){
                    DatabaseReference Book_like1= FirebaseDatabase.getInstance().getReference("book_info");
                    Book_like1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String[] tokens = type.split(" ");
                            for(String a : tokens){
                                int i = 0;
                                for(DataSnapshot ds : dataSnapshot.getChildren()){
                                    ListData bookList = ds.getValue(ListData.class);
                                    if(bookList.getClassification().equals(a) && i<3 ){
                                        listData_like.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                                bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                                bookList.getClassification(), bookList.getIndex()));
                                        i++;
                                    }
                                }
                            }
                            home_like = v.findViewById(R.id.home_like);
                            BookItem(home_like,listData_like);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else {
                    DatabaseReference Book_search= FirebaseDatabase.getInstance().getReference("book_info");
                    Book_search.orderByChild("Price").limitToFirst(10).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                ListData bookList = ds.getValue(ListData.class);
                                listData_like.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                                        bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                                        bookList.getClassification(), bookList.getIndex()));
                            }
                            home_like = v.findViewById(R.id.home_like);
                            BookItem(home_like,listData_like);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.w("onCancelled",databaseError.toException());
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //暢銷排行
        ArrayList<ListData> listData_rank = new ArrayList<>();
        //連資料庫
        DatabaseReference Book_rank= FirebaseDatabase.getInstance().getReference("bookList_hot");
        Book_rank.orderByChild("Price").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ListData bookList = ds.getValue(ListData.class);
                    //System.out.println("日期: " + bookList.getPublishDate());
                    listData_rank.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                            bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                            bookList.getClassification(), bookList.getIndex()));

                }
                Collections.reverse(listData_rank);
                //首頁排行的recyclerview
                home_rank = v.findViewById(R.id.home_rank);
                home_rank.setLayoutManager(new LinearLayoutManager(getActivity()));
                home_rank.setAdapter(new RankAdapter(getActivity(), listData_rank ,new RankAdapter.OnItemClickListener(){
                    @Override
                    public void onClick(int pos) {

                        ListData data = listData_rank.get(pos);
                        String isbn=data.getIsbn();
                        String book_name=data.getTitle();
                        int book_price = Integer.parseInt((data.getPrice()));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("onCancelled",databaseError.toException());
            }
        });



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

        //自動輪播最新消息
        // 初始化布局 View视图
        initViews();
        // Model数据
        initData();
        // Controller 控制器
        initAdapter();

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
    //輪播
    class TimerRunnable implements Runnable{
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            if (handler!=null){
                handler.postDelayed(this,4000);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        handler = null;
    }
    private void initViews() {
        viewPager = v.findViewById(R.id.viewPager);
//      viewPager.setOffscreenPageLimit(1);// 左右各保留几个对象
        ll_point_container = v.findViewById(R.id.SliderDots);
    }
    private void initData() {
        // 初始化要显示的数据
        // 图片资源id数组
        imageResIds = new int[]{R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
        // 初始化要展示的5个ImageView
        imageViewList = new ArrayList<ImageView>();
        LinearLayout.LayoutParams params;
        for (int i = 0; i < imageResIds.length; i++) {
            // 初始化要显示的图片对象
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundResource(imageResIds[i]);
            imageViewList.add(imageView);
            // 加小白点, 指示器
            View pointView = new View(getActivity());
//            if (i == 0) {
//                pointView.setBackgroundResource(R.drawable.point_select);
//            } else {
//                pointView.setBackgroundResource(R.drawable.point_normal);
//            }
            pointView.setBackgroundResource(R.drawable.point_normal);
            params = new LinearLayout.LayoutParams(15, 15);
            if(i != 0)
//            params.setMargins(8,8 ,8 , 8);
                params.leftMargin = 30;
            // 设置默认所有都不可用
//            pointView.setEnabled(false);
            ll_point_container.addView(pointView, params);
        }
    }
    private void initAdapter() {
//        ll_point_container.getChildAt(0).setEnabled(true);
        ll_point_container.getChildAt(0).setBackgroundResource(R.drawable.point_select);
        previousSelectedPosition = 0;
        // 设置适配器
        viewPager.setAdapter(new MyAdapter());
        // 默认设置到中间的某个位置
//        int pos = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2 % imageViewList.size());
        // 2147483647 / 2 = 1073741823 - (1073741823 % 5)
        viewPager.setCurrentItem(1000*imageViewList.size()); // 设置到某个位置
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 新的条目被选中时调用
                System.out.println("onPageSelected: " + position);
                int newPosition = position % imageViewList.size();
//      for (int i = 0; i < ll_point_container.getChildCount(); i++) {
//          View childAt = ll_point_container.getChildAt(position);
//          childAt.setEnabled(position == i);
//      }
                // 把之前的禁用, 把最新的启用, 更新指示器
//                ll_point_container.getChildAt(previousSelectedPosition).setEnabled(false);
//                ll_point_container.getChildAt(newPosition).setEnabled(true);
//                for (int i = 0; i < imageViewList.size(); i++) {
//                    if (position == i) {
//                        imageViewList.get(i).setBackgroundResource(R.drawable.point_select);
//                    } else {
//                        imageViewList.get(i).setBackgroundResource(R.drawable.point_normal);
//                    }
//                }
                ll_point_container.getChildAt(newPosition).setBackgroundResource(R.drawable.point_select);
                ll_point_container.getChildAt(previousSelectedPosition).setBackgroundResource(R.drawable.point_normal);
                // 记录之前的位置
                previousSelectedPosition  = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        // 3. 指定复用的判断逻辑, 固定写法
        @Override
        public boolean isViewFromObject(View view, Object object) {
//          System.out.println("isViewFromObject: "+(view == object));
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return view == object;
        }
        // 1. 返回要显示的条目内容, 创建条目
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println("instantiateItem初始化: " + position);
            // container: 容器: ViewPager
            // position: 当前要显示条目的位置 0 -> 4
//          newPosition = position % 5
            int newPosition = position % imageViewList.size();
            ImageView imageView = imageViewList.get(newPosition);
            // a. 把View对象添加到container中
            if (imageView != null) {
                ViewGroup parentViewGroup = (ViewGroup) imageView.getParent();
                if (parentViewGroup != null ) {
                    parentViewGroup.removeView(imageView);
                }
            }
            container.addView(imageView);
            // b. 把View对象返回给框架, 适配器
            return imageView; // 必须重写, 否则报异常
        }
        // 2. 销毁条目
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // object 要销毁的对象
            System.out.println("destroyItem销毁: " + position);
//            container.removeView((View)object);
        }
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
class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder>{

    private ArrayList listData;
    private ListData data;
    private Context rContext;
    private RankAdapter.OnItemClickListener rlistener;
    private FirebaseUser user;
    private String uid;

    public RankAdapter(Context context, ArrayList list, RankAdapter.OnItemClickListener listener){
        this.rContext = context;
        this.listData = list;
        this.rlistener = listener;
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }

    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RankViewHolder(LayoutInflater.from(rContext).inflate(R.layout.home_book_rank,parent,false));
    }

    @Override
    public void onBindViewHolder(RankAdapter.RankViewHolder viewHolder, final int position) {
        data = (ListData) listData.get(position);
        Glide.with(rContext)
                .load(data.getUrl())
                .into(viewHolder.photo);
        viewHolder.title.setText(data.getTitle());
        viewHolder.price.setText("$ "+data.getPrice());
        viewHolder.rank.setText(Integer.toString(position+1));
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
                Toast.makeText(rContext,"已加入我的最愛",Toast.LENGTH_LONG).show();
                //System.out.println( bookInfo.getTitle() + "加入我的最愛");
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlistener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    class RankViewHolder extends RecyclerView.ViewHolder{

        private TextView title,price,rank;
        private CheckBox unlike;
        private ImageView photo;


        public RankViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.hbr_title);
            price = itemView.findViewById(R.id.hbr_price);
            photo = itemView.findViewById(R.id.hbr_img);
            rank = itemView.findViewById(R.id.hbr_rank);
            unlike = itemView.findViewById(R.id.hbr_checkbox);
        }
    }

    public interface OnItemClickListener{
        void onClick(int pos);
    }
}