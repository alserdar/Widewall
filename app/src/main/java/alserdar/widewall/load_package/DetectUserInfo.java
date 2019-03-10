package alserdar.widewall.load_package;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.models.UserModel;

public class DetectUserInfo {


    public static void infoForHome(final Context context , String theUID , final TextView homeUserName)
    {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    if (user.isSus())
                    {
                        new OurToast().myToast(context, context.getString(R.string.you_are_suspended));
                        ((Activity)context).finish();

                    }else
                    {
                        homeUserName.setText(user.getUserName());
                    }

                }
            }
        });
    }

    /*
     public static String handle (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("handle" , "handle");
    }


     public static String name (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("name" , "name");
    }
*/
    public static String id (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("user_id" , "user_id");
    }



    public static String email (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("email" , "email");
    }

    public static String phoneNumber (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("phone" , "phone");
    }

    public static String faceBookUser (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("FacebookUser" , "FacebookUser");
    }

    public static String theUID (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("uid" , "uid");
    }


    public static String country (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("currentCountry" , "currentCountry");
    }

    public static String calculateTheAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
