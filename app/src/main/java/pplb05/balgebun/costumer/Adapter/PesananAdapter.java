package pplb05.balgebun.costumer.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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
import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.app.VolleySingleton;
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

    public void setStatus(String status, int position){
        food.get(position).setStatus(status);
    }
    public String getStatus(int position){
        return food.get(position).getSatus();

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
        if(!(food.get(position).getSatus().equals("belum")))
            btnBatal.setVisibility(View.GONE);

        btnBatal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                getStatus(""+food.get(position).getIdOrder(), position);

            }
        });

        return v;

    }


    public void dialogBox(final String id, final int pos) {
        System.out.println("====="+getStatus(pos));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Hapus Pesanan");
        if(!(getStatus(pos).equals("belum"))){

            alertDialogBuilder.setMessage("Mohon maaf Anda tidak bisa membatalkan pesanan. Pesanan Anda sedang dalam proses");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            setStatus(getStatus(pos),pos);
                            notifyDataSetChanged();
                        }
                    });
        }else{
            alertDialogBuilder.setMessage("Apakah Anda ingin membatalkan pesanan?");
            alertDialogBuilder.setPositiveButton("Hapus Pesanan",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            batalPesanan(id, pos); //delete the menu
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //do nothing if cancel
                        }
                    });
        }
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                        Toast.makeText(context,"Pesanan berhasil dihapus", Toast.LENGTH_LONG).show();
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

    /**
     * Method ini digunakan untuk mengecek status pesanan
     */
    public void getStatus(final String idOrder, final int pos){
        //queue = Volley.newRequestQueue(context);
        String url = "http://aaa.esy.es/coba_wahid/getStatus.php";
        final StringRequest stringResp = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE2", "Status Pesanan Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String tempStatus =jObj.getString("user");
                        setStatus(tempStatus, pos);
                        dialogBox(""+food.get(pos).getIdOrder(),pos);
                        System.out.println(getStatus(pos));

                    } else {
                        Toast toast = Toast.makeText(context, "Error get status", Toast.LENGTH_SHORT);
                        toast.show();
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
                // Posting params to getPemasukanPembeli url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_order", idOrder);
                return params;
            }


        };
        VolleySingleton.getInstance(context).addToRequestQueue(stringResp);

    }


}

