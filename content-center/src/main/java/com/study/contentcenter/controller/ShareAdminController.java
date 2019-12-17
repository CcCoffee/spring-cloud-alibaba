package com.study.contentcenter.controller;

import com.study.contentcenter.domain.dto.content.ShareAuditDTO;
import com.study.contentcenter.domain.entity.content.Share;
import com.study.contentcenter.service.content.ShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {

    private final ShareService shareService;

    @PutMapping("/audit/{id}")
    public Share audit(@PathVariable("id") Integer shareId, @RequestBody  ShareAuditDTO shareAuditDTO){

        return shareService.auditById(shareId,shareAuditDTO);
    }
}
