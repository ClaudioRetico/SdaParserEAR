package sda;

public class LogIFXRow{
	public LogIFXRow() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String theEvent=null ;
	private String laData = null;
	private String hhmmss = null;
	private String Val1 = null;
	private int Val2 =0;
	private boolean fl_wrt = false;
	public String getTheEvent() {
		return theEvent;
	}
	public void setTheEvent(String theEvent) {
		this.theEvent = theEvent;
	}
	public String getLaData() {
		return laData;
	}
	public void setLaData(String laData) {
		this.laData = laData;
	}
	public String getHhmmss() {
		return hhmmss;
	}
	public void setHhmmss(String hhmmss) {
		this.hhmmss = hhmmss;
	}
	public String getVal1() {
		return Val1;
	}
	public void setVal1(String val1) {
		Val1 = val1;
	}
	public boolean isFl_wrt() {
		return fl_wrt;
	}
	public void setFl_wrt(boolean fl_wrt) {
		this.fl_wrt = fl_wrt;
	}
	public int getVal2() {
		return Val2;
	}
	public void setVal2(int val2) {
		Val2 = val2;
	}

}
