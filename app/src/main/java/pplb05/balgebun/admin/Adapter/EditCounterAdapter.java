package pplb05.balgebun.admin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.admin.EditCounterActivity;
import pplb05.balgebun.admin.EditListCounterActivity;
import pplb05.balgebun.admin.Entity.EditCounterEntity;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;

/**
 * Created by Rahmi Julianasari on 25/04/2016.
 * Kelas ini sebagai adapter counter yang akan ditampilkan di list of counter
 */
public class EditCounterAdapter extends BaseAdapter {
    //initialization
    ArrayList<EditCounterEntity> counters=new ArrayList<>();
    private TextView counterName, username;
    private Button editButton, deleteButton;
    Context context;

    //generator
    public EditCounterAdapter(ArrayList<EditCounterEntity> counters, Context context){
        this.counters = counters;
        this.context = context;
    }

    @Override
    public int getCount() {
        return counters.size();
    }

    @Override
    public Object getItem(int position) {
        return counters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final EditCounterEntity counter = counters.get(position);
        LayoutInflater l = LayoutInflater.from(context);
        final View v = l.inflate(R.layout.adm_counter_layout, parent, false);

        //init var
        counterName = (TextView) v.findViewById(R.id.namaCounter);
        username = (TextView) v.findViewById(R.id.usernameCounter);
        editButton = (Button) v.findViewById(R.id.edit_button);
        deleteButton = (Button)v.findViewById(R.id.deleteC);

        //set text
        counterName.setText(counter.getCounterName());
        username.setText(counter.getUsername());

        //set button
        //jika button edit ditekan akan di refer ke EditCounterActivity
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCounter = counters.get(position).getCounterName();
                String strUsername = counters.get(position).getUsername();

                Intent i = new Intent(v.getContext(),EditCounterActivity.class);
                i.putExtra("counterUsername", strUsername);
                i.putExtra("counterName", strCounter);

                v.getContext().startActivity(i);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCounter = counters.get(position).getCounterName();
                String strUsername = counters.get(position).getUsername();
                dialogBox(strUsername, strCounter);
            }
        });


        return v;
    }

    private void deleteUser(final String usernameCounter, final String nameCounter){
        String tag_string_req = "req_delete";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_COUNTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(context,
                                "User successfully deleted", Toast.LENGTH_LONG).show();
                        //Setelah dihapus user dialihkan ke halaman EditCounterActivity
                        Intent intent= new Intent(context, EditListCounterActivity.class);
                        intent.putExtra("counterUsername", usernameCounter);
                        intent.putExtra("counterName", nameCounter);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(context,
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            //show error message
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
                            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to delete url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", usernameCounter);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*
     *Method ini untuk menampilkan dialogBox konfirmasi hapus counter
     * @param menu : nama menu yang akan dihapus
     * @param id : id menu yang akan dihapus
     */
    public void dialogBox(final String usernameCounter, final String namaCounter) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Hapus Menu");
        alertDialogBuilder.setMessage("Apakah Anda ingin menghapus menu " + namaCounter + "?");
        alertDialogBuilder.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteUser(usernameCounter, namaCounter); //delete the menu
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //do nothing if cancel
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}

