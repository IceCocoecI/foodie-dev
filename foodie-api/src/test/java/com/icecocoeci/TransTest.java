package com.icecocoeci;

import com.icecocoeci.service.StuService;
import com.icecocoeci.service.TestTransService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created with IntelliJ IDEA.
 *
 * @auther wlf
 * @Date: 2022/06/09/17:24
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodieApplication.class)
public class TransTest {

    @Autowired
    private StuService stuService;

    @Autowired
    private TestTransService testTransService;

    @Test
    public void myTest() {
//        stuService.testPropagationTrans();
        testTransService.testPropagationTrans();
    }

}
