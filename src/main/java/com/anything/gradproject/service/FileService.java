package com.anything.gradproject.service;

import com.anything.gradproject.entity.FileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileService {

    @Value("${file.dir.img}")
    private String imgDir;

    @Value("${file.dir.video}")
    private String videoDir;

    @Value("${file.dir.lectureData}")
    private String lectureDataDir;

    @Value("${file.dir.analysisVideo}")
    private String analysisDir;

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

        String fileDir = createFileDirByOriginalFileName(origName);

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

    public String saveFileImg(MultipartFile file) { // 파일을 받아 지정된 경로에 저장하고 경로를 String으로 반환
        if (file.isEmpty()) {
            return "file이 없습니다.";
        }

        try {
            // 파일 이름 추출, 변경 후 확장자와 함께 저장
            String randomName = UUID.randomUUID().toString();
            String originName = file.getOriginalFilename();
            String extension = originName.substring(originName.lastIndexOf(".")).toLowerCase();
            String fileDir = createFileDirByOriginalFileName(originName);
            String saveFilePath = fileDir + randomName + extension;

            File saveFile = new File(saveFilePath);
            file.transferTo(saveFile);
            return randomName + extension;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다.", e);
        }

    }

    public boolean removeFile(String fileName) {

        String fileDir = createFileDirByOriginalFileName(fileName);

        return FileSystemUtils.deleteRecursively((new File(fileDir + fileName)));
    }

    public String createFileDirByOriginalFileName(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();

        // 적절한 디렉토리 결정
        String fileDir;
        if (extension.equals(".pdf")) {
            fileDir = lectureDataDir;
        } else if (extension.equals(".mp4")) {
            fileDir = videoDir;
        } else if (extension.equals(".png") || extension.equals(".jpeg") || extension.equals(".jpg")) {
            fileDir = imgDir;
        } else {
            fileDir = lectureDataDir;
        }
        return fileDir;
    }

    public String saveFileAnalysis(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드 된 파일이 없습니다.");
        }
        String randomName = UUID.randomUUID().toString();
        String originName = file.getOriginalFilename();
        System.out.println(originName);
        String extension = originName.substring(originName.lastIndexOf(".")).toLowerCase();
        String fileDir = analysisDir;
        String saveFilePath = fileDir + randomName + extension;
        // 파일 이름 추출, 변경 후 확장자와 함께 저장
        try {
            File saveFile = new File(saveFilePath);
            System.out.println("집중도 분석 영상 저장 완료.");
            file.transferTo(saveFile);
            return randomName + extension;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드 중 오류가 발생했습다.", e);
        }
    }

    public void removeAnlysisVideo(String videoName) {

    }

}