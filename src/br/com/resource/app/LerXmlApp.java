package br.com.resource.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.resource.attachments.ReadAttachmentsNFe10_split;
import br.com.resource.attachments.xml.executor.NfeVersionExecutor;
import br.com.resource.attachments.xml.executor.parsers.v3.NfeParser;

public class LerXmlApp {
	public static void main(String[] args) {
		byte[] byteBuf = new byte[4096];

		int bytesRead = 0;
		
//		try (FileInputStream fis = new FileInputStream(new File("C:/Users/luciano.daher/Desktop/Continental/Nfes/problemas/e43a380_1005HTML000005.xml"))) {
		try (FileInputStream fis = new FileInputStream(new File("C:/Users/luciano.daher/Desktop/Continental/Nfes/problemas/26032018/6122.xml"))) {
//		try (FileInputStream fis = new FileInputStream(new File("C:/Users/luciano.daher/Desktop/Continental/Nfes/NFE-marshaller.xml"))) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((bytesRead = fis.read(byteBuf)) != -1) {
				baos.write(byteBuf, 0, bytesRead);
			}

			String attachmentContent = new String(baos.toByteArray(), "UTF-8");
			
			ReadAttachmentsNFe10_split read = new ReadAttachmentsNFe10_split();
			//read.retrieveElementFromNFeContentV2(attachmentContent);

			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
