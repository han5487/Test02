package itext.contents;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by yyx on 2016/9/5.
 */
public class Test {

    public static void main(String[] args){
        Document document = new Document();

        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream("contentsTestFei.pdf"));
            document.open();

            // 设置字体 （设置微软雅黑）
            BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\微软雅黑.TTF",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont, 10.5f);

            Paragraph paragraph02 = new Paragraph("start",font);
            Paragraph paragraph03 = new Paragraph("end",font);

            document.add(paragraph02);

            String[] strings = new String[]{"E://TESTPDF/信息页.png"
                    ,"E://TESTPDF/jiancejieguojiyongyaotishi.png"
                    ,"E://TESTPDF/jiezhichangaiyangbenhuizongjiancejieguohuozong9.png"
                    ,"E://TESTPDF/bianyijiyinyiyijiedu.png"
                    ,"E://TESTPDF/yaowulinchuangyanjiujiedu.png"
                    ,"E://TESTPDF/linchuangyiyibumingjiyinbianyijieguohuizong.png"
                    ,"E://TESTPDF/NGSjiancejieguo.png"
                    ,"E://TESTPDF/fulubufen.png"
                    ,"E://TESTPDF/fulu1jiancejiyinliebiao.png"
                    ,"E://TESTPDF/jichubanfulu2yuedushuoming.png"
                    ,"E://TESTPDF/fulu2hualiaoyaowujiancejieguoshuoming-nojichu.png"
                    ,"E://TESTPDF/fulu3yuedushuoming-nojichu.png"};
            for(int i = 0;i<strings.length;i++){
                Image image0 = Image.getInstance(strings[i]);
                if(i==2){
                    continue;
                }else if(i!=0){
                    // 设置图片相对于页面的绝对位置
                    image0.setAbsolutePosition(178, 650);
                    // 设置图片的绝对宽度
                    image0.scaleAbsoluteWidth(85f);
                    // 设置图片的绝对高度
                    image0.scaleAbsoluteHeight(16f);

                    document.newPage();
                    document.add(image0);

                } else {

                    // 设置图片相对于页面的绝对位置
                    //image0.setAbsolutePosition(178, 650);
                    // 设置图片的绝对宽度
                    image0.scaleAbsoluteWidth(300f);
                    // 设置图片的绝对高度
                    image0.scaleAbsoluteHeight(150f);

                    document.newPage();
                    document.add(image0);
                }

            }

            document.newPage();
            document.add(paragraph03);

            document.close();
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
