package com.example.bbs.controller;

import com.example.bbs.dto.FileDTO;
import com.example.bbs.util.FileUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FileController {

    @PostMapping(value = "/upload", produces = {"application/json;charset=UTF-8"})
    public FileDTO uploadFile(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile imageFile) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file =(imageFile == null) ? multipartRequest.getFile("editormd-image-file") : imageFile;
        String url = FileUtil.upload(file);
        FileDTO f = new FileDTO();
        f.setSuccess(1);
        f.setUrl(url);
        return f;
    }
//    @PostMapping(value = "/upload", produces = {"application/json;charset=UTF-8"})
//    public JsonResult uploadFile(@RequestParam("file") MultipartFile file, Model model) {
//        Map<String, Object> map = new HashMap<>(1);
//        String path = FileUtil.upload(file);
//        map.put("link", path);
//        model.addAttribute("a", map);
//        System.out.println(path+"///////////////////");
//        model.addAttribute("b",path);
//        return JsonResult.success("success",path);
//    }
}
