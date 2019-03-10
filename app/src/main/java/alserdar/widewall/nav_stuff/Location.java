package alserdar.widewall.nav_stuff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import alserdar.widewall.R;

public class Location extends AppCompatActivity {


    Integer[] countriesRes={
            R.mipmap.afghanistan,
            R.mipmap.aland,
            R.mipmap.albania,
            R.mipmap.algeria,
            R.mipmap.american_samoa,
            R.mipmap.andorra,
            R.mipmap.angola,
            R.mipmap.anguilla,
            R.mipmap.antractica,
            R.mipmap.antigua_and_barbuda,
            R.mipmap.argentina,
            R.mipmap.armenia,
            R.mipmap.aruba,
            R.mipmap.australia,
            R.mipmap.austria,
            R.mipmap.azerbaijan,
            R.mipmap.bahamas,
            R.mipmap.bahrain,
            R.mipmap.bangladesh,
            R.mipmap.barbados,
            R.mipmap.belarus,
            R.mipmap.belgium,
            R.mipmap.belize,
            R.mipmap.benin,
            R.mipmap.bermuda,R.mipmap.bhutan,R.mipmap.bolivia,R.mipmap.bosnia,R.mipmap.botswana,R.mipmap.brazil,
            R.mipmap.british_virgin_islands , R.mipmap.brunei , R.mipmap.bulgaria,R.mipmap.burkina_faso,
            R.mipmap.burundi,R.mipmap.cambodia,R.mipmap.cameroon,R.mipmap.canada,R.mipmap.cape_verde,
            R.mipmap.cayman_islands , R.mipmap.central_african_republic , R.mipmap.chad , R.mipmap.chile ,
            R.mipmap.china , R.mipmap.christmas_island , R.mipmap.colombia , R.mipmap.comoros , R.mipmap.cook_islands ,
            R.mipmap.costa_rica , R.mipmap.cote_d_voire , R.mipmap.croatia , R.mipmap.cuba , R.mipmap.cyprus ,
            R.mipmap.czech_republic , R.mipmap.democratic_republic_of_the_congo , R.mipmap.denmark , R.mipmap.djibouti ,
            R.mipmap.dominica , R.mipmap.dominican_republic , R.mipmap.ecuador , R.mipmap.egypt , R.mipmap.el_salvador ,
            R.mipmap.equatorial_guinea , R.mipmap.eritrea , R.mipmap.estonia , R.mipmap.ethiopia , R.mipmap.falkland_islands ,
            R.mipmap.faroe_islands , R.mipmap.fiji , R.mipmap.france , R.mipmap.french_polynesia , R.mipmap.gabon ,
            R.mipmap.gambia , R.mipmap.georgia , R.mipmap.germany , R.mipmap.ghana , R.mipmap.gibraltar ,
            R.mipmap.greece , R.mipmap.greenland , R.mipmap.grenada , R.mipmap.guam , R.mipmap.guatemala ,
            R.mipmap.guinea , R.mipmap.guinea_bissau , R.mipmap.guyana , R.mipmap.haiti , R.mipmap.honduras ,
            R.mipmap.hong_kong , R.mipmap.hungary , R.mipmap.iceland , R.mipmap.indonesia , R.mipmap.india ,
            R.mipmap.iran , R.mipmap.iraq , R.mipmap.ireland , R.mipmap.italy , R.mipmap.jamaica ,
            R.mipmap.japan , R.mipmap.jordan , R.mipmap.kazakhstan , R.mipmap.kenya , R.mipmap.kiribati,
            R.mipmap.kuwait , R.mipmap.kyrgyzstan , R.mipmap.laos , R.mipmap.latvia , R.mipmap.lebanon ,
            R.mipmap.lesotho , R.mipmap.liberia , R.mipmap.libya , R.mipmap.liechtenstein , R.mipmap.lithuania ,
            R.mipmap.luxembourg , R.mipmap.macao , R.mipmap.macedonia , R.mipmap.madagascar , R.mipmap.malawi ,
            R.mipmap.malaysia , R.mipmap.maldives , R.mipmap.mali , R.mipmap.malta , R.mipmap.marshall_islands,
            R.mipmap.martinique , R.mipmap.mauritania , R.mipmap.mauritius , R.mipmap.mexico , R.mipmap.micronesia ,
            R.mipmap.moldova , R.mipmap.monaco , R.mipmap.mongolia , R.mipmap.montserrat , R.mipmap.morocco ,
            R.mipmap.mozambique , R.mipmap.myanmar , R.mipmap.namibia,R.mipmap.nauru , R.mipmap.nepal ,
            R.mipmap.netherlands , R.mipmap.netherlands_antilles , R.mipmap.new_zealand , R.mipmap.nicaragua ,
            R.mipmap.niger , R.mipmap.nigeria ,R.mipmap.niue , R.mipmap.norfolk_island , R.mipmap.north_korea ,
            R.mipmap.norway , R.mipmap.oman , R.mipmap.pakistan , R.mipmap.palau , R.mipmap.palestine ,
            R.mipmap.panama , R.mipmap.papua_new_guinea , R.mipmap.paraguay , R.mipmap.peru ,
            R.mipmap.philippines , R.mipmap.pitcairn_islands , R.mipmap.poland , R.mipmap.portugal ,
            R.mipmap.puerto_rico , R.mipmap.qatar , R.mipmap.republic_of_the_congo , R.mipmap.romania ,
            R.mipmap.russian_federation , R.mipmap.rwanda , R.mipmap.saint_kitts_and_nevis , R.mipmap.saint_lucia ,
            R.mipmap.saint_pierre , R.mipmap.saint_vicent_and_the_grenadines , R.mipmap.samoa ,
            R.mipmap.san_marino , R.mipmap.sao_tome_and_principe , R.mipmap.saudi_arabia , R.mipmap.senegal ,
            R.mipmap.serbia_and_montenegro , R.mipmap.sierra_leone , R.mipmap.singapore , R.mipmap.slovakia ,
            R.mipmap.slovenia , R.mipmap.soloman_islands , R.mipmap.somalia , R.mipmap.south_africa ,
            R.mipmap.south_georgia , R.mipmap.south_korea , R.mipmap.spain , R.mipmap.sri_lanka ,
            R.mipmap.sudan , R.mipmap.suriname , R.mipmap.swaziland , R.mipmap.sweden , R.mipmap.switzerland ,
            R.mipmap.syria , R.mipmap.taiwan , R.mipmap.tajikistan , R.mipmap.tanzania ,
            R.mipmap.thailand , R.mipmap.tibet , R.mipmap.timor_leste , R.mipmap.togo ,
            R.mipmap.tonga , R.mipmap.trinidad_and_tobago , R.mipmap.tunisia , R.mipmap.turkey ,
            R.mipmap.turkmenistan , R.mipmap.turks_and_caicos_islands , R.mipmap.tuvalu , R.mipmap.uae ,
            R.mipmap.uganda , R.mipmap.ukraine , R.mipmap.united_kingdom , R.mipmap.uruguay ,
            R.mipmap.usa , R.mipmap.uzbekistan , R.mipmap.vanuatu , R.mipmap.vatican_city ,
            R.mipmap.venezuela , R.mipmap.vietnam , R.mipmap.yemen , R.mipmap.zambia , R.mipmap.zaire , R.mipmap.zimbabwe

    };

