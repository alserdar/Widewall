package alserdar.widewall.fragment_home_stuff;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.animate.RotateImage;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UploadThePostPleaseForCountry extends AppCompatActivity {

    LinearLayout listViewLay;
    LinearLayout messageLay, audioStuffLay, theSpace, startRecordLay, replayRecordLay, stopRecordLay;
    EditText typeMessage;
    Button thePost, recordAudio, replayRecord, stopRecord;
    String theUID , currentCountry , postCountry , whoSentYou;
    TextView myNickName , handle;
    ProgressBar progressAudio;
    public static final int RequestPermissionCode = 73;
    private String audioStatus = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_the_post_for_country);


        myNickName = findViewById(R.id.userName);
        handle = findViewById(R.id.handle);
        listViewLay = findViewById(R.id.listViewLayout);
        audioStuffLay = findViewById(R.id.audioStuffLay);
        theSpace = findViewById(R.id.spaceOfModes);
        startRecordLay = findViewById(R.id.startRecordLay);
        replayRecordLay = findViewById(R.id.replayRecordLay);
        stopRecordLay = findViewById(R.id.stopRecordLay);
        thePost = findViewById(R.id.postButton);
        recordAudio = findViewById(R.id.recordAudio);
        replayRecord = findViewById(R.id.replayAudio);
        stopRecord = findViewById(R.id.stopAudio);
        progressAudio = findViewById(R.id.progressTheAudio);
        messageLay = findViewById(R.id.typeMessageLay);
        typeMessage = findViewById(R.id.typeMessage);


        whoSentYou = this.getIntent().getExtras().getString("whoSentYou");

        if (checkAudioPermission()) {

        } else {
            requestPermission();
        }


        theUID = DetectUserInfo.theUID(getBaseContext());
        DetectUserInfoFromFirebase.userHandle(getBaseContext() , myNickName , handle);
        currentCountry = DetectUserInfo.country(getBaseContext());
        postCountry = DetectUserInfo.country(getBaseContext());

        ImageView pressBack = findViewById(R.id.press_back);
        ImageView profilePic = findViewById(R.id.profilePicInToolBar);
        final ImageView topList = findViewById(R.id.getTheTopOfList);


        LoadPictures.setCountryFlagByNameCountry(topList , currentCountry);
        LoadPictures.loadPicsForHome(UploadThePostPleaseForCountry.this , theUID , profilePic);

        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(UploadThePostPleaseForCountry.this , RoomCountry.class);
                startActivity(i);
                finish();
            }
        });


        thePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RotateImage.startRotateMyButton(thePost);
                if (audioStatus.trim().equals("voiceNotes")) {
                    CoreThePost.stopToUploadImmediately();
                    CoreThePost.uploadTheRecord(UploadThePostPleaseForCountry.this , theUID);
                } else {

                }

                CoreThePost.thePost(theUID , whoSentYou , postCountry,
                        currentCountry , typeMessage.getText().toString() , myNickName.getText().toString() , handle.getText().toString() ,
                        audioStatus , "picPath");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(UploadThePostPleaseForCountry.this , RoomCountry.class);
                        startActivity(i);
                        finish();
                    }
                }, 3000);
            }
        });

        recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAudioPermission()) {

                    audioStatus = "voiceNotes";
                    CoreThePost.MediaRecorderReady();

                    new OurToast().myToastPic(getBaseContext(), "" , R.mipmap.microphone);
                } else {
                    requestPermission();
                }

                startRecordLay.setVisibility(View.GONE);
                stopRecordLay.setVisibility(View.VISIBLE);
                replayRecordLay.setVisibility(View.GONE);

                CoreThePost.progressTheRecord(progressAudio);
            }
        });


        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopRecordLay.setVisibility(View.GONE);
                startRecordLay.setVisibility(View.GONE);
                replayRecordLay.setVisibility(View.VISIBLE);

                if (CoreThePost.isRecordStop()) {
                    audioStatus = "voiceNotes";
                } else {

                }
            }
        });


        replayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replayRecordLay.setVisibility(View.GONE);
                stopRecordLay.setVisibility(View.GONE);
                startRecordLay.setVisibility(View.VISIBLE);

                new OurToast().myToast(getBaseContext(),  getString(R.string.reset));
                audioStatus = "Reset";
                CoreThePost.resetTheRecord(progressAudio);
            }
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (typeMessage.getText().toString().trim().equals("")) {
                    thePost.setVisibility(View.GONE);
                } else {
                    thePost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        typeMessage.addTextChangedListener(watcher);
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(UploadThePostPleaseForCountry.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(UploadThePostPleaseForCountry.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                    }
                }
                break;
        }
    }

    public boolean checkAudioPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UploadThePostPleaseForCountry.this , RoomCountry.class);
        startActivity(i);
        finish();
    }
}
