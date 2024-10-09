#include <iostream>
#include <unordered_map>
#include <vector>
#include <string>
using namespace std;

class Book {
public:
    int bookId;
    string title;
    string author;
    bool isAvailable;

    Book(int id, string t, string a) : bookId(id), title(t), author(a), isAvailable(true) {}
};

class User {
public:
    int userId;
    string name;
    vector<int> borrowedBooks;

    User(int id, string n) : userId(id), name(n) {}

    void borrowBook(int bookId) {
        borrowedBooks.push_back(bookId);
    }

    void returnBook(int bookId) {
        for (auto it = borrowedBooks.begin(); it != borrowedBooks.end(); ++it) {
            if (*it == bookId) {
                borrowedBooks.erase(it);
                break;
            }
        }
    }
};

class Library {
private:
    unordered_map<int, Book> books;   // Book ID as key
    unordered_map<int, User> users;   // User ID as key

public:
    void addBook(int id, string title, string author) {
        books[id] = Book(id, title, author);
    }

    void addUser(int id, string name) {
        users[id] = User(id, name);
    }

    void borrowBook(int userId, int bookId) {
        if (books[bookId].isAvailable) {
            books[bookId].isAvailable = false;
            users[userId].borrowBook(bookId);
            cout << "User " << userId << " borrowed book " << bookId << endl;
        } else {
            cout << "Book " << bookId << " is not available" << endl;
        }
    }

    void returnBook(int userId, int bookId) {
        books[bookId].isAvailable = true;
        users[userId].returnBook(bookId);
        cout << "User " << userId << " returned book " << bookId << endl;
    }

    void searchBook(int bookId) {
        if (books.find(bookId) != books.end()) {
            Book book = books[bookId];
            cout << "Book ID: " << book.bookId << ", Title: " << book.title << ", Author: " << book.author << ", Available: " << (book.isAvailable ? "Yes" : "No") << endl;
        } else {
            cout << "Book not found!" << endl;
        }
    }

    void displayAllBooks() {
        for (auto &b : books) {
            cout << "Book ID: " << b.second.bookId << ", Title: " << b.second.title << ", Author: " << b.second.author << ", Available: " << (b.second.isAvailable ? "Yes" : "No") << endl;
        }
    }
};

int main() {
    Library lib;

    // Adding books
    lib.addBook(1, "The Catcher in the Rye", "J.D. Salinger");
    lib.addBook(2, "To Kill a Mockingbird", "Harper Lee");
    lib.addBook(3, "1984", "George Orwell");

    // Adding users
    lib.addUser(101, "Alice");
    lib.addUser(102, "Bob");

    // Borrow and return operations
    lib.borrowBook(101, 1);  // Alice borrows "The Catcher in the Rye"
    lib.borrowBook(102, 2);  // Bob borrows "To Kill a Mockingbird"
    lib.returnBook(101, 1);  // Alice returns the book
    lib.borrowBook(101, 3);  // Alice borrows "1984"

    // Search and display operations
    lib.searchBook(1);  // Search for book with ID 1
    lib.displayAllBooks();  // Display all books

    return 0;
}
