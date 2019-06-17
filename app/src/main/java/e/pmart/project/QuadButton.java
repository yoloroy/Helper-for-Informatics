package e.pmart.project;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;


public class QuadButton extends AppCompatButton {
    protected boolean correct = false;
    public QuadButton (Context context){
        super(context);
    }
    public QuadButton (Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public QuadButton (Context context, AttributeSet attrs, int defStyle){
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
