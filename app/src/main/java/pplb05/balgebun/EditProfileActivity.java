package pplb05.balgebun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import pplb05.balgebun.admin.EditCounterActivity;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.BuyerActivity;
import pplb05.balgebun.counter.PenjualActivity;
import pplb05.balgebun.helper.SessionManager;
import pplb05.balgebun.tools.RoundedImageView;

/**
 * Created by Wahid Nur Rohman on 4/29/2016.
 *
 * Kelas untuk edit profil bagi Penjual dan Pembeli
 */
public class EditProfileActivity extends Activity{
    private EditText editName;
    private EditText editEmail;
    private EditText editOldPassword;
    private EditText editNewPassword;
    private EditText editRetypePassword;
    private Button savePasswordButton;
    private String newName;
    private String newEmail;
    private String oldPassword;
    private String newPassword;
    private String retypePassword;
    private RoundedImageView imageUser;
    private Bitmap bitmap;
    private Bitmap decoded;
    private boolean changeImage;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private String role;
    private SessionManager session;
    private boolean errorUpdate;

    private String counterName, counterUsername, counterEmail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        // session manager
        session = new SessionManager(getApplicationContext());

        // Mendapatkan elemen-elemen di halaman
        editName = (EditText)findViewById(R.id.edit_name);
        editEmail = (EditText)findViewById(R.id.edit_email);
        editOldPassword = (EditText)findViewById(R.id.edit_old_password);
        editNewPassword = (EditText)findViewById(R.id.edit_password);
        editRetypePassword = (EditText)findViewById(R.id.edit_retypePassword);
        savePasswordButton = (Button)findViewById(R.id.edit_paswd_btn);
        imageUser = (RoundedImageView)findViewById(R.id.editImageView);

        // Set text nama dan email saat ini

        if(session.getRole().equals("3")){
            Intent i = getIntent();
            counterUsername = i.getStringExtra("counterUsername");
            counterName = i.getStringExtra("counterName");
            counterEmail = i.getStringExtra("counterEmail");
            editName.setText(counterName);
            editEmail.setText(counterEmail);
        }else{
            editName.setText(session.getName());
            editEmail.setText(session.getEmail());
        }
        role = session.getRole();

        // Download gambar profil
        getImage();

        // Apabila role pembeli, tidak bisa edit name
        if(role.equals("1")){
            editName.setEnabled(false);
        }

        // Listener untuk button update password
        savePasswordButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Profile
                newName = editName.getText().toString();
                newEmail = editEmail.getText().toString();

                // Password
                oldPassword = editOldPassword.getText().toString();
                newPassword = editNewPassword.getText().toString();
                retypePassword = editRetypePassword.getText().toString();

                //Params Upload
                Map<String, String> params_profile = new HashMap<String, String>();
                boolean profileChanged = false;
                boolean passwordChanged = false;

