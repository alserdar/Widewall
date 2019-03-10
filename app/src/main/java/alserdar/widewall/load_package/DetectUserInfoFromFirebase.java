package alserdar.widewall.load_package;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.widewall.models.UserModel;
import alserdar.widewall.nav_stuff.MyProfile;
import alserdar.widewall.user_stuff.UserProfile;

public class DetectUserInfoFromFirebase {


    public static void UserProfileInformationCollectInfo(final String hisUID ,
                                                         final TextView  userProfileNickName ,
                                                         final TextView  userProfileHandle ,
                                                         final TextView  userProfileInfo ,
                                                         final TextView  userProfileLink)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(hisUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    UserModel model = documentSnapshot.toObject(UserModel.class);
                    assert model != null;
                    userProfileNickName.setText(model.getUserName());
                    userProfileHandle.setText(String.format("@%s", model.getHandle()));
                    userProfileInfo.setText(model.getAboutUser());
                    userProfileLink.setText(model.getLinkUser());
                }

            }
        });
    }

    public static void UserProfileInformationGoToProfileSettings(final Context context , final ImageView myProfilePic,
                                                                 final EditText  myProfileNickName ,
                                                                 final TextView  myProfileHandle ,
                                                                 final EditText  myProfileInfo ,
                                                                 final EditText  myProfileLink ,
                                                                 final EditText myBirthDate)
    {
        final String theUID = DetectUserInfo.theUID(context);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    UserModel model = documentSnapshot.toObject(UserModel.class);
                    LoadPictures.loadPicsForHome(context , theUID , myProfilePic);
                    assert model != null;
                    myProfileNickName.setText(model.getUserName());
                    myProfileHandle.setText(model.getHandle());
                    myProfileInfo.setText(model.getAboutUser());
                    myProfileLink.setText(model.getLinkUser());
                    myBirthDate.setText(model.getBirthDate());
                }

            }
        });
    }

    public static void userHandle(Context context , final TextView userName , final TextView handle)
    {
        String theUID = DetectUserInfo.theUID(context);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    UserModel model = documentSnapshot.toObject(UserModel.class);
                    assert model != null;
                    userName.setText(model.getUserName());
                    handle.setText(String.format("@%s", model.getHandle()));
                }

            }
        });
    }


    public static void emptyListOrNot(String theUID , String theList , final LinearLayout theLay )
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(theUID).child(theList);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {

                }else
                {
                    theLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public static void clickTheUser(final Context context , String theUID , String hisUID)
    {
        if (theUID.equals(hisUID))
        {
            Intent i = new Intent(context , MyProfile.class);
            context.startActivity(i);
        }else
        {

            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference doc = db.collection("UserInformation").document(hisUID);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.exists())
                    {
                        UserModel model = documentSnapshot.toObject(UserModel.class);
                        Intent i = new Intent(context , UserProfile.class);
                        i.putExtra("hisName" , model.getUserName());
                        i.putExtra("hisHandle" , model.getHandle());
                        i.putExtra("hisUID" , model.getTheUID());
                        i.putExtra("hisId" , model.getId());
                        i.putExtra("hisCountry" , model.getCountry());
                        i.putExtra("hisInfo" , model.getAboutUser());
                        i.putExtra("hisLink" , model.getLinkUser());
                        context.startActivity(i);
                    }
                }
            });

        }
    }
}