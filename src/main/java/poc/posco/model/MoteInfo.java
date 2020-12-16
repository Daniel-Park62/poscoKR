package poc.posco.model;

import java.sql.Timestamp;

import javax.persistence.*;
/**
 * Entity implementation class for Entity: MoteInfo
 *
 */
@Entity
public class MoteInfo {
   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private int sensorNo ;
	private short act;
	private int stand;
	private short measure;
	private short loc;
	private int chock;
	private double temp;   
	private Timestamp tm;
	private int seq ;

	public MoteInfo() {
	}   
	
	public long getId() {
		return this.id;
	}

	public int getSensorNo() {
		return sensorNo;
	}

	public short getAct() {
		return this.act;
	}

	public int getStand() {
		return (this.stand % 10);
	}

	public short getMeasure() {
		return measure;
	}
	public short getLoc() {
		return this.loc;
	}

	public int getChock() {
		return this.chock;
	}

	public String getChockNm() {
		return "TRI" + (loc == 1 ? "T":"B") + String.format("D%02d",chock)  ;
	}

	public double getTemp() {
		return temp;
	}
	public Timestamp getTm() {
		return this.tm;
	}

	public int getSeq() {
		return seq;
	}

	public String getDispNm() {
		return "S" + String.format("%02d", seq) ;
	}
	
	public int getStatus() {
		return (this.stand / 10) ;
	}
	@Override
	public String toString() {
		return "MoteInfo [id=" + id + ", sensorNo=" + sensorNo + ", act=" + act + ", stand=" + stand + ", loc=" + loc
				+ ", chock=" + chock + ", temp=" + temp + ", tm=" + tm + "]";
	}
   
}
