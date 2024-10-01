package com.libraryman_api.member;

import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.notification.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing member-related operations in the LibraryMan system.
 *
 * <p>This service provides methods for retrieving, adding, updating, and deleting member records.
 * It integrates with the {@link NotificationService} to send notifications related to member
 * activities such as account creation, updates, and deletions.</p>
 *
 * <p>Each method interacts with the {@link MemberRepository} to perform database operations, ensuring
 * proper transactional behavior and consistency.</p>
 *
 * <p>In cases where a member record is not found, the service throws a
 * {@link ResourceNotFoundException} to indicate the error.</p>
 *
 * @author Ajay Negi
 */
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    /**
     * Constructs a new {@code MemberService} with the specified repositories and services.
     *
     * @param memberRepository the repository for managing member records
     * @param notificationService the service for sending notifications related to member activities
     */
    public MemberService(MemberRepository memberRepository, NotificationService notificationService) {
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }

    /**
     * Retrieves all members from the database.
     *
     * @return a list of all members
     */
    public List<Members> getAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * Retrieves a member record by its ID.
     *
     * @param memberId the ID of the member to retrieve
     * @return an {@code Optional} containing the found member, or {@code Optional.empty()} if no member was found
     * @throws ResourceNotFoundException if no member with the specified ID is found
     */
    public Members getMemberById(int memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    /**
     * Adds a new member to the library system.
     *
     * <p>This method saves the new member record in the database and sends a notification
     * about the account creation.</p>
     *
     * @param member the member details to be added
     * @return the saved member record
     */
    public Members addMember(Members member) {
        Members savedMember = memberRepository.save(member);
        notificationService.accountCreatedNotification(savedMember);
        return savedMember;
    }

    /**
     * Updates an existing member's details.
     *
     * <p>This method updates the member's details in the database. It throws a
     * {@link ResourceNotFoundException} if the member is not found. After updating,
     * a notification about the account details update is sent.</p>
     *
     * @param memberId the ID of the member to update
     * @param memberDetails the updated member details
     * @return the updated member record
     * @throws ResourceNotFoundException if the member is not found
     */
    public Members updateMember(int memberId, Members memberDetails) {
        Members existingMember = getMemberById(memberId);
        updateMemberDetails(existingMember, memberDetails);
        Members updatedMember = memberRepository.save(existingMember);
        notificationService.accountDetailsUpdateNotification(updatedMember);
        return updatedMember;
    }

    /**
     * Deletes a member from the library system.
     *
     * <p>This method deletes the member record from the database after ensuring that
     * the member has no outstanding fines or borrowed books. Before deletion, it
     * sends a notification about the account deletion.</p>
     *
     * @param memberId the ID of the member to delete
     * @throws ResourceNotFoundException if the member is not found
     */
    public void deleteMember(int memberId) {
        Members member = getMemberById(memberId);

        // TODO: Implement logic to check if the member has any outstanding fines or borrowed books.
        // If there are no pending obligations, delete all related notifications, borrowings, and fines.

        notificationService.accountDeletionNotification(member);
        memberRepository.delete(member);
    }

    /**
     * Updates the details of an existing member.
     *
     * @param existingMember the member to update
     * @param newDetails the new member details to apply
     */
    private void updateMemberDetails(Members existingMember, Members newDetails) {
        existingMember.setName(newDetails.getName());
        existingMember.setEmail(newDetails.getEmail());
        existingMember.setPassword(newDetails.getPassword());
        existingMember.setRole(newDetails.getRole());
        existingMember.setMembershipDate(newDetails.getMembershipDate());
    }
}
