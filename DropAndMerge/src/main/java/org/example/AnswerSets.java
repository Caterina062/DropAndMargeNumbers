package org.example;

import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.parsers.asp.ASPDataCollection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AnswerSets extends Output implements ASPDataCollection {
    protected List<AnswerSet> answersets;

    public AnswerSets(String out) {
        super(out);
    }

    public AnswerSets(String out, String err) {
        super(out, err);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<AnswerSet> getAnswersets() {
        if (this.answersets == null) {
            this.answersets = new ArrayList();
            this.parse();
        }

        return Collections.unmodifiableList(this.answersets);
    }
    public List<AnswerSet> getOptimalAnswerSets() {
        int level = 0;
        List<AnswerSet> answerSets = this.getAnswersets();
        List<AnswerSet> optimalAnswerSets = new ArrayList();
        Iterator var4 = ((List)answerSets).iterator();

        while(var4.hasNext()) {
            AnswerSet answerSet = (AnswerSet)var4.next();
            int maxLevel = (Integer)Collections.max(answerSet.getWeights().keySet());
            if (level < maxLevel) {
                level = maxLevel;
            }
        }

        while(level >= 1) {
            int minimumCost = Integer.MAX_VALUE;
            Iterator var9 = ((List)answerSets).iterator();

            while(var9.hasNext()) {
                AnswerSet answerSet = (AnswerSet)var9.next();
                int cost = (Integer)answerSet.getWeights().getOrDefault(level, 0);
                if (cost < minimumCost) {
                    optimalAnswerSets.clear();
                    minimumCost = cost;
                }

                if (cost == minimumCost) {
                    optimalAnswerSets.add(answerSet);
                }
            }

            answerSets = new ArrayList(optimalAnswerSets);
            --level;
        }

        return optimalAnswerSets;
    }
}
