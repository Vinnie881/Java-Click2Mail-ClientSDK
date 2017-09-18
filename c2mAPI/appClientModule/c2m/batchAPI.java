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





public class batchAPI {
	public String username="";
	public String password = "";
	public Mode mode;
	public int addressListMappingId = 2;
	List<job> jobs = new ArrayList<job>();
	

	
	
	public void addJob(job myJob)
	{
		jobs.add(myJob);
	}
	
	public batchAPI(String username,String password,Mode mode)
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
	
	
	public enum Mode{
		Stage,Production
	}
		public String pdfFile = "";
		private sendSettings ct;
		public static final Charset UTF_8 = Charset.forName("UTF-8");
	    public  static String _Smainurl = "https://stage-batch.click2mail.com";
		public static String  _Lmainurl = "https://batch.click2mail.com";
		
		
		public String batchId = "0";
		public String batchXML = "";
		
		private static String _authinfo  = "";
		
		public String createBatchXMl()
		{
			return "";
		}
		public String createBatchXML()
		{
			
				this.batchXML =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
				"<batch>" +  
				"<username>" + this.username + "</username>" +
				"<password>" + this.password + "</password>" + 
				 "<filename>" + this.pdfFile + "</filename>" +  
				 "<appSignature>JAVA API SDK" + "</appSignature>";
				  
				jobs.forEach((myJob)->{
					
					
					this.batchXML += "<job>" + 
					  "<startingPage>" +  myJob.startPage + "</startingPage>" +   
					  "<endingPage>" + myJob.endPage + "</endingPage>" + 
					  "<printProductionOptions>" + 
					  "<documentClass>" + myJob.ss.documentClass + "</documentClass>" +  
					  " <layout>" + myJob.ss.layout + "</layout>" +
					  " <productionTime>" + myJob.ss.productionTime + "</productionTime>" +
					  " <envelope>" + myJob.ss.envelope + "</envelope>" +
					  " <color>" + myJob.ss.color + "</color>" +
					  " <paperType>" + myJob.ss.paperType + "</paperType>" +
					  " <printOption>" + myJob.ss.printOption + "</printOption>" +
					  " <mailClass>" + myJob.ss.mailClass + "</mailClass>" +
					  "</printProductionOptions>";
					if(!myJob.ss.raAddress1.equalsIgnoreCase(""))
					{
						 this.batchXML +="<returnAddress>" + 
						   "<name>"+myJob.ss.raName+"</name>"  +
						   "<organization>"+myJob.ss.raOrganization+"</organization>" +  
						   "<address1>"+myJob.ss.raAddress1+"</address1>" + 
						   "<address2>"+myJob.ss.raAddress2+"</address2>" + 
						   "<city>"+myJob.ss.raCity+"</city>" + 
						   "<state>"+myJob.ss.raState+"</state>" + 
						   "<postalCode>"+myJob.ss.raZip+"</postalCode>"+   
						 "</returnAddress>";
					}
					   
					 this.batchXML +="<recipients>";
					   
					myJob.addresses.forEach((myAddresses)->{
						this.batchXML += "<address>";
						myAddresses.forEach( (k,v)->
						{
							 this.batchXML += "<" + k + ">" + v + "</" + k + ">";
						
						});
						 this.batchXML+= "</address>";
					});
					
					
					 this.batchXML+= "</recipients></job>";
				});
				 this.batchXML+= "</batch>";
				return this.batchXML;

		}
		
