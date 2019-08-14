package e.pmart.project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class RecyclerView_StringAdapter extends RecyclerView.Adapter<RecyclerView_StringAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<String> strings;
    private int list_item;

    RecyclerView_StringAdapter(Context context, List<String> strings, int list_item) {
        this.strings = strings;
        this.inflater = LayoutInflater.from(context);
        this.list_item = list_item;
    }

    RecyclerView_StringAdapter(Context context, String[] strings, int list_item) {
        this.strings = Arrays.asList(strings);
        this.inflater = LayoutInflater.from(context);
        this.list_item = list_item;
    }

    @Override
    public RecyclerView_StringAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView_StringAdapter.ViewHolder holder, int position) {
        holder.textView.setText(strings.get(position));
    }


    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.text1);
        }
    }
}
