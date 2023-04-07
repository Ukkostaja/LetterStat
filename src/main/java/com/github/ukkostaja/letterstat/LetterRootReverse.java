package com.github.ukkostaja.letterstat;

import java.util.HashMap;

public class LetterRootReverse implements Letters{

    HashMap<Character, LetterNodeReverse> map = new HashMap<>(29,1f);


    public void add(Word fullWord) {
        int position = 0;
        Character nextChar = Character.valueOf(fullWord.getWord().charAt(position));
        LetterNodeReverse node = map.get(nextChar);
        if (node == null) {
            map.put(nextChar, new LetterNodeReverse(fullWord,position));
        } else {
            node.extend(fullWord,position);
        }
    }
    @Override
    public long find(String word) {
        StringBuffer sb = new StringBuffer(word);
        sb.reverse();
        LetterNodeReverse firstNode = map.get(Character.valueOf(sb.charAt(0)));
        if(firstNode == null) {
            return 0;
        }
        return firstNode.find(sb.toString());

    }

    @Override
    public long getCountTotal() {
        long total = 0;
        for(Letters letter : map.values()) {
            total += letter.getCountTotal();
        }
        return total;
    }
}
