package e.pmart.project;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class QuadImage extends AppCompatImageView {
    protected boolean correct = false;
    public QuadImage (Context context){
        super(context);
    }
    public QuadImage (Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public QuadImage (Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }
    public void setCorrect (boolean correct){
        this.correct = correct;
    }
    public boolean getCorrect (){
        return this.correct;
    }
    @Override
    protected void onMeasure(int wSpec, int hSpec) {
        super.onMeasure(wSpec, hSpec);

        if (getMeasuredWidth() > getMeasuredHeight())
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        else
            setMeasuredDimension(getMeasuredHeight(), getMeasuredHeight());
    }
}
