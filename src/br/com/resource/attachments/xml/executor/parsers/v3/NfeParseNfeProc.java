package br.com.resource.attachments.xml.executor.parsers.v3;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.resource.attachments.xml.executor.parsers.v3.types.NfeTypesEnum;
import br.com.resource.attachments.xml.schema2java.nfeproc.TNfeProc;

public class NfeParseNfeProc extends NfeParser<TNfeProc> {
	
	private StringReader sr;
	
	public NfeParseNfeProc(String xmlContent) {
		this.sr = new StringReader(xmlContent);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void load() {
		try {
			super.ctx = JAXBContext.newInstance(NfeTypesEnum.NFEPROC.getPackagePath());
			
			Unmarshaller unmarshaller = super.ctx.createUnmarshaller();
			super.element = (JAXBElement<TNfeProc>) unmarshaller.unmarshal(sr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
