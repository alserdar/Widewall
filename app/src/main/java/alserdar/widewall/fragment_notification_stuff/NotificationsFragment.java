package alserdar.widewall.fragment_notification_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import alserdar.widewall.R;
import alserdar.widewall.core.CoreTheNotificationsList;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.NotificationModel;
import alserdar.widewall.post_stuff.ThePost;

public class NotificationsFragment extends Fragment {
    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_notifications, container, false);
        ListView notificationsOfMine = view.findViewById(R.id.notificationsOfMine);
        LinearLayout theLay = view.findViewById(R.id.theEmptyLay);
        final String theUID = DetectUserInfo.theUID(getContext());

        DetectUserInfoFromFirebase.emptyListOrNot(theUID , "notifications" , theLay);

        CoreTheNotificationsList.setGotNotificationAsTrueOrFalse(theUID , false);

        FirebaseListAdapter<NotificationModel> adapter =
                new FirebaseListAdapter<NotificationModel>(getContext(), NotificationModel.class, R.layout.list_of_notifications,
                        FirebaseDatabase.getInstance().getReference(theUID).child("notifications")) {

                    @Override
                    public NotificationModel getItem(int position) {
                        return super.getItem(super.getCount() - position - 1);

                    }


                    @Override
                    protected void populateView(final View v, final NotificationModel model, final int position) {
                        ImageView typeOfNotification = v.findViewById(R.id.typeOfNotification);
                        ImageView profilePicInNotification = v.findViewById(R.id.profilePicInNotification);
                        ImageView replyOnHim = v.findViewById(R.id.replyOnHim);
                        TextView userNameInNotifications = v.findViewById(R.id.userNameNotification);
                        TextView userHandleInNotifications = v.findViewById(R.id.userHandleNotification);
                        TextView thePostInNotification = v.findViewById(R.id.thePostInNotification);
                        TextView theReplyInNotification = v.findViewById(R.id.theReplyInNotification);

                        LinearLayout includeThePost = v.findViewById(R.id.thePostInNotificationLayout);
                        LinearLayout includeTheReply = v.findViewById(R.id.theReplyInNotificationLayout);

                        userNameInNotifications.setText(model.getNotificationerName());
                        userHandleInNotifications.setText(model.getNotificationerHandle());
                        LoadPictures.loadPicsForHome(v.getContext(),
                                model.getNotificationerUID() , profilePicInNotification);

                        profilePicInNotification.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DetectUserInfoFromFirebase.clickTheUser(getContext() , theUID , model.getNotificationerUID());

                            }
                        });

                        if (model.getThePost().equals(""))
                        {
                            includeThePost.setVisibility(View.GONE);
                        }else if (model.getTheReply().equals(""))
                        {
                            includeThePost.setVisibility(View.VISIBLE);
                            thePostInNotification.setText(model.getThePost());

                        }

                        includeThePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(Objects.requireNonNull(getActivity()).getBaseContext() , ThePost.class);
                                i.putExtra("ThePlanet" , model.getWhichRoom());
                                i.putExtra("postKey" , model.getThePostKey());
                                startActivity(i);
                            }
                        });

                        if (model.getTheReply().equals(""))
                        {
                            includeTheReply.setVisibility(View.GONE);
                        }else
                        {
                            theReplyInNotification.setText(String.format("%s %s", model.getNotificationerHandle(), model.getTheReply()));
                        }

                        /*
                         includeThePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(getContext() , ThePost.class);
                                i.putExtra("ThePlanet" , model.getWhichRoom());
                                i.putExtra("postKey" , model.getThePostKey());
                                startActivity(i);
                            }
                        });
                         */



                        /*
                         replyOnHim.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(getContext() , ReplayThePost.class);
                                i.putExtra("ThePlanet" , model.getWhichRoom());
                                i.putExtra( "thePosterUID", model.getThePosterUID());
                                i.putExtra( "thePosterMessage", model.getThePost());
                                i.putExtra( "thePosterNickName", model.getThePosterNickName());
                                i.putExtra( "thePosterHandle", model.getThePosterHandle());
                                i.putExtra( "audioStatus", "");
                                i.putExtra( "mainKey", model.getThePostKey());
                                i.putExtra( "thePosterCountry", "");
                                i.putExtra( "countKey", model.getTheReplyCountKey());
                                i.putExtra("postKey" , model.getTheReplyKey());
                                startActivity(i);
                            }
                        });

                         */



                        switch (model.getNotificationType())
                        {
                            case "follower" :
                                typeOfNotification.setBackgroundResource(R.mipmap.winged_heart);
                                break;
                            case "reply" :
                                typeOfNotification.setBackgroundResource(R.mipmap.conversation);
                                userNameInNotifications.setText(model.getNotificationerName());
                                userHandleInNotifications.setText(model.getNotificationerHandle());
                                thePostInNotification.setText(model.getThePost());
                                theReplyInNotification.setText(model.getTheReply());
                                LoadPictures.loadPicsForHome(Objects.requireNonNull(getContext()), model.getNotificationerUID() , profilePicInNotification);
                                break;
                            case "rePost" :
                                typeOfNotification.setBackgroundResource(R.mipmap.repost);
                                break;
                            case "like" :
                                typeOfNotification.setBackgroundResource(R.mipmap.heart_pulse);
                                break;
                            case "listen" :
                                typeOfNotification.setBackgroundResource(R.mipmap.headphone);
                                break;
                        }
                    }
                };

        notificationsOfMine.setAdapter(adapter);
        return  view ;
    }
}