    Integer[] countriesResTwo={

            R.mipmap.burundi,R.mipmap.cambodia,R.mipmap.cameroon,R.mipmap.canada,R.mipmap.cape_verde,
            R.mipmap.cayman_islands , R.mipmap.central_african_republic , R.mipmap.chad , R.mipmap.chile ,
            R.mipmap.china , R.mipmap.christmas_island , R.mipmap.colombia , R.mipmap.comoros , R.mipmap.cook_islands ,
            R.mipmap.costa_rica , R.mipmap.cote_d_voire , R.mipmap.croatia , R.mipmap.cuba , R.mipmap.cyprus ,
            R.mipmap.czech_republic , R.mipmap.democratic_republic_of_the_congo , R.mipmap.denmark , R.mipmap.djibouti ,
            R.mipmap.dominica , R.mipmap.dominican_republic , R.mipmap.ecuador , R.mipmap.egypt , R.mipmap.el_salvador ,
            R.mipmap.equatorial_guinea , R.mipmap.eritrea , R.mipmap.estonia , R.mipmap.ethiopia , R.mipmap.falkland_islands ,
            R.mipmap.faroe_islands , R.mipmap.fiji , R.mipmap.france , R.mipmap.french_polynesia , R.mipmap.gabon ,
            R.mipmap.gambia , R.mipmap.georgia , R.mipmap.germany , R.mipmap.ghana , R.mipmap.gibraltar ,
            R.mipmap.greece , R.mipmap.greenland , R.mipmap.grenada , R.mipmap.guam , R.mipmap.guatemala ,
            R.mipmap.guinea , R.mipmap.guinea_bissau , R.mipmap.guyana , R.mipmap.haiti , R.mipmap.honduras ,
            R.mipmap.hong_kong , R.mipmap.hungary , R.mipmap.iceland , R.mipmap.indonesia , R.mipmap.india ,
            R.mipmap.iran , R.mipmap.iraq , R.mipmap.ireland , R.mipmap.italy , R.mipmap.jamaica ,
            R.mipmap.japan , R.mipmap.jordan , R.mipmap.kazakhstan , R.mipmap.kenya , R.mipmap.kiribati,
            R.mipmap.kuwait , R.mipmap.kyrgyzstan , R.mipmap.laos , R.mipmap.latvia , R.mipmap.lebanon ,
            R.mipmap.lesotho , R.mipmap.liberia , R.mipmap.libya , R.mipmap.liechtenstein , R.mipmap.lithuania ,
            R.mipmap.luxembourg , R.mipmap.macao , R.mipmap.macedonia , R.mipmap.madagascar , R.mipmap.malawi ,
            R.mipmap.malaysia , R.mipmap.maldives , R.mipmap.mali , R.mipmap.malta , R.mipmap.marshall_islands,
            R.mipmap.martinique , R.mipmap.mauritania , R.mipmap.mauritius , R.mipmap.mexico , R.mipmap.micronesia ,
            R.mipmap.moldova , R.mipmap.monaco , R.mipmap.mongolia , R.mipmap.montserrat , R.mipmap.morocco ,
            R.mipmap.mozambique , R.mipmap.myanmar , R.mipmap.namibia,R.mipmap.nauru , R.mipmap.nepal ,
            R.mipmap.netherlands , R.mipmap.netherlands_antilles , R.mipmap.new_zealand , R.mipmap.nicaragua ,
            R.mipmap.niger , R.mipmap.nigeria ,R.mipmap.niue , R.mipmap.norfolk_island , R.mipmap.north_korea ,

    };

