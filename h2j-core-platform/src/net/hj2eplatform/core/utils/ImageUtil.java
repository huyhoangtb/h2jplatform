package net.hj2eplatform.core.utils;

///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.hj2eplatform.utils;
//
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
//import java.awt.AlphaComposite;
//import java.awt.Container;
//import java.awt.Graphics2D;
//import java.awt.GraphicsConfiguration;
//import java.awt.Image;
//import java.awt.MediaTracker;
//import java.awt.RenderingHints;
//import java.awt.Toolkit;
//import java.awt.Transparency;
//import java.awt.image.BufferedImage;
//import java.awt.image.BufferedImageOp;
//import java.awt.image.ConvolveOp;
//import java.awt.image.Kernel;
//import java.io.BufferedOutputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//import javax.imageio.ImageIO;
//import sun.awt.image.BufferedImageGraphicsConfig;
//
///**
// *
// * @author manhvv
// */
//public class ImageUtil {
//
//    public static byte[] resizeImage(File imageFile, int width, int height) throws IOException {
//        BufferedImage resizedImage = ImageIO.read(imageFile);
//        return resizeImage(resizedImage, width, height, "jpg", resizedImage.getType());
//    }
//
//    public static byte[] resizeImage(String url, int width, int height) throws MalformedURLException, IOException {
//        BufferedImage resizedImage = ImageIO.read(new URL(url));
//        return resizeImage(resizedImage, width, height, "jpg", resizedImage.getType());
//    }
//
//    public static byte[] resizeImage(File imageFile, int width, int height, String format) throws IOException {
//        BufferedImage resizedImage = ImageIO.read(imageFile);
//        return resizeImage(resizedImage, width, height, format, resizedImage.getType());
//    }
//
//    public static byte[] resizeImage(String url, int width, int height, String format) throws MalformedURLException, IOException {
//        BufferedImage resizedImage = ImageIO.read(new URL(url));
//        return resizeImage(resizedImage, width, height, format, resizedImage.getType());
//    }
////ham cu
//
//
//    public static File resizeImageFile(String url, String fileName, int width, int height) throws MalformedURLException, IOException, Exception {
////        BufferedImage resizedImage = ImageIO.read(new URL(url));
//        fileName = fileName + "_" + width + "x" + height + ".jpg";
//        return createThumbnail(url, fileName, width, height);
//    }
//
//    public static File resizeImageFile(String name, File imageFile, int width, int height, String format) throws IOException {
//        BufferedImage resizedImage = ImageIO.read(imageFile);
//        String fileName = name;
//        try {
////            fileName = fileName.substring(0, fileName.length() - 5) + "_" + width + "x" + height + ".jpg";
//            fileName = name + "_" + width + "x" + height + ".jpg";
//        } catch (Exception e) {
//            fileName = fileName + "_" + width + "x" + height + ".jpg";
//        }
//        return resizeImageFile(resizedImage, width, height, fileName, format, resizedImage.getType());
//    }
//
//    public static File resizeImageFile(String url, String fileName, int width, int height, String format) throws MalformedURLException, IOException {
//        BufferedImage resizedImage = ImageIO.read(new URL(url));
//        return resizeImageFile(resizedImage, width, height, fileName, format, resizedImage.getType());
//    }
//
//    private static byte[] resizeImage(BufferedImage originalImage, int width, int height, String format, int type) throws IOException {
//        BufferedImage resizedImage = resizeImage(originalImage, width, height, type);
//        // O P E N
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
//        // W R I T E
//        ImageIO.write(resizedImage, format /* "png" "jpeg" ... format desired */, baos);
//        // C L O S E
//        baos.flush();
//        byte[] resultImageAsRawBytes = baos.toByteArray();
//        baos.close();
//        return resultImageAsRawBytes;
//    }
//
//    private static byte[] resizeImageWithHint(BufferedImage originalImage, int width, int height, String format, int type) throws IOException {
//        BufferedImage resizedImage = resizeImageWithHint(originalImage, width, height, type);
//        // O P E N
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
//        // W R I T E
//        ImageIO.write(resizedImage, format /* "png" "jpeg" ... format desired */, baos);
//        // C L O S E
//        baos.flush();
//        byte[] resultImageAsRawBytes = baos.toByteArray();
//        baos.close();
//        return resultImageAsRawBytes;
//    }
//
//    private static File resizeImageFile(BufferedImage originalImage, int width, int height, String fileName, String format, int type) throws IOException {
//        BufferedImage resizedImage = resizeImageWithHint(originalImage, width, height, type);
//        // O P E N
//        File file = new File(fileName);
////        File file = File.createTempFile(fileName, "");
//        // W R I T E
//        ImageIO.write(resizedImage, format /* "png" "jpeg" ... format desired */, file);
//        return file;
//    }
//
//    private static File resizeImageFileWithHint(BufferedImage originalImage, int width, int height, String format, int type) throws IOException {
//        BufferedImage resizedImage = resizeImageWithHint(originalImage, width, height, type);
//        // O P E N
//        File file = new File("snap." + format);
////        File file = File.createTempFile("snap", "."+ format);
//        // W R I T E
//        ImageIO.write(resizedImage, format /* "png" "jpeg" ... format desired */, file);
//        return file;
//    }
//
//    private static BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) {
//        BufferedImage resizedImage = new BufferedImage(width, height, type);
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(originalImage, 0, 0, width, height, null);
//        g.dispose();
//        return resizedImage;
//    }
//
//    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int width, int height, int type) {
////        BufferedImage resizedImage = new BufferedImage(width, height, type);
////        Graphics2D g = resizedImage.createGraphics();
////        g.drawImage(originalImage, 0, 0, width, height, null);
////        g.dispose();
////        g.setComposite(AlphaComposite.Src);
////        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
////                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
////        g.setRenderingHint(RenderingHints.KEY_RENDERING,
////                RenderingHints.VALUE_RENDER_QUALITY);
////        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
////                RenderingHints.VALUE_ANTIALIAS_ON);
////        return resizedImage;
//        return resizeTrick(originalImage, width, height, type);
//    }
//
//    private static BufferedImage resizeTrick(BufferedImage image, int width, int height, int type) {
//        image = createCompatibleImage(image);
////        image = resize(image, 200, 300, type);
//        image = blurImage(image);
//        image = resize(image, width, height, type);
//        return image;
//    }
//
//    private static BufferedImage createCompatibleImage(BufferedImage image) {
//        GraphicsConfiguration gc = BufferedImageGraphicsConfig.getConfig(image);
//        int w = image.getWidth();
//        int h = image.getHeight();
//        BufferedImage result = gc.createCompatibleImage(w, h, Transparency.TRANSLUCENT);
//        Graphics2D g2 = result.createGraphics();
//        g2.drawRenderedImage(image, null);
//        g2.dispose();
//        return result;
//    }
//
//    private static BufferedImage resize(BufferedImage image, int width, int height, int type) {
////        int type1 = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
//        BufferedImage resizedImage = new BufferedImage(width, height, type);
//        Graphics2D g = resizedImage.createGraphics();
//        g.setComposite(AlphaComposite.Src);
//
//        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//
//        g.setRenderingHint(RenderingHints.KEY_RENDERING,
//                RenderingHints.VALUE_RENDER_QUALITY);
//
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//
//        g.drawImage(image, 0, 0, width, height, null);
//        g.dispose();
//        return resizedImage;
//    }
//
//    public static BufferedImage blurImage(BufferedImage image) {
//        float ninth = 1.0f / 9.0f;
//        float[] blurKernel = {
//            ninth, ninth, ninth,
//            ninth, ninth, ninth,
//            ninth, ninth, ninth
//        };
//
//        Map map = new HashMap();
//
//        map.put(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//
//        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//
//        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        RenderingHints hints = new RenderingHints(map);
//        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, blurKernel), ConvolveOp.EDGE_NO_OP, hints);
//        return op.filter(image, null);
//    }
//
//    public static File createThumbnail(File imgFilePath, String name, int thumbWidth, int thumbHeight) throws Exception {
//        Image image = Toolkit.getDefaultToolkit().createImage(read(imgFilePath));
//        MediaTracker mediaTracker = new MediaTracker(new Container());
//        mediaTracker.addImage(image, 0);
//        mediaTracker.waitForID(0);
//        BufferedImage thumbImage = new BufferedImage(thumbWidth,
//                thumbHeight, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics2D = thumbImage.createGraphics();
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
//        File f = new File(name);
//        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
//        int quality = 100;
//        param.setQuality((float) quality / 100.0f, false);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(thumbImage);
//        out.close();
//        return f;
//    }
//
//    public static File createThumbnail(String imgFilePath, String name, int thumbWidth, int thumbHeight) throws Exception {
//
//        Image image = Toolkit.getDefaultToolkit().getImage(new URL(imgFilePath));
//        MediaTracker mediaTracker = new MediaTracker(new Container());
//        mediaTracker.addImage(image, 0);
//        mediaTracker.waitForID(0);
////        double thumbRatio = (double) thumbWidth / (double) thumbHeight;
////        int imageWidth = image.getWidth(null);
////        int imageHeight = image.getHeight(null);
////        double imageRatio = (double) imageWidth / (double) imageHeight;
////        if (thumbRatio < imageRatio) {
////            thumbHeight = (int) (thumbWidth / imageRatio);
////        } else {
////            thumbWidth = (int) (thumbHeight * imageRatio);
////        }
//        BufferedImage thumbImage = new BufferedImage(thumbWidth,
//                thumbHeight, BufferedImage.TYPE_INT_RGB);
//        Graphics2D graphics2D = thumbImage.createGraphics();
//        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
//        File f = new File(name);
//        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
//        int quality = 100;
//        param.setQuality((float) quality / 100.0f, false);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(thumbImage);
//        out.close();
//        return f;
//    }
//
//    public static byte[] read(File file) throws IOException {
//
//        ByteArrayOutputStream ous = null;
//        InputStream ios = null;
//        try {
//            byte[] buffer = new byte[4096];
//            ous = new ByteArrayOutputStream();
//            ios = new FileInputStream(file);
//            int read = 0;
//            while ((read = ios.read(buffer)) != -1) {
//                ous.write(buffer, 0, read);
//            }
//        } finally {
//            try {
//                if (ous != null) {
//                    ous.close();
//                }
//            } catch (IOException e) {
//            }
//
//            try {
//                if (ios != null) {
//                    ios.close();
//                }
//            } catch (IOException e) {
//            }
//        }
//        return ous.toByteArray();
//    }
//}
