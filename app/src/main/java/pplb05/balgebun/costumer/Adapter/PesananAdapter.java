package pplb05.balgebun.costumer.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.costumer.Entity.Menu;

/**
 * @author febriyola anastasia
 * this class is used as adapter to show list of buyer's Pesanan
 */
public class PesananAdapter extends BaseAdapter {

    private ArrayList<Menu> food;
    private Context context ;
    private TextView nama, status, keterangan, jumlah;
    private Button btnBatal;
    private RequestQueue queue;

    public PesananAdapter(ArrayList<Menu> food, Context context) {
        this.food = food;
        this.context = context;
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
     * This method will set view for each pesanan
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.pesanan_item, parent, false);

        //init var
        nama = (TextView)v.findViewById(R.id.nama_menu);
        keterangan = (TextView)v.findViewById(R.id.keterangan_menu);
        status = (TextView)v.findViewById(R.id.status);
        jumlah = (TextView)v.findViewById(R.id.jumlah);
        btnBatal = (Button)v.findViewById(R.id.btnBatal);

        //set text for each var
        nama.setText(food.get(position).getNamaMenu());
        jumlah.setText("jumlah: " + food.get(position).getJumlah());
        keterangan.setText("Counter: " + food.get(position).getNamaCounter());
        status.setText(food.get(position).getSatus());

        //set visibility of cancellation button
        if(!food.get(position).getSatus().equals("belum"))
            btnBatal.setVisibility(View.GONE);


        btnBatal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                batalPesanan(""+food.get(position).getIdOrder(), position);
                //showPopUp();
            }
        });


        return v;

    }

    /*
     * This method is used to call function batalPesanan and it will delete the cancelled order on database
     */
    public void batalPesanan(final String idOrder, int position) {

        StringRequest stringReq = new StringRequest(Request.Method.POST, AppConfig.URL_BATAL_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.d("e",response.toString());
                    if (!error) {
                        Log.d("deletefunct","deletefunct");

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Log.d("erorDelete1","erorDelete1");
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
                // Posting params to batalPesanan url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_order", idOrder);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringReq, "req_order");
        food.remove(position);
        notifyDataSetChanged();
        Log.d("notify","notify");
    }


}

