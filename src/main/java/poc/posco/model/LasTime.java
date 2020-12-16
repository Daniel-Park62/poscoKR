package poc.posco.model;

import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: LasTime
 *
 */
@Entity
@NamedQuery(name="LasTime.findAll", query="SELECT l FROM LasTime l")
public class LasTime  {

	private static LasTime instance ;
	public static LasTime getInstance() {
		if ( instance == null )
			instance = new LasTime();
		return instance;
	}
	@Id
	private int id;
	private Timestamp lastm;

	private LasTime() {	}   
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public Timestamp getLastm() {
		return this.lastm;
	}

	public void setLastm(Timestamp lastm) {
		this.lastm = lastm;
	}
   
}
