package e.pmart.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


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
        FrameLayout curr;
        LinearLayout goto_layout = ((LinearLayout) root_view.findViewById(R.id.goto_layout));

        for (int i = 0; i < goto_layout.getChildCount(); i++) {
            curr = (FrameLayout) goto_layout.getChildAt(i);
            //curr.setBackgroundColor(getResources().getColor(R.color.colorMenuBackUnpressed));а

            ((ImageView) curr.getChildAt(0))
                    .setColorFilter(getResources().getColor(android.R.color.darker_gray));
            ((ImageView) curr.getChildAt(0)).setActivated(false);
            ((TextView) curr.getChildAt(1))
                    .setTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        curr = (FrameLayout) goto_layout.getChildAt(num);
        //curr.setBackgroundColor(getResources().getColor(R.color.colorMenuBackPressed));
        ((ImageView) curr.getChildAt(0))
                .setColorFilter(getResources().getColor(R.color.colorMenuBackUnpressed));
        ((ImageView) curr.getChildAt(0)).setActivated(true);
        ((TextView) curr.getChildAt(1))
                .setTextColor(getResources().getColor(R.color.colorMenuBackUnpressed));
    }
}
