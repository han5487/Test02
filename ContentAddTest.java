package itext.contents;

/**
 * Created by yyx on 2016/9/3.
 */

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.parser.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用来合并目录的类
 * 思路:  1、先把生成的不带目录的PDF,yongyao.pdf分为两段PDF,并命名为start.pdf,end.pdf
 *       2、通过遍历yongyao.pdf获取目录里的关键字的页码从而生成目录,middle.pdf
 *       3、合并start.pdf、middle.pdf、end.pdf
 */
public class ContentAddTest {


    public static void main(String[] args) throws Exception {

        String sourceFile = "E://TESTPDF/contentsTest.pdf";
        String sourceFileFei = "E://TESTPDF/contentsTestFei.pdf";
        String saveLocation = "E://TESTPDF/";
        String reportName = "testContents";
        String reportNameFei = "testContentsFei";
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
        Image[] arrImagesFei = new Image[]{image0,image1,image3,image4,image5,image6,image7,image8,image9,image10,image11};

        //test 基础版
        addContent(sourceFile,arr,arrImages,saveLocation,reportName);

        //test 非基础版
        addContent(sourceFileFei,array,arrImagesFei,saveLocation,reportNameFei);

    }

    /**
     * 给生成好的PDF加一个有页码的目录
     * @param sourcePath    生成好的PDF路径
     * @param arr           目录关键字数组,不同报告有不同关键字数组
     * @param savePath      合并之后要保存PDF的位置
     * @param reportName    合并之后PDF名字
     */
    public static void addContent(String sourcePath, String[] arr, Image[] images
            ,String savePath, String reportName) throws Exception {
        //文件名前缀  ==>    E://TESTPDF/2016-08-24-SDA8-UU-肿瘤基因体检-
        String prefix = sourcePath.substring(0,sourcePath.lastIndexOf(".pdf"))+"-";
        //1、先把生成的不带目录的PDF,yongyao.pdf分为两段PDF,并命名为start.pdf,end.pdf此种形式
        String start = prefix +"start.pdf";
        String end = prefix +"end.pdf";
        copyPdf(sourcePath,start,"1-1");
        copyPdf(sourcePath,end,"2-");
        //2、通过遍历yongyao.pdf获取目录里的关键字的页码从而生成目录,middle.pdf
        String middle = prefix + "middle.pdf";
        createCatalog(sourcePath,arr,images,middle);
        //3、合并start.pdf、middle.pdf、end.pdf
        String files[] = new String[] {start,middle,end};
        new MergeSomePDFs().getPDF(savePath, reportName, files);
    }

    /**
     * 复制pdf文档
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     * @param ranges   复制规则    "1-7"表示复制1到7页、"8-"表示复制从第八页开始到文档末尾
     */
    public static void copyPdf(String sourceFile ,String targetFile, String ranges)throws Exception{
        PdfReader pdfReader = new PdfReader(sourceFile);

        PdfStamper pdfStamper = new PdfStamper(pdfReader , new FileOutputStream(targetFile));
        pdfReader.selectPages(ranges);
        pdfStamper.close();
    }

    /**
     * 返回关键字所在页码和坐标
     * @param filePath
     * @param key
     * @return  List<float[]>  返回关键字所在的坐标和页数 float[0] >> X float[1] >> Y float[2] >> page
     */
    private static List<float[]> getKeyWords(String filePath, final String key, final Image image) {
        final List<float[]> arrays = new ArrayList<>();
            try {
                PdfReader pdfReader = new PdfReader(filePath);
                int pageNum = pdfReader.getNumberOfPages();
                PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(
                        pdfReader);
                for (int i = 1; i <= pageNum; i++) {

                    final int finalI = i;
                    pdfReaderContentParser.processContent(i, new RenderListener() {
                        //此方法是监听PDF里的文字内容
                        @Override
                        public void renderText(TextRenderInfo textRenderInfo) {
                            /*String text = textRenderInfo.getText(); // 整页内容

                            if (null != text && text.contains(key)) {
                                Rectangle2D.Float boundingRectange = textRenderInfo
                                        .getBaseline().getBoundingRectange();
                                float[] resu = new float[3];
                                resu[0] = boundingRectange.x;
                                resu[1] = boundingRectange.y;
                                resu[2] = finalI;
                                arrays.add(resu);
                            }*/
                        }

                        //此方法是监听PDF里的图片内容
                        @Override
                        public void renderImage(ImageRenderInfo arg0) {
                            PdfImageObject image0;
                            try {
                                image0 = arg0.getImage();
                                byte[] imageByte = image0.getImageAsBytes();
                                Image imageInPDF = Image.getInstance(imageByte);
                                if(image0!=null && imageInPDF.equals(image)){
                                    float[] resu = new float[3];
                                    // 0 => x;  1 => y;  2 => z
                                    //z的值始终为1
                                    resu[0] = arg0.getStartPoint().get(0);
                                    resu[1] = arg0.getStartPoint().get(1);
                                    resu[2] = finalI;
                                    arrays.add(resu);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (BadElementException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void endTextBlock() {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void beginTextBlock() {
                            // TODO Auto-generated method stub

                        }
                    });
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return arrays;
    }

    /**
     *
     * @param sourcePath        生成好的PDF路径
     * @param arr               关键字图片对应的文字数组
     * @param images            要匹配的图片集合
     * @param savePath
     * @throws FileNotFoundException
     */
    public static void createCatalog(String sourcePath, String[] arr, Image[] images, String savePath) throws FileNotFoundException {
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
            for(int j = 0; j < 13; j++){
                Paragraph para = new Paragraph("   ");
                para.setFirstLineIndent(145f);
                para.setIndentationRight(50f);
                document.add(para);
            }

            for(int i=0;i<arr.length;i++){
                Paragraph paragraph01 = new Paragraph(arr[i],font);
                Chunk chunk0101 = new Chunk(new DottedLineSeparator());

                //获取页码
                List<float[]> floats = getKeyWords(sourcePath,arr[i],images[i]);
                float f = -1f;
                if(floats.size()==1){//匹配的图片在PDF中是唯一的,size = 1
                    f = floats.get(0)[2];
                }else {
                    System.out.println("error *******   size = " +floats.size());
                }

                //添加页码
                Chunk chunk0102 = new Chunk((int)(f+1)+"");
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
