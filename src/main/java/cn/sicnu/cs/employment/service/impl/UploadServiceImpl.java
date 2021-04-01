package cn.sicnu.cs.employment.service.impl;

import cn.sicnu.cs.employment.service.IUploadService;
import com.google.gson.Gson;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements IUploadService {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

//    @Value("${qiniu.path}")
//    private String path;

    private Auth auth;
    private Configuration cfg;

    private void init() {
        //构造一个带指定Zone对象的配置类
        cfg = new Configuration(Zone.zone2());
        //生成上传凭证，然后准备后续工作
        auth = Auth.create(accessKey, secretKey);
    }

    /**
     * 获取凭证
     *
     * @param bucketName 空间名称
     * @return
     */
    public String getUpToken(String bucketName, String key) {
        //insertOnly 如果希望只能上传指定key的文件，并且不允许修改，那么可以将下面的 insertOnly 属性值设为 1
        return auth.uploadToken(bucketName, key, 3600, new StringMap().put("insertOnly", 0));
    }

    /**
     * 将图片上传到七牛云
     *
     * @param file 文件输入流
     * @param key  保存在空间中的名字，如果为空会使用文件的hash值为文件名
     */
    public String uploadImg(FileInputStream file, String key) throws Exception {
        init();
//        deleteImg(key);
        UploadManager uploadManager = new UploadManager(cfg);
        String upToken = getUpToken(bucket, key);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        Response response = uploadManager.put(file, key, upToken, null, null);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        log.info("返回的key={}", putRet.key);
//        return "http://" + path + "/" + putRet.key;
        return putRet.key;
    }

    @Override
    public void deleteImg(String key) {
        init();
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            log.info("删除的图片key：{}", key);
            bucketManager.delete(bucket, key);
        } catch (Exception e) {
            // 如果存在则删除
//            throw new CustomException(OTHER_ERROR, "删除文件时出现错误：" + e.getMessage());
        }
    }
}