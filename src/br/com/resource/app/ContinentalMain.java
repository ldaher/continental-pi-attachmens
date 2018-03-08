package br.com.resource.app;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

@SuppressWarnings("unused")
public class ContinentalMain {

	public static void main(String[] args) throws IOException {

		InputStream is = null;
		try {
			byte[] readAllBytes = Files
					.readAllBytes(FileSystems.getDefault().getPath("/Users/ldaher/Desktop/Continental/nfes/NFE.xml"));

			ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(readAllBytes));

			ZipEntry zipEntry = null;

			String names = null;
			while ((zipEntry = zis.getNextEntry()) != null) {
				names = zipEntry.getName();

				System.out.println(names);
			}
			
			if(names == null) {
				System.err.println("Não é um zip");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
