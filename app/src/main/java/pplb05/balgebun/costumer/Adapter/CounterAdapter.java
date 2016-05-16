package pplb05.balgebun.costumer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

import pplb05.balgebun.R;
import pplb05.balgebun.app.VolleySingleton;
import pplb05.balgebun.costumer.Entity.CounterEntity;

/**
 * Created by Wahid Nur Rohman on 3/24/2016.
 *
 * This Adapter used for making view and accessing item of counter.
 */
public class CounterAdapter extends BaseAdapter {
    ArrayList<CounterEntity> counters = new ArrayList<>();
    private Context context;
    private ImageView imgView;
    private TextView txtView;

    /**
     * Contructor of Counter Adapter
     *
     * @param counters data whic will be displayed
     * @param context
     */
    public CounterAdapter(ArrayList<CounterEntity> counters, Context context) {
        this.counters = counters;
        this.context = context;

    }

    /**
     * Get number of data
     *
     * @return size of counter
     */
    @Override
    public int getCount() {
        return counters.size();
    }

    /**
     * Get item object for specific position
     *
     * @param position which object will be chosen
     * @return object in that position
     */
    @Override
    public Object getItem(int position) {
        return counters.get(position);
    }

    /**
     * Get id from the position
     *
     * @param position position of the counter
     * @return id
     */
    @Override
    public long getItemId(int position) {
        return counters.get(position).getId();
    }

    /**
     * Get view from the specific position given
     *
     * @param position    the position of the counter
     * @param convertView
     * @param parent
     * @return view for a counter
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater l = LayoutInflater.from(context);
            convertView = l.inflate(R.layout.counter_layout, parent, false);

            holder = new ViewHolder();
            holder.txtView = (TextView) convertView.findViewById(R.id.textCounter);
            holder.imgView = (ImageView) convertView.findViewById(R.id.imageCounter);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
            holder.imgView.setImageResource(R.drawable.food);
        }
        CounterEntity counter = counters.get(position);
        holder.txtView.setText(counter.getCounterName());
        // get image from the hosting
        getImage(holder.imgView, counter.getImageURL());

        return convertView;
    }

    /**
     * Get image (bitmap) from hosting and draw it on ImageView of the counter
     *
     * @param imageView image view will be set
     * @param fileUrl   url of the image
     */
    private void getImage(final ImageView imageView, String fileUrl) {
        ImageRequest imgReq = new ImageRequest(fileUrl, new Response.Listener<Bitmap>() {

            /**
             * Drae thw image to the imageviw
             * @param response
             */
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Use VolleySingelton
        VolleySingleton.getInstance(context).addToRequestQueue(imgReq);

    }

    private static class ViewHolder{
        TextView txtView;
        ImageView imgView;
    }

}


