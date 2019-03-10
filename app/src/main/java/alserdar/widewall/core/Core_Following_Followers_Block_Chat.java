package alserdar.widewall.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.models.UserModel;

public class Core_Following_Followers_Block_Chat {

    public static void followMethod(final Context context , final String theUID , final String hisUID ,final FloatingActionButton follow , final FloatingActionButton unFollow)
    {
        HashMap<String , String> mapFollowing = new HashMap<>();
        mapFollowing.put("FollowingDude" , hisUID);

        FirebaseDatabase dbFollowing = FirebaseDatabase.getInstance();
        final DatabaseReference docFollowing = dbFollowing.getReference(theUID).child("Following").push();
        docFollowing.setValue(mapFollowing).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    new OurToast().myToastPic(context , "" , R.mipmap.winged_heart);
                    follow.setVisibility(View.GONE);
                    unFollow.setVisibility(View.VISIBLE);
                }
            }
        });


        HashMap<String , String> mapFollowers = new HashMap<>();
        mapFollowers.put("FollowersDude" , theUID);

        FirebaseDatabase dbFollowers = FirebaseDatabase.getInstance();
        final DatabaseReference docFollowers = dbFollowers.getReference(hisUID).child("Followers").push();
        docFollowers.setValue(mapFollowers).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists())
                            {
                                UserModel model = documentSnapshot.toObject(UserModel.class);
                                CoreTheNotificationsList.NotificationForFollowers(hisUID , "" ,
                                        "" , "" , "" , "" ,
                                        model.getTheUID() , model.getUserName() , model.getHandle() ,
                                        "" , "" , "");
                            }

                        }
                    });
                }
            }
        });
    }

    public static void unFollowMethod(String theUID , final String hisUID , final FloatingActionButton follow , final FloatingActionButton unFollow)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("Following").orderByChild("FollowingDude").equalTo(hisUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    follow.setVisibility(View.VISIBLE);
                    unFollow.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void block(final Context context , final String theUID , final String hisUID)
    {
        HashMap<String , String> map = new HashMap<>();
        map.put("BlockedDude" , hisUID);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference doc = db.getReference(theUID).child("Blocked").push();
        doc.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    unFollowHimWhenBlock(theUID , hisUID);
                    unFollowMeWhenBlock(theUID , hisUID);
                    new OurToast().myToastPic(context , "" , R.mipmap.block);
                }
            }
        });
    }

    private static void unFollowHimWhenBlock(String theUID , String hisUID)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("Following").orderByChild("FollowingDude").equalTo(hisUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private static void unFollowMeWhenBlock(String theUID , String hisUID)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(hisUID).child("Followers").orderByChild("FollowersDude").equalTo(theUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public static void unBlock(String theUID , String hisUID) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("Blocked").orderByChild("BlockedDude").equalTo(hisUID);

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
    }

    public static void amIFollowHimOrNot(String theUID , final String hisUID , final ImageView follow , final ImageView unFollow)
    {

        DatabaseReference followOrNot = FirebaseDatabase.getInstance().getReference(theUID).child("Following");
        followOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        if (ds.child("FollowingDude").getValue().equals(hisUID))
                        {
                            follow.setVisibility(View.GONE);
                            unFollow.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void isHeFollowMeOrNot(final String theUID , final String hisUID ,
                                         final TextView yupHeIsFollowingMe)
    {

        DatabaseReference followOrNot = FirebaseDatabase.getInstance().getReference(theUID).child("Followers");
        followOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        if (ds.child("FollowersDude").getValue().equals(hisUID))
                        {
                            yupHeIsFollowingMe.setText(R.string.follows_you);
                            yupHeIsFollowingMe.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.winged_heart, 0);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void amIBlockedOrNot(final String theUID , String hisUID , final ImageView blocked ,
                                       final ImageView follow , final ImageView chat )
    {
        DatabaseReference blockedOrNot = FirebaseDatabase.getInstance().getReference(hisUID).child("Blocked");
        blockedOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        if (ds.child("BlockedDude").getValue().equals(theUID))
                        {
                            follow.setVisibility(View.GONE);
                            chat.setVisibility(View.GONE);
                            blocked.setVisibility(View.VISIBLE);
                            blocked.setImageResource(R.mipmap.ghost);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void amIBlockedOrNotForSendingMessages(final String theUID , String hisUID , final EditText message ,
                                       final Button sendMessage)
    {
        DatabaseReference blockedOrNot = FirebaseDatabase.getInstance().getReference(hisUID).child("Blocked");
        blockedOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        if (ds.child("BlockedDude").getValue().equals(theUID))
                        {
                            message.setVisibility(View.GONE);
                            sendMessage.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getFollowingAndFollowers(final Context context , final TextView following ,
                                                final TextView followers)
    {
        final String theUID = DetectUserInfo.theUID(context);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference docFollowing = db.getReference(theUID).child("Following");
        docFollowing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        following.setText(context.getString(R.string.following) + " " + ds.getChildrenCount());
                    }
                }else
                {
                    following.setText(context.getString(R.string.following));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference docFollowers = db.getReference(theUID).child("Followers");
        docFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        followers.setText(context.getString(R.string.followers) + " " + ds.getChildrenCount());
                    }
                }else
                {
                    followers.setText(context.getString(R.string.followers));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
