package comteam_chimeragps_tracker.httpsgithub.bigbrother;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jeffrey on 3/5/2015.
 */
public class Helpers {

    public static void makeToast(Activity c, String text, int duration)
    {
        LayoutInflater inflater = LayoutInflater.from(c);
        View customToastroot = inflater.inflate(R.layout.custom_toast, null);
        Toast customtoast=new Toast(c);

        TextView t = (TextView)customToastroot.findViewById(R.id.toastView);
        t.setText(text);

        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.CENTER,0, 0);
        customtoast.setDuration(duration);
        customtoast.show();
    }

    public static void serviceToast(Context c, String text, int duration)
    {
        Toast t = Toast.makeText(c, text, duration);
        t.setGravity(Gravity.CENTER,0, 0);
        t.show();
    }
}
