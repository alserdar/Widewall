package alserdar.widewall.load_package;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import alserdar.widewall.R;

public class LoadPicFullScreenIPrivateChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_pic_full_screen);

        ImageView loadTheFullPic = findViewById(R.id.loadTheFullImage);

        long theSum = this.getIntent().getExtras().getLong("theSum");
        String picName =this.getIntent().getExtras().getString("picName");

        LoadPictures.loadPicturesInPrivateChat(getBaseContext() , theSum , loadTheFullPic , picName);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
