/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection.rules;

import evalentitydetection.Configuration;
import evalentitydetection.Word;
import java.util.List;

/**
 *
 * @author Aale
 */
public class ValidBeginInsideEndBoundaryRule extends Rule {

    @Override
    public boolean violate(List<Word> input) {
        if (input.size() > 1) {
            if (!input.get(0).label2.equals(Configuration.B_BEGIN)) {
                return true;
            }
            if (!input.get(input.size() - 1).label2.equals(Configuration.B_END)) {
                return true;
            }
            for (int i = 1; i < input.size() - 1; i++) {
                if (!input.get(i).label2.equals(Configuration.B_INSIDE)) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

}
