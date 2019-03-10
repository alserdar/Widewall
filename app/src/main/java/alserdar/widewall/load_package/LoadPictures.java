package alserdar.widewall.load_package;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.telephony.TelephonyManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import alserdar.widewall.OurToast;
import alserdar.widewall.R;
import alserdar.widewall.models.UserModel;

import static android.content.Context.TELEPHONY_SERVICE;

public class LoadPictures {

    public static void loadPicsForHome (final Context context , final String theUID , final ImageView pic)
    {
        Handler mainHandler = new Handler(context.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference doc = db.collection("UserInformation").document(theUID);
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {
                            UserModel model = documentSnapshot.toObject(UserModel.class);
                            boolean hasPic = model.isHasPic();
                            String gender = model.getGender();

                            if (hasPic)
                            {

                                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ theUID);
                                Glide.with(context.getApplicationContext())
                                        .using(new FirebaseImageLoader())
                                        .load(storageReference)
                                        .asBitmap()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .priority(Priority.IMMEDIATE)
                                        .into(new BitmapImageViewTarget(pic) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                                        Bitmap.createScaledBitmap(resource, 150, 150, false));
                                                drawable.setCircular(true);
                                                pic.setImageDrawable(drawable);
                                            }
                                        });
                            }else
                            {
                                if (gender.equals("male"))
                                {
                                    StorageReference storageMale = FirebaseStorage.getInstance().getReference().child("images/"+ "male.png");
                                    Glide.with(context.getApplicationContext())
                                            .using(new FirebaseImageLoader())
                                            .load(storageMale)
                                            .asBitmap()
                                            .into(new BitmapImageViewTarget(pic) {
                                                @Override
                                                protected void setResource(Bitmap resource) {
                                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                                            Bitmap.createScaledBitmap(resource, 150, 150, false));
                                                    drawable.setCircular(true);
                                                    pic.setImageDrawable(drawable);
                                                }
                                            });

                                }else if (gender.equals("female"))
                                {
                                    StorageReference storageFemale = FirebaseStorage.getInstance().getReference().child("images/"+ "female.png");
                                    Glide.with(context.getApplicationContext())
                                            .using(new FirebaseImageLoader())
                                            .load(storageFemale)
                                            .asBitmap()
                                            .into(new BitmapImageViewTarget(pic) {
                                                @Override
                                                protected void setResource(Bitmap resource) {
                                                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                                            Bitmap.createScaledBitmap(resource, 150, 150, false));
                                                    drawable.setCircular(true);
                                                    pic.setImageDrawable(drawable);
                                                }
                                            });
                                }
                            }

                        }
                    }
                });
            }
        };
        mainHandler.post(myRunnable);
    }

    public static void setCountryFlag(final Context context , final ImageView pic)
    {
        TelephonyManager telephonyManager;
        String Country = null;
        String dialCode ;
        String currentCountry = null;
        int simState ;

        telephonyManager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        simState = telephonyManager.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                pic.setBackgroundResource(R.mipmap.flag);
                break;

            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                pic.setBackgroundResource(R.mipmap.flag);
                break;

            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                pic.setBackgroundResource(R.mipmap.flag);
                break;

            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                pic.setBackgroundResource(R.mipmap.flag);
                break;

            case TelephonyManager.SIM_STATE_UNKNOWN:
                pic.setBackgroundResource(R.mipmap.flag);
                break;

            case TelephonyManager.SIM_STATE_READY:
                //new OurToast().myToast(getBaseContext() ,"SimCard is READY" );
                try {
                    Country = telephonyManager.getSimCountryIso();
                    if (Country != null && Country.length() == 2) {

                        // Toast.makeText(getBaseContext(), Country.toUpperCase(), Toast.LENGTH_LONG).show();
                    }
                    else if (telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA)
                    {
                        Country = telephonyManager.getNetworkCountryIso();
                        if (Country != null && Country.length() == 2)
                        {
                            new OurToast().myToast(context, currentCountry );
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

                if (Country != null) {
                    switch (Country.toUpperCase())
                    {

                        case "EG" :
                            currentCountry = "Egypt";
                            pic.setBackgroundResource(R.mipmap.egypt);
                            break;
                        case "AF" :
                            currentCountry = "Afghanistan";
                            pic.setBackgroundResource(R.mipmap.afghanistan);
                            break;
                        case "AX" :
                            currentCountry = "Aland Islands"; // withour flag
                            pic.setBackgroundResource(R.mipmap.aland);
                            break;
                        case "AL" :
                            currentCountry = "Albania";
                            pic.setBackgroundResource(R.mipmap.albania);
                            break;
                        case "DZ" :
                            currentCountry = "algeria";
                            pic.setBackgroundResource(R.mipmap.algeria);
                            break;
                        case "AS" :
                            currentCountry = "American Samoa";
                            pic.setBackgroundResource(R.mipmap.american_samoa);
                            break;
                        case "AD" :
                            currentCountry = "Andorra";
                            pic.setBackgroundResource(R.mipmap.andorra);
                            break;
                        case "AO" :
                            currentCountry = "Angola";
                            pic.setBackgroundResource(R.mipmap.angola);
                            break;
                        case "AI" :
                            currentCountry = "Anguilla";
                            pic.setBackgroundResource(R.mipmap.anguilla);
                            break;
                        case "AQ" :
                            currentCountry = "Antarctica"; // withour flag
                            pic.setBackgroundResource(R.mipmap.antractica);
                            break;
                        case "AG" :
                            currentCountry = "Antigua and Barbuda";
                            pic.setBackgroundResource(R.mipmap.antigua_and_barbuda);
                            break;
                        case "AR" :
                            currentCountry = "Argentina";
                            pic.setBackgroundResource(R.mipmap.argentina);
                            break;
                        case "AM" :
                            currentCountry = "Armenia";
                            pic.setBackgroundResource(R.mipmap.armenia);
                            break;
                        case "AW" :
                            currentCountry = "Aruba";
                            pic.setBackgroundResource(R.mipmap.aruba);
                            break;
                        case "AC" :
                            currentCountry = "Ascension Island";
                            pic.setBackgroundResource(R.mipmap.ascension);
                            break;
                        case "AU" :
                            currentCountry = "Australia";
                            pic.setBackgroundResource(R.mipmap.australia);
                            break;
                        case "AT" :
                            currentCountry = "Austria";
                            pic.setBackgroundResource(R.mipmap.austria);
                            break;
                        case "AZ" :
                            currentCountry = "Azerbaijan";
                            pic.setBackgroundResource(R.mipmap.azerbaijan);
                            break;
                        case "BS" :
                            currentCountry = "Bahamas";
                            pic.setBackgroundResource(R.mipmap.bahamas);
                            break;
                        case "BH" :
                            currentCountry = "Bahrain";
                            pic.setBackgroundResource(R.mipmap.bahrain);
                            break;
                        case "BB" :
                            currentCountry = "Barbados";
                            pic.setBackgroundResource(R.mipmap.barbados);
                            break;
                        case "BD" :
                            currentCountry = "Bangladesh";
                            pic.setBackgroundResource(R.mipmap.bangladesh);
                            break;
                        case "BY" :
                            currentCountry = "Belarus";
                            pic.setBackgroundResource(R.mipmap.belarus);
                            break;
                        case "BE" :
                            currentCountry = "Belgium";
                            pic.setBackgroundResource(R.mipmap.belgium);
                            break;
                        case "BZ" :
                            currentCountry = "Belize";
                            pic.setBackgroundResource(R.mipmap.belize);
                            break;
                        case "BJ" :
                            currentCountry = "Benin";
                            pic.setBackgroundResource(R.mipmap.benin);
                            break;
                        case "BM" :
                            currentCountry = "Bermuda";
                            pic.setBackgroundResource(R.mipmap.bermuda);
                            break;
                        case "BT" :
                            currentCountry = "Bhutan";
                            pic.setBackgroundResource(R.mipmap.bhutan);
                            break;
                        case "BW" :
                            currentCountry = "Botswana";
                            pic.setBackgroundResource(R.mipmap.botswana);
                            break;
                        case "BO" :
                            currentCountry = "Bolivia";
                            pic.setBackgroundResource(R.mipmap.bolivia);
                            break;
                        case "BA" :
                            currentCountry = "Bosnia and Herzegovina";
                            pic.setBackgroundResource(R.mipmap.bosnia);
                            break;
                        case "BV" :
                            currentCountry = "Bouvet Island";
                            pic.setBackgroundResource(R.mipmap.norway);
                            break;
                        case "BR" :
                            currentCountry = "Brazil";
                            pic.setBackgroundResource(R.mipmap.brazil);
                            break;
                        case "IO" :
                            currentCountry = "British Indian Ocean Territory"; // withour flag
                            pic.setBackgroundResource(R.mipmap.british_indian_ocean_territory);
                            break;
                        case "BN" :
                            currentCountry = "Brunei Darussalam";
                            pic.setBackgroundResource(R.mipmap.brunei);
                            break;
                        case "BG" :
                            currentCountry = "Bulgaria";
                            pic.setBackgroundResource(R.mipmap.bulgaria);
                            break;
                        case "BF" :
                            currentCountry = "Burkina Faso";
                            pic.setBackgroundResource(R.mipmap.burkina_faso);
                            break;

                        case "BI" :
                            currentCountry = "Burundi";
                            pic.setBackgroundResource(R.mipmap.burundi);
                            break;
                        case "KH" :
                            currentCountry = "Cambodia";
                            pic.setBackgroundResource(R.mipmap.cambodia);
                            break;
                        case "CM" :
                            currentCountry = "Cameroon";
                            pic.setBackgroundResource(R.mipmap.cameroon);
                            break;
                        case "CA" :
                            currentCountry = "Canada";
                            pic.setBackgroundResource(R.mipmap.canada);
                            break;
                        case "CV" :
                            currentCountry = "Cape Verde";
                            pic.setBackgroundResource(R.mipmap.cape_verde);
                            break;
                        case "KY" :
                            currentCountry = "Cayman Islands";
                            pic.setBackgroundResource(R.mipmap.cayman_islands);
                            break;
                        case "CF" :
                            currentCountry = "Central African Republic";
                            pic.setBackgroundResource(R.mipmap.central_african_republic);
                            break;
                        case "TD" :
                            currentCountry = "Chad";
                            pic.setBackgroundResource(R.mipmap.chad);
                            break;
                        case "CL" :
                            currentCountry = "Chile";
                            pic.setBackgroundResource(R.mipmap.chile);
                            break;
                        case "CN" :
                            currentCountry = "China";
                            pic.setBackgroundResource(R.mipmap.china);
                            break;
                        case "CX" :
                            currentCountry = "Christmas Island";
                            pic.setBackgroundResource(R.mipmap.christmas_island);
                            break;
                        case "CC" :
                            currentCountry = "Cocos (Keeling) Islands";
                            pic.setBackgroundResource(R.mipmap.cocos_islands);
                            break;
                        case "CO" :
                            currentCountry = "Colombia";
                            pic.setBackgroundResource(R.mipmap.colombia);
                            break;
                        case "KM" :
                            currentCountry = "Comoros";
                            pic.setBackgroundResource(R.mipmap.comoros);
                            break;
                        case "CG" :
                            currentCountry = "Congo";
                            pic.setBackgroundResource(R.mipmap.republic_of_the_congo);
                            break;
                        case "CD" :
                            currentCountry = "Congo, Democratic Republic";
                            pic.setBackgroundResource(R.mipmap.democratic_republic_of_the_congo);
                            break;
                        case "CK" :
                            currentCountry = "Cook Islands";
                            pic.setBackgroundResource(R.mipmap.cook_islands);
                            break;
                        case "CR" :
                            currentCountry = "Costa Rica";
                            pic.setBackgroundResource(R.mipmap.costa_rica);
                            break;
                        case "CI" :
                            currentCountry = "Cote D'Ivoire (Ivory Coast)";
                            pic.setBackgroundResource(R.mipmap.cote_d_voire);
                            break;
                        case "HR" :
                            currentCountry = "Croatia (Hrvatska)";
                            pic.setBackgroundResource(R.mipmap.croatia);
                            break;
                        case "CU" :
                            currentCountry = "Cuba";
                            pic.setBackgroundResource(R.mipmap.cuba);
                            break;
                        case "CY" :
                            currentCountry = "Cyprus";
                            pic.setBackgroundResource(R.mipmap.cyprus);
                            break;
                        case "CZ" :
                            currentCountry = "Czech Republic";
                            pic.setBackgroundResource(R.mipmap.czech_republic);
                            break;
                        case "CS" :
                            currentCountry = "Czechoslovakia (former)";
                            pic.setBackgroundResource(R.mipmap.czech_republic);
                            break;
                        case "DK" :
                            currentCountry = "Denmark";
                            pic.setBackgroundResource(R.mipmap.denmark);
                            break;
                        case "DJ" :
                            currentCountry = "Djibouti";
                            pic.setBackgroundResource(R.mipmap.djibouti);
                            break;
                        case "DM" :
                            currentCountry = "Dominica";
                            pic.setBackgroundResource(R.mipmap.dominica);
                            break;
                        case "DO" :
                            currentCountry = "Dominican Republic";
                            pic.setBackgroundResource(R.mipmap.dominican_republic);
                            break;
                        case "TP" :
                            currentCountry = "East Timor";
                            pic.setBackgroundResource(R.mipmap.timor_leste);
                            break;
                        case "EC" :
                            currentCountry = "Ecuador";
                            pic.setBackgroundResource(R.mipmap.ecuador);
                            break;
                        case "SV" :
                            currentCountry = "El Salvador";
                            pic.setBackgroundResource(R.mipmap.el_salvador);
                            break;
                        case "GQ" :
                            currentCountry = "Equatorial Guinea";
                            pic.setBackgroundResource(R.mipmap.equatorial_guinea);
                            break;
                        case "ER" :
                            currentCountry = "Eritrea";
                            pic.setBackgroundResource(R.mipmap.eritrea);
                            break;
                        case "EE" :
                            currentCountry = "Estonia";
                            pic.setBackgroundResource(R.mipmap.estonia);
                            break;
                        case "ET" :
                            currentCountry = "Ethiopia";
                            pic.setBackgroundResource(R.mipmap.ethiopia);
                            break;
                        case "FK" :
                            currentCountry = "Falkland Islands (Malvinas)";
                            pic.setBackgroundResource(R.mipmap.falkland_islands);
                            break;
                        case "FO" :
                            currentCountry = "Faroe Islands";
                            pic.setBackgroundResource(R.mipmap.faroe_islands);
                            break;
                        case "FJ" :
                            currentCountry = "Fiji";
                            pic.setBackgroundResource(R.mipmap.fiji);
                            break;
                        case "FI" :
                            currentCountry = "Finland";
                            pic.setBackgroundResource(R.mipmap.finland);
                            break;
                        case "FR" :
                            currentCountry = "France";
                            pic.setBackgroundResource(R.mipmap.france);
                            break;
                        case "FX" :currentCountry = "France, Metropolitan";
                            pic.setBackgroundResource(R.mipmap.france);
                            break;
                        case "GF" :currentCountry = "French Guiana";
                            pic.setBackgroundResource(R.mipmap.french_guiana);
                            break;
                        case "PF" :currentCountry = "French Polynesia";
                            pic.setBackgroundResource(R.mipmap.french_polynesia);
                            break;
                        case "TF" :currentCountry = "French Southern Territories";
                            pic.setBackgroundResource(R.mipmap.french_territories);
                            break;
                        case "MK" :currentCountry = "Macedonia";
                            pic.setBackgroundResource(R.mipmap.macedonia);
                            break;
                        case "GA" : currentCountry = "Gabon";
                            pic.setBackgroundResource(R.mipmap.gabon);
                            break;
                        case "GM" :currentCountry = "Gambia";
                            pic.setBackgroundResource(R.mipmap.gambia);
                            break;
                        case "GE" :currentCountry = "Georgia";
                            pic.setBackgroundResource(R.mipmap.georgia);
                            break;
                        case "DE" :currentCountry = "Germany";
                            pic.setBackgroundResource(R.mipmap.germany);
                            break;
                        case "GH" :currentCountry = "Ghana";
                            pic.setBackgroundResource(R.mipmap.ghana);
                            break;
                        case "GI" :currentCountry = "Gibraltar";
                            pic.setBackgroundResource(R.mipmap.gibraltar);
                            break;
                        case "GB" :currentCountry = "Great Britain (UK)";
                            pic.setBackgroundResource(R.mipmap.united_kingdom);
                            break;
                        case "GR" :currentCountry = "Greece";
                            pic.setBackgroundResource(R.mipmap.greece);
                            break;
                        case "GL" :currentCountry = "Greenland";
                            pic.setBackgroundResource(R.mipmap.greenland);
                            break;
                        case "GD" :currentCountry = "Grenada";
                            pic.setBackgroundResource(R.mipmap.grenada);
                            break;
                        case "GP" :currentCountry = "Guadeloupe";
                            pic.setBackgroundResource(R.mipmap.guadeloupe);
                            break;
                        case "GU" :currentCountry = "Guam";
                            pic.setBackgroundResource(R.mipmap.guam);
                            break;
                        case "GT" :currentCountry = "Guatemala";
                            pic.setBackgroundResource(R.mipmap.guatemala);
                            break;
                        case "GG" :currentCountry = "Guernsey";
                            pic.setBackgroundResource(R.mipmap.guernsey);
                            break;
                        case "GN" :currentCountry = "Guinea";
                            pic.setBackgroundResource(R.mipmap.guinea);
                            break;
                        case "GW" :currentCountry = "Guinea-Bissau";
                            pic.setBackgroundResource(R.mipmap.guinea_bissau);
                            break;
                        case "GY" :currentCountry = "Guyana";
                            pic.setBackgroundResource(R.mipmap.guyana);
                            break;
                        case "HT" :currentCountry = "Haiti";
                            pic.setBackgroundResource(R.mipmap.haiti);
                            break;
                        case "HM" :currentCountry = "Heard and McDonald Islands";
                            pic.setBackgroundResource(R.mipmap.mcdonald_islands);
                            break;
                        case "HN" :currentCountry = "Honduras";
                            pic.setBackgroundResource(R.mipmap.honduras);
                            break;
                        case "HK" :currentCountry = "Hong Kong";
                            pic.setBackgroundResource(R.mipmap.hong_kong);
                            break;
                        case "HU" :currentCountry = "Hungary";
                            pic.setBackgroundResource(R.mipmap.hungary);
                            break;
                        case "IS" :currentCountry = "Iceland";
                            pic.setBackgroundResource(R.mipmap.iceland);
                            break;
                        case "IN" :currentCountry = "India";
                            pic.setBackgroundResource(R.mipmap.india);
                            break;
                        case "ID" :currentCountry = "Indonesia";
                            pic.setBackgroundResource(R.mipmap.indonesia);
                            break;
                        case "IR" :currentCountry = "Iran";
                            pic.setBackgroundResource(R.mipmap.iran);
                            break;
                        case "IQ" :currentCountry = "Iraq";
                            pic.setBackgroundResource(R.mipmap.iraq);
                            break;
                        case "IE" :currentCountry = "Ireland";
                            pic.setBackgroundResource(R.mipmap.ireland);
                            break;
                        case "IL" :currentCountry = "Palestine";
                            pic.setBackgroundResource(R.mipmap.palestine);
                            break;
                        case "IM" :currentCountry = "Isle of Man";
                            pic.setBackgroundResource(R.mipmap.isle_of_man);
                            break;
                        case "IT" :currentCountry = "Italy";
                            pic.setBackgroundResource(R.mipmap.italy);
                            break;
                        case "JE" :currentCountry = "Jersey";
                            pic.setBackgroundResource(R.mipmap.jersey);
                            break;
                        case "JM" :currentCountry = "Jamaica";
                            pic.setBackgroundResource(R.mipmap.jamaica);
                            break;
                        case "JP" :currentCountry = "Japan";
                            pic.setBackgroundResource(R.mipmap.japan);
                            break;
                        case "JO" :currentCountry = "Jordan";
                            pic.setBackgroundResource(R.mipmap.jordan);
                            break;
                        case "KZ" :currentCountry = "Kazakhstan";
                            pic.setBackgroundResource(R.mipmap.kazakhstan);
                            break;
                        case "KE" :currentCountry = "Kenya";
                            pic.setBackgroundResource(R.mipmap.kenya);
                            break;
                        case "KI" :currentCountry = "Kiribati";
                            pic.setBackgroundResource(R.mipmap.kiribati);
                            break;
                        case "KP" :currentCountry = "Korea (North)";
                            pic.setBackgroundResource(R.mipmap.north_korea);
                            break;
                        case "KR" :currentCountry = "Korea (South)";
                            pic.setBackgroundResource(R.mipmap.south_korea);
                            break;
                        case "KW" :currentCountry = "Kuwait";
                            pic.setBackgroundResource(R.mipmap.kuwait);
                            break;
                        case "KG" :currentCountry = "Kyrgyzstan";
                            pic.setBackgroundResource(R.mipmap.kyrgyzstan);
                            break;
                        case "LA" :currentCountry = "Laos";
                            pic.setBackgroundResource(R.mipmap.laos);
                            break;
                        case "LV" :currentCountry = "Latvia";
                            pic.setBackgroundResource(R.mipmap.latvia);
                            break;
                        case "LB" :currentCountry = "Lebanon";
                            pic.setBackgroundResource(R.mipmap.lebanon);
                            break;
                        case "LI" :currentCountry = "Liechtenstein";
                            pic.setBackgroundResource(R.mipmap.liechtenstein);
                            break;
                        case "LR" :currentCountry = "Liberia";
                            pic.setBackgroundResource(R.mipmap.liberia);
                            break;
                        case "LY" :currentCountry = "Libya";
                            pic.setBackgroundResource(R.mipmap.libya);
                            break;
                        case "LS" :currentCountry = "Lesotho";
                            pic.setBackgroundResource(R.mipmap.lesotho);
                            break;
                        case "LT" :currentCountry = "Lithuania";
                            pic.setBackgroundResource(R.mipmap.lithuania);
                            break;
                        case "LU" :currentCountry = "Luxembourg";
                            pic.setBackgroundResource(R.mipmap.luxembourg);
                            break;
                        case "MO" :currentCountry = "Macau";
                            pic.setBackgroundResource(R.mipmap.macao);
                            break;
                        case "MG" :currentCountry = "Madagascar";
                            pic.setBackgroundResource(R.mipmap.madagascar);
                            break;
                        case "MW" :currentCountry = "Malawi";
                            pic.setBackgroundResource(R.mipmap.malawi);
                            break;
                        case "MY" :currentCountry = "Malaysia";
                            pic.setBackgroundResource(R.mipmap.malaysia);
                            break;
                        case "MV" :currentCountry = "Maldives";
                            pic.setBackgroundResource(R.mipmap.maldives);
                            break;
                        case "ML" :currentCountry = "Mali";
                            pic.setBackgroundResource(R.mipmap.mali);
                            break;
                        case "MT" :currentCountry = "Malta";
                            pic.setBackgroundResource(R.mipmap.malta);
                            break;
                        case "MH" :currentCountry = "Marshall Islands";
                            pic.setBackgroundResource(R.mipmap.marshall_islands);
                            break;
                        case "MQ" :currentCountry = "martinique";
                            pic.setBackgroundResource(R.mipmap.martinique);
                            break;
                        case "MR" :currentCountry = "mauritania";
                            pic.setBackgroundResource(R.mipmap.mauritania);
                            break;
                        case "MU" :currentCountry = "mauritius";
                            pic.setBackgroundResource(R.mipmap.mauritius);
                            break;
                        case "YT" :currentCountry = "Mayotte";
                            pic.setBackgroundResource(R.mipmap.mayotte);
                            break;
                        case "MX" :currentCountry = "mexico";
                            pic.setBackgroundResource(R.mipmap.mexico);
                            break;
                        case "FM" :currentCountry = "micronesia";
                            pic.setBackgroundResource(R.mipmap.micronesia);
                            break;
                        case "MC" :currentCountry = "monaco";
                            pic.setBackgroundResource(R.mipmap.monaco);
                            break;
                        case "MD" :currentCountry = "moldova";
                            pic.setBackgroundResource(R.mipmap.moldova);
                            break;
                        case "MN" :currentCountry = "mongolia";
                            pic.setBackgroundResource(R.mipmap.mongolia);
                            break;
                        case "ME" :currentCountry = "Montenegro";
                            pic.setBackgroundResource(R.mipmap.serbia_and_montenegro);
                            break;
                        case "MS" :currentCountry = "montserrat";
                            pic.setBackgroundResource(R.mipmap.montserrat);
                            break;
                        case "MA" :currentCountry = "morocco";
                            pic.setBackgroundResource(R.mipmap.morocco);
                            break;
                        case "MZ" :currentCountry = "Mozambique";
                            pic.setBackgroundResource(R.mipmap.mozambique);
                            break;
                        case "MM" :currentCountry = "Myanmar";
                            pic.setBackgroundResource(R.mipmap.myanmar);
                            break;
                        case "NA" :currentCountry = "Namibia";
                            pic.setBackgroundResource(R.mipmap.namibia);
                            break;
                        case "NR" :currentCountry = "Nauru";
                            pic.setBackgroundResource(R.mipmap.nauru);
                            break;
                        case "NP" :currentCountry = "Nepal";
                            pic.setBackgroundResource(R.mipmap.nepal);
                            break;
                        case "NL" :currentCountry = "Netherlands";
                            pic.setBackgroundResource(R.mipmap.netherlands);
                            break;
                        case "AN" :currentCountry = "Netherlands Antilles";
                            pic.setBackgroundResource(R.mipmap.netherlands_antilles);
                            break;
                        case "NT" :currentCountry = "Neutral Zone";
                            pic.setBackgroundResource(R.mipmap.flag_red);
                            break;
                        case "NC" :currentCountry = "New Caledonia";
                            pic.setBackgroundResource(R.mipmap.new_caledonia);
                            break;
                        case "NZ" :currentCountry = "New Zealand (Aotearoa)";
                            pic.setBackgroundResource(R.mipmap.new_zealand);
                            break;
                        case "NI" :currentCountry = "Nicaragua";
                            pic.setBackgroundResource(R.mipmap.nicaragua);
                            break;
                        case "NE" :currentCountry = "Niger";
                            pic.setBackgroundResource(R.mipmap.niger);
                            break;
                        case "NG" :currentCountry = "Nigeria";
                            pic.setBackgroundResource(R.mipmap.nigeria);
                            break;
                        case "NU" :currentCountry = "Niue";
                            pic.setBackgroundResource(R.mipmap.niue);
                            break;
                        case "NF" :currentCountry = "Norfolk Island";
                            pic.setBackgroundResource(R.mipmap.norfolk_island);
                            break;
                        case "MP" :currentCountry = "Northern Mariana Islands";
                            pic.setBackgroundResource(R.mipmap.northern_mariana_islands);
                            break;
                        case "NO" :currentCountry = "Norway";
                            pic.setBackgroundResource(R.mipmap.norway);
                            break;
                        case "OM" :currentCountry = "Oman";
                            pic.setBackgroundResource(R.mipmap.oman);
                            break;
                        case "PK" :currentCountry = "Pakistan";
                            pic.setBackgroundResource(R.mipmap.pakistan);
                            break;
                        case "PW" :currentCountry = "Palau";
                            pic.setBackgroundResource(R.mipmap.palau);
                            break;
                        case "PS" :currentCountry = "Palestine";
                            pic.setBackgroundResource(R.mipmap.palestine);
                            break;
                        case "PA" :currentCountry = "Panama";
                            pic.setBackgroundResource(R.mipmap.panama);
                            break;
                        case "PG" :currentCountry = "Papua New Guinea";
                            pic.setBackgroundResource(R.mipmap.papua_new_guinea);
                            break;
                        case "PY" :currentCountry = "Paraguay";
                            pic.setBackgroundResource(R.mipmap.paraguay);
                            break;
                        case "PE" :currentCountry = "Peru";
                            pic.setBackgroundResource(R.mipmap.peru);
                            break;
                        case "PH" :currentCountry = "Philippines";
                            pic.setBackgroundResource(R.mipmap.philippines);
                            break;
                        case "PN" :currentCountry = "Pitcairn";
                            pic.setBackgroundResource(R.mipmap.pitcairn_islands);
                            break;
                        case "PL" :currentCountry = "Poland";
                            pic.setBackgroundResource(R.mipmap.poland);
                            break;
                        case "PT" :currentCountry = "Portugal";
                            pic.setBackgroundResource(R.mipmap.portugal);
                            break;
                        case "PR" :currentCountry = "Puerto Rico";
                            pic.setBackgroundResource(R.mipmap.puerto_rico);
                            break;
                        case "QA" :currentCountry = "Qatar";
                            pic.setBackgroundResource(R.mipmap.qatar);
                            break;
                        case "RE" :currentCountry = "Reunion";
                            pic.setBackgroundResource(R.mipmap.flag_red);
                            break;
                        case "RO" :currentCountry = "Romania";
                            pic.setBackgroundResource(R.mipmap.romania);
                            break;
                        case "RU" :currentCountry = "Russian Federation";
                            pic.setBackgroundResource(R.mipmap.russian_federation);
                            break;
                        case "RW" :currentCountry = "Rwanda";
                            pic.setBackgroundResource(R.mipmap.rwanda);
                            break;
                        case "GS" :currentCountry = "S. Georgia";
                            pic.setBackgroundResource(R.mipmap.south_georgia);
                            break;
                        case "KN" :currentCountry = "Saint Kitts and Nevis";
                            pic.setBackgroundResource(R.mipmap.saint_kitts_and_nevis);
                            break;
                        case "LC" :currentCountry = "Saint Lucia";
                            pic.setBackgroundResource(R.mipmap.saint_lucia);
                            break;
                        case "VC" :currentCountry = "Saint Vincent & the Grenadines";
                            pic.setBackgroundResource(R.mipmap.saint_vicent_and_the_grenadines);
                            break;
                        case "WS" :currentCountry = "Samoa";
                            pic.setBackgroundResource(R.mipmap.samoa);
                            break;
                        case "SM" :currentCountry = "San Marino";
                            pic.setBackgroundResource(R.mipmap.san_marino);
                            break;
                        case "ST" :currentCountry = "Sao Tome and Principe";
                            pic.setBackgroundResource(R.mipmap.sao_tome_and_principe);
                            break;
                        case "SA" :currentCountry = "Saudi Arabia";
                            pic.setBackgroundResource(R.mipmap.saudi_arabia);
                            break;
                        case "SN" :currentCountry = "Senegal";
                            pic.setBackgroundResource(R.mipmap.senegal);
                            break;
                        case "RS" :currentCountry = "Serbia";
                            pic.setBackgroundResource(R.mipmap.serbia);
                            break;
                        case "YU" :currentCountry = "Serbia and Montenegro";
                            pic.setBackgroundResource(R.mipmap.serbia_and_montenegro);
                            break;
                        case "SC" :currentCountry = "Seychelles";
                            pic.setBackgroundResource(R.mipmap.seychelles);
                            break;
                        case "SL" :currentCountry = "Sierra Leone";
                            pic.setBackgroundResource(R.mipmap.sierra_leone);
                            break;
                        case "SG" :currentCountry = "Singapore";
                            pic.setBackgroundResource(R.mipmap.singapore);
                            break;
                        case "SI" :currentCountry = "Slovenia";
                            pic.setBackgroundResource(R.mipmap.slovenia);
                            break;
                        case "SK" :currentCountry = "Slovak Republic";
                            pic.setBackgroundResource(R.mipmap.slovakia);
                            break;
                        case "SB" :currentCountry = "Solomon Islands";
                            pic.setBackgroundResource(R.mipmap.soloman_islands);
                            break;
                        case "SO" :currentCountry = "Somalia";
                            pic.setBackgroundResource(R.mipmap.somalia);
                            break;
                        case "ZA" :currentCountry = "South Africa";
                            pic.setBackgroundResource(R.mipmap.south_africa);
                            break;
                        case "ES" :currentCountry = "Spain";
                            pic.setBackgroundResource(R.mipmap.spain);
                            break;
                        case "LK" :currentCountry = "Sri Lanka";
                            pic.setBackgroundResource(R.mipmap.sri_lanka);
                            break;
                        case "SH" :currentCountry = "St. Helena";
                            pic.setBackgroundResource(R.mipmap.st_helena);
                            break;
                        case "PM" :currentCountry = "St. Pierre and Miquelon";
                            pic.setBackgroundResource(R.mipmap.st_pierre__miquelon);
                            break;
                        case "SD" :currentCountry = "Sudan";
                            pic.setBackgroundResource(R.mipmap.sudan);
                            break;
                        case "SR" :currentCountry = "Suriname";
                            pic.setBackgroundResource(R.mipmap.suriname);
                            break;
                        case "SJ" :currentCountry = "Svalbard & Jan Mayen Islands";
                            pic.setBackgroundResource(R.mipmap.norway);
                            break;
                        case "SZ" :currentCountry = "Swaziland";
                            pic.setBackgroundResource(R.mipmap.swaziland);
                            break;
                        case "SE" :currentCountry = "Sweden";
                            pic.setBackgroundResource(R.mipmap.sweden);
                            break;
                        case "CH" :currentCountry = "Switzerland";
                            pic.setBackgroundResource(R.mipmap.switzerland);
                            break;
                        case "SY" :currentCountry = "Syria";
                            pic.setBackgroundResource(R.mipmap.syria);
                            break;
                        case "TW" :currentCountry = "Taiwan";
                            pic.setBackgroundResource(R.mipmap.taiwan);
                            break;
                        case "TJ" :currentCountry = "Tajikistan";
                            pic.setBackgroundResource(R.mipmap.tajikistan);
                            break;
                        case "TZ" :currentCountry = "Tanzania";
                            pic.setBackgroundResource(R.mipmap.tanzania);
                            break;
                        case "TH" :currentCountry = "Thailand";
                            pic.setBackgroundResource(R.mipmap.thailand);
                            break;
                        case "TG" :currentCountry = "Togo";
                            pic.setBackgroundResource(R.mipmap.togo);
                            break;
                        case "TK" :currentCountry = "Tokelau";
                            pic.setBackgroundResource(R.mipmap.american_samoa);
                            break;
                        case "TO" :currentCountry = "Tonga";
                            pic.setBackgroundResource(R.mipmap.tonga);
                            break;
                        case "TT" :currentCountry = "Trinidad and Tobago";
                            pic.setBackgroundResource(R.mipmap.trinidad_and_tobago);
                            break;
                        case "TN" :currentCountry = "Tunisia";
                            pic.setBackgroundResource(R.mipmap.tunisia);
                            break;
                        case "TR" :currentCountry = "Turkey";
                            pic.setBackgroundResource(R.mipmap.turkey);
                            break;
                        case "TM" :currentCountry = "Turkmenistan";
                            pic.setBackgroundResource(R.mipmap.turkmenistan);
                            break;
                        case "TC" :currentCountry = "Turks and Caicos Islands";
                            pic.setBackgroundResource(R.mipmap.turks_and_caicos_islands);
                            break;
                        case "TV" :currentCountry = "Tuvalu";
                            pic.setBackgroundResource(R.mipmap.tuvalu);
                            break;
                        case "UG" :currentCountry = "Uganda";
                            pic.setBackgroundResource(R.mipmap.uganda);
                            break;
                        case "UA" :currentCountry = "Ukraine";
                            pic.setBackgroundResource(R.mipmap.ukraine);
                            break;
                        case "AE" :currentCountry = "United Arab Emirates";
                            pic.setBackgroundResource(R.mipmap.uae);
                            break;
                        case "UK" :currentCountry = "United Kingdom";
                            pic.setBackgroundResource(R.mipmap.united_kingdom);
                            break;
                        case "US" : currentCountry = "United States";
                            pic.setBackgroundResource(R.mipmap.usa);
                            break;
                        case "UM" :currentCountry = "US Minor Outlying Islands";
                            pic.setBackgroundResource(R.mipmap.usa);
                            break;
                        case "UY" :currentCountry = "Uruguay";
                            pic.setBackgroundResource(R.mipmap.uruguay);
                            break;
                        case "SU" :currentCountry = "USSR (former)";
                            pic.setBackgroundResource(R.mipmap.russian_federation);
                            break;
                        case "UZ" :currentCountry = "Uzbekistan";
                            pic.setBackgroundResource(R.mipmap.uzbekistan);
                            break;
                        case "VU" :currentCountry = "Vanuatu";
                            pic.setBackgroundResource(R.mipmap.vanuatu);
                            break;
                        case "VA" :currentCountry = "Vatican City State";
                            pic.setBackgroundResource(R.mipmap.vatican_city);
                            break;
                        case "VE" :currentCountry = "Venezuela";
                            pic.setBackgroundResource(R.mipmap.venezuela);
                            break;
                        case "VN" :currentCountry = "Viet Nam";
                            pic.setBackgroundResource(R.mipmap.vietnam);
                            break;
                        case "VG" :currentCountry = "British Virgin Islands";
                            pic.setBackgroundResource(R.mipmap.british_virgin_islands);
                            break;
                        case "VI" :currentCountry = "Virgin Islands (U.S.)";
                            pic.setBackgroundResource(R.mipmap.us_virgin_islands);
                            break;
                        case "WF" :currentCountry = "Wallis and Futuna Islands";
                            pic.setBackgroundResource(R.mipmap.wallis_and_futuna);
                            break;
                        case "EH" :currentCountry = "Western Sahara";
                            pic.setBackgroundResource(R.mipmap.western_sahara);
                            break;
                        case "YE" :currentCountry = "Yemen";
                            pic.setBackgroundResource(R.mipmap.yemen);
                            break;
                        case "ZM" :currentCountry = "Zambia";
                            pic.setBackgroundResource(R.mipmap.zambia);
                            break;
                        case "ZR" :currentCountry = "Zaire";
                            pic.setBackgroundResource(R.mipmap.zaire);
                            break;
                        case "ZW" :currentCountry = "Zimbabwe";
                            pic.setBackgroundResource(R.mipmap.zimbabwe);
                            break;
                        default:currentCountry = "Unknown Country";
                            pic.setBackgroundResource(R.mipmap.alien);
                            break;
                    }
                }
                break;
        }

    }

    public static void setCountryFlagByNameCountry(final ImageView pic , final String countryName)
    {
                if (countryName != null) {
                    switch (countryName)
                    {

                        case "Egypt" :
                            pic.setBackgroundResource(R.mipmap.egypt);
                            break;
                        case "Afghanistan" :
                            pic.setBackgroundResource(R.mipmap.afghanistan);
                            break;
                        case "Aland Islands" :
                            pic.setBackgroundResource(R.mipmap.aland);
                            break;
                        case "Albania" :
                            pic.setBackgroundResource(R.mipmap.albania);
                            break;
                        case "algeria" :
                            pic.setBackgroundResource(R.mipmap.algeria);
                            break;
                        case "American Samoa" :
                            pic.setBackgroundResource(R.mipmap.american_samoa);
                            break;
                        case "Andorra" :
                            pic.setBackgroundResource(R.mipmap.andorra);
                            break;
                        case "Angola" :
                            pic.setBackgroundResource(R.mipmap.angola);
                            break;
                        case "Anguilla" :
                            pic.setBackgroundResource(R.mipmap.anguilla);
                            break;
                        case "Antarctica" :
                            pic.setBackgroundResource(R.mipmap.antractica);
                            break;
                        case "Antigua and Barbuda" :
                            pic.setBackgroundResource(R.mipmap.antigua_and_barbuda);
                            break;
                        case "Argentina":
                            pic.setBackgroundResource(R.mipmap.argentina);
                            break;
                        case "Armenia":
                            pic.setBackgroundResource(R.mipmap.armenia);
                            break;
                        case "Aruba":
                            pic.setBackgroundResource(R.mipmap.aruba);
                            break;
                        case  "Ascension Island":
                            pic.setBackgroundResource(R.mipmap.ascension);
                            break;
                        case  "Australia":
                            pic.setBackgroundResource(R.mipmap.australia);
                            break;
                        case  "Austria":
                            pic.setBackgroundResource(R.mipmap.austria);
                            break;
                        case  "Azerbaijan":
                            pic.setBackgroundResource(R.mipmap.azerbaijan);
                            break;
                        case  "Bahamas":
                            pic.setBackgroundResource(R.mipmap.bahamas);
                            break;
                        case  "Bahrain":
                            pic.setBackgroundResource(R.mipmap.bahrain);
                            break;
                        case  "Barbados":
                            pic.setBackgroundResource(R.mipmap.barbados);
                            break;
                        case "Bangladesh":
                            pic.setBackgroundResource(R.mipmap.bangladesh);
                            break;
                        case "Belarus":
                            pic.setBackgroundResource(R.mipmap.belarus);
                            break;
                        case  "Belgium":
                            pic.setBackgroundResource(R.mipmap.belgium);
                            break;
                        case  "Belize":
                            pic.setBackgroundResource(R.mipmap.belize);
                            break;
                        case  "Benin":
                            pic.setBackgroundResource(R.mipmap.benin);
                            break;
                        case  "Bermuda":
                            pic.setBackgroundResource(R.mipmap.bermuda);
                            break;
                        case "Bhutan":
                            pic.setBackgroundResource(R.mipmap.bhutan);
                            break;
                        case  "Botswana":
                            pic.setBackgroundResource(R.mipmap.botswana);
                            break;
                        case  "Bolivia":
                            pic.setBackgroundResource(R.mipmap.bolivia);
                            break;
                        case  "Bosnia and Herzegovina":
                            pic.setBackgroundResource(R.mipmap.bosnia);
                            break;
                        case  "Bouvet Island":
                            pic.setBackgroundResource(R.mipmap.norway);
                            break;
                        case  "Brazil":
                            pic.setBackgroundResource(R.mipmap.brazil);
                            break;
                        case  "British Indian Ocean Territory":
                            pic.setBackgroundResource(R.mipmap.british_indian_ocean_territory);
                            break;
                        case  "Brunei Darussalam":
                            pic.setBackgroundResource(R.mipmap.brunei);
                            break;
                        case "Bulgaria":
                            pic.setBackgroundResource(R.mipmap.bulgaria);
                            break;
                        case  "Burkina Faso":
                            pic.setBackgroundResource(R.mipmap.burkina_faso);
                            break;
                        case  "Burundi":
                            pic.setBackgroundResource(R.mipmap.burundi);
                            break;
                        case  "Cambodia":
                            pic.setBackgroundResource(R.mipmap.cambodia);
                            break;
                        case  "Cameroon":
                            pic.setBackgroundResource(R.mipmap.cameroon);
                            break;
                        case  "Canada":
                            pic.setBackgroundResource(R.mipmap.canada);
                            break;
                        case  "Cape Verde":
                            pic.setBackgroundResource(R.mipmap.cape_verde);
                            break;
                        case  "Cayman Islands":
                            pic.setBackgroundResource(R.mipmap.cayman_islands);
                            break;
                        case "Central African Republic":
                            pic.setBackgroundResource(R.mipmap.central_african_republic);
                            break;
                        case "Chad":
                            pic.setBackgroundResource(R.mipmap.chad);
                            break;
                        case "Chile":
                            pic.setBackgroundResource(R.mipmap.chile);
                            break;
                        case  "China":
                            pic.setBackgroundResource(R.mipmap.china);
                            break;
                        case  "Christmas Island":
                            pic.setBackgroundResource(R.mipmap.christmas_island);
                            break;
                        case "Cocos (Keeling) Islands":
                            pic.setBackgroundResource(R.mipmap.cocos_islands);
                            break;
                        case  "Colombia":
                            pic.setBackgroundResource(R.mipmap.colombia);
                            break;
                        case  "Comoros":
                            pic.setBackgroundResource(R.mipmap.comoros);
                            break;
                        case  "Congo":
                            pic.setBackgroundResource(R.mipmap.republic_of_the_congo);
                            break;
                        case  "Congo, Democratic Republic":
                            pic.setBackgroundResource(R.mipmap.democratic_republic_of_the_congo);
                            break;
                        case  "Cook Islands":
                            pic.setBackgroundResource(R.mipmap.cook_islands);
                            break;
                        case  "Costa Rica":
                            pic.setBackgroundResource(R.mipmap.costa_rica);
                            break;
                        case "Cote D'Ivoire (Ivory Coast)":
                            pic.setBackgroundResource(R.mipmap.cote_d_voire);
                            break;
                        case  "Croatia (Hrvatska)":
                            pic.setBackgroundResource(R.mipmap.croatia);
                            break;
                        case  "Cuba":
                            pic.setBackgroundResource(R.mipmap.cuba);
                            break;
                        case "Cyprus":
                            pic.setBackgroundResource(R.mipmap.cyprus);
                            break;
                        case  "Czech Republic":
                            pic.setBackgroundResource(R.mipmap.czech_republic);
                            break;
                        case  "Czechoslovakia (former)":
                            pic.setBackgroundResource(R.mipmap.czech_republic);
                            break;
                        case  "Denmark":
                            pic.setBackgroundResource(R.mipmap.denmark);
                            break;
                        case  "Djibouti":
                            pic.setBackgroundResource(R.mipmap.djibouti);
                            break;
                        case "Dominica":
                            pic.setBackgroundResource(R.mipmap.dominica);
                            break;
                        case  "Dominican Republic":
                            pic.setBackgroundResource(R.mipmap.dominican_republic);
                            break;
                        case  "East Timor":
                            pic.setBackgroundResource(R.mipmap.timor_leste);
                            break;
                        case  "Ecuador":
                            pic.setBackgroundResource(R.mipmap.ecuador);
                            break;
                        case  "El Salvador":
                            pic.setBackgroundResource(R.mipmap.el_salvador);
                            break;
                        case  "Equatorial Guinea":
                            pic.setBackgroundResource(R.mipmap.equatorial_guinea);
                            break;
                        case  "Eritrea":
                            pic.setBackgroundResource(R.mipmap.eritrea);
                            break;
                        case  "Estonia":
                            pic.setBackgroundResource(R.mipmap.estonia);
                            break;
                        case  "Ethiopia":
                            pic.setBackgroundResource(R.mipmap.ethiopia);
                            break;
                        case "Falkland Islands (Malvinas)":
                            pic.setBackgroundResource(R.mipmap.falkland_islands);
                            break;
                        case  "Faroe Islands":
                            pic.setBackgroundResource(R.mipmap.faroe_islands);
                            break;
                        case  "Fiji":
                            pic.setBackgroundResource(R.mipmap.fiji);
                            break;
                        case  "Finland":
                            pic.setBackgroundResource(R.mipmap.finland);
                            break;
                        case  "France":
                            pic.setBackgroundResource(R.mipmap.france);
                            break;
                        case  "France, Metropolitan":
                            pic.setBackgroundResource(R.mipmap.france);
                            break;
                        case  "French Guiana":
                            pic.setBackgroundResource(R.mipmap.french_guiana);
                            break;
                        case  "French Polynesia":
                            pic.setBackgroundResource(R.mipmap.french_polynesia);
                            break;
                        case  "French Southern Territories":
                            pic.setBackgroundResource(R.mipmap.french_territories);
                            break;
                        case  "Macedonia":
                            pic.setBackgroundResource(R.mipmap.macedonia);
                            break;
                        case  "Gabon":
                            pic.setBackgroundResource(R.mipmap.gabon);
                            break;
                        case  "Gambia":
                            pic.setBackgroundResource(R.mipmap.gambia);
                            break;
                        case  "Georgia":
                            pic.setBackgroundResource(R.mipmap.georgia);
                            break;
                        case  "Germany":
                            pic.setBackgroundResource(R.mipmap.germany);
                            break;
                        case  "Ghana":
                            pic.setBackgroundResource(R.mipmap.ghana);
                            break;
                        case  "Gibraltar":
                            pic.setBackgroundResource(R.mipmap.gibraltar);
                            break;
                        case  "Great Britain (UK)":
                            pic.setBackgroundResource(R.mipmap.united_kingdom);
                            break;
                        case  "Greece":
                            pic.setBackgroundResource(R.mipmap.greece);
                            break;
                        case "Greenland":
                            pic.setBackgroundResource(R.mipmap.greenland);
                            break;
                        case "Grenada":
                            pic.setBackgroundResource(R.mipmap.grenada);
                            break;
                        case  "Guadeloupe":
                            pic.setBackgroundResource(R.mipmap.guadeloupe);
                            break;
                        case  "Guam":
                            pic.setBackgroundResource(R.mipmap.guam);
                            break;
                        case  "Guatemala":
                            pic.setBackgroundResource(R.mipmap.guatemala);
                            break;
                        case  "Guernsey":
                            pic.setBackgroundResource(R.mipmap.guernsey);
                            break;
                        case  "Guinea":
                            pic.setBackgroundResource(R.mipmap.guinea);
                            break;
                        case  "Guinea-Bissau":
                            pic.setBackgroundResource(R.mipmap.guinea_bissau);
                            break;
                        case "Guyana":
                            pic.setBackgroundResource(R.mipmap.guyana);
                            break;
                        case  "Haiti":
                            pic.setBackgroundResource(R.mipmap.haiti);
                            break;
                        case  "Heard and McDonald Islands":
                            pic.setBackgroundResource(R.mipmap.mcdonald_islands);
                            break;
                        case  "Honduras":
                            pic.setBackgroundResource(R.mipmap.honduras);
                            break;
                        case  "Hong Kong":
                            pic.setBackgroundResource(R.mipmap.hong_kong);
                            break;
                        case  "Hungary":
                            pic.setBackgroundResource(R.mipmap.hungary);
                            break;
                        case  "Iceland":
                            pic.setBackgroundResource(R.mipmap.iceland);
                            break;
                        case "India":
                            pic.setBackgroundResource(R.mipmap.india);
                            break;
                        case  "Indonesia":
                            pic.setBackgroundResource(R.mipmap.indonesia);
                            break;
                        case  "Iran":
                            pic.setBackgroundResource(R.mipmap.iran);
                            break;
                        case  "Iraq":
                            pic.setBackgroundResource(R.mipmap.iraq);
                            break;
                        case  "Ireland":
                            pic.setBackgroundResource(R.mipmap.ireland);
                            break;
                        case  "Isle of Man":
                            pic.setBackgroundResource(R.mipmap.isle_of_man);
                            break;
                        case  "Italy":
                            pic.setBackgroundResource(R.mipmap.italy);
                            break;
                        case  "Jersey":
                            pic.setBackgroundResource(R.mipmap.jersey);
                            break;
                        case  "Jamaica":
                            pic.setBackgroundResource(R.mipmap.jamaica);
                            break;
                        case  "Japan":
                            pic.setBackgroundResource(R.mipmap.japan);
                            break;
                        case  "Jordan":
                            pic.setBackgroundResource(R.mipmap.jordan);
                            break;
                        case  "Kazakhstan":
                            pic.setBackgroundResource(R.mipmap.kazakhstan);
                            break;
                        case  "Kenya":
                            pic.setBackgroundResource(R.mipmap.kenya);
                            break;
                        case "Kiribati":
                            pic.setBackgroundResource(R.mipmap.kiribati);
                            break;
                        case  "Korea (North)":
                            pic.setBackgroundResource(R.mipmap.north_korea);
                            break;
                        case  "Korea (South)":
                            pic.setBackgroundResource(R.mipmap.south_korea);
                            break;
                        case  "Kuwait":
                            pic.setBackgroundResource(R.mipmap.kuwait);
                            break;
                        case  "Kyrgyzstan":
                            pic.setBackgroundResource(R.mipmap.kyrgyzstan);
                            break;
                        case "Laos":
                            pic.setBackgroundResource(R.mipmap.laos);
                            break;
                        case  "Latvia":
                            pic.setBackgroundResource(R.mipmap.latvia);
                            break;
                        case"Lebanon":
                            pic.setBackgroundResource(R.mipmap.lebanon);
                            break;
                        case  "Liechtenstein":
                            pic.setBackgroundResource(R.mipmap.liechtenstein);
                            break;
                        case  "Liberia":
                            pic.setBackgroundResource(R.mipmap.liberia);
                            break;
                        case  "Libya":
                            pic.setBackgroundResource(R.mipmap.libya);
                            break;
                        case  "Lesotho":
                            pic.setBackgroundResource(R.mipmap.lesotho);
                            break;
                        case  "Lithuania":
                            pic.setBackgroundResource(R.mipmap.lithuania);
                            break;
                        case  "Luxembourg":
                            pic.setBackgroundResource(R.mipmap.luxembourg);
                            break;
                        case "Macau":
                            pic.setBackgroundResource(R.mipmap.macao);
                            break;
                        case  "Madagascar":
                            pic.setBackgroundResource(R.mipmap.madagascar);
                            break;
                        case "Malawi":
                            pic.setBackgroundResource(R.mipmap.malawi);
                            break;
                        case  "Malaysia":
                            pic.setBackgroundResource(R.mipmap.malaysia);
                            break;
                        case  "Maldives":
                            pic.setBackgroundResource(R.mipmap.maldives);
                            break;
                        case "Mali":
                            pic.setBackgroundResource(R.mipmap.mali);
                            break;
                        case  "Malta":
                            pic.setBackgroundResource(R.mipmap.malta);
                            break;
                        case "Marshall Islands":
                            pic.setBackgroundResource(R.mipmap.marshall_islands);
                            break;
                        case "martinique":
                            pic.setBackgroundResource(R.mipmap.martinique);
                            break;
                        case "mauritania":
                            pic.setBackgroundResource(R.mipmap.mauritania);
                            break;
                        case "mauritius":
                            pic.setBackgroundResource(R.mipmap.mauritius);
                            break;
                        case  "Mayotte":
                            pic.setBackgroundResource(R.mipmap.mayotte);
                            break;
                        case  "mexico":
                            pic.setBackgroundResource(R.mipmap.mexico);
                            break;
                        case "micronesia":
                            pic.setBackgroundResource(R.mipmap.micronesia);
                            break;
                        case  "monaco":
                            pic.setBackgroundResource(R.mipmap.monaco);
                            break;
                        case "moldova":
                            pic.setBackgroundResource(R.mipmap.moldova);
                            break;
                        case  "mongolia":
                            pic.setBackgroundResource(R.mipmap.mongolia);
                            break;
                        case  "Montenegro":
                            pic.setBackgroundResource(R.mipmap.serbia_and_montenegro);
                            break;
                        case "montserrat":
                            pic.setBackgroundResource(R.mipmap.montserrat);
                            break;
                        case  "morocco":
                            pic.setBackgroundResource(R.mipmap.morocco);
                            break;
                        case  "Mozambique":
                            pic.setBackgroundResource(R.mipmap.mozambique);
                            break;
                        case  "Myanmar":
                            pic.setBackgroundResource(R.mipmap.myanmar);
                            break;
                        case  "Namibia":
                            pic.setBackgroundResource(R.mipmap.namibia);
                            break;
                        case  "Nauru":
                            pic.setBackgroundResource(R.mipmap.nauru);
                            break;
                        case  "Nepal":
                            pic.setBackgroundResource(R.mipmap.nepal);
                            break;
                        case  "Netherlands":
                            pic.setBackgroundResource(R.mipmap.netherlands);
                            break;
                        case  "Netherlands Antilles":
                            pic.setBackgroundResource(R.mipmap.netherlands_antilles);
                            break;
                        case  "Neutral Zone":
                            pic.setBackgroundResource(R.mipmap.flag_red);
                            break;
                        case  "New Caledonia":
                            pic.setBackgroundResource(R.mipmap.new_caledonia);
                            break;
                        case  "New Zealand (Aotearoa)":
                            pic.setBackgroundResource(R.mipmap.new_zealand);
                            break;
                        case  "Nicaragua":
                            pic.setBackgroundResource(R.mipmap.nicaragua);
                            break;
                        case  "Niger":
                            pic.setBackgroundResource(R.mipmap.niger);
                            break;
                        case "Nigeria":
                            pic.setBackgroundResource(R.mipmap.nigeria);
                            break;
                        case  "Niue":
                            pic.setBackgroundResource(R.mipmap.niue);
                            break;
                        case  "Norfolk Island":
                            pic.setBackgroundResource(R.mipmap.norfolk_island);
                            break;
                        case  "Northern Mariana Islands":
                            pic.setBackgroundResource(R.mipmap.northern_mariana_islands);
                            break;
                        case  "Norway":
                            pic.setBackgroundResource(R.mipmap.norway);
                            break;
                        case  "Oman":
                            pic.setBackgroundResource(R.mipmap.oman);
                            break;
                        case  "Pakistan":
                            pic.setBackgroundResource(R.mipmap.pakistan);
                            break;
                        case "Palau":
                            pic.setBackgroundResource(R.mipmap.palau);
                            break;
                        case "Palestine":
                            pic.setBackgroundResource(R.mipmap.palestine);
                            break;
                        case  "Panama":
                            pic.setBackgroundResource(R.mipmap.panama);
                            break;
                        case  "Papua New Guinea":
                            pic.setBackgroundResource(R.mipmap.papua_new_guinea);
                            break;
                        case  "Paraguay":
                            pic.setBackgroundResource(R.mipmap.paraguay);
                            break;
                        case  "Peru":
                            pic.setBackgroundResource(R.mipmap.peru);
                            break;
                        case  "Philippines":
                            pic.setBackgroundResource(R.mipmap.philippines);
                            break;
                        case  "Pitcairn":
                            pic.setBackgroundResource(R.mipmap.pitcairn_islands);
                            break;
                        case "Poland":
                            pic.setBackgroundResource(R.mipmap.poland);
                            break;
                        case  "Portugal":
                            pic.setBackgroundResource(R.mipmap.portugal);
                            break;
                        case "Puerto Rico":
                            pic.setBackgroundResource(R.mipmap.puerto_rico);
                            break;
                        case "Qatar":
                            pic.setBackgroundResource(R.mipmap.qatar);
                            break;
                        case  "Reunion":
                            pic.setBackgroundResource(R.mipmap.flag_red);
                            break;
                        case  "Romania":
                            pic.setBackgroundResource(R.mipmap.romania);
                            break;
                        case "Russian Federation":
                            pic.setBackgroundResource(R.mipmap.russian_federation);
                            break;
                        case "Rwanda":
                            pic.setBackgroundResource(R.mipmap.rwanda);
                            break;
                        case "S. Georgia":
                            pic.setBackgroundResource(R.mipmap.south_georgia);
                            break;
                        case "Saint Kitts and Nevis":
                            pic.setBackgroundResource(R.mipmap.saint_kitts_and_nevis);
                            break;
                        case "Saint Lucia":
                            pic.setBackgroundResource(R.mipmap.saint_lucia);
                            break;
                        case "Saint Vincent & the Grenadines":
                            pic.setBackgroundResource(R.mipmap.saint_vicent_and_the_grenadines);
                            break;
                        case  "Samoa":
                            pic.setBackgroundResource(R.mipmap.samoa);
                            break;
                        case "San Marino":
                            pic.setBackgroundResource(R.mipmap.san_marino);
                            break;
                        case  "Sao Tome and Principe":
                            pic.setBackgroundResource(R.mipmap.sao_tome_and_principe);
                            break;
                        case "Saudi Arabia":
                            pic.setBackgroundResource(R.mipmap.saudi_arabia);
                            break;
                        case "Senegal":
                            pic.setBackgroundResource(R.mipmap.senegal);
                            break;
                        case  "Serbia":
                            pic.setBackgroundResource(R.mipmap.serbia);
                            break;
                        case  "Serbia and Montenegro":
                            pic.setBackgroundResource(R.mipmap.serbia_and_montenegro);
                            break;
                        case "Seychelles":
                            pic.setBackgroundResource(R.mipmap.seychelles);
                            break;
                        case  "Sierra Leone":
                            pic.setBackgroundResource(R.mipmap.sierra_leone);
                            break;
                        case  "Singapore":
                            pic.setBackgroundResource(R.mipmap.singapore);
                            break;
                        case  "Slovenia":
                            pic.setBackgroundResource(R.mipmap.slovenia);
                            break;
                        case "Slovak Republic":
                            pic.setBackgroundResource(R.mipmap.slovakia);
                            break;
                        case  "Solomon Islands":
                            pic.setBackgroundResource(R.mipmap.soloman_islands);
                            break;
                        case  "Somalia":
                            pic.setBackgroundResource(R.mipmap.somalia);
                            break;
                        case  "South Africa":
                            pic.setBackgroundResource(R.mipmap.south_africa);
                            break;
                        case "Spain":
                            pic.setBackgroundResource(R.mipmap.spain);
                            break;
                        case "Sri Lanka":
                            pic.setBackgroundResource(R.mipmap.sri_lanka);
                            break;
                        case  "St. Helena":
                            pic.setBackgroundResource(R.mipmap.st_helena);
                            break;
                        case  "St. Pierre and Miquelon":
                            pic.setBackgroundResource(R.mipmap.st_pierre__miquelon);
                            break;
                        case  "Sudan":
                            pic.setBackgroundResource(R.mipmap.sudan);
                            break;
                        case  "Suriname":
                            pic.setBackgroundResource(R.mipmap.suriname);
                            break;
                        case  "Svalbard & Jan Mayen Islands":
                            pic.setBackgroundResource(R.mipmap.norway);
                            break;
                        case  "Swaziland":
                            pic.setBackgroundResource(R.mipmap.swaziland);
                            break;
                        case  "Sweden":
                            pic.setBackgroundResource(R.mipmap.sweden);
                            break;
                        case  "Switzerland":
                            pic.setBackgroundResource(R.mipmap.switzerland);
                            break;
                        case  "Syria":
                            pic.setBackgroundResource(R.mipmap.syria);
                            break;
                        case  "Taiwan":
                            pic.setBackgroundResource(R.mipmap.taiwan);
                            break;
                        case  "Tajikistan":
                            pic.setBackgroundResource(R.mipmap.tajikistan);
                            break;
                        case  "Tanzania":
                            pic.setBackgroundResource(R.mipmap.tanzania);
                            break;
                        case  "Thailand":
                            pic.setBackgroundResource(R.mipmap.thailand);
                            break;
                        case  "Togo":
                            pic.setBackgroundResource(R.mipmap.togo);
                            break;
                        case  "Tokelau":
                            pic.setBackgroundResource(R.mipmap.american_samoa);
                            break;
                        case  "Tonga":
                            pic.setBackgroundResource(R.mipmap.tonga);
                            break;
                        case  "Trinidad and Tobago":
                            pic.setBackgroundResource(R.mipmap.trinidad_and_tobago);
                            break;
                        case  "Tunisia":
                            pic.setBackgroundResource(R.mipmap.tunisia);
                            break;
                        case  "Turkey":
                            pic.setBackgroundResource(R.mipmap.turkey);
                            break;
                        case  "Turkmenistan":
                            pic.setBackgroundResource(R.mipmap.turkmenistan);
                            break;
                        case "Turks and Caicos Islands":
                            pic.setBackgroundResource(R.mipmap.turks_and_caicos_islands);
                            break;
                        case  "Tuvalu":
                            pic.setBackgroundResource(R.mipmap.tuvalu);
                            break;
                        case  "Uganda":
                            pic.setBackgroundResource(R.mipmap.uganda);
                            break;
                        case  "Ukraine":
                            pic.setBackgroundResource(R.mipmap.ukraine);
                            break;
                        case "United Arab Emirates":
                            pic.setBackgroundResource(R.mipmap.uae);
                            break;
                        case  "United Kingdom":
                            pic.setBackgroundResource(R.mipmap.united_kingdom);
                            break;
                        case "United States" :
                            pic.setBackgroundResource(R.mipmap.usa);
                            break;
                        case  "US Minor Outlying Islands":
                            pic.setBackgroundResource(R.mipmap.usa);
                            break;
                        case  "Uruguay":
                            pic.setBackgroundResource(R.mipmap.uruguay);
                            break;
                        case "USSR (former)":
                            pic.setBackgroundResource(R.mipmap.russian_federation);
                            break;
                        case "Uzbekistan":
                            pic.setBackgroundResource(R.mipmap.uzbekistan);
                            break;
                        case  "Vanuatu":
                            pic.setBackgroundResource(R.mipmap.vanuatu);
                            break;
                        case "Vatican City State":
                            pic.setBackgroundResource(R.mipmap.vatican_city);
                            break;
                        case  "Venezuela":
                            pic.setBackgroundResource(R.mipmap.venezuela);
                            break;
                        case  "Viet Nam":
                            pic.setBackgroundResource(R.mipmap.vietnam);
                            break;
                        case  "British Virgin Islands":
                            pic.setBackgroundResource(R.mipmap.british_virgin_islands);
                            break;
                        case "Virgin Islands (U.S.)":
                            pic.setBackgroundResource(R.mipmap.us_virgin_islands);
                            break;
                        case "Wallis and Futuna Islands":
                            pic.setBackgroundResource(R.mipmap.wallis_and_futuna);
                            break;
                        case  "Western Sahara":
                            pic.setBackgroundResource(R.mipmap.western_sahara);
                            break;
                        case "Yemen":
                            pic.setBackgroundResource(R.mipmap.yemen);
                            break;
                        case "Zambia":
                            pic.setBackgroundResource(R.mipmap.zambia);
                            break;
                        case "Zaire":
                            pic.setBackgroundResource(R.mipmap.zaire);
                            break;
                        case  "Zimbabwe":
                            pic.setBackgroundResource(R.mipmap.zimbabwe);
                            break;
                        case  "Unknown Country" :
                            pic.setBackgroundResource(R.mipmap.alien);
                            break ;
                        default :
                            pic.setBackgroundResource(R.mipmap.alien);
                            break;
                    }
                }
        }

    public static void loadPicturesInPrivateChat(final Context context , long theSum , final ImageView pic , String thePicName) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(theSum + " privateChat"+"/"+thePicName);
        Glide.with(context.getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .priority(Priority.IMMEDIATE)
                .into(new BitmapImageViewTarget(pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                Bitmap.createScaledBitmap(resource, 150, 150, false));
                        drawable.setCircular(false);
                        pic.setImageDrawable(drawable);
                    }
                });
    }

    public static void loadPicturesInMainChat(final Context context , String whichCountry , String thePosterUID , final ImageView pic , String thePicName) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(whichCountry + "/" + thePosterUID+"/"+thePicName);
        Glide.with(context.getApplicationContext())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .priority(Priority.IMMEDIATE)
                .into(new BitmapImageViewTarget(pic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),
                                Bitmap.createScaledBitmap(resource, 150, 150, false));
                        drawable.setCircular(false);
                        pic.setImageDrawable(drawable);
                    }
                });
    }
    public static void loadTheBigOne(final Context context , String whichCountry , String thePosterUID , final ImageView pic , String thePicName)
    {
        StorageReference storageReference =
                FirebaseStorage.getInstance().getReference().child(whichCountry + "/" + thePosterUID+"/"+thePicName);

        Glide.with(context).using(new FirebaseImageLoader()).load(storageReference)
                .fitCenter().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .dontTransform().into(pic);
    }
}
