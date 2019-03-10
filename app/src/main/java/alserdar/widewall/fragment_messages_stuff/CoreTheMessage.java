package alserdar.widewall.fragment_messages_stuff;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import alserdar.widewall.core.CoreTheNotificationsList;
import alserdar.widewall.models.MessagesListModel;
import alserdar.widewall.models.MessagesModel;

import static alserdar.widewall.core.CoreThePost.returnTheTime;
import static alserdar.widewall.core.CoreThePost.returnTheDate;

class CoreTheMessage {

    static void letUsChat(final String theUID, final String id, final String nickName,
                          final String handle, final String country, final String hisUID,
                          final String hisId, final String hisNickName, final String hisHandle,
                          final String hisCountry, String theMessage , String picPath , String picName)
    {
        //  final String thePlus = plusTheId(id, hisId);


        String first = id.substring(0,8);
        String second = hisId.substring(0,8);


        Long sumTheId = Long.parseLong(first) + Long.parseLong(second);


        FirebaseDatabase dbMessaging = FirebaseDatabase.getInstance();
        final DatabaseReference docMessaging = dbMessaging.getReference(sumTheId + " privateChat").push();
        docMessaging.setValue(new MessagesModel(theUID,  nickName,  handle,
                country,  hisUID,  hisNickName,
                hisHandle,  hisCountry, theMessage , picPath , picName,
                returnTheDate() , returnTheTime())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {

                    killUs(theUID , id , nickName , handle , country ,
                            hisUID ,  hisId , hisNickName , hisHandle , hisCountry);
                    rebornUs(theUID , id , nickName , handle , country ,
                            hisUID ,  hisId , hisNickName , hisHandle , hisCountry);

                    CoreTheNotificationsList.setGotMessageAsTrueOrFalse(hisUID , true);

                }else
                {

                }
            }
        });
    }

    private static void letHimDieAndRebornAgainPlease(final String theUID , final String hisUID , final String hisId ,
                                                      final String hisNickName ,
                                                      final String hisHandle , final String  hisCountry)
    {
        DatabaseReference hisRef = FirebaseDatabase.getInstance().getReference();
        Query hisApplesQuery = hisRef.child(hisUID).child("messages").orderByChild("lastUIDTalkedTo").equalTo(theUID);
        hisApplesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            FirebaseDatabase myDbMessageList = FirebaseDatabase.getInstance();
                            final DatabaseReference myDocMessageList = myDbMessageList.getReference(theUID).child("messages").push();
                            myDocMessageList.setValue(new MessagesListModel(hisUID , hisId , hisNickName , hisHandle , hisCountry , returnTheDate() ,
                                    returnTheTime(), hisUID ,  true ));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private static void letMeDieAndRebornAgainPlease(final String theUID , final String id , final String nickName , final String handle , final String  country ,
                                                     final String hisUID)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("messages").orderByChild("lastUIDTalkedTo").equalTo(hisUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            FirebaseDatabase hisDbMessageList = FirebaseDatabase.getInstance();
                            final DatabaseReference hisDocMessageList = hisDbMessageList.getReference(hisUID).child("messages").push();
                            hisDocMessageList.setValue(new MessagesListModel(theUID , id , nickName , handle , country , returnTheDate() ,
                                    returnTheTime() , theUID , false));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private static void rebornUs (final String theUID , final String id , final String nickName , final String handle , final String  country ,
                                  final String hisUID , final String hisId ,
                                  final String hisNickName ,
                                  final String hisHandle , final String  hisCountry)
    {
        FirebaseDatabase hisDbMessageList = FirebaseDatabase.getInstance();
        final DatabaseReference hisDocMessageList = hisDbMessageList.getReference(hisUID).child("messages").push();
        hisDocMessageList.setValue(new MessagesListModel(theUID , id , nickName , handle , country , returnTheDate() ,
                returnTheTime() , theUID , false));

        FirebaseDatabase myDbMessageList = FirebaseDatabase.getInstance();
        final DatabaseReference myDocMessageList = myDbMessageList.getReference(theUID).child("messages").push();
        myDocMessageList.setValue(new MessagesListModel(hisUID , hisId , hisNickName , hisHandle , hisCountry , returnTheDate() ,
                returnTheTime(), hisUID ,  true ));
    }


    private static void killUs(final String theUID , final String id , final String nickName , final String handle , final String  country ,
                               final String hisUID  , final String hisId ,
                               final String hisNickName ,
                               final String hisHandle , final String  hisCountry)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("messages").orderByChild("lastUIDTalkedTo").equalTo(hisUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference hisRef = FirebaseDatabase.getInstance().getReference();
        Query hisApplesQuery = hisRef.child(hisUID).child("messages").orderByChild("lastUIDTalkedTo").equalTo(theUID);
        hisApplesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
