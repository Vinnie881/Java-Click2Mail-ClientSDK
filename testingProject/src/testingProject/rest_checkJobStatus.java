package testingProject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import c2m.restAPI;
import c2m.sendSettings;

public class rest_checkJobStatus {

	public static void main(String [] args)
	{
		restAPI p = new restAPI("username","password",restAPI.Mode.Stage);
		p.jobId = "258012";
		String result = p.getJobStatus();
		System.out.println(result);
		System.out.println(p.getString(result ,"/job/description/text()"));
		
	}
}
