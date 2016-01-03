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
public class AllSameEntityRule extends Rule {

    @Override
    public boolean violate(List<Word> input) {
        if (input.size() > 0) {
            Word first = input.get(0);
            for (Word type : input) {
                if (!type.label2.equals(first) && type.label2.equals(Configuration.C_NO_ENTITY)) {
                    return true;
                }
            }
        }
        return false;
    }

}
