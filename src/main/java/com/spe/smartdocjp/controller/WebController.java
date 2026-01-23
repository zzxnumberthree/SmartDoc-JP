package com.spe.smartdocjp.controller;

import com.spe.smartdocjp.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller // 注意：这里是 @Controller，不是 @RestController
@RequiredArgsConstructor
public class WebController {

    private final DocumentService documentService;

    // 首页：展示列表
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("documents", documentService.getAllDocuments());
        return "index"; // 对应 index.html
    }

    // 上传动作：处理后重定向回首页
    @PostMapping("/upload-view")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("userId") Long userId) {
        try {
            documentService.uploadDocument(file, userId);
        } catch (IOException e) {
            // 简单处理：实际项目中可以添加错误信息到 RedirectAttributes
            e.printStackTrace();
        }
        return "redirect:/"; // 重定向回首页，刷新列表
    }
}