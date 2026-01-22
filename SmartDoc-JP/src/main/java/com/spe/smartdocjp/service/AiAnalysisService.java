package com.spe.smartdocjp.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class AiAnalysisService {

    private final ChatClient chatClient;

    // spring ai 提供了ChatClient.Builder 我们再build() 来生成chatClient
    public AiAnalysisService(ChatClient.Builder builder) {
        this.chatClient = builder
                // 基本的System Prompt
                .defaultSystem("你是一家大型IT企业里优秀的管理文档助手。")
                .build();
    }

    // test
    public String testConnect() {
        System.out.println("hello, this is testConnect()");
        return chatClient.prompt("hello,who are you?").call().content();
    }


    public String generateSummaryFromText(String documentText) {
        // 设置提示词模板 Message Template
        String messageTemplate =
                    "请分析一下文本，按照以下格式输出：" +
                    "1. 【文档概要】(100字程度)" +
                    "2. 【重要要点】(项目符号3点)" +
                    "3. 【技术栈/关键词】(如有)" +
                    "4. 【关注点/注意事项】(如有)" + documentText;

        // 使用 Fluent API 链式调用
        return chatClient
                .prompt(messageTemplate)
                .call()
                .content();
    }

    public String generateSummaryFromPdf(Resource pdfResource) {
        // 设置提示词模板 Message Template
        String messageTemplate = """
                    请分析附加的PDF文档，按照以下格式输出：
                    1. 【文档概要】(100字程度)
                    2. 【重要要点】(项目符号3点)
                    3. 【技术栈/关键词】(如有)
                    4. 【关注点/注意事项】(如有)
                """;

        UserMessage userMessage = new UserMessage(messageTemplate);
        // media 表示输入材料，mime自动匹配文件类型
        userMessage.getMedia().add(new Media(
                        MimeTypeUtils.parseMimeType("application/pdf"),
                        pdfResource
        ));

        return chatClient.prompt(new Prompt(userMessage))
                .call()
                .content();
    }


}