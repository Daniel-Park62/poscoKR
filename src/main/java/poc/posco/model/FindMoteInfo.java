package poc.posco.model;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.ibm.icu.util.Calendar;

import poc.posco.part.PocMain;

public class FindMoteInfo {

	
	private ArrayList<MoteInfo> tempList ;
	private ArrayList<MoteHist> tempList2 ;
	private ArrayList<ChartData> chList ;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat dateFmt1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateFmt2 = new SimpleDateFormat("HH:mm:ss");

    public ArrayList<MoteInfo> getMoteInfos(String fmdt, String todt, int seq) {
    	Timestamp ts_fmdt = Timestamp.valueOf(fmdt) ;
    	Timestamp ts_todt = Timestamp.valueOf(todt) ;

    	return getMoteInfos(ts_fmdt, ts_todt, seq)  ;
    }

    public ArrayList<MoteHist> getMoteHists(String fmdt, String todt, int seq) {
    	Timestamp ts_fmdt = Timestamp.valueOf(fmdt) ;
    	Timestamp ts_todt = Timestamp.valueOf(todt) ;

    	return getMoteHists(ts_fmdt, ts_todt, seq)  ;
    }

    public ArrayList<MoteInfo> getMoteInfos(Timestamp fmdt, Timestamp todt, int seq) {
		tempList = new ArrayList<MoteInfo>();
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		String sseqif ;
		if (seq == 0) {
			sseqif = "";
		} else {
			sseqif = "and t.seq = " + seq ;
		}
		
        TypedQuery<MoteInfo> qMotes = em.createQuery("select t from MoteInfo t " 
        		+ "where t.seq < 5 and t.tm between :fmdt and :todt " + sseqif + " order by t.seq ", MoteInfo.class);

        qMotes.setParameter("fmdt", fmdt);
        qMotes.setParameter("todt", todt);
        qMotes.getResultList().stream().forEach( t -> tempList.add(t)); 
        
		em.close();

		return tempList ;

    }

    public ArrayList<MoteHist> getMoteHists(Timestamp fmdt, Timestamp todt, int seq) {
		tempList2 = new ArrayList<MoteHist>();
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		String sseqif ;
		if (seq == 0) {
			sseqif = "";
		} else {
			sseqif = "and t.seq = " + seq ;
		}
		
        TypedQuery<MoteHist> qMotes = em.createQuery("select t from MoteHist t " 
        		+ "where t.seq < 5 and t.tm between :fmdt and :todt " + sseqif + " order by t.tm desc, t.seq ", MoteHist.class);

        qMotes.setParameter("fmdt", fmdt);
        qMotes.setParameter("todt", todt);
        qMotes.getResultList().stream().forEach( t -> tempList2.add(t));

		em.close();

		return tempList2 ;

    }

    public ArrayList<ChartData> getChartData(String fmdt, String todt, int seq) {

		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		String sseqif = " and a.tm between '" + fmdt + "' and '" + todt + "'";
		if (seq != 0) {
			sseqif += " and a.seq = " + seq ;
		}
		
		String sql = " select a.tm, a.seq, ifnull(a.temp,0) temp from motehist a " + 
				"where a.tm between '" + fmdt + "' and '" + todt + "' " + sseqif + " order by a.tm "; 
		Query nativeQuery = em.createNativeQuery(sql);
	    List<Object[]> resultList = nativeQuery.getResultList();
	    chList = resultList.stream().map(r -> new ChartData( 
	    		Timestamp.valueOf(r[0].toString()) , ((Long)r[1]).intValue(),  (Double)r[2] ))
	    		.collect(Collectors.toCollection(ArrayList::new)) ;
        
		em.close();

		return chList ;

    }
    public ArrayList<ChartData> getChartData2(String fmdt, String todt, int seq) {
		tempList2 = new ArrayList<MoteHist>();
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		String sseqif = " and a.tm between '" + fmdt + "' and '" + todt + "'";
		if (seq != 0) {
			sseqif += " and a.seq = " + seq ;
		}
		
		String sql = " select a.tm, a.seq, ifnull(b.temp,0) temp from " + 
				"(select  date_add( '" + fmdt + "' , interval x.seq second) as tm " +
				",y.seq from seq_0_to_3599 x, " + 
				"(select seq from seq_1_to_4 ) y ) a " + 
				"left join motehist b on a.tm = b.tm and a.seq = b.seq" + sseqif + " order by a.tm "; 
		Query nativeQuery = em.createNativeQuery(sql);
	    List<Object[]> resultList = nativeQuery.getResultList();
	    chList = resultList.stream().map(r -> new ChartData( 
	    		Timestamp.valueOf(r[0].toString()) , ((BigInteger)r[1]).intValue(),  (Double)r[2] ))
	    		.collect(Collectors.toCollection(ArrayList::new)) ;
        
		em.close();

		return chList ;

    }
    
    public ArrayList<MoteInfo> getMoteInfos(int seq, int cnt) {
    	Timestamp todt, fmdt ;
		EntityManager em = PocMain.emf.createEntityManager();
		todt = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;
		em.close();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.SECOND, -cnt); 
		
		fmdt = new Timestamp(cal.getTime().getTime());
		
    	return getMoteInfos(fmdt, todt, seq)  ;
		
    }
    public ArrayList<Timestamp> getTmlist() {
    	ArrayList<Timestamp> tmList = new ArrayList<Timestamp>() ; 
		EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
        TypedQuery<Timestamp> qTm = em.createQuery("select t from " 
        		+ "( select tm from moteinfo group by tm ) t order by tm desc LIMIT 20 ", Timestamp.class);
		qTm.getResultList().stream().forEach( tm -> tmList.add(tm));
		em.close();
    	
    	return tmList ;
    }
    
    public Timestamp getLasTime() {
    	Timestamp lstime ;
		EntityManager em = PocMain.emf.createEntityManager();
		lstime = em.createQuery("select t.lastm from LasTime t  ", Timestamp.class).getSingleResult() ;
		em.close();
    	return lstime  ;
    }
}