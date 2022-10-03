package itext.contents;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yyx on 2016/9/7.
 */
public class Test1 {


    public static void main(String[] args) throws IOException, BadElementException {

        Image image0 = Image.getInstance("E://TESTPDF/信息页.png");//信息页
        Image image1 = Image.getInstance("E://TESTPDF/jiancejieguojiyongyaotishi.png");//检测结果及用药提示
        Image image2 = Image.getInstance("E://TESTPDF/jiezhichangaiyangbenhuizongjiancejieguohuozong9.png");//样本检测结果汇总（9个基因）
        Image image3 = Image.getInstance("E://TESTPDF/bianyijiyinyiyijiedu.png");//变异基因意义解读
        Image image4 = Image.getInstance("E://TESTPDF/yaowulinchuangyanjiujiedu.png");//药物临床研究解读
        Image image5 = Image.getInstance("E://TESTPDF/linchuangyiyibumingjiyinbianyijieguohuizong.png");//临床意义不明基因变异结果汇总
        Image image6 = Image.getInstance("E://TESTPDF/NGSjiancejieguo.png");//NGS 检测结果
        Image image7 = Image.getInstance("E://TESTPDF/fulubufen.png");//附录部分
        Image image8 = Image.getInstance("E://TESTPDF/fulu1jiancejiyinliebiao.png");//附录 1 检测基因列表
        //基础版
        Image image9 = Image.getInstance("E://TESTPDF/jichubanfulu2yuedushuoming.png");//附录 2 阅读说明

        //非基础版
        Image image10 = Image.getInstance("E://TESTPDF/fulu2hualiaoyaowujiancejieguoshuoming-nojichu.png");//附录 2 化疗药物检测结果说明
        Image image11 = Image.getInstance("E://TESTPDF/fulu3yuedushuoming-nojichu.png");//附录 3 阅读说明

        //非基础版图片集合. 包括 胃癌、肺癌、结直肠癌、乳腺癌全面版|高级版 ctDNA     10个
        Image[] arrImagesFei = new Image[]{image0,image1,image3,image4,image5,image6,image7,image8,image10,image11};

        List<Object> list = new Test1().returnMapAndString();
        Map<URL,Integer> map = (Map<URL, Integer>) list.get(0);
        System.out.println(map.size());
        for(int i=0;i<map.size();i++){
            Integer s = map.get(arrImagesFei[i].getUrl());
            System.out.println(s);

        }
    }

    /**
     * 返回list   0 ->  map<URL,Integer>;   1 ->  fileSourcePath
     * @return
     */
    public List<Object> returnMapAndString() {
        Document document = new Document();
        List<Object> list = new ArrayList<>();
        Map<URL,Integer> map = new HashMap<>();
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document,
                    new FileOutputStream("contentsTestFei1.pdf"));
            document.open();

            // 设置字体 （设置微软雅黑）
            BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\微软雅黑.TTF",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont, 10.5f);

            Paragraph paragraph02 = new Paragraph("start", font);
            Paragraph paragraph03 = new Paragraph("end", font);

            document.add(paragraph02);

            String[] strings = new String[]{"E://TESTPDF/信息页.png"
                    , "E://TESTPDF/jiancejieguojiyongyaotishi.png"
                    , "E://TESTPDF/jiezhichangaiyangbenhuizongjiancejieguohuozong9.png"
                    , "E://TESTPDF/bianyijiyinyiyijiedu.png"
                    , "E://TESTPDF/yaowulinchuangyanjiujiedu.png"
                    , "E://TESTPDF/linchuangyiyibumingjiyinbianyijieguohuizong.png"
                    , "E://TESTPDF/NGSjiancejieguo.png"
                    , "E://TESTPDF/fulubufen.png"
                    , "E://TESTPDF/fulu1jiancejiyinliebiao.png"
                    , "E://TESTPDF/jichubanfulu2yuedushuoming.png"
                    , "E://TESTPDF/fulu2hualiaoyaowujiancejieguoshuoming-nojichu.png"
                    , "E://TESTPDF/fulu3yuedushuoming-nojichu.png"};
            for (int i = 0; i < strings.length; i++) {
                Image image0 = Image.getInstance(strings[i]);
                if (i == 2 || i == 9) {
                    continue;
                } else if (i != 0) {
                    // 设置图片相对于页面的绝对位置
                    image0.setAbsolutePosition(178, 650);
                    // 设置图片的绝对宽度
                    image0.scaleAbsoluteWidth(85f);
                    // 设置图片的绝对高度
                    image0.scaleAbsoluteHeight(16f);

                    document.newPage();
                    document.add(image0);

                    map.put(image0.getUrl(),pdfWriter.getPageNumber());
                    //System.out.println(pdfWriter.getPageNumber());

                } else {

                    // 设置图片相对于页面的绝对位置
                    //image0.setAbsolutePosition(178, 650);
                    // 设置图片的绝对宽度
                    image0.scaleAbsoluteWidth(300f);
                    // 设置图片的绝对高度
                    image0.scaleAbsoluteHeight(150f);

                    document.newPage();
                    document.add(image0);

                    map.put(image0.getUrl(),pdfWriter.getPageNumber());
                    //System.out.println(pdfWriter.getPageNumber());
                }

            }

            list.add(map);
            document.newPage();
            document.add(paragraph03);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileSourcePath = "D:\\IdeaProjects-15.0\\study\\contentsTestFei1.pdf";
        list.add(fileSourcePath);

        return list;
    }
}
