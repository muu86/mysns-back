package com.mj.mysns.common;

import com.querydsl.jpa.Hibernate5Templates;
import com.querydsl.sql.SQLOps;

public class CustomizedQuerydslTemplate extends Hibernate5Templates {

    public CustomizedQuerydslTemplate() {
        add(SQLOps.ROWNUMBER, "row_number()");
    }
}
