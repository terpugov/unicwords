package ru.inno.unicwords;
import org.apache.log4j.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Created by mikhail on 17/12/16.
 */

/**+
 * Класс Monitor ипользуется для хранения общих ресуросв потоков.
 * Флаг завершения, множество уникальных слов и сообщение о завершении работы программы
 */
public class Monitor {

    private HashSet<String> allWords;
    private volatile boolean finishit;
    private String message;

    Monitor(HashSet<String> allWords,boolean finishit, String message){

        this.allWords = allWords;
        this.finishit = finishit;
        this.message = message;

    }

    public String getMessage(){
        return this.message;
    }


    public void setMessage(String message){

        this.message += "\n" + message;

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
