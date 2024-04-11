package com.mj.mysns.common.file;

import com.mj.mysns.post.entity.PostFile;
import com.mj.mysns.user.entity.UserFile;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T03:29:59+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class FileMapperImpl extends FileMapper {

    @Override
    public FileDto userFileToFileDto(UserFile userFile) {
        if ( userFile == null ) {
            return null;
        }

        FileDto.FileDtoBuilder fileDto = FileDto.builder();

        fileDto.location( userFileFileLocation( userFile ) );
        fileDto.status( userFileFileStatus( userFile ) );

        return fileDto.build();
    }

    @Override
    public List<FileDto> userFileToFileDto(List<UserFile> userFiles, FileManager fileManager) {
        if ( userFiles == null ) {
            return null;
        }

        List<FileDto> list = new ArrayList<FileDto>( userFiles.size() );
        for ( UserFile userFile : userFiles ) {
            list.add( userFileToFileDto( userFile ) );
        }

        return list;
    }

    @Override
    public FileDto postFileToFileDto(PostFile postFile) {
        if ( postFile == null ) {
            return null;
        }

        FileDto.FileDtoBuilder fileDto = FileDto.builder();

        fileDto.location( postFileFileLocation( postFile ) );
        fileDto.status( postFileFileStatus( postFile ) );

        return fileDto.build();
    }

    private FileLocation userFileFileLocation(UserFile userFile) {
        if ( userFile == null ) {
            return null;
        }
        File file = userFile.getFile();
        if ( file == null ) {
            return null;
        }
        FileLocation location = file.getLocation();
        if ( location == null ) {
            return null;
        }
        return location;
    }

    private File.Status userFileFileStatus(UserFile userFile) {
        if ( userFile == null ) {
            return null;
        }
        File file = userFile.getFile();
        if ( file == null ) {
            return null;
        }
        File.Status status = file.getStatus();
        if ( status == null ) {
            return null;
        }
        return status;
    }

    private FileLocation postFileFileLocation(PostFile postFile) {
        if ( postFile == null ) {
            return null;
        }
        File file = postFile.getFile();
        if ( file == null ) {
            return null;
        }
        FileLocation location = file.getLocation();
        if ( location == null ) {
            return null;
        }
        return location;
    }

    private File.Status postFileFileStatus(PostFile postFile) {
        if ( postFile == null ) {
            return null;
        }
        File file = postFile.getFile();
        if ( file == null ) {
            return null;
        }
        File.Status status = file.getStatus();
        if ( status == null ) {
            return null;
        }
        return status;
    }
}
