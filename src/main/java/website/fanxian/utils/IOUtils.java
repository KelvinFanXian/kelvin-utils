package website.fanxian.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Kelvin范显
 * @createDate 2018年10月17日
 */
public class IOUtils {
    ///--字节流
    /**
     * 1.复制输入流的内容到输出流
     * Java9中，InputStream$transferTo(OutputStream out)
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copy(InputStream input,
                            OutputStream output) throws IOException {
        byte[] buf = new byte[4096];
        int bytesRead = 0;
        while((bytesRead = input.read(buf))!=-1){
            output.write(buf, 0, bytesRead);
        }
    }

    /**
     * 2.将文件读入字节数组-->copy(InputStream input, OutputStream output)
     * @param fileName
     * @return
     * @throws IOException
     */
    public static byte[] readFileToByteArray(String fileName) throws IOException{
        InputStream input = new FileInputStream(fileName);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try{
            copy(input, output);
            return output.toByteArray();
        }finally{
            input.close();
        }
    }

    /**
     * 3.将字节数组写到文件
     * @param fileName
     * @param data
     * @throws IOException
     */
    public static void writeByteArrayToFile(String fileName,
                                            byte[] data) throws IOException{
        OutputStream output = new FileOutputStream(fileName);
        try{
            output.write(data);
        }finally{
            output.close();
        }
    }

    ///--字符流

    /**
     * 复制Reader到Writer
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copy(final Reader input,
                            final Writer output) throws IOException {
        char[] buf = new char[4096];
        int charsRead = 0;
        while((charsRead = input.read(buf)) != -1) {
            output.write(buf, 0, charsRead);
        }
    }

    /**
     * 将文件全部内容读到一个字符串
     * @param fileName
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String readFileToString(final String fileName,
                                          final String encoding) throws IOException{
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), encoding));
            StringWriter writer = new StringWriter();
            copy(reader, writer);
            return writer.toString();
        }finally{
            if(reader!=null){
                reader.close();
            }
        }
    }

    /**
     *将字符串写到文件
     * @param fileName
     * @param data
     * @param encoding
     * @throws IOException
     */
    public static void writeStringToFile(final String fileName,
                                         final String data, final String encoding) throws IOException {
        Writer writer = null;
        try{
            writer = new OutputStreamWriter(
                    new FileOutputStream(fileName), encoding);
            writer.write(data);
        }finally{
            if(writer!=null){
                writer.close();
            }
        }
    }

    /**
     * 按行将多行数据写到文件
     * @param fileName
     * @param encoding
     * @param lines
     * @throws IOException
     */
    public static void writeLines(final String fileName, final String encoding,
                                  final Collection<?> lines) throws IOException {
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(fileName, encoding);
            for(Object line : lines){
                writer.println(line);
            }
        }finally{
            if(writer!=null){
                writer.close();
            }
        }
    }

    /**
     * 按行将文件内容读取到一个列表中
     * @param fileName
     * @param encoding
     * @return
     * @throws IOException
     */
    public static List<String> readLines(final String fileName,
                                         final String encoding) throws IOException{
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), encoding));
            List<String> list = new ArrayList<>();
            String line = reader.readLine();
            while(line!=null){
                list.add(line);
                line = reader.readLine();
            }
            return list;
        }finally{
            if(reader!=null){
                reader.close();
            }
        }
    }

    /// 文件和目录操作

    /**
     * 计算一个目录下的所有文件的大小（包括子目录）
     * @param directory
     * @return
     */
    public static long sizeOfDirectory(final File directory) {
        long size = 0;
        if(directory.isFile()) {
            return directory.length();
        } else {
            for(File file : directory.listFiles()) {
                if(file.isFile()) {
                    size += file.length();
                } else {
                    size += sizeOfDirectory(file);
                }
            }
        }
        return size;
    }

    /**
     * 在一个目录下，查找所有给定文件名的文件
     * @param directory
     * @param fileName
     * @return
     */
    public static Collection<File> findFile(final File directory,
                                            final String fileName) {
        List<File> files = new ArrayList<>();
        for(File f : directory.listFiles()) {
            if(f.isFile() && f.getName().equals(fileName)) {
                files.add(f);
            } else if(f.isDirectory()) {
                files.addAll(findFile(f, fileName));
            }
        }
        return files;
    }

    /**
     * 删除非空目录
     * @param file
     * @throws IOException
     */
    public static void deleteRecursively(final File file) throws IOException {
        if(file.isFile()) {
            if(!file.delete()) {
                throw new IOException("Failed to delete "
                        + file.getCanonicalPath());
            }
        } else if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                deleteRecursively(child);
            }
            if(!file.delete()) {
                throw new IOException("Failed to delete "
                        + file.getCanonicalPath());
            }
        }
    }
}
