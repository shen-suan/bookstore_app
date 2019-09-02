package com.example.bookstore.MemberInformation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.Book_types;
import com.example.bookstore.R;
import com.example.bookstore.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckboxDialog extends AppCompatDialogFragment{
    private static final String TITLE_TAG = "title";
    private static final String INITIAL_TEXT_TAG = "initial_text";
    private static final String HINT_TAG = "hint";
    private static final String CALLBACK_ID_TAG = "callback_id";
    boolean [] arraySelected = new boolean[8];
    boolean [] priviousSelected = new boolean[8];

    public static CheckboxDialog newInstance(String title,
                                          String initialText,
                                          String hint,
                                          int callbackId) {
        CheckboxDialog checkboxDialog = new CheckboxDialog();
        Bundle args = new Bundle();

        args.putString(TITLE_TAG, title);
        args.putString(INITIAL_TEXT_TAG, initialText);
        args.putString(HINT_TAG, hint);
        args.putInt(CALLBACK_ID_TAG, callbackId);

        checkboxDialog.setArguments(args);
        return checkboxDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptView = inflater.inflate(R.layout.checkbox_dialog, null);
        builder.setView(promptView);

        final TextView textView = promptView.findViewById(R.id.checkbox_label);
        final TextView hintView = promptView.findViewById(R.id.checkbox_hint);
        CheckBox ch1 = promptView.findViewById(R.id.ch1);
        CheckBox ch2 = promptView.findViewById(R.id.ch2);
        CheckBox ch3 = promptView.findViewById(R.id.ch3);
        CheckBox ch4 = promptView.findViewById(R.id.ch4);
        CheckBox ch5 = promptView.findViewById(R.id.ch5);
        CheckBox ch6 = promptView.findViewById(R.id.ch6);
        CheckBox ch7 = promptView.findViewById(R.id.ch7);
        CheckBox ch8 = promptView.findViewById(R.id.ch8);

        final LinearLayout checkbox_list1 = promptView.findViewById(R.id.checkbox_list_col1);
        final LinearLayout checkbox_list2 = promptView.findViewById(R.id.checkbox_list_col2);

        Bundle args = getArguments();
        String title = args.getString(TITLE_TAG);
        String hint = args.getString(HINT_TAG);

        final int callbackId = args.getInt(CALLBACK_ID_TAG);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/user_profile/"+uid);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
                Book_types bookType = dataSnapshot.child("book_type").getValue(Book_types.class);
                arraySelected[0] = bookType.getSearchtools();
                arraySelected[1] = bookType.getEducation();
                arraySelected[2] = bookType.getBiography();
                arraySelected[3] = bookType.getKid();
                arraySelected[4] = bookType.getPhilosophy();
                arraySelected[5] = bookType.getTravel();
                arraySelected[6] = bookType.getPsychology();
                arraySelected[7] = bookType.getSociology();
//                for(int i=0;i<8;i++) System.out.println(arraySelected[i]);
                //System.arraycopy(來源, 起始索引, 目的, 起始索引, 複製長度)
//                System.arraycopy(arraySelected, 0, priviousSelected, 0, arraySelected.length);
                if(arraySelected[0]) ch1.setChecked(true);
//                else ch1.setChecked(false);
                if(arraySelected[1]) ch2.setChecked(true);
//                else ch2.setChecked(false);
                if(arraySelected[2]) ch3.setChecked(true);
//                else ch3.setChecked(false);
                if(arraySelected[3]) ch4.setChecked(true);
//                else ch4.setChecked(false);
                if(arraySelected[4]) ch5.setChecked(true);
//                else ch5.setChecked(false);
                if(arraySelected[5]) ch6.setChecked(true);
//                else ch6.setChecked(false);
                if(arraySelected[6]) ch7.setChecked(true);
//                else ch7.setChecked(false);
                if(arraySelected[7]) ch8.setChecked(true);
//                else ch8.setChecked(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), " Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        textView.setText(title);
        if (!TextUtils.isEmpty(hint)) {
            hintView.setText(hint);
        } else {
            hintView.setVisibility(View.GONE);
        }
        //初始化陣列 之後要註解
        for(int i = 0;i < 8;i++){
            arraySelected[i]=false;
        }

        builder.setCancelable(true)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder liked_booktype = new StringBuilder();
                        int count = 0;
                        int count1 = checkbox_list1.getChildCount();
                        int count2 = checkbox_list2.getChildCount();
                        int select_id = 0;
                        boolean isEmpty = true;
                        for(int i = 0;i < count1;i++){
                            //获得子控件对象
                            View child = checkbox_list1.getChildAt(i);
                            //判断是否是CheckBox
                            if(child instanceof CheckBox){
                            //转为CheckBox对象
                                CheckBox cb = (CheckBox)child;
                                if(cb.isChecked()){
                                    liked_booktype.append(cb.getText()).append(" ");
                                    arraySelected[select_id]=true;
                                    count++;
                                    isEmpty = false;
                                } else arraySelected[select_id]=false;
                                select_id++;
                            }
                        }
                        for(int i = 0;i < count2;i++){
                            //获得子控件对象
                            View child = checkbox_list2.getChildAt(i);
                            //判断是否是CheckBox
                            if(child instanceof CheckBox){
                                //转为CheckBox对象
                                CheckBox cb = (CheckBox)child;
                                if(cb.isChecked()){
                                    liked_booktype.append(cb.getText()).append(" ");
                                    arraySelected[select_id]=true;
                                    count++;
                                    isEmpty = false;
                                }   else arraySelected[select_id]=false;
                                select_id++;
                            }
                        }
                        System.out.println(count);
                        if (getTargetFragment() instanceof Callback) {
                            if(count>3) {
                                Toast.makeText(getActivity(), "最多選擇三項", Toast.LENGTH_SHORT).show();
                                System.arraycopy(priviousSelected, 0, arraySelected, 0, arraySelected.length);
                            }
                            else if(!isEmpty) {
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("books",liked_booktype.toString());
                                myRef.updateChildren(childUpdates);
                                //書籍種類
                                Map<String, Object> bookupdate = new HashMap<>();
                                bookupdate .put("searchtools",arraySelected[0]);
                                bookupdate .put("education",arraySelected[1]);
                                bookupdate .put("biography",arraySelected[2]);
                                bookupdate .put("kid",arraySelected[3]);
                                bookupdate .put("philosophy",arraySelected[4]);
                                bookupdate .put("travel",arraySelected[5]);
                                bookupdate .put("psychology",arraySelected[6]);
                                bookupdate .put("sociology",arraySelected[7]);
//                                for(int i=0;i<8;i++) System.out.println(arraySelected[i]);
                                myRef.child("book_type").updateChildren(bookupdate);
                                Toast.makeText(getActivity(), "你選擇的是:"+liked_booktype.toString(), Toast.LENGTH_SHORT).show();
                                ((Callback) getTargetFragment())
                                        .onSuccessfulCheckbox(liked_booktype.toString(), callbackId);
                            }
                            else{
                                Toast.makeText(getActivity(), "請至少選擇一項", Toast.LENGTH_SHORT).show();
                                System.arraycopy(priviousSelected, 0, arraySelected, 0, arraySelected.length);
                            }
                        } else {
                            throw new ClassCastException("Target fragment doesn't implement CheckboxDialog.Callback");
                        }
                    }
                })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public interface Callback {
        void onSuccessfulCheckbox(String input, int callbackId);
    }
}
