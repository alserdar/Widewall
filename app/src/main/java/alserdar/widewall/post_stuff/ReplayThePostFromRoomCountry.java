package alserdar.widewall.post_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.widewall.R;
import alserdar.widewall.animate.RotateImage;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.core.CoreThePostFunctions;
import alserdar.widewall.core.CoreTheReply;
import alserdar.widewall.core.CoreTheVolcano;
import alserdar.widewall.fragment_home_stuff.RoomCountry;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.PostModel;
import alserdar.widewall.models.UserModel;

public class ReplayThePostFromRoomCountry extends AppCompatActivity {

    String theUID , main , thePosterUID , thePost , thePosterNickName , thePosterHandle  , thePosterCountry,
            audioStatus , mainKey , thePostKey , countKey ;
    private String myNickName  , handle;
    private String picPath , audioPath ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay_the_post);

        theUID = DetectUserInfo.theUID(ReplayThePostFromRoomCountry.this);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    UserModel model = documentSnapshot.toObject(UserModel.class);
                    myNickName = model.getUserName();
                    handle = model.getHandle();
                }

            }
        });


        main = this.getIntent().getExtras().getString("ThePlanet");
        thePosterUID = this.getIntent().getExtras().getString("thePosterUID");
        thePost = this.getIntent().getExtras().getString("thePosterMessage");
        thePosterNickName = this.getIntent().getExtras().getString("thePosterNickName");
        thePosterHandle = this.getIntent().getExtras().getString("thePosterHandle");
        thePosterCountry  = this.getIntent().getExtras().getString("thePosterCountry");
        audioStatus = this.getIntent().getExtras().getString("audioStatus");
        mainKey = this.getIntent().getExtras().getString("mainKey");
        countKey = this.getIntent().getExtras().getString("countKey");
        thePostKey  = this.getIntent().getExtras().getString("postKey");
        picPath = this.getIntent().getExtras().getString("picPath");
        audioPath = this.getIntent().getExtras().getString("audioPath");


        LinearLayout typePost = findViewById(R.id.textPost);
        LinearLayout postLay = findViewById(R.id.postLay);
        final LinearLayout audioPost = findViewById(R.id.audioPost);
        final TextView messageText = findViewById(R.id.textMessage);
        final TextView messageUser = findViewById(R.id.userMessage);
        final TextView messageHandle = findViewById(R.id.handleMessage);
        final TextView messageTime = findViewById(R.id.timeMessage);
        final ImageView flag = findViewById(R.id.flagMessage);
        final TextView countListeners = findViewById(R.id.count_listeners);
        final TextView countReplies = findViewById(R.id.countReplies);
        final TextView countAdss = findViewById(R.id.countAdss);
        final TextView countLikes = findViewById(R.id.countLikes);
        final TextView countFavs = findViewById(R.id.countFavs);

        final Button playMessage = findViewById(R.id.play_message_audio);
        final Button pauseMessage = findViewById(R.id.puase_message_audio);
        final SeekBar seekBarForPlay = findViewById(R.id.seek_bar_message_audio);
        final ImageView ppForChat = findViewById(R.id.userProfilePicture);
        final ImageView reply = findViewById(R.id.replyGlobal);
        final ImageView add = findViewById(R.id.addGlobal);
        final ImageView unAdd = findViewById(R.id.unAddGlobal);
        final ImageView like = findViewById(R.id.likeGlobal);
        final ImageView fave = findViewById(R.id.favGlobal);
        final ImageView unLike = findViewById(R.id.unLikeGlobal);
        final ImageView unFave = findViewById(R.id.unFavGlobal);

        final ImageView thePicture = findViewById(R.id.thePicture);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(main).child(thePostKey);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {

                    final PostModel model = dataSnapshot.getValue(PostModel.class);

                    if (audioStatus.trim().equals("voiceNotes") || model.getAudioStatus().equals("voiceNotes")) {
                        audioPost.setVisibility(View.VISIBLE);
                    } else {
                        audioPost.setVisibility(View.GONE);
                    }

                    assert model != null;
                    messageUser.setText(model.getThePosterNickname());
                    messageHandle.setText(model.getThePosterHandle());
                    messageTime.setText(String.format("%s %s", model.getThePostTime(), model.getThePostDate()));

                    messageUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DetectUserInfoFromFirebase.clickTheUser(ReplayThePostFromRoomCountry.this , theUID , thePosterUID);
                        }
                    });

                    messageText.setText(model.getThePosterMessage());
                    LoadPictures.loadPicsForHome(getBaseContext(), model.getThePosterUID(), ppForChat);
                    LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());
                    LoadPictures.loadPicturesInMainChat(ReplayThePostFromRoomCountry.this , model.getWhichRoom() ,
                            model.getThePosterUID() , thePicture , model.getPicPath());

                    ppForChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            DetectUserInfoFromFirebase.clickTheUser(ReplayThePostFromRoomCountry.this , theUID , model.getThePosterUID());

                        }
                    });

                    if (model.getAudioStatus().trim().equals("voiceNotes")) {
                        audioPost.setVisibility(View.VISIBLE);
                    } else {
                        audioPost.setVisibility(View.GONE);
                    }


                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            CoreThePostFunctions.repostMethod(theUID, model.getThePosterUID() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                    , model.getTheKeyPost() , model.getCountKeyPost() , add , unAdd ,
                                    model.getThePosterMessage() , model.getThePosterNickname() ,
                                    model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus());

                        }
                    });

                    unAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CoreThePostFunctions.unRepostMethod(theUID  , model.getThePosterUID() ,model.getTheMainPlanetKeyPost()   , model.getTheKeyPost() , model.getCountKeyPost() , unAdd , add);
                        }
                    });


                    like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            CoreThePostFunctions.likeMethod(theUID, model.getThePosterUID() ,
                                    model.getWhichRoom(), model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                    , model.getTheKeyPost() , model.getCountKeyPost() , like , unLike ,
                                    model.getThePosterMessage() , model.getThePosterNickname() ,
                                    model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus(),model.getPicPath());
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

                    CoreThePostFunctions.repostOrNot(theUID , model.getTheKeyPost() ,add , unAdd);
                    CoreThePostFunctions.likesOrNotMethod(theUID  , model.getTheKeyPost() ,like , unLike);
                    CoreThePostFunctions.favsOrNotMethod(theUID  , model.getTheKeyPost() ,fave , unFave);


                    //play audio stuff
                    playMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            CoreThePost.downloadURLToPlay(ReplayThePostFromRoomCountry.this , model.getThePosterUID() , model.getAudioPath() , seekBarForPlay , playMessage , pauseMessage);
                            CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                    model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                    model.getAudioPath() , model.getTheMainPlanetKeyPost());


                            CoreThePostFunctions.listenedOrNotMethod
                                    (model.getThePosterUID() , model.getThePosterNickname() ,
                                            model.getThePosterHandle() ,
                                            model.getThePosterMessage() , model.getCountKeyPost(),
                                            model.getTheKeyPost() ,
                                            theUID  , myNickName , handle , model.getAudioStatus() , model.getPicPath() , model.getAudioPath());
                        }
                    });


                    pauseMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            CoreThePost.stopTheExoPlayer(playMessage , pauseMessage);
                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final String theReplierUID ;
        theReplierUID = DetectUserInfo.theUID(ReplayThePostFromRoomCountry.this);
        FirebaseFirestore theReplierDB = FirebaseFirestore.getInstance();
        DocumentReference theReplierRef = theReplierDB.collection("UserInformation").document(theReplierUID);

        theReplierRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {

                    final UserModel userModel = documentSnapshot.toObject(UserModel.class);

                    final EditText replyText = findViewById(R.id.replyThePostEditText);
                    final Button replyButton = findViewById(R.id.replyThePostButton);

                    TextWatcher watcher = new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            if (replyText.getText().toString().trim().equals("")) {
                                replyButton.setVisibility(View.GONE);
                            } else {
                                replyButton.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    };

                    replyText.addTextChangedListener(watcher);

                    replyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RotateImage.startRotateMyButton(replyButton);
                            CoreTheReply.replyThePost(thePosterUID, thePost , thePostKey , thePosterNickName,
                                    thePosterHandle, thePosterCountry, thePosterCountry, theReplierUID,
                                    replyText.getText().toString(), userModel.getUserName(), userModel.getHandle(),
                                    userModel.getCountry() , audioStatus , picPath , audioPath);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 3000);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ReplayThePostFromRoomCountry.this , RoomCountry.class);
        startActivity(i);
        finish();
    }
}
