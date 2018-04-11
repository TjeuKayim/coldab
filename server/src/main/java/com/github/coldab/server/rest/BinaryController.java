package com.github.coldab.server.rest;

import com.github.coldab.shared.project.BinaryFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for BinaryServer.
 */
@RestController
@RequestMapping("/bin/")
public class BinaryController {

  @GetMapping(value = "download/{fileId}", produces = "application/octet-stream")
  byte[] download(@PathVariable Integer fileId) {
    return null;
  }

  @PostMapping("upload")
  BinaryFile upload(@RequestBody MultipartFile multipartFile) {
    return null;
  }
}
