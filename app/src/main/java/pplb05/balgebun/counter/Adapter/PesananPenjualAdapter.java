package pplb05.balgebun.counter.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.counter.Entity.PesananPenjual;
import pplb05.balgebun.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dananarief on 02-04-16.
 */
public class PesananPenjualAdapter extends BaseAdapter {

    private ArrayList<PesananPenjual> listPesanan;
    private Context context;
    private TextView namaMakanan;
    private TextView namaPembeli;
    private TextView jumlahPesanan;
    private Spinner stat;
    private Button batal;
    private int position2;

    public interface onDataChangeListener{

    }

    public PesananPenjualAdapter(ArrayList<PesananPenjual> listPesanan, Context context) {
        this.listPesanan = listPesanan;
        this.context = context;

    }

    @Override
    public int getCount() {
        return listPesanan.size();
    }

    @Override
    public Object getItem(int position) {
        return listPesanan.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listPesanan.get(position).getPosition();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        position2=position;
        Log.d("cekPos",""+position);
        Log.d("eror", "eror");
        LayoutInflater i = LayoutInflater.from(context);
        final View v = i.inflate(R.layout.pesanan_layout, parent, false);
        namaMakanan = (TextView) v.findViewById(R.id.nama_menu);
        namaPembeli = (TextView) v.findViewById(R.id.nama_pembeli);
        jumlahPesanan = (TextView) v.findViewById(R.id.banyak_pesanan);
        stat = (Spinner) v.findViewById(R.id.planets_spinner);
        batal = (Button) v.findViewById(R.id.cancelButton);

        namaMakanan.setText(listPesanan.get(position).getNamaMakanan());
        namaPembeli.setText(listPesanan.get(position).getNamaPembeli());
        jumlahPesanan.setText(Integer.toString(listPesanan.get(position).getJumlahPesanan()));

        if(listPesanan.get(position).getStatus().equals("belum")){
            stat.setSelection(0);
        } else if(listPesanan.get(position).getStatus().equals("dimasak")){
            stat.setSelection(1);
        } else {
            stat.setSelection(2);
        }

        Log.d("print id order ","id order " + listPesanan.get(position).getId());

        stat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int pos, long id) {
                System.out.println("POSISINYA = " + position);
                Log.d("statusListenerku1", listPesanan.get(position).getNamaMakanan() + " " + parent.getItemAtPosition(pos) + "");
                //notifyDataSetChanged();
                Log.d("hmm5", "" + id);
                Log.d("print id order ", "id orderku " + listPesanan.get(position).getId());
                Log.d("Button","id"+listPesanan.get(position).getId());

                String tag_string_order = "req_order";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.URL_CANCEL_ORDER, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d("respon batal", "Respon pesanan: " + response.toString());
                        //hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            Log.d("e",response.toString());
                            if (!error) {
                                Log.d("update","update");
                                //Toast.makeText(getApplicationContext(), "Berhasil memesan!", Toast.LENGTH_LONG).show();

                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("error_msg");
                                // Toast.makeText(getApplicationContext(),
                                //       errorMsg, Toast.LENGTH_LONG).show();
                                Log.d("erorUpdate1","erorUpdate1");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e(TAG, "Ordering Error: " + error.getMessage());
                        //Toast.makeText(getApplicationContext(),
                        //      error.getMessage(), Toast.LENGTH_LONG).show();
                        //hideDialog();
                        Log.d("erorUpdate","erorpdate");
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Log.d("SedangUpdate","SedangUpdate");
                        // Posting params to update url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_order", ""+listPesanan.get(position).getId());
                        params.put("status", ""+parent.getItemAtPosition(pos));

                        return params;
                    }

                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, "req_order");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tesButtonku","sukses");
                Log.d("id",""+listPesanan.get(position).getId());
                String tag_string_order = "req_order";

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.URL_DELETE_ORDER, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //Log.d("respon batal", "Respon pesanan: " + response.toString());
                        //hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            Log.d("e",response.toString());
                            if (!error) {
                                Log.d("deletefunct","deletefunct");
                                //Toast.makeText(getApplicationContext(), "Berhasil memesan!", Toast.LENGTH_LONG).show();

                            } else {

                                // Error occurred in registration. Get the error
                                // message
                                String errorMsg = jObj.getString("error_msg");
                                // Toast.makeText(getApplicationContext(),
                                //       errorMsg, Toast.LENGTH_LONG).show();
                                Log.d("erorDelete1","erorDelete1");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e(TAG, "Ordering Error: " + error.getMessage());
                        //Toast.makeText(getApplicationContext(),
                        //      error.getMessage(), Toast.LENGTH_LONG).show();
                        //hideDialog();
                        Log.d("erorDel","erorDel");
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Log.d("SedangDelete","SedangDelete");
                        // Posting params to update url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id_order", ""+listPesanan.get(position).getId());

                        return params;
                    }

                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, "req_order");
                listPesanan.remove(position);
                notifyDataSetChanged();
                Log.d("notify","notify");
            }

        });
        return v;
    }



}
