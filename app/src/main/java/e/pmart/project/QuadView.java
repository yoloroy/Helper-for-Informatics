package e.pmart.project;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;


public class QuadView extends CardView {
    protected boolean correct = false;
    public QuadView (Context context){
        super(context);
    }
    public QuadView (Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public QuadView (Context context, AttributeSet attrs, int defStyle){
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
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
