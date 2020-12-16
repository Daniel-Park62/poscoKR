package poc.posco.part;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtchart.Chart;
import org.eclipse.swtchart.IAxis;
import org.eclipse.swtchart.ILineSeries;
import org.eclipse.swtchart.ILineSeries.PlotSymbolType;
import org.eclipse.swtchart.ISeries.SeriesType;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swtchart.ISeriesLabel;
import org.eclipse.swtchart.Range;

import com.ibm.icu.util.Calendar;

import poc.posco.model.ChartData;
import poc.posco.model.FindMoteInfo;
import poc.posco.model.MoteHist;
import poc.posco.model.MoteInfo;

public class ViewChart {
	private Chart chart ;
	
	private static final double[] yS1 = { 30,31,32,33,34,35,36,37,38,37,35,34,33,32,31,30,29,27};
	private static final double[] yS2 = { 0.3, 0.4, 0.6, 0.8, 1.5, 1.8, 0.77, 0.55, 0.0, -0.30, -0.77, -0.99,
			-1.2, -0.95, -0.74, -0.30 , 1.0};
	
	private ILineSeries lineSeries1; 
	private ILineSeries lineSeries2; 
	private ILineSeries lineSeries3; 
	private ILineSeries lineSeries4;
	private int axisId ;
	final Color BLACK = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);
	final Color RED = Display.getDefault().getSystemColor(SWT.COLOR_RED);
	final Color BLUE = Display.getDefault().getSystemColor(SWT.COLOR_BLUE);
	final Color GREEN = Display.getDefault().getSystemColor(SWT.COLOR_GREEN);
	final Color CYAN = Display.getDefault().getSystemColor(SWT.COLOR_DARK_CYAN);
    private DateFormat dateFmt1 = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat dateFmt2 = new SimpleDateFormat("HH:mm:ss");
	
	FindMoteInfo findMoteinfo = new FindMoteInfo();
//	ArrayList<MoteHist> arrayinfo ;
	ArrayList<ChartData> arrayinfo ;

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
    public ViewChart(Composite parent, int style) {

	    final Font font2 = SWTResourceManager.getFont("Calibri", 16, SWT.NORMAL);
	    final Font font21 = SWTResourceManager.getFont("Calibri", 14, SWT.NORMAL);

	    final Image chart_icon = new Image(Display.getCurrent(),"images/chart_icon.png");
	    Image resize = resizeImage(chart_icon, 90, 90) ;
	    final Cursor busyc = PocMain.busyc ;
	    final Cursor curc = parent.getCursor() ;
	    
	    Color COLOR_T = SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT) ;
    	
    	SashForm sash1 = new SashForm(parent, SWT.VERTICAL);

    	Composite comps1 = new Composite(sash1, SWT.NONE);
    	comps1.setLayout(new GridLayout(2, false));
    	comps1.setBackground(parent.getBackground());
//    	comps1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    	
    	Label lticon = new Label(comps1, SWT.NONE);
    	lticon.setImage(resize);
    	lticon.setLayoutData(new GridData(SWT.FILL, GridData.VERTICAL_ALIGN_CENTER, false, true));
    	lticon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
//    	lticon.setSize(120, 120);
    	
		Label ltitle = new Label(comps1, SWT.NONE ) ;
    	ltitle.setText("  Chart View 제강 KR IMPELLER 내부온도 모니터링" ) ;
    	ltitle.setFont(SWTResourceManager.getFont("맑은 고딕", 22, SWT.BOLD ) );
    	ltitle.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    	ltitle.setLayoutData(new GridData( SWT.FILL , SWT.CENTER , true, false));
    	
