package testingProject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import c2m.restAPI;
import c2m.sendSettings;

public class rest_customAddressList {
	public static void main(String [] args)
	{
		restAPI p = new restAPI("username","password",restAPI.Mode.Stage);
		int addressListMappingId = 12314215;
		//Note field names need to be EXACT to what your addressmapping is.  The current example is using 
		Map<String, String> map = new HashMap<String, String>();
		map.put("CUSTOMFIELD", "John");
		map.put("Last_name", "Smith");
		map.put("Organization", "None");
		map.put("Address1", "12356 Testing");
		map.put("Address2", "");
		map.put("Address3", "");
		map.put("City", "Oak brook");
		map.put("State", "IL");
		map.put("Zip", "60523");
		map.put("Country_non-US", "");
		p.addresses.add(map);
		map = new HashMap<String, String>();
		map.put("CUSTOMFIELD", "John");
		map.put("Last_name", "Smith 2");
		map.put("Organization", "Org");
		map.put("Address1", "12345 Test Street");
		map.put("Address2", "APT2");
		map.put("Address3", "");
		map.put("City", "Oak Brook");
		map.put("State", "IL");
		map.put("Zip", "60523");
		map.put("Country_non-US", "");
		p.addresses.add(map);
		sendSettings ss = new sendSettings("#10 Double Window", "Address on First Page", "Letter 8.5 x 11", "Next Day", "Full Color", "White 24#", "Printing both sides", "First Class");
		
		
		try {
			p.createAndSubmitJob(ss,"C:/c2m/test.pdf",addressListMappingId); //Set the addressMappingId
			System.out.println("HERE IS MY JOB ID:" + p.jobId);
			System.out.println(p.getJobStatus());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
