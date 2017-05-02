package sda.nomn;

public class Lanciatore {
	 
	public static void main(String[] args) {
		new NmonCPUParser().start();
		new NmonDSKSpaceParser().start();
		new NmonIOParser().start();
	}

}
