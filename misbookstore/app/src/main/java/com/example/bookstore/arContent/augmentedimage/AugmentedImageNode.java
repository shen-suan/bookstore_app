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

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.TextView;

import com.example.bookstore.BookInformation.ListData;
import com.example.bookstore.R;
import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Node for rendering an augmented image. The image is framed by placing the virtual picture frame
 * at the corners of the augmented image trackable.
 */
@SuppressWarnings({"AndroidApiChecker"})
public class AugmentedImageNode extends AnchorNode {

  private static final String TAG = "AugmentedImageNode";
  private final Map<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();
  // The augmented image represented by this node.
  private AugmentedImage image;

  // Models of the 4 corners.  We use completable futures here to simplify
  // the error handling and asynchronous loading.  The loading is started with the
  // first construction of an instance, and then used when the image is set.
  private static CompletableFuture<ModelRenderable> ulCorner;
  private static CompletableFuture<ModelRenderable> urCorner;
  private static CompletableFuture<ModelRenderable> lrCorner;
  private static CompletableFuture<ModelRenderable> llCorner;
  private static CompletableFuture<ViewRenderable> view;
  private static CompletableFuture<ViewRenderable> view2;
  private ViewRenderable testViewRenderable;
  private Node cornerNode = new Node();

  @RequiresApi(api = Build.VERSION_CODES.N)
  public AugmentedImageNode(Context context) {
    // Upon construction, start loading the models for the corners of the frame.
    if (view == null) {
      view = ViewRenderable.builder()
              .setView(context, R.layout.ar_book_info)
              .build();
      view2 = ViewRenderable.builder()
              .setView(context, R.layout.ar_book_info) //新的XML
              .build();
    }
  }

  /**
   * Called when the AugmentedImage is detected and should be rendered. A Sceneform node tree is
   * created based on an Anchor created from the image. The corners are then positioned based on the
   * extents of the image. There is no need to worry about world coordinates since everything is
   * relative to the center of the image, which is the parent node of the corners.
   */
  @RequiresApi(api = Build.VERSION_CODES.N)
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  public int setImage(AugmentedImage image) {
    this.image = image;

    // Set the anchor based on the center of the image.
    setAnchor(image.createAnchor(image.getCenterPose()));

    // Make the 4 corner nodes.
    Vector3 localPosition = new Vector3();

    int imageindex = image.getIndex();


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

          view.thenAccept(view ->{
            for (DataSnapshot ds : dataSnapshot.getChildren()){
                ListData data = ds.getValue(ListData.class);
                if(data.getIndex() == imageindex){
                  TextView author = view.getView().findViewById(R.id.ar_author_info);
                  TextView name = view.getView().findViewById(R.id.ar_book_info);
                  TextView contain = view.getView().findViewById(R.id.ar_content_info);
                  author.setText(data.getAuthor());
                  name.setText(data.getTitle());
                  contain.setText(data.getOutline());
                  break;
                }
            }
          });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
          Log.w("onCancelled",databaseError.toException());
        }
      });



    localPosition.set(-0.2f * image.getExtentX(), 0.0f, 0.4f * image.getExtentZ());
    //cornerNode = new Node();
    cornerNode.setParent(this);
    cornerNode.setLocalPosition(localPosition);
    cornerNode.setLocalScale(new Vector3(image.getExtentZ() * 0.5f, image.getExtentZ() * 0.5f, image.getExtentX() * 0.5f));
    cornerNode.setLocalRotation(Quaternion.axisAngle(new Vector3(-1f, 0, 0), 90f));
    cornerNode.setRenderable(view.getNow(null));


    return imageindex;
  }

  public AugmentedImage getImage() {
    return image;
  }
}





