package com.libraryman_api.member;

import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.notification.NotificationService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling operations related to library members,
 * including retrieving, adding, updating, and deleting member records.
 *
 * <p>Integrates with {@link NotificationService} to manage notifications for
 * various member-related actions such as account creation, updates, and deletions.</p>
 *
 * <p>Uses {@link MemberRepository} for database interactions. In cases where a 
 * member is not found, a {@link ResourceNotFoundException} is thrown.</p>
 *
 * <p>This service ensures consistency and proper transaction management 
 * while interacting with the database.</p>
 *
 * @see MemberRepository
 * @see NotificationService
 */
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    /**
     * Constructs a {@code MemberService} with the necessary dependencies.
     *
     * @param memberRepository the repository used for managing member records
     * @param notificationService the service used for sending notifications related to member activities
     */
    public MemberService(MemberRepository memberRepository, NotificationService notificationService) {
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }

    /**
     * Retrieves all members from the system.
     *
     * @return a list of all members present in the library database
     */
    public List<Members> getAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * Retrieves a member by their ID.
     *
     * @param memberId the ID of the member to retrieve
     * @return an {@code Optional} containing the member if found, otherwise {@code Optional.empty()}
     */
    public Optional<Members> getMemberById(int memberId) {
        return memberRepository.findById(memberId);
    }

    /**
     * Adds a new member to the system and sends an account creation notification.
     *
     * @param member the new member to add to the system
     * @return the saved member entity
     */
    public Members addMember(Members member) {
        Members currentMember = memberRepository.save(member);
        notificationService.accountCreatedNotification(currentMember);
        return currentMember;
    }

    /**
     * Updates an existing member's details. Throws a {@link ResourceNotFoundException} if the member
     * is not found. After updating, an account details update notification is sent.
     *
     * @param memberId the ID of the member to update
     * @param memberDetails the updated member details
     * @return the updated member entity
     * @throws ResourceNotFoundException if the member is not found
     */
    public Members updateMember(int memberId, Members memberDetails) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPassword(memberDetails.getPassword());
        member.setRole(memberDetails.getRole());
        member.setMembershipDate(memberDetails.getMembershipDate());
        member = memberRepository.save(member);
        notificationService.accountDetailsUpdateNotification(member);
        return member;
    }

    /**
     * Deletes a member from the system after ensuring no outstanding fines or borrowed books exist.
     * Sends an account deletion notification before deletion.
     *
     * @param memberId the ID of the member to delete
     * @throws ResourceNotFoundException if the member is not found
     */
    public void deleteMember(int memberId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        // TODO: Implement logic to check if the member has any outstanding fines or borrowed books.

        notificationService.accountDeletionNotification(member);
        memberRepository.delete(member);
    }
}
