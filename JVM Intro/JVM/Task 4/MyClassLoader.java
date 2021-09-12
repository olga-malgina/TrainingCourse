package com.company;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

    private static Logger log = LogManager.getLogger();

    public Class findClass(String name) throws ClassFormatError {
        log.info("Loading class" + name);
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String name) {
        log.info("Loading class" + name);
        InputStream is = getClass().getClassLoader().getResourceAsStream(name.replace('.', File.separatorChar)
                + ".class");
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int value = 0;
        try {
            while ((value = is.read()) != -1) {
                byteStream.write(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }
}
