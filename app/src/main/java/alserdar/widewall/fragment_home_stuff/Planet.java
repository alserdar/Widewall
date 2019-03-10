package alserdar.widewall.fragment_home_stuff;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.core.CoreTheNotificationsList;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.core.CoreThePostFunctions;
import alserdar.widewall.core.CoreTheVolcano;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPicFullScreenInRooms;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.PostModel;
import alserdar.widewall.post_stuff.ThePost;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Planet extends AppCompatActivity {

    LinearLayout listViewLay;
    ListView listViewPlanet;
    FloatingActionButton postMode;
    LinearLayout messageLay, audioStuffLay, theSpace, startRecordLay, replayRecordLay, stopRecordLay;
    EditText typeMessage;
    Button thePost, recordAudio, replayRecord, stopRecord;
    String theUID , currentCountry , postCountry ;
    TextView myNickName , handle;
    Button addPics;
    ProgressBar progressAudio;
    public static final int RequestPermissionCode = 73;
    private String audioStatus = "";
    private LinearLayout showThePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);


        theUID = DetectUserInfo.theUID(getBaseContext());
        currentCountry = DetectUserInfo.country(getBaseContext());
        postCountry = DetectUserInfo.country(getBaseContext());

        myNickName = findViewById(R.id.userName);
        handle = findViewById(R.id.handle);

        listViewPlanet = findViewById(R.id.listChatPlanet);
        postMode = findViewById(R.id.postMode);
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
        ImageView pressBack = findViewById(R.id.press_back);
        ImageView profilePic = findViewById(R.id.profilePicInToolBar);
        final ImageView topList = findViewById(R.id.getTheTopOfList);



        DetectUserInfoFromFirebase.userHandle(getBaseContext() , myNickName , handle);
        LoadPictures.loadPicsForHome(Planet.this , theUID , profilePic);

        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 Intent i = new Intent(Planet.this , Home.class);
                startActivity(i);
                finish();
                 */

                Planet.super.onBackPressed();

            }
        });

        postMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Planet.this , UploadThePostPleaseForPlanet.class);
                i.putExtra("whoSentYou" , "ThePlanet");
                startActivity(i);
                finish();

                /*
                if (messageLay.getVisibility() == View.GONE) {
                    messageLay.setVisibility(View.VISIBLE);
                    listViewLay.setVisibility(View.GONE);
                    theSpace.setVisibility(View.VISIBLE);
                    audioStuffLay.setVisibility(View.VISIBLE);
                    postMode.setImageResource(R.mipmap.type_mode);
                } else {
                    messageLay.setVisibility(View.GONE);
                    theSpace.setVisibility(View.GONE);
                    audioStuffLay.setVisibility(View.GONE);
                    listViewLay.setVisibility(View.VISIBLE);
                    postMode.setImageResource(R.mipmap.keyboard);
                }
                 */

            }
        });

        topList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listViewPlanet.smoothScrollToPosition(0);
                /*
                topList.setImageResource(R.mipmap.male);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        topList.setImageResource(R.mipmap.planet);
                    }
                }, 5000);
                 */
            }
        });

        thePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (audioStatus.trim().equals("voiceNotes")) {
                    CoreThePost.uploadTheRecord(Planet.this , theUID);
                } else {

                }




                CoreThePost.thePost(theUID , "ThePlanet" , postCountry,
                        currentCountry , typeMessage.getText().toString() , myNickName.getText().toString() , handle.getText().toString() ,
                        audioStatus , "picPath");

                typeMessage.setText("");
                messageLay.setVisibility(View.GONE);
                audioStuffLay.setVisibility(View.GONE);
                theSpace.setVisibility(View.GONE);
                listViewLay.setVisibility(View.VISIBLE);
                onResume();

            }
        });

        recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAudioPermission()) {

                    audioStatus = "voiceNotes";
                    CoreThePost.MediaRecorderReady();

                    new OurToast().myToast(getBaseContext(),  getString(R.string.recording));
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

        displayThePlanetPeoplePosts();

    }

    private void displayThePlanetPeoplePosts() {


        FirebaseListAdapter<PostModel> adapter =
                new FirebaseListAdapter<PostModel>(Planet.this, PostModel.class, R.layout.list_of_posts,
                        FirebaseDatabase.getInstance().getReference("ThePlanet")) {

                    @Override
                    public PostModel getItem(int position) {
                        return super.getItem(super.getCount() - position - 1);

                    }

                    @Override
                    protected void populateView(final View v, final PostModel model, final int position) {

                        LinearLayout typePost = v.findViewById(R.id.textPost);
                        LinearLayout postLay = v.findViewById(R.id.postLay);
                        LinearLayout audioPost = v.findViewById(R.id.audioPost);
                        showThePicture = v.findViewById(R.id.showThePicture);
                        TextView messageText = v.findViewById(R.id.textMessage);
                        TextView messageUser = v.findViewById(R.id.userMessage);
                        TextView messageHandle = v.findViewById(R.id.handleMessage);
                        TextView timeMessage = v.findViewById(R.id.timeMessage);
                        ImageView flag = v.findViewById(R.id.flagMessage);
                        final TextView countListeners = v.findViewById(R.id.count_listeners);
                        final TextView countReplies = v.findViewById(R.id.countReplies);
                        final TextView countAdss = v.findViewById(R.id.countAdss);
                        final TextView countLikes = v.findViewById(R.id.countLikes);
                        final TextView countFavs = v.findViewById(R.id.countFavs);

                        final Button playMessage = v.findViewById(R.id.play_message_audio);
                        final Button pauseMessage = v.findViewById(R.id.puase_message_audio);
                        final SeekBar seekBarForPlay = v.findViewById(R.id.seek_bar_message_audio);
                        ImageView ppForChat = v.findViewById(R.id.userProfilePicture);
                        final ImageView reply = v.findViewById(R.id.replyGlobal);
                        final ImageView repost = v.findViewById(R.id.addGlobal);
                        final ImageView unrepost = v.findViewById(R.id.unAddGlobal);
                        final ImageView like = v.findViewById(R.id.likeGlobal);
                        final ImageView fave = v.findViewById(R.id.favGlobal);
                        final ImageView unLike = v.findViewById(R.id.unLikeGlobal);
                        final ImageView unFave = v.findViewById(R.id.unFavGlobal);

                        final ImageView thePicture = v.findViewById(R.id.thePicture);


                        messageUser.setText(model.getThePosterNickname());
                        messageHandle.setText(model.getThePosterHandle());
                        messageText.setText(model.getThePosterMessage());
                        timeMessage.setText(model.getThePostTime());
                        LoadPictures.loadPicsForHome(getBaseContext(), model.getThePosterUID(), ppForChat);
                        LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());
                        LoadPictures.loadPicturesInMainChat(Planet.this , model.getWhichRoom() ,
                                model.getThePosterUID() , thePicture , model.getPicPath());


                        thePicture.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(getBaseContext() , LoadPicFullScreenInRooms.class);
                                i.putExtra("whichRoom" , model.getWhichRoom());
                                i.putExtra("posterUID" , model.getThePosterUID());
                                i.putExtra("picPath" , model.getPicPath());
                                startActivity(i);
                            }
                        });

                        messageHandle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DetectUserInfoFromFirebase.clickTheUser(Planet.this , theUID , model.getThePosterUID());

                            }
                        });


                        ppForChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DetectUserInfoFromFirebase.clickTheUser(Planet.this , theUID , model.getThePosterUID());

                            }
                        });


                        messageUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DetectUserInfoFromFirebase.clickTheUser(Planet.this , theUID , model.getThePosterUID());
                            }
                        });

                        if (model.getAudioStatus().trim().equals("voiceNotes")) {
                            audioPost.setVisibility(View.VISIBLE);
                        } else {
                            audioPost.setVisibility(View.GONE);
                        }


                        repost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                CoreThePostFunctions.repostMethod(theUID , model.getThePosterUID() , model.getPostCountry(),
                                        model.getTheMainPlanetKeyPost(), model.getTheKeyPost() , model.getCountKeyPost(),
                                        repost , unrepost , model.getThePosterMessage() , model.getThePosterNickname() ,
                                        model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus());


                                CoreTheNotificationsList.NotificationForReposts(model.getThePosterUID() , model.getThePosterNickname() ,
                                        model.getThePosterHandle() , model.getThePosterMessage() , model.getTheKeyPost() , "planet" ,
                                        theUID  , myNickName.getText().toString() , handle.getText().toString() ,
                                        model.getAudioStatus() , model.getPicPath() , model.getAudioPath());


                                CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                        model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                        model.getAudioPath() , model.getTheMainPlanetKeyPost());

                            }
                        });

                        unrepost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CoreThePostFunctions.unRepostMethod(theUID, model.getThePosterUID() ,model.getTheMainPlanetKeyPost()   , model.getTheKeyPost() , model.getCountKeyPost() , unrepost , repost);
                            }
                        });


                        like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreThePostFunctions.likeMethod(theUID , model.getThePosterUID() ,
                                        model.getWhichRoom(), model.getPostCountry() ,
                                        model.getTheMainPlanetKeyPost(), model.getTheKeyPost() , model.getCountKeyPost()  ,
                                        like , unLike , model.getThePosterMessage() , model.getThePosterNickname() ,
                                        model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus(),
                                        model.getPicPath());


                                CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                        model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                        model.getAudioPath() , model.getTheMainPlanetKeyPost());

                                CoreTheNotificationsList.NotificationForLike(model.getThePosterUID() , model.getThePosterNickname() ,
                                        model.getThePosterHandle() , model.getThePosterMessage() , model.getTheKeyPost() , "planet" ,
                                        theUID  , myNickName.getText().toString() , handle.getText().toString() ,
                                        model.getAudioStatus() , model.getPicPath() , model.getAudioPath());


                            }
                        });

                        unLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreThePostFunctions.unLikeMehod(theUID , model.getThePosterUID()  ,model.getTheMainPlanetKeyPost()  , model.getTheKeyPost() , model.getCountKeyPost() , unLike , like);

                            }
                        });


                        fave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreThePostFunctions.favMethod(theUID, model.getThePosterUID() , model.getWhichRoom() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                        , model.getTheKeyPost() , model.getCountKeyPost() , fave , unFave ,
                                        model.getThePosterMessage() , model.getThePosterNickname() ,
                                        model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus() , model.getPicPath());

                                CoreTheVolcano.addTheVolcano(model.getThePosterUID() , model.getWhichRoom() , model.getPostCountry() ,
                                        model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                        model.getAudioPath() , model.getTheMainPlanetKeyPost());
                            }
                        });

                        unFave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreThePostFunctions.unFavMehod(theUID ,
                                        model.getTheMainPlanetKeyPost() ,
                                        model.getTheKeyPost() , model.getCountKeyPost() , unFave , fave);

                            }
                        });

                        CoreThePostFunctions.countPost(model.getTheMainPlanetKeyPost() , model.getCountKeyPost() , countReplies  ,
                                countAdss , countLikes , countFavs , countListeners);

                        CoreThePostFunctions.repostOrNot(theUID , model.getTheKeyPost() ,repost , unrepost);
                        CoreThePostFunctions.likesOrNotMethod(theUID  , model.getTheKeyPost() ,like , unLike);
                        CoreThePostFunctions.favsOrNotMethod(theUID , model.getTheKeyPost() ,fave , unFave);

                        //play audio stuff
                        playMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                CoreThePost.downloadURLToPlay(Planet.this , model.getThePosterUID() , model.getAudioPath() , seekBarForPlay , playMessage , pauseMessage);
                                CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                        model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                        model.getAudioPath() , model.getTheMainPlanetKeyPost());


                                CoreThePostFunctions.listenedOrNotMethod
                                        (model.getThePosterUID() , model.getThePosterNickname() ,
                                                model.getThePosterHandle() ,
                                                model.getThePosterMessage() , model.getCountKeyPost(),
                                                model.getTheKeyPost() ,
                                                theUID  , myNickName.getText().toString() , handle.getText().toString(),
                                                model.getAudioStatus() , model.getPicPath() , model.getAudioPath());
                            }
                        });


                        pauseMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                              CoreThePost.stopTheExoPlayer(playMessage , pauseMessage);
                            }
                        });

                        postLay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(Planet.this , ThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);
                            }
                        });


                        typePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(Planet.this , ThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);
                            }
                        });


                        reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                /*
                                   Intent i = new Intent(Planet.this , ReplayThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra( "thePosterUID", model.getThePosterUID());
                                i.putExtra( "thePosterMessage", model.getThePosterMessage());
                                i.putExtra( "thePosterNickName", model.getThePosterNickname());
                                i.putExtra( "thePosterHandle", model.getThePosterHandle());
                                i.putExtra( "audioStatus", model.getAudioStatus());
                                i.putExtra( "mainKey", model.getTheMainPlanetKeyPost());
                                i.putExtra( "thePosterCountry", model.getCurrentCountry());
                                i.putExtra( "countKey", model.getCountKeyPost());
                                i.putExtra("postKey" , model.getTheKeyPost());
                                startActivity(i);
                                 */
                                Intent i = new Intent(Planet.this , ThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);

                            }
                        });
                    }
                };

        listViewPlanet.setAdapter(adapter);

    }



    private void requestPermission() {
        ActivityCompat.requestPermissions(Planet.this, new
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
                        Toast.makeText(Planet.this, "Permission Granted",
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
        if (messageLay.getVisibility() == View.VISIBLE)
        {

            messageLay.setVisibility(View.GONE);
            theSpace.setVisibility(View.GONE);
            audioStuffLay.setVisibility(View.GONE);
            listViewLay.setVisibility(View.VISIBLE);

        }else
        {
            finish();
            super.onBackPressed();
        }
    }
}
