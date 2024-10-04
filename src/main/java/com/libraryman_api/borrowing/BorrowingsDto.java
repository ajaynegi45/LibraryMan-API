package com.libraryman_api.borrowing;


import com.libraryman_api.book.BookDto;
import com.libraryman_api.fine.Fines;
import com.libraryman_api.member.MembersDto;
import lombok.*;
import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class BorrowingsDto {


    private int borrowingId;

    private BookDto book;

    private Fines fine;

    private MembersDto member;

    private Date borrowDate;

    private Date dueDate;

    private Date returnDate;


}
