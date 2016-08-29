package projeto1.ufcg.edu.decasa.adapters;

import android.content.Context;
import android.content.Intent;
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
import projeto1.ufcg.edu.decasa.utils.MySharedPreferences;
import projeto1.ufcg.edu.decasa.views.AssessmentsActivity;
import projeto1.ufcg.edu.decasa.views.EvaluationProfessionalActivity;

public class AssessmentsAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Evaluation> items;
    private ImageView option;
    private Evaluation item;
    private String usernameValuer;
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
        item = items.get(position);
        convertView = mInflater.inflate(R.layout.my_item_evaluation, null);

        usernameValuer = item.getUsernameValuer();

        String stringPhoto = item.getPhoto();
        byte[] photoByte = Base64.decode(stringPhoto, Base64.DEFAULT);
        Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoByte, 0, photoByte.length);

        ((ImageView) convertView.findViewById(R.id.iv_user)).setImageBitmap(photoBitmap);
        ((TextView) convertView.findViewById(R.id.tv_user_name)).setText(usernameValuer);
        ((TextView) convertView.findViewById(R.id.tv_date_evaluation)).setText(item.getDate());
        ((RatingBar) convertView.findViewById(R.id.rb_evaluation_user)).setRating(item.
                getEvaluationValue());
        ((TextView) convertView.findViewById(R.id.tv_comment)).setText(item.getComment());

        option = (ImageView) convertView.findViewById(R.id.option);
        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        if (usernameValuer.equals(mySharedPreferences.getUserLogged())) {
            option.setVisibility(View.VISIBLE);
        }
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
                    Intent it = new Intent(AssessmentsActivity.mAssessmentsActivity,
                            EvaluationProfessionalActivity.class);
                    it.putExtra("EVALUATION", item);
                    AssessmentsActivity.mAssessmentsActivity.startActivity(it);
                    return true;
                default:
            }
            return false;
        }
    }
}
