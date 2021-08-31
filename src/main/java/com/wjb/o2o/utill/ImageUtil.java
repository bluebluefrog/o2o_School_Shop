package com.wjb.o2o.utill;

import com.wjb.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {

    private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final Random random = new Random();

    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 将CommonsMultipartFile转换成File
     * @param cFile
     * @return
     */
    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
        File file = new File(cFile.getOriginalFilename());
        try {
            cFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
        return file;
    }

    /**
     * 处理缩略图并返回图片相对路径
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail,String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relative addr is:" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目录路径所涉及到的目录
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath()+targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机的文件，每个文件名都不一样，当前的时间年月日小时分钟描述+5位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        //获取随机的5位数
        int rannum = random.nextInt(89999) + 10000;
        String nowTimestr = dateFormat.format(new Date());
        return nowTimestr + rannum;
    }

    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("C:\\Users\\user\\Pictures\\壁纸\\jellybear.jpg"))
                .size(200, 200).watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f).outputQuality(0.8f).toFile("C:\\Users\\user\\Pictures\\壁纸\\shuiyinImage\\newjellybear.jpg");
    }
    /**
        * storePath是文件的路径还是目录的路径， 如果storePath是文件路径则删除该文件，
            * 如果storePath是目录路径则删除该目录下的所有文件
	 *
             * @param storePath
	 */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);//得到原来图片储存的路径位置
        if(fileOrPath.exists()){//如果存在
            if(fileOrPath.isDirectory()) {//是一个目录，删除下面的所有文件
                File files[]=fileOrPath.listFiles();//列举额出所有的文件
                for (int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();//如果是文件则直接删除
        }
    }

    public static String generateNorMalImg(ImageHolder thumbnail,String targetAddr){
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relative addr is:" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return relativeAddr;
    }
}
