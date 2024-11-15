package com.libraryman_api.security.services;

import com.libraryman_api.exception.ResourceNotFoundException;
import com.libraryman_api.member.MemberRepository;
import com.libraryman_api.member.Members;
import com.libraryman_api.member.Role;
import com.libraryman_api.security.config.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class SignupService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    public SignupService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(Members members) {
        Optional<Members> memberOptId = memberRepository.findById(members.getMemberId());
        Optional<Members> memberOptUsername = memberRepository.findByUsername(members.getUsername());
        if (memberOptId.isPresent()) {
            throw new ResourceNotFoundException("User already Exists");
        }
        if (memberOptUsername.isPresent()) {
            throw new ResourceNotFoundException("User already Exists");
        }
        String encoded_password = passwordEncoder.bCryptPasswordEncoder().encode(members.getPassword());
        Members new_members = new Members();

//      TODO: check for proper username format
        new_members.setUsername(members.getUsername());

        new_members.setName(members.getName());

//      TODO: check for proper email format
        new_members.setEmail(members.getEmail());

        new_members.setRole(Role.USER);
        new_members.setMembershipDate(new Date());
        new_members.setPassword(encoded_password);
        memberRepository.save(new_members);
    }

    public void signupAdmin(Members members) {
        Optional<Members> memberOptId = memberRepository.findById(members.getMemberId());
        Optional<Members> memberOptUsername = memberRepository.findByUsername(members.getUsername());
        if (memberOptId.isPresent()) {
            throw new ResourceNotFoundException("User already Exists");
        }
        if (memberOptUsername.isPresent()) {
            throw new ResourceNotFoundException("User already Exists");
        }

        String encoded_password = passwordEncoder.bCryptPasswordEncoder().encode(members.getPassword());
        Members new_members = new Members();
        new_members.setEmail(members.getEmail());
        new_members.setName(members.getName());
        new_members.setPassword(encoded_password);
        new_members.setRole(Role.ADMIN);
        new_members.setMembershipDate(new Date());
        new_members.setUsername(members.getUsername());
        memberRepository.save(new_members);

    }

    public void signupLibrarian(Members members) {
        Optional<Members> memberOptId = memberRepository.findById(members.getMemberId());
        Optional<Members> memberOptUsername = memberRepository.findByUsername(members.getUsername());
        if (memberOptId.isPresent()) {
            throw new ResourceNotFoundException("User already Exists");
        }
        if (memberOptUsername.isPresent()) {
            throw new ResourceNotFoundException("User already Exists");
        }
        String encoded_password = passwordEncoder.bCryptPasswordEncoder().encode(members.getPassword());
        Members new_members = new Members();
        new_members.setEmail(members.getEmail());
        new_members.setName(members.getName());
        new_members.setPassword(encoded_password);
        new_members.setRole(Role.LIBRARIAN);
        new_members.setMembershipDate(new Date());
        new_members.setUsername(members.getUsername());
        memberRepository.save(new_members);
    }
}