                if(!isEmpty(newName) && !isEmpty(newEmail)){
                    if(!newName.equals(session.getName()) || !newEmail.equals(session.getEmail())) {
                        params_profile.put("role", role);
                        if(role.equals("3")){
                            params_profile.put("username", counterUsername);
                        }else{
                            params_profile.put("username", session.getUsername());
                        }
                        params_profile.put("new_name", newName);
                        params_profile.put("new_email", newEmail);
                        profileChanged = true;

                    }
                }
                // Cek validitas password
                if(!isEmpty(oldPassword) && !isEmpty(newPassword) && !isEmpty(retypePassword)) {
                    if (newPassword.equals(retypePassword)) {
                        if(role.equals("3")){
                            params_profile.put("username", counterUsername);
                        }else{
                            params_profile.put("username", session.getUsername());
                        }
                        params_profile.put("password", oldPassword);
                        params_profile.put("new_password", newPassword);
                        passwordChanged = true;

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Password berbeda!", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                if(profileChanged || passwordChanged) {
                    sendRequest(AppConfig.URL_UPDATE_PROFILE, params_profile);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Tidak ada yang Anda update!", Toast.LENGTH_SHORT)
                            .show();
                }
                Log.i("BOOLEAN", "errorUpdate: "+errorUpdate + "-passwordChanged: " + passwordChanged + "-profileChanged: " + profileChanged);


            }
        });

        // Listener saat user akan mengubah profile picture
        imageUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });

    }

    /**
     * Method untuk mendapatkan image yang dipilih dari filechooser
     *
     * @param requestCode - kode rquest pengambilan gambar
     * @param resultCode - status
     * @param data - imtent hasil pengambilan gambar
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                changeImage = true;
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                // Compress first
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

                //Setting the Bitmap to ImageView
                imageUser.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageUser.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method untuk mengirim request code
     *
     * @param url url tujuan
     * @param params paramter yang akan dikirim
     */
    public void sendRequest(String url, final Map<String, String> params){

        Log.i("URL", url);
        final ProgressDialog loading = ProgressDialog.show(this,"Updating...","Please wait...",false,false);
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("responses:", response);
                try {
                    // Ubah response ke json
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Jika tidak terjadi error
                    if (!error) {
                        if(role.equals("3")){
                            counterName=newEmail;
                            editEmail.setText(newEmail);
                        }else{
                            session.setEmail(newEmail);
                            editEmail.setText(newEmail);
                        }
                        if (role.equals("2")) {
                            session.setName(newName);
                            editName.setText(session.getName());
                        }
                        if (role.equals("3")) {
                            counterName=newName;
                            editName.setText(newName);
                        }


                        // Reset password fill menjadi kosong
                        editRetypePassword.setText("");
                        editNewPassword.setText("");
                        editOldPassword.setText("");

                        loading.dismiss();
                        Toast.makeText(getApplicationContext(),
                                "Sukses update profile Anda", Toast.LENGTH_SHORT)
                                .show();

                    } else {
                        // Error, tampilkan error
                        loading.dismiss();
                        String msg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
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

    /**
     * Method untuk melakukan request upload image
     */
    private void uploadImage(){
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_UPLOAD_IMAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Showing toast message of the response
                        loading.dismiss();
                        Toast.makeText(EditProfileActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.d("Sukses : ", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        //Showing toast
                        Toast.makeText(EditProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.d("Masalah : ", "s");
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(decoded);


                //Getting Image Name
                String name = "";
                if(session.getRole().equals("3"))
                    name =  counterUsername;
                else
                    name =  session.getUsername();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                if(session.getRole().equals("3"))
                    params.put("role", "2");
                else
                    params.put("role", role);



                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    /**
     * Method untuk mengubah image menjadi string (supaya bisa diupload)
     *
     * @param bmp - imag eyang akan diupload
     * @return -  String hasil encode iamge
     */
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
        final ProgressDialog loading = ProgressDialog.show(this,"","Please wait...",false,false);
        final String  fileUrl;
        System.out.println("nama foto"+counterUsername);
        if(session.getRole().equals("1"))
            fileUrl = AppConfig.URL_IMG_CUSTOMER + session.getUsername() + ".png";
        else if(session.getRole().equals("3"))
            fileUrl = AppConfig.URL_IMG + counterUsername + ".png";
        else
            fileUrl = AppConfig.URL_IMG + session.getUsername() + ".png";


        ImageRequest imgReqCtr = new ImageRequest(fileUrl, new Response.Listener<Bitmap>() {

            /**
             * Drae thw image to the imageviw
             *
             * @param response
             */
            @Override
            public void onResponse(Bitmap response) {
                imageUser.setImageBitmap(response);
                loading.dismiss();
            }
        }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("GAGAL IMAGE", fileUrl);
                        loading.dismiss();
                    }
                });
        // Use VolleySingelton
        VolleySingleton.getInstance(this).addToRequestQueue(imgReqCtr);
    }

    /**
     * Method yang dijalankan ketika back ditekan (Supaya image ter-reload
     */
    @Override
    public void onBackPressed() {
        Intent intent;
        if(session.getRole().equals("1"))
            intent = new Intent(EditProfileActivity.this, BuyerActivity.class);

        else if (session.getRole().equals("3")){
            intent = new Intent(EditProfileActivity.this, EditCounterActivity.class);
            intent.putExtra("counterUsername", counterUsername);
            intent.putExtra("counterName", counterName);
        }

        else
            intent = new Intent(EditProfileActivity.this, PenjualActivity.class);

        startActivity(intent);
        finish();

    }

    public boolean isEmpty(String in){
        return in == null || in.length() == 0;
    }



}
