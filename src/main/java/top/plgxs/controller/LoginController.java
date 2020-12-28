package top.plgxs.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import top.plgxs.common.JsonData;
import top.plgxs.common.PptUtils;

@Controller
public class LoginController {
    @Value("${ppt_path}")
    private String pptPath;

    @RequestMapping("/pdf")
    public String toIndex() {
        return "pdf";
    }

    @RequestMapping("/createPpt")
    @ResponseBody
    public JsonData<String> createPpt(@RequestBody JSONObject jsonObject) throws IOException,
                                                                          Exception {
        String pdfName = jsonObject.getString("pdfName");
        JSONArray images = jsonObject.getJSONArray("images");
        String title = StringUtils.isBlank(pdfName) ? "PPT" : pdfName;
        List<String> list = new ArrayList<>();
        if (images != null && images.size() > 0) {
            for (int i = 0, len = images.size(); i < len; i++) {
                list.add(images.getString(i));
            }
        }
        String fileName = PptUtils.createPpt(list, title, pptPath);
        return JsonData.success(fileName, "创建ppt成功！");
    }

    @RequestMapping("/exportPpt")
    public void exporotPpt(HttpServletRequest request, HttpServletResponse response, String name) {
        String downloadpath = null;
        //获取文件名
        FileInputStream in = null;
        OutputStream out = null;
        try {
            String fileName = URLDecoder.decode(name, "UTF-8");

            downloadpath = pptPath + fileName;
            //设置响应头，控制浏览器下载该文件
            response.setHeader("content-disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //读取要下载的文件，保存到文件输入流
            in = new FileInputStream(downloadpath);
            //创建输出流
            out = response.getOutputStream();
            //缓存区
            byte buffer[] = new byte[1024];
            int len = 0;
            //循环将输入流中的内容读取到缓冲区中
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //删除服务器上的临时文件
        if (StringUtils.isNotBlank(downloadpath)) {
            File deleteFile = new File(downloadpath);
            deleteFile.delete();
        }
    }
}
