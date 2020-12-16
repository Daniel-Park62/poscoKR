package poc.posco.part;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import poc.posco.model.MoteConfig;
import poc.posco.model.MoteInfo;
import poc.posco.model.MoteStatus;
import org.eclipse.wb.swt.SWTResourceManager;

@SuppressWarnings("unchecked")
public class DashBoard {

	private static class ContentProvider_1 implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object input) {
			return ((ArrayList<MoteStatus>)input).toArray();
		}
		@Override
		public void dispose() {
		}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}
	private static class ContentProvider implements IStructuredContentProvider {
		@Override
		public Object[] getElements(Object input) {
			//return new Object[0];
			return ((ArrayList<MoteInfo>)input).toArray();
		}
		@Override
		public void dispose() {
		}
		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

//	List<MoteInfo> selectedTagList = new ArrayList<MoteInfo>();
	Point selectedPoint = new Point(0, 0);
	EntityManager em = PocMain.emf.createEntityManager();
	HashMap<Integer, Integer> tagCount = new HashMap<Integer, Integer>();
	Bundle bundle = FrameworkUtil.getBundle(this.getClass());
    // use the org.eclipse.core.runtime.Path as import
    
//    URL url8 = FileLocator.find(bundle, new Path("images/slice_page1.png"), null);
//    ImageDescriptor slice_page1 = ImageDescriptor.createFromURL(url8);
	Image slice_page1 = new Image(Display.getCurrent(), "images/slice_page1.png");
	
    Label lblApActive;
    Label lblApInactive;
    Label lblTagActive;
    Label lblTagInactive;
    Label lblAlertActive;
    Label lblAlertInactive;
    Label lblDate, lblTime;
    Label lblinterval;
    
    private Table table;
    private TableViewer tableViewer;
    private TableViewer tableViewer_1;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    DateFormat dateFmt2 = new SimpleDateFormat("HH:mm:ss");
    Font font1 = new Font(null,"Microsoft Sans Serif", 22 , SWT.NORMAL ) ;
    Font font2 = new Font(null,"Calibri", 14, SWT.NORMAL);
    Font font3 = new Font(null,"Calibri", 13, SWT.NORMAL ) ;
    Thread uiUpdateThread ;
    
	public DashBoard(Composite parent, int style) {

//		super(parent, style) ;
		
		GridLayout gl_parent = new GridLayout(1, false);
		gl_parent.marginTop = 10;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		gl_parent.verticalSpacing = 0;
		parent.setLayout(gl_parent);
	
		Composite composite = new Composite(parent, SWT.NONE);
		
		composite.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
//				uiUpdateThread.interrupt();
			}
		});
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.horizontalSpacing = 0;
		gl_composite.verticalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		Composite composite_15 = new Composite(composite, SWT.NONE);
		GridData gd_composite_15 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_15.heightHint = 420;
		composite_15.setLayoutData(gd_composite_15);
		composite_15.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		composite_15.setBackgroundMode(SWT.INHERIT_DEFAULT);

		
		lblApActive = new Label(composite_15, SWT.NONE);
		lblApActive.setAlignment(SWT.RIGHT);
		lblApActive.setFont(font1);
		lblApActive.setBounds(295, 170, 40, 30);
		lblApActive.setText("99");
		
		lblApInactive = new Label(composite_15, SWT.NONE);
		lblApInactive.setAlignment(SWT.RIGHT);
		lblApInactive.setFont(font1);
		lblApInactive.setBounds(295, 205, 40, 30);
		lblApInactive.setText("99");
		
		lblTagActive = new Label(composite_15, SWT.NONE);
		lblTagActive.setAlignment(SWT.RIGHT);
		lblTagActive.setFont(font1);
		lblTagActive.setBounds(665, 170, 40, 30);
		lblTagActive.setText("99");
		
		lblTagInactive = new Label(composite_15, SWT.NONE);
		lblTagInactive.setAlignment(SWT.RIGHT);
		lblTagInactive.setFont(font1);
		lblTagInactive.setBounds(665, 205, 40, 30);
		lblTagInactive.setText(" 0");
		
		lblAlertActive = new Label(composite_15, SWT.NONE);
		lblAlertActive.setAlignment(SWT.RIGHT);
		lblAlertActive.setFont(font1);
		lblAlertActive.setBounds(1045, 170, 40, 30);
		lblAlertActive.setText(" 0");
		lblAlertActive.setBackground(SWTResourceManager.getColor( 255,240,240));
		
		lblAlertInactive = new Label(composite_15, SWT.NONE);
		lblAlertInactive.setAlignment(SWT.RIGHT);
		lblAlertInactive.setFont(font1);
		lblAlertInactive.setBounds(1045, 205, 40, 30);
		lblAlertInactive.setText(" 0");
		lblAlertInactive.setBackground(SWTResourceManager.getColor( 255,240,240));
		
		Label lblNewLabel_4 = new Label(composite_15, SWT.NONE);
		lblNewLabel_4.setBounds(0, 0, 1450, 480);
		lblNewLabel_4.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT)); 
		lblNewLabel_4.setBackgroundImage(slice_page1);
		
		lblApActive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblApInactive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblTagActive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblTagInactive.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		Label lbldate_1 = new Label(composite_15, SWT.SHADOW_IN);
		lbldate_1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lbldate_1.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		lbldate_1.setFont(font2);
		lbldate_1.setBounds(1020, 65, 50, 30);
		lbldate_1.setText("Date ");
		lbldate_1.pack();


		lblDate = new Label(composite_15, SWT.NONE);
		lblDate.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblDate.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		lblDate.setFont(font2);
		lblDate.setBounds(1070, 65, 155, 30);
		lblDate.setText("2019-02-02");

		lblinterval = new Label(composite_15, SWT.NONE);
		lblinterval.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblinterval.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		lblinterval.setFont(font2);
		lblinterval.setBounds(1020, 90, 220, 30);
		lblinterval.setText("Time Interval 10 sec ");

		lbldate_1.moveAbove(lblNewLabel_4);
		lblinterval.moveAbove(lblNewLabel_4);
		lblDate.moveAbove(lblNewLabel_4);

		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.marginRight = 10;
		gl_composite_3.marginLeft = 10;
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_3.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		
		SashForm  sash1 = new SashForm(composite_3, SWT.HORIZONTAL);
