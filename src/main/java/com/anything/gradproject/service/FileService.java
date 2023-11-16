package com.anything.gradproject.service;

import com.anything.gradproject.entity.FileEntity;
import com.anything.gradproject.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {


    @Value("${file.dir:${user.home}}")
    private String fileDir;

    public String saveFile(MultipartFile files, String fileName) throws IOException {
        if (files.isEmpty()) {
            return "file이 없습니다.";
        }

        // 원래 파일 이름 추출
        String origName = files.getOriginalFilename();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        String savedName = " ";
        // 파일 이름 정하기
        savedName = fileName + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 파일 엔티티 생성
        FileEntity file = FileEntity.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build();

        // 실제로 로컬에 uuid를 파일명으로 저장
        files.transferTo(new File(savedPath));

        return savedName;
    }

    public String saveFile2(MultipartFile file) { // 파일을 받아 지정된 경로에 저장하고 경로를 String으로 반환
        if (file.isEmpty()) {
            return "file이 없습니다.";
        }
        // 파일 이름 추출, 변경 후 확장자와 함께 저장
        String randomName = UUID.randomUUID().toString();
        String originName = file.getOriginalFilename();
        String extension = originName.substring(originName.lastIndexOf("."));
        String saveFilePath = fileDir + randomName + extension;
        //
        try {
            File saveFile = new File(saveFilePath);
            file.transferTo(saveFile);
            return randomName + extension;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드 중 오류가 발생했스빈다.", e);
        }

    }


    public boolean removeFile(String fileName) {
         return FileSystemUtils.deleteRecursively((new File(fileDir + fileName)));
    }
}