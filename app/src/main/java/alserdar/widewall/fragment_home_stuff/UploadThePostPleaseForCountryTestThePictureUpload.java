package alserdar.widewall.fragment_home_stuff;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Random;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.animate.RotateImage;
import alserdar.widewall.core.CoreThePost;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UploadThePostPleaseForCountryTestThePictureUpload extends AppCompatActivity {

    LinearLayout listViewLay;
    LinearLayout messageLay, audioStuffLay, theSpace, startRecordLay, replayRecordLay, stopRecordLay;
    EditText typeMessage;
    Button thePost, recordAudio, replayRecord, stopRecord , deleteThePickupImage;
    String theUID , currentCountry , postCountry , whoSentYou;
    TextView myNickName , handle;
    ProgressBar progressAudio;
    public static final int RequestPermissionCode = 73;
    private String audioStatus = "";
    private int STORAGE_PERMISSION_CODE = 25;
    Uri filePath;
    ImageView loadedImage, pickThePhoto;
    private String createPicName = CreateRandomAudioFileName(6);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_the_post_for_country_test_the_picture);


        myNickName = findViewById(R.id.userName);
        handle = findViewById(R.id.handle);
        listViewLay = findViewById(R.id.listViewLayout);
        audioStuffLay = findViewById(R.id.audioStuffLay);
        theSpace = findViewById(R.id.spaceOfModes);
        startRecordLay = findViewById(R.id.startRecordLay);
        replayRecordLay = findViewById(R.id.replayRecordLay);
        stopRecordLay = findViewById(R.id.stopRecordLay);
        thePost = findViewById(R.id.postButton);
        recordAudio = findViewById(R.id.recordAudio);
        replayRecord = findViewById(R.id.replayAudio);
        stopRecord = findViewById(R.id.stopAudio);
        progressAudio = findViewById(R.id.progressTheAudio);
        messageLay = findViewById(R.id.typeMessageLay);
        typeMessage = findViewById(R.id.typeMessage);


        pickThePhoto = findViewById(R.id.startPickupPhotosButton);
        loadedImage = findViewById(R.id.pickedUpImageView);
        deleteThePickupImage = findViewById(R.id.deleteThePickUpImage);

        whoSentYou = this.getIntent().getExtras().getString("whoSentYou");

        if (checkAudioPermission()) {

        } else {
            requestPermission();
        }


        theUID = DetectUserInfo.theUID(getBaseContext());
        DetectUserInfoFromFirebase.userHandle(getBaseContext() , myNickName , handle);
        currentCountry = DetectUserInfo.country(getBaseContext());
        postCountry = DetectUserInfo.country(getBaseContext());

        ImageView pressBack = findViewById(R.id.press_back);
        ImageView profilePic = findViewById(R.id.profilePicInToolBar);
        final ImageView topList = findViewById(R.id.getTheTopOfList);


        LoadPictures.setCountryFlagByNameCountry(topList , currentCountry);
        LoadPictures.loadPicsForHome(UploadThePostPleaseForCountryTestThePictureUpload.this , theUID , profilePic);

        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(UploadThePostPleaseForCountryTestThePictureUpload.this , RoomCountry.class);
                startActivity(i);
                finish();
            }
        });


        pickThePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isReadStorageAllowed()) {

                    Crop.pickImage(UploadThePostPleaseForCountryTestThePictureUpload.this);

                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        });

        deleteThePickupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteThePickupImage.setVisibility(View.GONE);
                pickThePhoto.setVisibility(View.VISIBLE);
                loadedImage.setImageResource(android.R.color.transparent);
            }
        });

        thePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RotateImage.startRotateMyButton(thePost);
                if (audioStatus.trim().equals("voiceNotes")) {
                    CoreThePost.stopToUploadImmediately();
                    CoreThePost.uploadTheRecord(UploadThePostPleaseForCountryTestThePictureUpload.this , theUID);
                } else {

                }

                if (filePath != null)
                {
                    uploadImage(currentCountry);
                    CoreThePost.thePost(theUID , whoSentYou , postCountry,
                            currentCountry , typeMessage.getText().toString() , myNickName.getText().toString() , handle.getText().toString() ,
                            audioStatus , createPicName);
                }else
                {
                    CoreThePost.thePost(theUID , whoSentYou , postCountry,
                            currentCountry , typeMessage.getText().toString() , myNickName.getText().toString() , handle.getText().toString() ,
                            audioStatus , "NoPicHere");
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent i = new Intent(UploadThePostPleaseForCountryTestThePictureUpload.this , RoomCountry.class);
                        startActivity(i);
                        finish();
                    }
                }, 3000);
            }
        });

        recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkAudioPermission()) {

                    audioStatus = "voiceNotes";
                    CoreThePost.MediaRecorderReady();

                    new OurToast().myToastPic(getBaseContext(), "" , R.mipmap.microphone);
                } else {
                    requestPermission();
                }

                startRecordLay.setVisibility(View.GONE);
                stopRecordLay.setVisibility(View.VISIBLE);
                replayRecordLay.setVisibility(View.GONE);

                CoreThePost.progressTheRecord(progressAudio);
            }
        });


        stopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopRecordLay.setVisibility(View.GONE);
                startRecordLay.setVisibility(View.GONE);
                replayRecordLay.setVisibility(View.VISIBLE);

                if (CoreThePost.isRecordStop()) {
                    audioStatus = "voiceNotes";
                } else {

                }
            }
        });


        replayRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                replayRecordLay.setVisibility(View.GONE);
                stopRecordLay.setVisibility(View.GONE);
                startRecordLay.setVisibility(View.VISIBLE);

                new OurToast().myToast(getBaseContext(),  getString(R.string.reset));
                audioStatus = "Reset";
                CoreThePost.resetTheRecord(progressAudio);
            }
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (typeMessage.getText().toString().trim().equals("")) {
                    thePost.setVisibility(View.GONE);
                } else {
                    thePost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        typeMessage.addTextChangedListener(watcher);
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

    private void uploadImage(String whichRoom) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        //  final FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(filePath != null)
        {
            StorageReference ref = storageReference.child(whichRoom);
            StorageReference mountains = ref.child(theUID);
            StorageReference mountainRef = mountains.child(createPicName);
            mountainRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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


    private void requestPermission() {
        ActivityCompat.requestPermissions(UploadThePostPleaseForCountryTestThePictureUpload.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(UploadThePostPleaseForCountryTestThePictureUpload.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else if(requestCode == STORAGE_PERMISSION_CODE){

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
                break;
        }
    }

    public boolean checkAudioPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
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
            loadedImage.setImageURI(Crop.getOutput(result));
            deleteThePickupImage.setVisibility(View.VISIBLE);
            pickThePhoto.setVisibility(View.GONE);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(UploadThePostPleaseForCountryTestThePictureUpload.this , RoomCountry.class);
        startActivity(i);
        finish();
    }
}
