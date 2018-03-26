package br.com.resource.attachments.xml.executor.parsers.v3;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public abstract class NfeParser<T> {

	protected JAXBContext ctx;
	protected JAXBElement<T> element;

	public abstract void load();

	public String parse() {
		load();
		
		try {
			StringWriter sw = new StringWriter();
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.marshal(element, sw);
			return sw.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return "";
	}

}
