package pplb05.balgebun.admin.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.admin.EditCounterActivity;
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.counter.EditSingleMenu;

/**
 * Created by Rahmi Julianasari on 28/04/2016.
 * TKelass ini sebagai adapter menu yang akan ditampilkan di list of menu
 */
public class EditMenuAdapter extends BaseAdapter{
    //initialization
    private ArrayList<Menu> foods = new ArrayList<>();
    private Context context;
    private TextView nama;
    private TextView harga;
    private Button editBtn, delBtn;
    private String usernameCounter, nameCounter;

    //generator
    public EditMenuAdapter(Context context, ArrayList<Menu> foods, String usernameCounter, String nameCounter) {
        this.foods = foods;
        this.context = context;
        this.usernameCounter=usernameCounter;
        this.nameCounter=nameCounter;
    }

    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foods.get(position).getPosition();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.edit_menu_item, parent, false);

        //init var
        nama = (TextView)v.findViewById(R.id.nama_menu);
        harga = (TextView)v.findViewById(R.id.harga_view);
        editBtn = (Button) v.findViewById(R.id.edit_button);
        delBtn = (Button) v.findViewById(R.id.del_button);

        //set var
        nama.setText(foods.get(position).getNamaMenu());
        harga.setText(foods.get(position).getHargaText()); //Get the text from your adapter for example

        //set button edit menu
        //jika button edit di click maka user akan di refer ke EditSingleMenu
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, EditSingleMenu.class);

                intent.putExtra("id_menu", ""+foods.get(position).getId());

                intent.putExtra("counterUsername", usernameCounter);
                intent.putExtra("counterName", nameCounter);

                System.out.println ("id = " + foods.get(position).getId());

                intent.putExtra("nama_menu",foods.get(position).getNamaMenu());
                System.out.println ("nama = " + foods.get(position).getNamaMenu());

                intent.putExtra("harga_menu",""+foods.get(position).getHarga());
                System.out.println ("harga = " + foods.get(position).getHarga());

                context.startActivity(intent);
            }
        });

        //set button delete menu
        //jika button delete di click maka user akan ditampilkan sebuah dialogBox
        //dialogBox meminta konfirmasi hapus menu
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox(foods.get(position).getNamaMenu(), ""+foods.get(position).getId());
            }
        });

        return v;
    }

    /*
     *Method ini untuk memanggil fungsi delete menu di API
     *dan fungsi API akan meghapus menu tersebut dari database
     *@param id_menu : id menu yang akan di hapus
     */
    private void delete(final String id_menu) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://aaa.esy.es/coba_wahid/deleteMenu.php";
        final StringRequest stringResp = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", "Menu Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast toast = Toast.makeText(context, "Berhasil menghapus data", Toast.LENGTH_LONG);
                        toast.show();

                        //Setelah dihapus user dialihkan ke halaman EditCounterActivity
                        Intent intent= new Intent(context, EditCounterActivity.class);
                        intent.putExtra("counterUsername", usernameCounter);
                        intent.putExtra("counterName", nameCounter);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameter counterUsername to getMenu url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_menu", id_menu);
                return params;
            }

        };
        queue.add(stringResp);
    }

    /*
     *Method ini untuk menampilkan dialogBox konfirmasi hapus menu
     * @param menu : nama menu yang akan dihapus
     * @param id : id menu yang akan dihapus
     */
    public void dialogBox(String menu, final String id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Hapus Menu");
        alertDialogBuilder.setMessage("Apakah Anda ingin menghapus menu " + menu + "?");
        alertDialogBuilder.setPositiveButton("Hapus",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        delete(id); //delete the menu
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
