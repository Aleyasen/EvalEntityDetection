/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evalentitydetection;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aale
 */
public class BoundaryFSM {

    List<Word> bwords = new ArrayList<Word>();
    List<Word> cwords = new ArrayList<Word>();
    int current;
    List<Word> currentEntityB;
    List<Word> currentEntityC;
    List<Word> currentNoEntity;

    List<List<Word>> entitiesB;
    List<List<Word>> entitiesC;
    List<List<Word>> noEntities;

    public List<List<Word>> getEntitiesB() {
        return entitiesB;
    }

    public List<List<Word>> getEntitiesC() {
        return entitiesC;
    }

    public List<List<Word>> getNoEntities() {
        return noEntities;
    }

    public BoundaryFSM(List<Word> bwords, List<Word> cwords) {
        this.bwords = bwords;
        this.cwords = cwords;
        current = 0;
        currentEntityB = new ArrayList<Word>();
        entitiesB = new ArrayList<List<Word>>();
        currentEntityC = new ArrayList<Word>();
        entitiesC = new ArrayList<List<Word>>();
        currentNoEntity = new ArrayList<Word>();
        noEntities = new ArrayList<List<Word>>();
    }

    public void run() {
        while (true) {
            if (current > bwords.size() - 1) {
                break;
            }
            switch (bwords.get(current).label1) {
                case Configuration.B_BEGIN:
                    goBegin();
                    break;
                case Configuration.B_END:
                    goEnd();
                    break;
                case Configuration.B_INSIDE:
                    goInside();
                    break;
                case Configuration.B_NO_ENTITY:
                    goNoEntity();
                    break;
                case Configuration.B_UNIGRAM:
                    goUniGram();
                    break;
                default:
                    goInvalid();
                    break;
            }
            next();
        }
    }

    public void next() {
        current++;
    }

    public void add() {
        entitiesB.add(currentEntityB);
        entitiesC.add(currentEntityC);
//        CombiningClassBoundary.printEntity(currentEntity);
        currentEntityB = new ArrayList<Word>();
        currentEntityC = new ArrayList<Word>();
    }

    public void addNoEntity() {
        if (currentNoEntity.size() > 0) {
            noEntities.add(currentNoEntity);
            currentNoEntity = new ArrayList<Word>();
        }
    }

    public void append() {
        currentEntityB.add(bwords.get(current));
        currentEntityC.add(cwords.get(current));
    }

    public void appendNoEntity() {
        currentNoEntity.add(cwords.get(current));
    }

    private void goBegin() {
        addNoEntity();
        append();
    }

    private void goInvalid() {
        System.out.println("Error @ " + bwords.get(current) + " " + cwords.get(current));
    }

    private void goNoEntity() {
        appendNoEntity();
    }

    private void goUniGram() {
        addNoEntity();
        append();
        add();

    }

    private void goEnd() {
        append();
        add();
    }

    private void goInside() {
        append();
    }

}
