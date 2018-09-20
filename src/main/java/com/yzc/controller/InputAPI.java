package com.yzc.controller;

import com.yzc.servers.ESDataInput;
import com.yzc.servers.InputPinyin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wei.wang on 2018/9/12 0012.
 */
@RestController
@RequestMapping(value = "/notify/data")
public class InputAPI {

    @Autowired
    ESDataInput esDataInput;

    @Autowired
    InputPinyin inputPinyin;

    @ResponseBody
    @RequestMapping(value = "/input", method = RequestMethod.POST)
    public void test(){
//        esDataInput.input();
        inputPinyin.inputData();
    }


}
