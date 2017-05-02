package sda.nomn;

public class TotalCPURow{
	private String Nome_Host =null; 
	private String StartTime =null; 
	private String StartDate =null; 
	private String OS =null; 
	private int Intervallo_campione=0;
	private int CPU_LAPR=0;
	private String Serial =null;
	private String Machine_Type =null;
	private int Num_Interval=0;
	private float User=0;
	private float Sys =0;
	private float Wait =0;
	private float Idle =0;
	private int Entitled =0;
	public TotalCPURow() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getNome_Host() {
		return Nome_Host;
	}
	public void setNome_Host(String nome_Host) {
		Nome_Host = nome_Host;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getOS() {
		return OS;
	}
	public void setOS(String oS) {
		OS = oS;
	}
	public int getIntervallo_campione() {
		return Intervallo_campione;
	}
	public void setIntervallo_campione(int intervallo_campione) {
		Intervallo_campione = intervallo_campione;
	}
	public int getCPU_LAPR() {
		return CPU_LAPR;
	}
	public void setCPU_LAPR(int cPU_LAPR) {
		CPU_LAPR = cPU_LAPR;
	}
	public String getSerial() {
		return Serial;
	}
	public void setSerial(String serial) {
		Serial = serial;
	}
	public String getMachine_Type() {
		return Machine_Type;
	}
	public void setMachine_Type(String machine_Type) {
		Machine_Type = machine_Type;
	}
	public int getNum_Interval() {
		return Num_Interval;
	}
	public void setNum_Interval(int num_Interval) {
		Num_Interval = num_Interval;
	}
	public float getUser() {
		return User;
	}
	public void setUser(float user) {
		User = user;
	}
	public float getSys() {
		return Sys;
	}
	public void setSys(float sys) {
		Sys = sys;
	}
	public float getWait() {
		return Wait;
	}
	public void setWait(float wait) {
		Wait = wait;
	}
	public float getIdle() {
		return Idle;
	}
	public void setIdle(float idle) {
		Idle = idle;
	}
	public int getEntitled() {
		return Entitled;
	}
	public void setEntitled(int entitled) {
		Entitled = entitled;
	}


}
