package alserdar.widewall.post_stuff;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import alserdar.widewall.R;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.core.CoreThePostFunctions;
import alserdar.widewall.core.CoreTheReply;
import alserdar.widewall.core.CoreTheReplyFunctions;
import alserdar.widewall.core.CoreTheVolcano;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.PostModel;
import alserdar.widewall.models.ReplyModel;
import alserdar.widewall.models.UserModel;

public class ThePost extends AppCompatActivity {

    String theUID , myNickName , handle ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_post);

        theUID = DetectUserInfo.theUID(getBaseContext());
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


        String main = this.getIntent().getExtras().getString("ThePlanet");
        String theKeyPost = this.getIntent().getExtras().getString("postKey");


        LinearLayout typePost = findViewById(R.id.textPost);
        LinearLayout postLay = findViewById(R.id.postLay);
        final LinearLayout audioPost = findViewById(R.id.audioPost);
        final TextView messageText = findViewById(R.id.textMessage);
        final TextView messageUser = findViewById(R.id.userMessage);
        final TextView messageHandle = findViewById(R.id.handleMessage);
        final ImageView flag = findViewById(R.id.flagMessage);
        final TextView countListeners = findViewById(R.id.count_listeners);
        final TextView countReplies = findViewById(R.id.countReplies);
        final TextView countAdss = findViewById(R.id.countAdss);
        final TextView countLikes = findViewById(R.id.countLikes);
        final TextView countFavs = findViewById(R.id.countFavs);
        final TextView timeMessage = findViewById(R.id.timeMessage);

        final Button playMessage = findViewById(R.id.play_message_audio);
        final Button pauseMessage = findViewById(R.id.puase_message_audio);
        final SeekBar seekBarForPlay = findViewById(R.id.seek_bar_message_audio);
        final ImageView ppForChat = findViewById(R.id.userProfilePicture);
        final ImageView reply = findViewById(R.id.replyGlobal);
        final ImageView add = findViewById(R.id.addGlobal);
        final ImageView like = findViewById(R.id.likeGlobal);
        final ImageView fave = findViewById(R.id.favGlobal);
        final ImageView unAdd = findViewById(R.id.unAddGlobal);
        final ImageView unLike = findViewById(R.id.unLikeGlobal);
        final ImageView unFave = findViewById(R.id.unFavGlobal);
        ListView listOfReplies = findViewById(R.id.listOfReplies);

        final ImageView thePicture = findViewById(R.id.thePicture);

        final LinearLayout openReply = findViewById(R.id.openTheReplyLayout);
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openReply.getVisibility() == View.VISIBLE)
                {
                    openReply.setVisibility(View.GONE);
                }else
                {
                    openReply.setVisibility(View.VISIBLE);
                }

            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(Objects.requireNonNull(main)).child(Objects.requireNonNull(theKeyPost));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final PostModel model = dataSnapshot.getValue(PostModel.class);

                if (model.getAudioStatus().trim().equals("voiceNotes")) {
                    audioPost.setVisibility(View.VISIBLE);
                } else {
                    audioPost.setVisibility(View.GONE);
                }


                final ImageView replyOnHimButton = findViewById(R.id.replyOnHimButton);
                final EditText replyOnHimEditText = findViewById(R.id.replyOnHimEditText);

                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        if (replyOnHimEditText.getText().toString().trim().equals("")) {
                            replyOnHimButton.setVisibility(View.GONE);
                        } else {
                            replyOnHimButton.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                };

                replyOnHimEditText.addTextChangedListener(watcher);


               // replyOnHimEditText.setText(model.getThePosterHandle());


                final String theReplierUID ;
                theReplierUID = DetectUserInfo.theUID(ThePost.this);
                FirebaseFirestore theReplierDB = FirebaseFirestore.getInstance();
                DocumentReference theReplierRef = theReplierDB.collection("UserInformation")
                        .document(theReplierUID);

                theReplierRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {

                            final UserModel userModel = documentSnapshot.toObject(UserModel.class);

                            replyOnHimButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CoreTheReply.replyThePost(model.getThePosterUID(), model.getThePosterMessage()
                                            , model.getTheKeyPost() , model.getThePosterNickname(),
                                            model.getThePosterHandle(), model.getPostCountry(),
                                            model.getWhichRoom(), theReplierUID, replyOnHimEditText.getText().toString(),
                                            userModel.getUserName(), userModel.getHandle(), userModel.getCountry(),
                                            model.getAudioStatus() , model.getPicPath() , model.getAudioPath());
                                    replyOnHimEditText.setText(model.getThePosterHandle());
                                    openReply.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                });



                messageUser.setText(model.getThePosterNickname());
                messageHandle.setText(model.getThePosterHandle());
                messageText.setText(model.getThePosterMessage());
                timeMessage.setText(String.format("%s %s",model.getThePostTime() , model.getThePostDate()));
                LoadPictures.loadPicsForHome(getBaseContext(), model.getThePosterUID(), ppForChat);
                LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());


                LoadPictures.loadPicturesInMainChat(ThePost.this , model.getWhichRoom() ,
                        model.getThePosterUID() , thePicture , model.getPicPath());

                messageUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DetectUserInfoFromFirebase.clickTheUser(getBaseContext() , theUID , model.getThePosterUID());
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


                        CoreThePostFunctions.repostMethod(model.getThePosterUID(), model.getThePosterUID() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                , model.getTheKeyPost() , model.getCountKeyPost()  , add , unAdd ,
                                model.getThePosterMessage() , model.getThePosterNickname() ,
                                model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus());

                    }
                });

                unAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CoreThePostFunctions.unRepostMethod(model.getThePosterUID() , theUID ,model.getTheMainPlanetKeyPost()   , model.getTheKeyPost() , model.getCountKeyPost() , unAdd , add);
                    }
                });


                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CoreThePostFunctions.likeMethod(model.getThePosterUID(), model.getThePosterUID() , model.getWhichRoom() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                , model.getTheKeyPost() , model.getCountKeyPost()  , like , unLike ,
                                model.getThePosterMessage() , model.getThePosterNickname() ,
                                model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus() , model.getPicPath());
                    }
                });

                unLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CoreThePostFunctions.unLikeMehod(model.getThePosterUID() , theUID  ,model.getTheMainPlanetKeyPost()  , model.getTheKeyPost() , model.getCountKeyPost() , unLike , like);

                    }
                });


                fave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CoreThePostFunctions.favMethod(model.getThePosterUID(), model.getThePosterUID() ,
                                model.getWhichRoom(), model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                , model.getTheKeyPost() , model.getCountKeyPost() , fave , unFave ,
                                model.getThePosterMessage() , model.getThePosterNickname() ,
                                model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus() ,
                                model.getPicPath());
                    }
                });

                unFave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CoreThePostFunctions.unFavMehod(model.getThePosterUID() ,
                                model.getTheMainPlanetKeyPost() ,
                                model.getTheKeyPost() , model.getCountKeyPost() , unFave , fave);

                    }
                });

                CoreThePostFunctions.countPost(model.getTheMainPlanetKeyPost() , model.getCountKeyPost() , countReplies  ,
                        countAdss , countLikes , countFavs , countListeners);

                CoreThePostFunctions.repostOrNot(model.getThePosterUID() , model.getTheKeyPost() ,add , unAdd);
                CoreThePostFunctions.likesOrNotMethod(model.getThePosterUID()  , model.getTheKeyPost() ,like , unLike);
                CoreThePostFunctions.favsOrNotMethod(model.getThePosterUID()  , model.getTheKeyPost() ,fave , unFave);

                //play audio stuff
                playMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CoreThePost.downloadURLToPlay(ThePost.this , model.getThePosterUID() , model.getAudioPath() , seekBarForPlay , playMessage , pauseMessage);
                        CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                model.getAudioPath() , model.getTheMainPlanetKeyPost());


                        CoreThePostFunctions.listenedOrNotMethod
                                (model.getThePosterUID() , model.getThePosterNickname() ,
                                        model.getThePosterHandle() ,
                                        model.getThePosterMessage() , model.getCountKeyPost(),
                                        model.getTheKeyPost() ,
                                        theUID  , myNickName , handle , model.getAudioStatus() ,
                                        model.getPicPath()  , model.getAudioPath());
                    }
                });


                pauseMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CoreThePost.stopTheExoPlayer(playMessage , pauseMessage);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //===================the replies ===================================================

        FirebaseListAdapter<ReplyModel> adapter =
                new FirebaseListAdapter<ReplyModel>(ThePost.this, ReplyModel.class, R.layout.list_of_replies,
                        FirebaseDatabase.getInstance().getReference("replyPost " + theKeyPost)) {


                    @Override
                    protected void populateView(final View v, final ReplyModel model, final int position) {

                        LinearLayout typePost = v.findViewById(R.id.textPost);
                        LinearLayout postLay = v.findViewById(R.id.postLay);
                        LinearLayout audioPost = v.findViewById(R.id.audioPost);
                        final TextView messageText = v.findViewById(R.id.textMessage);
                        final TextView messageUser = v.findViewById(R.id.userMessage);
                        final TextView messageHandle = v.findViewById(R.id.handleMessage);
                        final TextView messageTime = v.findViewById(R.id.timeMessage);
                        final ImageView flag = v.findViewById(R.id.flagMessage);
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
                        final ImageView add = v.findViewById(R.id.addGlobal);
                        final ImageView like = v.findViewById(R.id.likeGlobal);
                        final ImageView fave = v.findViewById(R.id.favGlobal);
                        final ImageView unAdd = v.findViewById(R.id.unAddGlobal);
                        final ImageView unLike = v.findViewById(R.id.unLikeGlobal);
                        final ImageView unFave = v.findViewById(R.id.unFavGlobal);


                        messageText.setText(model.getTheReply());
                        messageUser.setText(model.getTheReplierNickName());
                        messageHandle.setText(model.getTheReplierHandle());
                        messageTime.setText(String.format("%s %s", model.getReplyTime(), model.getReplyDate()));
                        LoadPictures.loadPicsForHome(ThePost.this, model.getTheReplierUID(), ppForChat);
                        LoadPictures.setCountryFlagByNameCountry(flag , model.getThePosterCountry());


                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                CoreTheReplyFunctions.addMethod(model.getThePosterUID()  , model.getThePostKey()
                                        , model.getTheReplyCountKey()  , fave , unFave ,
                                        model.getThePost(), model.getThePosterNickName() ,
                                        model .getThePosterHandle());

                            }
                        });

                        unAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CoreTheReplyFunctions.unAddMehod(model.getThePosterUID() ,
                                        model.getThePostKey() , model.getTheReplyCountKey() , unAdd , add);
                            }
                        });


                        like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreTheReplyFunctions.likeMethod(model.getThePosterUID()  , model.getThePostKey()
                                        , model.getTheReplyCountKey()  , fave , unFave ,
                                        model.getThePost(), model.getThePosterNickName() ,
                                        model .getThePosterHandle());
                            }
                        });

                        unLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreTheReplyFunctions.unLikeMehod(model.getThePosterUID() ,
                                        model.getThePostKey() , model.getTheReplyCountKey()  , unLike , like);

                            }
                        });


                        fave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreTheReplyFunctions.favMethod(model.getThePosterUID()  , model.getThePostKey()
                                        , model.getTheReplyCountKey()  , fave , unFave ,
                                        model.getThePost(), model.getThePosterNickName() ,
                                        model .getThePosterHandle());
                            }
                        });

                        unFave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreTheReplyFunctions.unFavMehod(model.getThePosterUID() ,
                                        model.getThePostKey() , model.getTheReplyCountKey() , unFave , fave);

                            }
                        });

                        CoreTheReplyFunctions.countPost(model.getThePostKey() , model.getTheReplyCountKey()
                                , countReplies  , countAdss , countLikes , countFavs , countListeners);



                        //DetectUserInfoFromFirebase.repostOrNot(model.getThePosterUID() , model.getReplyKey() ,add , unAdd);
                       // DetectUserInfoFromFirebase.likesOrNotMethod(model.getThePosterUID()  , model.getReplyKey() ,like , unLike);
                       // DetectUserInfoFromFirebase.favsOrNotMethod(model.getThePosterUID()  , model.getReplyKey() ,fave , unFave);

                    }
                };

        listOfReplies.setAdapter(adapter);

    }
}
