package cm.trixobase.sharenda.system.media;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cm.trixobase.sharenda.common.BaseName;
import cm.trixobase.sharenda.system.Sharenda;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by noumianguebissie on 5/12/18.
 */

public class FileProcess {

    private static final String USER_CONTACT_ID = "User_Id";



    public static void saveOwnerId(Context context, long id) {
        try {
            FileOutputStream out = context.openFileOutput(USER_CONTACT_ID, MODE_PRIVATE);
            out.write(String.valueOf(id).getBytes());
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(Sharenda.Log, "FileProcess getId - " + e);
        }
    }

    public static String getOwnerId(Context context) {
        FileInputStream in;
        String id = "";
        int n = 0;
        byte[] buf = new byte[8];
        try {
            in = context.openFileInput(USER_CONTACT_ID);
            while ((n = in.read(buf)) >= 0) {
                for (byte bit : buf)
                    id = id + String.valueOf((char)bit);
            }
            if(!id.isEmpty()) {
                return String.valueOf(id).trim();
            }
        } catch (FileNotFoundException e) {
            Log.e(Sharenda.Log, "FileProcess getOwnerId - " + e);
        } catch (IOException e) {
            Log.e(Sharenda.Log, "FileProcess getOwnerId - " + e);
        }
        return "";
    }

}
