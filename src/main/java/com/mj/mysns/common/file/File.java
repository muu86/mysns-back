package com.mj.mysns.common.file;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Embedded
    private FileLocation location;

    @Enumerated(EnumType.STRING)
    @Setter
    private Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }

    public File(FileLocation location) {
        this(location, Status.ACTIVE);
    }

    public File(FileLocation location, Status status) {
        this.location = location;
        this.status = status;
    }

    public FileDto toDto() {
        return FileDto.builder()
            .location(location)
            .status(status)
            .build();
    }
}
