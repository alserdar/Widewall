package alserdar.widewall;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.Objects;

import alserdar.widewall.core.CoreTheNotificationsList;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.core.CoreThePostFunctions;
import alserdar.widewall.core.CoreTheVolcano;
import alserdar.widewall.fragment_home_stuff.HomeFragment;
import alserdar.widewall.fragment_home_stuff.PlanetButWithFragment;
import alserdar.widewall.fragment_home_stuff.UploadThePostPleaseForPlanet;
import alserdar.widewall.fragment_messages_stuff.MessagesFragment;
import alserdar.widewall.fragment_notification_stuff.NotificationsFragment;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.PostModel;
import alserdar.widewall.nav_stuff.BlockList;
import alserdar.widewall.nav_stuff.FavouriteList;
import alserdar.widewall.nav_stuff.Location;
import alserdar.widewall.nav_stuff.MyProfile;
import alserdar.widewall.nav_stuff.Search;
import alserdar.widewall.nav_stuff.Volcano;
import alserdar.widewall.post_stuff.ReplayThePost;
import alserdar.widewall.post_stuff.ThePost;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeButWithTimeLine extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);


        navigation.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = PlanetButWithFragment.newInstance();
                                break;
                            case R.id.navigation_notifications:
                                selectedFragment = NotificationsFragment.newInstance();
                                break;
                            case R.id.navigation_dashboard:
                                selectedFragment = MessagesFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        navigation.setItemIconTintList(null);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();


        ImageView profilePic = findViewById(R.id.profilePicForHome);
        TextView homeUserName = findViewById(R.id.userNameForHome);

        theUID = DetectUserInfo.theUID(HomeButWithTimeLine.this);

        LoadPictures.loadPicsForHome(HomeButWithTimeLine.this , theUID, profilePic);
        DetectUserInfo.infoForHome(HomeButWithTimeLine.this , theUID, homeUserName);



         profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);

            }
        });




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                HomeButWithTimeLine.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView ppNav = header.findViewById(R.id.profilePictureNav);
        TextView userNameNav = header.findViewById(R.id.nameForNav);
        TextView handleNav = header.findViewById(R.id.handleForNav);
        DetectUserInfoFromFirebase.userHandle(getBaseContext() , userNameNav , handleNav);
        LoadPictures.loadPicsForHome(getBaseContext() , theUID, ppNav);
        navigationView.setNavigationItemSelectedListener(HomeButWithTimeLine.this);
        navigationView.setItemIconTintList(null);
        toggle.setDrawerIndicatorEnabled(false);



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
        profilePic = findViewById(R.id.profilePicInToolBar);
        final ImageView topList = findViewById(R.id.getTheTopOfList);



        DetectUserInfoFromFirebase.userHandle(getBaseContext() , myNickName , handle);
        LoadPictures.loadPicsForHome(HomeButWithTimeLine.this , theUID , profilePic);

        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeButWithTimeLine.this , Home.class);
                startActivity(i);
                finish();
            }
        });

        postMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(HomeButWithTimeLine.this , UploadThePostPleaseForPlanet.class);
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
                    CoreThePost.uploadTheRecord(HomeButWithTimeLine.this , theUID);
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

                    new OurToast().myToast(getBaseContext(), getString(R.string.recording));
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

                new OurToast().myToast(getBaseContext(), getString(R.string.reset));
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
                new FirebaseListAdapter<PostModel>(HomeButWithTimeLine.this, PostModel.class, R.layout.list_of_posts,
                        FirebaseDatabase.getInstance().getReference("ThePlanet").limitToLast(100)) {

                    @Override
                    public PostModel getItem(int position) {
                        return super.getItem(super.getCount() - position - 1);

                    }

                    @Override
                    protected void populateView(final View v, final PostModel model, final int position) {

                        LinearLayout typePost = v.findViewById(R.id.textPost);
                        LinearLayout postLay = v.findViewById(R.id.postLay);
                        LinearLayout audioPost = v.findViewById(R.id.audioPost);
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


                        messageUser.setText(model.getThePosterNickname());
                        messageHandle.setText(String.format("@%s", model.getThePosterHandle()));
                        messageText.setText(model.getThePosterMessage());
                        timeMessage.setText(model.getThePostTime());
                        LoadPictures.loadPicsForHome(getBaseContext(), model.getThePosterUID(), ppForChat);
                        LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());


                        messageUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DetectUserInfoFromFirebase.clickTheUser(HomeButWithTimeLine.this , theUID , model.getThePosterUID());
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


                                CoreThePostFunctions.repostMethod(theUID, model.getThePosterUID() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                        , model.getTheKeyPost() , model.getCountKeyPost()  , repost , unrepost ,
                                        model.getThePosterMessage() , model.getThePosterNickname() ,
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
                                        model.getWhichRoom(), model.getPostCountry() , model.getTheMainPlanetKeyPost()
                                        , model.getTheKeyPost() , model.getCountKeyPost()  , like , unLike ,
                                        model.getThePosterMessage() , model.getThePosterNickname() ,
                                        model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus() , model.getPicPath());


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

                                CoreThePostFunctions.unLikeMehod(theUID, model.getThePosterUID()  ,model.getTheMainPlanetKeyPost()  , model.getTheKeyPost() , model.getCountKeyPost() , unLike , like);

                            }
                        });


                        fave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreThePostFunctions.favMethod(theUID, model.getThePosterUID() , model.getWhichRoom() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                        , model.getTheKeyPost() , model.getCountKeyPost() , fave , unFave ,
                                        model.getThePosterMessage() , model.getThePosterNickname() ,
                                        model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus() , model.getPicPath());

                                CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
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

                                CoreThePost.downloadURLToPlay(HomeButWithTimeLine.this , model.getThePosterUID() , model.getAudioPath() , seekBarForPlay , playMessage , pauseMessage);
                                CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                        model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                        model.getAudioPath() , model.getTheMainPlanetKeyPost());


                                CoreThePostFunctions.listenedOrNotMethod
                                        (model.getThePosterUID() , model.getThePosterNickname() ,
                                                model.getThePosterHandle() ,
                                                model.getThePosterMessage() , model.getCountKeyPost(),
                                                model.getTheKeyPost() ,
                                                theUID  , myNickName.getText().toString() , handle.getText().toString() ,
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

                                Intent i = new Intent(HomeButWithTimeLine.this , ThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);
                            }
                        });


                        typePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(HomeButWithTimeLine.this , ThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);
                            }
                        });


                        reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(HomeButWithTimeLine.this , ReplayThePost.class);
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

                            }
                        });
                    }
                };

        listViewPlanet.setAdapter(adapter);

    }



    private void requestPermission() {
        ActivityCompat.requestPermissions(HomeButWithTimeLine.this, new
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
                        Toast.makeText(HomeButWithTimeLine.this, "Permission Granted",
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



    long back_pressed;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 3000 > System.currentTimeMillis()){
                super.onBackPressed();
            }
            else{

                new OurToast().myToastPic(getBaseContext() , "" , R.mipmap.close);
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(HomeButWithTimeLine.this , MyProfile.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_favourite) {
            Intent i = new Intent(HomeButWithTimeLine.this , FavouriteList.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_blocked) {
            Intent i = new Intent(HomeButWithTimeLine.this , BlockList.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_search) {
            Intent i = new Intent(HomeButWithTimeLine.this , Search.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_location) {
            Intent i = new Intent(HomeButWithTimeLine.this , Location.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_volcano) {
            Intent i = new Intent(HomeButWithTimeLine.this , Volcano.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_ads) {

        } else if (id == R.id.nav_contact_us) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","widewallapp@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WideWall User");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }else if (id == R.id.nav_settings) {
        }else if (id == R.id.nav_information)
        {

        }else if (id == R.id.nav_logout) {


                AlertDialog.Builder builder = new AlertDialog.Builder(HomeButWithTimeLine.this);

                String titleText = "Are you Sure !";
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
                ssBuilder.setSpan(
                        foregroundColorSpan,
                        0,
                        titleText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                builder.setTitle(ssBuilder);
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {

                            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                                ((ActivityManager)Objects.requireNonNull(getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData(); // note: it has a return value!
                            } else {
                                String packageName = getApplicationContext().getPackageName();
                                Runtime runtime = Runtime.getRuntime();
                                runtime.exec("pm clear "+packageName);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("No",null);
                AlertDialog dialog = builder.create();
               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
