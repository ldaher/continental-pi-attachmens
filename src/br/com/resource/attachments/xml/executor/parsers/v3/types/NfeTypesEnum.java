package br.com.resource.attachments.xml.executor.parsers.v3.types;

import br.com.resource.attachments.xml.schema2java.cteproc.CteProc;
import br.com.resource.attachments.xml.schema2java.nfeproc.TNfeProc;
import br.com.resource.attachments.xml.schema2java.proceventocte.ProcEventoCTe;
import br.com.resource.attachments.xml.schema2java.proceventonfe.TProcEvento;

public enum NfeTypesEnum {
	CTEPROC("cteProc", CteProc.class.getPackage().getName()), 
	NFEPROC("nfeProc", TNfeProc.class.getPackage().getName()),
	PROCEVENTONFE("procEventoNFe", TProcEvento.class.getPackage().getName()),
	PROCEVENTOCTE("procEventoCTe", ProcEventoCTe.class.getPackage().getName());
	
	private String type;
	private String packagePath;
	
	private NfeTypesEnum(String type, String packagePath) {
		this.type = type;
		this.packagePath = packagePath;
	}
	
	public String getType() {
		return type;
	}
	
	public String getPackagePath() {
		return this.packagePath;
	}

}
