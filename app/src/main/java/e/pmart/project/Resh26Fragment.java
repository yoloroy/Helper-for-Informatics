package e.pmart.project;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.blox.graphview.BaseGraphAdapter;
import de.blox.graphview.Graph;
import de.blox.graphview.GraphView;
import de.blox.graphview.Node;
import de.blox.graphview.ViewHolder;
import de.blox.graphview.tree.BuchheimWalkerAlgorithm;
import de.blox.graphview.tree.BuchheimWalkerConfiguration;


/**
 * A simple {@link Fragment} subclass.
 */
public class Resh26Fragment extends Fragment {
    View root_view;

    Map<Integer, Node> convert_data = new HashMap<>();
    Map<Integer, List<Integer>> relatives = new HashMap<>();

    GraphView graphView;

    int node_num = 0;
    int ending_leafs = 0;


    public Resh26Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_resh26, container, false);

        graphView = root_view.findViewById(R.id.answer_26);

        BaseGraphAdapter<ViewHolder> adapter = new BaseGraphAdapter<ViewHolder>() {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.node, parent, false);
                return new SimpleViewHolder(view);
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, Object data, int position) {
                String temp = "";
                String[] temp_arr = data.toString().split(" ");

                for (int i = 1; i < temp_arr.length; i++) {
                    temp += temp_arr[i];
                }

                ((SimpleViewHolder) viewHolder).textView.setText(temp);
            }
        };
        graphView.setAdapter(adapter);

        // set the algorithm here
        BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setLevelSeparation(70)
                .setSiblingSeparation(50)
                .setSubtreeSeparation(70)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build();
        adapter.setAlgorithm(new BuchheimWalkerAlgorithm(configuration));

        return root_view;
    }

    public void corrEdits(View view) {
        View corr_obj;

        switch (((View) view.getParent()).getId()) {
            case R.id.corr_edits1:
                corr_obj = getActivity().findViewById(R.id.pos_26);
                switch ((String) ((Button) view).getText()) {
                    case "+":
                        if (((GridLayout) corr_obj).getChildCount() < 8) {
                            EditText pos = (EditText) ((GridLayout) corr_obj).getChildAt(0);

                            EditText editText = new EditText(getContext());
                            editText.setPadding(
                                    pos.getCompoundPaddingLeft(),
                                    pos.getCompoundPaddingTop(),
                                    pos.getCompoundPaddingRight(),
                                    pos.getCompoundPaddingBottom());
                            editText.setLayoutParams(new LinearLayout
                                    .LayoutParams(pos.getLayoutParams()));
                            editText.setInputType(pos.getInputType());
                            editText.setText("0");
                            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, pos.getTextSize());

                            ((GridLayout) corr_obj).addView(editText);
                        }
                        break;
                    case "−":
                        if (((GridLayout) corr_obj).getChildCount() > 1)
                            ((GridLayout) corr_obj).removeViewAt(((GridLayout) corr_obj).getChildCount()-1);
                        break;
                    default:
                        throw new RuntimeException("Wrong onClick");
                }
                break;
            case R.id.corr_edits2:
                corr_obj = getActivity().findViewById(R.id.paths_26);
                switch ((String) ((Button) view).getText()) {
                    case "+":
                        EditText pos = (EditText) ((LinearLayout) corr_obj).getChildAt(0);

                        EditText editText = new EditText(getContext());
                        editText.setPadding(
                                pos.getCompoundPaddingLeft(),
                                pos.getCompoundPaddingTop(),
                                pos.getCompoundPaddingRight(),
                                pos.getCompoundPaddingBottom());
                        editText.setLayoutParams(new LinearLayout
                                .LayoutParams(pos.getLayoutParams()));
                        editText.setInputType(pos.getInputType());
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, pos.getTextSize());

                        ((LinearLayout) corr_obj).addView(editText);
                        break;
                    case "−":
                        if (((LinearLayout) corr_obj).getChildCount() > 1)
                            ((LinearLayout) corr_obj).removeViewAt(((LinearLayout) corr_obj).getChildCount()-1);
                        break;
                    default:
                        throw new RuntimeException("Wrong onClick");
                }
                break;
            case R.id.corr_edits3:
                corr_obj = getActivity().findViewById(R.id.ends_26);
                switch ((String) ((Button) view).getText()) {
                    case "+":
                        EditText pos = (EditText) ((LinearLayout) corr_obj).getChildAt(0);

                        EditText editText = new EditText(getContext());
                        editText.setPadding(
                                pos.getPaddingLeft(),
                                pos.getPaddingTop(),
                                pos.getPaddingRight(),
                                pos.getPaddingBottom());
                        editText.setLayoutParams(new LinearLayout
                                .LayoutParams(pos.getLayoutParams()));
                        editText.setInputType(pos.getInputType());
                        editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, pos.getTextSize());

                        ((LinearLayout) corr_obj).addView(editText);
                        break;
                    case "−":
                        if (((LinearLayout) corr_obj).getChildCount() > 1)
                            ((LinearLayout) corr_obj).removeViewAt(((LinearLayout) corr_obj).getChildCount()-1);
                        break;
                    default:
                        throw new RuntimeException("Wrong onClick");
                }
                break;
        }
    }

    public void onClickRun26(View view) {
        convert_data = new HashMap<>();
        relatives = new HashMap<>();
        node_num = 0;
        ending_leafs = 0;

        Integer[] start_pos = collectPos();
        String[] steps = collectSteps();
        String[] ends = collectEnds();
        String[] players = new String[] {
                ((EditText) root_view.findViewById(R.id.name1_26)).getText().toString(),
                ((EditText) root_view.findViewById(R.id.name2_26)).getText().toString()};

        Resh26 resh = new Resh26();
        Map<String, Object> tree = resh.call(start_pos, ends, steps);

        Log.i("test 26", "onClickRun26: " + tree);
        Log.i("test 26", "onClickRun26: " + resh.calculate_winner(tree, players));

        ((TextView) root_view.findViewById(R.id.winner_26)).setText(resh.calculate_winner(tree, players));


        root_view.findViewById(R.id.scrollView5).setVisibility(View.INVISIBLE);
        root_view.findViewById(R.id.answer_26_papa).setVisibility(View.VISIBLE);
        root_view.findViewById(R.id.run26_back).setVisibility(View.VISIBLE);

        Log.i("sebrew", "onClickRun26: "+graphView);

        create_res_to_visualisation(tree);
        Log.i("test 26", "onClickRun26: " + convert_data);
        Log.i("test 26", "onClickRun26: " + relatives);

        Graph graph = new Graph();
        List<Integer> nodes = new ArrayList<>();
        nodes.add(0);
        Log.i("test ending_leafs", "onClickRun26: "+ending_leafs);
        while ((graph.getEdges().size()) < (relatives.size() - 1 + ending_leafs)) {
            for (Integer parent : relatives.keySet()) {
                for (Integer child : relatives.get(parent)) {
                    if (nodes.contains(parent) && !nodes.contains(child)) {
                        graph.addEdge(convert_data.get(parent),
                                convert_data.get(child));
                        nodes.add(child);
                    }
                }
            }
        }


        Log.i("dfgjrm", "onClickRun26: "+graph.getNodeCount());
        Log.i("dfgjrm", "onClickRun26: "+graph.getNodes());
        Log.i("dfgjrm", "onClickRun26: "+graph.getEdges());



        graphView.getAdapter().setGraph(graph);
        graphView.getAdapter().notifyInvalidated();
        graphView.destroyDrawingCache();
        graphView.refreshDrawableState();
    }

    public void onClickBack26(View view) {
        getActivity().findViewById(R.id.scrollView5).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.answer_26_papa).setVisibility(View.INVISIBLE);
        getActivity().findViewById(R.id.run26_back).setVisibility(View.INVISIBLE);
    }

    private void create_res_to_visualisation(Map<String, Object> tree, Integer parent) {
        for (String key: tree.keySet()) {
            node_num++;
            convert_data.put(node_num, new Node(node_num + " " + key));
            if (relatives.containsKey(parent))
                relatives.get(parent).add(node_num);
            else {
                relatives.put(parent, new ArrayList<>());
                relatives.get(parent).add(node_num);
            }
            try {
                create_res_to_visualisation((Map<String, Object>) tree.get(key), node_num);
            } catch (Exception e) {
                convert_data.put(node_num+1, new Node(node_num+1 + " " + (tree.get(key))));
                relatives.put(node_num, new ArrayList<>());
                relatives.get(node_num).add(node_num+1);
                node_num++;
                ending_leafs++;
            }
        }
    }
    private void create_res_to_visualisation(Map<String, Object> tree) {
        convert_data.put(0, new Node(node_num + " " + tree
                        .keySet().iterator().next()));
        create_res_to_visualisation((Map<String, Object>) tree.get(tree
                .keySet().iterator().next()), 0);
    }

    private Integer[] collectPos() {
        GridLayout pos_26 = root_view.findViewById(R.id.pos_26);
        Integer[] start_pos = new Integer
                [pos_26.getChildCount()];
        for (int i = 0; i < start_pos.length; i++) {
            start_pos[i] = Integer.valueOf(((EditText) pos_26.getChildAt(i)).getText().toString());
        }
        return start_pos;
    }

    private String[] collectSteps() {
        LinearLayout steps_26 = root_view.findViewById(R.id.paths_26);
        String[] steps = new String
                [steps_26.getChildCount()];
        for (int i = 0; i < steps.length; i++) {
            steps[i] = ((EditText) steps_26.getChildAt(i)).getText().toString().toUpperCase();
        }
        return steps;
    }

    private String[] collectEnds() {
        LinearLayout ends_26 = root_view.findViewById(R.id.ends_26);
        String[] ends = new String
                [ends_26.getChildCount()];
        for (int i = 0; i < ends.length; i++) {
            ends[i] = ((EditText) ends_26.getChildAt(i)).getText().toString().toUpperCase();
        }
        return ends;
    }

    class SimpleViewHolder extends ViewHolder {
        TextView textView;

        SimpleViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
