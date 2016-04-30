package pplb05.balgebun.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Haris Dwi Prakoso on 3/27/2016.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    private static SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "BalgebunLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    private static final String KEY_ROLE = "role";

    private static final String KEY_NAME = "name";

    private static final String KEY_USERNAME = "username";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void setLogin(boolean isLoggedIn, String name, String role, String username) {

        //set login flag
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        //set user details
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_USERNAME, username);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, "-1");
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, "-1");
    }

    public String getName() {
        return pref.getString(KEY_NAME, "-1");
    }
}
