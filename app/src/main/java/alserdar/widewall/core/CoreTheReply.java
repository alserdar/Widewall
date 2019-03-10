package alserdar.widewall.core;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alserdar.widewall.models.CountModel;
import alserdar.widewall.models.ReplyModel;

import static alserdar.widewall.core.CoreThePost.returnTheTime;
import static alserdar.widewall.core.CoreThePost.returnTheDate;

public class CoreTheReply {

    public static void replyThePost(String thePosterUID , String thePost , String thePostKey,
                                    String thePosterNickName , String thePosterHandle ,
                                    String thePosterCountry, String whichRoom ,
                                    String theReplierUID , String theReply ,String theReplierNickName ,
                                    String theReplierHandle ,String theReplierCountry ,
                                    String audioStatus , String picPath , String audioPath)
    {

        /*
           String theReplyKey ;
        FirebaseDatabase replyThePostWithPostDB = FirebaseDatabase.getInstance();
        DatabaseReference replytReferenceWithPost = replyThePostWithPostDB.getReference(thePosterUID).child("posts").push();
        theReplyKey = replytReferenceWithPost.getRef().getKey();



           replytReferenceWithPost.setValue(new ReplyModel(thePosterUID, thePost , thePostKey , thePosterNickName,
                   thePosterHandle, thePosterCountry, whichRoom, theReplierUID,
                   theReply, theReplierNickName, theReplierHandle,
                   theReplierCountry, theReplyKey, theReplyCountKey,
                   "" , "" , "" , false , new Date()));
         */



        FirebaseDatabase replyThePost = FirebaseDatabase.getInstance();
        DatabaseReference replytReference = replyThePost.getReference("replyPost " + thePostKey).push();
        String theReplyKey = replytReference.getRef().getKey();
        String theReplyCountKey = count(theReplyKey);
        replytReference.setValue(new ReplyModel(thePosterUID, thePost , thePostKey , thePosterNickName,
                thePosterHandle, thePosterCountry, whichRoom, theReplierUID,
                theReply, theReplierNickName, theReplierHandle,
                theReplierCountry, theReplyKey, theReplyCountKey,
                "" , "" , "" , false , returnTheDate() , returnTheTime()));

        CoreTheNotificationsList.NotificationForReplies(thePost , thePosterUID , thePosterNickName , thePosterHandle
                , theReplierUID  , thePostKey, theReplierNickName ,
                theReplierHandle , whichRoom  , theReply , theReplyKey , theReplyCountKey
                , audioStatus , picPath , audioPath);
    }

    private static String count(String replyKey){

        String countKey ;
        FirebaseDatabase countThePost = FirebaseDatabase.getInstance();
        DatabaseReference countReference = countThePost.getReference("countPost " + replyKey).push();
        countKey = countReference.getRef().getKey();
        countReference.setValue(new CountModel(countKey, 0 , 0 , 0, 0 ,0 ,0));

        return countKey ;
    }


    private static void replyTheReplier()
    {

    }
}
