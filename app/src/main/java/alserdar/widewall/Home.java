package alserdar.widewall;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import alserdar.widewall.fcm_service_notification.FirebaseBackgroundServiceForMessages;
import alserdar.widewall.fcm_service_notification.FirebaseBackgroundServiceForNotifications;
import alserdar.widewall.fragment_home_stuff.HomeFragment;
import alserdar.widewall.fragment_messages_stuff.MessagesFragment;
import alserdar.widewall.fragment_notification_stuff.NotificationsFragment;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.UserModel;
import alserdar.widewall.nav_stuff.BlockList;
import alserdar.widewall.nav_stuff.FavouriteList;
import alserdar.widewall.nav_stuff.Location;
import alserdar.widewall.nav_stuff.MyProfile;
import alserdar.widewall.nav_stuff.Search;
import alserdar.widewall.nav_stuff.Volcano;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {


    private String theUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        theUID = DetectUserInfo.theUID(Home.this);

        startService(new Intent(Home.this , FirebaseBackgroundServiceForMessages.class));
        startService(new Intent(Home.this , FirebaseBackgroundServiceForNotifications.class));



        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                selectedFragment = HomeFragment.newInstance();
                                break;
                            case R.id.navigation_notifications:
                                selectedFragment = NotificationsFragment.newInstance();
                                removeBadge(navigation , R.id.navigation_notifications);
                                break;
                            case R.id.navigation_dashboard:
                                selectedFragment = MessagesFragment.newInstance();
                                removeBadge(navigation , R.id.navigation_dashboard);
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        navigation.setItemIconTintList(null);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);
                assert model != null;
                if (model.isGotNotification())
                {
                    showBadge(getBaseContext(), navigation, R.id.navigation_notifications, "1");

                }else
                {
                    removeBadge(navigation , R.id.navigation_notifications);
                }
            }
        });

        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel model = documentSnapshot.toObject(UserModel.class);
                assert model != null;
                if (model.isGotMessages())
                {
                    showBadge(getBaseContext(), navigation, R.id.navigation_dashboard, "1");

                }else
                {
                    removeBadge(navigation , R.id.navigation_dashboard);
                }
            }
        });



        ImageView profilePic = findViewById(R.id.profilePicForHome);
        TextView homeUserName = findViewById(R.id.userNameForHome);


        LoadPictures.loadPicsForHome(Home.this , theUID, profilePic);
        DetectUserInfo.infoForHome(Home.this , theUID, homeUserName);



         profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);

            }
        });




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                Home.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageView ppNav = header.findViewById(R.id.profilePictureNav);
        TextView userNameNav = header.findViewById(R.id.nameForNav);
        TextView handleNav = header.findViewById(R.id.handleForNav);
        DetectUserInfoFromFirebase.userHandle(getBaseContext() , userNameNav , handleNav);
        LoadPictures.loadPicsForHome(getBaseContext() , theUID, ppNav);
        navigationView.setNavigationItemSelectedListener(Home.this);
        navigationView.setItemIconTintList(null);
        toggle.setDrawerIndicatorEnabled(false);

        ppNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DetectUserInfoFromFirebase.clickTheUser(Home.this , theUID ,theUID);

            }
        });

    }

    long back_pressed;

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 3000 > System.currentTimeMillis()){
                startService(new Intent(Home.this , FirebaseBackgroundServiceForMessages.class));
                startService(new Intent(Home.this , FirebaseBackgroundServiceForNotifications.class));
                super.onBackPressed();

            }
            else{

                new OurToast().myToastPic(getBaseContext() , "" , R.mipmap.close);
            }
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(Home.this , MyProfile.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_favourite) {
            Intent i = new Intent(Home.this , FavouriteList.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_blocked) {
            Intent i = new Intent(Home.this , BlockList.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_search) {
            Intent i = new Intent(Home.this , Search.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_location) {
            Intent i = new Intent(Home.this , Location.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_volcano) {
            Intent i = new Intent(Home.this , Volcano.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_ads) {

        } else if (id == R.id.nav_contact_us) {

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","widewallapp@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "WideWall User");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));

        }else if (id == R.id.nav_settings) {
        }else if (id == R.id.nav_information)
        {

        }else if (id == R.id.nav_logout) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

                String titleText = "Are you Sure !";
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.RED);
                SpannableStringBuilder ssBuilder = new SpannableStringBuilder(titleText);
                ssBuilder.setSpan(
                        foregroundColorSpan,
                        0,
                        titleText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                builder.setTitle(ssBuilder);
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {

                            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                                ((ActivityManager)Objects.requireNonNull(getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData(); // note: it has a return value!
                            } else {
                                String packageName = getApplicationContext().getPackageName();
                                Runtime runtime = Runtime.getRuntime();
                                runtime.exec("pm clear "+packageName);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                builder.setNegativeButton("No",null);
                AlertDialog dialog = builder.create();
               // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void showBadge(Context context, BottomNavigationView
            bottomNavigationView, @IdRes int itemId, String value) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        View badge = LayoutInflater.from(context).inflate(R.layout.notification_badge, bottomNavigationView, false);

        TextView text = badge.findViewById(R.id.notificationsBadge);
        text.setText(value);
        itemView.addView(badge);
    }

    public static void removeBadge(BottomNavigationView bottomNavigationView, @IdRes int itemId) {
        BottomNavigationItemView itemView = bottomNavigationView.findViewById(itemId);
        if (itemView.getChildCount() == 3) {
            itemView.removeViewAt(2);
        }
    }

}
