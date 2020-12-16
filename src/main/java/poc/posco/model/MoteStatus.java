package poc.posco.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity implementation class for Entity: MoteStatus
 *
 */

@Entity
public class MoteStatus implements Serializable {
	   
	@Id
	private int seq;
	private int sensorNo;
	private short act;
	private String gubun;   
	
	private String spare;   
	private String mac;
	@Column(name="descript")
	private String desc;
	private float batt;
	@Column(name="batt_dt")
    private String battDt ;
	private static final long serialVersionUID = 1L;

	public MoteStatus() {
		super();
	}

	public MoteStatus(int seq) {
		super();
		this.seq = seq ;
		this.act = 0 ;
		this.spare = "N" ;
		this.gubun = "S";
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public short getAct() {
		return this.act ;
	}

	public void setAct(short act) {
		this.act = act;
	}
	
	public String getDispNm() {
		if ( this.gubun.equals("R") )
			return "R" + String.format("%02d", this.seq - 2) ;
		else
			return "M" + String.format("%02d", this.seq) ;
	}

	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	
	public String getSpare() {
		return spare;
	}
	public void setSpare(String spare) {
		this.spare = spare;
	}
	public String getMac() {
		return this.mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}   
	public int getSensorNo() {
		return this.sensorNo;
	}

	public void setSensorNo(int sensorNo) {
		this.sensorNo = sensorNo;
	}   
	public float getBatt() {
		return this.batt > (float)3.6 ? (float)3.6 : this.batt ;
	}
	public float getBattP() {
//		return (float)(100  - (3.6 - getBatt()) / 0.001714 );
		return (float) (getBatt() * 100 / 3.6) ;
	}

	public void setBatt(float batt) {
		this.batt = batt;
	}
	public String getBattDt() {
		return battDt;
	}

	public void setBattDt(String battDt) {
		this.battDt = battDt.length() > 8 ? battDt.substring(0, 8) : battDt;
	}

	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
   
}
