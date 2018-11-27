import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DealFile {
    /**
     * 将文件内容读取为字符串
     * @param filePath
     * @return
     */
    public static String readFile2String(String filePath) {
        if (filePath == null || filePath.trim().length() == 0)
            return null;

        String str = null;
        try {
            str = FileUtils.readFileToString(new File(filePath), "UTF-8");
            System.out.println("Read file=" + filePath + " succeed.");
        } catch (Exception e) {
            System.out.println("Read file=" + filePath + " failed!!");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将字符串写入文件
     * @param filePath
     * @param jsonFormat
     */
    public static void writeString2File(String filePath, String jsonFormat) {
        try {
            FileUtils.writeStringToFile(new File(filePath), jsonFormat, "UTF-8");
            System.out.println("Josn write to file=" + filePath + " succeed.");
        } catch (IOException ioe) {
            System.out.println("Josn write to file=" + filePath + " failed.!!");
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }
    }

    /**
     * 加载配置文件信息
     * @param filePath
     * @return
     */
    public static Properties loadProperties(String filePath) {
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = DealFile.class.getClassLoader().getResourceAsStream(filePath);
        // 使用properties对象加载输入流
        try {
            properties.load(in);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return properties;
    }

    /**
     * 获取文本文件的内容，将其每一行当做元素存储在集合中
     * @param filePath 文件路径
     * @return
     */
    public static List<String> getFileLines(String filePath) {
        if (filePath == null) {
            return null;
        }

        List list = null;
        try {
            list = FileUtils.readLines(new File(filePath));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            ioe.printStackTrace();
        }

        return list;
    }


    /**
     * 获取某一文件夹下所有文件的文件名
     * @param dirName
     * @return
     */
    public static List<String> getAllFileName(String dirName) {
        if (dirName == null) {
            return null;
        }

        File dirFile = new File(dirName);
        if (!dirFile.isDirectory()) {
            return null;
        }

        String[] fileNames = dirFile.list();
        if (fileNames == null) return null;

        return Arrays.asList(fileNames);
    }
}

