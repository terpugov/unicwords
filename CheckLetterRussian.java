package ru.inno.unicwords;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mikhail on 17/12/16.
 */
public class CheckLetterRussian implements TextCheckable{
    /**+
     * checkLetter get char and return true if chr is russian letter or punctuation mark
     * @param chr
     * @return
     */
    public boolean checkLetter(char chr){

        Pattern p = Pattern.compile("^[а-яА-яёЁ0-9]$");
        Matcher m = p.matcher(Character.toString(chr));
        if (m.matches()) return true;
        else return false;

    }

    public boolean checkSpace(char chr){
        Pattern p = Pattern.compile("[ \\f\\n\\r\\t\\v]*");
        Matcher m = p.matcher(Character.toString(chr));
        if (m.matches()) return true;
        else return false;
    }
}
