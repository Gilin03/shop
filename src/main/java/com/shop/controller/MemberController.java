package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;  // <- 수정된 부분
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.shop.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

import java.util.List;


@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @GetMapping("/manage")
    public String manageMembers(Model model) {
        List<Member> members = memberService.findAllMembers();
        model.addAttribute("members", members);
        return "member/manageMembers";
    }

    @GetMapping("/mypage")
    public String myPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            Member member = memberService.findByEmail(user.getUsername());
            model.addAttribute("member", member);
        }
        return "member/myPage";
    }

    @GetMapping("/edit/{id}")
    public String editMemberForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id);
        model.addAttribute("member", member);
        return "member/editMember";
    }

    // 회원 정보 수정 처리
    @PostMapping("/update")
    public String updateMember(@ModelAttribute Member member,
                               String currentPassword,
                               String password,
                               String confirmPassword,
                               Model model) {

        Member existingMember = memberService.findById(member.getId());

        // 현재 비밀번호 확인 로직
        if (!passwordEncoder.matches(currentPassword, existingMember.getPassword())) {
            model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            model.addAttribute("member", member); // 원래의 멤버 정보를 다시 모델에 추가
            return "member/editMember";
        }

        // 새 비밀번호와 비밀번호 확인 일치 여부 확인 로직
        if (password != null && !password.isEmpty()) {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다.");
                model.addAttribute("member", member); // 원래의 멤버 정보를 다시 모델에 추가
                return "member/editMember";
            }
            String encodedPassword = passwordEncoder.encode(password);
            member.setPassword(encodedPassword);
        }

        try {
            memberService.updateMember(member);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("member", member); // 원래의 멤버 정보를 다시 모델에 추가
            return "member/editMember";
        }

        return "redirect:/members/manage";
    }

    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/members/manage";
    }



}