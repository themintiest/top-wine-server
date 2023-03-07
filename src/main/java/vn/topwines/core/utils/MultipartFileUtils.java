package vn.topwines.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipartFileUtils {
    public static String getFileName(InputPart inputPart) {
        return getFileName(inputPart.getHeaders());
    }

    private static String getFileName(Map<String, List<String>> header) {
        String[] contentDisposition = header.get("Content-Disposition").get(0).split(";");
        for (String fileName : contentDisposition) {
            if (fileName.trim().startsWith("filename")) {
                return fileName.split("=")[1].replaceAll("\"", "");
            }
        }
        return "unknown";
    }
}
