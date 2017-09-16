package format;

enum SourceFormat {

	JSON(new JsonFormatter()), 
	XML(new XmlFormatter());
	
	private final StringFormatter sf;
	
	private SourceFormat(StringFormatter sf) {
		this.sf = sf;
	}
	
	public StringFormatter formatter() {
		return sf;
	}
	
}
