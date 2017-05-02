package sda.nomn;

public class Lanciatore {
	 
	public void main(String[] args) {
		new NmonCPUParser().start();
		new NmonDSKSpaceParser().start();
		new NmonIOParser().start();
	}

}
