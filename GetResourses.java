package ru.inno.unicwords;

import java.io.*;
import java.util.*;



/**
 * Created by mikhail on 17/12/16.
 */

/**+
 * На входе список файлов
 * Для каждого файла создаётся и запускается поток
 * В конструктор класса ThreadPull(Runnable) общие ресурс для потоков - множество слов
 * и file из которого будет прозводиться чтение
 *
 */
public class GetResourses {

    private static Thread thread;
    private static List<Thread> listOfThread = new LinkedList<>();
    public final static ThreadGroup GROUP = new ThreadGroup("Daemon demo");
    public static void main(String[] args) throws InterruptedException {

        HashSet<String> allWords = new HashSet<String>();

        int count = 0;

        Monitor monitor = new Monitor(allWords,true, " ");

        for (String i : args) {

            count++;
            File file = new File(i);
            ThreadPull thrdpull = new ThreadPull(file, monitor );
            thread = new Thread(GROUP, thrdpull, "Thread" + count );
            listOfThread.add(thread);
            thread.start();

        }

        try {

            for(Thread thread: listOfThread) thread.join();

        }

        catch (InterruptedException e) {

            e.printStackTrace();

        }

        int messageCount = 0;

        if (messageCount == 0){

            messageCount++;
            System.out.println(messageCount);
            System.out.println(monitor.getAllWords());
            System.out.println(monitor.getMessage() + " " + monitor.getAllWords().size());

        }

    }

}
