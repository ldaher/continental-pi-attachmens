package br.com.resource.attachments.xml.executor;

import br.com.resource.attachments.xml.executor.parsers.v3.NfeParser;

@SuppressWarnings("rawtypes")
public class NfeParseExecutor {

	private NfeParser nfeParse;

	public NfeParseExecutor(NfeParser nfeParsePerType) {
		this.nfeParse = nfeParsePerType;
	}

	public String execute() {
		return nfeParse.parse();
	}

}
