package com.mj.mysns.common.file;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface FileRepository {

    Optional<FileLocation> saveFile(MultipartFile file);

    FileUrl getFileUrl(FileLocation location);
}