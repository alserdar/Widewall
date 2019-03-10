package alserdar.widewall.core;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import alserdar.widewall.models.CountModel;
import alserdar.widewall.models.PostModel;

import static alserdar.widewall.core.CoreThePost.returnTheTime;
import static alserdar.widewall.core.CoreThePost.returnTheDate;

public class CoreTheReplyFunctions {

    public static void countPost(final String thePostKey , String countKey ,
                                 final TextView countReplies , final TextView countAdds ,
                                 final TextView countLikes , final TextView countFavs ,
                                 final TextView countListeners) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference doc = db.getReference("countPost " + thePostKey).child(countKey);
        doc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    CountModel countModel = dataSnapshot.getValue(CountModel.class);
                    assert countModel != null;
                    if (countModel.getCountReplies() <= 0)
                    {
                        countReplies.setText("");
                    }else
                    {
                        countReplies.setText(wideFormat(countModel.getCountReplies()));
                    }
                    if (countModel.getCountAdds() <= 0 )
                    {
                        countAdds.setText("");
                    }else
                    {
                        countAdds.setText(wideFormat(countModel.getCountAdds()));
                    }

                    if (countModel.getCountLikes() <= 0)
                    {
                        countLikes.setText("");
                    }else
                    {
                        countLikes.setText(wideFormat(countModel.getCountLikes()));

                    }

                    if (countModel.getCountFavourites()<= 0)
                    {
                        countFavs.setText("");
                    }else
                    {
                        countFavs.setText(wideFormat(countModel.getCountFavourites()));

                    }

                    if (countModel.getCountListeners() <= 0)
                    {
                        countListeners.setText("");
                    }else
                    {
                        countListeners.setText(wideFormat(countModel.getCountListeners()));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String wideFormat(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return wideFormat(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + wideFormat(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


    // reply the post ============

    public static void replyMethod(final String theUID , final String mainKeyPost ,
                                   final String keyPost , final String countKey  , final String typeMessage ,
                                   final String myNickName , final String handle ,
                                   final String getTheRandom , final String audioStatus ) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference doc = db.getReference(theUID).child("replies").push();
        doc.setValue(new PostModel(theUID, typeMessage , myNickName,
                handle, "currentCountry", "PostCountry" , "Planet", audioStatus,
                "picPath", getTheRandom, mainKeyPost, keyPost , countKey,
                false,  returnTheDate() , returnTheTime(), "" , "" , "")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    setPlusAndMinusThePostReplies("increaseScore" , mainKeyPost , countKey);
                }
            }
        });

    }

    public static void deleteReply(final String theUID ,final String mainKey , final String postKey , final String countKey) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("posts").orderByChild("theKeyPost").equalTo(postKey);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    setPlusAndMinusThePostReplies("decreaseScore" , mainKey , countKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void setPlusAndMinusThePostReplies(final String operation  , final String mainKey , final  String countKeyPost) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("countPost "+mainKey);
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {


                for (MutableData md : mutableData.getChildren())
                {
                    CountModel countModel = md.getValue(CountModel.class);
                    if (operation.equals("increaseScore")) {
                        mutableData.child(countKeyPost).child("countReplies").setValue(countModel.getCountReplies() + 1);
                    } else if (operation.equals("decreaseScore")){
                        mutableData.child(countKeyPost).child("countReplies").setValue(countModel.getCountReplies() - 1);
                    }
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
        });
    }







