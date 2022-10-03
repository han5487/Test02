package itext.contents;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


/**
 * Created by yyx on 2016/9/3.
 */
public class MergeSomePDFs {

    /**
     * 得到生成的PDF文件（多个合并）
     * @param savepath 文件保存的路径
     * @param rName 报表名称
     * @param files 生成的文件
     * @return String
     */
    public String getPDF(String savepath, String rName, String[] files)
    {
        String getResultName = null;
        savepath = savepath + rName + ".pdf";
        //如果是多个PDF文件进行合并
        if (files.length > 1)
        {
            //多个PDF文件合并
            mergePdfFiles(files, savepath);
            getResultName = savepath.subSequence(savepath.lastIndexOf("/"),
                    savepath.length()).toString();
        }
        else
        {
            if (reNameFile(files, savepath))
            {
                getResultName = rName + ".pdf";
            }
        }
        return getResultName;
    }
    /**
     * 多个PDF合并功能
     * @param files 多个PDF的路径
     * @param savepath 生成的新PDF路径
     * @return boolean boolean
     */
    public static boolean mergePdfFiles(String[]files,String savepath)
    {
        try
        {
            Document document = new Document(new PdfReader(files[0]).getPageSize(1));
            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));
            document.open();
            for (int i = 0; i < files.length; i++)
            {
                PdfReader reader = new PdfReader(files[i]);
                int n = reader.getNumberOfPages();
                for (int j = 1; j <= n; j++)
                {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }
            document.close();
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (DocumentException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件重命名
     */
    public static boolean reNameFile(String []oldName,String newName)
    {
        File oldfile = null;
        if (oldName != null)
        {
            for (int i = 0;i < oldName.length;i++)
            {
                oldfile = new File(oldName[i]);
            }
            if (oldfile == null)
            {
                return false;
            }
            File newfile = new File(newName);
            if (oldfile.renameTo(newfile))
            {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args)
    {
        String saveLocation = "E://TESTPDF/";
        String reportName = "chaptercontents";
        String files[] = new String[] {"E://TESTPDF/chapter.pdf","E://TESTPDF/contents.pdf"};
        new MergeSomePDFs().getPDF(saveLocation, reportName, files);

    }

}
