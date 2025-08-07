package com.libraryman_api.analytics;

import com.libraryman_api.book.BookRepository;
import com.libraryman_api.borrowing.BorrowingRepository;
import com.libraryman_api.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link AnalyticsService} class.
 */
@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowingRepository borrowingRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getLibraryOverview() {
        when(bookRepository.count()).thenReturn(100L);
        when(borrowingRepository.count()).thenReturn(50L);
        when(memberRepository.count()).thenReturn(25L);

        Map<String, Object> overview = analyticsService.getLibraryOverview();

        assertEquals(100L, overview.get("totalBooks"));
        assertEquals(25L, overview.get("totalMembers"));
        assertEquals(50L, overview.get("totalBorrowings"));
    }

    @Test
    void getPopularBooks() {
        List<Map<String, Object>> expectedList = List.of(Map.of("title", "Book A", "borrowCount", 10), Map.of("title", "Book B", "borrowCount", 8));
        when(borrowingRepository.findMostBorrowedBooks(2)).thenReturn(expectedList);

        List<Map<String, Object>> result = analyticsService.getPopularBooks(2);

        assertEquals(expectedList, result);
    }

    @Test
    void getBorrowingTrends() {
        Map<String, Long> trends = Map.of("2025-06-01", 5L, "2025-06-02", 3L);
        when(borrowingRepository.getBorrowingTrendsBetweenDates(any(), any())).thenReturn(trends);

        Map<String, Long> result = analyticsService.getBorrowingTrends(LocalDate.now().minusDays(1), LocalDate.now());

        assertEquals(trends, result);
    }

    @Test
    void getMemberActivityReport() {
        List<Map<String, Object>> report = List.of(Map.of("memberId", 1, "borrowCount", 10), Map.of("memberId", 2, "borrowCount", 5));
        when(memberRepository.getMemberActivityReport()).thenReturn(report);

        List<Map<String, Object>> result = analyticsService.getMemberActivityReport();

        assertEquals(report, result);
    }
}
