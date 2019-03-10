package alserdar.widewall.nav_stuff;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.animate.RotateImage;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;

public class MyProfileSettings extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 25;
    private int PICK_IMAGE_REQUEST = 75;
    private FirebaseFirestore db ;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;
    String theUID ;
    LinearLayout avi ;
    private String age;
    private ImageView profilePic;
    private Bitmap bitmap;
    private Button save , saveMyHandle , saveMyNickname , saveMyBio , saveMyLink , saveMyBirthdate ,
            doneMyHandle , doneMyNickname , doneMyBio , doneMyLink , doneMyBirthdate;
    private EditText myHandle , myNickname , myBirthdate , myInfo , myLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_settings);

        profilePic = findViewById(R.id.myProfilePictureInProfileSetting);
        myNickname = findViewById(R.id.myNickNameInProfileSetting);
        myHandle = findViewById(R.id.myHandleInProfileSetting);
        myLink = findViewById(R.id.myLinkInProfileSetting);
        myInfo = findViewById(R.id.myInfoInProfileSetting);
        myBirthdate = findViewById(R.id.myBirthdateInProfileSetting);
        save = findViewById(R.id.saveProfileSetting);
        saveMyHandle = findViewById(R.id.saveMyHandle);
        saveMyNickname = findViewById(R.id.saveMyNickname);
        saveMyBio = findViewById(R.id.saveMyBio);
        saveMyLink = findViewById(R.id.saveMyWebsite);
        saveMyBirthdate = findViewById(R.id.saveMyBirthdate);

        doneMyHandle = findViewById(R.id.doneMyHandle);
        doneMyNickname = findViewById(R.id.doneMyNickname);
        doneMyBio = findViewById(R.id.doneMyBio);
        doneMyLink = findViewById(R.id.doneMyWebsite);
        doneMyBirthdate = findViewById(R.id.doneMyBirthdate);
        avi = findViewById(R.id.aviLay);

        theUID = DetectUserInfo.theUID(MyProfileSettings.this);

        ImageView pressBack = findViewById(R.id.press_back);
        ImageView profilePicForToolbar = findViewById(R.id.profilePicInToolBar);

        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MyProfileSettings.this , MyProfile.class);
                startActivity(i);
                finish();
            }
        });

        LoadPictures.loadPicsForHome(MyProfileSettings.this , theUID , profilePicForToolbar);



        DetectUserInfoFromFirebase.UserProfileInformationGoToProfileSettings(MyProfileSettings.this , profilePic ,
                myNickname , myHandle ,myInfo , myLink , myBirthdate);


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (myHandle.getText().toString().equals("widewall")
                        || myHandle.getText().toString().equals("") || myHandle.getText().toString().length() < 4)



                {
                    saveMyHandle.setVisibility(View.GONE);
                    // save.setBackgroundResource(R.drawable.disable_button);
                    // new OurToast().myToastPic(NewUser.this , "" , R.mipmap.risk);

                    new OurToast().myToast(getBaseContext() , getString(R.string.handle_must_not_be_empty_or_less_than_four_letters));

                }else
                {
                    //save.setBackgroundResource(R.drawable.button);
                    saveMyHandle.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        myHandle.addTextChangedListener(watcher);

        saveMyHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").whereEqualTo("handleLowerCase" , myHandle.getText().toString().toLowerCase())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(final QuerySnapshot documentSnapshots) {

                                if (documentSnapshots.isEmpty())
                                {
                                    saveMyHandle.setVisibility(View.GONE);
                                    doneMyHandle.setVisibility(View.VISIBLE);
                                    RotateImage.startRotateMyButton(doneMyHandle);
                                    Map<String , Object> freshUser = new HashMap<>();
                                    freshUser.put("handle" , myHandle.getText().toString());
                                    freshUser.put("handleLowerCase" , myHandle.getText().toString().toLowerCase());

                                    db = FirebaseFirestore.getInstance();
                                    db.collection("UserInformation").document(theUID)
                                            .update(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                          //  RotateImage.stopRotateMyButton(saveMyHandle);
                                            new OurToast().myToastPic(getBaseContext() , "" , R.mipmap.done);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            new OurToast().myToast(getBaseContext() ,  getString(R.string.some_thing_went_wrong_try_again_later));
                                        }
                                    });

                                }else
                                {
                                    new OurToast().myToast(getBaseContext() , getString(R.string.try_another_one));
                                }
                            }
                        });
            }
        });



        // watcherNickname
        TextWatcher watcherNickname = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (myNickname.getText().toString().equals("") || myNickname.getText().toString().equals("widewall"))
                {
                    saveMyNickname.setVisibility(View.GONE);
                }else
                {
                    saveMyNickname.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        myNickname.addTextChangedListener(watcherNickname);

        saveMyNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMyNickname.setVisibility(View.GONE);
                doneMyNickname.setVisibility(View.VISIBLE);
                RotateImage.startRotateMyButton(doneMyNickname);
                Map<String , Object> freshUser = new HashMap<>();
                freshUser.put("userName" , myNickname.getText().toString());
                db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").document(theUID)
                        .update(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToastPic(getBaseContext() , "", R.mipmap.done);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext() ,  getString(R.string.some_thing_went_wrong_try_again_later));
                    }
                });

            }
        });



        // watcher biography
        TextWatcher watcherBio = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (myInfo.getText().toString().equals("") || myInfo.getText().toString().equals("widewall"))
                {
                    saveMyBio.setVisibility(View.GONE);
                }else
                {
                    saveMyBio.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        myInfo.addTextChangedListener(watcherBio);

        saveMyBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMyBio.setVisibility(View.GONE);
                doneMyBio.setVisibility(View.VISIBLE);
                RotateImage.startRotateMyButton(doneMyBio);
                Map<String , Object> freshUser = new HashMap<>();
                freshUser.put("aboutUser", myInfo.getText().toString());
                db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").document(theUID)
                        .update(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToastPic(getBaseContext() , "", R.mipmap.done);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext() ,  getString(R.string.some_thing_went_wrong_try_again_later));
                    }
                });

            }
        });


        // watcher biography
        TextWatcher watcherLink = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (myLink.getText().toString().equals("") || myLink.getText().toString().equals("widewall"))
                {
                    saveMyLink.setVisibility(View.GONE);
                }else
                {
                    saveMyLink.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        myLink.addTextChangedListener(watcherLink);

        saveMyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMyLink.setVisibility(View.GONE);
                doneMyLink.setVisibility(View.VISIBLE);
                RotateImage.startRotateMyButton(doneMyLink);
                Map<String , Object> freshUser = new HashMap<>();
                freshUser.put("linkUser", myLink.getText().toString());
                db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").document(theUID)
                        .update(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToastPic(getBaseContext() , "", R.mipmap.done);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext() ,  getString(R.string.some_thing_went_wrong_try_again_later));
                    }
                });

            }
        });


        // watcher biography
        TextWatcher watcherbirthDate = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (myBirthdate.getText().toString().equals("") || myBirthdate.getText().toString().equals("widewall"))
                {
                    saveMyBirthdate.setVisibility(View.GONE);
                }else
                {
                    saveMyBirthdate.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        myBirthdate.addTextChangedListener(watcherbirthDate);

        saveMyBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMyBirthdate.setVisibility(View.GONE);
                doneMyBirthdate.setVisibility(View.VISIBLE);
                RotateImage.startRotateMyButton(doneMyBirthdate);
                Map<String , Object> freshUser = new HashMap<>();
                freshUser.put("BirthDate" , myBirthdate.getText().toString());
                freshUser.put("age" , age);
                db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").document(theUID)
                        .update(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToastPic(getBaseContext() , "", R.mipmap.done);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext() ,  getString(R.string.some_thing_went_wrong_try_again_later));
                    }
                });

            }
        });




        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isReadStorageAllowed()) {

                    Crop.pickImage(MyProfileSettings.this);

                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear ,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                age = DetectUserInfo.calculateTheAge(year , monthOfYear , dayOfMonth);

                myBirthdate.setText(new StringBuilder().append(dayOfMonth).append("/")
                        .append(monthOfYear + 1).append("/").append(year));
            }

        };


        myBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(MyProfileSettings.this, date, 1989, 8,
                        9).show();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                avi.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
              //  RotateImage.startRotateMyButton(save);
                uploadImage();

                /*
                 Map<String , Object> freshUser = new HashMap<>();
                freshUser.put("userName" , myNickname.getText().toString());
                freshUser.put("handle" , myHandle.getText().toString());
                freshUser.put("handleLowerCase" , myHandle.getText().toString().toLowerCase());
                freshUser.put("aboutUser", myInfo.getText().toString());
                freshUser.put("linkUser", myLink.getText().toString());
                freshUser.put("BirthDate" , myBirthdate.getText().toString());
                freshUser.put("age" , age);




                db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").document(theUID)
                        .update(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToastPic(getBaseContext() , "", R.mipmap.done);
                        Intent i = new Intent(MyProfileSettings.this , MyProfile.class);
                        startActivity(i);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext() , "Sorry Something went bad , try again later !!");
                    }
                });

                 */


            }
        });
    }

    private void uploadImage() {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();
        if(filePath != null)
        {
            StorageReference ref = storageReference.child("images/"+ theUID);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
            profilePic.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    /*
     @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            try {
                filePath = data.getData();

                profilePic.setImageBitmap(bitmap);

    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

    resized(bitmap , 2048 , 2048);
}
            catch (IOException e)
                    {
                    e.printStackTrace();
                    }

                    }

                    super.onActivityResult(requestCode, resultCode, data);
                    }

public Bitmap resized(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
        }
     */

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MyProfileSettings.this , MyProfile.class);
        startActivity(i);
        finish();
    }
}
