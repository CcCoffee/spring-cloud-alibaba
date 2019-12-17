package com.study.contentcenter.controller;

import com.study.contentcenter.domain.dto.content.ShareDTO;
import com.study.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    @GetMapping("/{id}")
    @ResponseBody
    public ShareDTO findById(@PathVariable(name = "id") Integer id){
        return shareService.findById(id);
    }
}
