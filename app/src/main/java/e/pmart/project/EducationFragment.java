package e.pmart.project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


public class EducationFragment extends Fragment {
    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_education, container, false);


        GridView gridView = rootView.findViewById(R.id.education_grid);

        gridView.setAdapter(new EducationGridAdapter(getActivity(),
                new String[]{"Графы"},
                new String[]{"course_graphs"}));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity) getActivity()).changeMode(
                        ((EducationGridAdapter) gridView.getAdapter())
                                .mode_names[position]
                );
            }
        });

        return rootView;
    }

    public void changeMode(View view) {}
}
