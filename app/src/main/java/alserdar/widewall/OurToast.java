package alserdar.widewall;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ALAZIZY on 2/11/2017.
 */
public class OurToast {

    public void myToast(Context context , String msg)
    {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        View toastView = toast.getView(); //This'll return the default View of the Toast.

        /* And now you can get the TextView of the default View of the Toast. */
        TextView toastMessage = toastView.findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wide_wall , 0, 0, 0);
        toastMessage.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        toastMessage.setCompoundDrawablePadding(15);
        toastView.setBackgroundColor(Color.WHITE);
        toastView.setBackgroundResource(R.drawable.button);
        toast.show();

    }

    public void myToastPic(Context context , String msg , int icon)
    {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        View toastView = toast.getView(); //This'll return the default View of the Toast.

        /* And now you can get the TextView of the default View of the Toast. */
        TextView toastMessage = toastView.findViewById(android.R.id.message);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.wide_wall , 0, 0, 0);
        toastMessage.setCompoundDrawablesWithIntrinsicBounds(0 , 0, icon, 0);
        toastMessage.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL);
        toastMessage.setCompoundDrawablePadding(15);
        toastView.setBackgroundColor(Color.WHITE);
        toastView.setBackgroundResource(R.drawable.button);
        toast.show();

    }
}
