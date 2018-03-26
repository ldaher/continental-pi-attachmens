package br.com.resource.attachments.xml.executor.parsers.v3;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import br.com.resource.attachments.xml.executor.parsers.v3.types.NfeTypesEnum;
import br.com.resource.attachments.xml.schema2java.cteproc.CteProc;

public class NfeParserCteProc extends NfeParser<CteProc>{
	
	private StringReader sr;

	public NfeParserCteProc(String xmlContent) {
		this.sr = new StringReader(xmlContent);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void load() {
		try {
			super.ctx = JAXBContext.newInstance(NfeTypesEnum.CTEPROC.getPackagePath());

			Unmarshaller unmarshaller = ctx.createUnmarshaller();
			super.element = (JAXBElement<CteProc>) unmarshaller.unmarshal(sr);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
