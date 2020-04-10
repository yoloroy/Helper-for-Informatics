package e.pmart.project.slides;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import e.pmart.project.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EvalSlide extends Fragment {


    public EvalSlide() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.slide_eval, container, false);
    }

}