    Integer[] countriesResThree={
            R.mipmap.bulgaria,R.mipmap.burkina_faso,
            R.mipmap.burundi,R.mipmap.cambodia,R.mipmap.cameroon,R.mipmap.canada,R.mipmap.cape_verde,
            R.mipmap.cayman_islands , R.mipmap.central_african_republic , R.mipmap.chad , R.mipmap.chile ,
            R.mipmap.china , R.mipmap.christmas_island , R.mipmap.colombia , R.mipmap.comoros , R.mipmap.cook_islands ,
            R.mipmap.costa_rica , R.mipmap.cote_d_voire , R.mipmap.croatia , R.mipmap.cuba , R.mipmap.cyprus ,
            R.mipmap.czech_republic , R.mipmap.democratic_republic_of_the_congo , R.mipmap.denmark , R.mipmap.djibouti ,
            R.mipmap.dominica , R.mipmap.dominican_republic , R.mipmap.ecuador , R.mipmap.egypt , R.mipmap.el_salvador ,
            R.mipmap.equatorial_guinea , R.mipmap.eritrea , R.mipmap.estonia , R.mipmap.ethiopia , R.mipmap.falkland_islands ,
            R.mipmap.faroe_islands , R.mipmap.fiji , R.mipmap.france , R.mipmap.french_polynesia , R.mipmap.gabon ,
            R.mipmap.gambia , R.mipmap.georgia , R.mipmap.germany , R.mipmap.ghana , R.mipmap.gibraltar ,
            R.mipmap.greece , R.mipmap.greenland , R.mipmap.grenada , R.mipmap.guam , R.mipmap.guatemala ,
            R.mipmap.guinea , R.mipmap.guinea_bissau , R.mipmap.guyana , R.mipmap.haiti , R.mipmap.honduras ,
            R.mipmap.hong_kong , R.mipmap.hungary , R.mipmap.iceland , R.mipmap.indonesia , R.mipmap.india ,
            R.mipmap.iran , R.mipmap.iraq , R.mipmap.ireland , R.mipmap.italy , R.mipmap.jamaica ,
            R.mipmap.japan , R.mipmap.jordan , R.mipmap.kazakhstan , R.mipmap.kenya , R.mipmap.kiribati,
            R.mipmap.kuwait , R.mipmap.kyrgyzstan , R.mipmap.laos , R.mipmap.latvia , R.mipmap.lebanon ,
            R.mipmap.lesotho , R.mipmap.liberia , R.mipmap.libya , R.mipmap.liechtenstein , R.mipmap.lithuania ,
            R.mipmap.luxembourg , R.mipmap.macao , R.mipmap.macedonia , R.mipmap.madagascar , R.mipmap.malawi ,
            R.mipmap.malaysia , R.mipmap.maldives , R.mipmap.mali , R.mipmap.malta , R.mipmap.marshall_islands,
            R.mipmap.martinique , R.mipmap.mauritania , R.mipmap.mauritius , R.mipmap.mexico , R.mipmap.micronesia ,
            R.mipmap.moldova , R.mipmap.monaco , R.mipmap.mongolia , R.mipmap.montserrat , R.mipmap.morocco ,
            R.mipmap.mozambique , R.mipmap.myanmar , R.mipmap.namibia,R.mipmap.nauru , R.mipmap.nepal ,
            R.mipmap.netherlands , R.mipmap.netherlands_antilles , R.mipmap.new_zealand , R.mipmap.nicaragua ,
            R.mipmap.niger , R.mipmap.nigeria ,R.mipmap.niue , R.mipmap.norfolk_island , R.mipmap.north_korea
    };

