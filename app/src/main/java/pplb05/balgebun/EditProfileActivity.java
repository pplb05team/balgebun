package pplb05.balgebun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import pplb05.balgebun.admin.MainActivity;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.helper.SQLiteHandler;
import pplb05.balgebun.helper.SessionManager;
import pplb05.balgebun.tools.RoundedImageView;

/**
 * Created by Wahid Nur Rohman on 4/29/2016.
 */
public class EditProfileActivity extends Activity{
    private static SQLiteHandler db;
    private HashMap<String, String> user;
    private EditText editName;
    private EditText editEmail;
    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editRetypePassword;
    private Button saveProfileButton;
    private Button savePasswordButton;
    private Button saveImageButton;
    private String newName;
    private String newEmail;
    private String oldPassword;
    private String newPassword;
    private String retypePassword;
    private RoundedImageView imageUser;
    private Bitmap bitmap;
    private boolean changeImage;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private SessionManager session;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        // session manager
        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());
        user = new HashMap<String, String>();
        user = db.getUserDetails();

        editName = (EditText)findViewById(R.id.edit_name);
        editEmail = (EditText)findViewById(R.id.edit_email);
        editOldPassword = (EditText)findViewById(R.id.edit_old_password);
        editNewPassword = (EditText)findViewById(R.id.edit_password);
        editRetypePassword = (EditText)findViewById(R.id.edit_retypePassword);
        saveProfileButton = (Button)findViewById(R.id.edit_profile_btn);
        savePasswordButton = (Button)findViewById(R.id.edit_paswd_btn);
        saveImageButton = (Button)findViewById(R.id.edit_image_btn);
        imageUser = (RoundedImageView)findViewById(R.id.editImageView);

        getImage();
        editName.setText(user.get("name"));
        editEmail.setText(user.get("email"));

        if(user.get("role").equals("1")){
            editName.setEnabled(false);
        }

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                newName = editName.getText().toString();
                newEmail = editEmail.getText().toString();

                    if(!checkInput(newName, "") && !checkInput(newEmail, "")){
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("old_username", user.get("user"));
                        params.put("new_username", newName);
                        params.put("new_email", newEmail);
                        sendRequest(AppConfig.URL_UPDATE_PROFILE, params);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Isian tidak boleh kosong", Toast.LENGTH_SHORT)
                                .show();
                    }

            }
        });

        saveImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                uploadImage();
            }
        });

        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                oldPassword = editOldPassword.getText().toString();
                newPassword = editNewPassword.getText().toString();
                retypePassword = editRetypePassword.getText().toString();

                if(checkInput(newPassword, retypePassword)){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("username", newName);
                    params.put("password", newPassword);
                    sendRequest(AppConfig.URL_UPDATE_PASSWORD, params);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Password berbeda", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        imageUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                changeImage = true;
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageUser.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean checkInput(String pass, String retype){
        return pass.equals(retype);
    }

    public void sendRequest(String url, final Map<String, String> params){

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("respoinses:", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Sukses update profile Anda", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Gagal update profile Anda", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Koneksi error", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return new HashMap<String, String>(params);
            }

        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD_IMAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(EditProfileActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.d("Sukses : ", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(EditProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.d("Masalah : ", "s");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);


                //Getting Image Name
                String name =  user.get("name");

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                params.put("role", user.get("role"));

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    /**
     * This method will show the image of the counter
     */
    private void getImage() {
        final String fileUrl;
        if(user.get("role").equals("1"))
            fileUrl = AppConfig.URL_IMG_CUSTOMER + user.get("name") + ".png";
        else {
            fileUrl = AppConfig.URL_IMG + session.getUsername() + ".jpg";
        }
        ImageRequest imgReqCtr = new ImageRequest(fileUrl, new Response.Listener<Bitmap>() {

            /**
             * Drae thw image to the imageviw
             *
             * @param response
             */
            @Override
            public void onResponse(Bitmap response) {
                imageUser.setImageBitmap(response);
                Log.d("SUKSES IMAGE", fileUrl);
            }
        }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("GAGAL IMAGE", fileUrl);
                    }
                });
        // Use VolleySingelton
        VolleySingleton.getInstance(this).addToRequestQueue(imgReqCtr);
    }
}
