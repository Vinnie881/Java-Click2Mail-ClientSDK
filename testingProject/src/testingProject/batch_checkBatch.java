package testingProject;

import c2m.batchAPI;

public class batch_checkBatch {
	public static void main(String [] args)
	{
		batchAPI p = new batchAPI("username","password",batchAPI.Mode.Stage);
		p.batchId = "44275";
		String result = p.getBatchStatus();
		System.out.println(result);
		System.out.println(p.getString(result ,"/batchjob/submitted/text()"));
		
	}
}
