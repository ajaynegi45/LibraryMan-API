package com.libraryman_api.service;

import com.libraryman_api.entity.Members;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public List<Members> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Members> getMemberById(int memberId) {
        return memberRepository.findById(memberId);
    }

    public Members addMember(Members member) {
        return memberRepository.save(member);
    }

    public Members updateMember(int memberId, Members memberDetails) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPassword(memberDetails.getPassword());
        member.setRole(memberDetails.getRole());
        member.setMembershipDate(memberDetails.getMembershipDate());
        return memberRepository.save(member);
    }

    public void deleteMember(int memberId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        memberRepository.delete(member);
    }
}

