/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection;

import evalentitydetection.rules.AllEntityRule;
import evalentitydetection.rules.AllSameEntityRule;
import evalentitydetection.rules.ValidBeginInsideEndBoundaryRule;
import evalentitydetection.rules.Rule;
import evalentitydetection.rules.ValidUnigramBoundaryRule;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.PrintConversionEvent;

/**
 *
 * @author Aale
 */
public class CombiningClassBoundary {

    public List<Word> readFile(String filepath) {
        List<Word> words = new ArrayList<Word>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String line = br.readLine();

            while (line != null) {
                String[] split = line.split("\\t");
                if (split.length == 4) {
                    Word w = new Word(Integer.parseInt(split[0]), split[1], split[2], split[3]);
                    words.add(w);
                } else {
                    System.out.println("Error to parsing line=" + line);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(words.size() + " read from " + filepath);
        return words;
    }

    public void processCombination(String boundaryFile, String classFile) {
        final List<Word> bwords = readFile(boundaryFile);
        final List<Word> cwords = readFile(classFile);

//        List<Word> currentEntityC = new ArrayList<Word>();
//        List<Word> currentEntityB = new ArrayList<Word>();
        BoundaryFSM bfsm = new BoundaryFSM(bwords, cwords);
        bfsm.run();
        List<List<Word>> entitiesB = bfsm.getEntitiesB();
        List<List<Word>> entitiesC = bfsm.getEntitiesC();
        final List<List<Word>> noEntities = bfsm.getNoEntities();
//        for(List<Word> words: noEntities){
//            printEntity(words);
//        }
        /*   for (int i = 0; i < cwords.size(); i++) {
         if (!cwords.get(i).label1.equals(Configuration.C_NO_ENTITY)) {
         if (i == 0) {
         currentEntityC.add(cwords.get(i));
         currentEntityB.add(bwords.get(i));
         } else if (i > 0) {
         if (cwords.get(i).label1.equals(cwords.get(i - 1).label1) || (!cwords.get(i).label1.equals(cwords.get(i - 1).label1)) && cwords.get(i - 1).label1.equals(Configuration.C_NO_ENTITY)) {
         currentEntityC.add(cwords.get(i));
         currentEntityB.add(bwords.get(i));
         } else {
         if (currentEntityC.isEmpty()) {
         continue;
         }
         entitiesC.add(currentEntityC);
         entitiesB.add(currentEntityB);
         currentEntityC = new ArrayList<Word>();
         currentEntityB = new ArrayList<Word>();
         currentEntityC.add(cwords.get(i));
         currentEntityB.add(bwords.get(i));
         }
         }

         } else {
         if (currentEntityC.isEmpty()) {
         continue;
         }
         entitiesC.add(currentEntityC);
         entitiesB.add(currentEntityB);
         currentEntityC = new ArrayList<Word>();
         currentEntityB = new ArrayList<Word>();
         }
         }
         */
//        print(entitiesC, entitiesB);
        checkInsconsistence(entitiesC, entitiesB, noEntities);
    }

    private void print(List<List<Word>> entitiesC, List<List<Word>> entitiesB) {
        for (int i = 0; i < entitiesC.size(); i++) {
            printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
        }
    }

    private void printBoundaryAndClass(List<Word> entityC, List<Word> entityB) {
        System.out.println("Class:");
        printEntity(entityC);
        System.out.println("Boundary:");
        printEntity(entityB);
        System.out.println();
    }

    public static void printEntity(List<Word> entity) {
        for (Word word : entity) {
            System.out.print(word.index + "\t");
        }
        System.out.println();
        for (Word word : entity) {
            System.out.print(word.text + "\t");
        }
        System.out.println();
        for (Word word : entity) {
            System.out.print(word.label2 + "(" + word.label1 + ")" + "\t");
        }
        System.out.println();
    }

    private void checkInsconsistence(List<List<Word>> entitiesC, List<List<Word>> entitiesB, List<List<Word>> noEntities) {
        Rule beginInsideEndRule = new ValidBeginInsideEndBoundaryRule();
        Rule unigramBoundaryRule = new ValidUnigramBoundaryRule();
        Rule allEntityRule = new AllEntityRule();
        Rule allSameEntityRule = new AllSameEntityRule();
        int multiBVCount = 0;
        int singleBVCount = 0;
        int singleAllEntityRuleCount = 0;
        int multiAllEntityRuleCount = 0;
        int singleAllSameEntityRuleCount = 0;
        int multiAllSameEntityRuleCount = 0;
        int multiBFineAndCVCount = 0;
        int multiBVAndCFCount = 0;
        int singleBVAndCFCount = 0;
        int singleBFAndCVCount = 0;
        int singleCount = 0;
        int multiCount = 0;

        for (int i = 0; i < entitiesC.size(); i++) {
            if (entitiesC.get(i).size() > 1) {
                multiCount++;
                if (allEntityRule.violate(entitiesC.get(i))) {
                    multiAllEntityRuleCount++;
                    //printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
                if (allSameEntityRule.violate(entitiesC.get(i))) {
                    multiAllSameEntityRuleCount++;
                }
                if (beginInsideEndRule.violate(entitiesB.get(i))) {
                    multiBVCount++;
//                printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
                if (!allEntityRule.violate(entitiesC.get(i)) && beginInsideEndRule.violate(entitiesB.get(i))) {
                    multiBVAndCFCount++;
//                printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
                if (allEntityRule.violate(entitiesC.get(i)) && !beginInsideEndRule.violate(entitiesB.get(i))) {
                    multiBFineAndCVCount++;
//                printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
            } else {
                singleCount++;
                if (allEntityRule.violate(entitiesC.get(i))) {
                    singleAllEntityRuleCount++;
                    //printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
                if (allSameEntityRule.violate(entitiesC.get(i))) {
                    singleAllSameEntityRuleCount++;
                }
                if (unigramBoundaryRule.violate(entitiesB.get(i))) {
                    singleBVCount++;
//                    printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
                if (!allEntityRule.violate(entitiesC.get(i)) && unigramBoundaryRule.violate(entitiesB.get(i))) {
                    singleBVAndCFCount++;
//                    printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
                if (allEntityRule.violate(entitiesC.get(i)) && !unigramBoundaryRule.violate(entitiesB.get(i))) {
                    singleBFAndCVCount++;
//                    printBoundaryAndClass(entitiesC.get(i), entitiesB.get(i));
                }
            }
        }
        System.out.println("singleAllEntityRuleCount\t" + singleAllEntityRuleCount);
        System.out.println("multiAllEntityRuleCount\t" + multiAllEntityRuleCount);

        System.out.println("singleAllSameEntityRuleCount\t" + singleAllSameEntityRuleCount);
        System.out.println("multiAllSameEntityRuleCount\t" + multiAllSameEntityRuleCount);

        System.out.println("SingleBoundaryViolationCount=\t" + singleBVCount);
        System.out.println("MultiBoundaryViolationCount\t" + multiBVCount);

        System.out.println("SingleBoundaryViolationAndClassFineCount=\t" + singleBVAndCFCount);
        System.out.println("MultiBoundaryViolationAndClassFineCount=\t" + multiBVAndCFCount);

        System.out.println("SingleBoundaryFineAndClassViolationCount=\t" + singleBFAndCVCount);
        System.out.println("MultiBoundaryFineAndClassViolationCount\t" + multiBFineAndCVCount);

        System.out.println("All Single Entities\t" + singleCount);
        System.out.println("All Multi Entities\t" + multiCount);
        System.out.println("All Entities\t" + entitiesC.size());

    }

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            String fileIndex = i + "";
            String boundaryFilePath = String.format("C:\\Data\\SODA\\output\\boundary\\b500_ws-w-rgx-unk-edg-st-en_%s_BOUNDARY_Multi0_maxIter500_histSz1_v2_features.txt", fileIndex);
            String classFilePath = String.format("C:\\Data\\SODA\\output\\category\\c150_ws-w-rgx-unk-st-en_%s_CATEGORY_Multi0_maxIter150_histSz1_v2_features.txt", fileIndex);
            System.out.println(boundaryFilePath);
            System.out.println(classFilePath);
            CombiningClassBoundary comb = new CombiningClassBoundary();
//        final List<Word> bwords = comb.readFile(boundaryFilePath);
//            comb.processCombination(boundaryFilePath, classFilePath);
//            comb.wordBasedAnalysis(boundaryFilePath, classFilePath);
            comb.wordBasedAnalysis3(boundaryFilePath, classFilePath);
        }
    }

    public void wordBasedAnalysis(String boundaryFile, String classFile) {
        final List<Word> bwords = readFile(boundaryFile);
        final List<Word> cwords = readFile(classFile);
        int countBoth = 0;
        int countOR = 0;
        int countBandWrongClass = 0;
        int countBandNullClass = 0;
        int countBandExtraClass = 0;
        int countb = 0;
        int countc = 0;
        for (int i = 0; i < bwords.size(); i++) {
            if (!bwords.get(i).label1.equals(bwords.get(i).label2) || !cwords.get(i).label1.equals(cwords.get(i).label2)) {
                countOR++;
//                System.out.println(bwords.get(i).label2 + "," + isEntity(cwords.get(i).label2) + "," + bwords.get(i).label1);
//                System.out.println(bwords.get(i).label2 + "," + isEntity(cwords.get(i).label2) + "," + isEntity(cwords.get(i).label1));
            }

            if (!cwords.get(i).label1.equals(cwords.get(i).label2)) {
                if (cwords.get(i).label1.equals(Configuration.C_NO_ENTITY) && !cwords.get(i).label2.equals(Configuration.C_NO_ENTITY)) { //khodesh kelass nadashte man goftam ye kelasi
                    countBandExtraClass++;
                }
                if (cwords.get(i).label2.equals(Configuration.C_NO_ENTITY) && !cwords.get(i).label1.equals(Configuration.C_NO_ENTITY)) { //khodesh kelass dashte man goftam nadare
                    countBandNullClass++;
                }
                if (!cwords.get(i).label1.equals(Configuration.C_NO_ENTITY) && !cwords.get(i).label2.equals(Configuration.C_NO_ENTITY)) { //goftam class vali suti dadam
                    countBandWrongClass++;
                }
            }

            if (!bwords.get(i).label1.equals(bwords.get(i).label2) && !cwords.get(i).label1.equals(cwords.get(i).label2)) {
                countBoth++;
                // System.out.println("");
            }
            if (!bwords.get(i).label1.equals(bwords.get(i).label2)) {
                countb++;
                // System.out.println("");
            }
            if (!cwords.get(i).label1.equals(cwords.get(i).label2)) {
                countc++;
                // System.out.println("");
            }
        }

        System.out.println("countb" + "\t" + countb);

        System.out.println("countc" + "\t" + countc);

        System.out.println("countOR" + "\t" + countOR);
        System.out.println("countBoth" + "\t" + countBoth);
        System.out.println("countBandExtraClass" + "\t" + countBandExtraClass);
        System.out.println("countBandNullClass" + "\t" + countBandNullClass);
        System.out.println("countBandWrongClass" + "\t" + countBandWrongClass);
        System.out.println("All" + "\t" + cwords.size());
    }

    public void wordBasedAnalysis2(String boundaryFile, String classFile) {
        final List<Word> bwords = readFile(boundaryFile);
        final List<Word> cwords = readFile(classFile);
        int no_error = 0;
        int error_boundary = 0;
        int error_class = 0;
        int error_both = 0;
        for (int i = 0; i < bwords.size(); i++) {
            String predBoundary = bwords.get(i).label2;
            String predClass = cwords.get(i).label2;
            String realBoundary = bwords.get(i).label1;
            String realClass = cwords.get(i).label1;
            if (!predClass.equals(Configuration.C_NO_ENTITY)) { // the word is entity
                if (predBoundary.equals(Configuration.B_NO_ENTITY)) {
                    if (realBoundary.equals(predBoundary) && realClass.equals(predClass)) {
                        no_error++;
                    } else if (realBoundary.equals(predBoundary) && (!realClass.equals(predClass))) {
                        error_class++;
                    } else if ((!realBoundary.equals(predBoundary)) && realClass.equals(predClass)) {
                        error_boundary++;
                    } else if ((!realBoundary.equals(predBoundary)) && (!realClass.equals(predClass))) {
                        error_both++;
                    }
                }
            } else if (predClass.equals(Configuration.C_NO_ENTITY)) {
                if (Configuration.B_ALL_ENTITY_TOKENS.contains(predBoundary)) {
                    if (realBoundary.equals(predBoundary) && realClass.equals(predClass)) {
                        no_error++;
                    } else if (realBoundary.equals(predBoundary) && (!realClass.equals(predClass))) {
                        error_class++;
                    } else if ((!realBoundary.equals(predBoundary)) && realClass.equals(predClass)) {
                        error_boundary++;
                    } else if ((!realBoundary.equals(predBoundary)) && (!realClass.equals(predClass))) {
                        error_both++;
                    }
                }
            }
        }
        System.out.println("All=\t" + bwords.size());
        System.out.println("no_error=\t" + no_error);
        System.out.println("error_class=\t" + error_class);
        System.out.println("error_boundary=\t" + error_boundary);
        System.out.println("error_both=\t" + error_both);

    }

    public void wordBasedAnalysis3(String boundaryFile, String classFile) {
        final List<Word> bwords = readFile(boundaryFile);
        final List<Word> cwords = readFile(classFile);
        int no_error = 0;
        int error_boundary = 0;
        int error_class = 0;
        int error_both = 0;
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> rows = new HashMap<>();
        Map<String, Integer> cols = new HashMap<>();
        for (int i = 0; i < bwords.size(); i++) {
            String predBoundary = bwords.get(i).label2;
            String predClass = cwords.get(i).label2;
            String realBoundary = bwords.get(i).label1;
            String realClass = cwords.get(i).label1;
            String row = Configuration.labelB(predBoundary) + "-" + Configuration.labelC(predClass);
            String col = Configuration.labelB(realBoundary) + "-" + Configuration.labelC(realClass);
            String key = row + "\t" + col;
            incInMap(key, map);
            addToMap(row, rows);
            addToMap(col, cols);

        }
        System.out.println("All=\t" + bwords.size());
        for (String key : map.keySet()) {
            System.out.println(key + "\t" + map.get(key));
        }

        System.out.println();
        System.out.print("\t");
        for (String col : cols.keySet()) {
            System.out.print(col + "\t");
        }
        System.out.println();
        for (String row : rows.keySet()) {
            System.out.print(row + "\t");
            for (String col : cols.keySet()) {
                System.out.print(map.get(row + "\t" + col) + "\t");
            }
            System.out.println();
        }
//        System.out.println(map);
//        System.out.println("no_error=\t" + no_error);
//        System.out.println("error_class=\t" + error_class);
//        System.out.println("error_boundary=\t" + error_boundary);
//        System.out.println("error_both=\t" + error_both);

    }

    private String isEntity(String label) {
        if (label.equals(Configuration.C_NO_ENTITY)) {
            return "0";
        }
        return "1";
    }

    private void incInMap(String key, Map<String, Integer> map) {
        final Integer val = map.get(key);
        if (val == null) {
            map.put(key, 1);
        } else {
            map.put(key, val + 1);
        }
    }

    private void addToMap(String id, Map<String, Integer> map) {
        final Integer val = map.get(id);
        if (val == null) {
            int size = map.size();
            map.put(id, size + 1);
        }
    }
}
