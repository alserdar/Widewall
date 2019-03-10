package alserdar.widewall.nav_stuff;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import alserdar.widewall.R;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.models.VolcanoModel;

public class Volcano extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volcano);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());




        ViewPager mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.getTabAt(0).setIcon(R.mipmap.planet_earth);
            tabLayout.getTabAt(1).setIcon(R.mipmap.location);

        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            HotPlanet hotPlanet = new HotPlanet();
            HotCountry hotCountry = new HotCountry();
            switch (position)
            {
                case 0 : return hotPlanet;
                case 1 : return hotCountry ;
            }
            return null ;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "";
                case 1:
                    return "";
            }
            return null;
        }
    }


    public static class HotPlanet extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.hot_planet_list, container, false);
            final ListView hotPlanet = rootView.findViewById(R.id.hotPlanetList);
            try {


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Query query = db.collection("Volcano").orderBy("happiness", Query.Direction.DESCENDING).limit(15);

                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshots) {

                        if (documentSnapshots.isEmpty())
                        {

                        }else
                        {
                            List<VolcanoModel> listRooms = documentSnapshots.toObjects(VolcanoModel.class);
                            final VolcanoListAdapter adapter = new VolcanoListAdapter(getContext() , listRooms);
                            hotPlanet.setAdapter(adapter);
                            int currentPosition = hotPlanet.getFirstVisiblePosition();
                            hotPlanet.setSelection(currentPosition);
                        }

                    }
                });
            }catch (Exception e)
            {
                e.getMessage();
            }
            return rootView;
        }
    }

    // hot country =======================================
    public static class HotCountry extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.hot_country_list, container, false);
            ListView hotCountry = rootView.findViewById(R.id.hotCountryList);
            TextView whereIsThatHotStuff = rootView.findViewById(R.id.whereIsThatHotStuff);
            String country = DetectUserInfo.country(getContext());
            whereIsThatHotStuff.setText("Hot Stuff On " + country);
            return rootView;
        }
    }
}
