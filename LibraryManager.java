import java.util.ArrayList;

public interface LibraryManager {
    void addStudyBook(StudyBook book);
    void removeStudyBook(StudyBook book);
    void addNovel(Novel novel);
    void removeNovel(Novel novel);
    void addBorrower(Borrower borrower);
    void removeBorrower(Borrower borrower);
    void borrowBook(Borrower borrower, Book book, int days);
    void returnBook(Borrower borrower, Book book);
    StudyBook findStudyBooks(String title, String author);
    Novel findNovels(String title, String author);
    Book findBorrowedBook(Borrower borrower, String title, String author);
    ArrayList<StudyBook> getStudyBooks();
    ArrayList<Novel> getNovels();
    Borrower getBorrower(String borrowerName);
    ArrayList<Borrower> getBorrowersList();
}
