package com.mj.mysns.common.file;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class FileLocation {

    private String key;

    @Enumerated(EnumType.STRING)
    private FileLocationType type;

    public enum FileLocationType {
        S3
    }
}
