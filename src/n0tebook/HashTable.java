package n0tebook;
import static com.sun.jmx.snmp.ThreadContext.contains;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;



public class HashTable {
    private int hashSize = 1000;
    private String[] hashArray;
    public ArrayList<String> mispelledList = new ArrayList<String>();
    private int wordLength;
    private int hashValue;
    private char currentChar;
    private int currentInt;
    private String currentWord;
    private int arrayIndex;
    private static String dictionary;
    private static int wordTotal = 0;
    public static int size;
    private String testFile;
    private int wordCount = 0;
HashTable(){    
        
    this.hashArray = new String[1000];
    }
HashTable(int n){
       
    this.hashSize = n;
    this.hashArray = new String[this.hashSize];
}

void insert(String S){
    this.currentWord = S;
    this.wordLength = S.length();
    this.hashValue = getHashValue();
    this.arrayIndex = this.hashValue % this.hashSize;
    this.hashArray[arrayIndex] += (", " + this.currentWord);
  //  System.out.println(this.currentWord + " -- " + this.hashValue + " -- " + this.arrayIndex);
}
public int getAsciiValue(){
    currentInt = (int)this.currentChar;
    if(currentInt < 91 && currentInt > 64){ 
        return currentInt -= 64;
    }else if( currentInt < 123 && currentInt > 96){ 
        return currentInt -= 96;}
    else {
        return 0;
        }
}

   
public int getHashValue(){
    int n = 3;
    int currentValue = 0;
    for(int i = 0; i < this.wordLength; i++){
        this.currentChar = currentWord.charAt(i);
        currentInt = getAsciiValue();
        currentValue += currentInt * Math.pow(31, n);
        if(currentInt != 0){n--;}
        if(n == -1){ n = 3;}    
    }
    return currentValue;
}



public static void readDictionary() throws IOException{	
    dictionary = new String(Files.readAllBytes(Paths.get("Dictionary1.txt")));
    dictionary = dictionary.toLowerCase();
}  

public static void countWords(){
    int firstSpace = 0;
    if(dictionary.substring(0).equals(" ")){
        firstSpace = 1;
}
    int secondSpace = 0;
    String word;
    while(secondSpace < dictionary.length()){
        secondSpace = dictionary.indexOf(" ", firstSpace);
        if(secondSpace == -1){
            secondSpace = dictionary.length();
        }
        word = dictionary.substring(firstSpace, secondSpace);
        firstSpace = secondSpace + 1;
        wordTotal++;
    }
    
}  

public static void calculateHashSize(){
    size = wordTotal * 3;
    boolean sizeIsPrime = false;
    while(!sizeIsPrime){
        sizeIsPrime = isPrime(size);
        size++;
    }
    size--;
}

public static boolean isPrime(int n) {    
     if (n%2==0) 		
        return false;
     else
        for(int i=3; i*i<=n; i+=2) {
           if(n%i==0)
              return false;
        }
     return true;
}

public void insertDictionary(){
    int firstSpace = 0;
    if(dictionary.substring(0).equals(" ")){
        firstSpace = 1;
}
    int secondSpace = 0;
    String word;
    while(secondSpace < dictionary.length()){
        secondSpace = dictionary.indexOf(" ", firstSpace);
        if(secondSpace == -1){
            secondSpace = dictionary.length();
        }
        word = dictionary.substring(firstSpace, secondSpace);
        firstSpace = secondSpace + 1;
        insert(word);
        this.wordCount++;
    }
    
}

public void proofReadFile(String fileName) throws IOException{
   Scanner fileIn = new Scanner(new File(fileName));
  
    while(fileIn.hasNext()){
        this.currentWord = fileIn.next();
        
        String temp = new String();
        for(int i = 0; i < this.currentWord.length(); i++){
            if (Character.isLetter(this.currentWord.charAt(i))){
                temp += Character.toLowerCase(this.currentWord.charAt(i));
            } else if (this.currentWord.charAt(i) == '-' ||
                       this.currentWord.charAt(i) == '\'')
               temp += this.currentWord.charAt(i); 
            else 
                ; 
        }
        this.currentWord = temp;
        boolean isOnList = contains(this.currentWord);
        if(!isOnList&& !this.currentWord.equals("\n") && !this.currentWord.equals("")){
            this.mispelledList.add(this.currentWord);
        }
    }

    
}
public void proofReadFile(String allText, int ok) throws IOException{

   String temp = new String();
   boolean endWord = false;
    for(int z = 0; z < allText.length(); z++){
        while(!endWord){
            if (Character.isLetter(allText.charAt(z))){
                temp += Character.toLowerCase(allText.charAt(z));
            } else if (allText.charAt(z) == '-' ||
                       allText.charAt(z) == '\'')
               temp += allText.charAt(z); 
            else if (allText.charAt(z) == '\\'){
                z += 2;
                endWord = true;
            }else {
                endWord = true; 
            }
        }
        boolean isOnList = contains(temp);
        if(!isOnList){
            this.mispelledList.add(this.currentWord);
        }
        temp = new String();
        z++;
        }
     
        
    }
 

public int count(){
    return this.wordCount;
}
public boolean contains(String S){
    this.currentWord = S;
    this.wordLength = this.currentWord.length();
    this.hashValue = getHashValue();
    this.arrayIndex = this.hashValue % this.hashSize;
    if(this.hashArray[this.arrayIndex] == null){return false;
    }else if(this.hashArray[this.arrayIndex].contains(S)){return true;}
    return false;  
    }
}





