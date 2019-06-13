package com.huxin.miaosha.controller;

import com.huxin.miaosha.domain.User;
import com.huxin.miaosha.rabbitmq.MQSender;
import com.huxin.miaosha.redis.RedisService;
import com.huxin.miaosha.redis.UserKey;
import com.huxin.miaosha.result.CodeMsg;
import com.huxin.miaosha.result.Result;
import com.huxin.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    @RequestMapping("/mq/header")
    @ResponseBody
    public Result<String> header(){
        sender.sendHeader("hello, imooc");
        return Result.success("Hello , world");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout(){
        sender.sendFanout("hello, imooc");
        return Result.success("Hello , world");
    }

    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic(){
        sender.sendTopic("hello, imooc");
        return Result.success("Hello , world");
    }

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq(){
        sender.send("hello, imooc");
        return Result.success("Hello , world");
    }

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Huxin");
        return "hello";
    }

    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }

    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<String> redisGet() {
        String str  = redisService.get(UserKey.getById, ""+1, String.class);
        return Result.success(str);
    }

    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<String> redisSet() {
        boolean ret = redisService.set(UserKey.getById,""+1,"hello,imooc");
        String v1  = redisService.get(UserKey.getById, ""+1, String.class);
        return Result.success(v1);
    }

}
