package com.yuanliubei.hotchpotch.controller;

import com.yuanliubei.hotchpotch.common.PageResult;
import com.yuanliubei.hotchpotch.frame.Result;
import com.yuanliubei.hotchpotch.model.dto.ShopCreateDTO;
import com.yuanliubei.hotchpotch.model.dto.ShopUpdateDTO;
import com.yuanliubei.hotchpotch.model.query.ShopQuery;
import com.yuanliubei.hotchpotch.model.vo.ShopVO;
import com.yuanliubei.hotchpotch.service.IShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Tag(name = "店铺接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("shop")
public class ShopController {

    private final IShopService shopService;

    @Operation(summary = "新增")
    @PostMapping("/create")
    public Result<Void> create(@RequestBody ShopCreateDTO dto){
        shopService.create(dto);
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Void> update(@RequestBody ShopUpdateDTO dto){
        shopService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "查询")
    @PostMapping("/query")
    public Result<PageResult<ShopVO>> query(@RequestBody ShopQuery query){
        PageResult<ShopVO> pageResult = shopService.query(query);
        return Result.ok(pageResult);
    }

    @PostMapping("/delete/{id}")
    public Result<Void> delete(@PathVariable(name = "id") Long id){
        shopService.logicDel(id);
        return Result.ok();
    }

    @PostMapping("/deleteByName")
    public Result<Void> deleteByName(@RequestParam String name) {
        shopService.deleteByName(name);
        return Result.ok();
    }
}
