package e.pmart.project;

import android.support.annotation.NonNull;

public class ToNumSystem {
    // TODO: пофиксить неправильный перевод дробных чисел
    @NonNull
    public static String run(double num, int num_syst) {
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
                answer += (int) (temp * num_syst);

                temp = temp * num_syst - (int) (temp * num_syst);

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
