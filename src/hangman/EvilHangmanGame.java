
// This is the Code that I wrote in the exam
// I put it here because it is cleaner

package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame
{
  private SortedSet<Character> guessedLetters = new TreeSet<>();
  private HashMap<String, HashSet<String>> myMap = new HashMap<>();
  private HashSet<String> allWords = new HashSet<>();
  private int wordLength;

  // Constructor
  public EvilHangmanGame()
  {
    wordLength = 0;
  }

  // This is the function that starts the game by storing the appropriate dictionary words into the allWords HashSet
  @Override
  public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException
  {
    Scanner scanner = new Scanner(dictionary);

    // Checks if the dictionary is empty
    if (!scanner.hasNext())
    {
      throw new EmptyDictionaryException();
    }

    guessedLetters.clear();
    myMap.clear();
    this.wordLength = wordLength;

    String word;

    // Stores the appropriate words into allWords according to wordLength
    while(scanner.hasNext())
    {
      word = scanner.next();
      if (word.length() == wordLength)
      {
        allWords.add(word);
      }
    }

    // Checks if allWords is empty
    if (allWords.isEmpty())
    {
      throw new EmptyDictionaryException();
    }

  }

  // This is the function that does most of the work
  // It calculates and returns the best option for the EvilHangman
  @Override
  public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException
  {
    guess = Character.toLowerCase(guess);

    // Makes sure the guess hasn't already been made
    if (guessedLetters.contains(guess))
    {
      throw new GuessAlreadyMadeException();
    }

    guessedLetters.add(guess);

    // Gets all the words contained in allWords
    for (String word : allWords)
    {
      // This makes sure the word has the same size as wordLength
      // It then calculates the key and puts it into the map
      if (word.length() == wordLength)
      {
        String key = getSubsetKey(word,guess);
        calSubset(key,word);
      }
    }

    // This gets the best subset for the Evil Hangman
    allWords = getBestSubset(guess);

    myMap.clear();

    return allWords;
  }

  @Override
  public SortedSet<Character> getGuessedLetters() {
    return guessedLetters;
  }

  // This returns the key according to the guessed letter and the given word
  public String getSubsetKey(String word, char guess)
  {
    StringBuilder key = new StringBuilder();
    for (int i=0; i < word.length(); i++)
    {
      if (word.charAt(i) == guess)
      {
        key.append(guess);
      }
      else
      {
        key.append('-');
      }
    }
    return key.toString();
  }

  // This calculates and either creates a new subset or adds the word to an existing one in the map
  public void calSubset(String key, String word)
  {
    HashSet<String> set;
    if (myMap.containsKey(key))
    {
      set=myMap.get(key);
    }
    else
    {
      set=new HashSet<>();
    }
    set.add(word);
    myMap.put(key,set);
  }

  // This returns the best subset for the Evil Hangman according to the given letter
  public HashSet<String> getBestSubset(char guess)
  {
    HashSet<String> bestSubset = new HashSet<>();
    for (HashSet<String> set : myMap.values())
    {
      if (bestSubset.size() < set.size())
      {
        bestSubset = set;
      }
      else if (bestSubset.size() == set.size())
      {
        int count1 = 0;
        int count2 = 0;
        int rightMostIndex1 = 0;
        int rightMostIndex2 = 0;

        String word1 ="";
        String word2 ="";

        for (String w1 : bestSubset)
        {
          word1 = w1;
          break;
        }
        for (String w2 : set)
        {
          word2 = w2;
          break;
        }

        for (int i=0; i < wordLength; i++)
        {
          if (word1.charAt(i) == guess)
          {
            rightMostIndex1 = i;
            count1++;
          }
          if (word2.charAt(i) == guess)
          {
            rightMostIndex2 = i;
            count2++;
          }
        }

        if (count1 < count2)
        {
          continue;
        }
        if (count1 > count2)
        {
          bestSubset = set;
          continue;
        }

        if (rightMostIndex1 > rightMostIndex2)
        {
          continue;
        }
        if (rightMostIndex1 < rightMostIndex2)
        {
          bestSubset = set;
          continue;
        }

        int nextRightMostIndex1 = 0;
        int nextRightMostIndex2 = 0;

        for (int i=0; i < wordLength; i++)
        {
          if (word1.charAt(i) == guess && (i != rightMostIndex1))
          {
            nextRightMostIndex1 = i;
          }
          if (word2.charAt(i) == guess && (i != rightMostIndex2))
          {
            nextRightMostIndex2 = i;
          }
        }

        if (nextRightMostIndex1 > nextRightMostIndex2)
        {
          continue;
        }
        if (nextRightMostIndex1 < nextRightMostIndex2)
        {
          bestSubset = set;
        }
      }
    }
    return bestSubset;
  }

}


