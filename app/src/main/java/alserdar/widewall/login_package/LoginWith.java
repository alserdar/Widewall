package alserdar.widewall.login_package;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import alserdar.widewall.Home;
import alserdar.widewall.Launcher;
import alserdar.widewall.OurToast;
import alserdar.widewall.R;

public class LoginWith extends AppCompatActivity {


    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    LoginButton loginButton ;
    SignInButton signInButton ;
    private FirebaseUser firebaseUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_continue_with_login);

        signInButton = findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(LoginWith.this , ContinueWithGoogle.class);
                startActivity(i);
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        final LinearLayout avlLay = findViewById(R.id.aviLay);
//        avlLay.setVisibility(View.VISIBLE);
        loginButton = findViewById(R.id.continueWithFacebook);
        loginButton.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());

                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            Log.v("facebook - id", profile2.getId());
                            SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(LoginWith.this);
                            final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                            editor.putString("FacebookUser" , profile2.getFirstName());
                            editor.putString("user_id" , profile2.getId());
                            editor.apply();
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                    Log.v("facebook - id", profile.getId());
                    SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(LoginWith.this);
                    final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                    editor.putString("FacebookUser" , profile.getFirstName());
                    editor.putString("user_id" , profile.getId());
                    editor.apply();

                }
            }

            @Override
            public void onCancel()
            {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        });


    }



    // [START auth_with_facebook]
    private void handleFacebookAccessToken(final AccessToken token) {
        loginButton.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginWith.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            firebaseUser = mAuth.getCurrentUser();
                            SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(LoginWith.this);
                            final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                            editor.putString("uid" , mAuth.getCurrentUser().getUid());
                            editor.putString("email" , mAuth.getCurrentUser().getEmail());
                            editor.putString("phone" , mAuth.getCurrentUser().getPhoneNumber());
                            editor.apply();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(LoginWith.this);
                            String facebookId = getInfo.getString("user_id" , "user_id");
                            final String facebookUserName = getInfo.getString("FacebookUser" , "FacebookUser");
                            db.collection("UserInformation").whereEqualTo("theUID", mAuth.getUid())
                                    .limit(1).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                boolean isEmpty = task.getResult().isEmpty();
                                                if(isEmpty)
                                                {
                                                   buildThatNoob(mAuth.getUid());
                                                }else
                                                {
                                                    new OurToast().myToast(getBaseContext() , facebookUserName);
                                                    Intent i = new Intent(getBaseContext() , Home.class);
                                                    startActivity(i);
                                                    finish();
                                                }

                                            } else {
                                            }
                                        }
                                    });
                        } else
                            {

                                Intent i = new Intent(getBaseContext() , ContinueWithGoogle.class);
                                startActivity(i);


                            /*
                            GraphRequest.newMeRequest(
                                    token, new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject me, GraphResponse response) {
                                            if (response.getError() != null) {
                                                // handle error
                                            } else {
                                                try {
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                                                            .setTimestampsInSnapshotsEnabled(true)
                                                            .build();
                                                    db.setFirestoreSettings(settings);
                                                    db.collection("UserInformation").whereEqualTo("email", me.getString("email"))
                                                            .limit(1).get()
                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                    if (task.isSuccessful()) {
                                                                        boolean isEmpty = task.getResult().isEmpty();
                                                                        if(isEmpty)
                                                                        {
                                                                            Intent i = new Intent(getBaseContext() , NewUser.class);
                                                                            startActivity(i);
                                                                            finish();
                                                                        }else
                                                                        {
                                                                            new OurToast().myToast(getBaseContext() , "The same Gmail");
                                                                            new OurToast().myToast(getBaseContext() , mAuth.getCurrentUser().getDisplayName());
                                                                            Intent i = new Intent(getBaseContext() , Home.class);
                                                                            startActivity(i);
                                                                            finish();
                                                                        }

                                                                    } else {
                                                                    }
                                                                }
                                                            });
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).executeAsync();
                             */



                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void buildThatNoob(String theUID)
    {
        String theLittleUID = theUID.substring(0, 12);

        Map<String , Object> freshUser = new HashMap<>();
        freshUser.put("theUID" , theUID); // this will remove too
        freshUser.put("userName" , GenerateName() );
        freshUser.put("handle" , theLittleUID);
        freshUser.put("handleLowerCase", theLittleUID);
        freshUser.put("gender" , "male");
        freshUser.put("country" , "Unknown Country");
        freshUser.put("town" , "town");
        freshUser.put("age" , "");
        freshUser.put("relationShip", "");
        freshUser.put("aboutUser", "noob");
        freshUser.put("linkUser", "");
        freshUser.put("BirthDate" , "");
        freshUser.put("id", "");
        freshUser.put("phone" , "");
        freshUser.put("email", "");

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserInformation").document(theUID)
                .set(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(getBaseContext() , NewUser.class);
                i.putExtra("randomName" , GenerateName());
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                new OurToast().myToast(getBaseContext() , getString(R.string.some_thing_went_wrong_try_again_later));
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


    private String GenerateName()
    {
        String name [] = {"Art Axe" ,
                "The Illness" , "Rellie J" , "Bear Bae" ,
                "JK" , "Sweeper" ,
                "Big Love" , "Ki-No-Wa" , "Trouble" ,
                "Ci-Ci D." ,
                "Lucy Locks" , "Updog" ,
                "Dream Devil" , "Miss Gold" , "Vams" ,
                "Egg Tease" , "New Night" ,
                "Wild Kitty" ,
                "Fry Starter" , "Oldie" ,
                "X-Tra" , "Good Deal" , "Pretty Sue" ,
                "Young B" , "High Top" , "Quirk" ,
                "Zils Manils" , "Jetta" , "Prada" , "Dolce" , "Bentley" , "Burberry" , "Fendi" , "Mazda" , "Cartier"
                ,"Beamer","Lexus","Hermes","Ferrari","Tesla","Versace",
                "Benz","Louis-Vuitton","Chanel","Porsche","Gucci","Armani","Infiniti", "Winter" ,
                "Cloudburst" , "Sunshine" , "Frostbite" , "Cold Front" , "Monsoon" , "Wind" , "Cyclone" , "Breezy" ,
                "Stormy Waters" , "Dew Drop" , "Rainbow" , "Twister" , "Flurry", "Sandstorm" , "Hurricane" , "Diamond Dust" ,
                "Short-Fuse" , "Black Ice" , "Drizzle" , "Thunderstorm" , "Frost" , "Funnel Cloud" , "Thundercloud" , "Arctic Air" , "Hazy" , "Thunderhead" , "Blizzard" , "Jet Stream", "Icecap" , "Cirrus" , "Sunny" ,
                "Snowflake" ,
                "Radar" ,
                "FLAK" ,
                "Magnet" ,
                "Doppler" ,
                "SWAT" ,
                "Polar" ,
                "Jam Jam" ,
                "Helmet" ,
                "Sgt. Calhoun" ,
                "True North" ,
                "Kevlar" ,
                "Subzero" ,
                "True South" ,
                "Teflon" ,
                "Soldier" ,
                "Laser" ,
                "Grenade" ,
                "Robber" ,
                "Taze" ,
                "Land Mine" ,
                "Mizzen" ,
                "Boot" ,
                "Quest" ,
                "Camber" ,
                "Combat" ,
                "Detector" ,
                "Dogwatch" ,
                "Venus" ,
                "Luna" ,
                "Big Bang" ,
                "Eclipse" ,
                "Stardust" ,
                "Chaos" ,
                "Lunar" ,
                "Stargazer" ,
                "Dark Matter" ,
                "Neptune" ,
                "Earthshine" ,
                "Fireball" ,
                "Comet" ,
                "Equinox" ,
                "Hubble" ,
                "Orbit" ,
                "Retrograde" ,
                "Light Year" ,
                "Cosmos" ,
                "Solstice" ,
                "Nova" ,
                "Star" ,
                "Supernova" ,
                "Parallax" ,
                "Shooting Star" ,
                "Twilight" ,
                "Quasar" ,
                "Jupiter" ,
                "Zenith" ,
                "Virgo" ,
                "Pluto" ,
                "Absolute Zero" ,
                "Leo" ,
                "Galaxy" ,
                "Aurora" ,
                "X-Ray",
                "Guppy" ,
                "Congo" ,
                "Electric Blue" ,
                "Goldfish" ,
                "Flowerhorn" ,
                "Cobalt" ,
                "Tetra" ,
                "Peacock" ,
                "Swordtail" ,
                "Clownfish" ,
                "Flamingo" ,
                "Sunburst" ,
                "Betta" ,
                "Firemouth" ,
                "Mudskipper" ,
                "Angelfish" ,
                "Earth Eater" ,
                "Cosmic" ,
                "Koi" ,
                "Red Terror" ,
                "Starfire" ,
                "Ember" ,
                "Green Terror" ,
                "Squeaker" ,
                "Chardonnay" ,
                "Candy Cane" ,
                "Banana Bread" ,
                "Merlot" ,
                "Good ‘n Plenty" ,
                "Biscuit" ,
                "Sherry" ,
                "Tootsie" ,
                "Bun Bun" ,
                "Moonshine" ,
                "Skittle" ,
                "Cookie" ,
                "Bourbon" ,
                "Smartie" ,
                "Pecan" ,
                "Whisky Ginger" ,
                "Gummy Bear" ,
                "Hot Cocoa" ,
                "Soda Pop" ,
                "Wonka" ,
                "Brownie" ,
                "Rum Punch" ,
                "Pop Rock" ,
                "Vanilla Bean" ,
                "Rummy" ,
                "Taffy" ,
                "Banana Split" ,
                "Tequila" ,
                "Pixy" ,
                "Shortbread" ,
                "Brandy" ,
                "Lifesaver" ,
                "Shortcake" ,
                "Barbera" ,
                "Double" ,
                "Red Velvet" ,
                "Ginger" ,
                "Jelly Bean" ,
                "Caramel" ,
                "Cherry" ,
                "Peep" ,
                "S’Mores" ,
                "Wildberry" ,
                "Jolly" ,
                "Chocolate Chip" ,
                "Strawberry" ,
                "Dottie" ,
                "Gingersnap" ,
                "Blueberry" ,
                "Sixlet" ,
                "Protein" ,
                "Blackberry" ,
                "Pocky" ,
                "Fresh" ,
                "Apple" ,
                "Redvine" ,
                "Tart" ,
                "Mulberry" ,
                "Starburst" ,
                "Twisty" ,
                "Brambleberry" ,
                "Junior Mint" ,
                "Juice" ,
                "Peaches" ,
                "Peppermint" ,
                "Combo" ,
                "Lem / Lemon" ,
                "Cinnamon" ,
                "Whirly" ,
                "Champagne" ,
                "Kit-Kat" ,
                "Vitamin C" ,
                "Martini" ,
                "Twix" ,
                "Cayenne" ,
                "Manhattan" ,
                "Snickers" ,
                "Hitch" ,
                "Bonbon" ,
                "Cake" ,
                "Kicks" ,
                "Sugar Plum" ,
                "Cupcake" ,
                "Queen C" ,
                "Twizzler" ,
                "Muffin" ,
                "Snappie" ,
                "Sugar" ,
                "Snickerdoodle" ,
                "Snazz" ,
                "Brown Sugar" ,
                "Thumbprint" ,
                "Snapple" ,
                "Honey" ,
                "Apple Pie" ,
                "Polka-Dot" ,
                "Maple" ,
                "Cherry Pie" ,
                "Aqua" ,
                "Jail Bond" ,
                "Marine" ,
                "Outlaw" ,
                "Bazooka" ,
                "Boomer" ,
                "Renegade" ,
                "Chopper" ,
                "Boomstick" ,
                "Champ" ,
                "The Law" ,
                "Booter" ,
                "Sentinel" ,
                "Bullet" ,
                "Box Kicker" ,
                "Colonel" ,
                "The Enforcement" ,
                "Captain Jack" ,
                "Recruit" ,
                "Howitzer" ,
                "Doughboy" ,
                "General" ,
                "Patrol" ,
                "FLAK" ,
                "Air Raid" ,
                "Platoon" ,
                "FUBAR" ,
                "Blitz" ,
                "Saber" ,
                "Hoover" ,
                "Blitzkrieg" ,
                "Air Alert" ,
                "Hun" ,
                "Atomic" ,
                "Cannon" ,
                "Iron" ,
                "Bootleg" ,
                "Amphibian" ,
                "Genghis" ,
                "Guard" ,
                "HQ" ,
                "Scuttlebutt" ,
                "Gunslinger" ,
                "Armed Force" ,
                "Trigger" ,
                "Gunner" ,
                "Napoleon" ,
                "Deck" ,
                "Pistol" ,
                "Patton" ,
                "Basecamp" ,
                "Rifle" ,
                "Tank" ,
                "Bomber" ,
                "AK47" ,
                "Sub" ,
                "D-Day" ,
                "Shield" ,
                "WMD" ,
                "Rocket" ,
                "King Kong" ,
                "Rocky" ,
                "Caesar" ,
                "Godzilla" ,
                "Robin Hood" ,
                "Joker" ,
                "Kraken" ,
                "Butch Cassidy" ,
                "Goldfinger" ,
                "Frankenstein" ,
                "Sundance Kid" ,
                "Sherlock" ,
                "Terminator" ,
                "Superman" ,
                "Watson" ,
                "Mad Max" ,
                "Tarzan" ,
                "Grinch" ,
                "Tin Man" ,
                "Rooster" ,
                "Popeye" ,
                "Scarecrow" ,
                "Moses" ,
                "Bullwinkle" ,
                "Chewbacca" ,
                "Jesus" ,
                "Daffy" ,
                "Han Solo" ,
                "Zorro" ,
                "Porky" ,
                "Captain Redbeard" ,
                "Batman" ,
                "Mr. Magoo" ,
                "Blackbeard" ,
                "Lincoln" ,
                "Jetson" ,
                "HAL" ,
                "Hannibal" ,
                "Panther" ,
                "Wizard" ,
                "Darth Vader" ,
                "Gumby" ,
                "Zodiac" ,
                "Alien" ,
                "Underdog" ,
                "V-Mort" ,
                "The Shark" ,
                "Sylvester" ,
                "C-Brown" ,
                "Martian" ,
                "Space Ghost" ,
                "Finch" ,
                "Dracula" ,
                "Felix" ,
                "Indiana" ,
                "Kevorkian" ,
                "Jungle Man" ,
                "Count" ,
                "Master" ,
                "Earl" ,
                "Cardinal" ,
                "Knight" ,
                "Governor" ,
                "Esquiare" ,
                "Abbot" ,
                "Herald" ,
                "Junior" ,
                "Admiral of the Fleet" ,
                "King of Arms" ,
                "Senior" ,
                "Professor" ,
                "Lord Privy Seal" ,
                "The Third" ,
                "Baron" ,
                "Pharoah" ,
                "Lord Charles" ,
                "Bishop" ,
                "Saint" ,
                "Uncle" ,
                "Centurion" ,
                "Sultan" ,
                "Sir" ,
                "Don" ,
                "Tribune" ,
                "Boomer" ,
                "Subwoofer" ,
                "Coma" ,
                "Buster" ,
                "Tornado" ,
                "Scratch" ,
                "Mooch" ,
                "Thunder" ,
                "Crusher" ,
                "Butch" ,
                "Lightning" ,
                "Speed" ,
                "Buzz" ,
                "Storm" ,
                "Troubleshoot" ,
                "Spike" ,
                "Bass" ,
                "Vice" ,
                "Dreads" ,
                "Gr8 Beyond" ,
                "West Coast" ,
                "Sun Belt" ,
                "German" ,
                "East Coast" ,
                "Bible Belt" ,
                "High Altitude" ,
                "Dirty Dirty" ,
                "Pacific" ,
                "Lowlands" ,
                "Rust Belt" ,
                "Atlantic" ,
                "Cornfed" ,
                "Cheese" ,
                "BBQ" ,
                "Choco" ,
                "Meatball" ,
                "Tex-Mex" ,
                "Whiskey" ,
                "Budweiser" ,
                "Ice" ,
                "Tango" ,
                "Mustard" ,
                "Snicker" ,
                "Hard-Boiled" ,
                "Nightcap" ,
                "Snow Cone" ,
                "Egg" ,
                "Salty" ,
                "Twix" ,
                "Turk" ,
                "Pepper" ,
                "Red Hot" ,
                "Beef" ,
                "Ribeye" ,
                "Tabasco" ,
                "Belly" ,
                "Rhubarb" ,
                "Cornflake" ,
                "Spice" ,
                "Seasoning" ,
                "Crunchie" ,
                "Sammie" ,
                "Vinegar" ,
                "Crisp" ,
                "Foot-long" ,
                "Piston" ,
                "Wrangler" ,
                "Rocker" ,
                "Xccelerator" ,
                "Seatbelt" ,
                "Clutch" ,
                "Motor" ,
                "Volt" ,
                "Shift" ,
                "Engine" ,
                "Power Steering" ,
                "Automatic" ,
                "Railroad" ,
                "ATV" ,
                "Gasoline" ,
                "Sparkplug" ,
                "4-Wheel Drive" ,
                "High-Octane" ,
                "Diesel" ,
                "Hog" ,
                "Gear Bait" ,
                "Cadillac" ,
                "Fuse" ,
                "Collision" ,
                "Hummer" ,
                "Hydraulics" ,
                "Nut" ,
                "Drum" ,
                "Mega" ,
                "HTML" ,
                "The Keys" ,
                "Fiddle" ,
                "8-track" ,
                "Pipes" ,
                "Breakdance" ,
                "Frontflip" ,
                "Picasso" ,
                "Half-Pipe" ,
                "Hemingway" ,
                "Einstein" ,
                "Kickback" ,
                "Nobel Prize" ,
                "Banjo" ,
                "Hard-Drive" ,
                "Green Thumb" ,
                "Nintendo" ,
                "2-bit" ,
                "Lock Stock" ,
                "Sega" ,
                "Code Master" ,
                "Target" ,
                "Kraken" ,
                "Boomer" ,
                "Lumberjack" ,
                "Boomerang" ,
                "Mammoth" ,
                "Boss" ,
                "Mastadon" ,
                "Budweiser" ,
                "Master" ,
                "Bullseye" ,
                "Meatball" ,
                "Buster" ,
                "Mooch" ,
                "Butch" ,
                "Buzz" ,
                "Outlaw" ,
                "Canine" ,
                "Ratman" ,
                "Captian RedBeard" ,
                "Renegade" ,
                "Champ" ,
                "Sabertooth" ,
                "Coma" ,
                "Scratch" ,
                "Crusher" ,
                "Sentinel" ,
                "Diesel" ,
                "Speed" ,
                "Doctor" ,
                "Spike" ,
                "Dreads" ,
                "Subwoofer" ,
                "Frankenstein" ,
                "Thunderbird" ,
                "Froggy" ,
                "Tornado" ,
                "General" ,
                "Troubleshoot" ,
                "Godzilla" ,
                "Vice" ,
                "Hammerhead" ,
                "Viper" ,
                "Handy Man" ,
                "Wasp" ,
                "Hound Dog" ,
                "Wizard" ,
                "Indominus" ,
                "Zodiac" ,
                "King Kong" ,
                "Mammoth" ,
                "Lobster" ,
                "Highlander" ,
                "Mastodon" ,
                "Slug" ,
                "Prawn" ,
                "Canine" ,
                "Spider" ,
                "Taz" ,
                "Ratman" ,
                "Snake" ,
                "Sabre-Tooth" ,
                "Sabertooth" ,
                "Gecko" ,
                "Bear" ,
                "Hammerhead" ,
                "Dragon" ,
                "Yak" ,
                "Viper" ,
                "Vulture" ,
                "Zee-donk" ,
                "Thunderbird" ,
                "Fish" ,
                "Dino" ,
                "Froggy" ,
                "Jackal" ,
                "T-Rex" ,
                "Wasp" ,
                "Megalodon" ,
                "Raptor" ,
                "Hound Dog" ,
                "Bandicoot" ,
                "Bird" ,
                "Wildcat" , "Bulldog" , "Gator" , "Husky" , "Catfish" , "Bull" , "Trunk" ,
                "Dingo" , "Longhorn" , "Boomerang" ,"Sticks" , "Horsehide" , "Helmet" , "Tackle" , "Hose" , "The Bat" , "Wishbone" , "Hot Box" ,
                "Field Goal" , "X" , "Rain Delay" , "Touchdown" , "Z" , "Interstate" , "TD" , "ZD" ,
                "J-Run" , "Foul Play" , "ZB" , "Junk" , "Red Flag" , "Ace" , "Knuckleball" ,
                "Endzone" , "Airmail" , "Knee-Buckler" , "Defense" , "Aspirin" , "Leather" , "Offense" , "Bail" ,
                "Lights Out" , "First Down" , "Banjo Hitter" , "Lumber" , "Chip Shot" , "Barrel" , "Maddux" ,
                "Snap" , "Batter" , "Masher" , "Quarterback" , "Belt" , "Meat" , "QB" , "Big League" , "Bullseye" , "LB" ,
                "Major League" , "Dart" , "Flea Flicker" , "Bigs" , "Midnight" ,
                "Fourth Down" , "Bronx Bomber" , "Moonshot" , "FD" , "Bullpen" ,
                "Olympus" , "Fumble" , "Butcher Boy" , "Relay" , "Fullback" , "Captain Hook" , "Ribbie" ,
                "FB" , "The Chair" , "Road Game" , "Gridiron" , "Cheddar" , "Rookie" , "Halfback" ,
                "Chuck" , "Room Service" , "HB" , "Clinic" , "Shoe String" , "Huddle", "Cookie" , "The Show" , "Interference" ,
                "Creature" , "Showboat" , "Jack" , "Daisy Cutter" , "Shut Out" , "Mo" , "Dead Red" , "Sinker" ,
                "Jumbo" , "Deuce" , "Skyscraper" , "Juke" , "Earnie" ,
                "Slap Hitter" , "Monster Man" , "Evil Empire" , "Smoke" , "NFL" , "Ump" , "Southpaw" ,
                "Pancake" , "Baller" , "Stats" , "Red Shirt" , "Flamethrower" , "Stopper" , "Red Zone" , "GM" ,
                "Stretch" , "Ref" , "Grand Slam" , "Tailgate" , "Rover" , "Hammer" , "Tomahawk" , "Sam" ,
                "Hat Trick" , "Uncle Charlie" , "Sideline" , "Goalie" , "Yardwork" ,
                "Side Zone" , "Offsides" , "Zinger" , "Slobber-knocker" , "Home Plate" ,
                "Ballgame" , "Sneak" , "Homer" , "Matchpoint" , "Hook" , "Basket"};
                // getBaseContext().getResources().getStringArray(R.array.names_array);
        String selectedName = name[new Random().nextInt(name.length)];
        return selectedName;
    }


    @Override
    public void onBackPressed() {
        new OurToast().myToast(getBaseContext() , getString(R.string.you_must_login_first));
    }
}
