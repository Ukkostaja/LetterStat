package com.github.ukkostaja.letterstat;

import java.util.Scanner;

public class Reader {

    LetterRootReverse root;

    String s;

    public Reader(LetterRootReverse root) {
        this.root = root;
    }

    void startReading() {
        Scanner in = new Scanner(System.in);

        while((s= in.nextLine()) != ""){
            print(root.find(s));
        }
    }


    void print(long result){
        float percent = 100f * result / root.getCountTotal();
        System.out.println("Searching for: "+ s + " Found: "+result + " For a percent of " + percent);
    }

}
