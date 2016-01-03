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
public class ValidUnigramBoundaryRule extends Rule {

    @Override
    public boolean violate(List<Word> input) {
        if (input.size() == 1) {
            if (!input.get(0).label2.equals(Configuration.B_UNIGRAM)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}
