//package com.example.bookstore;
//
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.Button;
//
//import com.google.cloud.dialogflow.v2beta1.DetectIntentResponse;
////import com.google.api.gax.core.FixedCredentialsProvider;
////import com.google.auth.oauth2.GoogleCredentials;
////import com.google.auth.oauth2.ServiceAccountCredentials;
////import com.google.cloud.dialogflow.v2beta1.QueryInput;
////import com.google.cloud.dialogflow.v2beta1.SessionName;
////import com.google.cloud.dialogflow.v2beta1.SessionsClient;
////import com.google.cloud.dialogflow.v2beta1.SessionsSettings;
////import com.google.cloud.dialogflow.v2beta1.TextInput;
//
////import java.io.InputStream;
//import java.util.UUID;
//
//import ai.api.AIServiceContext;
//import ai.api.AIServiceContextBuilder;
//import ai.api.android.AIConfiguration;
//import ai.api.android.AIDataService;
//import ai.api.model.AIRequest;
//import ai.api.model.AIResponse;
////Listen import
//import ai.api.AIListener;
//import ai.api.model.AIError;
//import ai.api.model.Result;
//import ai.api.android.AIService;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//public class Voice_Assistant extends AppCompatActivity implements AIListener {
//    private Button sendBtn;
//    private RelativeLayout mic;
//    private CheckBox switch_btn;
//
//    //chatbox
//    private static final String TAG = Voice_Assistant.class.getSimpleName();
//    private static final int USER = 10001;
//    private static final int BOT = 10002;
//
//    private String uuid = UUID.randomUUID().toString();
//    private LinearLayout chatLayout;
//    private EditText queryEditText;
//
//    // Android client
//    private AIRequest aiRequest;
//    private AIDataService aiDataService;
//    private AIServiceContext customAIServiceContext;
//
//    // Java V2
////    private SessionsClient sessionsClient;
////    private SessionName session;
//    //Listen
//    private AIService aiService;
//    ImageButton listenButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_voice__assistant);
//
//        final ScrollView scrollview = findViewById(R.id.chatScrollView);
//        scrollview.post(() -> scrollview.fullScroll(ScrollView.FOCUS_DOWN));
//
//        chatLayout = findViewById(R.id.chatLayout);
//        listenButton = findViewById(R.id.listenButton);
//
//        sendBtn = findViewById(R.id.sendBtn);
//        sendBtn.setOnClickListener(this::sendMessage);
//
//        queryEditText = findViewById(R.id.queryEditText);
//        queryEditText.setOnKeyListener((view, keyCode, event) -> {
//            if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                switch (keyCode) {
//                    case KeyEvent.KEYCODE_DPAD_CENTER:
//                    case KeyEvent.KEYCODE_ENTER:
//                        sendMessage(sendBtn);
//                        return true;
//                    default:
//                        break;
//                }
//            }
//            return false;
//        });
//        mic = findViewById(R.id.mic);
//
//        //鍵盤與語音互換
//        switch_btn = findViewById(R.id.swith_btn);
//        switch_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(switch_btn.isChecked()){
//                    queryEditText.setVisibility(View.VISIBLE);
//                    sendBtn.setVisibility(View.VISIBLE);
//                    mic.setVisibility(View.GONE);
//                }else{
//                    queryEditText.setVisibility(View.GONE);
//                    sendBtn.setVisibility(View.GONE);
//                    mic.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });
//        //權限
//        int permission = ContextCompat.checkSelfPermission(this,
//                Manifest.permission.RECORD_AUDIO);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            makeRequest();
//        }
//
////         Android client
//        initChatbot();
//
//        // Java V2
////        initV2Chatbot();
//    }
//    protected void makeRequest() {
//        ActivityCompat.requestPermissions(this,
//                new String[]{Manifest.permission.RECORD_AUDIO},
//                101);
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 101: {
//
//                if (grantResults.length == 0
//                        || grantResults[0] !=
//                        PackageManager.PERMISSION_GRANTED) {
//                } else {
//                }
//                return;
//            }
//        }
//    }
//
//    public void listenButtonOnClick(final View view) {
//        aiService.startListening();
//    }
//
//
//    private void initChatbot() {
//        final AIConfiguration config = new AIConfiguration("4d12129ca3c44657b8029cbe88f190d2",
//                AIConfiguration.SupportedLanguages.ChineseTaiwan,
//                AIConfiguration.RecognitionEngine.System);
//        aiService = AIService.getService(this, config);
//        aiService.setListener(this);
//        aiDataService = new AIDataService(Voice_Assistant.this, config);
//        customAIServiceContext = AIServiceContextBuilder.buildFromSessionId(uuid);// helps to create new session whenever app restarts
//        aiRequest = new AIRequest();
//    }
//
////    private void initV2Chatbot() {
////        try {
////            InputStream stream = getResources().openRawResource(R.raw.test_agent_credentials);
////            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
////            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();
////
////            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
////            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
////            sessionsClient = SessionsClient.create(sessionsSettings);
////            session = SessionName.of(projectId, uuid);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//
//    private void sendMessage(View view) {
//        String msg = queryEditText.getText().toString();
//        if (msg.trim().isEmpty()) {
//            Toast.makeText(Voice_Assistant.this, "Please enter your query!", Toast.LENGTH_LONG).show();
//        } else {
//            showTextView(msg, USER);
//            queryEditText.setText("");
////             Android client
//            aiRequest.setQuery(msg);
//            RequestTask requestTask = new RequestTask(Voice_Assistant.this, aiDataService, customAIServiceContext);
//            requestTask.execute(aiRequest);
//
//            // Java V2
////            QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(msg).setLanguageCode("en-US")).build();
////            new RequestJavaV2Task(MainActivity.this, session, sessionsClient, queryInput).execute();
//        }
//    }
//
//    public void callback(AIResponse aiResponse) {
//        final Result result = aiResponse.getResult();
//        if (result != null) {
//            // process aiResponse here
//            String botReply = aiResponse.getResult().getFulfillment().getSpeech();
//            Log.d(TAG, "Bot Reply: " + botReply);
//            showTextView(botReply, BOT);
//        } else {
//            Log.d(TAG, "Bot Reply: Null");
//            showTextView("There was some communication issue. Please Try again!", BOT);
//        }
//    }
//
//    public void callbackV2(DetectIntentResponse response) {
//        if (response != null) {
//            // process aiResponse here
//            String botReply = response.getQueryResult().getFulfillmentText();
//            Log.d(TAG, "V2 Bot Reply: " + botReply);
//            showTextView(botReply, BOT);
//        } else {
//            Log.d(TAG, "Bot Reply: Null");
//            showTextView("There was some communication issue. Please Try again!", BOT);
//        }
//    }
//
//    private void showTextView(String message, int type) {
//        FrameLayout layout;
//        switch (type) {
//            case USER:
//                layout = getUserLayout();
//                break;
//            case BOT:
//                layout = getBotLayout();
//                break;
//            default:
//                layout = getBotLayout();
//                break;
//        }
//        layout.setFocusableInTouchMode(true);
//        chatLayout.addView(layout); // move focus to text view to automatically make it scroll up if softfocus
//        TextView tv = layout.findViewById(R.id.chatMsg);
//        tv.setText(message);
//        layout.requestFocus();
//        queryEditText.requestFocus(); // change focus back to edit text to continue typing
//    }
//
//    FrameLayout getUserLayout() {
//        LayoutInflater inflater = LayoutInflater.from(Voice_Assistant.this);
//        return (FrameLayout) inflater.inflate(R.layout.user_msg_layout, null);
//    }
//
//    FrameLayout getBotLayout() {
//        LayoutInflater inflater = LayoutInflater.from(Voice_Assistant.this);
//        return (FrameLayout) inflater.inflate(R.layout.bot_msg_layout, null);
//    }
//
//    @Override
//    public void onResult(final AIResponse response) {
//        //listen result
//        Log.d(TAG, response.toString());
//        final Result result = response.getResult();
//        showTextView(result.getResolvedQuery(), USER);
//        //response
//        final String botReply = result.getFulfillment().getSpeech();
//        Log.d(TAG, "Bot Reply: " + botReply);
//        showTextView(botReply, BOT);
//    }
//
//    @Override
//    public void onError(final AIError error) {
//
//    }
//
//    @Override
//    public void onAudioLevel(final float level) {
//
//    }
//
//    @Override
//    public void onListeningStarted() {
//
//    }
//
//    @Override
//    public void onListeningCanceled() {
//
//    }
//
//    @Override
//    public void onListeningFinished() {
//
//    }
//}