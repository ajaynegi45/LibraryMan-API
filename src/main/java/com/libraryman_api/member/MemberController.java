package com.libraryman_api.member;

import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.member.dto.MembersDto;
import com.libraryman_api.member.dto.UpdateMembersDto;
import com.libraryman_api.member.dto.UpdatePasswordDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
     * Retrieves a paginated and sorted list of all library members.
     *
     * @param pageable contains pagination information (page number, size, and sorting).
     * @param sortBy   (optional) the field by which to sort the results.
     * @param sortDir  (optional) the direction of sorting (asc or desc). Defaults to ascending.
     * @return a {@link Page} of {@link Members} representing all members in the library.
     * The results are sorted by name by default and limited to 5 members per page.
     */
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public Page<MembersDto> getAllMembers(@PageableDefault(page = 0, size = 5, sort = "name") Pageable pageable,
                                          @RequestParam(required = false) String sortBy,
                                          @RequestParam(required = false) String sortDir) {

        // Adjust the pageable based on dynamic sorting parameters
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = Sort.Direction.ASC; // Default direction

            if (sortDir != null && sortDir.equalsIgnoreCase("desc")) {
                direction = Sort.Direction.DESC;
            }

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(direction, sortBy));
        }

        return memberService.getAllMembers(pageable);
    }

    /**
     * Retrieves a library member by their ID.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.
     *
     * @param id the ID of the member to retrieve
     * @return a {@link ResponseEntity} containing the found {@link Members} object
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public ResponseEntity<MembersDto> getMemberById(@PathVariable int id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    /**
     * Updates an existing library member.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.
     *
     * @param id                the ID of the member to update
     * @param membersDtoDetails the {@link Members} object containing the updated details
     * @return the updated {@link Members} object
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.memberId)")
    public MembersDto updateMember(@PathVariable int id, @Valid @RequestBody UpdateMembersDto membersDtoDetails) {
        return memberService.updateMember(id, membersDtoDetails);
    }

    /**
     * Deletes a library member by their ID.
     * If the member is not found, a {@link ResourceNotFoundException} is thrown.
     *
     * @param id the ID of the member to delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN') or hasRole('ADMIN')")
    public void deleteMember(@PathVariable int id) {
        memberService.deleteMember(id);
    }

    /**
     * Updates the password for a library member.
     * If the member is not found or the update fails, an appropriate exception will be thrown.
     *
     * @param id                the ID of the member whose password is to be updated
     * @param updatePasswordDto the {@link UpdatePasswordDto} object containing the password details
     * @return a {@link ResponseEntity} containing a success message indicating the password was updated successfully
     */
    @PutMapping("/{id}/password")
    @PreAuthorize("#id == authentication.principal.memberId")
    public ResponseEntity<?> updatePassword(@PathVariable int id,
                                            @Valid @RequestBody UpdatePasswordDto updatePasswordDto) {
        memberService.updatePassword(id, updatePasswordDto);
        return ResponseEntity.ok("Password updated successfully.");
    }

}
