package com.you07.vtp.controller;

import com.alibaba.fastjson.JSON;
import com.you07.util.message.MessageBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 上传图片controller
 *
 * @author RY
 * @version 1.0
 * @since 2018-7-2 15:53:00
 */

@RestController
@CrossOrigin
@RequestMapping("/uploadImage")
@Api(value = "上传图片controller", tags = {"上传图片"})
public class UploadImageController {

    /**
     * 上传图片
     *
     * @param file
     * @param folder
     * @return
     */
    @ApiOperation("上传图片")
    @PostMapping("/resources/upload")
    @ResponseBody
    public String addModuleUseRecord(@ApiParam(name = "file", value = "图片文件", required = true) @RequestParam(value = "file", required = true) MultipartFile file,
                                     @ApiParam(name = "folder", value = "图片存储路径", required = true) @RequestParam(value = "folder", required = true) String folder) throws IOException {
        MessageBean<String> messageBean = new MessageBean<String>(null);
        System.out.println(file.getOriginalFilename());
        //获取根目录
//        File path = new File(ResourceUtils.getURL("classpath:").getPath());
//        if (!path.exists()) {
//            path = new File("");
//        }
//        System.out.println("path:" + path.getAbsolutePath());

        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        System.out.println(fileExtension);
        String newFileName = UUID.randomUUID() + fileExtension;
        File uploadFolder = new File("./upload/" + folder + "/");
        if (!uploadFolder.exists()) {
            uploadFolder.mkdir();
        }

        File uploadFile = new File(uploadFolder, newFileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);

        messageBean.setStatus(true);
        messageBean.setCode(200);
        messageBean.setData("upload/" + folder + "/" + newFileName);
        messageBean.setMessage("上传成功");

        return JSON.toJSONString(messageBean);
    }

    /**
     * 上传组图
     *
     * @param files
     * @param folder
     * @return
     */
    @ApiOperation("上传组图")
    @PostMapping("/uploadImages")
    @ResponseBody
    public String addModuleUseRecord(@ApiParam(name = "files", value = "图片文件列表", required = true) @RequestParam(value = "files", required = true) List<MultipartFile> files,
                                     @ApiParam(name = "folder", value = "图片存储路径", required = true) @RequestParam(value = "folder", required = true) String folder) throws IOException {
        MessageBean<List<String>> messageBean = new MessageBean<List<String>>(null);
        List<String> imgList = new ArrayList<String>();
        for (MultipartFile file : files) {
            System.out.println(file.getOriginalFilename());
            //获取根目录
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            System.out.println("path:" + path.getAbsolutePath());

            String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            System.out.println(fileExtension);
            String newFileName = UUID.randomUUID() + fileExtension;
            File uploadFolder = new File(path.getAbsolutePath(), "resources/upload/" + folder + "/");
            if (!uploadFolder.exists()) {
                uploadFolder.mkdir();
            }

            File uploadFile = new File(uploadFolder, newFileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);

            String imgUrl = "upload/" + folder + "/" + newFileName;
            imgList.add(imgUrl);
        }

        if (files.size() > 0) {
            messageBean.setStatus(true);
            messageBean.setCode(200);
            messageBean.setData(imgList);
            messageBean.setMessage("上传成功");
        } else {
            messageBean.setStatus(false);
            messageBean.setCode(1002);
            messageBean.setMessage("图片列表为空");
        }


        return JSON.toJSONString(messageBean);
    }
}
