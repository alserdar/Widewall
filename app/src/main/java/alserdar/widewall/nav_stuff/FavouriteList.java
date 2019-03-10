package alserdar.widewall.nav_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import alserdar.widewall.Home;
import alserdar.widewall.R;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.core.CoreThePostFunctions;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.PostModel;
import alserdar.widewall.post_stuff.ThePost;

public class FavouriteList extends AppCompatActivity {

    private String theUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_list);

        theUID  = DetectUserInfo.theUID(FavouriteList.this);
        final ListView favouriteLista = findViewById(R.id.listOfFavourite);
        ImageView pressBack = findViewById(R.id.press_back);
        ImageView profilePic = findViewById(R.id.profilePicInToolBar);
        ImageView topList = findViewById(R.id.getTheTopOfList);
        LinearLayout theLay = findViewById(R.id.theEmptyLay);

        DetectUserInfoFromFirebase.emptyListOrNot(theUID , "fav" , theLay);

        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FavouriteList.this , Home.class);
                startActivity(i);
                finish();
            }
        });

        LoadPictures.loadPicsForHome(FavouriteList.this , theUID , profilePic);

        topList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                favouriteLista.smoothScrollToPosition(0);
            }
        });


        FirebaseListAdapter<PostModel> adapter =
                new FirebaseListAdapter<PostModel>(FavouriteList.this, PostModel.class, R.layout.list_of_posts,
                        FirebaseDatabase.getInstance().getReference(theUID).child("fav")) {

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
                        final ImageView add = v.findViewById(R.id.addGlobal);
                        final ImageView unAdd = v.findViewById(R.id.unAddGlobal);
                        final ImageView like = v.findViewById(R.id.likeGlobal);
                        final ImageView fave = v.findViewById(R.id.favGlobal);
                        final ImageView unLike = v.findViewById(R.id.unLikeGlobal);
                        final ImageView unFave = v.findViewById(R.id.unFavGlobal);

                        final ImageView thePicture = v.findViewById(R.id.thePicture);


                        messageUser.setText(model.getThePosterNickname());
                        messageHandle.setText(model.getThePosterHandle());
                        messageText.setText(model.getThePosterMessage());
                        LoadPictures.loadPicsForHome(getBaseContext(), model.getThePosterUID(), ppForChat);
                        LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());

                        LoadPictures.loadPicturesInMainChat(FavouriteList.this , model.getWhichRoom() ,
                                model.getThePosterUID() , thePicture , model.getPicPath());

                        if (model.getAudioStatus().trim().equals("voiceNotes")) {
                            audioPost.setVisibility(View.VISIBLE);
                        } else {
                            audioPost.setVisibility(View.GONE);
                        }


                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                CoreThePostFunctions.repostMethod(theUID , model.getThePosterUID() , model.getPostCountry() , model.getTheMainPlanetKeyPost()
                                        , model.getTheKeyPost() , model.getCountKeyPost()  , add , unAdd ,
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

                                CoreThePostFunctions.likeMethod(theUID, model.getThePosterUID()  ,model.getWhichRoom(), model.getPostCountry()  , model.getTheMainPlanetKeyPost()
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
                        CoreThePostFunctions.favsOrNotMethod(theUID , model.getTheKeyPost() ,fave , unFave);

                        //play audio stuff
                        playMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CoreThePost.downloadURLToPlay(FavouriteList.this ,
                                        model.getThePosterUID() , model.getAudioPath() ,
                                        seekBarForPlay , playMessage , pauseMessage);

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

                                Intent i = new Intent(FavouriteList.this , ThePost.class);
                                i.putExtra("ThePlanet" , model.getWhichRoom());
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);
                            }
                        });


                        typePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(FavouriteList.this , ThePost.class);
                                i.putExtra("ThePlanet" ,  model.getWhichRoom());
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);
                            }
                        });


                        reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                /*
                                  Intent i = new Intent(FavouriteList.this , ReplayThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra( "thePosterUID", model.getThePosterUID());
                                i.putExtra( "thePosterMessage", model.getThePosterMessage());
                                i.putExtra( "thePosterNickName", model.getThePosterNickname());
                                i.putExtra( "thePosterHandle", model.getThePosterHandle());
                                i.putExtra( "audioStatus", model.getAudioStatus());
                                i.putExtra( "mainKey", model.getTheMainPlanetKeyPost());
                                i.putExtra( "countKey", model.getCountKeyPost());
                                i.putExtra("postKey" , model.getTheKeyPost());
                                startActivity(i);
                                 */

                                Intent i = new Intent(FavouriteList.this , ThePost.class);
                                i.putExtra("ThePlanet" , "ThePlanet");
                                i.putExtra("postKey" , model.getTheMainPlanetKeyPost());
                                startActivity(i);
                            }
                        });
                    }
                };

        favouriteLista.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(FavouriteList.this , Home.class);
        startActivity(i);
        finish();
    }
}
