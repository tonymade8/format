package format;

import java.io.File;
import java.util.List;

import com.beust.jcommander.Parameter;

class CommandLineArguments {
	
	@Parameter(names={"-h", "--help"}, help=true, description="Show help text", order=1)
    boolean help;
	
	@Parameter(names={"-j", "--json"}, description="Format as JSON", order=2)
    boolean json;
	
    @Parameter(names={"-x", "--xml"}, description="Format as XML", order=3)
    boolean xml;
    
    @Parameter(names={"-s", "--standardInput"}, description="Read from standard input instead of clipboard when no files are specified", order=4)
    boolean standardInput;
    
    @Parameter(names={"-e", "--encoding"}, description="File content encoding", order=5)
    String encoding = "UTF-8";
    
    @Parameter(names={"-d", "--debug"}, description="Show stracktrace in case of an exception", order=6)
    boolean debugMode;
    
    @Parameter(description = "[files to format]", order=7)
    public List<File> files;
    
}
