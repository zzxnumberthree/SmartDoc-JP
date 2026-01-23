package com.spe.smartdocjp.service;

import com.spe.smartdocjp.model.entity.Document;
import com.spe.smartdocjp.model.entity.User;
import com.spe.smartdocjp.repository.DocumentRepository;
import com.spe.smartdocjp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Lombok 自动生成构造函数(只含final字段)，并注入 Repository
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final AiAnalysisService aiAnalysisService;
    private final Path fileStorageLocation = Paths
            .get("./uploads").toAbsolutePath().normalize();
    // ./uploads 表示放在项目根目录下叫 uploads
    // 获取文件存放的根目录，转成绝对路径，清除多余..防止路径注入，适配不同平台

    // 上传文件到磁盘和数据库
    @Transactional // 报错后能回滚
    public Document uploadDocument(MultipartFile file, Long userId) throws IOException {
        // 先确保 存储目录存在
        Files.createDirectories(fileStorageLocation);

        // 查找用户，找不到则抛出异常
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("user not found"));

        // 存储前重新给文件命名 使用UUID
        String originalFilename = file.getOriginalFilename();
        String extension = ""; // extension 表示后缀名

        if (originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 以UUID码为文件名储存在磁盘
        //  String sortedFilename = UUID.randomUUID().toString() + extension;
        String sortedFilename = UUID.randomUUID() + extension;

        // 文件存储到磁盘的路径
        Path targetLocation = this.fileStorageLocation.resolve(sortedFilename); // resolve 自动处理不同系统的斜杠

        String summary = "Not Processed Yet";

        try { // 将文件存储到磁盘，REPLACE_EXISTING 文件名冲突则覆盖
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("文件写入失败", e);
        }


        // 简单的文件类型检查 (仅演示用，生产环境应检查 MIME Type)
        try {
            if (originalFilename.endsWith(".txt") | originalFilename.endsWith(".md") | originalFilename.endsWith(".java")) {
                // 读取文件内容 (注意：大文件直接读入内存有风险，这里仅做 Demo)
                String content = Files.readString(targetLocation);
                // 调用 AI 生成摘要
                summary = aiAnalysisService.generateSummaryFromText(content);
            } else if (originalFilename.endsWith(".pdf")) {

                Resource pdfResource = new FileSystemResource(targetLocation);

                summary = aiAnalysisService.generateSummaryFromPdf(pdfResource);
            } else {
                summary = "Unsupported format";
                System.out.println("Unsupported format");
            }
        } catch (Exception e) {
            // AI 失败不应阻断文件上传，记录日志即可
            // log.error("AI processing failed", e);
            summary = "AI Processing Failed: " + e.getMessage();
        }

        // 将文件信息存储到数据库
        Document doc = Document.builder()
                .title(originalFilename)
                .originalFilename(originalFilename)
                .storagePath(sortedFilename)
                .user(user)
                .summary(summary)
                .status(Document.DocStatus.completed)
                .isDeleted(false)
                .build();

        try {
            documentRepository.save(doc);
        } catch (Exception e) {
            // 捕获异常，删除刚才写进去的文件
            Files.deleteIfExists(targetLocation);
            System.out.println("检测到事务回滚，已清理垃圾文件");
            throw e; // 抛出异常，让 @Transactional 回滚数据库
        }

        return doc;
    }


    public List<Document> getAllDocuments() {
        return documentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }




}
