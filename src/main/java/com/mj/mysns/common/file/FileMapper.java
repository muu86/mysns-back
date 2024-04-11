package com.mj.mysns.common.file;

import com.mj.mysns.post.entity.PostFile;
import com.mj.mysns.user.entity.UserFile;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class FileMapper {

    @Mapping(target = "url", ignore = true)
    @Mapping(target = "location", source = "file.location")
    @Mapping(target = "status", source = "file.status")
    public abstract FileDto userFileToFileDto(UserFile userFile);

    public abstract List<FileDto> userFileToFileDto(List<UserFile> userFiles,
        @Context FileManager fileManager);

    @Mapping(target = "url", ignore = true)
    @Mapping(target = "location", source = "file.location")
    @Mapping(target = "status", source = "file.status")
    public abstract FileDto postFileToFileDto(PostFile postFile);
}
