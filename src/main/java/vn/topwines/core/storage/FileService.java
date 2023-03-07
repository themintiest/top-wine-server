package vn.topwines.core.storage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import vn.topwines.exception.BadRequestException;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
@RequiredArgsConstructor
public class FileService {
    private final StorageProperties storageProperties;

    public String uploadFile(String fileName, InputStream inputStream) throws IOException {
        String filePath = getPath(fileName).toAbsolutePath().toString();
        File file = new File(filePath);
        FileUtils.copyInputStreamToFile(inputStream, file);
        return storageProperties.defaultHost() + "/storages/download?fileName=" + fileName;
    }

    public String getFilePath(String fileName) {
        return buildPath(fileName);
    }

    private Path getPath(String name) throws IOException {
        Path path = Paths.get(storageProperties.defaultPath()).toAbsolutePath().normalize();
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        path = path.resolve(name);
        if (Files.exists(path)) {
            throw new BadRequestException("Tên file đã tồn tại, vui lòng đổi tên file khác");
        }
        return path;
    }

    private String buildPath(String name) {
        return String.format("%s/%s", storageProperties.defaultPath(), name);
    }
}
