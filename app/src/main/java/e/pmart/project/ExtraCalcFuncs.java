package e.pmart.project;

import android.util.Log;

import org.mariuszgromada.math.mxparser.Function;
import org.mariuszgromada.math.mxparser.FunctionExtension;

import static android.support.constraint.Constraints.TAG;

class ExtraCalcFuncs {
    public Function[] getExtraCalcFuncs() {
        Function[] a = {
                new Function("bnot", new BinaryNot()),
                new Function("square_root", new MathRoot() {
                    @Override
                    public void setParameterValue(int parameterIndex, double parameterValue) {
                        if (parameterIndex == 0) a = parameterValue;
                        b = 2;
                    }
                    @Override
                    public int getParametersNumber() {
                        return 1;
                    }
                    @Override
                    public MathRoot clone() {
                        return new MathRoot(a, 2);
                    } }),
                new Function("volume_root", new MathRoot() {
                    @Override
                    public void setParameterValue(int parameterIndex, double parameterValue) {
                        if (parameterIndex == 0) a = parameterValue;
                        b = 3;
                    }
                    @Override
                    public int getParametersNumber() {
                        return 1;
                    }
                    @Override
                    public MathRoot clone() {
                        return new MathRoot(a, 3);
                    } })
        };
        return a;
    }

    public class BinaryNot implements FunctionExtension {
        double a;
        double b;
        public BinaryNot() {
            a = Double.NaN;
            b = Double.NaN;
        }
        public BinaryNot(double a, double b) {
            this.a = a;
            this.b = b;
        }
        public int getParametersNumber() {
            return 2;
        }
        public void setParameterValue(int parameterIndex, double parameterValue) {
            if (parameterIndex == 0) a = parameterValue;
            if (parameterIndex == 1) b = parameterValue;
        }
        @Override
        public double calculate(double... parameters) {
            String num = Integer.toBinaryString((int) a);
            Log.i(TAG, "calculate: "+num);
            num = num.replace('0', '2');
            num = num.replace('1', '0');
            num = num.replace('2', '1');

            while (num.length() <= b)
                num = "1" + num;
            return (double) (Integer.parseInt(num, 2));
        }

        public BinaryNot clone() {
            return new BinaryNot(a, b);
        }
    }
    public class MathRoot implements FunctionExtension {
        double a;
        double b;
        public MathRoot() {
            a = Double.NaN;
            b = Double.NaN;
        }
        public MathRoot(double a, double b) {
            this.a = a;
            this.b = b;
        }
        public int getParametersNumber() {
            return 2;
        }
        public void setParameterValue(int parameterIndex, double parameterValue) {
            if (parameterIndex == 0) a = parameterValue;
            if (parameterIndex == 1) b = parameterValue;
        }
        @Override
        public double calculate(double... parameters) {
            return Math.pow(a, 1 / b);
        }

        public MathRoot clone() {
            return new MathRoot(a, b);
        }
    }
}
