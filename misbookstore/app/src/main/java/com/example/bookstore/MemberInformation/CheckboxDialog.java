package com.example.bookstore.MemberInformation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookstore.R;

public class CheckboxDialog extends AppCompatDialogFragment{
    private static final String TITLE_TAG = "title";
    private static final String INITIAL_TEXT_TAG = "initial_text";
    private static final String HINT_TAG = "hint";
    private static final String CALLBACK_ID_TAG = "callback_id";


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

        textView.setText(title);
        if (!TextUtils.isEmpty(hint)) {
            hintView.setText(hint);
        } else {
            hintView.setVisibility(View.GONE);
        }

        builder.setCancelable(true)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StringBuilder liked_booktype = new StringBuilder();
                        int count1 = checkbox_list1.getChildCount();
                        int count2 = checkbox_list2.getChildCount();
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
                                    isEmpty = false;
                                }
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
                                    isEmpty = false;
                                }
                            }
                        }
                        if (getTargetFragment() instanceof Callback) {
                            if(!isEmpty) {
                                Toast.makeText(getActivity(), "你選擇的是:"+liked_booktype.toString(), Toast.LENGTH_SHORT).show();
                                ((Callback) getTargetFragment())
                                        .onSuccessfulCheckbox(liked_booktype.toString(), callbackId);
                            }
                            else{
                                Toast.makeText(getActivity(), "請至少選擇一項", Toast.LENGTH_SHORT).show();
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
