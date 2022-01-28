
// This is the code that I wrote in the Exam.
// It was cleaner so I put it here.

package hangman;

        import java.io.File;
        import java.io.IOException;
        import java.util.*;
        import java.lang.String;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException
    {
        // Takes the arguments and places them into the correct location
        File file = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int numOfGuesses = Integer.parseInt(args[2]);

        // Declare and initialize the EvilHangmanGame
        EvilHangmanGame game = new EvilHangmanGame();

        // Start the game with given dictionary of words and the word length of the game
        game.startGame(file, wordLength);

        SortedSet<Character> guessedLetter;

        char [] key = new char[wordLength];
        char [] oldKey = new char[wordLength];

        // Give key and oldKey its initial values
        for (int i=0; i < wordLength; i++)
        {
            key[i] = '-';
            oldKey[i] = '-';
        }

        String input;
        String word = null;

        HashSet<String> result = new HashSet<>();

        int numOfGuessedLettersFoundInWord;

        boolean finishedGame;

        while (numOfGuesses != 0)
        {
            // Inform the user the amount of turns that he/she has left
            System.out.println("You have " + numOfGuesses + " left");

            guessedLetter = game.getGuessedLetters();

            // Display the letters already used in the current game
            System.out.println("Used Letters: " + guessedLetter);

            // Display the status of the game
            System.out.println("Word: " + String.valueOf(key));

            // Prompt user to enter guess
            System.out.println("Enter Guess: ");

            Scanner scanner = new Scanner(System.in);
            input = scanner.next();
            input = input.toLowerCase();

            // If the user input more than one character then display a invalid input message and the above info and re-prompt user
            if (input.length() > 1)
            {
                System.out.println("Invalid input\n");
                continue;
            }

            // Give guess the value of the guessed input
            char guess = input.toCharArray()[0];

            // If guess is not an alphabetical letter then display a invalid input message and the above info and re-prompt user
            if (guess < 'a' || guess > 'z')
            {
                System.out.println("Invalid input\n");
                continue;
            }

            // If the user has input a guess that has already been used then inform the user the status of the guess and the above info and re-prompt user
            if (guessedLetter.contains(guess))
            {
                System.out.println("Guess already made\n");
                continue;
            }

            // Make the guess and pass the result to result
            result = (HashSet<String>) game.makeGuess(guess);

            // Obtain the first word found in result
            for (String n : result)
            {
                word = n;
                break;
            }

            // Check if word is null
            if (word == null)
            {
                continue;
            }

            // Get the key of word
            word = game.getSubsetKey(word,guess);
            numOfGuessedLettersFoundInWord = 0;

            // Update key
            for (int i=0; i < wordLength; i++)
            {
                if (word.charAt(i) == guess)
                {
                    key[i] = guess;
                    numOfGuessedLettersFoundInWord++;
                }
            }

            // Compare key with last iterations oldKey and output appropriate message and info
            if (Arrays.equals(key,oldKey))
            {
                System.out.println("Sorry there are no " + guess + "'s");
                numOfGuesses--;
            }
            else
            {
                System.out.println("Yes there is " + numOfGuessedLettersFoundInWord + " " + guess + "'s");
            }

            // Set oldKey to key
            oldKey = key.clone();

            finishedGame = true;

            // Check if the game is finished and the player has won
            for (int i=0; i < wordLength; i++)
            {
                if (key[i] == '-')
                {
                    finishedGame = false;
                    break;
                }
            }

            // if the player has won the inform the user and output the correct word
            if (finishedGame)
            {
                for (String n : result)
                {
                    word = n;
                    break;
                }
                System.out.println("You win! You guessed the word: " + word);
                break;
            }

            System.out.println("\n");
        }

        // If the user ran out of guesses then inform the user that they lost and display the correct word
        if (numOfGuesses == 0)
        {
            for (String n : result)
            {
                word = n;
                break;
            }
            System.out.println("Sorry, you lost! The word was: " + word);
        }
    }
}

/*
// This is the original code.



package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
*/

