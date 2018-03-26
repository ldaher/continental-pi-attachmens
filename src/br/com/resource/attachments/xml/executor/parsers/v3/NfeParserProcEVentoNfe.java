package br.com.resource.attachments.xml.executor.parsers.v3;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.resource.attachments.xml.executor.parsers.v3.types.NfeTypesEnum;
import br.com.resource.attachments.xml.schema2java.proceventonfe.TProcEvento;

public class NfeParserProcEVentoNfe extends NfeParser<TProcEvento> {
	
	private StringReader sr;

	public NfeParserProcEVentoNfe(String xmlContent) {
		this.sr = new StringReader(xmlContent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		try {
			super.ctx = JAXBContext.newInstance(NfeTypesEnum.PROCEVENTONFE.getPackagePath());

			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			super.element = (JAXBElement<TProcEvento>) unmarshaller.unmarshal(sr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
