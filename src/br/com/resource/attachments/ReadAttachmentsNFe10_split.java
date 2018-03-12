package br.com.resource.attachments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sap.aii.mapping.api.AbstractTransformation;
import com.sap.aii.mapping.api.Attachment;
import com.sap.aii.mapping.api.InputAttachments;
import com.sap.aii.mapping.api.InputPayload;
import com.sap.aii.mapping.api.StreamTransformationException;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mapping.api.TransformationOutput;

public class ReadAttachmentsNFe10_split extends AbstractTransformation {
	private final String TAG_XI_MESSAGE = "ns0:Messages";
	private final String TAG_Message1 = "ns0:Message1";
	private final String TAG_Message2 = "ns0:Message2";
	private final String TAG_Message3 = "ns0:Message3";
	private final String TAG_Message4 = "ns0:Message4";

	private final String NS_XI_MESSAGE = "http://sap.com/xi/XI/SplitAndMerge";
	private final String ATTR_XI_MESSAGE = "xmlns:ns0";

	private Document xmlDoc;
	private Element messageTag;

	private Element attachmentTag1;
	private Element attachmentTag2;
	private Element attachmentTag3;
	private Element attachmentTag4;

	@Override
	public void transform(TransformationInput tIn, TransformationOutput tOut) throws StreamTransformationException {

		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		Transformer trans = null;

		try {
			// Instantiate a new XML document
			factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			builder = factory.newDocumentBuilder();
			this.xmlDoc = builder.newDocument();

			// Create main tag - output
			this.messageTag = xmlDoc.createElementNS(NS_XI_MESSAGE, TAG_XI_MESSAGE);
			this.xmlDoc.appendChild(messageTag);

			Attr ns0 = this.xmlDoc.createAttribute(ATTR_XI_MESSAGE);
			ns0.setNodeValue(NS_XI_MESSAGE);
			messageTag.setAttributeNode(ns0);

			attachmentTag1 = this.xmlDoc.createElement(TAG_Message1);
			attachmentTag2 = this.xmlDoc.createElement(TAG_Message2);
			attachmentTag3 = this.xmlDoc.createElement(TAG_Message3);
			attachmentTag4 = this.xmlDoc.createElement(TAG_Message4);

			getHelper().getTrace().addInfo("starting NFe fetch process");
			if (isMessageTagContentValid(tIn)) {
				this.xmlDoc.normalize();

				OutputStream os = tOut.getOutputPayload().getOutputStream();

				// Transform resulting XML into output stream:
				trans = TransformerFactory.newInstance().newTransformer();
				trans.transform(new DOMSource(this.xmlDoc), new StreamResult(os));
			} else {
				throw new StreamTransformationException(
						"There are no valid XML attachments nor body content on the e-mail received.");
			}

		} catch (TransformerConfigurationException e) {
			throw new StreamTransformationException("TransformerConfigurationException: " + e.getMessage());
		} catch (TransformerFactoryConfigurationError e) {
			throw new StreamTransformationException("TransformerFactoryConfigurationError: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new StreamTransformationException("ParserConfigurationException: " + e.getMessage());
		} catch (TransformerException e) {
			throw new StreamTransformationException("TransformerException:" + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new StreamTransformationException("UnsupportedEncodingException: " + e.getMessage());
		}
	}

	private boolean isMessageTagContentValid(TransformationInput tIn)
			throws UnsupportedEncodingException, StreamTransformationException {
		InputAttachments viaAtt = tIn.getInputAttachments();
		if (viaAtt != null) {
			setMessageElementContentViaAttachment(viaAtt);

			if (isAttachmentTagHasChild()) {
				getHelper().getTrace()
						.addInfo("a valid NFe content was found in the attachment content body, operation succeed");
				return true;
			}

		}

		getHelper().getTrace()
				.addWarning("no valid NFe content found in the available attachment(s), trying body content instead");

		InputPayload viaBodyPayload = tIn.getInputPayload();
		if (viaBodyPayload != null) {
			setMessageElementContentViaBody(viaBodyPayload);
			if (isAttachmentTagHasChild()) {
				getHelper().getTrace().addInfo("a valid NFe content was found in the message body, operation succeed");
				return true;
			}
		}

		getHelper().getTrace().addWarning("no valid NFe content found in the message body, operation failed");

		return false;
	}

	private boolean isAttachmentTagHasChild() {
		if (attachmentTag1.hasChildNodes() || attachmentTag2.hasChildNodes() || attachmentTag3.hasChildNodes()
				|| attachmentTag4.hasChildNodes()) {
			messageTag.appendChild(attachmentTag1);
			messageTag.appendChild(attachmentTag2);
			messageTag.appendChild(attachmentTag3);
			messageTag.appendChild(attachmentTag4);

			return true;
		}

		return false;
	}

	private void setMessageElementContentViaAttachment(InputAttachments att)
			throws UnsupportedEncodingException, StreamTransformationException {
		if (!att.areAttachmentsAvailable()) {
			getHelper().getTrace().addWarning("no attachments found, trying body content");
			return;
		} else {
			getHelper().getTrace().addInfo("attachments found, proceeding with mail validation and content fetching");
		}

		Collection<String> collectionIDs = att.getAllContentIds(true);

		for (String attachmentID : collectionIDs) {
			String attachmentContent = null;

			// Get attachment content
			Attachment attachments = att.getAttachment(attachmentID);
			getHelper().getTrace().addInfo("attachment content type: " + attachments.getContentType());
			
			try {
				FileType zipFileType = FileType.fileTypePerByte(attachments.getContent());
				
				if(zipFileType.equals(FileType.ZIP)) {
					continue;
				}
				
				ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(attachments.getContent()));

				ZipEntry zipEntry = null;
				
				//Checks whether this attachment`s array of bytes represents a ZIP file
				// If this "zip" object contains a ZIP file, the "while" code block will be executed
				while ((zipEntry = zip.getNextEntry()) != null) {
					
					String entryName = zipEntry.getName();
					
					if (entryName.matches("(?i:.*\\.xml)")) {
						ByteArrayOutputStream out = new ByteArrayOutputStream();

						byte[] byteBuf = new byte[4096];

						int bytesRead = 0;

						try {
							while ((bytesRead = zip.read(byteBuf)) != -1) {
								out.write(byteBuf, 0, bytesRead);
							}
							
							FileType xmlFileType = FileType.fileTypePerByte(out.toByteArray());
							
							if(!xmlFileType.equals(FileType.XML)) {
								continue;
							}
							
							attachmentContent = new String(out.toByteArray(), "UTF-8");
							retrieveElementFromNFeContent(attachmentContent);

							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				if(attachmentContent == null) {
					attachmentContent = new String(attachments.getContent(), "UTF-8");
					
					retrieveElementFromNFeContent(attachmentContent);
				}
			} catch (IOException e) {
				getHelper().getTrace().addWarning("failed to read zip content file");
				getHelper().getTrace().addDebugMessage(e.getMessage(), e);
			}
		}
	}

	private void setMessageElementContentViaBody(InputPayload payload) {
		String inStr = "";
		String xmlContent = "";

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		InputStream in = payload.getInputStream();

		int c = -1;

		try {
			while ((c = in.read()) != -1)
				baos.write(c);
			inStr = baos.toString("UTF8");

			xmlContent = retrieveTagValue("<Content>", "</Content>", inStr);

			retrieveElementFromNFeContent(xmlContent);
		} catch (Exception e) {
			throw new SecurityException("XML de origem mal formado: " + e);
		}
	}

	private void retrieveElementFromNFeContent(String content) {
		Pattern pattern = Pattern.compile("(nfeProc|procEventoNFe|cteProc|procEventoCTe)");

		Matcher m = pattern.matcher(content);

		if (m.find()) {
			getHelper().getTrace()
					.addInfo("NFe required tags found, proceeding with content fetching and new XML output");

			String tag = m.group(0);
			getHelper().getTrace().addInfo(tag + " added");

			int xmlIndex = content.indexOf("?>"); // remove XML header like <?xml version="1.0" encoding="UTF-8" ?>
			if (xmlIndex != -1)
				content = content.substring(xmlIndex + 2);

			Node textTag = this.xmlDoc.createTextNode(content);
			textTag.normalize();

			switch (tag) {
			case "nfeProc":
				attachmentTag1.appendChild(textTag);
				break;
			case "procEventoNFe":
				attachmentTag2.appendChild(textTag);
				break;
			case "cteProc":
				attachmentTag3.appendChild(textTag);
				break;
			case "procEventoCTe":
				attachmentTag4.appendChild(textTag);
				break;
			}
		}
	}

	private static String retrieveTagValue(String AbreTag, String FechaTag, String xml) {

		String result = "";
		int s = xml.indexOf(AbreTag);
		int e = xml.indexOf(FechaTag);

		if (s == -1) {
			result = "";
		} else {
			result = xml.substring(s + AbreTag.length(), e);
		}
		return result;
	}
}