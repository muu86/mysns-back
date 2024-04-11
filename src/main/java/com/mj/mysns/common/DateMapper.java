package com.mj.mysns.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public abstract class DateMapper {

    public String toString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
