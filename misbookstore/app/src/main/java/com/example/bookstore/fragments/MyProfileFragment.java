package com.example.bookstore.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bookstore.MemberInformation.CheckboxDialog;
import com.example.bookstore.MemberInformation.CircleImageView;
import com.example.bookstore.MemberInformation.DatepickerDialog;
import com.example.bookstore.MemberInformation.RadioDialog;
import com.example.bookstore.R;
import com.example.bookstore.ResetPassword;
import com.example.bookstore.User;
import com.example.bookstore.utils.FileCompressor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


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
    private TextView mi_account;
    private TextView mi_edit_password;

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;
    FileCompressor mCompressor;
    @BindView(R.id.mi_pic)
    CircleImageView mi_pic;
    private StorageReference mStorageRef;
    private FirebaseUser user;
    private String uid;

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
        ButterKnife.bind(this,view);
        mCompressor = new FileCompressor(view.getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mi_pic = view.findViewById(R.id.mi_pic);

        mi_nickname = view.findViewById(R.id.mi_nickname);
        mi_name = view.findViewById(R.id.mi_name);
        mi_gender = view.findViewById(R.id.mi_gender);
        mi_birth = view.findViewById(R.id.mi_birth);
        mi_book_type = view.findViewById(R.id.mi_book_type);
        mi_account = view.findViewById(R.id.profile_set_account);
        mi_edit_password = view.findViewById(R.id.mi_edit_password);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        //連接資料庫
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://unmanned-bookst.firebaseio.com/user_profile/"+uid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
//                Book_types bookType = dataSnapshot.child("book_type").getValue(Book_types.class);
                mi_account.setText(user.getAccount());
                mi_nickname.setText(user.getNickname());
                mi_name.setText(user.getName());
                mi_gender.setText(user.getUserGender());
                mi_birth.setText(user.getbirthday());
                mi_book_type.setText(user.getBooks());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(), " Database Error", Toast.LENGTH_SHORT).show();
            }
        });
        //換頭貼
        Glide.with(getActivity())
                .load(user.getPhotoUrl())
                .apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.head))
                .into(mi_pic);
        System.out.println(user);
        System.out.println(user.getPhotoUrl());


        mi_nickname.setOnClickListener(
                createOnClickListener(
                        getString(R.string.input_nickname),
                        null,
                        mi_nickname,
                        "nickname"));
        mi_name.setOnClickListener(
                createOnClickListener(
                        getString(R.string.input_name),
                        null,
                        mi_name,
                        "username"));
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
                        getString(R.string.input_book_type_hint),
                        mi_book_type));

        mi_edit_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPassword.class);
                startActivity(intent);
            }
        });

        return view;
    }

    // edit name ,nickname
    private View.OnClickListener createOnClickListener(final String dialogTitle,
                                                       final String hint,
                                                       final TextView textView,
                                                       final String nameornickname) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileInputDialogFragment inputDialog = ProfileInputDialogFragment.newInstance(dialogTitle,
                        textView.getText()
                                .toString(),
                        hint, nameornickname,
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
        Toast.makeText(getActivity(), " 修改成功", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getActivity(), " 修改性別成功", Toast.LENGTH_SHORT).show();
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
        String format = "您設定的日期為:"+ input;
        Toast.makeText(getActivity(), format, Toast.LENGTH_SHORT).show();
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

    //edit self picture
    /**
     * Alert dialog for capture or select from galley
     */
    @OnClick(R.id.mi_pic)
    public void onViewClicked() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Choose from Library")) {
                requestStoragePermission(false);
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * Select image from gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_PHOTO) {
                Uri selectedImage = data.getData();
                String imgPath = getPath(getActivity(), selectedImage);
                Toast.makeText(getActivity(), imgPath, Toast.LENGTH_SHORT).show();
                try {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));
                    System.out.println(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Glide.with(getActivity()).load(mPhotoFile)
                        .apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.head))
                        .into(mi_pic);
                //更換頭貼時，先刪除Storage內原本的照片
                if(user.getPhotoUrl() != null){
                    //delete previous image from storage
                    StorageReference deleRef = FirebaseStorage.getInstance().getReference().child(uid);
                    deleRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "刪除成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                uploadImg(imgPath);
            }
        }
    }

    /**
     * Requesting multiple permissions (storage and camera) at once
     * This uses multiple permission model from dexter
     * On permanent denial opens settings dialog
     */
    private void requestStoragePermission(final boolean isCamera) {
        Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            dispatchGalleryIntent();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(error -> Toast.makeText(getActivity().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //upload image
    private void uploadImg(String path){
        Uri file = Uri.fromFile(new File(path));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();
        StorageReference riversRef = mStorageRef.child(uid); //以UID作為照片檔名
        UploadTask uploadTask = riversRef.putFile(file, metadata);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), "上傳失敗", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "上傳成功", Toast.LENGTH_SHORT).show();

                //取得圖片下載網址 存進database中
                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String url = uri.toString();
                        Log.e("TAG:", "the url is: " + url);
                        user = FirebaseAuth.getInstance().getCurrentUser();

                        //更新user profile
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(Uri.parse(url))
                                .build();
                        user.updateProfile(profileUpdates);
                    }
                });
            }
        });
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

}
