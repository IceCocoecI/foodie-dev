package com.icecocoeci.mapper;

import com.icecocoeci.pojo.Stu;
import com.icecocoeci.utils.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//@Mapper
@Repository
public interface StuMapper extends MyMapper<Stu> {
}