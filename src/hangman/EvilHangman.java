package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.SortedSet;

public class EvilHangman
{
    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {
        String dictionaryFileName = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int numOfGuess = Integer.parseInt(args[2]);

        File file = new File(dictionaryFileName);

        EvilHangmanGame game = new EvilHangmanGame();
        game.startGame(file,wordLength);

        HashSet<String> gameResult = new HashSet<>();

        char [] key = new char[wordLength];
        char [] oldKey = new char[wordLength];

        for (int i=0; i < wordLength; i++)
        {
            key[i] = '-';
            oldKey[i] = '-';
        }

        while (numOfGuess != 0)
        {
            System.out.println("You have " + numOfGuess + " guesses left");

            StringBuilder usedLetters = new StringBuilder();

            SortedSet<Character> set = game.getGuessedLetters();

            for (char character : set)
            {
                usedLetters.append(character);
                usedLetters.append(", ");
            }
            if (set.size() != 0)
            {
                usedLetters.append("\b\b");
            }

            System.out.println("Used letters: [" + usedLetters.toString() + "]");


            System.out.println("Word: " + Arrays.toString(key));


            System.out.println("Enter guess: ");
            Scanner sc = new Scanner(System.in);
            String guess = sc.next();
            guess = guess.toLowerCase();

            if (guess.length() > 1 || guess.charAt(0) < 'a' || guess.charAt(0) > 'z')
            {
                System.out.println("Invalid input!\n\n");
                continue;
            }

            char [] theGuess = guess.toCharArray();

            if (set.contains(theGuess[0]))
            {
                System.out.println("Guess already made!\n\n");
                continue;
            }

            gameResult = (HashSet<String>) game.makeGuess(theGuess[0]);


            for (String n : gameResult)
            {
                for (int i=0; i < wordLength; i++)
                {
                    if (n.charAt(i) == theGuess[0])
                    {
                        key[i] = n.charAt(i);
                    }
                }
                break;
            }


            if (Arrays.equals(key,oldKey))
            {
                System.out.println("Sorry, there are no " + theGuess[0] + "\n\n");
                numOfGuess--;
            }
            else
            {
                int num = 0;

                for (int i=0; i < wordLength; i++)
                {
                    if (key[i] == theGuess[0])
                    {
                        num++;
                    }
                }
                System.out.println("Yes, there is " + num + " " + theGuess[0] + "'s\n\n");
            }

            oldKey = key.clone();

            boolean finished = true;

            for (int i=0; i < wordLength; i++)
            {
                if (key[i] == '-')
                {
                    finished = false;
                }
            }

            if (finished)
            {
                System.out.println("You Win!\nYou guessed the word: " + Arrays.toString(key));
                break;
            }

        }

        if (numOfGuess == 0)
        {
            HashSet<String> correctWord = gameResult;  //game.getAllWords();
            String word = "";
            for (String n : correctWord)
            {
                word = n;
                break;
            }
            System.out.println("You Lose!\nThe word was: " + word);
        }
    }
}
