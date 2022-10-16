package zzh.darfing.mycrm.commons.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static ResponseEntity<byte[]> fileDownUtil(String realPath, String filename) {
        //获取ServletContext对象
        InputStream is = null;
        try {
            // 创建输入流
            is = new FileInputStream(realPath);
            //创建字节数组
            byte[] bytes = new byte[is.available()];
            // 将流读到字节数组中
            is.read(bytes);
            //创建HttpHeaders对象设置响应头信息
            MultiValueMap<String, String> headers = new HttpHeaders();
            //设置要下载方式以及下载文件的名字
            headers.add("Content-Disposition", "attachment;filename=" + filename);
            //设置响应状态码
            HttpStatus statusCode = HttpStatus.OK;
            //创建ResponseEntity对象
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(bytes, headers, statusCode);
            return responseEntity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            //关闭输入流
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void fileUpload(MultipartFile multipartFile, String filename) throws IOException {
        //通过ServletContext获取photo文件夹在服务器中的路径
        String fileRealPath = "D:\\Java\\crm\\mycrm\\src\\main\\webapp\\upload\\";
        File file = new File(fileRealPath);
        if (!file.exists()) {
            file.mkdir();
        }
        //获取文件的绝对路径名
        String finalFile = fileRealPath + filename;
        multipartFile.transferTo(new File(finalFile));
    }

}
