package alserdar.widewall.nav_stuff;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import alserdar.widewall.R;

public class CountryListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] countryName;
    private final Integer[] countryFlag;

    CountryListAdapter(Activity context, String[] countryName, Integer[] countryFlag) {
        super(context, R.layout.all_countries_flags, countryName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.countryName=countryName;
        this.countryFlag=countryFlag;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(R.layout.all_countries_flags,
                null,true);

        TextView countryText = rowView.findViewById(R.id.countryTextView);
        ImageView countryImage = rowView.findViewById(R.id.countryImageView);

        countryText.setText(countryName[position]);
        countryImage.setImageResource(countryFlag[position]);
        return rowView;

    };
}
