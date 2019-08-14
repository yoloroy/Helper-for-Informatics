package e.pmart.project;

import android.support.annotation.NonNull;
import android.util.Log;

import org.mariuszgromada.math.mxparser.Function;
import org.mariuszgromada.math.mxparser.FunctionExtension;

import static android.support.constraint.Constraints.TAG;

class ExtraCalcFuncs {
    final Function[] extra_calc_funcs = {
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
                } }),
            new Function("my_log", new MyLog()),
            new Function("my_log2", new MyLog2())
    };
    public Function[] getExtraCalcFuncs() {
        return extra_calc_funcs;
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
    public class MyLog implements FunctionExtension {
        double a;


        public MyLog() {
            a = Double.NaN;
        }
        public MyLog(double a) {
            this.a = a;
        }
        public int getParametersNumber() {
            return 1;
        }
        public void setParameterValue(int parameterIndex, double parameterValue) {
            if (parameterIndex == 0) a = parameterValue;
        }
        @Override
        public double calculate(double... parameters) {
            return Math.log(a);
        }

        public MyLog clone() {
            return new MyLog(a);
        }
    }
    public class MyLog2 implements FunctionExtension {
        double a;


        public MyLog2() {
            a = Double.NaN;
        }
        public MyLog2(double a) {
            this.a = a;
        }
        public int getParametersNumber() {
            return 1;
        }
        public void setParameterValue(int parameterIndex, double parameterValue) {
            if (parameterIndex == 0) a = parameterValue;
        }
        @Override
        public double calculate(double... parameters) {
            return Math.log(a) / Math.log(2);
        }

        public MyLog2 clone() {
            return new MyLog2(a);
        }
    }
}

class ToNumSystem {
    @NonNull
    static String run(double num, int num_syst) {
        String answer = "";

        if (num_syst > 37 || num_syst < 2)
            throw new RuntimeException("Wrong numeral system");

        // before point part
        answer += Integer.toString((int) num, num_syst);

        // after point part
        double temp = num - (int) num;

        if (temp != 0.0d) {
            answer += ".";

            for (int i = 0; i <= 16; i++) {
                Log.i("####", "run: ");
                answer += (int) (temp * num_syst);
                Log.i("    ", "run: " + (temp * num_syst));
                Log.i("    ", "run: " + (int) (temp * num_syst));

                temp = temp * num_syst - (int) (temp * num_syst);
                Log.i("    ", "run: " + (temp * num_syst));

                if (temp == 0)
                    break;
            }

            while (answer.endsWith("0"))
                answer = answer.substring(0, answer.length() - 1);

            if (answer.endsWith("."))
                answer = answer.substring(0, answer.length() - 1);
        }

        return answer;
    }
}
