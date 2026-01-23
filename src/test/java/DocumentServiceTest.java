import com.spe.smartdocjp.model.entity.Document;
import com.spe.smartdocjp.model.entity.User;
import com.spe.smartdocjp.repository.DocumentRepository;
import com.spe.smartdocjp.repository.UserRepository;
import com.spe.smartdocjp.service.AiAnalysisService;
import com.spe.smartdocjp.service.DocumentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

// 静态导入 Mockito 方法，提升代码可读性
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Step 6.5: Service 层单元测试
 * 技术栈：JUnit 5 (Jupiter) + Mockito
 * 特点：
 * 1. 使用 @ExtendWith(MockitoExtension.class) 而非 @SpringBootTest，实现轻量级快速测试。
 * 2. 模拟外部依赖，只关注 Service 自身的业务逻辑。
 */
// 不启动 Spring 容器, 不加载 Bean, 只用 Mockito 造对象
@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AiAnalysisService aiAnalysisService;

    @InjectMocks // Mockito 会自动实例化 DocumentService，并将上面的 @Mock 对象注入其中
    private DocumentService documentService;

    @Test
    @DisplayName("测试文件上传成功场景")
    void testUploadFile_Success() throws IOException {
        // 准备数据 (Arrange)
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Hello World".getBytes()
        );
        Long userId = 1L;
        User mockUser = new User();
        Document mockDocument = new Document();
        mockUser.setId(userId);

        // 定义 Mock 行为 (Stubbing) 声明规则
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(documentRepository.save(any())).thenReturn(mockDocument);
        when(aiAnalysisService.generateSummaryFromText(anyString())).thenReturn("Mock Summary");

        // 原则只调用一个方法
        documentService.uploadDocument(file, userId);

        // 验证 documentRepository.save()和 AI 服务 是否被调用了 1次
        verify(documentRepository, times(1)).save(any());
        verify(aiAnalysisService, times(1)).generateSummaryFromText(any());

        System.out.println("Test passed: Upload logic verified without Real DB/AI.");
    }


    @Test
    @DisplayName("测试上传空文件应抛出异常")
    void testUploadFile_EmptyFile_ShouldThrowException() {
        // Arrange: 创建一个空内容的模拟文件
        MockMultipartFile emptyFile = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        Assertions.assertThrows(RuntimeException.class, () -> {
            documentService.uploadDocument(emptyFile, 1L);
        });

        // 确保失败时，不污染系统状态
         verify(documentRepository, times(0)).save(any());
    }
}