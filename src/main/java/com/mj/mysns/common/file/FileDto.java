package com.mj.mysns.common.file;

import com.mj.mysns.common.file.File.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class FileDto {

    @Setter
    private FileUrl url;

    private FileLocation location;

    private Status status;
}