//		sash1.setLayout(gl_composite_3);
		sash1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		sash1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		
		tableViewer_1 = new TableViewer(sash1, SWT.BORDER | SWT.FULL_SELECTION);
		
		table_1 = tableViewer_1.getTable();
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);
		table_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table_1.setFont(font3);
		table_1.setHeaderBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		tableViewer_1.setUseHashlookup(true);		
		TableViewerColumn tvc_seq = new TableViewerColumn(tableViewer_1, SWT.CENTER);
		tvc_seq.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return element == null ? "" :((MoteStatus)element).getSeq() +""  ;
			}
		});
		TableColumn tc_seq = tvc_seq.getColumn();
		tc_seq.setWidth(50);
		tc_seq.setText("SEQ");
		tc_seq.setAlignment(SWT.CENTER);

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		tableViewerColumn_6.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return element == null ? "" :((MoteStatus)element).getDispNm()   ;
			}
		});
		TableColumn tblclmnNewColumn_6 = tableViewerColumn_6.getColumn();
		tblclmnNewColumn_6.setAlignment(SWT.CENTER);
		tblclmnNewColumn_6.setWidth(80);
		tblclmnNewColumn_6.setText("Mote ID");
		
		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		tableViewerColumn_7.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				// TODO Auto-generated method stub
				return element == null ? "" :((MoteStatus)element).getDesc() +""  ;
			}
		});
		TableColumn tblclmnNewColumn_7 = tableViewerColumn_7.getColumn();
		tblclmnNewColumn_7.setAlignment(SWT.LEFT);
		tblclmnNewColumn_7.setWidth(200);
		tblclmnNewColumn_7.setText("Description");
		
		TableViewerColumn tableViewerColumn_8 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		tableViewerColumn_8.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				String tmp = "";
				switch (((MoteStatus)element).getAct()) {
				case 2:
					tmp = "Active";
					break;

				case 1:
					tmp = "Waiting";
					break;

				default:
					tmp = "Inactive";
					break;
				}
				return element == null ? "" :tmp  ;
			}
		});
		TableColumn tblclmnNewColumn_8 = tableViewerColumn_8.getColumn();
		tblclmnNewColumn_8.setAlignment(SWT.CENTER);
		tblclmnNewColumn_8.setWidth(100);
		tblclmnNewColumn_8.setText("Active");
		
		TableViewerColumn tableViewerColumn_9 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		tableViewerColumn_9.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				double dbatt = ((MoteStatus)element).getBatt() ;
				return element == null ? "" : (dbatt == 0) ? "" : String.format("%1.3f", dbatt)  ;
			}
		});
		TableColumn tblclmnNewColumn_9 = tableViewerColumn_9.getColumn();
		tblclmnNewColumn_9.setAlignment(SWT.CENTER);
		tblclmnNewColumn_9.setWidth(100);
		tblclmnNewColumn_9.setText("Battery(V)");
		
		TableViewerColumn tvc_battdt = new TableViewerColumn(tableViewer_1, SWT.NONE);
		tvc_battdt.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				String lsBattDt = ((MoteStatus)element).getBattDt();
				return lsBattDt.length() < 8 ? "" : lsBattDt.substring(0, 4) + "-" + lsBattDt.substring(4, 6) + "-" + lsBattDt.substring(6) ;
			}
		});
		
		TableColumn tcl_battdt = tvc_battdt.getColumn();
		tcl_battdt.setAlignment(SWT.CENTER);
		tcl_battdt.setWidth(140);
		tcl_battdt.setText("Replacement Date");
		
		TableViewerColumn tvc_day = new TableViewerColumn(tableViewer_1, SWT.NONE);
		tvc_day.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				String lval = "";
			    DateFormat dateFmt1 = new SimpleDateFormat("yyyyMMdd");
				String battdt = ((MoteStatus)element).getBattDt() ;
				if ( battdt.length() == 8 )
					try {
						Date startd = dateFmt1.parse( battdt );
						Date currentTime = new Date ();
						long lday = ( currentTime.getTime() - startd.getTime() ) / (24 * 60 * 60 * 1000);
						lval = String.valueOf(lday) ;
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				return lval ;
			}
		});

		TableColumn tcl_day = tvc_day.getColumn() ;
		tcl_day.setText("∞Ê∞˙¿œºˆ");
		tcl_day.setAlignment(SWT.CENTER);
		tcl_day.setWidth(90);
		
		TableViewerColumn tableViewerColumn_10 = new TableViewerColumn(tableViewer_1, SWT.NONE);
		tableViewerColumn_10.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return element == null || ((MoteStatus)element).getBattP() <= 30.0 && ((MoteStatus)element).getAct() > 0 ? "L/B" : ""  ;
			}
		});
		TableColumn tblclmnNewColumn_10 = tableViewerColumn_10.getColumn();
		tblclmnNewColumn_10.setAlignment(SWT.CENTER);
		tblclmnNewColumn_10.setWidth(60);
		tblclmnNewColumn_10.setText("Alert");
		table_1.redraw();
