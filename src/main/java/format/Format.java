package format;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.beust.jcommander.JCommander;

public class Format {

    public static void main(String[] args) {

    	CommandLineArguments cla = new CommandLineArguments();
		JCommander jc = JCommander.newBuilder().addObject(cla).build();
		jc.setProgramName("f");
		try {
			jc.parse(args);
		} catch (Throwable t) {
			printHelp(jc);
		}

		if (cla.help) {
			printHelp(jc);
		} else {
	
			try {
				
				if (cla.files == null || cla.files.isEmpty()) {
					if (cla.standardInput) {
						formatStandardInput(cla);						
					} else {
						formatClipboardContent(cla);	
					}
				} else {
					cla.files.forEach(f -> {
						formatFileContent(cla, f);
					});
				}

	        } catch (Throwable e) {
	        	System.err.println("Unable to parse input");
	        	if (cla.debugMode) {
	        		e.printStackTrace(System.err);	
	        	}
	        	System.exit(2);
	        }

			
		}
		
	}

	private static void printHelp(JCommander jc) {
		jc.usage();
		System.out.println("If no files are passed the content from the clipboard will be used if possible.");
		System.out.println("If no format is specified or multiple formats are specified the program will try to determine the format automatically.");
		System.exit(1);
	}

	private static void formatStandardInput(CommandLineArguments cla) throws IOException {
		String content = getStandardInContent();
		SourceFormat sf = getSourceFormat(cla, content);
		System.out.println(sf.formatter().format(content));
	}

	private static String getStandardInContent() throws IOException {
		StringBuilder content = new StringBuilder();
		try (InputStreamReader isr = new InputStreamReader(System.in); BufferedReader in = new BufferedReader(isr)) {
			String s;
			while ((s = in.readLine()) != null) {
				content.append(s + "\n");			
			}
		}
		return content.toString();
	}
	
	private static void formatClipboardContent(CommandLineArguments cla) throws UnsupportedFlavorException, IOException {
		String clipboardContent = getClipboardContent();
		SourceFormat sf = getSourceFormat(cla, clipboardContent);
		System.out.println(sf.formatter().format(clipboardContent));
	}
	
	private static String getClipboardContent() throws UnsupportedFlavorException, IOException {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		return (String) clipboard.getData(DataFlavor.stringFlavor);
	}

	private static void formatFileContent(CommandLineArguments cla, File file) {
		if (file.exists()) {
			try {
				byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
				String content = new String(encoded, cla.encoding);
				SourceFormat sf = getSourceFormat(cla, content);
				System.out.println(sf.formatter().format(content));
			} catch (UnsupportedFormatException e) {
				System.err.println("Unknown format for '" + file.getAbsolutePath() + "'");
				if (cla.debugMode) {
					e.printStackTrace(System.err);	
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("Unsupported encoding: " + cla.encoding, e);
			} catch (IOException e) {
				System.err.println("Unable to read '" + file.getAbsolutePath() + "'");
				if (cla.debugMode) {
					e.printStackTrace(System.err);	
				}
			}
		} else {
			System.err.println(file.getAbsolutePath() + " does not exist.");
		}
	}
	
	private static SourceFormat getSourceFormat(CommandLineArguments cla, String content) {
		if (cla.json && !cla.xml) {
			// use forced JSON formatting
			return SourceFormat.JSON;
		} else if (cla.xml && !cla.json) {
			// use forced XML formatting
			return SourceFormat.XML;
		}
		// try to determine format automatically
		for (SourceFormat sf: SourceFormat.values()) {
			if (sf.formatter().canProbablyFormat(content)) {
				return sf;
			}
		}
		throw new UnsupportedFormatException();
	}
	
}
