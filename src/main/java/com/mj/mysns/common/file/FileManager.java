package com.mj.mysns.common.file;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileManager {

    private final FileRepository fileRepository;

    public Optional<FileLocation> saveFile(MultipartFile file) {
        return fileRepository.saveFile(file);
    }

    public List<FileLocation> saveFile(List<? extends MultipartFile> files) {
        List<FileLocation> succeed = files.stream()
            .map(this::saveFile)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();

        if (succeed.size() != files.size()) {
            log.info("파일 저장 실패, {} / {}개 중 ",
                files.size() - succeed.size(),
                files.size());
        }

        return succeed;
    }

    public FileUrl getFileUrl(FileLocation location) { return fileRepository.getFileUrl(location); }
}