//		Composite composite_1 = new Composite(composite_3, SWT.V_SCROLL);
//		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//		composite_1.setSize(438, 0);
//		composite_1.setLayout(new GridLayout(1, false));
//		composite_1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		
		tableViewer = new TableViewer(sash1, SWT.BORDER | SWT.FULL_SELECTION );
		
		table = tableViewer.getTable();
		table.setHeaderBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(font3);
		tableViewer.setUseHashlookup(true);
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				// TODO Auto-generated method stub
				//return element == null ? "" : element.toString();
				return element == null ? "" : dateFormat.format( new Date( ((MoteInfo)element).getTm().getTime() ) ) ;
			}
		});
		TableColumn tblclmnTime = tableViewerColumn.getColumn();
		tblclmnTime.setAlignment(SWT.CENTER);
		tblclmnTime.setWidth(170);
		tblclmnTime.setText("Time");

		TableViewerColumn tvcinfo_seq = new TableViewerColumn(tableViewer, SWT.NONE);
		tvcinfo_seq.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return element == null ? "" : ((MoteInfo)element).getSeq()+""  ;
			}
		});
		TableColumn tcinfo_seq = tvcinfo_seq.getColumn();
		tcinfo_seq.setAlignment(SWT.CENTER);
		tcinfo_seq.setWidth(50);
		tcinfo_seq.setText("SEQ");

		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_1.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return element == null ? "" : ((MoteInfo)element).getDispNm()  ;
			}
		});
		TableColumn tc1 = tableViewerColumn_1.getColumn();
		tc1.setAlignment(SWT.CENTER);
		tc1.setWidth(80);
		tc1.setText("Sensor ID");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		tableViewerColumn_4.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
				return element == null ? "" : String.format("%2.2f", ((MoteInfo)element).getTemp() ) ;
			}
		});
		TableColumn tblclmnNewColumn_3 = tableViewerColumn_4.getColumn();
		tblclmnNewColumn_3.setAlignment(SWT.CENTER);
		tblclmnNewColumn_3.setWidth(100);
		tblclmnNewColumn_3.setText("Data('C)");

		TableViewerColumn tvloc = new TableViewerColumn(tableViewer, SWT.NONE);
		tvloc.setLabelProvider(new ColumnLabelProvider() {
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
			public String getText(Object element) {
//				return ((MoteInfo)element).getLoc() == 1 ? "T" : "B"   ;
				switch (((MoteInfo)element).getSeq()) {
				case 1:
					return "«œ";
				case 2:
					return "¡ﬂ";
				case 3:
					return "ªÛ";
				default:
					return "√÷ªÛ";
				}
				
			}
		});
		TableColumn tcloc = tvloc.getColumn();
		tcloc.setAlignment(SWT.CENTER);
		tcloc.setWidth(110);
		tcloc.setText("Location");

		
		tableViewer.setContentProvider(new ContentProvider());
		tableViewer_1.setContentProvider(new ContentProvider_1());

		sash1.setWeights(new int[] {60,40});
		tc_seq.setAlignment(SWT.CENTER);
		tblclmnTime.setAlignment(SWT.CENTER);
		
		uiUpdateThread = new MyThread(Display.getCurrent(), 10000) ;
		uiUpdateThread.setDaemon(true);
		uiUpdateThread.start();
	}

	private class MyThread extends Thread {
		private Display display = null;
		private int interval ;
		MyThread(Display display, int interval){
			this.display = display ;
			this.interval = interval ;
		}
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted() && !lblinterval.isDisposed()) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						refreshSensorList();
					}
				});
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
//					e.printStackTrace();
				}

			}
		}

	}

	int activeCnt = 0;
	int inactiveCnt = 0;

	int activeSsCnt = 0;
	int failCnt = 0;
	int moteLBCnt = 0;
	int tagLBCnt = 0;
	private Table table_1;
	private Timestamp time_s = Timestamp.valueOf("1900-01-01 00:00:00") ;
	private Timestamp time_c = Timestamp.valueOf("1900-01-01 00:00:00") ;

