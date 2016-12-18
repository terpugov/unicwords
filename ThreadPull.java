package ru.inno.unicwords;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mikhail on 17/12/16.
 */
public class ThreadPull implements Runnable {
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

                while ((c = br.read()) != -1 || word.size() > 0) {


                    if (checker.checkSpace((char) c)) {
                        if (word.size() > 0) {

                            StringBuilder strB = new StringBuilder();

                            for (char s : word) {
                                strB.append(s);

                            }

/*                            synchronized (allWords){
                                finishit = allWords.add(strB.toString());
                                if (!finishit){
                                    monitor.setAllWords(allWords);
                                    monitor.setFinishit(finishit);

                                    monitor.setMessage("Duplicate have been founded");
                                    return;
                                }
                            }*/
                            try {
                                if (lock.tryLock()) {
                                    finishit = allWords.add(strB.toString());
                                    if (!finishit) {
                                        monitor.setAllWords(allWords);
                                        monitor.setFinishit(finishit);

                                        monitor.setMessage("Duplicate have been founded");
                                        return;
                                    }
                                }
                            } finally {
                                lock.unlock();
                            }

                            word.clear();

                        }
                    } else if (checker.checkLetter((char) c)) {

                        word.add((char) c);
                    } else {

                        StringBuilder strB = new StringBuilder();
                        for (char s : word) {
                            strB.append(s);
                        }

/*                        synchronized (allWords){

                            finishit = allWords.add(strB.toString());
                            if (!finishit){
                                monitor.setAllWords(allWords);
                                monitor.setFinishit(finishit);
                                monitor.setMessage("Duplicate have been founded");
                                return;

                            }

                        }*/
                        try {
                            if (lock.tryLock()) {
                                finishit = allWords.add(strB.toString());
                                if (!finishit) {
                                    monitor.setAllWords(allWords);
                                    monitor.setFinishit(finishit);

                                    monitor.setMessage("Duplicate have been founded");
                                    return;
                                }
                            }
                        } finally {
                            lock.unlock();
                        }
                        word.clear();
                        //                        System.out.println(word);
                    }

                }

            } catch (IOException ex) {
                ex.printStackTrace();
//                System.out.println(ex.getMessage());
            }
//            System.out.println("allWords to String = " + allWords.toString() + monitor + Thread.currentThread().getName());
            finishit = false;
            monitor.setMessage("File have been ended " + Thread.currentThread().getName());
        }
//        System.out.println(allWords.toString());

    }
}