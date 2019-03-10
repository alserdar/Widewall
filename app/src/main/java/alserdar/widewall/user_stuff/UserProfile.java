package alserdar.widewall.user_stuff;

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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.widewall.R;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.core.CoreThePostFunctions;
import alserdar.widewall.core.CoreTheVolcano;
import alserdar.widewall.core.Core_Following_Followers_Block_Chat;
import alserdar.widewall.fragment_messages_stuff.PrivateChat;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.PostModel;
import alserdar.widewall.models.UserModel;

public class UserProfile extends AppCompatActivity {

    private static String theUID , id , nickName , handle, country  ,
            hisUID , hisId , hisNickName , hisHandle , hisCountry , hisLink , hisInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        id = DetectUserInfo.id(UserProfile.this);
        theUID = DetectUserInfo.theUID(UserProfile.this);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    UserModel model = documentSnapshot.toObject(UserModel.class);
                    nickName = model.getUserName();
                    handle = model.getHandle();
                }

            }
        });

        hisUID = getIntent().getExtras().getString("hisUID");
        hisId = getIntent().getExtras().getString("hisId");
        hisNickName = getIntent().getExtras().getString("hisName");
        hisHandle = getIntent().getExtras().getString("hisHandle");
        hisInfo = getIntent().getExtras().getString("hisInfo");
        hisCountry = getIntent().getExtras().getString("hisCountry");
        hisLink = getIntent().getExtras().getString("hisLink");
        final FloatingActionButton fabUnFollow = findViewById(R.id.unfollow);
        final FloatingActionButton fabFollow = findViewById(R.id.follow);
        fabFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Core_Following_Followers_Block_Chat.
                        followMethod(UserProfile.this , theUID , hisUID , fabFollow , fabUnFollow);
            }
        });



        fabUnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Core_Following_Followers_Block_Chat.unFollowMethod(theUID , hisUID , fabFollow , fabUnFollow);

            }
        });

        FloatingActionButton fabChat = findViewById(R.id.chat);
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(UserProfile.this , PrivateChat.class);
                i.putExtra("hisUID" , hisUID);
                i.putExtra("hisId" , hisId);
                startActivity(i);
            }
        });

        FloatingActionButton fabBlock = findViewById(R.id.block);
        fabBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Core_Following_Followers_Block_Chat.block(UserProfile.this , theUID , hisUID);
            }
        });

        ImageView userProfilePic = findViewById(R.id.userProfilePic);
        ImageView userProfileCountry = findViewById(R.id.userProfileCountry);
        TextView userProfileNickName = findViewById(R.id.userProfileNickName);
        TextView  userProfileHandle = findViewById(R.id.userProfileHandle);
        TextView  userProfileInfo = findViewById(R.id.userProfileInfo);
        TextView  userProfileLink = findViewById(R.id.userProfileLink);
        TextView  userFollowsText = findViewById(R.id.userFollowsText);
        ImageView blocked = findViewById(R.id.blockedImage);


        LoadPictures.loadPicsForHome(UserProfile.this , hisUID , userProfilePic);
        LoadPictures.setCountryFlagByNameCountry(userProfileCountry , hisCountry);

        DetectUserInfoFromFirebase.UserProfileInformationCollectInfo( hisUID ,
                userProfileNickName , userProfileHandle ,
                userProfileInfo , userProfileLink);

        Core_Following_Followers_Block_Chat.amIFollowHimOrNot(theUID , hisUID , fabFollow , fabUnFollow);
        Core_Following_Followers_Block_Chat.isHeFollowMeOrNot(theUID , hisUID , userFollowsText);
        Core_Following_Followers_Block_Chat.amIBlockedOrNot(theUID , hisUID , blocked , fabFollow , fabChat);

        SectionsPagerAdapter mSectionsPagerAdapter = new UserProfile.SectionsPagerAdapter(getSupportFragmentManager());


        ViewPager mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.getTabAt(0).setIcon(R.mipmap.type_mode);
           // tabLayout.getTabAt(1).setIcon(R.mipmap.replay);
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
          //  Reply replies = new Reply();
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
               // case 1:
                //    return "Replies";
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


            FirebaseListAdapter<PostModel> adapter =
                    new FirebaseListAdapter<PostModel>(getContext(), PostModel.class, R.layout.list_of_replies,
                            FirebaseDatabase.getInstance().getReference(hisUID).child("posts")) {


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


                            if (model.getAudioStatus().trim().equals("voiceNotes")) {
                                audioPost.setVisibility(View.VISIBLE);
                            } else {
                                audioPost.setVisibility(View.GONE);
                            }

                            messageText.setText(model.getThePosterMessage());
                            messageUser.setText(model.getThePosterNickname());
                            messageHandle.setText(model.getThePosterHandle());
                            messageText.setText(model.getThePosterMessage());
                            LoadPictures.loadPicsForHome(getContext(), model.getThePosterUID(), ppForChat);
                            LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());
                            LoadPictures.loadPicturesInMainChat(getContext() , model.getWhichRoom() ,
                                    model.getThePosterUID() , thePicture , model.getPicPath());


                            add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    CoreThePostFunctions.
                                            repostMethod(model.getThePosterUID(), model.getThePosterUID() , model.getPostCountry() , model.getTheMainPlanetKeyPost()
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
                                            model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus() ,
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

                            CoreThePostFunctions.countPost(model.getTheMainPlanetKeyPost() , model.getCountKeyPost() , countReplies  ,
                                    countAdss , countLikes , countFavs , countListeners);

                            CoreThePostFunctions.repostOrNot(model.getThePosterUID() , model.getTheMainPlanetKeyPost() ,add , unAdd);
                            CoreThePostFunctions.likesOrNotMethod(model.getThePosterUID()  , model.getTheMainPlanetKeyPost() ,like , unLike);
                            CoreThePostFunctions.favsOrNotMethod(model.getThePosterUID()  , model.getTheMainPlanetKeyPost() ,fave , unFave);

                            //play audio stuff
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
                                                    theUID  , nickName , handle , model.getAudioStatus() ,
                                                    model.getPicPath() , model.getAudioPath());
                                }
                            });


                            pauseMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CoreThePost.stopTheExoPlayer(playMessage , pauseMessage);
                                }
                            });

                        }
                    };

            posts.setAdapter(adapter);

            return rootView;
        }
    }

    // replies
    public static class Reply extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.user_information_replies_list, container, false);
            final ListView posts = rootView.findViewById(R.id.repliesListViewInUserProfileInformation);
            ViewCompat.setNestedScrollingEnabled(posts , true);

            FirebaseListAdapter<PostModel> adapter =
                    new FirebaseListAdapter<PostModel>(getContext(), PostModel.class, R.layout.list_of_replies,
                            FirebaseDatabase.getInstance().getReference(hisUID).child("replies")) {


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
                            messageHandle.setText("@"+model.getThePosterHandle());
                            messageText.setText(model.getThePosterMessage());
                            LoadPictures.loadPicsForHome(getContext(), model.getThePosterUID(), ppForChat);
                            LoadPictures.setCountryFlagByNameCountry(flag , model.getPostCountry());

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

                                    CoreThePostFunctions.likeMethod(model.getThePosterUID(), model.getThePosterUID() ,
                                            model.getWhichRoom(), model.getPostCountry()  , model.getTheMainPlanetKeyPost()
                                            , model.getTheKeyPost() , model.getCountKeyPost() , like , unLike ,
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
                                            , model.getTheKeyPost() , model.getCountKeyPost()  , fave , unFave ,
                                            model.getThePosterMessage() , model.getThePosterNickname() ,
                                            model .getThePosterHandle() , model.getAudioPath() , model.getAudioStatus(),
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

                                    CoreThePost.downloadURLToPlay(getContext() , model.getThePosterUID() , model.getAudioPath() , seekBarForPlay , playMessage , pauseMessage);
                                    CoreTheVolcano.addTheVolcano(model.getThePosterUID() , "planet" , model.getPostCountry() ,
                                            model.getThePosterMessage() , model.getThePosterNickname() , model.getThePosterHandle() ,
                                            model.getAudioPath() , model.getTheMainPlanetKeyPost());


                                    CoreThePostFunctions.listenedOrNotMethod
                                            (model.getThePosterUID() , model.getThePosterNickname() ,
                                                    model.getThePosterHandle() ,
                                                    model.getThePosterMessage() , model.getCountKeyPost(),
                                                    model.getTheKeyPost() ,
                                                    theUID  , nickName , handle ,
                                                    model.getAudioStatus() , model.getPicPath() , model.getAudioPath());
                                }
                            });


                            pauseMessage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    CoreThePost.stopTheExoPlayer(playMessage , pauseMessage);
                                }
                            });

                        }
                    };

            posts.setAdapter(adapter);

            return rootView;
        }
    }





    // like
    public static class Liked extends Fragment {



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc ;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.user_information_liked_list, container, false);
            final ListView liked = rootView.findViewById(R.id.likesListViewInUserProfileInformation);

            ViewCompat.setNestedScrollingEnabled(liked , true);

            FirebaseListAdapter<PostModel> adapter =
                    new FirebaseListAdapter<PostModel>(getContext(), PostModel.class, R.layout.list_of_replies,
                            FirebaseDatabase.getInstance().getReference(hisUID).child("likes")) {


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


                            if (model.getAudioStatus().trim().equals("voiceNotes")) {
                                audioPost.setVisibility(View.VISIBLE);
                            } else {
                                audioPost.setVisibility(View.GONE);
                            }

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
        super.onBackPressed();
        finish();
    }
}
