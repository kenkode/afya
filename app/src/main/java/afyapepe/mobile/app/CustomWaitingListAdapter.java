package afyapepe.mobile.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import afyapepe.mobile.R;
import afyapepe.mobile.activity.Patient;
import afyapepe.mobile.model.Movie;
import afyapepe.mobile.model.Patients;
import afyapepe.mobile.model.WaitingList;

/**
 * Created by Elisha on 7/24/2017.
 */

public class CustomWaitingListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<WaitingList> patientItems;
    //ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomWaitingListAdapter(Activity activity, List<WaitingList> patientItems) {
        this.activity = activity;
        this.patientItems = patientItems;
    }

    @Override
    public int getCount() {
        return patientItems.size();
    }

    @Override
    public Object getItem(int location) {
        return patientItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.waiting_list, null);

        /*if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();*/
        /*NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);*/
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView age = (TextView) convertView.findViewById(R.id.age);
        TextView gender = (TextView) convertView.findViewById(R.id.gender);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        // getting movie data for the row
        WaitingList p = patientItems.get(position);

        // thumbnail image
        //thumbNail.setImageUrl(p.getThumbnailUrl(), imageLoader);

        // title
        name.setText(p.getName());

        // rating
        age.setText("Age: " + String.valueOf(p.getAge()));

        gender.setText("Gender: " +p.getGender());

        // release year
        time.setText("Time waited to see the doctor: " +p.getTime());

        return convertView;
    }

}