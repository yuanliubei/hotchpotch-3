package com.yuanliubei.hotchpotch.controller;

import com.yuanliubei.hotchpotch.frame.Result;
import com.yuanliubei.hotchpotch.model.dao.UserRepository;
import com.yuanliubei.hotchpotch.model.domain.UserV2;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuanlb
 * @since 2023/5/30
 */
@Validated
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/getByName")
    public Result<UserV2> getByName(@RequestParam(name = "name") String name) throws IllegalAccessException {
        UserV2 user = userRepository.findByName(name);
        return Result.ok(user);
    }

    @GetMapping("/queryByName")
    public Result<UserV2> queryByName(@RequestParam(name = "name") String name) throws IllegalAccessException {
        UserV2 user = userRepository.queryByName(name);
        return Result.ok(user);
    }

    @GetMapping("/getByAge")
    public Result<List<UserV2>> getByAge(@RequestParam(name = "age") Integer age) {
        List<UserV2> users = userRepository.listByAge(age);
        return Result.ok(users);
    }

    @GetMapping("/update")
    public Result<Void> update(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "age") Integer age) {
        userRepository.update(id, age);
        return Result.ok();
    }

    @GetMapping("{id}")
    public Result<Void> getUser(@PathVariable @Pattern(regexp = "^\\d{19}$", message = "用户ID，应为19位数字") String id) {
        // 示例代码；实际业务逻辑中应该从数据库中获取数据
        return Result.ok();
    }
}
