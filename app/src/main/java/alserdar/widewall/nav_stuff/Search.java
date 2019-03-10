package alserdar.widewall.nav_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import alserdar.widewall.Home;
import alserdar.widewall.R;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.UserModel;

public class Search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String theUID = DetectUserInfo.theUID(Search.this);

        ImageView pressBack = findViewById(R.id.press_back);
        ImageView profilePic = findViewById(R.id.profilePicInToolBar);
        ImageView topList = findViewById(R.id.getTheTopOfList);
        final EditText searchEditText = findViewById(R.id.searchEditText);
        final ListView searchLista = findViewById(R.id.listOfSearch);
        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Search.this , Home.class);
                startActivity(i);
                finish();
            }
        });

        LoadPictures.loadPicsForHome(Search.this , theUID , profilePic);

        topList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                searchLista.smoothScrollToPosition(0);
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {

                if (charSequence.length() > 0)
                {

                    /*
                    .startAt(searchEditText.getText().toString().toUpperCase()).endAt(searchEditText.getText().toString()
                            .toLowerCase()+ "\uf8ff")
                     */


                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("UserInformation").whereEqualTo("handleLowerCase" , searchEditText.getText().toString().toLowerCase())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(final QuerySnapshot documentSnapshots) {

                                    if (documentSnapshots.isEmpty())
                                    {
                                        db.collection("UserInformation").whereEqualTo("userName" , searchEditText.getText().toString())
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(final QuerySnapshot documentSnapshots) {

                                                        if (documentSnapshots.isEmpty())
                                                        {

                                                        }else
                                                        {
                                                            List<UserModel> listUsers = documentSnapshots.toObjects(UserModel.class);
                                                            final SearchUserListAdapter adapter = new SearchUserListAdapter(getBaseContext() , listUsers);
                                                            searchLista.setAdapter(adapter);

                                                        }
                                                    }
                                                });
                                    }else
                                    {
                                        List<UserModel> listUsers = documentSnapshots.toObjects(UserModel.class);
                                        final SearchUserListAdapter adapter = new SearchUserListAdapter(getBaseContext() , listUsers);
                                        searchLista.setAdapter(adapter);

                                    }
                                }
                            });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        searchEditText.addTextChangedListener(watcher);

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Search.this , Home.class);
        startActivity(i);
        finish();
    }
}
