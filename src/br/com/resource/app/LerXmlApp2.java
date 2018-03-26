package br.com.resource.app;

import java.io.StringWriter;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.com.resource.attachments.xml.schema2java.nfeproc.ObjectFactory;
import br.com.resource.attachments.xml.schema2java.nfeproc.TNfeProc;

@SuppressWarnings("unchecked")
public class LerXmlApp2 {
	public static void main(String[] args) {

		try {
//			byte[] readAllBytes = Files
//					.readAllBytes(Paths.get("C:/Users/luciano.daher/Desktop/Continental/Nfes/NFE.xml"));

			JAXBContext ctx = JAXBContext.newInstance("trainning.reading.xml.schema2java.nfeproc");

			Unmarshaller unmarshaller = ctx.createUnmarshaller();

//			JAXBElement<TNfeProc> tNfeUnmarshaller = (JAXBElement<TNfeProc>) unmarshaller.unmarshal(new ByteArrayInputStream(readAllBytes));
			JAXBElement<TNfeProc> tNfeUnmarshaller = (JAXBElement<TNfeProc>) unmarshaller.unmarshal(Paths.get("C:/Users/luciano.daher/Desktop/Continental/Nfes/NFE.xml").toFile());

			TNfeProc tNfeProc = tNfeUnmarshaller.getValue();
			
			ObjectFactory factory = new ObjectFactory();
			
			JAXBElement<TNfeProc> element = factory.createNfeProc(tNfeProc);

			StringWriter sw = new StringWriter();
			
			Marshaller marshaller = ctx.createMarshaller();
			
			marshaller.marshal(element, sw);
			
			String marshalledContent = sw.toString();
			
			System.out.println(marshalledContent);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}
}
