package alserdar.widewall.load_package;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import alserdar.widewall.R;

public class LoadPicFullScreenInRooms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_pic_full_screen);

        ImageView loadTheFullPic = findViewById(R.id.loadTheFullImage);

        String whichRoom = this.getIntent().getExtras().getString("whichRoom");
        String posterUID =this.getIntent().getExtras().getString("posterUID");
        String picPath =this.getIntent().getExtras().getString("picPath");

        LoadPictures.loadTheBigOne(LoadPicFullScreenInRooms.this , whichRoom ,
                posterUID , loadTheFullPic , picPath);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