    // repost or add posts =========================
    public static void addOrNotMethod(String theUID , final String keyPost , final ImageView add, final ImageView unAdd) {


        DatabaseReference addOrNot = FirebaseDatabase.getInstance().getReference(theUID).child("posts");
        addOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {

                        if (ds.child("addKey").getValue().equals(keyPost))
                        {
                            add.setVisibility(View.GONE);
                            unAdd.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void addMethod(final String theUID ,final String keyPost , final String countKey,
                                 final ImageView add , final ImageView unAdd , final String typeMessage ,
                                 final String myNickName , final String handle) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference doc = db.getReference(theUID).child("posts").push();
        doc.setValue(new PostModel(theUID, typeMessage , myNickName,
                handle, "currentCountry", "PostCountry" , "Planet", "",
                "picPath", "", "", keyPost , countKey,
                false,  returnTheDate() , returnTheTime(), keyPost , "" , "")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    add.setVisibility(View.GONE);
                    unAdd.setVisibility(View.VISIBLE);
                    setPlusAndMinusThePostADDS("increaseScore" , countKey , keyPost);
                }
            }
        });

    }

    public static void unAddMehod(final String theUID , final String postKey , final String countKey , final ImageView unAdd , final ImageView add) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("posts").orderByChild("theKeyPost").equalTo(postKey);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    unAdd.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                    setPlusAndMinusThePostADDS("decreaseScore" , countKey , postKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static void setPlusAndMinusThePostADDS(final String operation , final  String countKeyPost ,final String mainKey) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("countPost " +mainKey);
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                for (MutableData md : mutableData.getChildren())
                {
                    CountModel countModel = md.getValue(CountModel.class);
                    if (operation.equals("increaseScore")) {
                        mutableData.child(countKeyPost).child("countAdds").setValue(countModel.getCountAdds() + 1);
                    } else if (operation.equals("decreaseScore")){
                        mutableData.child(countKeyPost).child("countAdds").setValue(countModel.getCountAdds() - 1);
                    }
                }


                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
        });
    }


    //=========== likes
    public static void likesOrNotMethod(String theUID, final String keyPost, final ImageView like, final ImageView unLike) {

        DatabaseReference likesOrNot = FirebaseDatabase.getInstance().getReference(theUID).child("likes");
        likesOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (ds.child("likeKey").getValue().equals(keyPost))
                        {

                            like.setVisibility(View.GONE);
                            unLike.setVisibility(View.VISIBLE);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void likeMethod(final String theUID  ,
                                  final String keyPost , final String countKey , final ImageView like ,
                                  final ImageView unlike , final String typeMessage ,
                                  final String myNickName , final String handle) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference doc = db.getReference(theUID).child("likes").push();
        doc.setValue(new PostModel(theUID, typeMessage , myNickName,
                handle, "currentCountry", "PostCountry" , "Planet", "",
                "picPath", "", "", keyPost , countKey,
                false,  returnTheDate() , returnTheTime()  , "" , keyPost , "")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    like.setVisibility(View.GONE);
                    unlike.setVisibility(View.VISIBLE);
                    setPlusAndMinusThePostLikes("increaseScore" , countKey , keyPost);
                }
            }
        });

    }

    public static void unLikeMehod(final String theUID , final String postKey , final String countKey ,
                                   final ImageView unlike , final ImageView like) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("likes").orderByChild("theKeyPost").equalTo(postKey);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    unlike.setVisibility(View.GONE);
                    like.setVisibility(View.VISIBLE);
                    setPlusAndMinusThePostLikes("decreaseScore" , countKey , postKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static void setPlusAndMinusThePostLikes(final String operation , final  String countKeyPost , final String keyPost) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("countPost " +keyPost);
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {


                for (MutableData md : mutableData.getChildren())
                {
                    CountModel countModel = md.getValue(CountModel.class);

                    if (operation.equals("increaseScore")) {
                        mutableData.child(countKeyPost).child("countLikes").setValue(countModel.getCountAdds() + 1);
                    } else if (operation.equals("decreaseScore")){
                        mutableData.child(countKeyPost).child("countLikes").setValue(countModel.getCountAdds() - 1);
                    }
                }



                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
        });
    }

    //favs ===========


    public static void favsOrNotMethod(String theUID, final String keyPost, final ImageView fav, final ImageView unFav) {

        DatabaseReference favOrNot = FirebaseDatabase.getInstance().getReference(theUID).child("fav");
        favOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        if (ds.child("favouriteKey").getValue().equals(keyPost))
                        {

                            fav.setVisibility(View.GONE);
                            unFav.setVisibility(View.VISIBLE);

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public static void favMethod(final String theUID , final String keyPost ,
                                 final String countKey , final ImageView fav ,
                                 final ImageView unFav , final String typeMessage ,
                                 final String myNickName , final String handle) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference doc = db.getReference(theUID).child("fav").push();
        doc.setValue(new PostModel(theUID, typeMessage , myNickName,
                handle, "currentCountry", "PostCountry" , "Planet", "",
                "picPath", "", "", keyPost , countKey,
                false,  returnTheDate() , returnTheTime() , "" , "" , keyPost)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete())
                {
                    fav.setVisibility(View.GONE);
                    unFav.setVisibility(View.VISIBLE);
                    setPlusAndMinusThePostFav("increaseScore" , countKey , keyPost);
                }
            }
        });

    }

    public static void unFavMehod(final String theUID , final String postKey , final String countKey ,
                                  final ImageView unFav , final ImageView fav) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(theUID).child("fav").orderByChild("theKeyPost").equalTo(postKey);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    unFav.setVisibility(View.GONE);
                    fav.setVisibility(View.VISIBLE);
                    setPlusAndMinusThePostFav("decreaseScore" , countKey , postKey);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private static void setPlusAndMinusThePostFav(final String operation , final  String countKeyPost , final String keyPost) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("countPost " +keyPost);
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                for (MutableData md : mutableData.getChildren())
                {
                    CountModel countModel = md.getValue(CountModel.class);


                    if (operation.equals("increaseScore")) {
                        mutableData.child(countKeyPost).child("countFavourites").setValue(countModel.getCountAdds() + 1);
                    } else if (operation.equals("decreaseScore")){
                        mutableData.child(countKeyPost).child("countFavourites").setValue(countModel.getCountAdds() - 1);
                    }
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
        });
    }
}
