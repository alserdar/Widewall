package alserdar.widewall.nav_stuff;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import alserdar.widewall.R;
import alserdar.widewall.user_stuff.UserProfile;
import alserdar.widewall.load_package.DetectUserInfo;
import alserdar.widewall.load_package.LoadPictures;
import alserdar.widewall.models.UserModel;

/**
 * Created by ALAZIZY on 11/28/2017.
 */


public class SearchUserListAdapter extends BaseAdapter implements Adapter {

    private Context c  ;
    private List<UserModel> list ;

    SearchUserListAdapter(Context context, List<UserModel> model) {

        c = context ;
        list = model ;
    }

    public void setUp (List<UserModel> model)
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

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final String theUID ;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.search_user_list_adapter, parent, false);
        }

        final UserModel model = (UserModel) getItem(position);

        theUID = DetectUserInfo.theUID(c.getApplicationContext());

        TextView userNameIs = view.findViewById(R.id.userSearchedName);
        TextView userHandleIs = view.findViewById(R.id.userSearchedHandle);
        ImageView userImageIs = view.findViewById(R.id.userSearchedImage);
        ImageView userCountryIs = view.findViewById(R.id.userSearchedCountry);
        userNameIs.setText(model.getUserName());
        userHandleIs.setText(model.getHandle());
        final String hisName = model.getUserName();
        final String hisUID = model.getTheUID() ;
        final String hisHandle = model.getHandle() ;
        final String hisId = model.getId();
        final String hisCountry = model.getCountry();
        final String hisInfo = model.getAboutUser();
        final String hisLink = model.getLinkUser();
        LoadPictures.loadPicsForHome(view.getContext().getApplicationContext() , model.getTheUID() , userImageIs);
        LoadPictures.setCountryFlagByNameCountry(userCountryIs , model.getCountry());
        userNameIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getTheUID().equals(theUID))
                {

                }else
                {
                    Intent i = new Intent(c , UserProfile.class);
                    i.putExtra("hisName" , hisName);
                    i.putExtra("hisHandle" , hisHandle);
                    i.putExtra("hisUID" , hisUID);
                    i.putExtra("hisId" , hisId);
                    i.putExtra("hisCountry" , hisCountry);
                    i.putExtra("hisInfo" , hisInfo);
                    i.putExtra("hisLink" , hisLink);
                    view.getContext().startActivity(i);
                }
            }
        });
        return view;
    }

}
