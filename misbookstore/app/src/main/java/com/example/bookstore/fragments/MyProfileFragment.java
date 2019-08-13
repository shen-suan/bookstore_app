package com.example.bookstore.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.bookstore.MemberInformation.CheckboxDialog;
import com.example.bookstore.MemberInformation.DatepickerDialog;
import com.example.bookstore.MemberInformation.RadioDialog;
import com.example.bookstore.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment implements
        ProfileInputDialogFragment.Callback ,
        RadioDialog.Callback ,
        DatepickerDialog.Callback ,
        CheckboxDialog.Callback {
    private static final String DIALOG_TAG = "DIALOG";

    private TextView mi_nickname;
    private TextView mi_name;
    private TextView mi_gender;
    private TextView mi_birth;
    private TextView mi_book_type;


    public static MyProfileFragment newInstance() {
        return new MyProfileFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_profile_setting, container, false);

        mi_nickname = view.findViewById(R.id.mi_nickname);
        mi_name = view.findViewById(R.id.mi_name);
        mi_gender = view.findViewById(R.id.mi_gender);
        mi_birth = view.findViewById(R.id.mi_birth);
        mi_book_type = view.findViewById(R.id.mi_book_type);

        mi_nickname.setOnClickListener(
                createOnClickListener(
                        getString(R.string.input_nickname),
                        null,
                        mi_nickname,
                        false));
        mi_name.setOnClickListener(
                createOnClickListener(
                        getString(R.string.input_name),
                        null,
                        mi_name,
                        false));
        mi_gender.setOnClickListener(
                radioOnClickListener(
                        getString(R.string.input_gender),
                        null,
                        mi_gender));
        mi_birth.setOnClickListener(
                dateOnClickListener(
                        getString(R.string.input_birth),
                        null,
                        mi_birth));
        mi_book_type.setOnClickListener(
                checkboxOnClickListener(
                        getString(R.string.input_book_type),
                        null,
                        mi_book_type));


        return view;
    }

    // edit name ,nickname
    private View.OnClickListener createOnClickListener(final String dialogTitle,
                                                       final String hint,
                                                       final TextView textView,
                                                       final boolean isMultiline) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileInputDialogFragment inputDialog = ProfileInputDialogFragment.newInstance(dialogTitle,
                        textView.getText()
                                .toString(),
                        hint, isMultiline,
                        textView.getId());
                inputDialog.setTargetFragment(MyProfileFragment.this, 0);
                inputDialog.show(getFragmentManager(), DIALOG_TAG);
            }
        };
    }

    // choose gender
    private View.OnClickListener radioOnClickListener(final String dialogTitle,
                                                       final String hint,
                                                       final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioDialog inputDialog = RadioDialog.newInstance(dialogTitle,
                        textView.getText()
                                .toString(),
                        hint, textView.getId());
                inputDialog.setTargetFragment(MyProfileFragment.this, 0);
                inputDialog.show(getFragmentManager(), DIALOG_TAG);
            }
        };
    }

    // choose date
    private View.OnClickListener dateOnClickListener(final String dialogTitle,
                                                       final String hint,
                                                       final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatepickerDialog inputDialog = DatepickerDialog.newInstance(dialogTitle,
                        textView.getText()
                                .toString(),
                        hint, textView.getId());
                inputDialog.setTargetFragment(MyProfileFragment.this, 0);
                inputDialog.show(getFragmentManager(), DIALOG_TAG);
            }
        };
    }

    // choose checkbox
    private View.OnClickListener checkboxOnClickListener(final String dialogTitle,
                                                       final String hint,
                                                       final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckboxDialog inputDialog = CheckboxDialog.newInstance(dialogTitle,
                        textView.getText()
                                .toString(),
                        hint, textView.getId());
                inputDialog.setTargetFragment(MyProfileFragment.this, 0);
                inputDialog.show(getFragmentManager(), DIALOG_TAG);
            }
        };
    }


    @Override
    public void onSuccessfulInput(String input, int callbackId) {
        View rootView = getView();
        if (rootView == null) {
            return;
        }

        TextView textView = rootView.findViewById(callbackId);
        updateLabel(textView, input);
        updateMyProfileForLabel(textView);
    }
    //資料庫更新
    private void updateMyProfileForLabel(TextView textView) {

    }

    private void updateLabel(TextView textView, String text) {
        textView.setText(text);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessfulRadio(String input, int callbackId) {
        View rootView = getView();
        if (rootView == null) {
            return;
        }
        TextView textView = rootView.findViewById(callbackId);
        textView.setText(input);
        if (TextUtils.isEmpty(input)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessfulDate(String input, int callbackId) {
        View rootView = getView();
        if (rootView == null) {
            return;
        }
        TextView textView = rootView.findViewById(callbackId);
        textView.setText(input);
        if (TextUtils.isEmpty(input)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessfulCheckbox(String input, int callbackId) {
        View rootView = getView();
        if (rootView == null) {
            return;
        }
        TextView textView = rootView.findViewById(callbackId);
        textView.setText(input);
        if (TextUtils.isEmpty(input)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

}
