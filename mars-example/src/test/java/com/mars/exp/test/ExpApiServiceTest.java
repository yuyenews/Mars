package com.mars.exp.test;

import com.mars.common.annotation.bean.MarsWrite;
import com.mars.common.annotation.junit.MarsTest;
import com.mars.common.base.config.MarsConfig;
import com.mars.exp.ExpStart;
import com.mars.exp.api.vo.ExpVO;
import com.mars.exp.core.service.ExpApiService;
import com.mars.junit.MarsJunit;
import org.junit.Test;

import java.util.List;

/**
 * 单元测试
 */
@MarsTest(startClass = ExpStart.class)
public class ExpApiServiceTest extends MarsJunit {

    @MarsWrite("expApiService")
    private ExpApiService expApiService;

    /**
     * 返回配置文件
     * @return
     */
    @Override
    public MarsConfig getMarsConfig() {
        return new ExpTestConfig();
    }

    @Test
    public void testExpPostRequest(){
        ExpVO expVO = new ExpVO();
        expVO.setName("单测");
        expVO.setNames(new String[]{"单测1","单测2"});
        List<ExpVO> list = expApiService.expPostRequest(expVO);

        // 这只是一个示例，演示一下单测怎么用，所以就偷个懒直接打印了，实际是要对返回值做校验的
        System.out.println(list);
    }
}
