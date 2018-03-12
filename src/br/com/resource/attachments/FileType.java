package br.com.resource.attachments;

import java.util.Arrays;
import java.util.List;

public enum FileType {
	PDF("PDF", new byte[][] { { 0x25, 0x50, 0x44, 0x46 } }), ZIP("ZIP", new byte[][] { { 0x50, 0x4b } }), XML("XML",
			new byte[][] { {0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20} }), UNKNOWN("UNKNOWN", new byte[][] { {} });
	
	private byte[][] fileBytes;
	private String fileType;

	private FileType(String fileType, byte[][] fileBytes) {
		this.fileBytes = fileBytes;
		this.fileType = fileType;
	}

	public static FileType fileTypePerByte(byte[] fileAsByte) {
		List<FileType> asList = Arrays.asList(FileType.values());

		for (FileType f : asList) {
			byte[] newByte = Arrays.copyOf(fileAsByte, f.fileBytes[0].length);

			if (Arrays.equals(f.fileBytes[0], newByte)) {
				return f;
			}
		}

		return UNKNOWN;
	}

	public byte[][] getFileBytes() {
		return fileBytes;
	}

	public String getFileType() {
		return fileType;
	}
	
	

}