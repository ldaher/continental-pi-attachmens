package br.com.resource.attachments.xml.executor.versions.v3;

import br.com.resource.attachments.xml.executor.parsers.v3.NfeParseNfeProc;
import br.com.resource.attachments.xml.executor.parsers.v3.NfeParseProcEventoCte;
import br.com.resource.attachments.xml.executor.parsers.v3.NfeParser;
import br.com.resource.attachments.xml.executor.parsers.v3.NfeParserCteProc;
import br.com.resource.attachments.xml.executor.parsers.v3.NfeParserProcEVentoNfe;
import br.com.resource.attachments.xml.executor.parsers.v3.types.NfeTypesEnum;
import br.com.resource.attachments.xml.executor.versions.NfeVersion;

public class NfeVersion3 implements NfeVersion {

	private String nfeType;
	private String xmlContent;

	public NfeVersion3(String xmlContent, String nfeType) {
		this.nfeType = nfeType;
		this.xmlContent = xmlContent;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public NfeParser nfe() {
		NfeParser nfeParser = null;

		if (NfeTypesEnum.NFEPROC.getType().equals(nfeType)) {
			nfeParser = new NfeParseNfeProc(xmlContent);
		} else if (NfeTypesEnum.CTEPROC.getType().equals(nfeType)) {
			nfeParser = new NfeParserCteProc(xmlContent);
		} else if (NfeTypesEnum.PROCEVENTONFE.getType().equals(nfeType)) {
			nfeParser = new NfeParserProcEVentoNfe(xmlContent);
		} else if (NfeTypesEnum.PROCEVENTOCTE.getType().equals(nfeType)) {
			nfeParser = new NfeParseProcEventoCte(xmlContent);
		}

		return nfeParser;
	}

}
