package ru.linxmedia.for_linxmediaru.helpers;

import android.content.Context;
import android.widget.Toast;



public class ToastHelper {

    public static void showMessage(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showMessageLong(Context context, String string){
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }
}
