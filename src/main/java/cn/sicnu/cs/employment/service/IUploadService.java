package cn.sicnu.cs.employment.service;


import java.io.FileInputStream;

public interface IUploadService {

    String uploadImg(FileInputStream file, String key) throws Exception;

    void deleteImg(String key);
}
