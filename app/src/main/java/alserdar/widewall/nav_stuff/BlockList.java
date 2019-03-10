package alserdar.widewall.nav_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import alserdar.widewall.Home;
import alserdar.widewall.R;
import alserdar.widewall.core.Core_Following_Followers_Block_Chat;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.UserModel;

public class BlockList extends AppCompatActivity {

    String theUID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_list);

        theUID = DetectUserInfo.theUID(BlockList.this);
        final ListView blockLista = findViewById(R.id.blockLista);

        ImageView pressBack = findViewById(R.id.press_back);
        ImageView profilePic = findViewById(R.id.profilePicInToolBar);
        ImageView topList = findViewById(R.id.getTheTopOfList);
        LinearLayout theLay = findViewById(R.id.theEmptyLay);

        DetectUserInfoFromFirebase.emptyListOrNot(theUID , "Blocked" , theLay);

        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(BlockList.this , Home.class);
                startActivity(i);
                finish();
            }
        });

        LoadPictures.loadPicsForHome(BlockList.this , theUID , profilePic);

        topList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                blockLista.smoothScrollToPosition(0);
            }
        });

        FirebaseListAdapter<UserModel> adapter =
                new FirebaseListAdapter<UserModel>(BlockList.this, UserModel.class, R.layout.list_of_blocked,
                        FirebaseDatabase.getInstance().getReference(theUID).child("Blocked")) {

                    @Override
                    public UserModel getItem(int position) {
                        return super.getItem(super.getCount() - position - 1);

                    }

                    @Override
                    protected void populateView(final View v, final UserModel model, final int position) {

                        ImageView userBlockedImage = v.findViewById(R.id.userBlockedImage);
                        ImageView userBlockedCountry = v.findViewById(R.id.userBlockedCountry);
                        ImageView userBlockedRemove = v.findViewById(R.id.userBlockedRemove);

                        TextView userBlockedName = v.findViewById(R.id.userBlockedName);
                        TextView userBlockedHandle = v.findViewById(R.id.userBlockedHandle);

                        userBlockedName.setText(model.getUserName());
                        userBlockedHandle.setText(model.getHandle());

                        LoadPictures.loadPicsForHome(getBaseContext() , model.getTheUID() , userBlockedImage);
                        LoadPictures.setCountryFlagByNameCountry(userBlockedCountry , model.getCountry());

                        userBlockedRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Core_Following_Followers_Block_Chat.unBlock(theUID , model.getTheUID());
                            }
                        });
                    }
                };

        blockLista.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BlockList.this , Home.class);
        startActivity(i);
        finish();
    }
}
