package com.libraryman_api.borrowing;

import com.libraryman_api.book.Book;
import com.libraryman_api.fine.Fines;
import com.libraryman_api.member.Members;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Borrowings {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "borrowing_id_generator")
    @SequenceGenerator(name = "borrowing_id_generator",
            sequenceName = "borrowing_id_sequence",
            allocationSize = 1)
    @Column(name = "borrowing_id")
    private int borrowingId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "fine_id")
    private Fines fine;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Members member;

    @Column(name = "borrow_date", nullable = false)
    private Date borrowDate;

    @Column(name = "due_date", nullable = false)
    private Date dueDate;

    @Column(name = "return_date")
    private Date returnDate;


}