package e.pmart.project;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import static e.pmart.project.MainActivity.APP_TEMP;

public class EducationGridAdapter extends ArrayAdapter<String> {
    public Activity context;
    private final String[] names;
    public String[] mode_names;

    public EducationGridAdapter(Activity context, String[] names, String[] mode_names) {
        super(context, R.layout.education_link_node, names);
        this.context = context;
        this.names = names;
        this.mode_names = mode_names;
    }

    static class ViewHolder {
        public TextView name;
        public TextView finished;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.education_link_node, null, true);
            holder = new ViewHolder();
            holder.name = rowView.findViewById(R.id.name);
            holder.finished = rowView.findViewById(R.id.finished);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.name.setText(names[position]);

        SharedPreferences mSettings =
                context.getSharedPreferences(APP_TEMP, Context.MODE_PRIVATE);
        if (mSettings.contains(mode_names[position])) {
            Log.i("-ed", "getView: "+mSettings.contains(mode_names[position]));
            if (mSettings.getBoolean(mode_names[position], false)) {
                holder.finished.getBackground()
                        .setTint(context.getResources().getColor(R.color.colorAppTrue));
                holder.finished.setText("Пройдено");
            } else {
                holder.finished.getBackground()
                        .setTint(context.getResources().getColor(R.color.colorAppFalse));
                holder.finished.setText("Непройдено");
            }
        } else {
            Log.i("-ed", "getView: course settings not contains");
            holder.finished.getBackground()
                    .setTint(context.getResources().getColor(R.color.colorAppFalse));
            holder.finished.setText("Непройдено");
        }
        Log.i("-ed", "getView: "+mode_names[position]);

        return rowView;
    }
}
