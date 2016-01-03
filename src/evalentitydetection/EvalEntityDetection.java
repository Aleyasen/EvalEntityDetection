/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection;

import com.aliasi.classify.ConfusionMatrix;
import com.aliasi.classify.PrecisionRecallEvaluation;
import java.io.BufferedReader;
import java.io.File;
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

    public static void buildConfusionMatrix(String file, String output) {

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
        cats = new String[classNum - 1];
        for (int i = 0; i < classNum - 1; i++) {
            cats[i] = "c" + i;
        }

        cells = new int[classNum - 1][classNum - 1];
        for (int i = 0; i < classNum - 1; i++) {
            for (int j = 0; j < classNum - 1; j++) {
                cells[i][j] = tempCells[i][j];
            }
        }

        String out_str = "";
        for (int i = 0; i < classNum - 1; i++) {
            out_str += cats[i] + ":\t";
            System.out.print(cats[i] + ":\t");
            for (int j = 0; j < classNum - 1; j++) {
                out_str += cells[i][j] + "\t";
                System.out.print(cells[i][j] + "\t");
            }
            out_str += "\n";
            System.out.println();
        }
        IOUtils.writeDataIntoFile(out_str, output, false);

    }

    public static void main(String[] args) {

        String file = args[0];
//        String file = "C:\\Data\\SODA\\entityextractor\\target_AllFeatureExp\\b300_ws-w-rgx-unk-edg-st-en_1_BOUNDARY_Multi0_maxIter300_histSz1_v2_features.txt";
//        String file = "C:\\Data\\SODA\\entityextractor\\target_AllFeatureExp\\c300_ws-w-rgx-unk-edg-st-en_1_CATEGORY_Multi0_maxIter300_histSz1_v2_features.txt";
        File fileobj = new File(file);
        String dir = fileobj.getParent();
        String infile = fileobj.getName();
        String outfile = fileobj.getName() + ".cmat";
        System.out.println("output_file=" + outfile);
        String output_path = dir + "/" + outfile;
        String all_outputs = dir + "/all.out";
        System.out.println("output_dir=" + output_path);
//        String file = "C:\\Data\\SODA\\output\\boundary\\all_boundary_v2_features.txt";
        buildConfusionMatrix(file, output_path);
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
        String prf = file + "\t" + infile + "\t" + microPrec + "\t" + microRec + "\t" + microF + "\t" + macroPrec + "\t" + macroRec + "\t" + macroF + "\n";
        IOUtils.writeDataIntoFile("\n" + prf + "\n\n", output_path);
        System.out.println(prf);
        IOUtils.writeDataIntoFile(prf, all_outputs, true);

        String prf2_macro = String.format("Macro prec=%5.6f rec=%5.6f F=%5.6f\n",
                macroPrec, macroRec, macroF);
        String prf2_micro = String.format("Micro prec=%5.6f rec=%5.6f F=%5.6f\n",
                microPrec, microRec, microF);
        System.out.println(prf2_micro);
        IOUtils.writeDataIntoFile(prf2_micro + "\n", output_path);
        System.out.println(prf2_macro);
        IOUtils.writeDataIntoFile(prf2_macro + "\n", output_path);

    }

}
