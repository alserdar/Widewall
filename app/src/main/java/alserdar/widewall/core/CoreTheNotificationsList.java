package alserdar.widewall.core;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import alserdar.widewall.models.NotificationModel;

import static alserdar.widewall.core.CoreThePost.returnTheDate;
import static alserdar.widewall.core.CoreThePost.returnTheTime;

public class CoreTheNotificationsList {

    public static void NotificationForReplies(String thePost , String thePosterUID  ,
            String thePosterNickName , String thePosterHandle, String theReplierUID , String thePostKey  ,
                                              String theReplierNickName ,
                                              String theReplierHandle ,
                                              String whichRoom , String theReply , String replyKey , String replyCountKey ,
                                              String audioStatus ,String picPath ,String audioPath)
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference doc = db.getReference(thePosterUID).child("notifications").push();
        doc.setValue(new NotificationModel(thePosterUID ,thePosterNickName , thePosterHandle , thePost ,
                thePostKey , whichRoom  , theReplierUID
                , "reply"  , theReplierNickName ,
                theReplierHandle , theReply , replyKey ,
                replyCountKey , audioStatus , picPath , audioPath , returnTheDate() , returnTheTime()));
        CoreTheNotificationsList.setGotNotificationAsTrueOrFalse(thePosterUID , true);
    }

    public static void NotificationForLike(String thePosterUID, String thePosterNickName,
                                           String thePosterHandle, String thePost,
                                           String thePostKey, String whichRoom,
                                           String notificationerUID,
                                           String notificationerName, String notificationerHandle , String audioStatus ,String picPath ,String audioPath)
    {

        FirebaseDatabase dbLike = FirebaseDatabase.getInstance();
        final DatabaseReference docLike = dbLike.getReference(thePosterUID).child("notifications").push();
        docLike.setValue(new NotificationModel(thePosterUID ,thePosterNickName , thePosterHandle , thePost ,
                thePostKey , whichRoom  , notificationerUID
                , "like"  , notificationerName ,
                notificationerHandle , "" , " " ,
                "" ,  audioStatus , picPath , audioPath , returnTheDate() , returnTheTime()));

        CoreTheNotificationsList.setGotNotificationAsTrueOrFalse(thePosterUID , true);
    }

    public static void NotificationForListen(String thePosterUID, String thePosterNickName,
                                             String thePosterHandle, String thePost,
                                             String thePostKey, String whichRoom,
                                             String notificationerUID,
                                             String notificationerName, String notificationerHandle  , String audioStatus ,String picPath ,String audioPath)
    {
        FirebaseDatabase dbListen = FirebaseDatabase.getInstance();
        final DatabaseReference docListen = dbListen.getReference(thePosterUID).child("notifications").push();
        docListen.setValue(new NotificationModel(thePosterUID ,thePosterNickName , thePosterHandle , thePost ,
                thePostKey , whichRoom  , notificationerUID
                , "listen"  , notificationerName ,
                notificationerHandle , "" , " " ,
                "" ,  audioStatus , picPath , audioPath , returnTheDate() , returnTheTime()));

        docListen.setValue(new NotificationModel(thePosterUID ,thePosterNickName , thePosterHandle , thePost ,
                thePostKey , whichRoom  , notificationerUID
                , "listen"  , notificationerName ,
                notificationerHandle , "" , " " ,
                "" ,  audioStatus , picPath , audioPath , returnTheDate() , returnTheTime()));

        CoreTheNotificationsList.setGotNotificationAsTrueOrFalse(thePosterUID , true);
    }

    public static void NotificationForFollowers(String thePosterUID, String thePosterNickName,
                                                String thePosterHandle, String thePost,
                                                String thePostKey, String whichRoom,
                                                String notificationerUID,
                                                String notificationerName, String notificationerHandle , String audioStatus ,String picPath ,String audioPath)
    {
        FirebaseDatabase dbFollowing = FirebaseDatabase.getInstance();
        final DatabaseReference docFollowing = dbFollowing.getReference(thePosterUID).child("notifications").push();
        docFollowing.setValue(new NotificationModel(thePosterUID ,thePosterNickName , thePosterHandle , thePost ,
                thePostKey , whichRoom  , notificationerUID
                , "follower"  , notificationerName ,
                notificationerHandle , "" , " " ,
                "" ,  audioStatus , picPath , audioPath , returnTheDate() , returnTheTime()));

        CoreTheNotificationsList.setGotNotificationAsTrueOrFalse(thePosterUID , true);
    }

    public static void NotificationForReposts(String thePosterUID, String thePosterNickName,
                                              String thePosterHandle, String thePost,
                                              String thePostKey, String whichRoom,
                                              String notificationerUID,
                                              String notificationerName, String notificationerHandle , String audioStatus ,String picPath ,String audioPath)
    {
        FirebaseDatabase dbFollowing = FirebaseDatabase.getInstance();
        final DatabaseReference docFollowing = dbFollowing.getReference(thePosterUID).child("notifications").push();
        docFollowing.setValue(new NotificationModel(thePosterUID ,thePosterNickName , thePosterHandle , thePost ,
                thePostKey , whichRoom  , notificationerUID
                , "rePost"  , notificationerName ,
                notificationerHandle , "" , " " ,
                "" ,  audioStatus , picPath , audioPath , returnTheDate() , returnTheTime()));


        CoreTheNotificationsList.setGotNotificationAsTrueOrFalse(thePosterUID , true);
    }

    public static void setGotNotificationAsTrueOrFalse(String theUID , final boolean hasOrNot)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                HashMap<String , Object> map = new HashMap<>();
                map.put("gotNotification" , hasOrNot);
                doc.update(map);
            }
        });
    }

    public static void setGotMessageAsTrueOrFalse(String theUID , final boolean hasOrNot)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                HashMap<String , Object> map = new HashMap<>();
                map.put("gotMessages" , hasOrNot);
                doc.update(map);
            }
        });
    }
}
