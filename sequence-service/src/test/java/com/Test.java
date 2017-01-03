package com;

import com.dinfo.common.date.Dates;
import com.dinfo.common.model.Response;
import com.dinfo.sequence.dto.SequenceId;
import com.dinfo.sequence.service.Generator;
import com.dinfo.sequence.service.GeneratorService;
import com.google.common.base.Stopwatch;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by winston on 15/10/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Test.class)
public class Test {

    @Autowired
    private GeneratorService generator;
    @Autowired
    private ApplicationContext context;

    @org.junit.Test
    public void test(){
        Stopwatch stopwatch=Stopwatch.createStarted();
        int j=0;
        for(int i=0;i<1;i++){
            Response<SequenceId> response=generator.generateDateStrId("aaa",7);
            if(response.isSuccess()){
                j++;
                //System.out.println("id="+response.getData().getId());
            }
        }
        stopwatch.stop();
        System.out.println("j="+j);
        System.out.println("time="+stopwatch.elapsed(TimeUnit.MILLISECONDS)/1000);

        Date date=new Date(119701l);
        System.out.println("date=" + Dates.format(date));
    }
    @org.junit.Test
    public void testconfig(){
    }
}
