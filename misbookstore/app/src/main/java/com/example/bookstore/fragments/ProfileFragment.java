package com.example.bookstore.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bookstore.LoginActivity;
import com.example.bookstore.MyProfileActivity;
import com.example.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

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
