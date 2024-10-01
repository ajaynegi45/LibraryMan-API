package com.libraryman_api.member;

import com.libraryman_api.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing library members.
 * This controller provides endpoints for performing CRUD operations on members.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * Constructs a new {@code MemberController} with the specified {@link MemberService}.
     *
     * @param memberService the service to handle member-related operations
     */
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * Retrieves a list of all library members.
     *
     * @return a list of {@link Members} representing all members in the library
     */
    @GetMapping
    public List<Members> getAllMembers() {
        return memberService.getAllMembers();
    }

    /**
     * Retrieves a library member by their ID.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.
     *
     * @param id the ID of the member to retrieve
     * @return a {@link ResponseEntity} containing the found {@link Members} object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Members> getMemberById(@PathVariable int id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    /**
     * Adds a new library member.
     *
     * @param member the {@link Members} object representing the new member
     * @return the added {@link Members} object
     */
    @PostMapping
    public Members addMember(@RequestBody Members member) {
        return memberService.addMember(member);
    }

    /**
     * Updates an existing library member.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.
     *
     * @param id the ID of the member to update
     * @param memberDetails the {@link Members} object containing the updated details
     * @return the updated {@link Members} object
     */
    @PutMapping("/{id}")
    public Members updateMember(@PathVariable int id, @RequestBody Members memberDetails) {
        return memberService.updateMember(id, memberDetails);
    }

    /**
     * Deletes a library member by their ID.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.
     *
     * @param id the ID of the member to delete
     */
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable int id) {
        memberService.deleteMember(id);
    }
}