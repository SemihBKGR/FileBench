package com.semihbg.filebench.server.service;

import java.io.InputStream;

public interface FileIOService {

    void createFolder(String folderName);

    void deleteFolder(String folderName);

    void createFile(String folderName, String fileName, InputStream inputStream);

    void deleteFile(String folderName, String fileName);

}
