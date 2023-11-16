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
import org.springframework.web.multipart.MultipartFile;

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
        String saveFileName = fileService.saveFile2(dto.getFile());
        dto.setMember(member);
        TeacherDetail teacherDetail = dto.toEntity(dto, member, saveFileName);
        teacherDetailRepository.save(teacherDetail);
    }

    @Transactional
    public void setTeacherDetail(TeacherDenyDto dto) {
        TeacherDetail teacherDetail = teacherDetailRepository.findById(dto.getTeacherDetailSeq()).orElseThrow(() -> new IllegalArgumentException("not found: " + dto.getTeacherDetailSeq()));
        if (dto.getStatus() == 0) {
            teacherDetail.getMember().setTeacherStatus(TeacherStatus.APPROVE);
        } else if (dto.getStatus() == 1) {
            teacherDetail.getMember().setTeacherStatus(TeacherStatus.REFUSE);
            teacherDenyRepository.save(TeacherDeny.builder()
                    .teacherDetail(teacherDetail)
                    .denyReason(dto.getReason())
                    .build());
        }
    }

    public TeacherDetailResponseDto getMyTeacherDetail(long userSeq) {
        TeacherDetail teacherDetail = teacherDetailRepository.findByMember_UserSeq(userSeq).orElseThrow(() -> new EntityNotFoundException("강사정보 등록을 하지 않았습니다."));
        return TeacherDetailResponseDto.toDto(teacherDetail);
    }
    public List<TeacherDetailResponseDto> getAllTeacherDetail() { // 어드민용
        return teacherDetailRepository.findAll().stream().map(TeacherDetailResponseDto::toDto).collect(Collectors.toList());
    }
}