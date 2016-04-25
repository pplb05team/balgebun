package pplb05.balgebun.admin.Adapter;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pplb05.balgebun.R;
import pplb05.balgebun.costumer.Entity.CounterEntity;

/**
 * Created by febriyolaanastasia on 4/19/16.
 */
public class BayarPemasukanAdapter extends BaseAdapter {

    private ArrayList<CounterEntity> counterArr = new ArrayList<>();
    private Context context;
    private TextView nama;
    private TextView jumlah;
    private Button tombolBayar;
    private OnDataChangeListener mOnDataChangeListener;


    public interface OnDataChangeListener{
        public void onDataChanged();
    }


    public void setOnDataChangeListener(OnDataChangeListener change){
        mOnDataChangeListener = change;
    }

    //Generator
    public BayarPemasukanAdapter(ArrayList<CounterEntity> counterArr, Context context) {
        this.counterArr = counterArr;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return counterArr.get(position).getId();
    }


    @Override
    public int getCount() {
        return counterArr.size();
    }

    @Override
    public Object getItem(int position) {
        return counterArr.get(position);
    }


    /**
     * Set view for each counter
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.counter_kredit_item, parent, false);

        //init var
        nama = (TextView)v.findViewById(R.id.nama_counter);
        jumlah = (TextView)v.findViewById(R.id.pemasukan_id);
        tombolBayar = (Button)v.findViewById(R.id.bayar_button);

        //set var
        nama.setText(counterArr.get(position).getCounterName());

        int totalTemp = counterArr.get(position).getPemasukan();

        if(totalTemp == 0){
            jumlah.setText("Rp. 0,00");
            tombolBayar.setEnabled(false);
        }else{
            int ribuan = totalTemp / 1000;
            totalTemp = totalTemp - ribuan * 1000;
            if (totalTemp == 0) {
                jumlah.setText("Rp. " + ribuan + ".000,00");
            } else {
                jumlah.setText("Rp. " + ribuan + "" + totalTemp + ",00");
            }
        }

        //if button tombolBayar is pressed, bayar pemasukan untuk counter tsb
        tombolBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bayar(counterArr.get(position).getUsername(), counterArr.get(position).getCounterName());
                tombolBayar.setEnabled(false);
                counterArr.get(position).bayar();
                notifyDataSetChanged();
            }
        });
        return v;
    }


    public void bayar(String user, final String nama ) {

        final String username = user;


        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://aaa.esy.es/coba_wahid/bayar.php";
        final StringRequest stringReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d("RESPONSE", "BAYAR Response: " + response.toString());

                Toast toast = Toast.makeText(context, "Berhasil membayar " + nama, Toast.LENGTH_SHORT);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to bayar url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                return params;
            }
        };

        queue.add(stringReq);
    }
}
