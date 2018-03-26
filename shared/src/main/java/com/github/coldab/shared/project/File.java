package com.github.coldab.shared.project;

import java.time.LocalDateTime;

public abstract class File {
    private String path;
    private LocalDateTime creationDate;

    private String getPath() {
        return path;
    }

    private String getExtension() {
        throw new UnsupportedOperationException();
    }
}
