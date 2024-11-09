package com.libraryman_api.analytics;

import com.libraryman_api.book.BookRepository;
import com.libraryman_api.borrowing.BorrowingRepository;
import com.libraryman_api.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final BookRepository bookRepository;
    private final BorrowingRepository borrowingRepository;
    private final MemberRepository memberRepository;

    public AnalyticsService(BookRepository bookRepository, BorrowingRepository borrowingRepository, MemberRepository memberRepository) {
        this.bookRepository = bookRepository;
        this.borrowingRepository = borrowingRepository;
        this.memberRepository = memberRepository;
    }

    public Map<String, Object> getLibraryOverview() {
        Map<String, Object> overview = new HashMap<>();
        overview.put("totalBooks", bookRepository.count());
        overview.put("totalMembers", memberRepository.count());
        overview.put("totalBorrowings", borrowingRepository.count());
        return overview;
    }

    public List<Map<String, Object>> getPopularBooks(int limit) {
        return borrowingRepository.findMostBorrowedBooks(limit);
    }

    public Map<String, Long> getBorrowingTrends(LocalDate startDate, LocalDate endDate) {
        return borrowingRepository.getBorrowingTrendsBetweenDates(startDate, endDate);
    }

    public List<Map<String, Object>> getMemberActivityReport() {
        return memberRepository.getMemberActivityReport();
    }
}