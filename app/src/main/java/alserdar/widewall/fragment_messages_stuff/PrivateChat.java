package alserdar.widewall.fragment_messages_stuff;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Objects;
import java.util.Random;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.core.CoreTheNotificationsList;
import alserdar.widewall.core.Core_Following_Followers_Block_Chat;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPicFullScreenIPrivateChat;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.MessagesModel;
import alserdar.widewall.models.UserModel;

public class PrivateChat extends AppCompatActivity {

    String theUID , hisUID , id , hisId ;
    private int STORAGE_PERMISSION_CODE = 25;
    Uri filePath;
    private ImageView pickThePicture;
    private long sumTheId;
    LinearLayout theDeletePictureLay , theMessageLay , sendTextLay , sendPicLay;
    private EditText theMessage;
    private String createPicName = CreateRandomAudioFileName(6);
    private boolean isImageFitToScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        theMessage = findViewById(R.id.typedMessageInPrivateChat);
        final Button sendTheMessage = findViewById(R.id.sendTheMessageButtonInPrivateChat);
        final Button sendThePicture = findViewById(R.id.sendThePictureButtonInPrivateChat);
        pickThePicture = findViewById(R.id.pickThePictureButtonInPrivateChat);

        theMessageLay = findViewById(R.id.typedMessageInPrivateChatLayout);
        theDeletePictureLay = findViewById(R.id.deleteThePicLayout);
        sendTextLay = findViewById(R.id.sendTextLay);
        sendPicLay = findViewById(R.id.sendPicLay);
        Button deleteThePic = findViewById(R.id.deleteThePicButton);


        final ImageView profilePic = findViewById(R.id.profilePicInPrivateChat);
        final TextView hisNickName = findViewById(R.id.hisNickNameInPrivateChat);
        final TextView hisHandle = findViewById(R.id.hisHandleInPrivateChat);


        final TextView myNickName = findViewById(R.id.myNickNameInPrivateChat);
        final TextView myHandle = findViewById(R.id.myHandleInPrivateChat);

        final TextView myCountry = findViewById(R.id.myCountryInPrivateChat);
        final TextView hisCountry = findViewById(R.id.hisCountryInPrivateChat);

        ImageView pressBack = findViewById(R.id.press_back);
        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                Intent i = new Intent(PrivateChat.this , Home.class);
                startActivity(i);
                finish();
                 */

