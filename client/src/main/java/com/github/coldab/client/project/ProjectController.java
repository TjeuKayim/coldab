package com.github.coldab.client.project;

import com.github.coldab.shared.project.TextFile;

public interface ProjectController {

  TextFileController openFile(TextFile file, TextFileObserver textFileObserver);

  void closeFile(TextFile file);
}
