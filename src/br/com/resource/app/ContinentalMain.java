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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import br.com.resource.app.ContinentalMain.FileType;

@SuppressWarnings("unused")
public class ContinentalMain {

	enum FileType {
		PDF("PDF", new byte[][] { { 0x25, 0x50, 0x44, 0x46 } }), ZIP("ZIP", new byte[][] { { 0x50, 0x4b } }), XML("XML",
				new byte[][] { {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20} }), UNKNOWN("UNKNOWN", new byte[][] { {} });
		
		private byte[][] fileBytes;
		private String fileType;

		private FileType(String fileType, byte[][] fileBytes) {
			this.fileBytes = fileBytes;
			this.fileType = fileType;
		}

		public static String fileTypePerByte(byte[] fileAsByte) {
			List<FileType> asList = Arrays.asList(FileType.values());

			for (FileType f : asList) {
				byte[] newByte = Arrays.copyOf(fileAsByte, f.fileBytes[0].length);

				if (Arrays.equals(f.fileBytes[0], newByte)) {
					return f.fileType;
				}
			}

			return UNKNOWN.fileType;
		}

	}

	public static void main(String[] args) throws IOException {
		InputStream is = null;
		try {
			byte[] readAllBytes = Files
					.readAllBytes(FileSystems.getDefault().getPath("/Users/ldaher/Desktop/Continental/nfes/NFE.xml"));
			
			String a = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			byte[] bytes = a.getBytes();
			Arrays.sort(bytes);
			
			String fileType = FileType.fileTypePerByte(readAllBytes);
			
			System.out.println(fileType);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
