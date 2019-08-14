package e.pmart.project;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculatorFragment extends Fragment {
    View root_view;


    public CalculatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_calculator, container, false);

        Integer[] calc_num_systems = {16, 10, 8, 2};
        ((Spinner) root_view.findViewById(R.id.calc_num_system_spinner))
                .setAdapter(new ArrayAdapter<Integer>(getContext(),
                        R.layout.my_simple_dropdown_spinner_item,
                        calc_num_systems));

        ((Spinner) root_view.findViewById(R.id.calc_num_system_spinner))
                .setSelection(1);

        ((Spinner) root_view.findViewById(R.id.calc_num_system_spinner))
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((MainActivity) getActivity()).calc_onClickInstantEvaluate(view);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

        return root_view;
    }

    public void calc_onClickEvaluate(View view) {}
    public void calc_onClickButton(View view) {}
}
