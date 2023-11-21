package com.anything.gradproject.service;

import com.anything.gradproject.constant.TeacherStatus;
import com.anything.gradproject.dto.TeacherDenyDto;
import com.anything.gradproject.dto.TeacherDetailFormDto;
import com.anything.gradproject.dto.TeacherDetailResponseDto;
import com.anything.gradproject.entity.Member;
import com.anything.gradproject.entity.TeacherDeny;
import com.anything.gradproject.entity.TeacherDetail;
import com.anything.gradproject.repository.MemberRepository;
import com.anything.gradproject.repository.TeacherDenyRepository;
import com.anything.gradproject.repository.TeacherDetailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherDetailService {

    private final TeacherDetailRepository teacherDetailRepository;
    private final FileService fileService;
    private final TeacherDenyRepository teacherDenyRepository;
    private final MemberRepository memberRepository;

    public void saveTeacherDetail(TeacherDetailFormDto dto, Member member) throws IOException {
        String saveFileName = fileService.saveFileImg(dto.getFile());
        dto.setMember(member);
        TeacherDetail teacherDetail = dto.toEntity(dto, member, saveFileName);
        teacherDetailRepository.save(teacherDetail);
    }

    @Transactional
    public void setTeacherDetail(TeacherDenyDto dto) {
        TeacherDetail teacherDetail = teacherDetailRepository.findById(dto.getTeacherDetailSeq()).orElseThrow(() -> new IllegalArgumentException("not found: " + dto.getTeacherDetailSeq()));
        if (dto.getStatus() == 0) {
            teacherDetail.getMember().setTeacherStatus(TeacherStatus.APPROVE);
            if (teacherDetail.getTeacherDeny() != null) {
                teacherDenyRepository.delete(teacherDenyRepository.findByDenySeq(teacherDetail.getTeacherDeny().getDenySeq()).get());
                teacherDetailRepository.delete(teacherDetail);
            }
        } else if (dto.getStatus() == 1) {
            teacherDetail.getMember().setTeacherStatus(TeacherStatus.REFUSE);
            teacherDetailRepository.delete(teacherDetail);
            teacherDenyRepository.save(TeacherDeny.builder()
                    .teacherDetail(teacherDetail)
                    .denyReason(dto.getReason())
                    .build());
        }

    }


    public TeacherDetailResponseDto getMyTeacherDetail(long userSeq) {
        TeacherDetail teacherDetail = teacherDetailRepository.findByMember_UserSeq(userSeq).orElseThrow(() -> new EntityNotFoundException("강사정보 등록을 하지 않았습니다."));
        return this.toDto(teacherDetail);
    }
    public List<TeacherDetailResponseDto> getAllTeacherDetail() { // 어드민용
        return teacherDetailRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public TeacherDetailResponseDto toDto(TeacherDetail teacherDetail) {
        TeacherDetailResponseDto dto = new TeacherDetailResponseDto();
        dto.setTeacherName(teacherDetail.getMember().getName());
        dto.setUserSeq(teacherDetail.getMember().getUserSeq());
        dto.setTeacherDetailSeq(teacherDetail.getTeacherDetailSeq());
        dto.setTeacherField(teacherDetail.getTeacherField());
        dto.setTeacherCareer(teacherDetail.getTeacherCareer());
        dto.setTeacherImg(teacherDetail.getTeacherImg());
        dto.setTeacherIntro(teacherDetail.getTeacherIntro());
        dto.setTeacherStatus(teacherDetail.getMember().getTeacherStatus());
        if (teacherDetail.getMember().getTeacherStatus() == TeacherStatus.REFUSE) {
            dto.setReason("거절 사유 : " + teacherDetail.getTeacherDeny().getDenyReason());
        } else if (teacherDetail.getMember().getTeacherStatus() == TeacherStatus.APPROVE) {
            dto.setReason("강사 신청이 승인 되었습니다.");
        } else {
            dto.setReason("승인 대기중 입니다.");
        }
        return dto;
    }
}