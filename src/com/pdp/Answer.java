package com.pdp;

import java.util.Scanner;

/**
 * Enum representing user roles.
 */
enum Role {
    STUDENT, TEACHER
}

/**
 * Class representing an answer.
 */
class Answer {
    private String answer; // The answer text
    private boolean isCorrect; // Whether this answer is correct

    public Answer(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}

/**
 * Class representing a question in the quiz.
 */
class Question {
    private String question; // The question text
    private Answer[] answers = new Answer[4]; // Array of possible answers

    public Question(String question, Answer[] answers) {
        this.question = question;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public Answer[] getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(question + "\n");
        for (int i = 0; i < answers.length; i++) {
            sb.append(i + 1).append(". ").append(answers[i].getAnswer()).append("\n");
        }
        return sb.toString();
    }
}

/**
 * Singleton class representing the application logic.
 */
class Application {
    private static Application instance;
    private static final int MAX_USERS = 10;
    private static final int MAX_QUESTIONS = 10;
    private String[][] userList = new String[MAX_USERS][3]; // Array to store user details
    private Question[] questions = new Question[MAX_QUESTIONS]; // Array to store quiz questions
    private String[][] userResult = new String[MAX_USERS][3]; // Array to store user results

    private Application() {
        // Private constructor for Singleton pattern
    }

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        // Initialize with some users
        addUser("user1", "pass1", Role.STUDENT);
        addUser("user2", "pass2", Role.TEACHER);
        addUser("user3", "pass3", Role.STUDENT);
        addUser("user4", "pass4", Role.STUDENT);
        addUser("user5", "pass5", Role.STUDENT);

        System.out.println("1. Login\n2. Register");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                loginUser(scanner);
                break;
            case 2:
                registerUser(scanner);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private void addUser(String username, String password, Role role) {
        for (int i = 0; i < userList.length; i++) {
            if (userList[i][0] == null) {
                userList[i][0] = username;
                userList[i][1] = password;
                userList[i][2] = role.toString();
                break;
            }
        }
    }

    private void loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (String[] user : userList) {
            if (user[0] != null && user[0].equals(username) && user[1].equals(password)) {
                System.out.println("Login successful as " + user[2]);
                if (Role.valueOf(user[2]) == Role.STUDENT) {
                    showStudentMenu(scanner);
                } else if (Role.valueOf(user[2]) == Role.TEACHER) {
                    showTeacherMenu(scanner);
                }
                return;
            }
        }

        System.out.println("User not found");
    }

    private void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        for (String[] user : userList) {
            if (user[0] != null && user[0].equals(username)) {
                System.out.println("User already exists");
                return;
            }
        }

        addUser(username, password, Role.STUDENT);
        System.out.println("User registered successfully with STUDENT role");
    }

    private void showStudentMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Start Quiz\n2. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    startQuiz(scanner);
                    break;
                case 2:
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private void startQuiz(Scanner scanner) {
        for (Question question : questions) {
            if (question == null) continue;
            System.out.println(question);

            int answerIndex = scanner.nextInt() - 1;
            scanner.nextLine(); // Consume newline

            boolean isCorrect = question.getAnswers()[answerIndex].isCorrect();
            System.out.println("Your answer is " + (isCorrect ? "correct" : "incorrect"));

            // Store the result
            storeResult(question.getQuestion(), answerIndex, isCorrect);
        }

        displayResults();
    }

    private void storeResult(String question, int answerIndex, boolean isCorrect) {
        for (int i = 0; i < userResult.length; i++) {
            if (userResult[i][0] == null) {
                userResult[i][0] = question;
                userResult[i][1] = String.valueOf(answerIndex + 1);
                userResult[i][2] = String.valueOf(isCorrect);
                break;
            }
        }
    }

    private void displayResults() {
        for (String[] result : userResult) {
            if (result[0] != null) {
                System.out.println(result[0] + " -> " + result[1] + " => " + result[2]);
            }
        }
    }

    private void showTeacherMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Create Quiz\n2. Delete Quiz\n3. Update Quiz\n4. List Quiz\n5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createQuiz(scanner);
                    break;
                case 2:
                    deleteQuiz(scanner);
                    break;
                case 3:
                    updateQuiz(scanner);
                    break;
                case 4:
                    listQuiz();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private void createQuiz(Scanner scanner) {
        System.out.print("Enter question: ");
        String questionText = scanner.nextLine();

        Answer[] answers = new Answer[4];
        for (int i = 0; i < 4; i++) {
            System.out.print("Enter answer " + (i + 1) + ": ");
            String answerText = scanner.nextLine();
            System.out.print("Is this the correct answer? (true/false): ");
            boolean isCorrect = scanner.nextBoolean();
            scanner.nextLine(); // Consume newline
            answers[i] = new Answer(answerText, isCorrect);
        }

        for (int i = 0; i < questions.length; i++) {
            if (questions[i] == null) {
                questions[i] = new Question(questionText, answers);
                break;
            }
        }
    }

    private void deleteQuiz(Scanner scanner) {
        listQuiz();
        System.out.print("Enter the index of the question to delete: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (index >= 0 && index < questions.length && questions[index] != null) {
            questions[index] = null;
            System.out.println("Question deleted successfully");
        } else {
            System.out.println("Invalid index");
        }
    }

    private void updateQuiz(Scanner scanner) {
        listQuiz();
        System.out.print("Enter the index of the question to update: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        if (index >= 0 && index < questions.length && questions[index] != null) {
            System.out.print("Enter new question: ");
            String questionText = scanner.nextLine();

            Answer[] answers = new Answer[4];
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter new answer " + (i + 1) + ": ");
                String answerText = scanner.nextLine();
                System.out.print("Is this the correct answer? (true/false): ");
                boolean isCorrect = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline
                answers[i] = new Answer(answerText, isCorrect);
            }

            questions[index] = new Question(questionText, answers);
            System.out.println("Question updated successfully");
        } else {
            System.out.println("Invalid index");
        }
    }

    private void listQuiz() {
        for (int i = 0; i < questions.length; i++) {
            if (questions[i] != null) {
                System.out.println((i + 1) + ". " + questions[i].getQuestion());
                for (int j = 0; j < questions[i].getAnswers().length; j++) {
                    System.out.println("    " + (j + 1) + ": " + questions[i].getAnswers()[j].getAnswer());
                }
            }
        }
    }

    public static void main(String[] args) {
        // Singleton instance orqali run() methodini ishga tushirish
        Application app = Application.getInstance();
        app.run();
    }
}
