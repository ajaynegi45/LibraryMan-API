package com.libraryman_api.controller;

import com.libraryman_api.entity.Members;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public List<Members> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Members> getMemberById(@PathVariable int id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
    }

    @PostMapping
    public Members addMember(@RequestBody Members member) {
        return memberService.addMember(member);
    }

    @PutMapping("/{id}")
    public Members updateMember(@PathVariable int id, @RequestBody Members memberDetails) {
        return memberService.updateMember(id, memberDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable int id) {
        memberService.deleteMember(id);
    }
}

