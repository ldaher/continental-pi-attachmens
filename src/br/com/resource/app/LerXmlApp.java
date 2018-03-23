package br.com.resource.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.resource.attachments.xml.executor.NfeVersionExecutor;
import br.com.resource.attachments.xml.executor.parsers.v3.NfeParser;

public class LerXmlApp {
	public static void main(String[] args) {
		byte[] byteBuf = new byte[4096];

		int bytesRead = 0;
		
//		try (FileInputStream fis = new FileInputStream(new File("C:/Users/luciano.daher/Desktop/Continental/Nfes/problemas/e43a380_1005HTML000005.xml"))) {
//		try (FileInputStream fis = new FileInputStream(new File("C:/Users/luciano.daher/Desktop/Continental/Nfes/Cte.xml"))) {
		try (FileInputStream fis = new FileInputStream(new File("C:/Users/luciano.daher/Desktop/Continental/Nfes/NFE-marshaller.xml"))) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((bytesRead = fis.read(byteBuf)) != -1) {
				baos.write(byteBuf, 0, bytesRead);
			}

			String attachmentContent = new String(baos.toByteArray(), "UTF-8");

			Matcher m = Pattern.compile("(nfeProc|cteProc)(\\s+?)(versao\\=\\\")(\\d{1,})(\\.?\\d{1,})(\\\")")
					.matcher(attachmentContent);

			String nt = "";
			String nv = "";

			if (m.find()) {
				nt = m.group(1);
				nv = m.group(4);

				System.out.println(nv);
			} else {
				new RuntimeException("Nem a versão nem o tipo da NFe foram encontrados no arquivo XML informado");
			}

			NfeVersionExecutor nfeVersionExecutor = new NfeVersionExecutor(nv, nt, attachmentContent);
			
			NfeParser loadNfeParser = nfeVersionExecutor.loadNfeParser();
			String parsedNfe = loadNfeParser.parse();
			
			System.out.println(parsedNfe);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
