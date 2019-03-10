package alserdar.widewall;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import alserdar.widewall.check_internet.NetworkUtil;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.login_package.GetCountryBySimCard;
import alserdar.widewall.login_package.NewUser;
import alserdar.widewall.models.UserModel;

public class Launcher extends AppCompatActivity {

    FirebaseFunctions mFunctions ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        final LinearLayout avlLay = findViewById(R.id.aviLay);

/*
 startService(new Intent(Launcher.this , FirebaseBackgroundServiceForMessages.class));
        startService(new Intent(Launcher.this , FirebaseBackgroundServiceForNotifications.class));

 */



        FirebaseDatabase.getInstance().getReference().keepSynced(true);

        /*
          mFunctions = FirebaseFunctions.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("Messages")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "sub";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Toast.makeText(Launcher.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
         */


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Launcher.this,
                new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);

            }
        });

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.getMessage();
                } finally {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (NetworkUtil.getConnectivityStatusString(getBaseContext()).equals("Internet enabled")) {
                                boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                                boolean signIn = GoogleSignIn.getLastSignedInAccount(Launcher.this) != null ;
                                if (loggedIn || signIn) {
                                    AVLoadingIndicatorView view = new AVLoadingIndicatorView(getBaseContext());
                                    avlLay.setVisibility(View.VISIBLE);
                                    view.show();

                                    String theUID = DetectUserInfo.theUID(getBaseContext());
                                    if (theUID == null) {
                                        Intent i = new Intent(getBaseContext(), GetCountryBySimCard.class);
                                        startActivity(i);
                                    } else {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        DocumentReference doc = db.collection("UserInformation").document(theUID);
                                        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    UserModel user = documentSnapshot.toObject(UserModel.class);
                                                    String handle = Objects.requireNonNull(user).getHandle();
                                                    if (handle.isEmpty()) {
                                                        Intent i = new Intent(getBaseContext(), NewUser.class);
                                                        startActivity(i);
                                                    } else {
                                                        Intent i = new Intent(getBaseContext(), Home.class);
                                                        startActivity(i);
                                                    }
                                                }else
                                                {
                                                    Intent i = new Intent(getBaseContext(), NewUser.class);
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                    }


                                } else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent i = new Intent(getBaseContext(), GetCountryBySimCard.class);
                                            startActivity(i);
                                        }
                                    }).start();

                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                            Looper.prepare();
                                        new OurToast().myToast(getBaseContext(), getString(R.string.check_internet));
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        };
        timer.start();
    }

    private Task<String> addMessage(String text) {
        // Create the arguments to the callable function.
        Map<String, Object> data = new HashMap<>();
        data.put("text", text);
        data.put("push", true);

        return mFunctions
                .getHttpsCallable("pushNotificationNewFollower")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.
                        String result = (String) task.getResult().getData();
                        return result;
                    }
                });
    }
}
