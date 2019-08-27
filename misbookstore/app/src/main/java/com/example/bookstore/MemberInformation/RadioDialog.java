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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.HashMap;
import java.util.Map;

public class RadioDialog extends AppCompatDialogFragment {

    private static final String TITLE_TAG = "title";
    private static final String INITIAL_TEXT_TAG = "initial_text";
    private static final String HINT_TAG = "hint";
    private static final String CALLBACK_ID_TAG = "callback_id";
    private RadioGroup edit_radio_gender;
    private RadioButton radioButton;
    private View promptView;

    public static RadioDialog newInstance(String title,
                                          String initialText,
                                          String hint,
                                          int callbackId) {
        RadioDialog radioDialog = new RadioDialog();
        Bundle args = new Bundle();

        args.putString(TITLE_TAG, title);
        args.putString(INITIAL_TEXT_TAG, initialText);
        args.putString(HINT_TAG, hint);
        args.putInt(CALLBACK_ID_TAG, callbackId);

        radioDialog.setArguments(args);
        return radioDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        promptView = inflater.inflate(R.layout.radio_dialog,null);
        builder.setView(promptView);

        final TextView textView = promptView.findViewById(R.id.radio_label);
        final TextView hintView = promptView.findViewById(R.id.radio_hint);
        final RadioButton radio_male = promptView.findViewById(R.id.edit_radio_male);
        final RadioButton radio_female = promptView.findViewById(R.id.edit_radio_female);

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
                User user = dataSnapshot.getValue(User.class);
                String gender_check = user.getUserGender();
                if(gender_check.equals("男")){
                    radio_male.setChecked(true);
                    radio_female.setChecked(false);
                }
                if(gender_check.equals("女")){
                    radio_male.setChecked(false);
                    radio_female.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), " Database Error", Toast.LENGTH_SHORT).show();
            }
        });

        textView.setText(title);
        if (!TextUtils.isEmpty(hint)) {
            hintView.setText(hint);
        } else {
            hintView.setVisibility(View.GONE);
        }


        builder.setCancelable(true)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (getTargetFragment() instanceof Callback) {
                            edit_radio_gender = promptView.findViewById(R.id.edit_radio_gender);
                            radioButton = promptView.findViewById(edit_radio_gender.getCheckedRadioButtonId());
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("userGender",radioButton.getText().toString());
                            myRef.updateChildren(childUpdates);
                            ((Callback) getTargetFragment())
                                    .onSuccessfulRadio(radioButton.getText().toString(), callbackId);
                        } else {
                            throw new ClassCastException("Target fragment doesn't implement RadioDialog.Callback");
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
        void onSuccessfulRadio(String input, int callbackId);
    }
}
