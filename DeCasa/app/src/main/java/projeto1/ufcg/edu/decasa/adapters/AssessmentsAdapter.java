package projeto1.ufcg.edu.decasa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Professional;

public class AssessmentsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    //TODO change Professional to Evaluation class
    private List<Professional> items;
    Context context;

    //TODO change Professional to Evaluation class
    public AssessmentsAdapter(Context context, List<Professional> items) {
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
        //TODO change Professional to Evaluation class
        Professional item = items.get(position);
        convertView = mInflater.inflate(R.layout.my_item_evaluation, null);

        //TODO set up values about Evaluation class...
        //((ImageView) convertView.findViewById(R.id.iv_user)).setImageBitmap();
        ((TextView) convertView.findViewById(R.id.tv_user_name)).setText(item.getName());
        //((TextView) convertView.findViewById(R.id.tv_date_evaluation)).setText();
        //((TextView) convertView.findViewById(R.id.rb_evaluation_user)).setText();
        //((TextView) convertView.findViewById(R.id.tv_comment)).setText();

        return convertView;
    }

}
