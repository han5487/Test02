package itext.contents;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import itext.contents.ContentAddTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yyx on 2016/9/7.
 */
public class ContentAddTest1 {

    public static void main(String[] args) throws Exception {

        List<Object> list = new Test1().returnMapAndString();

        //获取Test1生成PDF之后记录的页码映射
        @SuppressWarnings("unchecked")
        Map<Image,Integer> map = (Map<Image, Integer>) list.get(0);
        String sourceFile = "E://TESTPDF/contentsTest.pdf";
        //获取Test1生成的PDF
        String sourceFileFei = (String) list.get(1);
        String saveLocation = "E://TESTPDF/";
        String reportName = "testContents";
        String reportNameFei = "testContentsFei1";
        //基础版图片集合对应的目录文字数组. 包括 胃癌、肺癌、结直肠癌、乳腺癌基础版    4个
        String[] arr = new String[]{"个人信息"
                ,"检测结果及用药提示"
                ,"样本检测结果汇总（9个基因）"
                ,"变异基因意义解读"
                ,"药物临床研究解读"
                ,"临床意义不明基因变异结果汇总"
                ,"NGS 检测结果"
                ,"附录部分"
                ,"附录 1 检测基因列表"
                ,};
        //非基础版图片集合对应的目录文字数组. 包括 胃癌、肺癌、结直肠癌、乳腺癌全面版|高级版 ctDNA     10个
        String[] array = new String[]{"个人信息"
                ,"检测结果及用药提示"
                ,"变异基因意义解读"
                ,"药物临床研究解读"
                ,"临床意义不明基因变异结果汇总"
                ,"NGS 检测结果"
                ,"附录部分"
                ,"附录 1 检测基因列表"
                ,"附录 2 化疗药物检测结果说明"
                ,"附录 3 阅读说明"};

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

        //基础版图片集合. 包括 胃癌、肺癌、结直肠癌、乳腺癌基础版     4个
        Image[] arrImages = new Image[]{image0,image1,image2,image3,image4,image5,image6,image7,image8,image9};

        //非基础版图片集合. 包括 胃癌、肺癌、结直肠癌、乳腺癌全面版|高级版 ctDNA     10个
        Image[] arrImagesFei = new Image[]{image0,image1,image3,image4,image5,image6,image7,image8,image10,image11};

        //test 基础版
        //ContentAddTest.addContent(sourceFile,arr,arrImages,saveLocation,reportName);

        //test 非基础版
        addContent1(sourceFileFei,array,arrImagesFei,saveLocation,reportNameFei,map);


    }


    /**
     * 给生成好的PDF加一个有页码的目录
     * @param sourcePath    生成好的PDF路径
     * @param arr           目录关键字数组,不同报告有不同关键字数组
     * @param images        保存的是章节图片集合
     * @param savePath      合并之后要保存PDF的位置
     * @param reportName    合并之后PDF名字
     * @param map           章节对应页码集合
     * @throws Exception
     */
    public static void addContent1(String sourcePath, String[] arr, Image[] images
            ,String savePath, String reportName, Map<Image,Integer> map) throws Exception {
        //文件名前缀  ==>    E://TESTPDF/2016-08-24-SDA8-UU-肿瘤基因体检-
        String prefix = sourcePath.substring(0,sourcePath.lastIndexOf(".pdf"))+"-";
        //1、先把生成的不带目录的PDF,yongyao.pdf分为两段PDF,并命名为start.pdf,end.pdf此种形式
        String start = prefix +"start1.pdf";
        String end = prefix +"end1.pdf";
        ContentAddTest.copyPdf(sourcePath,start,"1-1");
        ContentAddTest.copyPdf(sourcePath,end,"2-");
        //2、通过遍历yongyao.pdf获取目录里的关键字的页码从而生成目录,middle.pdf
        String middle = prefix + "middle1.pdf";
        createCatalog(arr,images,middle,map);
        //3、合并start.pdf、middle.pdf、end.pdf
        String files[] = new String[] {start,middle,end};
        MergeSomePDFs mergeSomePDFs = new MergeSomePDFs();
        mergeSomePDFs.getPDF(savePath, reportName, files);
    }


    /**
     *  只遍历一遍PDF的生成目录方法
     * @param arr               关键字图片对应的文字数组
     * @param images            要匹配的图片集合
     * @param savePath
     * @param map               目录图片与页码集合映射
     * @throws FileNotFoundException
     */
    public static void createCatalog(String[] arr, Image[] images, String savePath, Map<Image,Integer> map) throws FileNotFoundException {
        Document document = new Document();
        FileOutputStream res = new FileOutputStream(savePath);
        try {

            PdfWriter.getInstance(document,res);

            document.open();

            Image image = Image.getInstance("E://TESTPDF/contenthead.png");
            // 设置图片相对于页面的绝对位置
            image.setAbsolutePosition(178, 560);
            // 设置图片的绝对宽度
            image.scaleAbsoluteWidth(417f);
            // 设置图片的绝对高度
            image.scaleAbsoluteHeight(80f);
            document.add(image);

            // 设置字体 （设置微软雅黑）
            BaseFont baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\微软雅黑.TTF",
                    BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont, 10.5f);//  五号

            //开始添加目录正文
            //先用几段空白段落填充文档
            for(int i = 0; i < 14; i++){
                Paragraph par = new Paragraph("   ");
                //par.setFirstLineIndent(145f);
                //par.setIndentationRight(50f);
                document.add(par);
            }

            for(int i=0;i<arr.length;i++){
                Paragraph paragraph01 = new Paragraph(arr[i],font);
                Chunk chunk0101 = new Chunk(new DottedLineSeparator());

                //获取页码
                Integer page = map.get(images[i].getUrl());
                //添加页码
                Chunk chunk0102 = new Chunk(page+1+"");
                if(i!=0){
                    paragraph01.setSpacingBefore(15f);
                }else {
                    paragraph01.setSpacingBefore(8f);
                }


                paragraph01.add(chunk0101);
                paragraph01.add(chunk0102);
                paragraph01.setFirstLineIndent(145f);
                paragraph01.setIndentationRight(50f);

                document.add(paragraph01);
            }

            document.close();
        }catch (DocumentException e){
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
