package com.github.ukkostaja.letterstat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class LetterNodeReverse implements Letters {

    StringBuffer debugBuffer;
    Character letter;
    HashMap<Character, LetterNodeReverse> map = new HashMap<>(29,1f);
    boolean leafNode;
    String prePath;
    int position;

    Logger logger;
    StringBuilder loggerString;

    long countHere=0;
    long countUnder=0;

    LetterNodeReverse(Word fullWord, int position ) {
        logger = LoggerFactory.getLogger(this.getClass());
        loggerString = new StringBuilder();
        debugBuffer = new StringBuffer(fullWord.getWord());
        if(fullWord == null) {
            leafNode = true;
            logger.trace(loggerString.toString()+":FAILED");
            throw new LetterNodeException();
        }
        this.position = position;
        letter = Character.valueOf(fullWord.getWord().charAt(position));
        prePath = fullWord.getWord().substring(0,position);
        initHashMap(fullWord);
    }

    private void initHashMap(Word fullWord) {
        if(fullWord.getWord().length() <= position) {
            logger.trace(loggerString.toString()+":Leafed1");
            leafNode = true;
            countHere = fullWord.getCount();
            return;
        }
        leafNode = false;

        extend(fullWord);
    }

    private void extend(Word fullWord) {
        extend(fullWord,position);
    }

    void extend(Word fullWord, int position) {
        debugBuffer = new StringBuffer(fullWord.getWord());
        debugBuffer.reverse();
        int newPosition = position+1;
        loggerString.append(position+":");
        if(position >= fullWord.getWord().length()-1) {
            logger.trace(debugBuffer.toString()+":"+fullWord.getCount()+":Leafed2");
            countHere+=fullWord.getCount();
            return;
        }
        Character nextChar = Character.valueOf(fullWord.getWord().charAt(newPosition));
        LetterNodeReverse node = map.get(nextChar);
        countUnder+= fullWord.getCount();
        if (node == null) {
            map.put(nextChar, new LetterNodeReverse(fullWord,newPosition));
        } else {
            node.extend(fullWord,newPosition);
        }
    }

    public long verifyCount() {
        throw new UnsupportedOperationException("verifyCount not yet implemented");
    }

    boolean isLeafNode() {
        return leafNode;
    }

    boolean isRoot() {
        return letter == null;
    }

    String getPrePath() {
        return prePath;
    }

    String getUptoPath() {
        return prePath+letter;
    }

    public long getCountHere() {
        return countHere;
    }

    public long getCountUnder(){
        return countUnder;
    }

    public long getCountTotal(){
        return getCountUnder()+getCountHere();
    }

    @Override
    public long find(String word) {
        if(word.length() == getUptoPath().length()) {
            logger.trace("Search ended at: " + getUptoPath() + " with found of: "+getCountTotal());
            return getCountTotal();
        }
        //logger.trace(debugBuffer);
        int pos = prePath.length();
        Character ch= Character.valueOf(word.charAt(pos+1));
        LetterNodeReverse letterNodeReverse = map.get(ch);
        if (letterNodeReverse == null) {
            return 0;
        }
        return letterNodeReverse.find(word);
    }

    private void logLineEnd(String lastAdd){
        logger.trace(debugBuffer.toString()+":"+lastAdd);
    }
}
