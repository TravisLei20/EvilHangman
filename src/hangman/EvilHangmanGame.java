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
