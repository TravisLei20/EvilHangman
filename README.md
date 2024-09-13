# Evil Hangman

While many strategies exist for building competitive computer game players, one of the most entertaining approaches is simply to cheat. Instead of teaching a computer the complexities of strategy, why not program it to bend the rules and win every time? Evil Hangman is a mischievous twist on the classic game, where the computer plays unfairly, stacking the odds against the human player in a sly and humorous way.

In this version of Hangman, the computer doesn't commit to a single word from the start. Instead, it dynamically changes the hidden word to maximize its chances of winning by using a concept called "word families." This method ensures that the computer always has an advantage, making it nearly impossible for the human player to correctly guess the word.

## How the Game Works:

The program loads a list of over 125,000 words from the "dictionary.txt" file to ensure a wide range of potential words to choose from.

The user is asked to input a word length, and the program checks that there are valid words of that length. The user is also prompted to enter the number of guesses allowed.

Throughout the game, the program displays the number of guesses remaining, and the letters already guessed.

Whenever the user guesses a letter, the program partitions the remaining words into "word families," based on their patterns of letter placement. The computer then selects the largest word family, effectively choosing a new secret word and ensuring that the user's guess reveals as little as possible.

The game continues until the user either guesses the word correctly or runs out of guesses. If the player exhausts their guesses, the computer triumphantly reveals a word from the remaining list and pretends that was the chosen word all along.

## Usage

In the src directory run java -cp . hangman.EvilHangman ../dictionary.txt {number for the length of the word} {number of guesses the player wants.}

***Example:*** java -cp . hangman.EvilHangman ../dictionary.txt 6 12
