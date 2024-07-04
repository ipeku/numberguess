package week6;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Week6 implements WindowListener, ActionListener {

    private Frame frame;
    private Panel panel;
    private TextField guessField;
    private Button guessButton;
    private TextField resultLabel;
    private Button resetButton;
    private Button showNumberButton;
    private Label scoreLabel;
    private int score;
    private boolean gameWon;
    private boolean numberShown;

    private int[] randomNumberArray;
    private List<Integer> randomDigits;
    private int digitCount;

    public Week6() {
        frame = new Frame("Number Guessing Game");
        panel = new Panel();

        Label label = new Label("Your Guess:");
        panel.add(label);

        guessField = new TextField(10);
        panel.add(guessField);

        guessButton = new Button("OK");
        panel.add(guessButton);

        showNumberButton = new Button("Show the Number");
        panel.add(showNumberButton);

        resetButton = new Button("Restart");
        panel.add(resetButton);

        guessButton.addActionListener(this);
        showNumberButton.addActionListener(this);
        resetButton.addActionListener(this);

        resultLabel = new TextField(50);
        panel.add(resultLabel);

        scoreLabel = new Label("Score: 0");
        panel.add(scoreLabel);

        frame.add(panel);
        frame.addWindowListener(this);
        frame.setSize(1000, 400);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        frame.setBackground(Color.YELLOW);
        frame.setResizable(false);
        frame.setVisible(true);

        generateRandomNumber();
    }

    private void generateRandomNumber() {
        Random random = new Random();
        boolean validRandomNumber = false;

        while (!validRandomNumber) {
            randomNumberArray = new int[4];
            randomDigits = new ArrayList<>();

            for (int i = 0; i < 4; i++) {
                int digit;
                do {
                    digit = random.nextInt(10);
                } while (randomDigits.contains(digit));
                randomNumberArray[i] = digit;
                randomDigits.add(digit);
            }

            boolean duplicateDigits = randomDigits.size() != 4;
            if (!duplicateDigits) {
                validRandomNumber = true;
            }
        }

     

        digitCount = 4;
        gameWon = false;
        numberShown = false;
    }

    public static void main(String[] args) {
        Week6 game = new Week6();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == guessButton) {
            if (gameWon) {
                resultLabel.setBackground(Color.white);
                resultLabel.setText("Please press Restart to start a new game.");
                return;
            }
            if (numberShown) {
                resultLabel.setBackground(Color.white);
                resultLabel.setText("Please press Restart to start a new game after showing the number.");
                return;
            }

            String guessText = guessField.getText();
            if (guessText.isEmpty()) {
                resultLabel.setBackground(Color.white);
                resultLabel.setText("Please make a guess before pressing OK.");
                return;
            }
            
            try {
                int guess = Integer.parseInt(guessText);

                if (guess < 1234 || guess > 9876) {
                    resultLabel.setBackground(Color.white);
                    resultLabel.setText("ERROR: Please enter a number between 1234 and 9876.");
                    return;
                }

                boolean[] digits = new boolean[10];
                boolean duplicateDigits = false;
                int tempGuess = guess;
                while (tempGuess > 0) {
                    int digit = tempGuess % 10;
                    if (digits[digit]) {
                        duplicateDigits = true;
                        break;
                    }
                    digits[digit] = true;
                    tempGuess /= 10;
                }

                if (duplicateDigits) {
                    resultLabel.setBackground(Color.white);
                    resultLabel.setText("ERROR: The number cannot contain duplicate digits.");
                    return;
                }

                int[] guessArray = new int[4];
                guessArray[0] = guess / 1000;
                guessArray[1] = (guess / 100) % 10;
                guessArray[2] = (guess / 10) % 10;
                guessArray[3] = guess % 10;

                int correctDigits = 0;
                int correctPositions = 0;

                boolean[] matchedRandomDigits = new boolean[4];
                boolean[] matchedGuessDigits = new boolean[4];

                for (int i = 0; i < 4; i++) {
                    if (randomDigits.contains(guessArray[i])) {
                        correctDigits++;
                        int index = randomDigits.indexOf(guessArray[i]);
                        if (index == i) {
                            correctPositions++;
                            matchedRandomDigits[i] = true;
                            matchedGuessDigits[i] = true;
                        }
                    }
                }

                int incorrectPositions = 0;
                for (int i = 0; i < 4; i++) {
                    if (!matchedGuessDigits[i]) {
                        int digit = guessArray[i];
                        if (randomDigits.contains(digit) && !matchedRandomDigits[randomDigits.indexOf(digit)]) {
                            incorrectPositions++;
                        }
                    }
                }

                if (correctPositions == 4) {
                    resultLabel.setBackground(Color.green);
                    resultLabel.setText("Congrats, you guessed right!");
                    score++;
                    scoreLabel.setText("Score: " + score);
                    gameWon = true;
                } else if (correctPositions != 0 && correctDigits != 0) {
                    if (correctDigits - correctPositions != 0)
                        resultLabel.setText("\n" + correctPositions + "+" + (correctDigits - correctPositions) + "-");
                    else
                        resultLabel.setText("\n" + correctPositions + "+");
                } else if (correctPositions != 0) {
                    resultLabel.setBackground(Color.white);
                    resultLabel.setText(correctPositions + "+");
                } else if (correctDigits != 0) {
                    resultLabel.setBackground(Color.white);
                    if (correctDigits - correctPositions != 0)
                        resultLabel.setText((correctDigits - correctPositions) + "-");
                } else {
                    resultLabel.setText(("0"));
                }

            } catch (NumberFormatException ex) {
                resultLabel.setBackground(Color.white);
                resultLabel.setText("ERROR: Please enter a valid number.");
            }
        } else if (e.getSource() == resetButton) {
            scoreLabel.setText("Score: " + score);
            generateRandomNumber();
            guessField.setText("");
            resultLabel.setText("");
            gameWon = false; 
            numberShown = false; 
            guessButton.setEnabled(true); 
        } else if (e.getSource() == showNumberButton) {
           
            numberShown = true;
            resultLabel.setBackground(Color.white);
            resultLabel.setText("Random Number: " + randomNumberArray[0] + randomNumberArray[1] + randomNumberArray[2] + randomNumberArray[3]);
            guessButton.setEnabled(false); 
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
