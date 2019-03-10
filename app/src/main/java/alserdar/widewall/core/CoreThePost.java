package alserdar.widewall.core;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.models.CountModel;
import alserdar.widewall.models.ListenModel;
import alserdar.widewall.models.PostModel;


public class CoreThePost {

    private static ValueAnimator animator;
    private static SimpleExoPlayer player;
    private static MediaRecorder mediaRecorder ;
    private static String AudioSavePathInFireStorage = null;
    private static String getTheRandom = "" ;


    public static void thePost(final String theUID , final String whichRoom , final String postCountry,
                               final String currentCountry , final String typeMessage ,
                               final String myNickName , final String handle ,
                               final String audioStatus, final String picPath)
    {




        String mainKeyPost ;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(whichRoom).push();
        mainKeyPost = myRef.getRef().getKey();

        String countKey = count(mainKeyPost);

        ServerValue serverValue = new ServerValue();


        if (myNickName.equals("") || handle.equals(""))
        {

        }else
        {
            myRef.setValue(new PostModel(theUID, typeMessage , myNickName,
                    handle, currentCountry, postCountry , whichRoom, audioStatus,
                    picPath, getTheRandom, mainKeyPost, mainKeyPost , countKey,
                    false,  returnTheDate() , returnTheTime()  , "" , "" , "" ));

            FirebaseDatabase myTimeline = FirebaseDatabase.getInstance();
            DatabaseReference myRefTimeline = myTimeline.getReference(theUID).child("posts").push();
            String keyPost = myRefTimeline.getRef().getKey();
            myRefTimeline.setValue(new PostModel(theUID, typeMessage, myNickName,
                    handle, currentCountry, postCountry , whichRoom, audioStatus,
                    picPath, getTheRandom, mainKeyPost, keyPost , countKey ,
                    false,  returnTheDate() , returnTheTime() , "" , "" , "" ));

        }

    }

    private static String count(String keyPost){

        String countKey ;
        FirebaseDatabase countThePost = FirebaseDatabase.getInstance();
        DatabaseReference countReference = countThePost.getReference("countPost " + keyPost).push();
        countKey = countReference.getRef().getKey();
        countReference.setValue(new CountModel(countKey, 0 , 0 , 0, 0 ,0 ,0));

        return countKey ;
    }

    public static void downloadURLToPlay(final Context context , String modelUID , String modelAudioPath , final SeekBar seekBarForPlay ,
                             final Button playMessage , final Button pauseMessage) {


        playMessage.setVisibility(View.GONE);
        pauseMessage.setVisibility(View.VISIBLE);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Get reference to the file
        StorageReference forestRef = storageRef.child("voice");
        StorageReference forest = forestRef.child(modelUID);
        StorageReference fores = forest.child(modelAudioPath);


        fores.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                exoPlayerPreparing(context , uri);


                animator = ValueAnimator.ofInt(0 , seekBarForPlay.getMax());
                animator.setDuration(10000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        seekBarForPlay.setProgress((Integer) animation.getAnimatedValue());
                        player.setPlayWhenReady(true);
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        player.setPlayWhenReady(false);

                        playMessage.setVisibility(View.VISIBLE);
                        pauseMessage.setVisibility(View.GONE);


                    }
                });
                animator.start();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

    private static void exoPlayerPreparing(Context context, Uri uri) {

        TrackSelector trackSelector = new DefaultTrackSelector();
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "WideWall"), (TransferListener<? super DataSource>) bandwidthMeter);
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
        player.prepare(mediaSource);
    }

    public static void stopTheExoPlayer(Button  playMessage, Button pauseMessage)
    {
        playMessage.setVisibility(View.VISIBLE);
        pauseMessage.setVisibility(View.GONE);
        if (player == null)
        {

        }else
        {
            player.stop();
        }

        if (animator == null)
        {

        }else
        {
            animator.pause();
        }

    }

    // audio stuff
    public static void MediaRecorderReady() {


        AudioSavePathInFireStorage = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                CoreThePost.CreateRandomAudioFileName(9) + "WideWallRecording.3gp";


        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInFireStorage);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public static void uploadTheRecord(final Context context , String theUID)
    {
        getTheRandom = CreateRandomAudioFileName(9);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("voice");
        StorageReference mountains = mountainsRef.child(theUID);
        StorageReference mount = mountains.child(getTheRandom);


        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("audio/mpeg")
                .build();
        Uri uri = Uri.fromFile(new File(AudioSavePathInFireStorage));

        mount.putFile(uri , metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                new OurToast().myToastPic(context , "" , R.mipmap.done);

            }
        });
    }

    private void deleteTheRecord(Context context)
    {
        // Set up the projection (we only need the ID)
        String[] projection = { MediaStore.Images.Media._ID };

// Match on the file path
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArgs = new String[] { AudioSavePathInFireStorage };

        // Query for the ID of the media matching the file path
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();
        Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
        if (c.moveToFirst()) {
            // We found the ID. Deleting the item via the content provider will also remove the file
            long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            contentResolver.delete(deleteUri, null, null);
        } else {
            // File not found in media store DB
        }
        c.close();
    }

    public static void progressTheRecord(final ProgressBar progressAudio)
    {
        animator = ValueAnimator.ofInt(0, progressAudio.getMax());
        animator.setDuration(10000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressAudio.setProgress((Integer) animation.getAnimatedValue());

            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (mediaRecorder == null) {

                } else {
                    mediaRecorder.stop();
                }
            }
        });


        animator.start();
    }

    public static void resetTheRecord(ProgressBar progressAudio)
    {
        if (animator.isRunning()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                animator.pause();
                if (mediaRecorder == null) {

                } else {
                    mediaRecorder.reset();
                    progressAudio.setProgress(0);
                }
            }
        } else {
            animator.removeAllUpdateListeners();
            if (mediaRecorder == null) {

            } else {
                mediaRecorder.reset();
            }
            progressAudio.setProgress(0);
        }
    }

    public static boolean isRecordStop()
    {
        if (animator.isRunning()) {
            animator.removeAllUpdateListeners();
        }
        return true ;
    }

    public static long hasSpace ()
    {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize, availableBlocks;
        blockSize = stat.getBlockSizeLong();
        availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    public static void stopToUploadImmediately()
    {
        try{

            if (animator == null)
            {

            }else
            {
                animator.pause();
                mediaRecorder.stop();
            }

        }catch (Exception e)
        {

        }

    }

    public static void CoreTheListen(String theUID , String nickName ,String handle ,String keyPost ,
                                     String listenerUID ,String listenerNickname ,String listenerHandle)
    {
        FirebaseDatabase coreListen = FirebaseDatabase.getInstance();
        DatabaseReference refListen = coreListen.getReference(theUID).child("listened").push();
        refListen.setValue(new ListenModel(theUID , nickName , handle , keyPost ,
                getTheRandom , listenerUID , listenerNickname , listenerHandle , true));
    }


    public static String returnTheDate(){

        Date date = new Date();
      //  Date newDate = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt = new SimpleDateFormat("dd-MMM-yyyy");
        return dt.format(date);
    }

    public static String returnTheTime(){

        /*
          Date date = new Date();
        Date newTime = new Date(date.getTime() + (604800000L * 2) + (24 * 60 * 60));
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt = new SimpleDateFormat("HH:mm");
        return dt.format(newTime);
         */
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        return timeFormatter.format(date);
    }

}
