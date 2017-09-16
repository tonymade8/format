package format;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

/**
 * Not thread-safe.
 */
class XmlFormatter implements StringFormatter {

	private final DocumentBuilder documentBuilder;
	private final LSSerializer lsSerializer;
	
	XmlFormatter() {
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			DOMImplementationRegistry domImplementationRegistry = DOMImplementationRegistry.newInstance();
			DOMImplementationLS domImplementation = (DOMImplementationLS) domImplementationRegistry.getDOMImplementation("LS");
			lsSerializer = domImplementation.createLSSerializer();
			lsSerializer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE);
		} catch (Throwable e) {
			throw new RuntimeException("Unable to create DOMImplementation", e);
		}
	}
	
	public String format(String s) {
		
		if (s == null) {
			return null;
		}
		
		try (StringReader sr = new StringReader(s)) {
			
            InputSource src = new InputSource(sr);
            Node document = documentBuilder.parse(src).getDocumentElement();
            Boolean keepDeclaration = Boolean.valueOf(s.trim().startsWith("<?xml"));
            lsSerializer.getDomConfig().setParameter("xml-declaration", keepDeclaration);
            //May need this: System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");
            return lsSerializer.writeToString(document);
            
        } catch (Exception e) {
            throw new RuntimeException("Unable to format XML", e);
        }
    	
    }
	
}
