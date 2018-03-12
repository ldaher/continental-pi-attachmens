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
import java.lang.instrument.Instrumentation;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import br.com.resource.attachments.FileType;

@SuppressWarnings("unused")
public class ContinentalMain {
	public static void main(String[] args) throws IOException {
		InputStream is = null;
		try {
			byte[] readAllBytes = Files
					.readAllBytes(FileSystems.getDefault().getPath("/Users/ldaher/Desktop/Continental/nfes/nfes.zip"));
			
/*			String a = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			byte[] bytes = a.getBytes();
			Arrays.sort(bytes);
			
			FileType fileType = FileType.fileTypePerByte(readAllBytes);
			
			System.out.println(fileType.getFileType());
			
			Byte b = new Byte(readAllBytes[0]);*/
			
			ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(readAllBytes));

			ZipEntry zipEntry = null;
			
			//Checks whether this attachment`s array of bytes represents a ZIP file
			// If this "zip" object contains a ZIP file, the "while" code block will be executed
			while ((zipEntry = zip.getNextEntry()) != null) {
				String entryName = zipEntry.getName();
				
				System.out.printf( "%s: ", entryName);

				if (entryName.matches("(?i:.*\\.xml)")) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();

					byte[] byteBuf = new byte[4096];

					int bytesRead = 0;

					try {
						while ((bytesRead = zip.read(byteBuf)) != -1) {
							out.write(byteBuf, 0, bytesRead);
						}
						
						FileType fileTypePerByte = FileType.fileTypePerByte(out.toByteArray());
						
						System.out.println(fileTypePerByte.getFileType());
						
						String attachmentContent = new String(out.toByteArray(), "UTF-8");
//						System.out.println(attachmentContent);
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
