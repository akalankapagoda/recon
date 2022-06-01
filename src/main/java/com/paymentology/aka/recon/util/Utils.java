package com.paymentology.aka.recon.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Utils {

    /**
     * Write an input stream to disk.
     *
     * @param filename The fully qualified filename
     * @param inputStream The input stream of file data
     * @throws IOException
     */
    public static final void writeFileToDisk(String filename, InputStream inputStream) throws IOException {

        File file = new File(filename);
        file.mkdirs();

        FileOutputStream outputStream = new FileOutputStream(filename);

        IOUtils.copy(inputStream, outputStream);
    }
}
