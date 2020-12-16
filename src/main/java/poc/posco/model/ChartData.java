package poc.posco.model;

import java.sql.Timestamp;

public class ChartData {
	private Timestamp tm;
	private int seq ;
	private double temp;
	
	public ChartData(Timestamp tm, int seq, double temp) {
		super();
		this.tm = tm;
		this.seq = seq;
		this.temp = temp;
	}
	
	public Timestamp getTm() {
		return tm;
	}
	public void setTm(Timestamp tm) {
		this.tm = tm;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}   

}
