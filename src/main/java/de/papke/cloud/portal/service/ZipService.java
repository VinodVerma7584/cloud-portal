package de.papke.cloud.portal.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ZipService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ZipService.class);
	
	public static void zip(File sourceDir, File outputFile) throws IOException, FileNotFoundException {
		
		ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
		Path srcPath = Paths.get(sourceDir.toURI());
		zipFolder(srcPath.getParent().toString(), srcPath.getFileName().toString(), zipFile);
		IOUtils.closeQuietly(zipFile);
	}

	private static void zipFolder(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
		
		String dir = Paths.get(rootDir, sourceDir).toString();
		
		for (File file : new File(dir).listFiles()) {
			
			if (!file.getName().startsWith(".")) {
			
				if (file.isDirectory()) {
					zipFolder(rootDir, Paths.get(sourceDir,file.getName()).toString(), out);
				} 
				else {
					ZipEntry entry = new ZipEntry(Paths.get(sourceDir,file.getName()).toString());
					out.putNextEntry(entry);
	
					FileInputStream in = new FileInputStream(Paths.get(rootDir, sourceDir, file.getName()).toString());
					IOUtils.copy(in, out);
					IOUtils.closeQuietly(in);
				}
			}
		}
	}
	
	public static void unzip(File archive, File outputDir) {
        try {
            ZipFile zipfile = new ZipFile(archive);
            for (Enumeration<?> e = zipfile.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                unzipEntry(zipfile, entry, outputDir);
            }
        } 
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private static void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {

        if (entry.isDirectory()) {
            createDir(new File(outputDir, entry.getName()));
            return;
        }
        
        File outputFile = new File(outputDir, entry.getName());
        if (!outputFile.getParentFile().exists()){
            createDir(outputFile.getParentFile());
        }

        BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

        try {
            IOUtils.copy(inputStream, outputStream);
        } 
        finally {
            outputStream.close();
            inputStream.close();
        }
    }

    private static void createDir(File dir) {
        if(!dir.mkdirs()) throw new RuntimeException("Can not create dir "+dir);
    }
}
