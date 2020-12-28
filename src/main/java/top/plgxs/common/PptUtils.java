package top.plgxs.common;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.sl.usermodel.TextParagraph.TextAlign;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.springframework.util.Base64Utils;

public class PptUtils {

    public static String createPpt(List<String> images, String title,
                                   String filePath) throws Exception, IOException {

        //创建ppt对象
        XMLSlideShow ppt = new XMLSlideShow();
        if (images != null && images.size() > 0) {
            for (int i = 0, len = images.size(); i < len; i++) {
                XSLFSlide slide = ppt.createSlide();
                createPicture(slide, images.get(i), ppt);
            }
        }

        StringBuffer fileUrl = new StringBuffer();
        String fileName = title + System.currentTimeMillis() + ".pptx";
        fileUrl.append(filePath);
        fileUrl.append(fileName);
        FileOutputStream out = null;
        try {
            File file = new File(fileUrl.toString());
            out = new FileOutputStream(file);
            ppt.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 填充图文ppt
     *
     * @param strings
     * @param contentSlides
     * @param titles
     * @param picturePaths
     * @param ppt
     */
    private static void fillOtherContent(List<XSLFSlide> contentSlides, String[] titles,
                                         String[] picturePaths, String[] picCreateTimes,
                                         XMLSlideShow ppt) {
        if (contentSlides != null) {
            int len = contentSlides.size();
            for (int i = 0; i < len; i++) {
                createTitle(contentSlides.get(i), titles[i]);
                createPicture(contentSlides.get(i), picturePaths[i], ppt);
                createTime(contentSlides.get(i), picCreateTimes[i]);
            }
        }
    }

    /**
     * 填充时间
     *
     * @param xslfSlide
     * @param picCreateTime
     */
    private static void createTime(XSLFSlide xslfSlide, String picCreateTime) {
        //标题文本框
        XSLFTextBox xslfTextBox = xslfSlide.createTextBox();
        xslfTextBox.setAnchor(new Rectangle(400, 460, 300, 80));
        xslfTextBox.setFlipHorizontal(true);
        //段落
        XSLFTextParagraph paragraph0 = xslfTextBox.addNewTextParagraph();
        paragraph0.setTextAlign(TextAlign.LEFT);
        XSLFTextRun xslfTextRun = paragraph0.addNewTextRun();
        xslfTextRun.setFontSize((double) 18);
        //宋体 (正文)
        xslfTextRun.setFontFamily("\u5b8b\u4f53 (\u6b63\u6587)");
        String text = "123";
        xslfTextRun.setText(String.format(text, picCreateTime));
    }

    /**
     * 填充图片
     *
     * @param slide
     * @param pictureBase
     * @param ppt
     */
    private static void createPicture(XSLFSlide slide, String pictureBase, XMLSlideShow ppt) {
        byte[] pictureData = Base64Utils.decodeFromString(pictureBase);
        XSLFPictureData pictureIndex = ppt.addPicture(pictureData, PictureType.PNG);
        XSLFPictureShape pictureShape = slide.createPicture(pictureIndex);
        pictureShape.setAnchor(new Rectangle(0, 0, 760, 600));
    }

    /**
     * 目录填充
     *
     * @param xslfSlide
     * @param contentSlides
     * @param dataMap
     * @param titles
     */
    private static void fillSecondContent(XSLFSlide xslfSlide, List<XSLFSlide> contentSlides,
                                          HashMap<String, Object> dataMap, String[] titles) {
        createTitle(xslfSlide, "目录");
        //内容文本框
        XSLFTextBox xslfTextBox = xslfSlide.createTextBox();
        xslfTextBox.setAnchor(new Rectangle(120, 140, 500, 250));
        xslfTextBox.setFlipHorizontal(true);
        //段落
        XSLFTextParagraph paragraph = xslfTextBox.addNewTextParagraph();
        paragraph.setTextAlign(TextAlign.LEFT);
        for (int i = 0; i <= 4; i++) {
            XSLFTextRun xslfTextRun = paragraph.addNewTextRun();
            xslfTextRun.setUnderlined(true);
            xslfTextRun.setFontSize((double) 36);
            xslfTextRun.setFontFamily("\u5b8b\u4f53 (\u6b63\u6587)");
            xslfTextRun.setText(i + 1 + "、" + titles[i]);
            xslfTextRun.createHyperlink().setAddress(contentSlides.get(i).toString());
        }
    }

    /**
     * 生成标题头
     *
     * @param xslfSlide
     * @param title
     */
    private static void createTitle(XSLFSlide xslfSlide, String title) {
        //标题文本框
        XSLFTextBox xslfTextBox = xslfSlide.createTextBox();
        xslfTextBox.setAnchor(new Rectangle(10, 25, 700, 85));
        xslfTextBox.setFlipHorizontal(true);
        //段落
        XSLFTextParagraph paragraph0 = xslfTextBox.addNewTextParagraph();
        paragraph0.setTextAlign(TextAlign.CENTER);
        XSLFTextRun xslfTextRun = paragraph0.addNewTextRun();
        xslfTextRun.setFontSize((double) 44);
        //黑体
        xslfTextRun.setFontFamily("\u9ed1\u4f53");
        xslfTextRun.setBold(true);
        xslfTextRun.setText(title);
    }

    /**
     * 填充首页内容
     *
     * @param xslfSlide
     * @param dataMap
     */
    private static void fillIndexContent(XSLFSlide xslfSlide, HashMap<String, Object> dataMap) {
        //文本框
        XSLFTextBox xslfTextBox = xslfSlide.createTextBox();
        xslfTextBox.setAnchor(new Rectangle(65, 25, 616, 480));
        xslfTextBox.setFlipHorizontal(true);
        //段落
        XSLFTextParagraph paragraph = xslfTextBox.addNewTextParagraph();
        paragraph.setTextAlign(TextAlign.CENTER);
        //标题
        XSLFTextRun xslfTextRun = paragraph.addNewTextRun();
        xslfTextRun.setBold(true);
        xslfTextRun.setFontSize((double) 44);
        //宋体 (标题)
        xslfTextRun.setFontFamily("\u5b8b\u4f53 (\u6807\u9898)");
        String title = "1234";
        title = String.format(title, dataMap.get("seismicYear").toString(),
            dataMap.get("seismicMonth").toString(), dataMap.get("seismicDay").toString(),
            dataMap.get("place").toString() + dataMap.get("magnitude").toString());
        xslfTextRun.setText(title);

        //制作单位
        XSLFTextRun xslfTextRun2 = paragraph.addNewTextRun();
        xslfTextRun2.setBold(false);
        xslfTextRun2.setFontSize((double) 24);
        //宋体 (正文)
        xslfTextRun2.setFontFamily("\u5b8b\u4f53 (\u6b63\u6587)");
        xslfTextRun2.setText("12345\n\n\n\n\n\n\n\n");
        //生成时间
        XSLFTextRun xslfTextRun3 = paragraph.addNewTextRun();
        xslfTextRun3.setBold(true);
        xslfTextRun3.setFontSize((double) 44);
        //宋体 (正文)
        xslfTextRun3.setFontFamily("\u5b8b\u4f53 (\u6807\u9898)");
        xslfTextRun3.setText(String.format("%s年%s月%s日", dataMap.get("createYear").toString(),
            dataMap.get("createMonth").toString(), dataMap.get("createDay").toString()));
    }
}
