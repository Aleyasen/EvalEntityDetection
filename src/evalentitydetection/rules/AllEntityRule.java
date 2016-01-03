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
public class AllEntityRule extends Rule {

    @Override
    public boolean violate(List<Word> input) {
        for (Word type : input) {
            if (type.label2.equals(Configuration.C_NO_ENTITY)) {
                return true;
            }
        }
        return false;
    }

}
