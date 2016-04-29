package pplb05.balgebun.counter.Adapter;

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
import pplb05.balgebun.costumer.Entity.Menu;
import pplb05.balgebun.counter.EditMenu;
import pplb05.balgebun.counter.EditSingleMenu;

/**
 * Created by febriyolaanastasia on 4/26/16.
 * This class is used as adapter for showing menu of a spesific counter
 */
public class MenuListAdapter extends BaseAdapter {

    private ArrayList<Menu> food;
    private Context context;
    private TextView nama;
    private TextView harga;
    private Button editBtn, delBtn;
    private String usernameCounter, nameCounter;

    OnDataChangeListener mOnDataChangeListener;


    public interface OnDataChangeListener{
        public void onDataChanged();
    }


    public void setOnDataChangeListener(OnDataChangeListener change){
        mOnDataChangeListener = change;
    }

    //Generator
    public MenuListAdapter(ArrayList<Menu> food,Context context, String usernameCounter, String nameCounter) {
        this.food = food;
        this.context = context;
        this.usernameCounter=usernameCounter;
        this.nameCounter=nameCounter;
    }

    @Override
    public long getItemId(int position) {
        return food.get(position).getPosition();
    }


    @Override
    public int getCount() {
        return food.size();
    }

    @Override
    public Object getItem(int position) {
        return food.get(position);
    }


    /**
     * Set view for each menu
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.edit_menu_item, parent, false);

        //init var
        nama = (TextView)v.findViewById(R.id.nama_menu);
        harga = (TextView)v.findViewById(R.id.harga_view);
        editBtn = (Button) v.findViewById(R.id.edit_button);
        delBtn = (Button) v.findViewById(R.id.del_button);

        //set var
        nama.setText(food.get(position).getNamaMenu());
        harga.setText(food.get(position).getHargaText()); //Get the text from your adapter for example

        //if button is pressed, edit that menu
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, EditSingleMenu.class);

                intent.putExtra("id_menu", ""+food.get(position).getId());

                intent.putExtra("counterUsername", usernameCounter);
                intent.putExtra("counterName", nameCounter);

                System.out.println ("id = " + food.get(position).getId());

                intent.putExtra("nama_menu",food.get(position).getNamaMenu());
                System.out.println ("nama = " + food.get(position).getNamaMenu());

                intent.putExtra("harga_menu",""+food.get(position).getHarga());
                System.out.println ("harga = " + food.get(position).getHarga());

                context.startActivity(intent);
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox(food.get(position).getNamaMenu(), ""+food.get(position).getId());
            }
        });

        return v;
    }

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

                        Intent intent= new Intent(context, EditMenu.class);
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