		public String startProcess(String file ) throws IOException
		{
			String results = "";
			
			//ct.readAddressListToVariable();
			pdfFile = file;
			createBatchXML();
			 System.out.println("Createing Batch");
			createBatch();
			
			 try {
				 System.out.println("Uploading XML");
				 if( Integer.parseInt(batchId) > 0)
				 {
					 batchId = "0";
				 }
				 else if(!postXmlFile() )
				 {
					 batchId = "0";
				 }
				 else
				 {
					 System.out.println( "Uploading PDF");
					 uploadPDF();
					 System.out.println( "Submitting For Processing");
					 submitBatch();
					 System.out.println("Verify Status");
					 results = getBatchStatus();
				 }
				 
				 if(batchId.equals("0"))
				 {
					 System.out.println( "Did not send to Click2Mail Successfully, Most times this is due to an Incorrect username/password combo or invalid SKU Settings");
				 }
				 else
				 {
					 System.out.println("Successfully Processed batchID: " + batchId);
				 }
				 
			} catch (IOException e) {
				e.printStackTrace();
			}
			 return results;
		}
		public void uploadPDF() throws IOException{
			File importFile = new File(pdfFile);
			String request        = get_url() + "/v1/batches/" + batchId;
			URL    url            = new URL( request );
			
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput( true );
			conn.setInstanceFollowRedirects( false );
			conn.setRequestMethod( "PUT" );
			String authString = username + ":" + password;
			String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
							
			conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
			conn.setRequestProperty( "Content-Type", "application/pdf"); 
			
	        try {
	            
	            OutputStream outputStream = conn.getOutputStream();
	            InputStream inputStream = new FileInputStream(importFile);
	 
	            byte[] buffer = new byte[1024];
	            int bytesRead = -1;
	            while ((bytesRead = inputStream.read(buffer)) != -1) {
	                outputStream.write(buffer, 0, bytesRead);
	            }
	 
	            
	          //  System.out.println(conn.getResponseCode());
	            InputStream is =conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
			//	String result = sb.toString();

				//System.out.println("*** BEGIN ***");
				//System.out.println(result);
				//System.out.println("*** END ***");
				outputStream.close();
	            is.close();
	            inputStream.close();
	          
	            conn.disconnect();
	            System.out.println("PDF File uploaded");
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		
						
		}


		private  String createBatch() {
			String result = "";
			try {
				
				String webPage = get_url() +  "/v1/batches";
			//	System.setProperty("javax.net.ssl.trustStore", "C:/test/cacerts");
			//	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

				String authString = username + ":" + password;
				//System.out.println("auth string: " + authString);
				String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			//	System.out.println("Base64 encoded auth string: " + authStringEnc);

				URL url = new URL(webPage);
				
				HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
				
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				urlConnection.setRequestMethod("POST");
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				result = sb.toString();

				/*System.out.println("*** BEGIN ***");
				System.out.println(result);
				System.out.println("*** END ***");
				*/
				batchId =  getString(result,"/batchjob/id/text()");
				is.close();
				isr.close();
				urlConnection.disconnect();
			} catch (MalformedURLException e) {
				batchId =  "0";
			//	e.printStackTrace();
			} catch (IOException e) {
				batchId =  "0";
				//e.printStackTrace();
			}
			return result;
		}
		
		
		public  void submitBatch() {

			try {
				String webPage = get_url() +  "/v1/batches/" + batchId;
			//	System.setProperty("javax.net.ssl.trustStore", "C:/test/cacerts");
			//	System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

				String authString = username + ":" + password;
			//	System.out.println("auth string: " + authString);
				String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
			//	System.out.println("Base64 encoded auth string: " + authStringEnc);

				URL url = new URL(webPage);
				
				HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
				
				urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
				urlConnection.setRequestMethod("POST");
				InputStream is = urlConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);

				int numCharsRead;
				char[] charArray = new char[1024];
				StringBuffer sb = new StringBuffer();
				while ((numCharsRead = isr.read(charArray)) > 0) {
					sb.append(charArray, 0, numCharsRead);
				}
				//String result = sb.toString();

				//System.out.println("*** BEGIN ***");
				//System.out.println(result);
				//System.out.println("*** END ***");
				
				//batchid =  getString("id",result);
				is.close();
				isr.close();
				urlConnection.disconnect();
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	
		public  String getBatchStatus() {

			try {
				String webPage = get_url() +  "/v1/batches/" + batchId;
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
		private boolean postXmlFile() throws IOException
		{

		String urlParameters  = this.batchXML;
		//System.out.println(urlParameters);
		String request        = get_url() + "/v1/batches/" + batchId;
		URL    url            = new URL( request );
		
		HttpURLConnection conn= (HttpURLConnection) url.openConnection();
		String authString = username + ":" + password;
		conn.setDoOutput( true );
		conn.setInstanceFollowRedirects( false );
		conn.setRequestMethod( "PUT" );
		String authStringEnc = new String(Base64.getEncoder().encode(authString.getBytes()));
						
		conn.setRequestProperty("Authorization", "Basic " + authStringEnc);
		conn.setRequestProperty( "Content-Type", "application/xml"); 
		
        try {
            
            OutputStream outputStream = conn.getOutputStream();
            InputStream inputStream = new ByteArrayInputStream(urlParameters.getBytes(UTF_8));
 
            byte[] buffer = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
 
            
            //System.out.println(conn.getResponseCode());
            InputStream is =conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			//String result = sb.toString();

			//System.out.println("*** BEGIN ***");
			//System.out.println(result);
			//System.out.println("*** END ***");
			outputStream.close();
            is.close();
            inputStream.close();
          
            conn.disconnect();
            System.out.println("XML File uploaded");
            return true;
        } catch (IOException ex) {
           return false;
        }
	
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
			return this._Smainurl;
			else
				return this._Lmainurl;
	}


	

}
