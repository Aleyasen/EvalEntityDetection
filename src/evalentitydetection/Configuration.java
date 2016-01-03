/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Aale
 */
public class Configuration {

    public static final String B_END = "0";
    public static final String B_INSIDE = "1";
    public static final String B_BEGIN = "2";
    public static final String B_UNIGRAM = "3";
    public static final String B_NO_ENTITY = "4";
    public static final String C_NO_ENTITY = "94";

    public static List<String> B_ALL_ENTITY_TOKENS = Arrays.asList(B_END, B_INSIDE, B_BEGIN, B_UNIGRAM);

    public static String labelB(String code) {
        if (code.equals(B_END)) {
            return "END";
        } else if (code.equals(B_INSIDE)) {
            return "INSIDE";
        } else if (code.equals(B_BEGIN)) {
            return "BEGIN";
        } else if (code.equals(B_UNIGRAM)) {
            return "UNIGRAM";
        } else if (code.equals(B_NO_ENTITY)) {
            return "NOENTB";
        }
        return null;
    }

    public static String labelC(String code) {
        if (code.equals(C_NO_ENTITY)) {
            return "NOENTC";
        } else {
            return "ENTITY";
        }
    }

}
