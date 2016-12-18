package ru.inno.unicwords;


import java.util.HashSet;
import java.util.Set;


/**
 * Created by mikhail on 17/12/16.
 */
public class Monitor {
    private volatile boolean finishit;
    private HashSet<String> allWords;
    private String message;

    Monitor(HashSet<String> allWords,boolean finishit, String message){
        this.allWords = allWords;
        this.finishit = finishit;
        this.message = message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return this.message;
    }

    public HashSet<String> getAllWords() {

        return allWords;

    }

    public void setAllWords(HashSet<String> allWords){
        this.allWords = allWords;
    }

    public boolean getFinishit(){
        return finishit;
    }

    public void setFinishit(boolean finishit){

        this.finishit = finishit;

    }
}
