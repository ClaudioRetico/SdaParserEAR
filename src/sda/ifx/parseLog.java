package sda.ifx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.naming.NamingException;
import javax.sql.DataSource;

public class parseLog {
	DataSource ds = null;
	Connection cn = null;
	LogIFXRow ifxRow;

	public String parseIFXLog() throws Throwable {

		Statement st = null;

		try {
			getCon();
			st = cn.createStatement();

			@SuppressWarnings("unused")
			boolean tr = st.execute("TRUNCATE TABLE DASH13540.LOGINFORMIX IMMEDIATE");
			cn.commit();

			ResultSet inp = st.executeQuery("SELECT RIGALOGINFORMIX  FROM DASH13540.LOGINFORMIXRAW");

			String riga = null;
			int cnt = 0;
			int wrt = 0;
			boolean rigabuona = false;

			while (inp.next()) {
				cnt = cnt + 1;
				riga = inp.getString("RIGALOGINFORMIX");
				if (riga != null) {
					rigabuona = riga.contains("/");
				} else {
					rigabuona = false;
				}
				if (rigabuona) {
					ifxRow = new LogIFXRow();
					if ( riga.substring(19, riga.length()).contains("Checkpoint Completed")) { // Parse checkpoint duration
						ifxRow = parseCheckPoint(riga);
					}
					
					if ( riga.substring(19, riga.length()).contains("Maximum server")) { // Parse Server connections
						ifxRow = maxSrvCon(riga);
					}
					
					if ( riga.substring(19, riga.length()).contains("Checkpoint Statistics")) { // Parse checkpoint duration statistics
						ifxRow = chkStat(riga);
					}
					
					if (ifxRow.isFl_wrt()) {
						ScriviLogIFX(ifxRow);
						wrt = wrt + 1;
					}
				}
				System.out.println("Righe lette:" + cnt + " - righe scritte:" + wrt);
			}
			cn.close();
			return "Il conteggio è:" + cnt;
		} catch (Exception e) {
			e.printStackTrace();
			return "ERRORE" + e.toString();
		}
	}

	private String prendiData(String theRiga) {
		String laData = theRiga.substring(0, 8);
		String laNuovaData = laData.substring(6, 8) + "/" + laData.substring(0, 2) + "/" + laData.substring(3, 5);
		return laNuovaData;

	}

	private boolean ScriviLogIFX(LogIFXRow theRowDTO) throws NamingException, SQLException {

		PreparedStatement stOut = null;

		stOut = cn.prepareStatement(
  	"INSERT INTO DASH13540.LOGINFORMIX (LADATA, TEMPO, EVENTO, VALORE1, VALORE2) VALUES (?,?,?,?,?)");
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

	private LogIFXRow parseCheckPoint(String theRiga) {
		LogIFXRow ifxRow = new LogIFXRow();
		ifxRow.setLaData(prendiData(theRiga));
		ifxRow.setHhmmss(theRiga.substring(9, 18));
		ifxRow.setTheEvent("CheckPoint");
		ifxRow.setVal1(null);
		ifxRow.setFl_wrt(false);
		StringTokenizer tt = null;
		tt = new StringTokenizer(theRiga.substring(19, theRiga.length()));
		boolean exitloop = false;
		while (tt.hasMoreTokens() && !exitloop) {
			String stapp = tt.nextToken();
			if (stapp.equals("was")) {
				ifxRow.setVal2(Integer.parseInt(tt.nextToken()));
				ifxRow.setFl_wrt(true);
				exitloop = true;
			}
		}
		return ifxRow;
	}
	
	private LogIFXRow maxSrvCon(String theRiga) {
		LogIFXRow ifxRow = new LogIFXRow();
		ifxRow.setLaData(prendiData(theRiga));
		ifxRow.setHhmmss(theRiga.substring(9, 18));
		ifxRow.setTheEvent("Server_Connections");
		ifxRow.setVal1(null);
		ifxRow.setFl_wrt(false);
		StringTokenizer tt = null;
		tt = new StringTokenizer(theRiga.substring(19, theRiga.length()));
		boolean exitloop = false;
		while (tt.hasMoreTokens() && !exitloop) {
			String stapp = tt.nextToken();
			if (stapp.equals("connections")) {
				ifxRow.setVal2(Integer.parseInt(tt.nextToken()));
				ifxRow.setFl_wrt(true);
				exitloop = true;
			}
		}
		return ifxRow;
	}
	
	private LogIFXRow chkStat(String theRiga) {
		LogIFXRow ifxRow = new LogIFXRow();
		ifxRow.setLaData(prendiData(theRiga));
		ifxRow.setHhmmss(theRiga.substring(9, 18));
		ifxRow.setTheEvent("CHK_Stat_Time_&_Block");
		ifxRow.setFl_wrt(false);
		StringTokenizer tt = null;
		tt = new StringTokenizer(theRiga.substring(19, theRiga.length()));
		boolean exitloop = false;
		while (tt.hasMoreTokens() && !exitloop) {
			String stapp = tt.nextToken();
			if (stapp.equals("Time")) {
				String tm=tt.nextToken();
				ifxRow.setVal1(tm.substring(0, tm.length()-1));
			}
			if (stapp.equals("blocked")) {
				String blk=tt.nextToken();
				ifxRow.setVal2(Integer.parseInt(blk.substring(0, blk.length()-1)));
				ifxRow.setFl_wrt(true);
				exitloop = true;
			}
		}
		return ifxRow;
	}

	private void getCon() throws Throwable {
		cn = DriverManager.getConnection("jdbc:db2://dashdb-entry-yp-dal09-10.services.dal.bluemix.net:50000/BLUDB:user=dash13540;password=Ah6tsLL@T2f@;");
//		ds = InitialContext.doLookup("jdbc/dashDB");
//		cn = ds.getConnection();
	}
}
