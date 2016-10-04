package projeto1.ufcg.edu.decasa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Professional;
import projeto1.ufcg.edu.decasa.utils.DownloadFile;

public class ProfessionalsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Professional> items;
    Context context;

    public ProfessionalsAdapter(Context context, List<Professional> items) {
        this.items = items;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Professional item = items.get(position);
        convertView = mInflater.inflate(R.layout.my_item_professional, null);

        if (!item.getPlan().equals("free") && !item.getPlan().equals("bronze")) {
            String address = item.getNeighborhood() + " - " + item.getCity() ;
            TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            tv_address.setText(address);
            tv_address.setTextColor(Color.BLACK);
        }

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        tv_name.setText(item.getName());
        tv_name.setTextColor(Color.BLACK);
        ((RatingBar) convertView.findViewById(R.id.rb_evaluation_professionals)).
                setRating(item.getAvg());

        CircularImageView iv_professional = ((CircularImageView) convertView.
                findViewById(R.id.iv_professional));

        if (item.getPicture() != null && !item.getPicture().equals("null")) {
            File f = new File(DownloadFile.getPathDownload() + item.getPicture());
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            iv_professional.setImageBitmap(bmp);
        }

        return convertView;
    }

}
