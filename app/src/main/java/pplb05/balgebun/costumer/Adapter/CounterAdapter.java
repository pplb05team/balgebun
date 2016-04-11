package pplb05.balgebun.costumer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import pplb05.balgebun.costumer.Entity.CounterEntity;
import pplb05.balgebun.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Wahid Nur Rohman on 3/24/2016.
 */
public class CounterAdapter extends BaseAdapter {
    ArrayList<CounterEntity> counters;
    private Context context;
    private ImageView imgView;
    private TextView txtView;
    private LruCache<String, Bitmap> mMemoryCache;

    private ImageLoader imageLoader;

    public CounterAdapter(ArrayList<CounterEntity> counters, Context context) {
        this.counters = counters;
        this.context = context;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number
                // of items.
                if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 12) {
                    return bitmap.getByteCount();
                } else {
                    return (bitmap.getRowBytes() * bitmap.getHeight());
                }
            }
        };

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
        return counters.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CounterEntity counter = counters.get(position);
        LayoutInflater l = LayoutInflater.from(context);
        View v = l.inflate(R.layout.counter_layout, parent, false);

        txtView = (TextView)v.findViewById(R.id.textCounter);
        imgView = (ImageView)v.findViewById(R.id.imageCounter);

        downloadFile(imgView, counter.getImageURL(), counter);

        txtView.setText(counter.getCounterName());
        //imgView.setImageResource(mThumbIds[position]);

        return v;
    }

    void downloadFile(final ImageView imageView, String fileUrl, final CounterEntity counter) {

        if(counter.getBitmap() != null) {
            imageView.setImageBitmap(counter.getBitmap());
            //Log.d("Cache", "Success");
        } else {
            AsyncTask<String, Object, String> task = new AsyncTask<String, Object, String>() {
                Bitmap bmImg;
                @Override
                protected String doInBackground(String... params) {
                    URL myFileUrl = null;
                    try {
                        myFileUrl = new URL(params[0]);
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection conn = (HttpURLConnection) myFileUrl
                                .openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();

                        bmImg = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    return null;
                }

                protected void onPostExecute(String unused) {
                    imageView.setImageBitmap(bmImg);
                    counter.setBitmap(bmImg);
                }
            };
            task.execute(fileUrl);
       }
    }
}
