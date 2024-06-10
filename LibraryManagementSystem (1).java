import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;

abstract class Book extends ArrayList<Book> implements Serializable {
    private String title;
    private String author;
    private boolean isAvailable;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    Book() {}

    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author;
    }
}

class StudyBook extends Book implements Serializable {
    private String subject;

    public StudyBook(String title, String author, String subject) {
        super(title, author);
        this.subject = subject;
    }

    public StudyBook() {
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return super.toString() + ", Subject: " + subject;
    }
}

class Novel extends Book implements Serializable {
    private String genre;

    public Novel(String title, String author, String genre) {
        super(title, author);
        this.genre = genre;
    }

    public Novel() {
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return super.toString() + ", Genre: " + genre;
    }
}

class Person implements Serializable {
    protected String username;
    private String password;
    private String email;

    private static final String USERS_FILE = "users.txt";

    public Person(String n, String password) {
        this.username = username;
        this.password = password;
    }

    public Person(String username) {
        this.username = username;
    }

    public Person() {
    }

    public void register() {
        JFrame frame = new JFrame("Register");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 2));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        customizeButton1(registerButton);
        customizeButton1(cancelButton);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();

            if (Person.isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(frame, "Username already exists. Please choose another one.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Person.isPasswordValid(password)) {
                JOptionPane.showMessageDialog(frame, "Password must be exactly 8 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Person.isEmailValid(email)) {
                JOptionPane.showMessageDialog(frame, "Email must be in the format -----@gmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Person.saveUserData(username, password, email);
            JOptionPane.showMessageDialog(frame, "Registration successful.");

            // Assuming we want to show the main menu after registration
            frame.dispose();
            LibraryManagementSystem.showMainMenu();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            LibraryManagementSystem.showMainMenu();
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(emailLabel);
        frame.add(emailField);
        frame.add(registerButton);
        frame.add(cancelButton);

        frame.setVisible(true);
    }

    private static void customizeButton1(JButton button) {
        button.setFont(new Font("Times New Roman", Font.BOLD, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(193, 128, 88)); // Light background for contrast
        button.setBorder(new LineBorder(Color.BLACK, 1));
    }

    static boolean isUsernameTaken(String username) {
            try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] userData = line.split(",");
                    if (userData[0].equals(username)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        static boolean isPasswordValid(String password) {
            return password.length() == 8;
        }

        static boolean isEmailValid(String email) {
            String emailPattern = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
            return Pattern.matches(emailPattern, email);
        }

        static void saveUserData(String username, String password, String email) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                writer.write(username + "," + password + "," + email);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public String getUserName() {
        return username;
    }
}

class Borrower extends Person implements Serializable {
    private ArrayList<Book> borrowedBooks = new ArrayList<>();
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String phoneNumber;
    private String address;

    public Borrower(String username, Book book) {
        super(username);
        borrowedBooks.add(book);
    }

    public Borrower() {}

    public void setUserName(String name) {
        this.username = name;
    }

    public String getUserName() {
        return username;
    }

    public void setBorrowedBooks(Book book) {
        borrowedBooks.add(book);
    }

    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void borrowBook(Book book) {
        book.setAvailable(false);
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        book.setAvailable(true);
        borrowedBooks.remove(book);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Borrower Name: " + this.getUserName() + ", Borrowed Date: " + this.getBorrowDate() + ", Return Date: " + this.getReturnDate();
    }
}

class Manager extends Person implements LibraryManager, Serializable {
    private ArrayList<StudyBook> studyBooksRecord = new ArrayList<>();
    private ArrayList<Novel> novelsRecord = new ArrayList<>();
    private ArrayList<Borrower> borrowersRecord = new ArrayList<>();

    Manager(String n, String password) {
        super(n, password);
    }

    public Manager() {
    }

    public void addStudyBook(StudyBook book) {
        studyBooksRecord.add(book);
    }

    public void removeStudyBook(StudyBook book) {
        studyBooksRecord.remove(book);
    }

    public void addNovel(Novel novel) {
        novelsRecord.add(novel);
    }

    public void removeNovel(Novel novel) {
        novelsRecord.remove(novel);
    }

    public void addBorrower(Borrower borrower) {
        borrowersRecord.add(borrower);
    }

    public void removeBorrower(Borrower borrower) {
        borrowersRecord.remove(borrower);
    }

    public void borrowBook(Borrower borrower, Book book, int days) {
        borrower.borrowBook(book);
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusDays(days);
        borrower.setBorrowDate(borrowDate);
        borrower.setReturnDate(returnDate);
    }

    public void returnBook(Borrower borrower, Book book) {
        borrower.returnBook(book);
        if (borrower.getBorrowedBooks().isEmpty()) {
            removeBorrower(borrower);
        }
    }

    public StudyBook findStudyBooks(String title, String author) {
        for (StudyBook studyBooks : studyBooksRecord) {
            if (studyBooks.getTitle().equalsIgnoreCase(title) && studyBooks.getAuthor().equalsIgnoreCase(author) && studyBooks.isAvailable()) {
                return studyBooks;
            }
        }
        return null;
    }

    public Novel findNovels(String title, String author) {
        for (Novel novels : novelsRecord) {
            if (novels.getTitle().equalsIgnoreCase(title) && novels.getAuthor().equalsIgnoreCase(author) && novels.isAvailable()) {
                return novels;
            }
        }
        return null;
    }

    public Book findBorrowedBook(Borrower borrower, String title, String author) {
        for (Book book : borrower.getBorrowedBooks()) {
            if (book.getTitle().equalsIgnoreCase(title) && book.getAuthor().equalsIgnoreCase(author)) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<StudyBook> getStudyBooks() {
        return studyBooksRecord;
    }

    public ArrayList<Novel> getNovels() {
        return novelsRecord;
    }

    public void setStudyBooks(ArrayList<StudyBook> studybook) {
        studyBooksRecord = studybook;
    }

    public void setNovel(ArrayList<Novel> novelbook) {
        novelsRecord = novelbook;
    }

    public Borrower getBorrower(String borrowerName) {
        for (Borrower borrower : borrowersRecord) {
            if (borrower.getUserName().equalsIgnoreCase(borrowerName)) {
                return borrower;
            }
        }
        return null;
    }

    public ArrayList<Borrower> getBorrowersList() {
        return borrowersRecord;
    }
}

public class LibraryManagementSystem {
    private static final String DATA_FILE = "library_data.ser";
    private static final String BORROWER_FILE = "borrower_data.ser";
    private static final String BORROWED_BOOKS_FILE = "borrowed_books_data.ser";
    public static String previousMenu;

    private static Manager manager = new Manager();
    private static Borrower borrower1 = new Borrower();
    private static StudyBook studyBook = new StudyBook();
    private static Novel novel = new Novel();

    public LibraryManagementSystem() {
        // Load data from file if exists
        loadLibraryData();
    }

    private static void loadLibraryData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            manager = (Manager) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading library data: " + e.getMessage());
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(BORROWER_FILE))) {
            borrower1 = (Borrower) in.readObject();
            System.out.println("Updated!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading borrower data: " + e.getMessage());
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(BORROWED_BOOKS_FILE))) {
           Book book = (Book) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading borrower data: " + e.getMessage());
        }
    }

    private static void saveLibraryData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(manager);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BORROWER_FILE))) {
            oos.writeObject(borrower1);
            System.out.println("Updated!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void saveLibraryData(StudyBook studyBook) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BORROWED_BOOKS_FILE))) {
            oos.writeObject(studyBook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void saveLibraryData(Novel novel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BORROWED_BOOKS_FILE))) {
            oos.writeObject(novel);
            System.out.println("Borrower object serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void showMainMenu() {
        JFrame frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);  // Adjust size as necessary

        // Create a layered pane for background and buttons
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(400, 300));

        // Load background image
        ImageIcon backgroundImage = new ImageIcon("C:\\Users\\M S\\Downloads\\background.jpg");  // Replace with the path to your image
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, 400, 300);

        JLabel titleLabel = new JLabel("BOOKWHEELS");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 23));
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 40, 400, 25);  // Adjust position and size as necessary

        // Create buttons
        JButton librarianButton = new JButton("Librarian Login");
        JButton borrowerButton = new JButton("Borrower Login");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");

        // Customize buttons
        customizeButton(librarianButton);
        customizeButton(borrowerButton);
        customizeButton(registerButton);
        customizeButton(exitButton);

        // Set button bounds and positions
        int buttonWidth = 100;
        int buttonHeight = 25;
        int xPosition = (400 - buttonWidth) / 2;
        int yStartPosition = 100;
        int yGap = 10;

        librarianButton.setBounds(xPosition, yStartPosition, buttonWidth, buttonHeight);
        borrowerButton.setBounds(xPosition, yStartPosition + buttonHeight + yGap, buttonWidth, buttonHeight);
        registerButton.setBounds(xPosition, yStartPosition + 2 * (buttonHeight + yGap), buttonWidth, buttonHeight);
        exitButton.setBounds(xPosition, yStartPosition + 3 * (buttonHeight + yGap), buttonWidth, buttonHeight);

        // Add action listeners
        librarianButton.addActionListener(e -> {
            frame.dispose();
            showLibrarianLogin();
        });

        borrowerButton.addActionListener(e -> {
            frame.dispose();
            showBorrowerMenu();
        });

        registerButton.addActionListener(e -> {
            frame.dispose();
            Person person = new Person();
            person.register();
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Add components to layered pane
        layeredPane.add(backgroundLabel, Integer.valueOf(0));
        layeredPane.add(titleLabel, Integer.valueOf(1));       // Title above background
        layeredPane.add(librarianButton, Integer.valueOf(1));
        layeredPane.add(borrowerButton, Integer.valueOf(1));
        layeredPane.add(registerButton, Integer.valueOf(1));
        layeredPane.add(exitButton, Integer.valueOf(1));

        frame.add(layeredPane);
        frame.pack();
        frame.setVisible(true);
        previousMenu = "Main Menu";
    }

    private static void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE); // Light background for contrast
        button.setBorder(new LineBorder(Color.BLACK, 1));
    }

    private static void showLibrarianLogin() {
        JFrame frame = new JFrame("Librarian Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridBagLayout());

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        customizeLabel(passwordLabel);
        customizeButton1(loginButton);
        customizeButton1(cancelButton);

        loginButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            if (checkPassword(password)) {
                frame.dispose();
                showLibrarianMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 0, 10);

        // Add password label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(passwordLabel, gbc);

        // Add password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(passwordField, gbc);

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 253, 204));
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        // Add button panel
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(buttonPanel, gbc);

        frame.setVisible(true);
        previousMenu = "Librarian Login";
    }

    private static void customizeButton1(JButton button) {
        button.setFont(new Font("Times New Roman", Font.BOLD, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(193, 128, 88)); // Light background for contrast
        button.setBorder(new LineBorder(Color.BLACK, 1));
    }

    private static boolean checkPassword(String enteredPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(","); // Split the line into an array
                if (userData.length >= 2 && userData[1].trim().equals(enteredPassword)) {
                    return true; // Password matches
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Password not found or file error
    }

    private static void customizeLabel(JLabel label) {
        label.setFont(new Font("Times New Roman", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
    }

    private static void showLibrarianMenu() {
        JFrame frame = new JFrame("Librarian Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Custom JPanel for background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("C:\\Users\\M S\\Downloads\\download.jpg"); // Change the path to your image
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());

        JButton addBookButton = new JButton("Add Book");
        JButton removeBookButton = new JButton("Remove Book");
        JButton viewRecordsButton = new JButton("View Records");
        JButton viewBorrowersButton = new JButton("View Borrowers");
        JButton logoutButton = new JButton("Logout");

        customizeButton1(addBookButton);
        customizeButton1(removeBookButton);
        customizeButton1(viewRecordsButton);
        customizeButton1(viewBorrowersButton);
        customizeButton1(logoutButton);

        addBookButton.addActionListener(e -> {
            frame.dispose();
            showAddBookMenu();
        });

        removeBookButton.addActionListener(e -> {
            frame.dispose();
            showRemoveBookMenu();
        });

        viewRecordsButton.addActionListener(e -> {
            frame.dispose();
            previousMenu = "LibrarianMenu"; // Set previousMenu before navigating
            showViewRecordsMenu();
        });

        viewBorrowersButton.addActionListener(e -> {
            frame.dispose();
            previousMenu = "LibrarianMenu"; // Set previousMenu before navigating
            showViewBorrowersMenu();
        });

        logoutButton.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panel.add(addBookButton, gbc);
        panel.add(removeBookButton, gbc);
        panel.add(viewRecordsButton, gbc);
        panel.add(viewBorrowersButton, gbc);
        panel.add(logoutButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
        previousMenu = "Librarian Menu";
    }

    private static void showAddBookMenu() {
        JFrame frame = new JFrame("Add Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Study Book", "Novel"});
        JLabel extraLabel = new JLabel();
        JTextField extraField = new JTextField();
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        customizeLabel(titleLabel);
        customizeLabel(authorLabel);
        customizeLabel(typeLabel);
        customizeComboBox(typeComboBox);
        customizeLabel(extraLabel);
        customizeButton1(addButton);
        customizeButton1(cancelButton);


        typeComboBox.addActionListener(e -> {
            String selectedType = (String) typeComboBox.getSelectedItem();
            if ("Study Book".equals(selectedType)) {
                extraLabel.setText("Subject:");
            } else {
                extraLabel.setText("Genre:");
            }
        });

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String type = (String) typeComboBox.getSelectedItem();
            String extra = extraField.getText();

            if ("Study Book".equals(type)) {
                studyBook = new StudyBook(title, author, extra);
                manager.addStudyBook(studyBook);
                JOptionPane.showMessageDialog(frame, "Study book added successfully.");
            } else {
                novel = new Novel(title, author, extra);
                manager.addNovel(novel);
                JOptionPane.showMessageDialog(frame, "Novel added successfully.");
            }

            saveLibraryData();
            frame.dispose();
            showLibrarianMenu();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showLibrarianMenu();
        });

        frame.add(titleLabel);
        frame.add(titleField);
        frame.add(authorLabel);
        frame.add(authorField);
        frame.add(typeLabel);
        frame.add(typeComboBox);
        frame.add(extraLabel);
        frame.add(extraField);
        frame.add(addButton);
        frame.add(cancelButton);

        frame.setVisible(true);
    }

    private static void showRemoveBookMenu() {
        JFrame frame = new JFrame("Remove Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(6, 2));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Study Book", "Novel"});
        JButton removeButton = new JButton("Remove");
        JButton cancelButton = new JButton("Cancel");

        customizeLabel(titleLabel);
        customizeTextField(titleField);
        customizeLabel(authorLabel);
        customizeTextField(authorField);
        customizeLabel(typeLabel);
        customizeComboBox(typeComboBox);
        customizeButton1(removeButton);
        customizeButton1(cancelButton);

        removeButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String type = (String) typeComboBox.getSelectedItem();
            loadLibraryData();

            if ("Study Book".equals(type)) {
                studyBook = manager.findStudyBooks(title, author);
                if (studyBook != null) {
                    manager.removeStudyBook(studyBook);
                    JOptionPane.showMessageDialog(frame, "Study book removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Study book not found.");
                }
            } else {
                novel = manager.findNovels(title, author);
                if (novel != null) {
                    manager.removeNovel(novel);
                    JOptionPane.showMessageDialog(frame, "Novel removed successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Novel not found.");
                }
            }

            saveLibraryData();
            frame.dispose();
            showLibrarianMenu();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showLibrarianMenu();
        });

        frame.add(titleLabel);
        frame.add(titleField);
        frame.add(authorLabel);
        frame.add(authorField);
        frame.add(typeLabel);
        frame.add(typeComboBox);
        frame.add(removeButton);
        frame.add(cancelButton);

        frame.setVisible(true);
    }

    private static void customizeTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setForeground(Color.BLACK);
        textField.setBackground(Color.WHITE);
        textField.setBorder(new LineBorder(Color.BLACK, 1));
    }

    private static void customizeComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setForeground(Color.BLACK);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(new LineBorder(Color.GRAY, 1));
    }


    private static void showViewRecordsMenu() {
        JFrame frame = new JFrame("View Records");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder();
        JLabel recordsLabel = new JLabel(String.valueOf(sb.append("----- Study Books -----\n")));
        for (StudyBook studyBook : manager.getStudyBooks()) {
            sb.append(studyBook.toString()).append("\n");
        }

        sb.append("------ Novels ------\n");
        for (Novel novel : manager.getNovels()) {
            sb.append(novel.toString()).append("\n");
        }
        customizeLabel(recordsLabel);
        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            // Show the previous menu, depending on the current menu
            if (previousMenu == "MainMenu") {
                showMainMenu();
            } else if (previousMenu == "LibrarianMenu") {
                showLibrarianMenu();
            }
        });
        backButton.setPreferredSize(new Dimension(80, 30));
        frame.add(backButton, BorderLayout.NORTH);
        customizeButton1(backButton);

        frame.setVisible(true);
    }

    private static void showViewBorrowersMenu() {
        JFrame frame = new JFrame("View Borrowers");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        JLabel borrowersLabel = new JLabel("Borrowers:");
        StringBuilder sb = new StringBuilder();

        for (Borrower borrower : manager.getBorrowersList()) {
            sb.append(borrower.toString()).append("\n");
        }
        customizeLabel(borrowersLabel);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 12));

        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);
        JButton backButton = new JButton("Back");

        backButton.addActionListener(e -> {
            frame.dispose();
            // Show the previous menu, depending on the current menu
            if (previousMenu == "MainMenu") {
                showMainMenu();
            } else if (previousMenu == "LibrarianMenu") {
                showLibrarianMenu();
            }
        });
        backButton.setPreferredSize(new Dimension(80, 30));
        frame.add(backButton, BorderLayout.NORTH);
        customizeButton1(backButton);

        frame.setVisible(true);
    }

    private static void showBorrowerMenu() {
        JFrame frame = new JFrame("Borrower Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(255, 253, 204));

        JLabel nameLabel = new JLabel("Enter your name:");
        JTextField nameField = new JTextField(15);
        JButton proceedButton = new JButton("Proceed");
        JButton cancelButton = new JButton("Cancel");

        customizeButton1(proceedButton);
        customizeButton1(cancelButton);

        proceedButton.addActionListener(e -> {
            String name = nameField.getText();

            // Check if the username exists in users.txt
            if (usernameExists(name)) {
                frame.dispose();
                borrower1 = new Borrower();
                borrower1.setUserName(name);
                showBorrowerOptionsMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 10, 10);

        // Add name label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(nameLabel, gbc);

        // Add name field
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);

        // Add proceed button
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(proceedButton, gbc);

        // Add cancel button
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(cancelButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
        previousMenu = "Borrower Menu";
    }

    private static boolean usernameExists(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(","); // Split the line into an array
                if (userData.length >= 2 && userData[0].trim().equals(name)) {
                    return true; // Password matches
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Password not found or file error
    }


    private static void showBorrowerOptionsMenu() {
        JFrame frame = new JFrame("Borrower Options");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        // Custom JPanel for background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("C:\\Users\\M S\\Downloads\\Muriva Adele Garrett Plain Texture Beige Wallpaper - M55127.jpg"); // Change the path to your image
                Image image = icon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());

        JButton borrowBookButton = new JButton("Borrow Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton viewBorrowedBooksButton = new JButton("View Borrowed Books");
        JButton backButton = new JButton("Back");

        customizeButton1(borrowBookButton);
        customizeButton1(returnBookButton);
        customizeButton1(viewBorrowedBooksButton);
        customizeButton1(backButton);

        borrowBookButton.addActionListener(e -> {
            frame.dispose();
            showBorrowBookMenu();
        });

        returnBookButton.addActionListener(e -> {
            frame.dispose();
            showReturnBookMenu();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            showMainMenu();
        });

        viewBorrowedBooksButton.addActionListener(e -> {
            frame.dispose();
            previousMenu = "Borrower Menu"; // Set previousMenu before navigating
            showViewBorrowedBooksMenu();
        });

        JLabel quoteLabel = new JLabel("<html><div style='text-align: center;'>Bringing your desired world<br>at your doorstep</div></html>");
        quoteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        quoteLabel.setForeground(new Color(50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add quote label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(quoteLabel, gbc);

        // Add buttons
        gbc.gridx = 1;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        panel.add(borrowBookButton, gbc);
        panel.add(returnBookButton, gbc);
        panel.add(viewBorrowedBooksButton, gbc);
        panel.add(backButton, gbc);

        frame.add(panel);
        frame.setVisible(true);
        previousMenu = "Borrower Options";
    }

    private static void showBorrowBookMenu() {
        JFrame frame = new JFrame("Borrow Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(8, 2));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Study Book", "Novel"});
        JLabel daysLabel = new JLabel("Days:");
        JTextField daysField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField();
        JButton borrowButton = new JButton("Borrow");
        JButton cancelButton = new JButton("Cancel");

        customizeLabel(titleLabel);
        customizeTextField(titleField);
        customizeLabel(authorLabel);
        customizeTextField(authorField);
        customizeLabel(typeLabel);
        customizeComboBox(typeComboBox);
        customizeLabel(daysLabel);
        customizeTextField(daysField);
        customizeLabel(phoneLabel);
        customizeTextField(phoneField);
        customizeLabel(addressLabel);
        customizeTextField(addressField);
        customizeButton1(borrowButton);
        customizeButton1(cancelButton);

        borrowButton.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String type = (String) typeComboBox.getSelectedItem();
            int days = Integer.parseInt(daysField.getText());
            String phoneNumber = phoneField.getText();
            String address = addressField.getText();

            if (phoneNumber.length() != 11) {
                JOptionPane.showMessageDialog(frame, "Phone number must be 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Study Book".equals(type)) {
                studyBook = manager.findStudyBooks(title, author);
                if (studyBook != null) {
                    manager.borrowBook(borrower1, studyBook, days);
                    manager.addBorrower(borrower1);
                    LocalDate borrowDate = LocalDate.now();
                    LocalDate returnDate = borrowDate.plusDays(days);
                    JOptionPane.showMessageDialog(frame, "Study book borrowed successfully.\nBorrow Date: " + borrowDate + "\nReturn Date: " + returnDate);
                    saveLibraryData(studyBook);
                } else {
                    JOptionPane.showMessageDialog(frame, "Study book not found.");
                }
            } else {
                novel = manager.findNovels(title, author);
                if (novel != null) {
                    manager.borrowBook(borrower1, novel, days);
                    manager.addBorrower(borrower1);
                    LocalDate borrowDate = LocalDate.now();
                    LocalDate returnDate = borrowDate.plusDays(days);
                    JOptionPane.showMessageDialog(frame, "Novel borrowed successfully.\nBorrow Date: " + borrowDate + "\nReturn Date: " + returnDate);
                    saveLibraryData(novel);
                } else {
                    JOptionPane.showMessageDialog(frame, "Novel not found.");
                }
            }

            saveLibraryData();
            frame.dispose();
            showBorrowerOptionsMenu();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showBorrowerOptionsMenu();
        });

        frame.add(titleLabel);
        frame.add(titleField);
        frame.add(authorLabel);
        frame.add(authorField);
        frame.add(typeLabel);
        frame.add(typeComboBox);
        frame.add(daysLabel);
        frame.add(daysField);
        frame.add(phoneLabel);
        frame.add(phoneField);
        frame.add(addressLabel);
        frame.add(addressField);
        frame.add(borrowButton);
        frame.add(cancelButton);

        frame.setVisible(true);
    }

    private static void showReturnBookMenu() {
        JFrame frame = new JFrame("Return Book");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 2));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        JButton returnButton = new JButton("Return");
        JButton cancelButton = new JButton("Cancel");

        customizeLabel(titleLabel);
        customizeTextField(titleField);
        customizeLabel(authorLabel);
        customizeTextField(authorField);
        customizeButton1(returnButton);
        customizeButton1(cancelButton);

        returnButton.addActionListener(e -> {
            loadLibraryData(); // Load library data before returning a book
            String title = titleField.getText();
            String author = authorField.getText();
            Book book = manager.findBorrowedBook(borrower1, title, author);
            if (book != null) {
                manager.returnBook(borrower1, book);
                JOptionPane.showMessageDialog(frame, "Book returned successfully.");
                manager.removeBorrower(borrower1);
            } else {
                JOptionPane.showMessageDialog(frame, "Book not found in borrowed list.");
            }

            saveLibraryData();
            frame.dispose();
            showBorrowerOptionsMenu();
        });

        cancelButton.addActionListener(e -> {
            frame.dispose();
            showBorrowerOptionsMenu();
        });

        frame.add(titleLabel);
        frame.add(titleField);
        frame.add(authorLabel);
        frame.add(authorField);
        frame.add(returnButton);
        frame.add(cancelButton);

        frame.setVisible(true);
    }

    private static void showViewBorrowedBooksMenu() {
        JFrame frame = new JFrame("View Borrowed Books");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));

        frame.getContentPane().setBackground(new Color(255, 253, 204));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        loadLibraryData();

        StringBuilder sb = new StringBuilder();
        sb.append("----- Borrowed Books -----\n");
        for (Book book : borrower1.getBorrowedBooks()) {
            sb.append(book.toString()).append("\n");
        }
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        textArea.setForeground(Color.BLACK);
        textArea.setBackground( Color.WHITE);

        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            // Show the previous menu, depending on the current menu
            if (previousMenu == "MainMenu") {
                showMainMenu();
            } else if (previousMenu == "Borrower Menu") {
                showBorrowerOptionsMenu();
            }
        });
        backButton.setPreferredSize(new Dimension(80, 30));
        frame.add(backButton, BorderLayout.NORTH);
        customizeButton(backButton);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        LibraryManagementSystem libraryManagementSystem = new LibraryManagementSystem();
        libraryManagementSystem.showMainMenu();
    }
}