    String[] countryName ={
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "","" , "" , ""
    };

    String[] countryNameTwo ={
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",

    };

    String[] countryNameThree ={
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "", "",
            "",

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        CountryListAdapter adapter=new CountryListAdapter(this, countryName, countriesRes);
        CountryListAdapter adapterTwo=new CountryListAdapter(this, countryNameTwo, countriesResTwo);
        CountryListAdapter adapterThree=new CountryListAdapter(this, countryNameThree, countriesResThree);



        ListView list = findViewById(R.id.countryList);
        ListView listTwo = findViewById(R.id.countryListTwo);
        ListView listThree = findViewById(R.id.countryListThree);

        list.setAdapter(adapter);
        listTwo.setAdapter(adapterTwo);
        listThree.setAdapter(adapterThree);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String SlectedCountry = countryName[+position];
                Toast.makeText(getApplicationContext(), SlectedCountry, Toast.LENGTH_SHORT).show();

            }
        });


        listTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String SlectedCountry = countryNameTwo[+position];
                Toast.makeText(getApplicationContext(), SlectedCountry, Toast.LENGTH_SHORT).show();

            }
        });

        listThree.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                String SlectedCountry = countryNameThree[+position];
                Toast.makeText(getApplicationContext(), SlectedCountry, Toast.LENGTH_SHORT).show();

            }
        });


    }
}