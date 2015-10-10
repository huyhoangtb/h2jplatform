/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.writer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.hj2eplatform.resource.dto.Folder;
import org.imgscalr.Scalr;

/**
 *
 * @author hoang_000
 */
public class ImageOptimizeWriter {

    private File fileContent;
    private String imageSrcUrl;
    private final static List<ImageSize> optimizeWriterFolder;

    static {
        optimizeWriterFolder = new ArrayList<ImageSize>();
        optimizeWriterFolder.add(ImageSize.XLARGE);
        optimizeWriterFolder.add(ImageSize.LARGE);
        optimizeWriterFolder.add(ImageSize.MEDIUM);
        optimizeWriterFolder.add(ImageSize.SMALL);
        optimizeWriterFolder.add(ImageSize.THUMB);
    }

    public ImageOptimizeWriter(String imageSrcUrl) {
        this.fileContent = new File(Folder.ROOT_IMG_FOLDER + File.separator + imageSrcUrl);
        this.imageSrcUrl = imageSrcUrl;
    }

    public void output() {
        try {
           BufferedImage resizeImageJpg = ImageIO.read(fileContent);
            for (ImageSize imageSize : optimizeWriterFolder) {
                resizeImageJpg
                        = Scalr.resize(resizeImageJpg, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH,
                                imageSize.getWidth(), Scalr.THRESHOLD_BALANCED_SPEED);
                writeData(resizeImageJpg, imageSize.getFolder(), imageSrcUrl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeData(BufferedImage resizeImageJpg, String folder, String fileName) throws IOException {
            File file = new File(Folder.ROOT_IMG_FOLDER + File.separator + folder + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            
            InputStream inputStream = null;
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(resizeImageJpg, Folder.extension(fileName), os);
            inputStream = new ByteArrayInputStream(os.toByteArray());
            String folderOutput = Folder.ROOT_IMG_FOLDER + File.separator + folder + File.separator + fileName;
            File fileOutput = new java.io.File(folderOutput);
            if(!fileOutput.getParentFile().exists()) {
                fileOutput.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(fileOutput);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            inputStream.close();
            out.flush();
            out.close();
           
    }

}

enum ImageSize {

    XLARGE(1000, "xlarge", "Ảnh rất lơn"),
    LARGE(800, "large", "Ảnh lớn"),
    MEDIUM(600, "medium", "Ảnh trung bình"),
    SMALL(150, "small", "Ảnh nhỏ"),
    THUMB(50, "thumb", "Ảnh thumb");
    private Integer width;
    private String desc;
    private String folder;

    ImageSize(Integer width, String folder, String desc) {
        this.width = width;
        this.folder = folder;
        this.desc = desc;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

}
