/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.hj2eplatform.resource.dto;

import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.hj2eplatform.core.utils.SystemConfig;

/**
 *
 * @author HuyHoang
 */
public class Folder implements Serializable {
    public static final String FOLDER_PATH_SPACE= "/";
    private String path;
    private String folderPath;
    private String folderName;
    private java.io.File content;
    private List<Folder> fileList;
    public boolean hasSubFolder = false;
    boolean isRoot = false;
    public final static String ROOT_IMG_FOLDER = SystemConfig.getResource("hj2eplatform.directory.fileupload");

    public Folder(java.io.File file, String folderPath) {
        this.content = file;
        this.path = file.getPath();
        this.folderName = file.getName();
        this.folderPath = folderPath;
    }

    public Folder() {
    }

    public Folder(String path, String forderPath) {
        this.path = path;
        this.content = new java.io.File(path);

        if (!this.content.exists()) {
            this.content.mkdirs();
        }

        this.folderPath = forderPath;

    }

    public Folder(String path) {
        this.path = path;
        this.content = new java.io.File(path);

        if (!this.content.exists()) {
            this.content.mkdirs();
        }
    }

    public List<Folder> getAllSubFolder() {
        List<Folder> files = new ArrayList<Folder>();
        files = this.getFile(Boolean.TRUE);
        if (files != null && files.size() > 0) {
            hasSubFolder = true;
        }
        return files;
    }

    public boolean hasSubFolder() {
        return hasSubFolder;
    }

    private List<Folder> getFile(Boolean loadFolderOnly) {
        java.io.File parent = new java.io.File(path);
        java.io.File[] subDirs;
        subDirs = parent.listFiles(new MyFileFilter(loadFolderOnly));
        if (subDirs == null) {
            return null;
        }
        List<Folder> directoryList = new ArrayList<Folder>();
        for (java.io.File subDir : subDirs) {
            directoryList.add(new Folder(subDir, this.folderPath + FOLDER_PATH_SPACE + subDir.getName()));
        }
        return directoryList;
    }

    public String getPath() {
        return path;
    }

    public List<Folder> getFileList() {
        return fileList;
    }

    public void setFileList(List<Folder> fileList) {
        this.fileList = fileList;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public java.io.File getContent() {
        return content;
    }

    public void setContent(java.io.File content) {
        this.content = content;
    }

    public void setHasSubFolder(boolean hasSubFolder) {
        this.hasSubFolder = hasSubFolder;
    }

    public static String extension(String fullPath) {
        int dot = fullPath.lastIndexOf(".");
        return fullPath.substring(dot + 1);
    }

    public static String filename(String fileName) { // gets filename without extension
        int dot = fileName.lastIndexOf(".");
        return fileName.substring(0, dot);
    }

    class MyFileFilter implements FileFilter {

        private boolean loadFolderOnly = false;

        public MyFileFilter(boolean loadFolderOnly) {
            this.loadFolderOnly = loadFolderOnly;
        }

        public boolean accept(java.io.File pathname) {
            if (loadFolderOnly) {
                return pathname.isDirectory();
            } else {
                return pathname.isFile();
            }

        }
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFolderName() {
     
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