/*
package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.*;

public class EvilHangmanGame implements IEvilHangmanGame
{
  private SortedSet<Character> guessedLetters;
  private HashSet<String> allWords;

  private int wordLength;

  private HashMap<String,HashSet<String>> myMap;


  public EvilHangmanGame()
  {
    wordLength = 0;
    myMap = new HashMap<String, HashSet<String>>();
    allWords = new HashSet<>();
    guessedLetters = new TreeSet<Character>();
  }


  @Override
  public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException
  {
    guessedLetters.clear();
    allWords.clear();
    myMap.clear();
    this.wordLength = wordLength;

    Scanner scanner = new Scanner(dictionary);

    if (!scanner.hasNext())
    {
      EmptyDictionaryException error = new EmptyDictionaryException();
      throw error;
    }

    while(scanner.hasNext())
    {
      String word = scanner.next();
      if (word.length() == wordLength)
      {
        allWords.add(word);
      }
    }

    if (allWords.isEmpty())
    {
      EmptyDictionaryException error = new EmptyDictionaryException();
      throw error;
    }
  }

  @Override
  public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException
  {
    guess = Character.toLowerCase(guess);

    if (guessedLetters.contains(guess))
    {
      GuessAlreadyMadeException error = new GuessAlreadyMadeException();
      throw error;
    }

    guessedLetters.add(guess);

    for (String word : allWords)
    {
      String key = getSubsetKey(word,guess);
      calcSubsetKey(key,word);
    }

    allWords = getBestSubset(guess);

    myMap.clear();

    return allWords;
  }

  @Override
  public SortedSet<Character> getGuessedLetters()
  {
    return guessedLetters;
  }

  public HashSet<String> getBestSubset(char guessedLetter)
  {
    HashSet<String> greatestSet = new HashSet<String>();
    for (HashSet<String> set : myMap.values())
    {
      if (greatestSet.size() < set.size())
      {
        greatestSet = set;
      }
      else if (greatestSet.size() == set.size())
      {
        String set1 = "";
        for (String string : greatestSet)
        {
          set1 = string;
          break;
        }
        String set2 = "";
        for (String string : set)
        {
          set2 = string;
          break;
        }

        boolean containsChar1 = false;
        boolean containsChar2 = false;

        for (int i=0; i < wordLength; i++)
        {
          if (set1.charAt(i) == guessedLetter)
          {
            containsChar1 = true;
          }
          if (set2.charAt(i) == guessedLetter)
          {
            containsChar2 = true;
          }
        }

        if (!containsChar1)
        {
          continue;
        }
        else if (!containsChar2)
        {
          greatestSet = set;
          continue;
        }

        int count1 = 0;
        int count2 = 0;

        int rightIndex1 = 0;
        int rightIndex2 = 0;

        for (int i=0; i < wordLength; i++)
        {
          if (set1.charAt(i) == guessedLetter)
          {
            count1++;
            rightIndex1 = i;
          }
          if (set2.charAt(i) == guessedLetter)
          {
            count2++;
            rightIndex2 = i;
          }
        }

        if (count1 < count2)
        {
          continue;
        }
        else if (count1 > count2)
        {
          greatestSet = set;
          continue;
        }

        if (rightIndex1 > rightIndex2)
        {
          continue;
        }
        else if (rightIndex1 < rightIndex2)
        {
          greatestSet = set;
          continue;
        }

        int nextRightIndex1 = 0;
        int nextRightIndex2 = 0;

        for (int i=0; i < wordLength; i++)
        {
          if (set1.charAt(i) == guessedLetter && i != rightIndex1)
          {
            nextRightIndex1 = i;
          }
          if (set2.charAt(i) == guessedLetter && i != rightIndex2)
          {
            nextRightIndex2 = i;
          }
        }

        if (nextRightIndex1 > nextRightIndex2)
        {
          continue;
        }
        else if (nextRightIndex1 < nextRightIndex2)
        {
          greatestSet = set;
          continue;
        }
      }
    }
    return greatestSet;
  }

  public String getSubsetKey(String word, char guessedLetter)
  {
    char [] subsetKey = new char[wordLength];

    for (int i=0; i < wordLength; i++)
    {
      if (word.charAt(i) == guessedLetter)
      {
        subsetKey[i] = guessedLetter;
      }
      else
      {
        subsetKey[i] = '-';
      }
    }

    return Arrays.toString(subsetKey);
  }

  public void calcSubsetKey(String key, String word)
  {
    if (myMap.containsKey(key))
    {
      HashSet<String> setWord = myMap.get(key);
      setWord.add(word);
      myMap.put(key,setWord);
    }
    else
    {
      HashSet<String> setWord = new HashSet<String>();
      setWord.add(word);
      myMap.put(key,setWord);
    }
  }
}
*/

