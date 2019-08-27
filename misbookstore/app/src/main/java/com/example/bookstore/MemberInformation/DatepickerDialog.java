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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

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

public class DatepickerDialog extends AppCompatDialogFragment{

    private static final String TITLE_TAG = "title";
    private static final String INITIAL_TEXT_TAG = "initial_text";
    private static final String HINT_TAG = "hint";
    private static final String CALLBACK_ID_TAG = "callback_id";
    private View promptView;

    public static DatepickerDialog newInstance(String title,
                                          String initialText,
                                          String hint,
                                          int callbackId) {
        DatepickerDialog datepickerDialog = new DatepickerDialog();
        Bundle args = new Bundle();

        args.putString(TITLE_TAG, title);
        args.putString(INITIAL_TEXT_TAG, initialText);
        args.putString(HINT_TAG, hint);
        args.putInt(CALLBACK_ID_TAG, callbackId);

        datepickerDialog.setArguments(args);
        return datepickerDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        promptView = inflater.inflate(R.layout.datepicker_dialog,null);
        builder.setView(promptView);

        final TextView textView = promptView.findViewById(R.id.date_label);
        final TextView hintView = promptView.findViewById(R.id.date_hint);
        final DatePicker edit_birth = promptView.findViewById(R.id.edit_birth);

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
                String birth_date = user.getbirthday();
                String[] tokens = birth_date.split("-");
                int year = Integer.valueOf(tokens[0]);
                int month = Integer.valueOf(tokens[1]);
                int day = Integer.valueOf(tokens[2]);
                if(month==1) {
                    month=13;
                    year=year-1;
                }
                edit_birth.init(year, month-1, day, null);
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
                            int month = edit_birth.getMonth(); //一月是0
                            month +=1;
                            String birth = edit_birth.getYear()+"-"+month+"-"+edit_birth.getDayOfMonth();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("birthday",birth);
                            myRef.updateChildren(childUpdates);
                            ((Callback) getTargetFragment())
                                    .onSuccessfulDate(birth, callbackId);
                        } else {
                            throw new ClassCastException("Target fragment doesn't implement DatepickerDialog.Callback");
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
        void onSuccessfulDate(String input, int callbackId);
    }
}

