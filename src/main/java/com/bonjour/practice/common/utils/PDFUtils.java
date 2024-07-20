import com.itextpdf.text.pdf.BaseFont;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.UUID;

//package com.bonjour.practice.common.utils;
//
///**
// * @authur tc
// * @date 2023/9/27 16:30
// */
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.itextpdf.text.BaseColor;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfContentByte;
//import com.itextpdf.text.pdf.PdfReader;
//import com.itextpdf.text.pdf.PdfStamper;
//import com.lowagie.text.BadElementException;
//import com.lowagie.text.Image;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import org.apache.commons.io.IOUtils;
//import org.apache.velocity.VelocityContext;
//import org.apache.velocity.app.VelocityEngine;
//import org.apache.velocity.exception.VelocityException;
//import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
//import org.apache.velocity.tools.generic.DateTool;
//import org.mohrss.leaf.core.framework.web.exception.BusinessException;
//import org.springframework.util.Base64Utils;
//import org.w3c.dom.Element;
//import org.xhtmlrenderer.extend.FSImage;
//import org.xhtmlrenderer.extend.ReplacedElement;
//import org.xhtmlrenderer.extend.ReplacedElementFactory;
//import org.xhtmlrenderer.extend.UserAgentCallback;
//import org.xhtmlrenderer.layout.LayoutContext;
//import org.xhtmlrenderer.layout.SharedContext;
//import org.xhtmlrenderer.pdf.ITextFSImage;
//import org.xhtmlrenderer.pdf.ITextImageElement;
//import org.xhtmlrenderer.pdf.ITextRenderer;
//import org.xhtmlrenderer.render.BlockBox;
//import org.xhtmlrenderer.simple.extend.FormSubmissionListener;
//
public class PDFUtils {
//    private static final String PDF_TEMP_DIR = "/file_temp_dir";
//    private static final String HTML_TEMPLATE_DIR = "htmltemplate/";
//
//    public PDFUtils() {
//    }
//
//    public static Map<String, Object> generatePdfByHtmlForByteArray(String templateFile, Map<String, Object> dataMap) {
//        Map<String, Object> result = new HashMap();
//        result.put("code", "error");
//
//        try {
//            templateFile = templateFile.replace("/", File.separator);
//            dataMap.put("dateTool", new DateTool());
//            dataMap.put("nowTime", Timestamp.valueOf(LocalDateTime.now()));
//            String html = getHtmlByVelocity("htmltemplate/" + templateFile, dataMap);
//            html = html.replace("&nbsp;", "  ");
//            byte[] data = htmlToPdfForByteArray(html);
//            result.put("code", "ok");
//            result.put("data", data);
//            return result;
//        } catch (Exception var5) {
//            result.put("msg", "生成pdf过程中失败：" + var5.getMessage());
//            return result;
//        }
//    }
//
//    private static String getHtmlByVelocity(String templateFile, Map<String, Object> dataMap) {
//        String html = "";
//        VelocityEngine ve = new VelocityEngine();
//        ve.setProperty("ISO-8859-1", "UTF-8");
//        ve.setProperty("input.encoding", "UTF-8");
//        ve.setProperty("output.encoding", "UTF-8");
//        ve.setProperty("resource.loader", "classpath");
//        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
//        ve.init();
//        html = mergeTemplateIntoString(ve, templateFile, "UTF-8", dataMap);
//        return escapeReplace(html);
//    }
//
//    public static String escapeReplace(String str) {
//        str = str.replaceAll("\\&([^a-zA-Z#])", "&amp;$1");
//        return str;
//    }
//
//    private static byte[] htmlToPdfForByteArray(String html) throws Exception {
//        OutputStream os = null;
//        InputStream insm = null;
//        byte[] byteArray = null;
//        String tempFileFullName = PDFUtils.class.getResource("/file_temp_dir").getPath() + File.separator + UUID.randomUUID().toString().replace("-", "") + ".pdf";
//
//        byte[] var8;
//        try {
//            ITextRenderer renderer = new ITextRenderer();
//            SharedContext sharedContext = renderer.getSharedContext();
//            sharedContext.setReplacedElementFactory(new B64ImgReplacedElementFactory());
//            sharedContext.getTextRenderer().setSmoothingThreshold(0.0F);
//            renderer.setDocumentFromString(html.toString());
//            File tempFile = new File(tempFileFullName);
//            if (!tempFile.getParentFile().exists()) {
//                tempFile.getParentFile().mkdirs();
//            }
//
//            if (!tempFile.exists()) {
//                tempFile.createNewFile();
//            }
//
//            os = new FileOutputStream(tempFile);
//            renderer.layout();
//            renderer.createPDF(os);
//            renderer.finishPDF();
//            os.close();
//            tempFile = new File(tempFileFullName);
//            insm = new FileInputStream(tempFile);
//            byte[] byteArray = IOUtils.toByteArray(insm);
//            insm.close();
//            tempFile.delete();
//            var8 = byteArray;
//        } catch (Exception var20) {
//            throw var20;
//        } finally {
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (IOException var19) {
//                }
//            }
//
//            if (insm != null) {
//                try {
//                    insm.close();
//                } catch (IOException var18) {
//                }
//            }
//
//        }
//
//        return var8;
//    }
//
//    public static byte[] addWatermark(InputStream inputStream, String watermark) throws IOException, DocumentException {
//        PdfReader reader = new PdfReader(inputStream);
//
//        try {
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            Throwable var4 = null;
//
//            try {
//                PdfStamper stamper = new PdfStamper(reader, os);
//                int total = reader.getNumberOfPages() + 1;
//                BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", true);
//
//                for(int i = 1; i < total; ++i) {
//                    PdfContentByte content = stamper.getUnderContent(i);
//                    content.beginText();
//                    content.setColorFill(new BaseColor(234, 234, 234));
//                    content.setFontAndSize(baseFont, 24.0F);
//                    content.setTextMatrix(400.0F, 780.0F);
//
//                    for(int x = 0; x < 5; ++x) {
//                        for(int y = 0; y < 5; ++y) {
//                            content.showTextAlignedKerned(1, watermark, 100.0F + (float)(x * 350), 40.0F + (float)(y * 150), 30.0F);
//                        }
//                    }
//
//                    content.endText();
//                }
//
//                stamper.close();
//                byte[] var29 = os.toByteArray();
//                return var29;
//            } catch (Throwable var26) {
//                var4 = var26;
//                throw var26;
//            } finally {
//                if (os != null) {
//                    if (var4 != null) {
//                        try {
//                            os.close();
//                        } catch (Throwable var25) {
//                            var4.addSuppressed(var25);
//                        }
//                    } else {
//                        os.close();
//                    }
//                }
//
//            }
//        } finally {
//            reader.close();
//        }
//    }
//
//    private static void mergeTemplate(VelocityEngine velocityEngine, String templateLocation, String encoding, Map<String, Object> model, Writer writer) throws Exception {
//        VelocityContext velocityContext = new VelocityContext(model);
//        velocityEngine.mergeTemplate(templateLocation, encoding, velocityContext, writer);
//    }
//
//    private static String mergeTemplateIntoString(VelocityEngine velocityEngine, String templateLocation, String encoding, Map<String, Object> model) throws Exception {
//        StringWriter result = new StringWriter();
//        mergeTemplate(velocityEngine, templateLocation, encoding, model, result);
//        return result.toString();
//    }
//
//    public static String monthToYear(int month) {
//        if (month == 0) {
//            return null;
//        } else {
//            int year = month / 12;
//            month %= 12;
//            return (year == 0 ? "" : year + "年") + (month == 0 ? "" : month + "个月");
//        }
//    }
//
//    static class B64ImgReplacedElementFactory implements ReplacedElementFactory {
//        B64ImgReplacedElementFactory() {
//        }
//
//        @Override
//        public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
//            Element e = box.getElement();
//            if (e == null) {
//                return null;
//            } else {
//                String nodeName = e.getNodeName();
//                if (nodeName.equals("img")) {
//                    String attribute = e.getAttribute("src");
//
//                    FSImage fsImage;
//                    try {
//                        fsImage = this.buildImage(attribute, uac);
//                    } catch (BadElementException var11) {
//                        fsImage = null;
//                    } catch (IOException var12) {
//                        fsImage = null;
//                    }
//
//                    if (fsImage != null) {
//                        if (cssWidth != -1 || cssHeight != -1) {
//                            fsImage.scale(cssWidth, cssHeight);
//                        }
//
//                        return new ITextImageElement(fsImage);
//                    }
//                }
//
//                return null;
//            }
//        }
//
//        protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws IOException, BadElementException {
//            FSImage fsImage = null;
//            if (srcAttr.startsWith("data:image/")) {
//                String b64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
//                byte[] decodedBytes = Base64Utils.decodeFromString(b64encoded);
//
//                try {
//                    fsImage = new ITextFSImage(Image.getInstance(decodedBytes));
//                } catch (BadElementException var7) {
//                    var7.printStackTrace();
//                }
//            } else {
//                fsImage = uac.getImageResource(srcAttr).getImage();
//            }
//
//            return (FSImage)fsImage;
//        }
//
//        @Override
//        public void reset() {
//        }
//
//        @Override
//        public void remove(Element arg0) {
//        }
//
//        @Override
//        public void setFormSubmissionListener(FormSubmissionListener arg0) {
//        }
//    }
//
//    public static void main(String[] args) {
//        String s = "{\"code\":0,\"data\":{\"responsedata\":{\"callstatus\":\"1\",\"reportid\":\"202309120927161130000321444009\",\"data\":{\"personFeeDetailDTOList\":[{\"siNo\":\"120106196807150578\",\"months\":1,\"unitPayAccountInterest\":0,\"yearMonth\":\"202308\",\"localFeeType\":\"402\",\"unitPayCollectiveFee\":3321.32,\"uscc\":\"911101086003726929\",\"personFeeBase\":33891.00,\"unitPayAccountFee\":0.00,\"personFee\":680.82,\"realityLateFeeDate\":\"20230927\",\"lateFeeTotal\":0.00,\"lateFeeDays\":0,\"totalFee\":4002.14,\"empName\":\"北京键凯科技股份有限公司\",\"lateFee\":0.00,\"psnName\":\"魏晨光\"}],\"logDetailDTOList\":[],\"auditseq\":\"202309281338292130000321503008\",\"unitFeeDetailDTOList\":[{\"lateFeeTotal\":0.00,\"personTotalFee\":680.82,\"totalFee\":4002.14,\"yearMonth\":\"202308\",\"empName\":\"北京键凯科技股份有限公司\",\"fee\":0.00,\"uscc\":\"911101086003726929\",\"interestTotal\":0,\"empNo\":\"11000021000000026106\",\"capitalTotal\":4002.14,\"unitTotalFee\":3321.32,\"personCount\":1}]}},\"responseheader\":{\"msghead\":{\"requestid\":\"202309120927161130000321444009\"}}},\"type\":\"success\",\"message\":\"成功\"}";
//        JSONObject jsonObject = JSON.parseObject(s);
//        String type = jsonObject.get("type").toString();
//        if (!"success".equals(type)) {
//            throw new BusinessException("");
//        }
//        JSONObject outdata = (JSONObject) jsonObject.get("data");
//        JSONObject responsedata = (JSONObject) outdata.get("responsedata");
//        JSONObject innerdata = (JSONObject) responsedata.get("data");
//    }

    public static void main(String[] args) throws Exception {
        String DEFAULT_HTML = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\" />\n" +
                "<style> \n" +
                "  body{ padding:0; margin:0; font-family:Microsoft YaHei; @page {size:20mm, 35mm;}} \n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  $!{aaa}\n" +
                "</body>\n" +
                "</html>";

        String html = "";
//        VelocityEngine ve = new VelocityEngine();
//        ve.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
//        ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");//指定编码格式，避免生成模板就造成乱码，影响到转pdf后的文件
//        ve.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
//        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");// 这是模板所在路径
//        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
//        ve.init();
//        html = com.ylz.bjyf.common.util.VelocityEngineUtils.mergeTemplateIntoString(ve, templateFile, "UTF-8", dataMap);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        htmlToPdf2(DEFAULT_HTML, outputStream);
    }

    public static void htmlToPdf2(String html, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        OutputStream os = null;
        InputStream insm = null;
        String tempFileFullName = "D:\\Java\\practice\\src\\main\\resources\\pdf\\" + UUID.randomUUID().toString().replace("-", "") + ".pdf";
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            ITextFontResolver fontResolver = renderer.getFontResolver();
            // 获取字体文件路径
            fontResolver.addFont(getFontPath2(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//            renderer.getSharedContext().setReplacedElementFactory(new ImgReplacedElementFactory());
            File tempFile = new File(tempFileFullName);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            }
            os = new FileOutputStream(tempFile);
            renderer.layout();
            renderer.createPDF(byteArrayOutputStream);
            os.flush();
            tempFile = new File(tempFileFullName);
            insm = new FileInputStream(tempFile);
            insm.close();
        } catch (Exception e) {
            System.out.println(e);
            //            throw new CommonException("Html To Pdf Failed:" + e.getMessage());
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    // 字体路径
    private static String getFontPath2() {
        return "D:\\Java\\practice\\src\\main\\resources\\pdf\\simsun.ttc";
    }
}