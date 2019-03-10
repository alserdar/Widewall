package alserdar.widewall.nav_stuff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import alserdar.widewall.R;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.VolcanoModel;
import alserdar.widewall.post_stuff.ThePost;
import alserdar.widewall.user_stuff.UserProfile;

public class VolcanoListAdapter extends BaseAdapter implements Adapter {

    private Context c  ;
    private List<VolcanoModel> list ;
    private int counter = 0;

    public VolcanoListAdapter(Context context , List<VolcanoModel> model ) {

        c = context ;
        list = model ;
    }

    public void setUp (List<VolcanoModel> model)
    {
        list = model ;
    }

    @Override
    public int getCount() {
        if (list != null){
            return list.size();
        }
        return 0 ;
    }


    @Override
    public Object getItem(int i) {
        if (list != null){
            return list.get(i);
        }
        return null ;
    }

    @Override
    public long getItemId(int i)
    {
        if (list != null) {
            return list.get(i).hashCode();
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.volcano_list_adapter, parent, false);
        }
        final VolcanoModel model = (VolcanoModel) getItem(position);

        LinearLayout typePost = view.findViewById(R.id.textPost);
        LinearLayout postLay =view. findViewById(R.id.postLay);
        final LinearLayout audioPost = view.findViewById(R.id.audioPost);
        final TextView messageText = view.findViewById(R.id.textMessage);
        final TextView messageUser = view. findViewById(R.id.userMessage);
        final TextView messageHandle = view.findViewById(R.id.handleMessage);
        final ImageView flag = view.findViewById(R.id.flagMessage);
        final TextView countListeners = view.findViewById(R.id.count_listeners);
        final Button playMessage = view.findViewById(R.id.play_message_audio);
        final Button pauseMessage = view.findViewById(R.id.puase_message_audio);
        final SeekBar seekBarForPlay = view.findViewById(R.id.seek_bar_message_audio);
        final ImageView ppForChat = view.findViewById(R.id.userProfilePicture);

        if (model.getAudioStatus().equals(""))
        {
            audioPost.setVisibility(View.GONE);
        }else
        {
            audioPost.setVisibility(View.VISIBLE);
        }

        messageHandle.setText(model.getHandle());
        messageUser.setText(model.getNickName());
        messageText.setText(model.getThePost());
        LoadPictures.loadPicsForHome(view.getContext() , model.getTheUID() , ppForChat);
        LoadPictures.setCountryFlagByNameCountry(flag , model.getCountry());

        typePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(view.getContext() , ThePost.class);
                i.putExtra("ThePlanet" , "ThePlanet");
                i.putExtra("postKey" , model.getKeyPost());
                view.getContext().startActivity(i);
            }
        });

        postLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i =  new Intent(view.getContext() , ThePost.class);
                i.putExtra("ThePlanet" , "ThePlanet");
                i.putExtra("postKey" , model.getKeyPost());
                view.getContext().startActivity(i);
            }
        });

        messageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i =  new Intent(view.getContext() , UserProfile.class);
                i.putExtra("hisUID" , model.getTheUID());
                view.getContext().startActivity(i);
            }
        });

        return view;
    }
}
