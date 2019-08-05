package e.pmart.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class GotoFragment extends Fragment {
    View root_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root_view =
                inflater.inflate(R.layout.goto_down_menu, container, false);
        return root_view;
    }
    public void setSelection(int num) {
        ImageButton curr;
        LinearLayout goto_layout = ((LinearLayout) root_view.findViewById(R.id.goto_layout));

        for (int i = 0; i < goto_layout.getChildCount(); i++) {
            curr = (ImageButton) goto_layout.getChildAt(i);
            curr.setBackgroundColor(getResources().getColor(R.color.colorMenuBackUnpressed));
            curr.setColorFilter(getResources().getColor(R.color.colorMenuBackPressed));
        }

        curr = (ImageButton) goto_layout.getChildAt(num);
        curr.setBackgroundColor(getResources().getColor(R.color.colorMenuBackPressed));
        curr.setColorFilter(getResources().getColor(R.color.colorMenuBackUnpressed));
    }
}
