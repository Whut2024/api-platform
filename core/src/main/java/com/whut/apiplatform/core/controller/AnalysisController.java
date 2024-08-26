package com.whut.apiplatform.core.controller;

import com.whut.apiplatform.model.vo.InterfaceInfoVo;
import com.whut.webs.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author whut2024
 * @since 2024-08-25
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {


    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVo>> listTopInvokedInterfaceInfo() {

        return null;
    }
}
