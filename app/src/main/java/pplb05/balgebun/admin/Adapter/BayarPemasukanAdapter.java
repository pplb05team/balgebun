package pplb05.balgebun.admin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.admin.Entity.DetailPemasukan;
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
    private boolean dibayar;


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

                Intent intent = new Intent(context, DetailPemasukan.class);
                intent.putExtra("counterUsername", counterArr.get(position).getUsername());
                intent.putExtra("counterName", counterArr.get(position).getCounterName());
                intent.putExtra("counterPemasukan", counterArr.get(position).getPemasukan());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        return v;
    }



}