//	@SuppressWarnings("unchecked")
	public void refreshSensorList() {
	    EntityManager em = PocMain.emf.createEntityManager();
		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
		
		PocMain.MOTECNF = em.createQuery("select m from MoteConfig m ", MoteConfig.class).getSingleResult() ;
		ArrayList<MoteInfo> tempList = new ArrayList<MoteInfo>();
		ArrayList<MoteStatus> apList = new ArrayList<MoteStatus>();
        
        TypedQuery<MoteStatus> q2 = 
        		 em.createQuery("select t from MoteStatus t where t.spare = 'N' and t.seq < 15 order by t.seq", MoteStatus.class);
        q2.getResultList().stream().forEach(t -> apList.add(t) );

		activeCnt = 0;
		inactiveCnt = 0;
		activeSsCnt = 0;
		failCnt = 0;
		moteLBCnt = 0;
		tagLBCnt = 0;
		for (MoteStatus moteStatus : apList) {
			if(moteStatus.getAct() == 2) {
				activeCnt++;
			}else {
				inactiveCnt++;
			}
			if (moteStatus.getBatt() <= (3.6 * 0.3) && moteStatus.getAct() > 0 ) moteLBCnt++ ;
		}

/*		
        tagCount.clear();

		for (MoteStatus moteStatus : apList) {
			int count = 0;
			for (MoteInfo temp1 : tempList) {
				if(moteStatus.getSensorNo() == temp1.getSensorNo() ) {
					count++;
				}
			}
			if(count > 0) {
				tagCount.put(moteStatus.getSensorNo(), count);
			}
		}
*/
//		Timestamp time_c = em.createQuery("select max(t.tm) from MoteInfo t ", Timestamp.class).getSingleResult() ;
		time_c = em.createQuery("select t.lastm from LasTime t ", Timestamp.class).getSingleResult() ;

/*		
        TypedQuery<MoteInfo> qMaxTime = em.createQuery("select t from MoteInfo t order by t.time desc ", MoteInfo.class);
        qMaxTime.setFirstResult(0);
        qMaxTime.setMaxResults(1);
        final MoteInfo tag = (MoteInfo) qMaxTime.getSingleResult();
*/        

        TypedQuery<MoteInfo> qMotes = em.createQuery("select t from MoteInfo t "
        		+ "where t.tm = :time and t.seq < 5 order by t.seq  "
        		, MoteInfo.class);

        qMotes.setParameter("time", time_c).getResultList().stream().forEach( t -> tempList.add(t));
        activeSsCnt = (int)tempList.stream().filter(a -> a.getTemp() != 0).count() ;
        failCnt = 4 - activeSsCnt ;
//		for (MoteInfo moteInfo : tempList) {
//			switch (moteInfo.getAct()) {
//			case 0:
//				failCnt++;
//				break ;
//			default:
//				activeSsCnt++;
//				break ;
//			}
//		}

		em.close();


//		if(!canvas.isDisposed()) {
			
				//String apStatus = "»∞ÔøΩÔøΩ : " + activeCnt + " | ÔøΩÔøΩ»∞ÔøΩÔøΩ : " + inactiveCnt;
				//String tagStatus = "»∞ÔøΩÔøΩ : " + activeTagCnt + " | SOS : " + sosTagCnt;
				lblinterval.setText("Time Interval  "+ PocMain.MOTECNF.getMeasure() + " sec ");
				lblDate.setText(dateFormat.format(time_c ) );
				lblDate.pack();
				lblApActive.setText(activeCnt+"");
				lblApInactive.setText(inactiveCnt+"");

				lblTagActive.setText(activeSsCnt+"");
				lblTagInactive.setText(failCnt+"");
				lblAlertActive.setText(moteLBCnt+"");
				tableViewer_1.setInput(apList);
				tableViewer_1.refresh();
				tableViewer.setInput(tempList);
				tableViewer.refresh();
			
//			sync.syncExec(()->{
//				canvas.redraw();
////				pushSession.stop();
//			});

//		}

	}
	
}