                PrivateChat.super.onBackPressed();

            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DetectUserInfoFromFirebase.clickTheUser(PrivateChat.this , theUID , hisUID);

            }
        });


        pickThePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isReadStorageAllowed()) {

                    Crop.pickImage(PrivateChat.this);

                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        });

        theUID = DetectUserInfo.theUID(PrivateChat.this);
        id = DetectUserInfo.id(PrivateChat.this);

        hisUID = Objects.requireNonNull(getIntent().getExtras()).getString("hisUID");
        hisId = getIntent().getExtras().getString("hisId");


        Core_Following_Followers_Block_Chat.amIBlockedOrNotForSendingMessages(theUID , hisUID ,theMessage ,  sendTheMessage);
        CoreTheNotificationsList.setGotMessageAsTrueOrFalse(theUID , false);
        String first = id.substring(0,8);
        String second = hisId.substring(0,8);


        sumTheId = Long.parseLong(first) + Long.parseLong(second);


        deleteThePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filePath = null;
                pickThePicture.setImageResource(R.mipmap.gallery);
                theDeletePictureLay.setVisibility(View.GONE);
                theMessageLay.setVisibility(View.VISIBLE);
                sendPicLay.setVisibility(View.GONE);
                sendTextLay.setVisibility(View.VISIBLE);
            }
        });

        FirebaseFirestore hisDb = FirebaseFirestore.getInstance();
        DocumentReference hisDoc = hisDb.collection("UserInformation").document(hisUID);
        hisDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    assert user != null;
                    LoadPictures.loadPicsForHome(PrivateChat.this , user.getTheUID() , profilePic);
                    hisNickName.setText(user.getUserName());
                    hisHandle.setText(user.getHandle());
                    hisCountry.setText(user.getCountry());
                }
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    assert user != null;
                    LoadPictures.loadPicsForHome(PrivateChat.this , user.getTheUID() , profilePic);
                    myNickName.setText(user.getUserName());
                    myHandle.setText(user.getHandle());
                    myCountry.setText(user.getCountry());
                }
            }
        });

        ListView ourChat = findViewById(R.id.ourChatList);

        FirebaseListAdapter<MessagesModel> adapter =
                new FirebaseListAdapter<MessagesModel>(PrivateChat.this, MessagesModel.class, R.layout.list_of_our_private_chat,
                        FirebaseDatabase.getInstance().getReference(sumTheId + " privateChat").limitToLast(100)) {
                    @Override
                    protected void populateView(final View v, final MessagesModel model, final int position) {

                        final TextView chat = v.findViewById(R.id.thePrivateMessage);
                        final ImageView chatPics = v.findViewById(R.id.thePrivatePicture);


                        if (model.getTheUID().equals(theUID))
                        {
                            if (model.getTheMessage().equals(""))
                            {
                                chat.setVisibility(View.GONE);
                            }else
                            {
                                chat.setBackgroundResource(R.drawable.bubble_me);
                                chat.setGravity(Gravity.CENTER | Gravity.START);
                            }

                        }else
                        {

                            if (model.getTheMessage().equals(""))
                            {
                                chat.setVisibility(View.GONE);
                            }else
                            {
                                chat.setBackgroundResource(R.drawable.bubble_him);
                                chat.setGravity(Gravity.CENTER | Gravity.END);
                            }

                        }


                        LoadPictures.loadPicturesInPrivateChat(getBaseContext() , sumTheId , chatPics , model.getPicName());
                        chatPics.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent i = new Intent(getBaseContext() , LoadPicFullScreenIPrivateChat.class);
                                i.putExtra("theSum" , sumTheId);
                                i.putExtra("picName" , model.getPicName());
                                startActivity(i);
                            }
                        });
                        chat.setText(model.getTheMessage());
                    }
                };

        ourChat.setAdapter(adapter);


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (theMessage.getText().toString().equals(""))
                {
                    sendTheMessage.setEnabled(false);
                }else
                {
                    sendTheMessage.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        theMessage.addTextChangedListener(watcher);


        sendThePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

                CoreTheMessage.letUsChat(theUID  ,id , myNickName.getText().toString() ,
                        myHandle.getText().toString() , myCountry.getText().toString() , hisUID , hisId  , hisNickName.getText().toString() ,
                        hisHandle.getText().toString() , hisCountry.getText().toString(),
                        theMessage.getText().toString() , String.valueOf(filePath) , createPicName);


                theDeletePictureLay.setVisibility(View.GONE);
                theMessageLay.setVisibility(View.VISIBLE);
                sendPicLay.setVisibility(View.GONE);
                sendTextLay.setVisibility(View.VISIBLE);

                pickThePicture.setImageResource(R.mipmap.gallery);
            }
        });

        sendTheMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CoreTheMessage.letUsChat(theUID  ,id , myNickName.getText().toString() ,
                        myHandle.getText().toString() , myCountry.getText().toString() , hisUID , hisId  , hisNickName.getText().toString() ,
                        hisHandle.getText().toString() , hisCountry.getText().toString(),
                        theMessage.getText().toString() , "" , "");


                /*
                 Core_Following_Followers_Block_Chat.detectIfWeTalkBeforeOrNo(theUID  ,id , myNickName.getText().toString() ,
                        myHandle.getText().toString() , myCountry.getText().toString() , hisUID , hisId  , hisNickName.getText().toString() ,
                        hisHandle.getText().toString() , hisCountry.getText().toString());

                 */

                theMessage.setText("");

            }
        });
    }

    public static String CreateRandomAudioFileName(int string) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            String randomAudioFileName = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            stringBuilder.append(randomAudioFileName.
                    charAt(random.nextInt(randomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString().toLowerCase();
    }

    private void uploadImage() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
      //  final FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(filePath != null)
        {
            StorageReference ref = storageReference.child(sumTheId + " privateChat");
            StorageReference mountains = ref.child(createPicName);
            mountains.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    /*
                       WriteBatch batch = db.batch();
                    DocumentReference nycRef = db.collection("UserInformation").document(theUID);
                    batch.update(nycRef, "hasPic" , true);
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            new OurToast().myToast(getBaseContext() ,  getString(R.string.picture_uploaded));
                            avi.setVisibility(View.GONE);
                            save.setVisibility(View.VISIBLE);
                        }
                    });
                     */

                    new OurToast().myToastPic(getBaseContext() , " " , R.mipmap.done);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new OurToast().myToast(getBaseContext() ,  getString(R.string.failed));
                            new OurToast().myToast(getBaseContext() , e.getMessage());

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                new OurToast().myToast(getBaseContext() ,  getString(R.string.permission_granted));
            }else{
                //Displaying another toast if permission is not granted
                new OurToast().myToast(getBaseContext() ,  getString(R.string.permission_denied));
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
        filePath = destination;
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            pickThePicture.setImageURI(Crop.getOutput(result));
            theDeletePictureLay.setVisibility(View.VISIBLE);
            theMessageLay.setVisibility(View.GONE);
            sendPicLay.setVisibility(View.VISIBLE);
            sendTextLay.setVisibility(View.GONE);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*
     @Override
    public void onBackPressed() {
        Intent i = new Intent(PrivateChat.this , Home.class);
        startActivity(i);
        finish();
    }
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
