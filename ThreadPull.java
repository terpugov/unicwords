package ru.inno.unicwords;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mikhail on 17/12/16.
 */
public class ThreadPull implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPull.class);

    private ReentrantLock lock = new ReentrantLock();
    private File file;
    private int c = 0;
    private String[] words;
    private List<Character> word = new ArrayList<Character>();
    private HashSet<String> allWords;
    private Monitor monitor;
    private TextCheckable checker = new CheckLetterRussian();

    ThreadPull(File file, Monitor monitor) {

        this.file = file;
        this.monitor = monitor;

    }

    public void run() {

        allWords = monitor.getAllWords();
        boolean finishit = monitor.getFinishit();
//        System.out.println(Thread.currentThread().getId() + " " + monitor);


        while (finishit) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                // чтение посимвольно

                while ((c = br.read()) != -1 || word.size() > 0)   {

                    if (checker.checkSpace((char) c)) {


                        if (word.size() > 0) {
//                            logger.debug("word.size() =  {}", word.size());
                            StringBuilder strB = new StringBuilder();

                            for (char s : word) {
                                strB.append(s);

                            }

                            try {
                                if (lock.tryLock()) {
                                    finishit = allWords.add(strB.toString());
                                    if (!finishit) {
                                        monitor.setAllWords(allWords);
                                        monitor.setFinishit(finishit);

                                        monitor.setMessage("Duplicate have been founded1");
                                        return;
                                    }
                                }
                            }
                            finally {
                                lock.unlock();
                            }

                            word.clear();

                        }
                        else{word.clear();}
                    }
                    else if (c == -1){

                        StringBuilder strB = new StringBuilder();
                        for (char s : word) {
                            strB.append(s);
                        }

                        try {
                            if (lock.tryLock()) {
                                finishit = allWords.add(strB.toString());
                                if (!finishit) {
                                    monitor.setAllWords(allWords);
                                    monitor.setFinishit(finishit);

                                    monitor.setMessage("Duplicate have been founded2");
                                    return;
                                }
                            }
                        } finally {
                            lock.unlock();
                        }
                        word.clear();
                        //                        System.out.println(word);
                    }


                    else{
                        if (checker.checkLetter((char) c)) {

                            word.add((char) c);

                        }
                        else{
                            try {
                                if (lock.tryLock()) {


                                        monitor.setFinishit(finishit);

                                        monitor.setMessage("Wrong text in file");
                                    System.out.println((char)c +" " +  c + "ight here");
                                        return;
                                    }
                                }
                            finally {
                                lock.unlock();
                            }

                        }

                    }



                }

            } catch (IOException ex) {
                ex.printStackTrace();
//                System.out.println(ex.getMessage());
            }
//            System.out.println("allWords to String = " + allWords.toString() + monitor + Thread.currentThread().getName());
            finishit = false;
            monitor.setMessage("File have been ended " + Thread.currentThread().getName());

//            logger.debug("File have been ended");
        }
//        System.out.println(allWords.toString());

    }
}