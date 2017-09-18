package c2m;

import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.security.Key;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
//import java.security.spec.*;
//import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

//import java.util.Collection;
//import sun.misc.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;



public class ImportCA {


	static String stage = new StringBuilder()//.append("-----BEGIN CERTIFICATE-----")
	.append("MIIGojCCBYqgAwIBAgIDApkSMA0GCSqGSIb3DQEBBQUAMIGMMQswCQYDVQQGEwJJ")
	.append("TDEWMBQGA1UEChMNU3RhcnRDb20gTHRkLjErMCkGA1UECxMiU2VjdXJlIERpZ2l0")
	.append("YWwgQ2VydGlmaWNhdGUgU2lnbmluZzE4MDYGA1UEAxMvU3RhcnRDb20gQ2xhc3Mg")
	.append("MiBQcmltYXJ5IEludGVybWVkaWF0ZSBTZXJ2ZXIgQ0EwHhcNMTQxMDMxMDA1MjE5")
	.append("WhcNMTYxMDMxMDUzMzMwWjCBpjEZMBcGA1UEDRMQNEpWMjlENW5vMWsxRVgzWTEL")
	.append("MAkGA1UEBhMCVVMxETAPBgNVBAgTCFZpcmdpbmlhMRIwEAYDVQQHEwlBcmxpbmd0")
	.append("b24xETAPBgNVBAoTCEMyTSwgTExDMRkwFwYDVQQDFBAqLmNsaWNrMm1haWwuY29t")
	.append("MScwJQYJKoZIhvcNAQkBFhh3ZWJtYXN0ZXJAY2xpY2sybWFpbC5jb20wggEiMA0G")
	.append("CSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC4AmKQKPchsc2xVN1gSPO+oQHOgTZp")
	.append("DJoIo6cibbQoWIVTPk+LbCWPn9adgqoqzhRP5H7BOPzy1P4xHpXphoo9J1T3Kpy+")
	.append("JNyfGjKOUWk0V8tZuW2EEfAuhcn+SXoxPKJf4gEin7VfEYCd0j5AebXIwtmBy1eu")
	.append("pXKTP511mQOUNK7hvoPv1X3twylTumldFdJLvQiF5H/DxayjfKY+NPDdfqK/Mugv")
	.append("WoBGStv/c8ftPbjIe8nmWlFjobzkGRdpURTdDyGy1reXlhyakkicA5ctkTI3/MqK")
	.append("7qbaMw/lO1Uza+MHJMO1cErbSwzagFmK6Ep+rsy0OQe46JphGM/eI8pNAgMBAAGj")
	.append("ggLvMIIC6zAJBgNVHRMEAjAAMAsGA1UdDwQEAwIDqDAdBgNVHSUEFjAUBggrBgEF")
	.append("BQcDAgYIKwYBBQUHAwEwHQYDVR0OBBYEFN5EbLM2QheLSLi+py9pBrZiOVWkMB8G")
	.append("A1UdIwQYMBaAFBHbI0X9VMxqcW+EigPXvvcBLyaGMCsGA1UdEQQkMCKCECouY2xp")
	.append("Y2sybWFpbC5jb22CDmNsaWNrMm1haWwuY29tMIIBVgYDVR0gBIIBTTCCAUkwCAYG")
	.append("Z4EMAQICMIIBOwYLKwYBBAGBtTcBAgMwggEqMC4GCCsGAQUFBwIBFiJodHRwOi8v")
	.append("d3d3LnN0YXJ0c3NsLmNvbS9wb2xpY3kucGRmMIH3BggrBgEFBQcCAjCB6jAnFiBT")
	.append("dGFydENvbSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTADAgEBGoG+VGhpcyBjZXJ0")
	.append("aWZpY2F0ZSB3YXMgaXNzdWVkIGFjY29yZGluZyB0byB0aGUgQ2xhc3MgMiBWYWxp")
	.append("ZGF0aW9uIHJlcXVpcmVtZW50cyBvZiB0aGUgU3RhcnRDb20gQ0EgcG9saWN5LCBy")
	.append("ZWxpYW5jZSBvbmx5IGZvciB0aGUgaW50ZW5kZWQgcHVycG9zZSBpbiBjb21wbGlh")
	.append("bmNlIG9mIHRoZSByZWx5aW5nIHBhcnR5IG9ibGlnYXRpb25zLjA1BgNVHR8ELjAs")
	.append("MCqgKKAmhiRodHRwOi8vY3JsLnN0YXJ0c3NsLmNvbS9jcnQyLWNybC5jcmwwgY4G")
	.append("CCsGAQUFBwEBBIGBMH8wOQYIKwYBBQUHMAGGLWh0dHA6Ly9vY3NwLnN0YXJ0c3Ns")
	.append("LmNvbS9zdWIvY2xhc3MyL3NlcnZlci9jYTBCBggrBgEFBQcwAoY2aHR0cDovL2Fp")
	.append("YS5zdGFydHNzbC5jb20vY2VydHMvc3ViLmNsYXNzMi5zZXJ2ZXIuY2EuY3J0MCMG")
	.append("A1UdEgQcMBqGGGh0dHA6Ly93d3cuc3RhcnRzc2wuY29tLzANBgkqhkiG9w0BAQUF")
	.append("AAOCAQEA3k6gwjWL2l/EF1iyLCToO8In5yqHryqqURRarW/0HHYRXGCJxKd5Hr+R")
	.append("00RGTM67x5y1o63wnCHzW/LEiuye+HaYCRfwYi4MQyCo+SlGi45/qykxUryGMB21")
	.append("2g3Jq95u6iTi8akN2kOd4ajLFQOu+97til92wNKklWtJdXf/IGBfxGdI8ozYHgeR")
	.append("Xnz4Py7T35R5SdKwv1Iqjy/aqZ7uUqnamFmG3+FPTjUDRaY0NyCoBOLWrhZPBUh9")
	.append("iRK8gTeVeU1DDZxRaiWv0SQLTe9mHyS0o77EerNNk3dOlZRaYvq2LaEzIz7S2R+U")
	.append("z7f42FrdbtiGL1xXfBwxWwBRujUZzg==").toString();
	//.append("-----END CERTIFICATE-----").toString();
	static String c2m = new StringBuilder()//.append("-----BEGIN CERTIFICATE-----")
			.append("MIIIxzCCB6+gAwIBAgINAPYNTf4AAAAAVMxYlTANBgkqhkiG9w0BAQsFADCBujEL")
			.append("MAkGA1UEBhMCVVMxFjAUBgNVBAoTDUVudHJ1c3QsIEluYy4xKDAmBgNVBAsTH1Nl")
			.append("ZSB3d3cuZW50cnVzdC5uZXQvbGVnYWwtdGVybXMxOTA3BgNVBAsTMChjKSAyMDE0")
			.append("IEVudHJ1c3QsIEluYy4gLSBmb3IgYXV0aG9yaXplZCB1c2Ugb25seTEuMCwGA1UE")
			.append("AxMlRW50cnVzdCBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eSAtIEwxTTAeFw0xNTEx")
			.append("MDUyMDQzNDhaFw0xNjExMDUyMTEzNDZaMIHNMQswCQYDVQQGEwJVUzERMA8GA1UE")
			.append("CBMIVmlyZ2luaWExEjAQBgNVBAcTCUFybGluZ3RvbjETMBEGCysGAQQBgjc8AgED")
			.append("EwJVUzEZMBcGCysGAQQBgjc8AgECEwhEZWxhd2FyZTEdMBsGA1UEChMUQ2xpY2sy")
			.append("bWFpbCAoQzJNIExMQykxHTAbBgNVBA8TFFByaXZhdGUgT3JnYW5pemF0aW9uMRAw")
			.append("DgYDVQQFEwczNzM0ODM2MRcwFQYDVQQDEw5jbGljazJtYWlsLmNvbTCCASIwDQYJ")
			.append("KoZIhvcNAQEBBQADggEPADCCAQoCggEBAM4yIt0LJVN/nKLrMPUunbtEBdMZ/o9Z")
			.append("wda60z1RQn4DkDGImSB5d+b4gOVYJ6EjJ3YqVoNIL/qYHvqWlbW/HTfkvexNZqg2")
			.append("WWBes6pKMqXI6fD7cS6gE27qQAh+IldJxkL9OiBW13qcj3s8NK/XDvbe8Fo95KoQ")
			.append("3VN4CuUEtWe/bc1GfCMOgANGx2IAgqeItucKAh1afO/ek7BQKu882dwC3uLtNCQU")
			.append("ii27DsTM6fSSdOmPTTm6EtcfCLQxr8ppfWeICRsl9kJ1KTqF0g7zU+/dOkKqLYNN")
			.append("F4bdbTW4HSIMPTBTtEQLXaGdWKEZVqD9dxi0+/YPSUSY9DzBszAq9NcCAwEAAaOC")
			.append("BLUwggSxMIIB0wYDVR0RBIIByjCCAcaCDmNsaWNrMm1haWwuY29tghNibG9nLmNs")
			.append("aWNrMm1haWwuY29tghhpbnRlZ3JhdGUuY2xpY2sybWFpbC5jb22CGnByaW50ZHJp")
			.append("dmVyLmNsaWNrMm1haWwuY29tghh0ZW1wbGF0ZXMuY2xpY2sybWFpbC5jb22CFGJh")
			.append("dGNoLmNsaWNrMm1haWwuY29tgh1tYWlsaW5nLW9ubGluZS5jbGljazJtYWlsLmNv")
			.append("bYIWcmVwb3J0cy5jbGljazJtYWlsLmNvbYIfZWFzeWxldHRlcnNlbmRlci5jbGlj")
			.append("azJtYWlsLmNvbYIZZWRpdG9yLXByby5jbGljazJtYWlsLmNvbYIacmVtaW5kc2Fu")
			.append("dGEuY2xpY2sybWFpbC5jb22CGHByaW50c2hvcC5jbGljazJtYWlsLmNvbYIdZGVz")
			.append("aWduc2VydmljZXMuY2xpY2sybWFpbC5jb22CGWRldmVsb3BlcnMuY2xpY2sybWFp")
			.append("bC5jb22CGGtub3dsZWRnZS5jbGljazJtYWlsLmNvbYITcmVzdC5jbGljazJtYWls")
			.append("LmNvbYITZWRkbS5jbGljazJtYWlsLmNvbYISd3d3LmNsaWNrMm1haWwuY29tMIIB")
			.append("fQYKKwYBBAHWeQIEAgSCAW0EggFpAWcAdgBo9pj4H2SCvjqM7rkoHUz8cVFdZ5PU")
			.append("RNEKZ6y7T0/7xAAAAVDZf5TLAAAEAwBHMEUCIBDNn6dOOnuAKNM/1Iw2IcRv8k/v")
			.append("Nt84AqWrp39PUf/MAiEA+qZnIN4GR3XyZsUs2l85rysL3FursEp3VmHugsGHVXoA")
			.append("dQBWFAaaL9fC7NP14b1Esj7HRna5vJkRXMDvlJhV1onQ3QAAAVDZf5jNAAAEAwBG")
			.append("MEQCICblJujiN0ugInBLGb+zVF983J80J95gimdqW+6QuXsVAiBc7nu0FOTMfC+Y")
			.append("oev15vkrX3BJ+MIeyHHTuUl2kxTEiAB2AHRhtKCc+z1B11FZV1sudkmkRajSdwmw")
			.append("zFZKZIK360GjAAABUNl/m+AAAAQDAEcwRQIgcChegoiA98KDNSIWncYTblE08v+d")
			.append("NvktgAQijJgX7hQCIQD31Zv2opwvNe3OFoOcM/RaevwMgd2m/WLp9h97qCyx1jAL")
			.append("BgNVHQ8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMGgGCCsG")
			.append("AQUFBwEBBFwwWjAjBggrBgEFBQcwAYYXaHR0cDovL29jc3AuZW50cnVzdC5uZXQw")
			.append("MwYIKwYBBQUHMAKGJ2h0dHA6Ly9haWEuZW50cnVzdC5uZXQvbDFtLWNoYWluMjU2")
			.append("LmNlcjAzBgNVHR8ELDAqMCigJqAkhiJodHRwOi8vY3JsLmVudHJ1c3QubmV0L2xl")
			.append("dmVsMW0uY3JsMEEGA1UdIAQ6MDgwNgYKYIZIAYb6bAoBAjAoMCYGCCsGAQUFBwIB")
			.append("FhpodHRwOi8vd3d3LmVudHJ1c3QubmV0L3JwYTAfBgNVHSMEGDAWgBTD99C1KjCt")
			.append("rw2RIXA5VN28iXDHOjAdBgNVHQ4EFgQUkhrskO6Bj4EiIffYttOG5qYIbBAwCQYD")
			.append("VR0TBAIwADANBgkqhkiG9w0BAQsFAAOCAQEAtqjO8KzLDMyL80PIp6B38oDJW9hE")
			.append("aMM8ZeEMVyX+knrH/HCkTzheTQASrkpCA4oPq2+rCMntY5HPrf2yjkumu5uS3AFf")
			.append("xogORHArVggMu5aPbjlToPqbF36vT1U8LMV1hXecUwZ8Sn0R80NtdQVhM3HdqAuS")
			.append("sld7Xk3m0/G6ieYajQ1TA8vKeJp6ErQ4s8Pq1y8kLfosy9dhl4BCjIhN4OGfjsSd")
			.append("8ShUpnGSIeS1K2EC3npZEuO/bBU8Vh8sxwxROVmE6NKBYur5LjjRxvfhxwmSDUxu")
			.append("6vYCXmMHJjlBoHY48Cosn6at6fhzySkQmOtZaM0oeUST/MgVPWdRc7+Llg==").toString();
//			.append("-----END CERTIFICATE-----").toString();

public static void importCAsStage() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
{

	
	InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(stage));
	X509Certificate ca = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(stream);

	KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	ks.load(null, null);
	ks.setCertificateEntry(Integer.toString(1), ca);

	TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	tmf.init(ks);

	SSLContext context = SSLContext.getInstance("TLS");
	try {
		context.init(null, tmf.getTrustManagers(), null);
	} catch (KeyManagementException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
public static void depreciated(String passphrase,String Alias) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
{
	
	InputStream certIn=  new ByteArrayInputStream(Base64.getDecoder().decode(c2m));
	final char sep = File.separatorChar;
    File dir = new File(System.getProperty("java.home") + sep + "lib" + sep + "security");
    File file = new File(dir, "cacerts");
    InputStream localCertIn = new FileInputStream(file);

    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(localCertIn, passphrase.toCharArray());
    if (keystore.containsAlias(Alias)) {
        certIn.close();
        localCertIn.close();
        return;
    }
    localCertIn.close();

    BufferedInputStream bis = new BufferedInputStream(certIn);
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    while (bis.available() > 0) {
        Certificate cert = cf.generateCertificate(bis);
        keystore.setCertificateEntry("myAlias", cert);
    }

    certIn.close();

    OutputStream out = new FileOutputStream(file);
    keystore.store(out, passphrase.toCharArray());
    out.close();
}
public static void importCAs() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
{
	
	InputStream stream = new ByteArrayInputStream(Base64.getDecoder().decode(c2m));

		    		
	X509Certificate ca = (X509Certificate) CertificateFactory.getInstance("X.509")
	                        .generateCertificate(stream);

	KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
	ks.load(null, null);
	ks.setCertificateEntry(Integer.toString(1), ca);

	TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
	tmf.init(ks);

	SSLContext context = SSLContext.getInstance("TLS");
	try {
		context.init(null, tmf.getTrustManagers(), null);
	} catch (KeyManagementException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}



}