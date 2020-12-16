package poc.posco.part;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.swtchart.ISeriesLabel;
import org.eclipse.wb.swt.SWTResourceManager;

import poc.posco.model.FindMoteInfo;
import poc.posco.model.LasTime;
import poc.posco.model.MoteInfo;

public class RealChart {
	private Chart chart ;
	
	private static final double[] yS1 = { 30,31,32,33,34,35,36,37,38,37,35,34,33,32,31,30,29,27};
	private static final double[] yS2 = { 0.3, 0.4, 0.6, 0.8, 1.5, 1.8, 0.77, 0.55, 0.0, -0.30, -0.77, -0.99,
			-1.2, -0.95, -0.74, -0.30 , 1.0};
	
	private ILineSeries lineSeries1; 
	private ILineSeries lineSeries2; 
	private ILineSeries lineSeries3; 
	private ILineSeries lineSeries4;
	private int axisId ;
	final Color BLACK = SWTResourceManager.getColor(SWT.COLOR_BLACK);
	final Color RED = SWTResourceManager.getColor(SWT.COLOR_RED);
	final Color BLUE = SWTResourceManager.getColor(SWT.COLOR_BLUE);
	final Color GREEN = SWTResourceManager.getColor(SWT.COLOR_GREEN);
	final Color CYAN = SWTResourceManager.getColor(SWT.COLOR_DARK_CYAN);
	/**
     * Create the composite.
     *
     * @param parent
     * @param style
     * @wbp.parser.entryPoint
     */
	int px ;
	double py ;
	int sel = 0;
	Label lblDate, lblTime,  lblfrom ,lblfromd, lblto, lbltod ;
	public RealChart(Composite parent, int style) {
		this(parent, style, 0 ) ;
	}
    public RealChart(Composite parent, int style , int sno) {

    	sel = sno ;
	    final Font font2 = SWTResourceManager.getFont("Calibri", 16, SWT.NORMAL);
	    final Image slice_page = SWTResourceManager.getImage("images/slice_page6.png");
	    
	    Color COLOR_T = SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT) ;
    	
    	SashForm sash1 = new SashForm(parent, SWT.VERTICAL);
    	Composite comps1 = new Composite(sash1, SWT.NONE);
    	sash1.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				uiUpdateThread.stop();
			}
		});

    	comps1.setBackground(parent.getBackground());
		GridLayout gl_in = new GridLayout(2,true);
		gl_in.marginRight = 50;
		gl_in.marginLeft = 65;
		
