/**
 * 
 */
package sda.nomn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.sql.DataSource;

import sda.ifx.LogIFXRow;

/**
 * @author IT040213
 *
 */
public class NmonCPUParser extends Thread {

	private static final String LISTFILE = "C:\\Filemio\\S&D\\Architettura Luminosa\\NMONPArser\\FileList.txt";
	private static final String NMONDIRIN = "C:\\Filemio\\S&D\\Architettura Luminosa\\NMONPArser\\InputFile\\";
	private static final String NMONFILROUT = "C:\\Filemio\\S&D\\Architettura Luminosa\\NMONPArser\\OutputFile\\TotalCPU.csv";
	private static String host;
	private static String os;
	private static String sec_interval;
	private static String cpu_used;
	private static String serial;
	private static String machine_type;
	private static String interval;
	private static String CPU_Data;
	private static String st_time;
	private static String st_date;

	DataSource ds = null;
	Connection cn = null;
	TotalCPURow cpuRow;

	/**
	 * @param args
	 */
	public void run() {

		BufferedReader br = null;
		FileReader fr = null;
		BufferedReader brNmon = null;
		FileReader frNmon = null;
		BufferedWriter brOut = null;
		FileWriter frOut = null;

		try {
			fr = new FileReader(LISTFILE);
			br = new BufferedReader(fr);
			String sCurrentLine;
			frOut = new FileWriter(NMONFILROUT);
			brOut = new BufferedWriter(frOut, 32768);
			String sOutline;
			sOutline = "Nome_Host,StartTime,StartDate,OS,Intervallo_campione,CPU_LPAR,Serial,Machine_Type,Num_Interval,User,Sys,Wait,IdleEntitled Capacity";
			brOut.write(sOutline);
			brOut.newLine();
			br = new BufferedReader(new FileReader(LISTFILE));
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println("CPUParser: " + sCurrentLine);
				frNmon = new FileReader(NMONDIRIN + sCurrentLine.trim());
				brNmon = new BufferedReader(frNmon, 32768);
				host = "";
				os = "";
				sec_interval = "";
				cpu_used = "";
				serial = "";
				machine_type = "";
				interval = "";
				CPU_Data = "";
				st_time = "";
				st_date = "";
				String sNmonLine = "";
				while ((sNmonLine = brNmon.readLine()) != null) {
					if (parse(sNmonLine.trim())) {
						sOutline = host.trim() + "," + st_time.trim() + "," + st_date.trim() + "," + os.trim() + ","
								+ sec_interval.trim() + "," + cpu_used.trim() + "," + serial.trim() + ","
								+ machine_type.trim() + "," + interval.trim() + "," + CPU_Data.trim();
						brOut.write(sOutline);
						brOut.newLine();
					}
					;
				}
				brNmon.close();
				frNmon.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
				brOut.close();
				frOut.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static boolean parse(String line) {

		if (line.substring(0, 3).equals("AAA")) {
			if (line.contains("AAA,host")) {
				host = line.substring(9, line.length());
			}
			if (line.contains("AAA,date,")) {
				st_date = line.substring(9, line.length());
			}
			if (line.contains("AAA,time,")) {
				st_time = line.substring(9, line.length());
			}
			if (line.contains("AAA,AIX,")) {
				os = line.substring(8, line.length());
			}
			if (line.contains("AAA,interval,")) {
				sec_interval = line.substring(13, line.length());
			}
			if (line.contains("AAA,cpus,")) {
				StringTokenizer st2 = new StringTokenizer(line, ",");
				while (st2.hasMoreElements()) {
					cpu_used = st2.nextElement().toString();
				}
			}
			if (line.contains("AAA,SerialNumber,")) {
				serial = line.substring(17, line.length());
			}
			if (line.contains("AAA,MachineType,")) {
				machine_type = line.substring(line.length() - 8, line.length());
			}
			return false;
		} else {
			// @SuppressWarnings("unused")
			// String appo=line.substring(0, 11);
			if (line.contains("PCPU_ALL,T")) {
				interval = line.substring(10, 14);
				CPU_Data = line.substring(15, line.length());
				return true;
			} else {
				return false;
			}

		}
	}

	private void getCon() throws Throwable {
		cn = DriverManager.getConnection(
				"jdbc:db2://dashdb-entry-yp-dal09-10.services.dal.bluemix.net:50000/BLUDB:user=dash13540;password=Ah6tsLL@T2f@;");
		// ds = InitialContext.doLookup("jdbc/dashDB");
		// cn = ds.getConnection();
	}

	private boolean ScriviLogIFX(LogIFXRow theRowDTO) throws NamingException, SQLException {

		PreparedStatement stOut = null;

		stOut = cn.prepareStatement(
				"INSERT INTO DASH13540.TOTALCPU (LADATA, TEMPO, EVENTO, VALORE1, VALORE2) VALUES (?,?,?,?,?)");
		stOut.setString(1, theRowDTO.getLaData());
		stOut.setString(2, theRowDTO.getHhmmss());
		stOut.setString(3, theRowDTO.getTheEvent());
		stOut.setString(4, theRowDTO.getVal1());
		stOut.setInt(5, theRowDTO.getVal2());
		stOut.executeUpdate();
		stOut.close();
		cn.commit();
		return true;
	}
}