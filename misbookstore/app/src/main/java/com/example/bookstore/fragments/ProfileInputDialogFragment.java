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

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInputDialogFragment extends DialogFragment {
    private static final String TITLE_TAG = "title";
    private static final String INITIAL_TEXT_TAG = "initial_text";
    private static final String HINT_TAG = "hint";
    private static final String IS_MULTILINE_TAG = "is_multiline";
    private static final String CALLBACK_ID_TAG = "callback_id";


    public static ProfileInputDialogFragment newInstance(String title,
                                                         String initialText,
                                                         String hint,
                                                         boolean isMultiline,
                                                         int callbackId) {
        ProfileInputDialogFragment profileInputDialogFragment = new ProfileInputDialogFragment();
        Bundle args = new Bundle();

        args.putString(TITLE_TAG, title);
        args.putString(INITIAL_TEXT_TAG, initialText);
        args.putString(HINT_TAG, hint);
        args.putBoolean(IS_MULTILINE_TAG, isMultiline);
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
        Boolean isMultiline = args.getBoolean(IS_MULTILINE_TAG);
        String initialText = args.getString(INITIAL_TEXT_TAG);
        final int callbackId = args.getInt(CALLBACK_ID_TAG);

        textView.setText(title);
        if (!TextUtils.isEmpty(hint)) {
            hintView.setText(hint);
        } else {
            hintView.setVisibility(View.GONE);
        }

        if (!isMultiline) {
            editText.setMaxLines(1);
        }
        if (!TextUtils.isEmpty(initialText)) {
            editText.setText(initialText);
            editText.setSelection(0, initialText.length());
        }

        builder.setCancelable(true)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (getTargetFragment() instanceof Callback) {
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
