package vn.topwines.core.storage;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "storage")
public interface StorageProperties {
    String defaultPath();
    String defaultHost();
}
