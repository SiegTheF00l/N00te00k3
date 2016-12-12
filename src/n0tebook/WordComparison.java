
package n0tebook;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class WordComparison {

    public static int distance(String string1, String string2) {
    string1 = string1.toLowerCase();
    string2 = string2.toLowerCase();

    int[] tempInt = new int[string2.length() + 1];
    for (int i = 0; i <= string1.length(); i++) {
      int lValue = i;
      for (int j = 0; j <= string2.length(); j++) {
        if (i == 0)
          tempInt[j] = j;
        else {
          if (j > 0) {
            int newVal = tempInt[j - 1];
            if (string1.charAt(i - 1) != string2.charAt(j - 1))
              newVal = Math.min(Math.min(newVal, lValue),
                  tempInt[j]) + 1;
            tempInt[j - 1] = lValue;
            lValue = newVal;
          }
        }
      }
      if (i > 0)
        tempInt[string2.length()] = lValue;
    }
    return tempInt[string2.length()];
  }
    
    public static double similarity(String string1, String string2) {
        String l = string1;
        String s = string2;
        if (string1.length() < string2.length()) {
            l = string2;
            s = string1;
        }
        int lLength = l.length();
        if (lLength == 0) {
            return 1.0;
             }
        return (lLength - distance(l, s)) / (double) lLength;
    }
    
    public static ObservableList<String> findSuggestion(String currentWord){
        ObservableList<String> suggestedWords = FXCollections
        .observableArrayList();
        
        try {
             Scanner fileIn = new Scanner(new File("Dictionary1.txt"));
  
    String dicWord;
    
    double sim;
    while(fileIn.hasNext()){
        dicWord = fileIn.next();
        
        if (dicWord.length() < currentWord.length()){
            sim = similarity(currentWord, dicWord);
        } else
            sim = similarity(dicWord, currentWord);
        
        if (sim >= .6)
            suggestedWords.add(dicWord);
    }
        }catch (Exception e){}
        return suggestedWords;
    }
}
