package vt.thanhnd58.utils;

import vt.thanhnd58.constant.PluginConstant;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class FileUtils {

    public static File getCurrentFolderContainAppUsingDot() {
        return new File(".");
    }

    public static String getCurrentFolderContainAppUsingUserDir() {
        return System.getProperty("user.dir");
    }

    public static boolean overrideFile(String source, String dest) {
        try {
            Files.copy(Paths.get(source), Paths.get(dest).resolveSibling(getFileNameFromAbsPath(dest)), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFilePathInFolderContainApp(String fileName) {
        String folder = getCurrentFolderContainAppUsingUserDir();
        String filePath = folder + "\\" + fileName;
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return folder + "\\" + fileName;
    }

    public static Properties getPropFileFromFolderContainApp(String fileName) {
        String filePath = getFilePathInFolderContainApp(fileName);
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            // load properties from file
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close objects
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static String getFileNameFromAbsPath(String input) {
        return input.substring(input.lastIndexOf("\\") + 1);
    }


    public static Properties getPropFileFromClasspath(String fileName) {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = FileUtils.class.getClassLoader()
                    .getResourceAsStream(fileName);

            // load properties from file
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // close objects
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static boolean savePropFileFromFolderContainApp(Properties props, String fileName) {
        String filePath = getFilePathInFolderContainApp(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            props.store(fos, null);
            fos.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean overrideUpdaterJarFile() {
        Properties projectProps = FileUtils.getPropFileFromFolderContainApp(PluginConstant.PROJECT_PROPS);
        String checkNewVersionUpdater = projectProps.getProperty("updater");
        if (checkNewVersionUpdater == null || checkNewVersionUpdater.equals("false")) {
            System.out.println("not found new version updater");
            return false;
        }
        String destPath = FileUtils.getFilePathInFolderContainApp(PluginConstant.UPDATER_JAR_FILE);
        String sourcePath = FileUtils.getFilePathInFolderContainApp(PluginConstant.TEMP_UPDATER_JAR_FILE);
        boolean success = FileUtils.overrideFile(sourcePath, destPath);
        if (success) {
            System.out.println("success override updater");
            //delete temp file
            try {
                File file = new File(sourcePath);
                boolean deleteTempFile = file.delete();
                if (deleteTempFile) {
                    System.out.println("deleted " + PluginConstant.TEMP_UPDATER_JAR_FILE);
                } else {
                    System.out.println("cannot delete " + PluginConstant.TEMP_UPDATER_JAR_FILE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // save file project.properties
            projectProps.put("updater", String.valueOf(false));
            FileUtils.savePropFileFromFolderContainApp(projectProps, PluginConstant.PROJECT_PROPS);
            return true;
        } else {
            return false;
        }
    }
}

