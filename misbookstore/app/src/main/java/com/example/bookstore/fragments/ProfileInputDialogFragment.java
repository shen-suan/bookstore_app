package com.example.bookstore.fragments;


import android.support.v7.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInputDialogFragment extends DialogFragment {
    private static final String TITLE_TAG = "title";
    private static final String INITIAL_TEXT_TAG = "initial_text";
    private static final String HINT_TAG = "hint";
    private static final String NAME_TYPE_TAG = "nameornickname";
    private static final String CALLBACK_ID_TAG = "callback_id";


    public static ProfileInputDialogFragment newInstance(String title,
                                                         String initialText,
                                                         String hint,
                                                         String nameornickname,
                                                         int callbackId) {
        ProfileInputDialogFragment profileInputDialogFragment = new ProfileInputDialogFragment();
        Bundle args = new Bundle();

        args.putString(TITLE_TAG, title);
        args.putString(INITIAL_TEXT_TAG, initialText);
        args.putString(HINT_TAG, hint);
        args.putString(NAME_TYPE_TAG, nameornickname);
        args.putInt(CALLBACK_ID_TAG, callbackId);

        profileInputDialogFragment.setArguments(args);
        return profileInputDialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View promptView = inflater.inflate(R.layout.text_dialog,null);
        builder.setView(promptView);

        final TextView textView = promptView.findViewById(R.id.my_profile_dialog_label);
        final EditText editText = promptView.findViewById(R.id.my_profile_dialog_input);
        final TextView hintView = promptView.findViewById(R.id.my_profile_dialog_hint);

        Bundle args = getArguments();
        String title = args.getString(TITLE_TAG);
        String hint = args.getString(HINT_TAG);
        String initialText = args.getString(INITIAL_TEXT_TAG);
        String nametype = args.getString(NAME_TYPE_TAG);
        final int callbackId = args.getInt(CALLBACK_ID_TAG);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        System.out.println(uid);
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/user_profile/"+uid);
        textView.setText(title);
        if (!TextUtils.isEmpty(hint)) {
            hintView.setText(hint);
        } else {
            hintView.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(initialText)) {
            editText.setText(initialText);
            editText.setSelection(0, initialText.length());
        }
        System.out.println(nametype);
        builder.setCancelable(true)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (getTargetFragment() instanceof Callback) {
                            if(nametype=="nickname") {
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("nickname", editText.getText().toString());
                                myRef.updateChildren(childUpdates);
                            } else {
                                Map<String, Object> childUpdates = new HashMap<>();
                                childUpdates.put("name", editText.getText().toString());
                                myRef.updateChildren(childUpdates);
                            }
                            ((Callback) getTargetFragment())
                                    .onSuccessfulInput(editText.getText().toString(), callbackId);
                        } else {
                            throw new ClassCastException("Target fragment doesn't implement ProfileInputDialogFragment.Callback");
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
        void onSuccessfulInput(String input, int callbackId);
    }

}
