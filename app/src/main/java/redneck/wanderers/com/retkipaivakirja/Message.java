package redneck.wanderers.com.retkipaivakirja;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ari Iivari on 11.2.2015.
 */
public class Message {
    public static void message(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
