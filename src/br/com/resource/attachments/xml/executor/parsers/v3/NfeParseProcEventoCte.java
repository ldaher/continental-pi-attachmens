package br.com.resource.attachments.xml.executor.parsers.v3;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.resource.attachments.xml.executor.parsers.v3.types.NfeTypesEnum;
import br.com.resource.attachments.xml.schema2java.proceventocte.ProcEventoCTe;

public class NfeParseProcEventoCte extends NfeParser<ProcEventoCTe> {
	private StringReader sr;

	public NfeParseProcEventoCte(String xmlContent) {
		this.sr = new StringReader(xmlContent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		try {
			super.ctx = JAXBContext.newInstance(NfeTypesEnum.PROCEVENTOCTE.getPackagePath());

			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			super.element = (JAXBElement<ProcEventoCTe>) unmarshaller.unmarshal(sr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

}
