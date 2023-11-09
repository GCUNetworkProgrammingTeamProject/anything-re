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

        dto.setMember(member);

        TeacherDetail teacherDetail = dto.toEntity(dto, member);

        String imageName = fileService.saveFile(dto.getTeacherImg(), teacherDetail.getTeacherImg());
        teacherDetail.setTeacherImg(imageName);

        teacherDetailRepository.save(teacherDetail);
    }
    @Transactional
    public void setTeacherDetail(TeacherDenyDto dto) {
        TeacherDetail teacherDetail = teacherDetailRepository.findById(dto.getTeacherDetailSeq()).orElseThrow(() -> new IllegalArgumentException("not found: " + dto.getTeacherDetailSeq()));
        if (dto.getStatus() == 0) {
            teacherDetail.getMember().setTeacherStatus(TeacherStatus.APPROVE);
        } else if (dto.getStatus() == 1) {
            teacherDetail.getMember().setTeacherStatus(TeacherStatus.REFUSE);
            TeacherDeny teacherDeny = new TeacherDeny();
            teacherDeny.builder()
                    .teacherDetail(teacherDetail)
                    .denyReason(dto.getReason())
                    .build();
            teacherDenyRepository.save(teacherDeny);
        }
    }

    public TeacherDetailResponseDto getMyTeacherDetail(long userSeq) {
        return TeacherDetailResponseDto.toDto(teacherDetailRepository.findByMember_UserSeq(userSeq).orElseThrow(()-> new EntityNotFoundException("강사정보 등록을 하지 않았습니다.")));
    }
    public List<TeacherDetailResponseDto> getAllTeacherDetail() { // 어드민용
        return teacherDetailRepository.findAll().stream().map(TeacherDetailResponseDto::toDto).collect(Collectors.toList());
    }
}