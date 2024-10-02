package com.libraryman_api.member;

import com.libraryman_api.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling CRUD operations related to library members.
 *
 * <p>This controller provides API endpoints for retrieving, creating, updating,
 * and deleting library members. It interacts with {@link MemberService} to
 * manage these operations and uses standard HTTP responses for success or error scenarios.</p>
 *
 * @see MemberService
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * Constructs a new {@code MemberController} with the specified {@link MemberService}.
     *
     * @param memberService the service responsible for managing member-related operations
     */
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Retrieves a list of all members in the library system.
     *
     * <p>This method handles HTTP GET requests and returns a list of all members.</p>
     *
     * @return a list of {@link Members} representing all members in the system
     */
    @GetMapping
    public List<Members> getAllMembers() {
        return memberService.getAllMembers();
    }

    /**
     * Retrieves a member by their ID.
     *
     * <p>This method handles HTTP GET requests with a member ID as a path variable.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param id the ID of the member to retrieve
     * @return a {@link ResponseEntity} containing the member data if found
     * @throws ResourceNotFoundException if the member with the given ID does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Members> getMemberById(@PathVariable int id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    /**
     * Adds a new member to the library system.
     *
     * <p>This method handles HTTP POST requests to create a new member record.</p>
     *
     * @param member the {@link Members} object representing the new member
     * @return the newly added {@link Members} object
     */
    @PostMapping
    public Members addMember(@RequestBody Members member) {
        return memberService.addMember(member);
    }

    /**
     * Updates the details of an existing member.
     *
     * <p>This method handles HTTP PUT requests to update a member's details.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param id the ID of the member to update
     * @param memberDetails the {@link Members} object containing the updated details
     * @return the updated {@link Members} object
     * @throws ResourceNotFoundException if the member with the given ID does not exist
     */
    @PutMapping("/{id}")
    public Members updateMember(@PathVariable int id, @RequestBody Members memberDetails) {
        return memberService.updateMember(id, memberDetails);
    }

    /**
     * Deletes a member by their ID.
     *
     * <p>This method handles HTTP DELETE requests to remove a member from the system.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.</p>
     *
     * @param id the ID of the member to delete
     * @throws ResourceNotFoundException if the member with the given ID does not exist
     */
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable int id) {
        memberService.deleteMember(id);
    }
}
