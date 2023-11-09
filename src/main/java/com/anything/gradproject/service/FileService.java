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
            return "Error";
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

    public String saveFile2(MultipartFile file) { // 파일을 받아 지정된 경로에 저장하고 경로를 String으로 반환, 중복시 파일 덮어쓰기.

        Path copyOfLocation = Paths.get(fileDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return e.getMessage();
        }

        return copyOfLocation.toString();
    }


    public boolean removeFile(String fileName) {
         return FileSystemUtils.deleteRecursively((new File(fileDir + fileName)));
    }
}