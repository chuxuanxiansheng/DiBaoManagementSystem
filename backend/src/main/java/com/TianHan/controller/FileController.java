package com.TianHan.controller;


import cn.hutool.core.io.FileUtil;
import com.TianHan.exception.CustomException;
import com.TianHan.utils.AuthAccess;
import com.TianHan.utils.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@CrossOrigin
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${ip:localhost}")
    private String ip;

    @Value("${server.port:8080}")
    private String port;

    //System.getProperty("user.dir") 获取当前项目路径
    private static final String filePath = System.getProperty("user.dir") + "\\backend/src/main/resources/static/files/";

    @AuthAccess
    @GetMapping("/download/{filename}")
    public void download(@PathVariable("filename") String filename, HttpServletResponse response) {
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");
            OutputStream outputStream = response.getOutputStream();
            String realFilePath = filePath + filename;
            outputStream.write(FileUtil.readBytes(realFilePath));
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("500", "下载失败");
        }
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();
        String mainName = FileUtil.mainName(originalFilename);
        String extension = FileUtil.extName(originalFilename);
        String projectPath = System.getProperty("user.dir");
        String filesPath = projectPath + "\\backend\\src\\main\\resources\\static\\files\\";
        File parentFile = new File(filesPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File saveFile = new File(filesPath + originalFilename);
        if (saveFile.exists()) {
            originalFilename = System.currentTimeMillis() + "_" + mainName + "." + extension;
            saveFile = new File(filesPath + originalFilename);
        }
        file.transferTo(saveFile);
        String url = "http://" + ip + ":" + port + "/files/download/" + originalFilename;
        return Result.success(url);
    }

    @AuthAccess
    @PostMapping("/wang/upload")
    public Map<String, Object> wangEditorUpload(@RequestParam("file") MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String mainName = FileUtil.mainName(originalFilename);
        String extension = FileUtil.extName(originalFilename);
        String projectPath = System.getProperty("user.dir");
        String filesPath = projectPath + "\\backend\\src\\main\\resources\\static\\files\\";
        File parentFile = new File(filesPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        File saveFile = new File(filesPath + originalFilename);
        if (saveFile.exists()) {
            originalFilename = System.currentTimeMillis() + "_" + mainName + "." + extension;
            saveFile = new File(filesPath + originalFilename);
        }
        file.transferTo(saveFile);
        String url = "http://" + ip + ":" + port + "/files/download/" + originalFilename;
        // 返回参数
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> urlMap = new HashMap<>();
        urlMap.put("url", url);
        list.add(urlMap);
        resMap.put("errno", 0);
        resMap.put("data", list);
        return resMap;
    }
}