//		comps1.setLayout(gl_in);
		GridData gd_in = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_in.heightHint = 30;
		gd_in.minimumHeight = 30;

		comps1.setLayoutData(gd_in);
		comps1.setBackgroundImage(slice_page);
		
		lblDate = new Label(comps1, SWT.NONE);
		
		lblDate.setBounds(340, 60, 160, 30);
		lblDate.setFont(SWTResourceManager.getFont( "Microsoft Sans Serif", 16, SWT.NORMAL));
		lblDate.setText("New Label");
		lblDate.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lblDate.setBackground(SWTResourceManager.getColor( 141,153,208));

		lblTime = new Label(comps1, SWT.NONE);
		lblTime.setBounds(340, 90 , 160, 30);
		lblTime.setFont(SWTResourceManager.getFont( "Microsoft Sans Serif", 22, SWT.NORMAL));
		lblTime.setText("New Label");
		lblTime.setBackground(SWTResourceManager.getColor( 141,153,208));
		lblTime.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		{
			Label lbl = new Label(comps1, GridData.VERTICAL_ALIGN_CENTER) ;
			lbl.setText(" * ID Select ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.setBounds(650, 120, 150, 50);
			lbl.pack();
		}
		Combo cbddown = new Combo(comps1, SWT.DROP_DOWN | SWT.BORDER);
		cbddown.setFont(font2);
		cbddown.setItems(new String[] {" ALL ", " S01 "," S02 "," S03 "," S04 "});
		cbddown.select(sno);
		cbddown.pack();
		cbddown.setBounds(760, 120, 80,50 );
		cbddown.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				sel = cbddown.getSelectionIndex();
				try {
					setYdata();
					reLocate();
				} catch (Exception e) {
					// TODO: handle exception
				}
				chart.setFocus();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		final Button bstart = new Button(comps1, SWT.ARROW | SWT.RIGHT);
		final Button bpause = new Button(comps1, SWT.PUSH | SWT.CENTER);
		bpause.setFont(font2);
		bpause.setText(" PAUSE ");
		bpause.setBounds(900, 115, 80, 40);
		bpause.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (uiUpdateThread == null) return ;
				uiUpdateThread.stop();
				uiUpdateThread = null;
				bstart.setEnabled(true);
				bpause.setEnabled(false);
			}
		}); 
		bpause.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false, 1, 1));
		
		
		bstart.setFont(font2);
		bstart.setToolTipText("Play");
		bstart.setBounds(1000, 115, 80, 40);
		bstart.setEnabled(false);
		bstart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ( uiUpdateThread == null) {
					uiUpdateThread = new MyThread(Display.getCurrent(), 10000) ;
					uiUpdateThread.start();
					bstart.setEnabled(false);
					bpause.setEnabled(true);
				}
			}
		}); 
		bstart.setLayoutData(new GridData(SWT.FILL, GridData.CENTER, true, false, 1, 1));
    	
		chart = new Chart(sash1, SWT.NONE);
		chart.setBackground(parent.getBackground());
		chart.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_CROSS));
		Composite compb = new Composite(sash1, SWT.NONE);

		compb.setBackground(parent.getBackground());
		compb.setLayout(new GridLayout(5, true)) ;
		lblfrom = new Label(compb,SWT.FILL );
		lblfromd = new Label(compb,SWT.BORDER);
		lblto = new Label(compb,SWT.NONE);
		lbltod = new Label(compb,SWT.BORDER);
		lblfrom.setLayoutData(new GridData(SWT.RIGHT,SWT.CENTER, true, true, 1, 1));
		lblfromd.setLayoutData(new GridData(SWT.FILL,SWT.CENTER, true, true, 1, 1));
		lblto.setLayoutData(new GridData(SWT.RIGHT,GridData.CENTER, true, true, 1, 1));
		lbltod.setLayoutData(new GridData(SWT.FILL,GridData.CENTER, true, true, 1, 1));
		lblfrom.setFont(font2) ;
		lblto.setFont(font2);
		lblfromd.setFont(font2) ;
		lbltod.setFont(font2);
		lblfrom.setText(" 조회기간  *From Date/Time ");
		lblto.setText(" *To Date/Time ");
		compb.pack();
		lblfrom.setBackground(parent.getBackground());
		lblto.setBackground(parent.getBackground());
		lblfrom.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		lblto.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		
		chart.setLayoutData(new FillLayout());
		// set titles
		chart.getTitle().setText("Temporary Chart");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Time(Sec)");
		chart.getAxisSet().getYAxis(0).getTitle().setText("");
		// create second Y axis
//		axisId = chart.getAxisSet().createYAxis();
		axisId = 0 ;
		
		// set the properties of second Y axis
//		IAxis yAxis2 = chart.getAxisSet().getYAxis(axisId);
//		yAxis2.setPosition(Position.Secondary);

		// create line series
		lineSeries1 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S01");
		lineSeries2 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S02");
		lineSeries3 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S03");
		lineSeries4 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S04");

		lineSeries1.setYSeries(yS1);
		lineSeries1.setLineColor(BLUE);
//
		lineSeries2.setYSeries(yS1);
		lineSeries2.setLineColor(RED);
		// assign series to second Y axis
		lineSeries2.setYAxisId(axisId);
		
		lineSeries3.setYSeries(yS1);
		lineSeries3.setLineColor(CYAN);
		lineSeries3.setYAxisId(axisId);
		lineSeries4.setYSeries(yS1);
		lineSeries4.setLineColor(GREEN);
		lineSeries4.setYAxisId(axisId);
		chart.getLegend().setFont(SWTResourceManager.getFont( "Calibri", 14, SWT.NORMAL));
		// adjust the axis range
