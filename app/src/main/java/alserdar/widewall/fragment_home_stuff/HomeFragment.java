package alserdar.widewall.fragment_home_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Objects;

import alserdar.widewall.R;
import alserdar.widewall.animate.RotateThe3D;
import alserdar.widewall.load_package.LoadPictures;

public class HomeFragment extends Fragment {
    private ImageView planet;
    private ImageView join;
    private ImageView island ;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_home, container, false);


        planet = view.findViewById(R.id.planetIcon);
        ImageView location = view.findViewById(R.id.locationFlag);
        join = view.findViewById(R.id.joinYourCountry);
        island = view.findViewById(R.id.islandIcon);


        LoadPictures.setCountryFlag(Objects.requireNonNull(getContext()), location);


        planet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateThe3D.startRotation(0,360 , planet);
                Thread timer = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.getMessage();
                        } finally {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(Objects.requireNonNull(getContext()) , Planet.class);
                                    startActivity(i);
                                }
                            }).start();

                        }
                    }
                };
                timer.start();
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateThe3D.startRotation(0,360 , join);
                Thread timer = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.getMessage();
                        } finally {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(Objects.requireNonNull(getContext()) , RoomCountry.class);
                                    startActivity(i);
                                }
                            }).start();

                        }
                    }
                };
                timer.start();
            }
        });

        island.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateThe3D.startRotation(0,360 , island);
                Thread timer = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.getMessage();
                        } finally {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(Objects.requireNonNull(getContext()) , IslandRoom.class);
                                    startActivity(i);
                                }
                            }).start();

                        }
                    }
                };
                timer.start();
            }
        });

        return view;
    }
}
