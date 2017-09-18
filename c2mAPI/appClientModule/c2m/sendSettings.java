package c2m;

public class sendSettings{
	
	public String envelope = "#10 Double Window";
	public String layout ="Address on First Page";
	public String documentClass="Letter 8.5 x 11";;
	public String productionTime="Next Day";;
	public String color="Full Color";;
	public String paperType="White 24#";;
	public String printOption="Printing both sides";;
	public String mailClass="First Class";;
	
	public String raName="";
	public String raAddress1="";
	public String raAddress2="";
	public String raOrganization="";;
	public String raCity="";
	public String raState="";
	public String raZip="";
	public sendSettings(String envelope,String layout, String documentClass,String productionTime, String color, String paperType, String printOption, String mailClass)
	{
		this.envelope = envelope;
		this.layout = layout;
		this.documentClass = documentClass;
		this.productionTime = productionTime;
		this.color = color;
		this.paperType = paperType;
		this.printOption = printOption;
		this.mailClass = mailClass;
	}
}
