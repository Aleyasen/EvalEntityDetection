/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection;

/**
 *
 * @author Aale
 */
public class Word {

    public int index;
    public String text;
    public String label1; //real label
    public String label2; //predicted label

    public Word(int index, String text, String label1, String label2) {
        this.index = index;
        this.text = text;
        this.label1 = label1;
        this.label2 = label2;
    }

    @Override
    public String toString() {
        return "Word{" + "index=" + index + ", text=" + text + ", label1=" + label1 + ", label2=" + label2 + '}';
    }

}
