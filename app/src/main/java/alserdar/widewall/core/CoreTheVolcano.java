package alserdar.widewall.core;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import alserdar.widewall.models.CountModel;

public class CoreTheVolcano {

    public static void addTheVolcano(final String theUID , final String whichRoom , final String country,
                                     final String thePost , final String nickName , final String handle ,
                                     final String audioStatus  , final String keyPost)
    {
        Map<String , Object> freshUser = new HashMap<>();
        freshUser.put("theUID" , theUID); // this will remove too
        freshUser.put("whichRoom" , whichRoom);
        freshUser.put("country" , country);
        freshUser.put("thePost" , thePost);
        freshUser.put("nickName" , nickName);
        freshUser.put("handle" , handle);
        freshUser.put("audioStatus" , audioStatus);
        freshUser.put("keyPost" , keyPost);


        FirebaseFirestore dbVolcano= FirebaseFirestore.getInstance();
        dbVolcano.collection("Volcano").document(keyPost).set(freshUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                coreTheHappiness(keyPost);

            }
        });
    }

    private static void coreTheHappiness(final String mainKey) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("countPost " +mainKey);
        ref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {


                for (MutableData md : mutableData.getChildren())
                {
                    CountModel countModel = md.getValue(CountModel.class);

                    int countReposts = countModel.getCountLikes();
                    int countLikers = countModel.getCountLikes();
                    int countFaves = countModel.getCountFavourites();
                    int countListeners = countModel.getCountListeners();

                    int Happiness = countReposts + countLikers + countFaves + countListeners;
                    FirebaseFirestore dbVolcano= FirebaseFirestore.getInstance();
                    dbVolcano.collection("Volcano").document(mainKey).update("happiness" , Happiness);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {}
        });
    }

}
