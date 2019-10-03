/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bookstore.arContent.augmentedimage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bookstore.BookInformation.ListData;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.samples.common.helpers.SnackbarHelper;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Extend the ArFragment to customize the ARCore session configuration to include Augmented Images.
 */
public class AugmentedImageFragment extends ArFragment {
  private static final String TAG = "AugmentedImageFragment";

  // This is the name of the image in the sample database.  A copy of the image is in the assets
  // directory.  Opening this image on your computer is a good quick way to test the augmented image
  // matching.
  private static final String DEFAULT_IMAGE_NAME = "blue2.jpg";
  private static final String IMAGE_NAME_TEST1 = "blue2.jpg";
  private static final String IMAGE_NAME_TEST2 = "blue2.jpg";
  private ListData data;

  // This is a pre-created database containing the sample image.
  private static final String SAMPLE_IMAGE_DATABASE = "sample_database.imgdb";

  // Augmented image configuration and rendering.
  // Load a single image (true) or a pre-generated image database (false).
  private static final boolean USE_SINGLE_IMAGE = true;//改TRUE才跑的近if

  // Do a runtime check for the OpenGL level available at runtime to avoid Sceneform crashing the
  // application.
  private static final double MIN_OPENGL_VERSION = 3.0;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    // Check for Sceneform being supported on this device.  This check will be integrated into
    // Sceneform eventually.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
      Log.e(TAG, "Sceneform requires Android N or later");
      SnackbarHelper.getInstance()
          .showError(getActivity(), "Sceneform requires Android N or later");
    }

    String openGlVersionString =
        ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
            .getDeviceConfigurationInfo()
            .getGlEsVersion();
    if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
      Log.e(TAG, "Sceneform requires OpenGL ES 3.0 or later");
      SnackbarHelper.getInstance()
          .showError(getActivity(), "Sceneform requires OpenGL ES 3.0 or later");
    }
    //強制網路連線，UI體驗可能會慢 搜尋android.os.NetworkOnMainThreadException改進
    if (android.os.Build.VERSION.SDK_INT > 9) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    // Turn off the plane discovery since we're only looking for images
    getPlaneDiscoveryController().hide();
    getPlaneDiscoveryController().setInstructionView(null);
    getArSceneView().getPlaneRenderer().setEnabled(false);

    return view;
  }

  @Override
  protected Config getSessionConfiguration(Session session) {
    Config config = super.getSessionConfiguration(session);
      try {
          if (!setupAugmentedImageDatabase(config, session)) {
            SnackbarHelper.getInstance()
                .showError(getActivity(), "Could not setup augmented image database");
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      return config;
  }


  private boolean setupAugmentedImageDatabase(Config config, Session session) throws IOException {
    System.out.println(config + "1===========================================================================");
    AugmentedImageDatabase augmentedImageDatabase;
//    augmentedImageDatabase = new AugmentedImageDatabase(session);//創建新的資料庫
//    Bitmap augmentedImageBitmap = null;
    ImageView imgView = null;

    AssetManager assetManager = getContext() != null ? getContext().getAssets() : null;
    if (assetManager == null) {
      Log.e(TAG, "Context is null, cannot intitialize image database.");
      //return false;
    }

    // There are two ways to configure an AugmentedImageDatabase:
    // 1. Add Bitmap to DB directly
    // 2. Load a pre-built AugmentedImageDatabase
    // Option 2) has
    // * shorter setup time
    // * doesn't require images to be packaged in apk.


    //用於存放Firebase取回的資料，限定型態為ListData。
    ArrayList<ListData> listData = new ArrayList<>();
    //USE_SINGLE_IMAGE設TRUE才能加入圖片
    if (USE_SINGLE_IMAGE) {
      augmentedImageDatabase = new AugmentedImageDatabase(session);//創建新的資料庫
      Bitmap augmentedImageBitmap;
//      for (int i = 0; i <= listData.size(); i ++){
//        ListData data = (ListData) listData.get(i);
//        String url = data.getUrl();
//        URL imgUrl = new URL(url);
//        //用http連線
//        HttpURLConnection httpURLConnection = (HttpURLConnection) imgUrl.openConnection();
//        httpURLConnection.connect();
//        //轉成inputstream，跟下面function相同
//        InputStream inputStream = httpURLConnection.getInputStream();
//        int length = (int) httpURLConnection.getContentLength();
//        int tmpLength = 512;
//        int readLen = 0,desPos = 0;
//        byte[] img = new byte[length];
//        byte[] tmp = new byte[tmpLength];
//        if (length != -1) {
//          while ((readLen = inputStream.read(tmp)) > 0) {
//            System.arraycopy(tmp, 0, img, desPos, readLen);
//            desPos += readLen;
//          }
//          augmentedImageBitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
//          if(desPos != length){
//            throw new IOException("Only read" + desPos +"bytes");
//          }
//        }
//        if (augmentedImageBitmap == null) {
//          return false;
//        }
//        augmentedImageDatabase.addImage("test", augmentedImageBitmap);
//      }



//      readData(new MyCallback() {
//        @Override
//        public void onCallback(ArrayList value) throws IOException {
//              for (int i = 0; i < value.size(); i++){
//                data = (ListData) value.get(i);
//                String url = data.getUrl();
//                String name = data.getIsbn();
//                System.out.println(url+"===========================================================================");
//                System.out.println(name+"===========================================================================");
//                InputStream is = new java.net.URL(url).openStream();
//                try {
//                  is = new URL(url).openStream();
//                  System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
//                } catch (IOException e) {
//                  e.printStackTrace();
//                }
//                Bitmap augmentedImageBitmap = BitmapFactory.decodeStream(is);
//                //augmentedImageDatabase.addImage(name + ".jpg", augmentedImageBitmap);
//              }
//        }
//      });
      String url, name;
      //第一本書
      url = "https://firebasestorage.googleapis.com/v0/b/unmanned-bookst.appspot.com/o/9780593072868.jpg?alt=media&token=02e0a54a-9b5d-49e3-b00e-aee640de06f7";
      InputStream is = new java.net.URL(url).openStream();
      name  = "9780593072868";
      augmentedImageBitmap = BitmapFactory.decodeStream(is);
      augmentedImageDatabase.addImage("9780593072868.jpg", augmentedImageBitmap);

      //第二本書
      url = "https://firebasestorage.googleapis.com/v0/b/unmanned-bookst.appspot.com/o/9780618243757.jpg?alt=media&token=a703f5f2-34bb-4dd1-b6e3-f4cf35899819";
      is = new java.net.URL(url).openStream();
      name  = "9780618243757";
      augmentedImageBitmap = BitmapFactory.decodeStream(is);
      augmentedImageDatabase.addImage(name + ".jpg", augmentedImageBitmap);

      //第三本書
      System.out.println("=================================================================================================================");
      url = "https://firebasestorage.googleapis.com/v0/b/unmanned-bookst.appspot.com/o/9789571199474.jpg?alt=media&token=7adf0576-8bd6-4308-93e2-a4a5874cc46b";
      is = new java.net.URL(url).openStream();
      name  = "9789571199474";
      augmentedImageBitmap = BitmapFactory.decodeStream(is);
      augmentedImageDatabase.addImage(name + ".jpg", augmentedImageBitmap);

      //第四本書
      url = "https://firebasestorage.googleapis.com/v0/b/unmanned-bookst.appspot.com/o/9789571371511.jpg?alt=media&token=6d476390-fbd9-4cc0-a189-f23019ba2d44";
      is = new java.net.URL(url).openStream();
      name  = "9789571199474";
      augmentedImageBitmap = BitmapFactory.decodeStream(is);
      augmentedImageDatabase.addImage(name + ".jpg", augmentedImageBitmap);

      //第五本書
      url = "https://firebasestorage.googleapis.com/v0/b/unmanned-bookst.appspot.com/o/9789573273349.jpg?alt=media&token=b1356683-17df-4780-8e0f-5d2148c9fb47";
      is = new java.net.URL(url).openStream();
      name  = "9789573273349";
      augmentedImageBitmap = BitmapFactory.decodeStream(is);
      augmentedImageDatabase.addImage(name + ".jpg", augmentedImageBitmap);
      config.setAugmentedImageDatabase(augmentedImageDatabase);
    } else {
      // This is an alternative way to initialize an AugmentedImageDatabase instance,
      // load a pre-existing augmented image database.
      try (InputStream is = getContext().getAssets().open(SAMPLE_IMAGE_DATABASE)) {
        augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, is);
      } catch (IOException e) {
        Log.e(TAG, "IO exception loading augmented image database.", e);
        //return false;
      }
    }
    System.out.println(config + "2===========================================================================");
    return true;
  }


  private Bitmap loadAugmentedImageBitmap(AssetManager assetManager, String image) {
    try (InputStream is = assetManager.open(image)) {
      return BitmapFactory.decodeStream(is);
    } catch (IOException e) {
      Log.e(TAG, "IO exception loading augmented image bitmap.", e);
    }
    return null;
  }

  public void readData(MyCallback myCallback) {
    ArrayList<ListData> listData = new ArrayList<>();
    listData.clear();
    //連資料庫
    DatabaseReference Book_list = FirebaseDatabase.getInstance().getReference("bookList");
    Book_list.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren() ){
          ListData bookList = ds.getValue(ListData.class);
          listData.add(new ListData(bookList.getTitle(),bookList.getPrice(),bookList.getIsbn(), bookList.getUrl(),
                  bookList.getAuthor(), bookList.getPublisher(), bookList.getPublishDate(), bookList.getVersion(), bookList.getOutline(),
                  bookList.getClassification(), bookList.getIndex()));
        }
        try {
          myCallback.onCallback(listData);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.w("onCancelled",databaseError.toException());
      }
    });
  }
  public interface MyCallback{
    void onCallback(ArrayList value) throws IOException;
  }
}
