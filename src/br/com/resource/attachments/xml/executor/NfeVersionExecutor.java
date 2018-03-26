package br.com.resource.attachments.xml.executor;

import br.com.resource.attachments.xml.executor.parsers.v3.NfeParser;
import br.com.resource.attachments.xml.executor.versions.NfeVersion;
import br.com.resource.attachments.xml.executor.versions.v3.NfeVersion3;

@SuppressWarnings("rawtypes")
public class NfeVersionExecutor {

	private String nfeVersion;
	private String nfeType;
	private String xmlContent;

	public NfeVersionExecutor(String nfeVersion, String nfeType, String xmlContent) {
		this.nfeVersion = nfeVersion;
		this.nfeType = nfeType;
		this.xmlContent = xmlContent;
	}

	public NfeParser loadNfeParser() {
		NfeVersion nfeVersionImpl;
		
		switch (nfeVersion) {
		case "3":
			nfeVersionImpl = new NfeVersion3(xmlContent, nfeType);
			return nfeVersionImpl.nfe();
		default:
			throw new RuntimeException(String.format("Versão da NFe %s não está contemplada no sistema", nfeVersion));
		}
	}

}
