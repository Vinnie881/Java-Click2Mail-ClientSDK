package testingProject;

import java.io.IOException;

import c2m.batchAPI;
import c2m.job;
import c2m.sendSettings;

public class batch_simpleSend {

	public static void main(String[] args) {
	string un = "MyUserName";
	string pw = "MyPW";
	batchAPI p = new batchAPI(un,pw,batchAPI.Mode.Stage);
	sendSettings ss = new sendSettings("#10 Double Window", "Address on First Page", "Letter 8.5 x 11", "Next Day", "Full Color", "White 24#", "Printing both sides", "First Class");
	job myJob = new job(ss, 1, 5);
	myJob.addJobAddress("My Name", "ORG", "12345 Test St", "APT 3", "", "Oak Brook", "IL","60523", "", "");
	myJob.addJobAddress("My Name 2nd address", "ORG", "12345 Test St", "APT 3", "", "Oak Brook", "IL","60523", "", "");
	p.addJob(myJob);
		
	myJob = new job(ss, 6, 10);
	myJob.addJobAddress("My Name 1st Address for job 2", "ORG", "12345 Test St", "APT 3", "", "Oak Brook", "IL","60523", "", "");
	myJob.addJobAddress("My Name 2nd address for job 2", "ORG", "12345 Test St", "APT 3", "", "Oak Brook", "IL","60523", "", "");
	p.addJob(myJob);
	try {
		System.out.println(p.startProcess("C:/c2m/test.pdf"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	}
}
