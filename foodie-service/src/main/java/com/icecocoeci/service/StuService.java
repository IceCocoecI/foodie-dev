package com.icecocoeci.service;

import com.icecocoeci.pojo.Stu;

/**
 * Created with IntelliJ IDEA.
 *
 * @auther wlf
 * @Date: 2022/06/08/10:14
 * @Description:
 */
public interface StuService {
    public Stu getStuInfo(int id);

    public void saveStu();

    public void updateStu(int id);

    public void deleteStu(int id);

    public void saveParent();
    public void saveChildren();
}
