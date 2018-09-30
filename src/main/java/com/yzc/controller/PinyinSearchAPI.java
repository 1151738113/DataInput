package com.yzc.controller;

import com.yzc.search.PinyinSearch;
import com.yzc.servers.ESDataInput;
import com.yzc.servers.InputPinyin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by wei.wang on 2018/9/30 0030.
 */

//@RequestMapping(value = "/notify/data")
@Controller
public class PinyinSearchAPI {

    @Autowired
    PinyinSearch pinyinSearch;

    @Autowired
    InputPinyin inputPinyin;

    @Autowired
    ESDataInput esDataInput;

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String select(Model model, String text)
    {
        if(text != null){
            List<String> companys = pinyinSearch.search(text);
            model.addAttribute("list",companys);
        }
        return "index";
    }

    @RequestMapping(value = "/update/index", method = RequestMethod.GET)
    public String index(){
        return "update";
    }

    @RequestMapping(value = "/pinyin/input")
    public void createPinyin(){
        inputPinyin.inputData();
    }

    @RequestMapping(value = "/notice/input")
    public void noticeInput(){
        esDataInput.input();
    }



}