//		chart.getAxisSet().adjustRange();
		sash1.setWeights(new int[] {23,90,5});
		
		uiUpdateThread = new MyThread(Display.getCurrent(), 10000) ;
		uiUpdateThread.start();
		chart.setFocus();
		final IAxis xAxis = chart.getAxisSet().getXAxis(0);
		final IAxis yAxis = chart.getAxisSet().getYAxis(0);
		xAxis.getTick().setForeground(BLACK);
		xAxis.getTick().setFont(SWTResourceManager.getFont("Calibri", 12, SWT.BOLD));
		xAxis.getTitle().setForeground(BLACK);

		yAxis.getTick().setForeground(BLACK);
		yAxis.getTitle().setForeground(BLACK);
		yAxis.getTitle().setText("Temp.");
		yAxis.getTick().setFont(SWTResourceManager.getFont("Calibri", 12, SWT.BOLD));
		Composite plot = (Composite)chart.getPlotArea() ;
		plot.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) {
				// TODO Auto-generated method stub
				chart.setRedraw(false);
				if (arg0.button == 1) { 
					chart.getAxisSet().getXAxis(0).zoomIn(px);
					chart.getAxisSet().getYAxis(0).zoomIn(py);
				};
				if (arg0.button == 3) { 
					chart.getAxisSet().getXAxis(0).zoomOut(px);
					chart.getAxisSet().getYAxis(0).zoomOut(py);
					if (chart.getAxisSet().getXAxis(0).getRange().lower < 0) {
						chart.getAxisSet().getXAxis(0).scrollUp();
					}
				};
				chart.setRedraw(true);
				chart.setFocus();
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		
		plot.addMouseMoveListener(new MouseMoveListener() {

			public void mouseMove(MouseEvent e) {

				px = (int)xAxis.getDataCoordinate(e.x);
				py = yAxis.getDataCoordinate(e.y);
				if ( px >=  0 && px < catdate.length)
				try {
					chart.getPlotArea().setToolTipText(catdate[px] + "\n 온도:" + String.format("%2.2f", py));	
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		});
		
		chart.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseScrolled(MouseEvent arg0) {
				int wheelCount = (int) Math.ceil(arg0.count / 3.0f);
				chart.setRedraw(false);
				while (wheelCount != 0) {
						
						if (wheelCount > 0) {
							chart.getAxisSet().getYAxis(0).scrollUp();
							wheelCount-- ;
						} else {
							chart.getAxisSet().getYAxis(0).scrollDown();
							wheelCount++ ;
						}
				}
				
				chart.setRedraw(true);
                  
			}
		});

    }
    
    Thread uiUpdateThread ;

	private class MyThread extends Thread {
		private Display display = null;
		private int interval ;
		MyThread(Display display, int interval){
			this.display = display ;
			this.interval = interval ;
		}
		@Override
		public void run() {
			while(!Thread.currentThread().isInterrupted() && !chart.isDisposed() ) {
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						try {
							setYdata();
							reLocate();
						} catch (Exception e) {
							// TODO: handle exception
						}
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

	String[] catdate = {""};
	
    private void setYdata() {
    	ArrayList<MoteInfo> arrayinfo ;
    	final int CNT = 7200 ;
		// create line series


    	IAxis xAxis = chart.getAxisSet().getXAxis(0);

    	try {
    		lineSeries1.setVisible(false);;
    		lineSeries2.setVisible(false);;
    		lineSeries3.setVisible(false);;
    		lineSeries4.setVisible(false);;
    		
//    		chart.getSeriesSet().deleteSeries("S01");
//    		chart.getSeriesSet().deleteSeries("S02");
//    		chart.getSeriesSet().deleteSeries("S03");
//    		chart.getSeriesSet().deleteSeries("S04");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		double[] ys1 , ys2, ys3, ys4 ;
    	int sz,  sw = 0 ;
    	FindMoteInfo findMoteinfo = new FindMoteInfo();
    	time_c = findMoteinfo.getLasTime() ;
    	arrayinfo = findMoteinfo .getMoteInfos(0, CNT) ;
    	catdate = null ;
    	catdate = arrayinfo.stream().map(a -> datefmt.format(a.getTm()))
    							.sorted().distinct().toArray(String[]::new)  ;
    	ISeriesLabel seriesLabel ;
    	if (sel == 0 || sel == 1 ) {
    		sw = 1;
    		sz = 0;
    		ys1 =  new double[(int)arrayinfo.stream().filter(m -> m.getSeq() == 1).count()];
    		for ( MoteInfo motinfo : arrayinfo ) {
    			if (motinfo.getSeq() == 1) {
        			ys1[sz]  = motinfo.getTemp();
        			sz++;
    			}
    		}
//    		lineSeries1 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S01");
    		lineSeries1.setYSeries(ys1);
//			lineSeries1.setDataModel(model);
    		lineSeries1.setLineColor(BLUE);
    		lineSeries1.setYAxisId(axisId);
    		lineSeries1.setSymbolType(PlotSymbolType.NONE);
    		lineSeries1.setAntialias(SWT.ON);

    		lineSeries1.setVisible(true);
//        	xAxis.setCategorySeries(sc1);
    	}
    	if (sel == 0 || sel == 2 ) {
    		ys2 =  new double[(int)arrayinfo.stream().filter(m -> m.getSeq() == 2).count()];
    		sz = 0;
    		for ( MoteInfo motinfo : arrayinfo ) {
    			if (motinfo.getSeq() == 2) {
    				ys2[sz]  = motinfo.getTemp();
    				sz++;
    			}
    		}
//    		lineSeries2 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S02");
    		lineSeries2.setYSeries(ys2);
    		lineSeries2.setLineColor(RED);
    		lineSeries2.setYAxisId(axisId);
    		lineSeries2.setSymbolType(PlotSymbolType.NONE);
    		lineSeries2.setAntialias(SWT.ON);
    		lineSeries2.setVisible(true);

//        	if (sw == 0) xAxis.setCategorySeries(sc2);
        	sw = 1;
    	}
    	if (sel == 0 || sel == 3 ) {
    		ys3 =  new double[(int)arrayinfo.stream().filter(m -> m.getSeq() == 3).count()];
    		sz = 0;
    		for ( MoteInfo motinfo : arrayinfo ) {
    			if (motinfo.getSeq() == 3) {
    				ys3[sz]  = motinfo.getTemp();
    				sz++;
    			}
    		}
//    		lineSeries3 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S03");
    		lineSeries3.setYSeries(ys3);
    		lineSeries3.setLineColor(CYAN);
    		lineSeries3.setYAxisId(axisId);
    		lineSeries3.setSymbolType(PlotSymbolType.NONE);
    		lineSeries3.setAntialias(SWT.ON);
    		lineSeries3.setVisible(true);
//        	if (sw == 0) xAxis.setCategorySeries(sc3);
        	sw = 1;
    	}
    	if (sel == 0 || sel == 4 ) {
    		ys4 =  new double[(int)arrayinfo.stream().filter(m -> m.getSeq() == 4).count()];
    		sz = 0;
    		for ( MoteInfo motinfo : arrayinfo ) {
    			if (motinfo.getSeq() == 4) {
    				ys4[sz]  = motinfo.getTemp();
    				sz++;
    			}
    		}
//    		lineSeries4 = (ILineSeries)chart.getSeriesSet().createSeries(SeriesType.LINE, "S04");
    		lineSeries4.setYSeries(ys4);
    		lineSeries4.setLineColor(GREEN);
    		lineSeries4.setYAxisId(axisId);
    		lineSeries4.setSymbolType(PlotSymbolType.NONE);
    		lineSeries4.setAntialias(SWT.ON);
    		lineSeries4.setVisible(true);
//        	if (sw == 0) xAxis.setCategorySeries(sc4);
    	}
    	lblfromd.setText( catdate.length > 0 ? catdate[0] : datefmt.format(time_c) ) ; 
    	lbltod.setText(datefmt.format(time_c)) ;
    	xAxis.enableCategory(true) ;
    	
		// adjust the axis range
		try {

			chart.getAxisSet().adjustRange();
			IAxis yAxis = chart.getAxisSet().getYAxis(0);
			yAxis.adjustRange();
			chart.getAxisSet().getXAxis(0).adjustRange();
			chart.redraw();
		} catch (SWTException e) {
			// TODO: handle exception
		}
		
		
    }
	DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private DateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Timestamp time_c = Timestamp.valueOf("1900-01-01 00:00:00") ;
    private   void reLocate() {
    	LasTime lastime = LasTime.getInstance() ;
		lblDate.setText(dateFormat.format(time_c));
		lblTime.setText(timeFormat.format(time_c));
		lblDate.pack();
		lblTime.pack();
	}    	
}
