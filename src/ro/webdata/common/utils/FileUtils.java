package ro.webdata.common.utils;

import java.io.*;

public class FileUtils {
    /**
     * Write the processed data on the disc
     * @param sw The writer input
     * @param filePath The full path where the file will be written
     *
     * @deprecated deprecated in favour of write(StringWriter sw, String filePath, boolean override)
     */
    //TODO: remove it
    public static void write(StringWriter sw, String filePath) {
        FileWriter fw = null;

        try {
            fw = new FileWriter(filePath);
            fw.write(sw.toString());
//            System.out.println("The records have been written on the disc.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                System.err.println("The 'FileWriter' could not be closed."
                        + "\nError: " + e.getMessage());
            }
        }
    }

    /**
     * Write the processed data on the disc
     * @param sw The writer input
     * @param filePath The full path where the file will be written
     * @param append Specify if the text should be appended to the existing one
     */
    public static void write(StringWriter sw, String filePath, boolean append) {
        FileWriter fw = null;

        try {
            fw = new FileWriter(filePath, append);
            fw.write(sw.toString());
//            System.out.println("The records have been written on the disc.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                System.err.println("The 'FileWriter' could not be closed."
                        + "\nError: " + e.getMessage());
            }
        }
    }

    public static StringBuilder read(String fileName) {
        BufferedReader br = null;
        StringBuilder sb = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            sb = new StringBuilder();
            String readLine;

            while ((readLine = br.readLine()) != null) {
                if (readLine.length() > 0) {
                    sb.append(readLine + "\n");
                }
            }

            // Remove the last "Enter"
            sb.delete(sb.lastIndexOf("\n"), sb.length());
        } catch (FileNotFoundException e) {
            System.err.println("The file " + fileName + " have not been found."
                    + "\nError: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.err.println("The file 'BufferedReader' could not be closed."
                        + "\nError: " + e.getMessage());
            }
        }

        return sb;
    }
}
