/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author aleyase2-admin
 */
public class test {

    public static double[] salam(String[] args) {
        List<Double> stream = new ArrayList<Double>();
        stream.addAll(Arrays.asList(1.0, 2.0, 3.0, 4.9));
        Integer count = 0;
        Double mean = 0.0;
        Double variance = 0.0;
        Double temp2 = 0.0;
        Double temp1 = 0.0;
//        for (Double d : stream) { // we calculate var*(count-1) in this loop becuase we don’t want to confront divide by zero problem when count=1 , temp stores var*(count-1)
//            count++;
//            mean = (mean * (count - 1) + d) / count;
//            temp = temp + d * d + (d * d + 2 * d * mean * count) / count;
//        }

        for (Double d : stream) { // we calculate var*(count-1) in this loop becuase we don’t want to confront divide by zero problem when count=1 , temp stores var*(count-1)
            count++;
            temp1 = (d - mean);
            mean = mean + temp1 / count;
            temp2 = temp2 + temp1 * (d - mean);
        }
        System.out.println("mean= " + mean);
        System.out.println("var " + (temp2 / (count - 1)));
        return new double[]{mean, temp2};

    }

    public static void main(String[] args) {
        System.out.println(salam(args)[0]);
    }
}
