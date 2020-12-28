package top.plgxs.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * 文件操作类
 */
public class FileTool {

    /**
     * 临时文件目录名称
     */
    public static final String TEMP_DIR_NAME = "tempdir";

    /**
     * 得到文件的扩展名
     * @param fileName 文件名称,可以包含路径
     * @return 文件的扩展名包括"."，如".txt"
     */
    public static String getFileExtName(String fileName) {
        String extName = "";
        int idx = fileName.lastIndexOf(".");
        if (idx != -1) {
            extName = fileName.substring(idx);
        }
        return extName;
    }

    /**
     * 从完整的文件名（包括路径）中获得文件名
     * @param fileNameWithPath
     * @return
     */
    public static String getFileName(String fileNameWithPath) {
        String tmpFileName = fileNameWithPath;
        int idx = fileNameWithPath.lastIndexOf(File.separator);
        if (idx != -1) {
            tmpFileName = fileNameWithPath.substring(idx);
        }
        return tmpFileName;
    }

    /**
     * 得到文件路径
     * @param fileNameWithPath
     * @return
     */
    public static String getFilePath(String fileNameWithPath) {
        String filePath = "";
        fileNameWithPath = fileNameWithPath.replace("/", "\\");
        int idx = fileNameWithPath.lastIndexOf(File.separator);
        if (idx != -1) {
            filePath = fileNameWithPath.substring(0, idx);
        }
        return filePath;
    }

    /**
     * 将内容写入文件
     * @param context 内容
     * @param fileName 目标文件
     * @throws Exception
     */
    public static void writeFile(String context, String fileName) throws Exception {
        byte[] bytes = context.getBytes();
        File file = new File(fileName);
        FileOutputStream fop = new FileOutputStream(file);
        BufferedOutputStream out = new BufferedOutputStream(fop);
        if (!file.exists()) {
            file.mkdirs();
        }
        out.write(bytes);
        out.flush();
        out.close();
    }

    /**
     * 复制文件
     * @param srcFileName 要复制的源文件
     * @param destFileName 目标文件
     * @throws Exception
     */
    public static void copyFile(String srcFileName, String destFileName) throws Exception {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(srcFileName)));
            String destPath = getFilePath(destFileName);
            File pathFile = new File(destPath);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            out = new BufferedOutputStream(new FileOutputStream(new File(destFileName)));
            int len;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            System.out.println("复制文件失败：" + e.getMessage());
            throw e;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception ex) {
            }
            try {
                if (out != null)
                    out.close();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * 下载文件（通用方法）
     * @param filePath 文件存储路径
     * @param response
     * @throws IOException 
     */
    public void downloadFile(String fileName, String filePath,
                             HttpServletResponse response) throws IOException {
        BufferedOutputStream output = null;
        BufferedInputStream input = null;
        try {
            String attch_name = "";
            byte[] buffer = new byte[4096]; //  缓冲区 
            if (filePath == null) {
                System.out.println("文件不存在,或者禁止下载!");
                return;
            }
            // 读到流中
            output = new BufferedOutputStream(response.getOutputStream());
            File file = new File(filePath);//先校验文件是否存在
            if (!file.exists()) {
                String mess = "文件不存在!";
                buffer = mess.getBytes();
                output.write(buffer);
            } else {
                // 设置输出的格式
                response.reset();
                response.setContentType("application/x-msdownload");
                input = new BufferedInputStream(new FileInputStream(filePath));
                if (attch_name.lastIndexOf("$") != -1) {
                    attch_name = attch_name.substring(attch_name.lastIndexOf("$") + 1);
                }
                //解决文件名为中文下载时不显示文件名的问题
                fileName = new String(fileName.getBytes(), "ISO-8859-1");
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                int n = -1;
                // 循环取出流中的数据
                while ((n = input.read(buffer, 0, 4096)) > -1) {
                    output.write(buffer, 0, n);
                }
            }
            response.flushBuffer();
        } catch (Exception e) {
            if (e instanceof java.io.FileNotFoundException) {
                try {
                    response.sendRedirect("/tip/file_not_found.html");
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            } else {
                e.printStackTrace(System.err);
            }
        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        }
    }

    /**
     * 删除文件
     * @param fileName
     * @return
     */
    public static boolean deleteFile(String fileName) {
        boolean del = false;
        try {
            File file = null;
            if (null != fileName && !"".equals(fileName))
                file = new File(fileName);
            if (file.isFile() && file.exists()) {
                file.delete();
                System.out.println("删除文件" + fileName + "成功！");
                del = true;
            } else {
                System.out.println("删除文件" + fileName + "失败！");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return del;
    }

    /**
     * 响应图片流
     * @param response
     * @param picSrc
     * @throws Exception
     */
    public static void writePic(HttpServletResponse response, String picSrc) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setLocale(new java.util.Locale("zh", "CN"));
        //查询出当前流图返回字节流
        File file = new File(picSrc);
        FileInputStream is = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        for (int b = -1; (b = is.read()) != -1;) {
            out.write(b);
        }
        out.close();
        is.close();
    }

}
