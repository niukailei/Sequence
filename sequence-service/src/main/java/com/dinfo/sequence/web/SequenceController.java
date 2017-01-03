package com.dinfo.sequence.web;

import com.dinfo.common.model.Response;
import com.dinfo.sequence.dto.SequenceId;
import com.dinfo.sequence.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * Created by winston.wang on 2017/1/2.
 */
@RestController
public class SequenceController {

    @Autowired
    private GeneratorService generatorService;

    /**
     * 批量生成序列号
     * @param batchSize 生成数量
     * @return 结果
     */
    @RequestMapping(value = "/generateIdBatch", method = RequestMethod.GET)
    public Response<List<SequenceId>> generateIdBatch(@RequestParam long batchSize) {
        return generatorService.generateIdBatch(batchSize);
    }
    /**
     * 生成序列号(long)
     * @return 结果
     */
    @RequestMapping(value = "/generateId", method = RequestMethod.GET)
    public Response<SequenceId> generateId() {
        return generatorService.generateId();
    }

    /**
     * 批量生成序列号(date)
     * @return 结果
     */
    @RequestMapping(value = "/generateDateStrIdBatch", method = RequestMethod.GET)
    public Response<List<SequenceId>> generateDateStrIdBatch(@RequestParam String keyName,@RequestParam int keySize,@RequestParam long batchSize) {
        return generatorService.generateDateStrIdBatch(keyName,keySize,batchSize);
    }

    /**
     * 生成序列号(date)
     * @return 结果
     */
    @RequestMapping(value = "/generateDateStrId", method = RequestMethod.GET)
    public Response<SequenceId> generateDateStrId(@RequestParam String keyName,@RequestParam int keySize) {
        return generatorService.generateDateStrId(keyName,keySize);
    }
}
