package projeto1.ufcg.edu.decasa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Evaluation;
import projeto1.ufcg.edu.decasa.models.Professional;

public class AssessmentsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Evaluation> items;
    Context context;

    public AssessmentsAdapter(Context context, List<Evaluation> items) {
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
        Evaluation item = items.get(position);
        convertView = mInflater.inflate(R.layout.my_item_evaluation, null);

        //TODO set up values about Evaluation class...
        //((ImageView) convertView.findViewById(R.id.iv_user)).setImageBitmap();
        ((TextView) convertView.findViewById(R.id.tv_user_name)).setText(item.getUsernameValuer());
        ((TextView) convertView.findViewById(R.id.tv_date_evaluation)).setText(item.getDate());
        ((RatingBar) convertView.findViewById(R.id.rb_evaluation_user)).setRating(item.
                getEvaluationValue());
        ((TextView) convertView.findViewById(R.id.tv_comment)).setText(item.getComment());

        return convertView;
    }

}
