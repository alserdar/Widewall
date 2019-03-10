package alserdar.widewall.nav_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import alserdar.widewall.Home;
import alserdar.widewall.R;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.core.CoreThePostFunctions;
import alserdar.widewall.core.CoreTheVolcano;
import alserdar.widewall.core.Core_Following_Followers_Block_Chat;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.PostModel;

public class MyProfile extends AppCompatActivity {

    String theUID ;
    private static TextView userProfileNickName , userProfileHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        theUID = DetectUserInfo.theUID(MyProfile.this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MyProfile.this , MyProfileSettings.class);
                startActivity(i);
                finish();
            }
        });

        ImageView userProfilePic = findViewById(R.id.userProfilePic);
        ImageView userProfileCountry = findViewById(R.id.userProfileCountry);
        userProfileNickName = findViewById(R.id.userProfileNickName);
        userProfileHandle = findViewById(R.id.userProfileHandle);
        TextView  userProfileInfo = findViewById(R.id.userProfileInfo);
        TextView  userProfileLink = findViewById(R.id.userProfileLink);
        TextView  userProfileFollowing = findViewById(R.id.userProfileFollowing);
        TextView  userProfileFollowers = findViewById(R.id.userProfileFollowers);

        userProfileLink.setMovementMethod(LinkMovementMethod.getInstance());

        LoadPictures.loadPicsForHome(MyProfile.this , theUID , userProfilePic);
        LoadPictures.setCountryFlag(MyProfile.this , userProfileCountry);

        DetectUserInfoFromFirebase.UserProfileInformationCollectInfo( theUID ,
                userProfileNickName , userProfileHandle ,
                userProfileInfo , userProfileLink);


        Core_Following_Followers_Block_Chat.getFollowingAndFollowers(MyProfile.this , userProfileFollowing , userProfileFollowers);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        ViewPager mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.getTabAt(0).setIcon(R.mipmap.type_mode);
          //  tabLayout.getTabAt(1).setIcon(R.mipmap.replay);
            tabLayout.getTabAt(1).setIcon(R.mipmap.like_heart);
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Posts posts = new Posts();
            Reply replies = new Reply();
            Liked liked = new Liked();
            switch (position)
            {
                case 0 : return posts ;
              //  case 1 : return replies ;
                case 1 : return liked;
            }
            return null ;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.posts);
               // case 1: return "Replies";
                case 1:
                    return getString(R.string.liked);
            }
            return null;
        }
    }

    public static class Posts extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.user_information_posts_list, container, false);
            final ListView posts = rootView.findViewById(R.id.postsListViewInUserProfileInformation);

            ViewCompat.setNestedScrollingEnabled(posts , true);
            final String theUID = DetectUserInfo.theUID(getContext());

            LinearLayout theLay = rootView.findViewById(R.id.theEmptyLay);
            DetectUserInfoFromFirebase.emptyListOrNot(theUID , "posts" , theLay);

            FirebaseListAdapter<PostModel> adapter =
                    new FirebaseListAdapter<PostModel>(getContext(), PostModel.class, R.layout.list_of_replies,
                            FirebaseDatabase.getInstance().getReference(theUID).child("posts")) {


                        @Override
                        public PostModel getItem(int position) {
                            return super.getItem(super.getCount() - position - 1);

                        }


                        @Override
                        protected void populateView(final View v, final PostModel model, final int position) {

                            LinearLayout typePost = v.findViewById(R.id.textPost);
                            LinearLayout postLay = v.findViewById(R.id.postLay);
                            LinearLayout audioPost = v.findViewById(R.id.audioPost);
                            final TextView messageText = v.findViewById(R.id.textMessage);
                            final TextView messageUser = v.findViewById(R.id.userMessage);
                            final TextView messageHandle = v.findViewById(R.id.handleMessage);
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

                            final ImageView thePicture = v.findViewById(R.id.thePicture);

                            messageText.setText(model.getThePosterMessage());
                            messageUser.setText(model.getThePosterNickname());
                            messageHandle.setText(model.getThePosterHandle());

                            LoadPictures.loadPicsForHome(Objects.requireNonNull(getContext()), model.getThePosterUID(), ppForChat);
                            LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());


                            LoadPictures.loadPicturesInMainChat(getContext() , model.getWhichRoom() ,
                                    model.getThePosterUID() , thePicture , model.getPicPath());

                            if (model.getAudioStatus().trim().equals("voiceNotes")) {
                                audioPost.setVisibility(View.VISIBLE);
                            } else {
                                audioPost.setVisibility(View.GONE);
                            }


                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    CoreThePostFunctions.repostMethod(model.getThePosterUID() , model.getThePosterUID() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
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

                                    CoreThePostFunctions.likeMethod(model.getThePosterUID(), model.getThePosterUID(),
                                            model.getWhichRoom(), model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                            , model.getTheKeyPost() , model.getCountKeyPost()  , like , unLike ,
                                            model.getThePosterMessage() , model.getThePosterNickname() ,
                                            model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus(),
                                            model.getPicPath());
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


                            playMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CoreThePost.downloadURLToPlay(getContext() , model.getThePosterUID() , model.getAudioPath() , seekBarForPlay , playMessage , pauseMessage);
                                    CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                            model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                            model.getAudioPath() , model.getTheMainPlanetKeyPost());


                                    CoreThePostFunctions.listenedOrNotMethod
                                            (model.getThePosterUID() , model.getThePosterNickname() ,
                                                    model.getThePosterHandle() ,
                                                    model.getThePosterMessage() , model.getCountKeyPost(),
                                                    model.getTheKeyPost() ,
                                                    theUID  , userProfileNickName.getText().toString() ,
                                                    userProfileHandle.getText().toString() ,
                                                    model.getAudioStatus() , model.getPicPath() , model.getAudioPath());
                                }
                            });


                            pauseMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CoreThePost.stopTheExoPlayer(playMessage , pauseMessage);
                                }
                            });


                            CoreThePostFunctions.countPost(model.getTheMainPlanetKeyPost() , model.getCountKeyPost() , countReplies  ,
                                    countAdss , countLikes , countFavs , countListeners);

                            CoreThePostFunctions.repostOrNot(model.getThePosterUID() , model.getTheKeyPost() ,add , unAdd);
                            CoreThePostFunctions.likesOrNotMethod(model.getThePosterUID()  , model.getTheKeyPost() ,like , unLike);
                            CoreThePostFunctions.favsOrNotMethod(model.getThePosterUID()  , model.getTheKeyPost() ,fave , unFave);



                        }
                    };

            posts.setAdapter(adapter);

            return rootView;
        }
    }

    // replies
    public static class Reply extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.user_information_replies_list, container, false);
            final ListView posts = rootView.findViewById(R.id.repliesListViewInUserProfileInformation);
            ViewCompat.setNestedScrollingEnabled(posts , true);

            final String theUID = DetectUserInfo.theUID(getContext());

            FirebaseListAdapter<PostModel> adapter =
                    new FirebaseListAdapter<PostModel>(getContext(), PostModel.class, R.layout.list_of_replies,
                            FirebaseDatabase.getInstance().getReference(theUID).child("replies")) {


                        @Override
                        protected void populateView(final View v, final PostModel model, final int position) {

                            LinearLayout typePost = v.findViewById(R.id.textPost);
                            LinearLayout postLay = v.findViewById(R.id.postLay);
                            LinearLayout audioPost = v.findViewById(R.id.audioPost);
                            final TextView messageText = v.findViewById(R.id.textMessage);
                            final TextView messageUser = v.findViewById(R.id.userMessage);
                            final TextView messageHandle = v.findViewById(R.id.handleMessage);
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


                            if (model.getAudioStatus().trim().equals("voiceNotes")) {
                                audioPost.setVisibility(View.VISIBLE);
                            } else {
                                audioPost.setVisibility(View.GONE);
                            }

                           // messageText.setText("@"+model.getThePosterHandle()+ '\n' + model.getTheReply());
                            messageUser.setText(model.getThePosterNickname());
                            messageHandle.setText(model.getThePosterHandle());
                            messageText.setText(model.getThePosterMessage());
                            LoadPictures.loadPicsForHome(getContext(), model.getThePosterUID(), ppForChat);
                            LoadPictures.setCountryFlag(getContext(), flag);

                            if (model.getAudioStatus().trim().equals("voiceNotes")) {
                                audioPost.setVisibility(View.VISIBLE);
                            } else {
                                audioPost.setVisibility(View.GONE);
                            }


                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    CoreThePostFunctions.repostMethod(model.getThePosterUID(), model.getThePosterUID() , model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                            , model.getTheKeyPost() , model.getCountKeyPost() , add , unAdd ,
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

                                    CoreThePostFunctions.likeMethod(model.getThePosterUID(), model.getThePosterUID(),
                                            model.getWhichRoom(), model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                            , model.getTheKeyPost() , model.getCountKeyPost() , like , unLike ,
                                            model.getThePosterMessage() , model.getThePosterNickname() ,
                                            model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus(),model.getPicPath());
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
                                            , model.getTheKeyPost() , model.getCountKeyPost()  , fave , unFave ,
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



                        }
                    };

            posts.setAdapter(adapter);

            return rootView;
        }
    }





    // like
    public static class Liked extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.user_information_liked_list, container, false);
            final ListView liked = rootView.findViewById(R.id.likesListViewInUserProfileInformation);

            ViewCompat.setNestedScrollingEnabled(liked , true);
            final String theUID = DetectUserInfo.theUID(getContext());

            LinearLayout theLay = rootView.findViewById(R.id.theEmptyLay);
            DetectUserInfoFromFirebase.emptyListOrNot(theUID , "likes" , theLay);



            FirebaseListAdapter<PostModel> adapter =
                    new FirebaseListAdapter<PostModel>(getContext(), PostModel.class, R.layout.list_of_replies,
                            FirebaseDatabase.getInstance().getReference(theUID).child("likes")) {


                        @Override
                        public PostModel getItem(int position) {
                            return super.getItem(super.getCount() - position - 1);

                        }


                        @Override
                        protected void populateView(final View v, final PostModel model, final int position) {

                            LinearLayout typePost = v.findViewById(R.id.textPost);
                            LinearLayout postLay = v.findViewById(R.id.postLay);
                            LinearLayout audioPost = v.findViewById(R.id.audioPost);
                            final TextView messageText = v.findViewById(R.id.textMessage);
                            final TextView messageUser = v.findViewById(R.id.userMessage);
                            final TextView messageHandle = v.findViewById(R.id.handleMessage);
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

                            final ImageView thePicture = v.findViewById(R.id.thePicture);

                            messageText.setText(model.getThePosterMessage());
                            messageUser.setText(model.getThePosterNickname());
                            messageHandle.setText(model.getThePosterHandle());

                            LoadPictures.loadPicsForHome(getContext(), model.getThePosterUID(), ppForChat);
                            LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());

                            LoadPictures.loadPicturesInMainChat(getContext() , model.getWhichRoom() ,
                                    model.getThePosterUID() , thePicture , model.getPicPath());


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
                                            model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus(),
                                            model.getPicPath());
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


                            playMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CoreThePost.downloadURLToPlay(getContext() , model.getThePosterUID() , model.getAudioPath() , seekBarForPlay , playMessage , pauseMessage);
                                    CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                            model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                            model.getAudioPath() , model.getTheMainPlanetKeyPost());


                                    CoreThePostFunctions.listenedOrNotMethod
                                            (model.getThePosterUID() , model.getThePosterNickname() ,
                                                    model.getThePosterHandle() ,
                                                    model.getThePosterMessage() , model.getCountKeyPost(),
                                                    model.getTheKeyPost() ,
                                                    theUID  , userProfileNickName.getText().toString() ,
                                                    userProfileHandle.getText().toString() ,
                                                    model.getAudioStatus() , model.getPicPath() , model.getAudioPath());
                                }
                            });


                            pauseMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CoreThePost.stopTheExoPlayer(playMessage , pauseMessage);
                                }
                            });


                            CoreThePostFunctions.countPost(model.getTheMainPlanetKeyPost() , model.getCountKeyPost() , countReplies  ,
                                    countAdss , countLikes , countFavs , countListeners);

                            CoreThePostFunctions.repostOrNot(model.getThePosterUID() , model.getTheKeyPost() ,add , unAdd);
                            CoreThePostFunctions.likesOrNotMethod(model.getThePosterUID()  , model.getTheKeyPost() ,like , unLike);
                            CoreThePostFunctions.favsOrNotMethod(model.getThePosterUID()  , model.getTheKeyPost() ,fave , unFave);



                        }
                    };

            liked.setAdapter(adapter);

            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MyProfile.this , Home.class);
        startActivity(i);
        finish();
    }
}
