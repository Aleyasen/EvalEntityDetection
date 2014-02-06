/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection;

import com.aliasi.classify.ConfusionMatrix;
import com.aliasi.classify.PrecisionRecallEvaluation;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aleyase2-admin
 */
public class EvalEntityDetection {

    final static int maxClassNum = 100;
    static int classNum = 0;
    static String[] cats;

    static int[][] tempCells;
    static int[][] cells;

    public static void buildConfusionMatrix(String file) {

        tempCells = new int[maxClassNum][maxClassNum];
        for (int i = 0; i < maxClassNum; i++) {
            for (int j = 0; j < maxClassNum; j++) {
                tempCells[i][j] = 0;
            }
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split("\\s+");
                if (split.length == 4) {
                    int real = Integer.parseInt(split[2]);
                    tempCells[real][Integer.parseInt(split[3])]++;
                    if (real > classNum) {
                        classNum = real;
                    }
                } else {
                    System.out.println("problem in parsing");
                }

                line = br.readLine();
            }
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(EvalEntityDetection.class.getName()).log(Level.SEVERE, null, ex);
        }
        classNum++;
        cats = new String[classNum];
        for (int i = 0; i < classNum; i++) {
            cats[i] = "a" + i;
        }

        cells = new int[classNum][classNum];
        for (int i = 0; i < classNum; i++) {
            for (int j = 0; j < classNum; j++) {
                cells[i][j] = tempCells[i][j];
            }
        }

        for (int i = 0; i < classNum; i++) {
            for (int j = 0; j < classNum; j++) {
                System.out.print(cells[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {

//        String file = args[0];
        String file = "C:\\Data\\EntityExtractor_Data\\out2\\v2_files\\c100_ws-w-unk-rgx-edg-st-en_5_CATEGORY_Multi0_maxIter100_histSz1_v2_features.txt";
        buildConfusionMatrix(file);
        /*x MicroMacroAvg.1 */
        ConfusionMatrix cm = new ConfusionMatrix(cats, cells);

        double macroPrec = cm.macroAvgPrecision();
        double macroRec = cm.macroAvgRecall();
        double macroF = cm.macroAvgFMeasure();

        PrecisionRecallEvaluation prMicro = cm.microAverage();
        double microPrec = prMicro.precision();
        double microRec = prMicro.recall();
        double microF = prMicro.fMeasure();
        /*x*/

        /*x MicroMacroAvg.2 */
//        for (int i = 0; i < cats.length; ++i) {
//            PrecisionRecallEvaluation pr = cm.oneVsAll(i);
//            double prec = pr.precision();
//            double rec = pr.recall();
//            double f = pr.fMeasure();
//            /*x*/
//            System.out.printf("cat=%8s prec=%5.3f rec=%5.3f F=%5.3f\n",
//                    cats[i], prec, rec, f);
//        }
        System.out.println();

        System.out.println(file + " " + microPrec + " " + microRec + " " + microF);

//        System.out.printf("Macro prec=%5.3f rec=%5.3f F=%5.3f\n",
//                macroPrec, macroRec, macroF);
        System.out.printf("Micro prec=%5.3f rec=%5.3f F=%5.3f\n",
                microPrec, microRec, microF);
    }

}
