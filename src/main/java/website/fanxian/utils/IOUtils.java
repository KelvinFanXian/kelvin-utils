package website.fanxian.utils;

import java.io.*;

/**
 * @author Kelvin范显
 * @createDate 2018年10月17日
 */
public class IOUtils {
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
}
