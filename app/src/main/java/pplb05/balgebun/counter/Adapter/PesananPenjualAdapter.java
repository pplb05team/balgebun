package pplb05.balgebun.counter.Adapter;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import pplb05.balgebun.app.AppConfig;
import pplb05.balgebun.app.AppController;
import pplb05.balgebun.counter.Entity.PesananPenjual;
import pplb05.balgebun.R;
import pplb05.balgebun.counter.Fragment.TabFragment;
import pplb05.balgebun.counter.Fragment.MenuActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dananarief on 02-04-16.
 * Kelas ini merupakan adapter untuk mengatur segalah hal terkait halaman list pesanan di penjual
 */
public class PesananPenjualAdapter extends BaseAdapter {

    private ArrayList<PesananPenjual> listPesanan;
    private Context context;
    private TextView namaMakanan;
    private TextView namaPembeli;
    private TextView jumlahPesanan;
    private Spinner stat;
    private Button batal;
    EditText inputBatal;
    private int checkSpin = 0;
    MenuActivity frag;

    public interface onDataChangeListener {

    }

    //constructor
    public PesananPenjualAdapter(ArrayList<PesananPenjual> listPesanan, Context context, MenuActivity frag) {
        this.listPesanan = listPesanan;
        this.context = context;
        this.frag = frag;
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

        //Beberapa baris kode ini untuk inisialisasi sesuai tampilan layout
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

        //untuk mengatur tampilan status sesuai data di database
        if (listPesanan.get(position).getStatus().equals("belum")) {
            stat.setSelection(0);
            stat.setTag(0);
        } else if (listPesanan.get(position).getStatus().equals("dimasak")) {
            stat.setSelection(1);
            stat.setTag(1);
        } else {
            stat.setSelection(2);
            stat.setTag(2);
        }
        Log.d("print id order ", "id order " + listPesanan.get(position).getId());

        //Spinner atau dropdown list status
        stat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //agar spinner tidak di load saat inisasliasasi layar
            int countSpin = 0;

            //method yang digunakan ketika memilih salah satu status di dropdown list
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long id) {

                if (countSpin >= 1) {

                    //handle untuk kasus status "belum" yang langsung loncat ke status "selesai"
                    if (listPesanan.get(position).getStatus().equals("belum")) {
                        //jika status dari belum ke selesai, tidak boleh
                        if (parent.getItemAtPosition(pos).equals("selesai")) {
                            Toast.makeText(context.getApplicationContext(), "Pesanan harus dimasak dahulu", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();

                        } else {
                            //jika status dari belum ke belum atau dari belum ke diamasak, boleh
                            updateStatus(parent, view, pos, id, position);
                            Toast.makeText(context.getApplicationContext(), "Berhasil Ubah Status", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //jika status berada di "dimasak" dan akan memilih status "selesai"
                        if (parent.getItemAtPosition(pos).equals("selesai")) {
                            //generate dialog box confirmation
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Konfirmasi Pesanan Selesai");
                            inputBatal = new EditText(context);

                            builder.setMessage("Apa anda yakin telah selesai?").setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //update status di database
                                    updateStatus(parent, view, pos, id, position);
                                    Toast.makeText(context.getApplicationContext(), "Pesanan selesai", Toast.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //jika tidak jadi mimilih status "selesai"
                                    notifyDataSetChanged();
                                }
                            });

                            AlertDialog alert = builder.create();
                            builder.show();
                        } else {
                            Log.d("boleh status", "case aman");
                            updateStatus(parent, view, pos, id, position);
                        }

                    }

                }
                countSpin++;
                Log.d("laptop", "" + countSpin);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        //tombol batal untuk cancel pesanan
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Konfirmasi Pembatalan");
                inputBatal = new EditText(context);

                //set pop up
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(40, 0, 30, 0);
                layout.addView(inputBatal, params);

                builder.setView(layout);

                builder.setMessage("Apa anda yakin akan menghapus?").setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletePesanan(position);
                    }
                }).setNegativeButton("Tidak", null);

                AlertDialog alert = builder.create();

                builder.show();
            }
        });
        return v;
    }

    //method untuk mengatur tampilan konfirmasi pembatalan
    public static float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    //method update status yang akan mengubah database dan merefesh tampilan
    public void updateStatus(final AdapterView<?> parent, View view, final int pos, long id, final int position) {
        Log.d("apakah loading27", "loadd");

        String tag_string_order = "req_order";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CANCEL_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.d("e", response.toString());
                    if (!error) {
                        Log.d("update", "update");

                        frag.getPesananList();

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Log.d("erorUpdate1", "erorUpdate1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("erorUpdate", "erorpdate");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Log.d("SedangUpdate", "SedangUpdate");
                // Posting params to update url
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_order", "" + listPesanan.get(position).getId());
                params.put("status", "" + parent.getItemAtPosition(pos));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "req_order");

    }

    //method delete pesanan yang akan mengubpdate database dan merefresh tampilan
    public void deletePesanan(final int position) {
        Log.d("confirmation", "berhasil menghapus Lagi");
        Log.d("tesButtonkuGitu", "sukses");
        Log.d("id", "" + listPesanan.get(position).getId());
        Log.d("cek input batal", inputBatal.getText().toString());
        String tag_string_order = "req_order";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("respon batal", "Respon pesanan: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Log.d("deleteRespon", response.toString());
                    if (!error) {
                        Log.d("deletefunct", "deletefunct");
                        frag.getPesananList();

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Log.d("erorDelete1", "erorDelete1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("erorDel", "erorDel");
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Log.d("SedangDelete", "SedangDelete");
                // Posting params to update url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("jumlaArray", "" + listPesanan.size());
                params.put("id_order", "" + listPesanan.get(position).getId());
                Log.d("cekIdDelete", "" + listPesanan.get(position).getId());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "req_order");

    }

}
