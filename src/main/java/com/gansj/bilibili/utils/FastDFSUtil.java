package com.gansj.bilibili.utils;

import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.exception.BusinessException;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FastDFSUtil {

    @Resource
    private FastFileStorageClient fastFileStorageClient;

    @Resource
    private AppendFileStorageClient appendFileStorageClient;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    private static final String DEFAULT_GROUP="group1";
    private static final String PATH_KEY="path-key";
    private static final String UPLOAD_SIZE_KEY="upload-size-key";
    private static final String UPLOAD_NO_KEY="upload-no-key";
    private  static  final int SLICE_SIZE=1024*1024*2;

    //获取文件类型
    public String getFileType(MultipartFile file){
        if (file == null) {
            throw new BusinessException(ErrorCode.FILE_ERROR);
        }
        String filename = file.getOriginalFilename();
        int index = filename.lastIndexOf(".");
        String fileType = filename.substring(index + 1);
        return fileType;
    }

    //上传文件
    public String uploadCommonFile(MultipartFile file) throws IOException {
        Set<MetaData> metaDataSet = new HashSet<>();
        String fileType = this.getFileType(file);
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, metaDataSet);
        return storePath.getPath();
    }

    //删除文件
    public void deleteFile(String filePath){
        fastFileStorageClient.deleteFile(filePath);
    }


    //上传可以断点续传的文件
    //对文件分片，来进行上传，对第一个分片返回一个路径
    public String uploadAppenderFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String fileType = this.getFileType(file);
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(DEFAULT_GROUP, file.getInputStream(), file.getSize(), fileType);
        return storePath.getPath();
    }

    //第一个分片完成以后，后续的分片开始添加
    public void modifyAppendFile(MultipartFile file,String filePath,long offset) throws Exception{
        appendFileStorageClient.modifyFile(DEFAULT_GROUP,filePath, file.getInputStream(), file.getSize(),offset);
    }

    //断点续传核心方法
    public String uploadFileBySlices(MultipartFile file,String fileMD5,Integer sliceNo,Integer totalSliceNo) throws Exception {
        if (file==null || sliceNo==null || totalSliceNo == null){
            throw new BusinessException(ErrorCode.FILE_ERROR,"参数异常");
        }
        String pathKey = PATH_KEY + fileMD5;
        String uploadedSizeKey = UPLOAD_SIZE_KEY + fileMD5;
        String uploadNoKey = UPLOAD_NO_KEY+fileMD5;
        //先去Redis查询已上传的文件总大小
        String uploadSizeStr = redisTemplate.opsForValue().get(uploadedSizeKey);
        Long uploadedSize=0L;
        if (StringUtils.isNotBlank(uploadSizeStr)){
            uploadedSize = Long.valueOf(uploadSizeStr);
        }
        //获取文件类型
        String fileType = this.getFileType(file);
        if (sliceNo==1){
            String path = this.uploadAppenderFile(file);
            if (StringUtils.isBlank(path)){
                throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
            }
            redisTemplate.opsForValue().set(pathKey,path);
            redisTemplate.opsForValue().set(uploadNoKey,"1");
        }else{
            String filePath = redisTemplate.opsForValue().get(pathKey);
            if (StringUtils.isBlank(filePath)){
                throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
            }
            this.modifyAppendFile(file,filePath,uploadedSize);
            redisTemplate.opsForValue().increment(uploadNoKey);
        }
        //更新已上传的分片文件总大小
        uploadedSize+=file.getSize();
        redisTemplate.opsForValue().set(uploadedSizeKey,String.valueOf(uploadedSize));
        //最后判断所有的分片是否全部上传完毕
        String uploadNoStr = redisTemplate.opsForValue().get(uploadNoKey);
        Integer uploadNo = Integer.valueOf(uploadNoStr);
        String resultPath = "";
        if (uploadNo.equals(totalSliceNo)){
            resultPath = redisTemplate.opsForValue().get(pathKey);
            List<String> ketList = Arrays.asList(uploadedSizeKey, uploadNoKey, pathKey);
            redisTemplate.delete(ketList);
        }
        return resultPath;
    }


    //分片功能(方便本地测试)
    public void convertFileToSlices(MultipartFile multipartFile) throws  Exception{
        String fileName=multipartFile.getOriginalFilename();
        String fileType=this.getFileType(multipartFile);
        File file=this.multipartFileToFile(multipartFile);
        long fileLength=file.length();
        int count=1;
        for(int i=0;i<fileLength;i+=SLICE_SIZE){
            RandomAccessFile randomAccessFile=new RandomAccessFile(file,"r");
            randomAccessFile.seek(i);
            byte[] bytes=new byte[SLICE_SIZE];
            int len=randomAccessFile.read(bytes);
            String path="C:\\Users\\86177\\Postman\\files\\temp\\"+count+"."+fileType;
            File slice=new File(path);
            FileOutputStream fos=new FileOutputStream(slice);
            fos.write(bytes,0,len);
            fos.close();
            randomAccessFile.close();
            count++;
        }
        file.delete();
    }
    public File multipartFileToFile(MultipartFile multipartFile) throws  Exception{
        String originalFileName=multipartFile.getOriginalFilename();
        String[] fileName=originalFileName.split("\\.");
        File file=File.createTempFile(fileName[0],"."+fileName[1]);
        multipartFile.transferTo(file);
        return  file;
    }
}
