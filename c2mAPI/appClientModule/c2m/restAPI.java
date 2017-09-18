package c2m;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;





public class restAPI {
	public String username="";
	public String password = "";
	public Mode mode;
	public int addressListMappingId = 2;
	public List<Map<String, String>> addresses = new ArrayList<Map<String, String>>();
	public String addressListXML= "";
	public String addressId;
	public String documentId;
	public String jobId;
	
	public restAPI(String username,String password,Mode mode)
	{
		this.username = username;
		this.password =password;
		this.mode = mode;
		try {
			ImportCA.importCAsStage();
			ImportCA.importCAs();
		} catch (KeyStoreException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoSuchAlgorithmException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (CertificateException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	public String createAddressListXML(int addressListMappingId )
	{
		
		this.addressListXML =  "<addressList><addressListName>" + addressListMappingId + "</addressListName>" +
		  "<addressMappingId>2</addressMappingId>" +
		  "<addresses>";
		  
		addresses.forEach((mymap)->{
			this.addressListXML += "<address>";
			mymap.forEach( (k,v)->
			{
				this.addressListXML += "<" + k + ">" + v + "</" + k + ">";
			
			});
			this.addressListXML += "</address>";
		});
		this.addressListXML += "</addresses></addressList>";
		return this.addressListXML;
	}
	public enum Mode{
		Stage,Production
	}
	public String pdfFile = "";
	private sendSettings ct;
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	    private String _file  = "";
	    public static String  _restapi = "https://rest.click2mail.com";
		public static String  _Srestapi = "https://stage-rest.click2mail.com";
		
		private String  _url  = "";
		private String _xml = "";
		
		private static String _authinfo  = "";
		
				//Public Delegate Sub UpdateStatusTextCallback(ByVal text As String)
			    //Public Delegate Sub UpdateCountTextCallback(ByVal text As String)
			    //Public Delegate Sub processdone(ByVal success As Integer, results As String)
			    //Private frm As SetupStationaryFields
			    //Private _keepopen As Boolean = False
		public  String submitJob() throws IOException
		{

		
			String url = this.get_url() + "/molpro/jobs/" + Integer.parseInt(jobId.trim()) + "/submit";
			
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects( false );
			
	        String authString = username + ":" + password;
			String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
			
			
			
		    			
		    String data = URLEncoder.encode("billingType", "UTF-8") + "=" + URLEncoder.encode("User Credit", "UTF-8");
		    			
		    
		    OutputStreamWriter wr =  new OutputStreamWriter( connection.getOutputStream());
			wr.write(data);
			wr.flush();
						
			String result = null;
			try
			{
				
			
		     InputStream is =connection.getInputStream();
		     
						InputStreamReader isr = new InputStreamReader(is);

						int numCharsRead;
						char[] charArray = new char[1024];
						StringBuffer sb = new StringBuffer();
						while ((numCharsRead = isr.read(charArray)) > 0) {
							sb.append(charArray, 0, numCharsRead);
						}
			 result = sb.toString();
			//System.out.println(result); // Should be 200
			isr.close();
			is.close();
			
			} catch (IOException e) {
				BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
				String line = "";
				
				StringBuilder sb = new StringBuilder();	
				
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString(); 
			//	submitJob(jobId);
				
			}
			return result;
			
			
		}
		public  String createJob(String documentId, String addressListId) throws IOException
		{

			String url = get_url() + "/molpro/jobs";	
				HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setInstanceFollowRedirects( false );
	        String authString = username + ":" + password;
			String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
			
			
			
		    String data = URLEncoder.encode("documentClass", "UTF-8") + "=" + URLEncoder.encode(ct.documentClass, "UTF-8");
		    data += "&" + URLEncoder.encode("layout", "UTF-8") + "=" + URLEncoder.encode(ct.layout, "UTF-8");
		    data += "&" + URLEncoder.encode("productionTime", "UTF-8") + "=" + URLEncoder.encode(ct.productionTime, "UTF-8");
		    if(ct.envelope != null)
		    {
		    data += "&" + URLEncoder.encode("envelope", "UTF-8") + "=" + URLEncoder.encode(ct.envelope, "UTF-8");
		    }
		    else
		    {
		    	data += "&" + URLEncoder.encode("envelope", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");	
		    }
		    data += "&" + URLEncoder.encode("color", "UTF-8") + "=" + URLEncoder.encode(ct.color, "UTF-8");
		    data += "&" + URLEncoder.encode("paperType", "UTF-8") + "=" + URLEncoder.encode(ct.paperType, "UTF-8");
		    data += "&" + URLEncoder.encode("printOption", "UTF-8") + "=" + URLEncoder.encode(ct.printOption, "UTF-8");
		    data += "&" + URLEncoder.encode("documentId", "UTF-8") + "=" + URLEncoder.encode(documentId, "UTF-8");
		    data += "&" + URLEncoder.encode("addressId", "UTF-8") + "=" + URLEncoder.encode(addressListId, "UTF-8");
		    data += "&" + URLEncoder.encode("mailClass", "UTF-8") + "=" + URLEncoder.encode(ct.mailClass, "UTF-8");
		    if(!ct.raName.equals("") || !ct.raOrganization.equals(""))
		    {
		    data += "&" + URLEncoder.encode("rtnName", "UTF-8") + "=" + URLEncoder.encode(ct.raName, "UTF-8");
		    data += "&" + URLEncoder.encode("rtnOrganization", "UTF-8") + "=" + URLEncoder.encode(ct.raOrganization, "UTF-8");
		    data += "&" + URLEncoder.encode("rtnaddress1", "UTF-8") + "=" + URLEncoder.encode(ct.raAddress1, "UTF-8");
		    data += "&" + URLEncoder.encode("rtnaddress2", "UTF-8") + "=" + URLEncoder.encode(ct.raAddress2, "UTF-8");
		    data += "&" + URLEncoder.encode("rtnCity", "UTF-8") + "=" + URLEncoder.encode(ct.raCity, "UTF-8");
		    data += "&" + URLEncoder.encode("rtnState", "UTF-8") + "=" + URLEncoder.encode(ct.raState, "UTF-8");
		    data += "&" + URLEncoder.encode("rtnZip", "UTF-8") + "=" + URLEncoder.encode(ct.raZip, "UTF-8");
		    }
		    data += "&" + URLEncoder.encode("appSignature", "UTF-8") + "=" + URLEncoder.encode("MailJackPlus", "UTF-8");
		    
			
			OutputStreamWriter wr =  new OutputStreamWriter( connection.getOutputStream());
			   wr.write(data);
			wr.flush();
			String result = "";
			try
			{
						
			
		     InputStream is =connection.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);

						int numCharsRead;
						char[] charArray = new char[1024];
						StringBuffer sb = new StringBuffer();
						while ((numCharsRead = isr.read(charArray)) > 0) {
							sb.append(charArray, 0, numCharsRead);
						}
			 result = sb.toString();
			//System.out.println(result); // Should be 200
			wr.close();
			isr.close();
			is.close();
			} catch (IOException e) {
				BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
				String line = "";
				
				StringBuilder sb = new StringBuilder();	
				
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				return sb.toString(); 
			//	submitJob(jobId);
				
			}
			return result;
			
		}
		public  String createDocument() throws IOException
		{

			String url = this.get_url() + "/molpro/documents";
			String charset = "UTF-8";
			String documentClass = ct.documentClass ;
			String documentFormat = "PDF";
			String documentName = "";
			
			String filename = this._file;
			 File binaryFile = new File(filename);
			
			String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
			String CRLF = "\r\n"; // Line separator required by multipart/form-data.

			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	        String authString = username + ":" + password;
			String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
			
			
			    OutputStream output = connection.getOutputStream();
			    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
			

			    writer.append("--" + boundary).append(CRLF);
			    writer.append("Content-Disposition: form-data; name=\"documentClass\"").append(CRLF);
			    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
			    writer.append(CRLF).append(documentClass).append(CRLF).flush();
			    
			    
			    writer.append("--" + boundary).append(CRLF);
			    writer.append("Content-Disposition: form-data; name=\"documentFormat\"").append(CRLF);
			    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
			    writer.append(CRLF).append(documentFormat).append(CRLF).flush();

			    writer.append("--" + boundary).append(CRLF);
			    writer.append("Content-Disposition: form-data; name=\"documentName\"").append(CRLF);
			    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
			    writer.append(CRLF).append(documentName).append(CRLF).flush();
		    
			    writer.append("--" + boundary).append(CRLF);
			    writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
			    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
			    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
			    writer.append(CRLF).flush();
			    
			    
			    FileInputStream inputStream = new FileInputStream(binaryFile);
		    
			    byte[] buffer = new byte[4096];
		        int bytesRead = -1;
		        while ((bytesRead = inputStream.read(buffer)) != -1) {
		            output.write(buffer, 0, bytesRead);
		        }
		        output.flush();
		        inputStream.close();
			   
			    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
			    writer.append("--" + boundary + "--").append(CRLF).flush();
			    writer.append(CRLF).flush();
			inputStream.close();
			output.close();
			// Request is lazily fired whenever you need to obtain information about response.
			//int responseCode = connection.getResponseCode();
			try{
				
		     InputStream is =connection.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);

						int numCharsRead;
						char[] charArray = new char[1024];
						StringBuffer sb = new StringBuffer();
						while ((numCharsRead = isr.read(charArray)) > 0) {
							sb.append(charArray, 0, numCharsRead);
						}
			String result = sb.toString();
			//System.out.println(result); // Should be 200
			isr.close();
			is.close();
			return result;
		} catch (IOException e) {
			BufferedReader br = new BufferedReader(new InputStreamReader((connection.getErrorStream())));
			String line = "";
			
			StringBuilder sb = new StringBuilder();	
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString(); 
		//	submitJob(jobId);
			
		}
		}
		public  String createAddressList() throws IOException
		{

			String url = this.get_url() + "/molpro/addressLists";
			String charset = "UTF-8";
			
			
			//String filename = this._file;
			 //File binaryFile = new File(filename);
			
			//String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
			

			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/xml");
	        String authString = username + ":" + password;
			String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			connection.setRequestProperty("Authorization", "Basic " + authStringEnc);		
			
			
			    OutputStream output = connection.getOutputStream();
			       
			       OutputStreamWriter wout = new OutputStreamWriter(output, charset);
		
			       wout.write(this.addressListXML);
			       wout.flush();
			       output.close();  
			    
			
		     InputStream is =connection.getInputStream();
						InputStreamReader isr = new InputStreamReader(is);

						int numCharsRead;
						char[] charArray = new char[1024];
						StringBuffer sb = new StringBuffer();
						while ((numCharsRead = isr.read(charArray)) > 0) {
							sb.append(charArray, 0, numCharsRead);
						}
			String result = sb.toString();
			//System.out.println(result); // Should be 200
			isr.close();
			is.close();
			return result;
			
		}
		@SuppressWarnings("unchecked")
		public void createAndSubmitJob(sendSettings ss,String file,int addressMappingId) throws IOException, TransformerException
		{
			ct = ss;
			
			pdfFile = file;
					
			  String str = "";
			    this._file = pdfFile;

			  
			  	String results =createDocument();
			  	System.out.println( "Creating C2m Document");
				this.documentId = getString(results,"/document/id/text()");
				System.out.println( "DocumentID: "+ this.documentId);
				
				if(Integer.parseInt( this.documentId )== 0)
				{
					if(results.contains("<?xml"))
					{
					System.out.println( getString(results,"/document/description/text()"));
					}
					else
					{
						
						System.out.println("The username/password does not appear to be correct.  Please note you are using the ");
					}
					return;
				}
				
				this.createAddressListXML(addressMappingId);
		  		
		  		System.out.println( "Uploading Address List");
		  		String res =createAddressList();
				this.addressId = getString(res,"/addressList/id/text()");
				
				String aListStatus = getString(res,"/addressList/status/text()");
				while(!aListStatus.equalsIgnoreCase("3"))
				{
					System.out.println("Address List has not finished processing, waiting...");
					try {
						TimeUnit.SECONDS.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					aListStatus = getString(getAddressListStatus(),"/addressList/status/text()");
				}
				
				System.out.println(res);
				//return;
				
				System.out.println( "AddressID: "+ this.addressId);
				System.out.println( "Creating Job");
				
				res = createJob(this.documentId,this.addressId );
				
				this.jobId =getString(res ,"/job/id/text()");
				String r = "";
				if(jobId.equalsIgnoreCase("0"))
				{
					r = getString(res,"/job/description/text()");
				
				}
				else
				{
					System.out.println( "Submitting Job: " + this.jobId);
					r = getString(submitJob(),"/job/description/text()");
				}
				
				if(r.equalsIgnoreCase("success"))
				{
					System.out.println( "Job successfuly submitted: " + this.jobId);
					System.out.println( "FINAL STATUS : " +getString(getJobStatus(),"/job/description/text()" ));
		
				}
				else
				{
					System.out.println( "There was an error in submitting: " + r);
				}
				
				
				
			return;
			
		}
		public  String getJobStatus() {

			try {
				String webPage = get_url() +  "/molpro/jobs/" + this.jobId;
				String authString = username + ":" + password;		
				String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			
				URL url = new URL(webPage);
				
				HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
				
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				urlConnection.setRequestMethod("GET");
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();

				//System.out.println("*** BEGIN ***");
				//System.out.println(result);
				//System.out.println("*** END ***");
				
				is.close();
				isr.close();
				urlConnection.disconnect();
				return result;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
		public  String getAddressListStatus() {

			try {
				String webPage = get_url() +  "/molpro/addressLists/" + this.addressId;
				String authString = username + ":" + password;		
				String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			
				URL url = new URL(webPage);
				
				HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
				
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				urlConnection.setRequestMethod("GET");
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				String result = sb.toString();

				//System.out.println("*** BEGIN ***");
				//System.out.println(result);
				//System.out.println("*** END ***");
				
				is.close();
				isr.close();
				urlConnection.disconnect();
				return result;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}
	
		
	
	public  String getString(String xml,String exprStr) {
		   DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        try {
	        	InputStream is = new ByteArrayInputStream(xml.getBytes());
	            DocumentBuilder builder = domFactory.newDocumentBuilder();
	            Document dDoc = builder.parse(is);

	            XPath xPath = XPathFactory.newInstance().newXPath();
	            XPathExpression expr =  xPath.compile(exprStr);
	            NodeList nodes = (NodeList) expr.evaluate(dDoc, XPathConstants.NODESET);
	            for (int i = 0; i < nodes.getLength(); )
	            {
	            //	System.out.println( nodes.item(i).getNodeValue());
	            	return( nodes.item(i).getNodeValue());
	            	
	            //System.out.println(node.getNodeValue());
	            }
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	return "0";		
    }
	
	

	public String get_url() {
		if(mode == Mode.Stage)
			return this._Srestapi;
			else
				return this._restapi;
	}

}
