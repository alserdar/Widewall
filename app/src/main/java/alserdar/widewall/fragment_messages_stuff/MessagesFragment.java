package alserdar.widewall.fragment_messages_stuff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import alserdar.widewall.R;
import alserdar.widewall.core.CoreTheNotificationsList;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.DetectUserInfoFromFirebase;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.MessagesListModel;

public class MessagesFragment extends Fragment {
    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_item_messages, container, false);




        ListView messagesList = view.findViewById(R.id.messagesList);
        LinearLayout theLay = view.findViewById(R.id.theEmptyLay);
        final String theUID = DetectUserInfo.theUID(getContext());

        DetectUserInfoFromFirebase.emptyListOrNot(theUID , "messages" , theLay);
        CoreTheNotificationsList.setGotMessageAsTrueOrFalse(theUID , false);


        FirebaseListAdapter<MessagesListModel> adapter =
                new FirebaseListAdapter<MessagesListModel>(getContext(), MessagesListModel.class, R.layout.list_of_messages,
                        FirebaseDatabase.getInstance().getReference(theUID).child("messages")) {
                    @Override
                    protected void populateView(final View v, final MessagesListModel model, final int position) {

                        LinearLayout messageLay = v.findViewById(R.id.messageLayToReadIt);
                        ImageView profilePic = v.findViewById(R.id.profilePicInMessages);
                        ImageView countryFlag = v.findViewById(R.id.countryFlagInMessages);

                        TextView dateInMessages = v.findViewById(R.id.dateInMessage);
                        TextView userNameInMessages = v.findViewById(R.id.userNameInMessages);
                        TextView handleInMessages = v.findViewById(R.id.handleInMessages);
                        TextView messageInMessages = v.findViewById(R.id.theMessageInMessages);

                        LoadPictures.loadPicsForHome(v.getContext() , model.getTheUID() , profilePic);
                        LoadPictures.setCountryFlagByNameCountry(countryFlag, model.getCountry());

                        userNameInMessages.setText(model.getNickNAme());
                        handleInMessages.setText(String.format("@%s", model.getHandle()));
                        dateInMessages.setText(String.format("%s %s", model.getMessageDate(), model.getMessageTime()));

                        messageInMessages.setText(model.getLastMessage());

                        if (model.isReadIt())
                        {
                            messageLay.setBackgroundResource(R.drawable.button);
                        }else
                        {

                        }

                        messageLay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                Intent i = new Intent(v.getContext() , PrivateChat.class);
                                i.putExtra("theUID" , theUID);
                                i.putExtra("hisUID" , model.getTheUID());
                                i.putExtra("hisId" , model.getId());
                                startActivity(i);

                                /*
                                 HashMap<String, Object> readIt = new HashMap<>();
                                readIt.put("readIt", true);

                                FirebaseDatabase.getInstance().getReference().child(theUID).child("messages")
                                        .updateChildren(readIt).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }
                                });
                                 */

                            }
                        });
                    }
                };
        adapter.notifyDataSetChanged();
        messagesList.setAdapter(adapter);

        return view ;
    }
}
