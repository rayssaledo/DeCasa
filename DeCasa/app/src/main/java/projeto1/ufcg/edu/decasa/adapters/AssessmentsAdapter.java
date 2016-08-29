package projeto1.ufcg.edu.decasa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import projeto1.ufcg.edu.decasa.R;
import projeto1.ufcg.edu.decasa.models.Evaluation;

public class AssessmentsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Evaluation> items;
    private ImageView option;
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

        String stringPhoto = item.getPhoto();
        byte[] photoByte = Base64.decode(stringPhoto, Base64.DEFAULT);
        Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);

        ((ImageView) convertView.findViewById(R.id.iv_user)).setImageBitmap(photoBitmap);
        ((TextView) convertView.findViewById(R.id.tv_user_name)).setText(item.getUsernameValuer());
        ((TextView) convertView.findViewById(R.id.tv_date_evaluation)).setText(item.getDate());
        ((RatingBar) convertView.findViewById(R.id.rb_evaluation_user)).setRating(item.
                getEvaluationValue());
        ((TextView) convertView.findViewById(R.id.tv_comment)).setText(item.getComment());

        option = (ImageView) convertView.findViewById(R.id.option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(option);
            }
        });

        return convertView;
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_evaluation, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_edit_evaluation:
                    Toast.makeText(context, "Edit evaluation", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(context, "Delete evaluation", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
}
