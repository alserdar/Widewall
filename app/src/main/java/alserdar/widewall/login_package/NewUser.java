package alserdar.widewall.login_package;

import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import alserdar.widewall.Home;
import alserdar.widewall.Launcher;
import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.animate.RotateImage;
import alserdar.widewall.load_package.DetectUserInfo;

public class NewUser extends AppCompatActivity {

    String  country , theUID , email , phone , name , id;
    Button saveAll ;
    ImageView regPic ;
    EditText handle , regNickName , regAge;
    LinearLayout avi ;
    RadioButton regMale , regFemale ;
    private int STORAGE_PERMISSION_CODE = 25;
    private int PICK_IMAGE_REQUEST = 75;
    private TextView whichGender;

    FirebaseStorage storage;
    StorageReference storageReference;
    Uri filePath;
    private String age;
    private FirebaseFirestore db;
    private String randomNickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);



        randomNickname = this.getIntent().getExtras().getString("randomName");
        theUID = DetectUserInfo.theUID(NewUser.this);
        country = DetectUserInfo.country(NewUser.this);
        email = DetectUserInfo.email(NewUser.this);
        phone = DetectUserInfo.phoneNumber(NewUser.this);
        name =  DetectUserInfo.faceBookUser(NewUser.this);
        id =  DetectUserInfo.id(NewUser.this);

        if (theUID.equals("uid") || theUID.equals(""))
        {
            Intent i = new Intent(NewUser.this , LoginWith.class);
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(i);
            finish();
        }

        saveAll = findViewById(R.id.reg_save);
        regPic = findViewById(R.id.regPic);
        regNickName = findViewById(R.id.reg_nickname);
        handle = findViewById(R.id.handle);
        regAge = findViewById(R.id.reg_age);
        regMale = findViewById(R.id.reg_male);
        regFemale = findViewById(R.id.reg_female);
        whichGender = findViewById(R.id.whichGenderIs);
        avi = findViewById(R.id.aviLay);

        handle.setText(randomNickname);

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear ,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                age = DetectUserInfo.calculateTheAge(year , monthOfYear , dayOfMonth);

                regAge.setText(new StringBuilder().append(dayOfMonth).append("/")
                        .append(monthOfYear + 1).append("/").append(year));
            }

        };


        regAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(NewUser.this, date, 1989, 8,
                        9).show();
            }
        });

        regPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isReadStorageAllowed()) {
                    //If permission is already having then showing the toast
                    //Existing the method with return

                    /*
                     Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                     */


                    Crop.pickImage(NewUser.this);
                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        });

        regMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //checkHandle();
                if (regMale.isChecked())
                {
                    whichGender.setText("male");
                    regPic.setImageResource(R.mipmap.male);
                }
            }
        });

        regFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //checkHandle();
                if (regFemale.isChecked())
                {
                    whichGender.setText("female");
                    regPic.setImageResource(R.mipmap.female);
                }
            }
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (handle.getText().toString().equals("WideWall")
                        || handle.getText().toString().equals(""))



                {
                    saveAll.setVisibility(View.GONE);
                   // new OurToast().myToastPic(NewUser.this , "" , R.mipmap.risk);

                }else
                {
                    saveAll.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        handle.addTextChangedListener(watcher);



        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                avi.setVisibility(View.VISIBLE);
                RotateImage.startRotateMyButton(saveAll);


                //  avlLay.setVisibility(View.VISIBLE);

                String theLittleUID = theUID.substring(0, 12);

                Map<String , Object> freshUser = new HashMap<>();
                freshUser.put("theUID" , theUID); // this will remove too
                freshUser.put("userName" , handle.getText().toString());
                freshUser.put("handle" , theLittleUID);
                freshUser.put("handleLowerCase", theLittleUID);
                freshUser.put("gender" , whichGender.getText().toString());
                freshUser.put("country" , country);
                freshUser.put("town" , "town");
                freshUser.put("age" , age);
                freshUser.put("relationShip", "");
                freshUser.put("aboutUser", "");
                freshUser.put("linkUser", "");
                freshUser.put("BirthDate" , regAge.getText().toString());
                freshUser.put("id", id);
                freshUser.put("phone" , phone);
                freshUser.put("email", email);



                freshUser.put("online" , false);
                freshUser.put("star" , false);
                freshUser.put("globallyPerson" , false);
                freshUser.put("locallyPerson" , false);
                freshUser.put("paid" , false);
                freshUser.put("sus" , false);
                freshUser.put("privateAccount" , false);
                freshUser.put("hasPic" , false);

                freshUser.put("gotNotification" , false);
                freshUser.put("gotMessages" , false);


                freshUser.put("reportAccounts" , 0);
                freshUser.put("getX" , 0);

                freshUser.put("createAccount" , new Date());
                freshUser.put("lastJoin" , new Date());

                uploadImage();

                db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").document(theUID)
                        .update(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new OurToast().myToast(getBaseContext() , getString(R.string.welcome) + handle.getText().toString());
                        Intent i = new Intent(getBaseContext() , Home.class);
                        startActivity(i);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext() ,  getString(R.string.some_thing_went_wrong_try_again_later));
                        try {

                            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                                ((ActivityManager)Objects.requireNonNull(getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData(); // note: it has a return value!
                            } else {
                                String packageName = getApplicationContext().getPackageName();
                                Runtime runtime = Runtime.getRuntime();
                                runtime.exec("pm clear "+packageName);

                                Intent i = new Intent(getBaseContext() , Launcher.class);
                                startActivity(i);
                            }

                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void checkHandle() {


        db = FirebaseFirestore.getInstance();
        db.collection("UserInformation").whereEqualTo("handle" , handle.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(final QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.isEmpty())
                        {
                            handle.setTextColor(getResources().getColor(R.color.ourGreen));

                        }else
                        {
                            handle.setTextColor(Color.RED);
                            new OurToast().myToast(getBaseContext() , getString(R.string.try_another_nickname));
                            saveAll.setVisibility(View.GONE);
                        }
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

                            new OurToast().myToast(getBaseContext() , getString(R.string.picture_uploaded));
                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new OurToast().myToast(getBaseContext() , getString(R.string.failed));
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
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
            regPic.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
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
                new OurToast().myToast(getBaseContext() , getString(R.string.permission_granted));
            }else{
                //Displaying another toast if permission is not granted
                new OurToast().myToast(getBaseContext() , getString(R.string.permission_denied));
            }
        }
    }

    @Override
    public void onBackPressed() {

        new OurToast().myToast(getBaseContext() , getString(R.string.save_your_information_first));
    }

}
