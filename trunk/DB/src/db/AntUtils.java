package db;

import java.io.File;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class AntUtils{

	public enum Targets{
		SETUP,
		BUILD,
		POPULATE;		
	}
	
	public static void executeTarget(Targets target){
		File buildFile = new File("build.xml");
		Project p = new Project();
		
		DefaultLogger consoleLogger = new DefaultLogger();
		consoleLogger.setErrorPrintStream(System.err);
		consoleLogger.setOutputPrintStream(System.out);
		consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
		p.addBuildListener(consoleLogger);
		
		p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		p.init();
		
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		p.addReference("ant.projectHelper", helper);
		helper.parse(p, buildFile);
		
		String antTarget = target.toString().toLowerCase();
		p.executeTarget(antTarget);
	}
	

}
