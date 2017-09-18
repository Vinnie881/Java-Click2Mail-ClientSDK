package c2m;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class job {
	public int startPage;
	public int endPage;
	public sendSettings ss;
	public List<Map<String, String>> addresses = new ArrayList<Map<String, String>>();
	public job(sendSettings ss,int startPage,int endPage)
	{
		this.startPage = startPage;
		this.endPage = endPage;
		this.ss = ss;
	}
	public void addJobAddress(String name, String organization, String address1, String address2, String address3, String city, String state,String zip, String country, String uniqueId )
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);;
		map.put("organization", organization);;
		map.put("address1", address1);;
		map.put("address2", address2);;
		map.put("address3", address3);;
		map.put("city", city);;
		map.put("state", state);;
		map.put("postalCode", zip);
		map.put("country",country);
	//	map.put("c2m_uniqueId", uniqueId);
		addresses.add(map);
		 
	}
}