//    	ltitle.setBounds(121, 0, -1,-1);
    	
		Composite composite_2 = new Composite(sash1, SWT.NONE);
		GridLayout gl_in = new GridLayout(10,false);
		gl_in.marginRight = 50;
		gl_in.marginLeft = 65;
		
		composite_2.setLayout(gl_in);
		GridData gd_in = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_in.heightHint = 50;

		composite_2.setLayoutData(gd_in);
		composite_2.setBackground(COLOR_T);
		{
			Label lbl = new Label(composite_2, SWT.NONE) ;
			lbl.setText(" * ID Select ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.pack();
			lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		}
		Combo cbddown = new Combo(composite_2, SWT.DROP_DOWN | SWT.BORDER);
		cbddown.setFont(font21);
		cbddown.setItems(new String[] {" ALL ", " S01 "," S02 "," S03 "," S04 "});
		cbddown.select(0);
		cbddown.pack();
		cbddown.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		cbddown.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				sel = cbddown.getSelectionIndex();
				try {
					setYdata();
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
		
		{
			Label lbl = new Label(composite_2, SWT.NONE) ;
			lbl.setText("  * From Date/Time ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.pack();
			lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		}
		
		GridData gdinput = new GridData(100,20);
		DateText  fromDate = new DateText (composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER  );
		fromDate.setLayoutData(gdinput);
		fromDate.setFont(font21);
		
		TimeText fromTm = new TimeText(composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER );
		fromTm.setLayoutData(gdinput);
		fromTm.setFont(font21);

		{
			Label lbl = new Label(composite_2, SWT.NONE) ;
			lbl.setText("  * To Date/Time ");
			lbl.setBackground(COLOR_T);
			lbl.setFont(font2);
			lbl.pack();
			lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		}
		DateText toDate = new DateText(composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER );
		toDate.setLayoutData(gdinput);
		toDate.setFont(font21);
		
		TimeText toTm = new TimeText(composite_2, SWT.SINGLE | SWT.BORDER | SWT.CENTER );
		toTm.setLayoutData(gdinput);
		toTm.setFont(font21);
		
		{
			Button b = new Button(composite_2, SWT.PUSH);
			b.setFont(font2);
			b.setText(" Search ");
			b.pack();
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String sfrom = fromDate.getText() + " " + fromTm.getText() ;
					String sto = toDate.getText() + " " + toTm.getText() ;
					
					try {
						Timestamp ts_dt = Timestamp.valueOf(sfrom) ;
						ts_dt = Timestamp.valueOf(sto) ;
					} catch (Exception e2) {
						MessageBox dialog =  new MessageBox(parent.getShell() , SWT.ICON_ERROR | SWT.CLOSE);
						dialog.setText("날짜 오류");
						dialog.setMessage("날짜입력을 바르게 하세요.");
						dialog.open();
						return ;
					}
					parent.getShell().setCursor( busyc);
//					arrayinfo = findMoteinfo.getMoteHists( sfrom, sto, cbddown.getSelectionIndex());
					arrayinfo =  (ArrayList<ChartData>) findMoteinfo.getChartData( sfrom, sto, cbddown.getSelectionIndex());
			    	catdate = null ;
			    	catdate = arrayinfo.stream().map(a -> datefmt.format(a.getTm()))
			    							.sorted().distinct().toArray(String[]::new)  ;

					setYdata();
					parent.getShell().setCursor( curc);
				}
			}); 
		}
		Timestamp todt = new Timestamp(findMoteinfo.getLasTime().getTime() ) ;
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(todt.getTime()); 
		cal.add(Calendar.SECOND, -3600); 
		
		Timestamp fmdt = new Timestamp(cal.getTime().getTime());
		fromDate.setText(dateFmt1.format(fmdt ) );
		fromTm.setText(dateFmt2.format(fmdt ) );
		toDate.setText(dateFmt1.format(todt ) );
		toTm.setText(dateFmt2.format(todt ) );
		
    	
		chart = new Chart(sash1, SWT.NONE);
		chart.setBackground(parent.getBackground());
		chart.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_CROSS));
		
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
		chart.getLegend().setFont(new Font(Display.getCurrent(),"Calibri", 14, SWT.NORMAL));
		// adjust the axis range
//		chart.getAxisSet().adjustRange();
		sash1.setWeights(new int[] {12,5,88});
		
		chart.setFocus();
		final IAxis xAxis = chart.getAxisSet().getXAxis(0);
		final IAxis yAxis = chart.getAxisSet().getYAxis(0);
		xAxis.getTick().setForeground(BLACK);
		xAxis.getTick().setFont(new Font(Display.getCurrent(),"Calibri", 12, SWT.BOLD));
		xAxis.getTitle().setForeground(BLACK);

		yAxis.getTick().setForeground(BLACK);
		yAxis.getTitle().setForeground(BLACK);
		yAxis.getTitle().setText("Temp.");
		yAxis.getTick().setFont(new Font(Display.getCurrent(),"Calibri", 12, SWT.BOLD));
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
				if (px >= 0 && px < catdate.length)
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
    
	String[] catdate = {""};
	
    private void setYdata() {

    	IAxis xAxis = chart.getAxisSet().getXAxis(0);

    	try {
    		lineSeries1.setVisible(false);
    		lineSeries2.setVisible(false);
    		lineSeries3.setVisible(false);;
    		lineSeries4.setVisible(false);
    		
//    		chart.getSeriesSet().deleteSeries("S01");
//    		chart.getSeriesSet().deleteSeries("S02");
//    		chart.getSeriesSet().deleteSeries("S03");
//    		chart.getSeriesSet().deleteSeries("S04");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		double[] ys1 , ys2, ys3, ys4 ;
    	int sz,  sw = 0 ;
    	
    	time_c = findMoteinfo.getLasTime() ;
    	
    	ISeriesLabel seriesLabel ;
    	if (sel == 0 || sel == 1 ) {
    		sw = 1;
    		sz = 0;
    		ys1 =  new double[(int)arrayinfo.stream().filter(m -> m.getSeq() == 1).count()];
    		for ( ChartData motinfo : arrayinfo ) {
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
    		for ( ChartData motinfo : arrayinfo ) {
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
    		for ( ChartData motinfo : arrayinfo ) {
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
    		for ( ChartData motinfo : arrayinfo ) {
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

    private DateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Timestamp time_c = Timestamp.valueOf("1900-01-01 00:00:00") ;
	
    private Image resizeImage(Image image, int width, int height) {
        Image scaled = new Image(Display.getDefault(), width, height);
        GC gc = new GC(scaled);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        gc.drawImage(image, 0, 0,image.getBounds().width, image.getBounds().height, 0, 0, width, height);
        gc.dispose();
        image.dispose();
        return scaled;
    }
}
