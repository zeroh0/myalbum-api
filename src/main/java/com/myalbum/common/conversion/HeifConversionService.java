package com.myalbum.common.conversion;

import com.myalbum.common.conversion.enums.HeifConversionError;
import com.myalbum.common.error.exception.AppException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HeifConversionService {

    private static final Set<String> HEIF_EXTENSIONS = Set.of("heic", "heif");

    private final HeifConvertProperties heifConvertProperties;

    public HeifConversionService(HeifConvertProperties heifConvertProperties) throws IOException {
        this.heifConvertProperties = heifConvertProperties;
        init();
    }

    /**
     * 파일 변환에 필요한 임시 디렉토리를 생성
     */
    @PostConstruct
    public void init() {
        try {
            Path rootLocation = Path.of(heifConvertProperties.getTempDirPath());
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            log.error("임시 디렉토리 생성 실패", e);
            AppException.exception(HeifConversionError.HEIF_CONVERSION_FAILED);
        }
    }

    public boolean isHeif(String filename) {
        String ext = getExtension(filename).toLowerCase();
        return HEIF_EXTENSIONS.contains(ext);
    }

    /**
     * HEIF 파일을 JPG로 변환하여 새로운 임시 파일 경로를 반환
     */
    public Path convertToJpg(Path heifPath) throws IOException {
        Path jpgPath = Files.createTempFile(Path.of(heifConvertProperties.getTempDirPath()), "converted-", ".jpg");

        ProcessBuilder pb = new ProcessBuilder(
                heifConvertProperties.getPath(),
                heifPath.toString(),
                jpgPath.toString()
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();

        String output;
        try (var reader = process.inputReader()) {
            output = reader.lines().collect(Collectors.joining("\n"));
        }

        boolean finished = waitForProcess(process);
        if (!finished || process.exitValue() != 0) {
            Files.deleteIfExists(jpgPath);
            log.error("HEIF 변환 실패. exitCode={}, output={}", process.exitValue(), output);
            AppException.exception(HeifConversionError.HEIF_CONVERSION_FAILED);
        }

        return jpgPath;
    }

    /**
     * HEIF 변환 프로세스가 정상적으로 종료될 때까지 대기
     *
     * @param process HEIF 변환 프로세스
     * @return 프로세스가 정상적으로 종료되었는지 여부
     * @throws IOException 프로세스 실행 중 I/O 오류 발생 시
     */
    private boolean waitForProcess(Process process) throws IOException {
        try {
            return process.waitFor(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            process.destroyForcibly();
            log.error("HEIF 변환 프로세스가 중단되었습니다.", e);
            AppException.exception(HeifConversionError.HEIF_CONVERSION_INTERRUPTED);
        }

        return false;
    }

    /**
     * 파일 확장자 추출
     *
     * @param filename 파일명
     * @return 파일 확장자
     */
    private String getExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        return idx == -1 ? "" : filename.substring(idx + 1);
    }

}
