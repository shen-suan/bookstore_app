package com.example.bookstore.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstore.Book_types;
import com.example.bookstore.LoginActivity;
import com.example.bookstore.MemberInformation.CircleImageView;
import com.example.bookstore.MyProfileActivity;
import com.example.bookstore.R;
import com.example.bookstore.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    boolean [] arraySelected = new boolean[8];

    public ProfileFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        View myProfileView = view.findViewById(R.id.my_profile);
        View logout = view.findViewById(R.id.mi_btn_log_out);
        TextView pf_account = view.findViewById(R.id.profile_account);
        TextView pf_nickname = view.findViewById(R.id.profile_nickname);
        TextView pf_name = view.findViewById(R.id.profile_name);
        TextView pf_gender = view.findViewById(R.id.profile_gender);
        TextView pf_birth = view.findViewById(R.id.profile_birth);
        CircleImageView pf_pic = view.findViewById(R.id.profile_pic);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        //頭貼
        Glide.with(getActivity())
                .load(user.getPhotoUrl())
                .apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.head))
                .into(pf_pic);
        //連接資料庫
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/user_profile/"+uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
//                Book_types bookType = dataSnapshot.child("book_type").getValue(Book_types.class);
                pf_account.setText(user.getAccount());
                pf_nickname.setText(user.getNickname());
                pf_name.setText(user.getName());
                pf_gender.setText(user.getUserGender());
                pf_birth.setText(user.getbirthday());
                LinearLayout linearLayout = view.findViewById(R.id.profile_book_type);
                linearLayout.removeAllViews();
//                arraySelected[0] = bookType.getSearchtools();
//                arraySelected[1] = bookType.getEducation();
//                arraySelected[2] = bookType.getBiography();
//                arraySelected[3] = bookType.getKid();
//                arraySelected[4] = bookType.getPhilosophy();
//                arraySelected[5] = bookType.getTravel();
//                arraySelected[6] = bookType.getPsychology();
//                arraySelected[7] = bookType.getSociology();
                String book_type = user.getBooks();
                String[] tokens = book_type.split(" ");
                int margin_in_dp = 5;
                final float scale = getResources().getDisplayMetrics().density;
                int margin_in_px = (int) (margin_in_dp * scale + 0.5f);
                int padding_in_dp = 8;
                final float scale2 = getResources().getDisplayMetrics().density;
                int padding_in_px = (int) (padding_in_dp * scale2 + 0.5f);

                for(int i = 0;i<tokens.length;i++) {
                    TextView textView = new TextView(view.getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10*padding_in_px,LinearLayout.LayoutParams.WRAP_CONTENT,0);
                    layoutParams.setMargins(margin_in_px,margin_in_px,margin_in_px,margin_in_px);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(0,padding_in_px,0,padding_in_px);
                    textView.setTextColor(Color.BLACK);
                    textView.setBackgroundResource(R.drawable.bg_booktype);
                    textView.setText(tokens[i]);
                    textView.setLayoutParams(layoutParams);
                    linearLayout.addView(textView);
                }
                int count = linearLayout.getChildCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), " Database Error", Toast.LENGTH_SHORT).show();
            }
        });





        myProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;

    }



}
