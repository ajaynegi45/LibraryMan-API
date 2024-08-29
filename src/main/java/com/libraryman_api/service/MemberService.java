package com.libraryman_api.service;

import com.libraryman_api.entity.Members;
import com.libraryman_api.entity.Notifications;
import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.repository.MemberRepository;
import com.libraryman_api.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    public MemberService(MemberRepository memberRepository, NotificationService notificationService) {
        this.memberRepository = memberRepository;
        this.notificationService = notificationService;
    }

    public List<Members> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Members> getMemberById(int memberId) {
        return memberRepository.findById(memberId);
    }

    public Members addMember(Members member) {
       Members CurrentMember =  memberRepository.save(member);
        notificationService.accountCreatedNotification(CurrentMember);

        return CurrentMember;
    }

    public Members updateMember(int memberId, Members memberDetails) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));
        member.setName(memberDetails.getName());
        member.setEmail(memberDetails.getEmail());
        member.setPassword(memberDetails.getPassword());
        member.setRole(memberDetails.getRole());
        member.setMembershipDate(memberDetails.getMembershipDate());
        member =  memberRepository.save(member);
        notificationService.accountDetailsUpdateNotification(member);
        return member;
    }

    public void deleteMember(int memberId) {
        Members member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        // write logic to check if there is any fine or any book to return. If there is nothing, then delete all notification, borrow, fine
        notificationService.accountDeletionNotification(member);
        memberRepository.delete(member);
    }
}

