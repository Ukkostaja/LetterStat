package com.github.ukkostaja.letterstat;

import java.util.Scanner;
import java.util.logging.Logger;

public class Reader {

    Letters root;

    String s;

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public Reader(Letters root) {
        this.root = root;

    }

    void startReading() {
        Scanner in = new Scanner(System.in);

        while((s= in.nextLine()) != ""){
            logger.finest(s);
            print(root.find(s));
        }
    }


    void print(long result){
        float percent = 100f * result / root.getCountTotal();
        String line = "Searching for: "+ s + " Found: "+result + " For a percent of " + percent;
        logger.finer(line);
        System.out.println(line);
    }

}
