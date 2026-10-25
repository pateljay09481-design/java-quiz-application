
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private String username;
    private int currentQuestion = 0;
    private int score = 0;

    private final String[] questions = {
        "Which language is used for Android development?",
        "Which company originally developed Java?",
        "Which keyword creates an object in Java?",
        "Which method is the entry point of Java?",
        "Which symbol ends a Java statement?"
    };

    private final String[][] options = {
        {"Python", "Java", "HTML", "CSS"},
        {"Microsoft", "Apple", "Sun Microsystems", "Google"},
        {"class", "new", "void", "static"},
        {"start()", "run()", "main()", "init()"},
        {".", ":", ",", ";"}
    };

    // Correct answers are stored as option indexes (0 to 3)
    private final int[] answers = {1, 2, 1, 2, 3};

    private JLabel questionLabel;
    private JLabel progressLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JButton nextButton;
    private JPanel mainPanel;

    public Main() {
        setTitle(" Quiz Application");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showWelcomeScreen();
        setVisible(true);
    }

    private void showWelcomeScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 245, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(" QUIZ APP", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(40, 70, 150));

        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField nameField = new JTextField(18);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton startButton = new JButton("Start Quiz");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridy = 1;
        panel.add(nameLabel, gbc);

        gbc.gridy = 2;
        panel.add(nameField, gbc);

        gbc.gridy = 3;
        panel.add(startButton, gbc);

        setContentPane(panel);

        startButton.addActionListener(e -> {
            username = nameField.getText().trim();

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Please enter your name."
                );
                return;
            }

            currentQuestion = 0;
            score = 0;
            showQuizScreen();
        });

        revalidate();
        repaint();
    }

    private void showQuizScreen() {
        mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.setBackground(Color.WHITE);

        progressLabel = new JLabel("", SwingConstants.RIGHT);
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 19));

        JPanel topPanel = new JPanel(new BorderLayout(10, 20));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(progressLabel, BorderLayout.NORTH);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        optionButtons = new JRadioButton[4];
        optionGroup = new ButtonGroup();

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        optionsPanel.setBackground(Color.WHITE);

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            optionButtons[i].setBackground(Color.WHITE);
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        nextButton = new JButton("Next Question");
        nextButton.setFont(new Font("Arial", Font.BOLD, 15));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(nextButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        loadQuestion();

        nextButton.addActionListener(e -> checkAnswer());

        revalidate();
        repaint();
    }

    private void loadQuestion() {
        progressLabel.setText(
            "Question " + (currentQuestion + 1) + " of " + questions.length
        );

        questionLabel.setText(
            "<html>" + questions[currentQuestion] + "</html>"
        );

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[currentQuestion][i]);
        }

        optionGroup.clearSelection();

        if (currentQuestion == questions.length - 1) {
            nextButton.setText("Finish Quiz");
        } else {
            nextButton.setText("Next Question");
        }
    }

    private void checkAnswer() {
        int selected = -1;

        for (int i = 0; i < 4; i++) {
            if (optionButtons[i].isSelected()) {
                selected = i;
                break;
            }
        }

        if (selected == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select an answer."
            );
            return;
        }

        if (selected == answers[currentQuestion]) {
            score++;
        }

        currentQuestion++;

        if (currentQuestion < questions.length) {
            loadQuestion();
        } else {
            showResultScreen();
        }
    }

    private void showResultScreen() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 245, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.gridx = 0;

        JLabel title = new JLabel("QUIZ COMPLETED!");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel name = new JLabel("Name: " + username);
        JLabel result = new JLabel(
            "Your Score: " + score + " / " + questions.length
        );

        double percentage = score * 100.0 / questions.length;

        JLabel percent = new JLabel(
            String.format("Percentage: %.1f%%", percentage)
        );

        JLabel message = new JLabel(
            score >= 4 ? "Excellent work!" :
            score >= 3 ? "Good job!" : "Keep practicing!"
        );

        name.setFont(new Font("Arial", Font.PLAIN, 18));
        result.setFont(new Font("Arial", Font.BOLD, 22));
        percent.setFont(new Font("Arial", Font.PLAIN, 18));
        message.setFont(new Font("Arial", Font.ITALIC, 18));

        JButton restartButton = new JButton("Restart Quiz");
        JButton exitButton = new JButton("Exit");

        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridy = 1;
        panel.add(name, gbc);

        gbc.gridy = 2;
        panel.add(result, gbc);

        gbc.gridy = 3;
        panel.add(percent, gbc);

        gbc.gridy = 4;
        panel.add(message, gbc);

        gbc.gridy = 5;
        panel.add(restartButton, gbc);

        gbc.gridy = 6;
        panel.add(exitButton, gbc);

        setContentPane(panel);

        restartButton.addActionListener(e -> showWelcomeScreen());
        exitButton.addActionListener(e -> System.exit(0));

        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
