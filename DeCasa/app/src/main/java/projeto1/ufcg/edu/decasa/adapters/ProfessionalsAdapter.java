package projeto1.ufcg.edu.decasa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Professional;

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

        ((TextView) convertView.findViewById(R.id.tv_professional_name)).setText(item.getProfessionalName());
        ((TextView) convertView.findViewById(R.id.tv_service)).setText(item.getService());

        return convertView;
    }

}
