package poc.posco.part;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class SensorWidget2 extends Composite {
	
	final int w = 80, h = 80 ;

	Image img_active = PocMain.resize( new Image( null, "images/icon_active.png"), w, h);
	Image img_inactive = PocMain.resize(new Image( null, "images/icon_inactive.png"), w, h);
	Image img_low = PocMain.resize( new Image( null, "images/icon_low.png"), w, h);
	
	String sloc ;
	String temp ;
	String id ;
	int idx ;
	Label lbl_temp ;
	Label lbl ;
	
	public SensorWidget2(Composite parent, String id, String text) {
		super(parent, SWT.NONE);
		this.setSize(w, w);
		this.setBackgroundImage(img_active);
		this.temp = text ;
		this.id  = id ;
//		this.pack();
		this.setLayout(new GridLayout(1,false));
		this.setLayoutData(new GridData(SWT.FILL , SWT.FILL, true,true));
		lbl_temp = new Label(this, SWT.CENTER);
		lbl = new Label(this, SWT.CENTER);

		lbl_temp.setFont(SWTResourceManager.getFont( "¸¼Àº °íµñ", 16, SWT.BOLD));
		lbl_temp.setForeground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		lbl_temp.setText(text);
//		lbl_temp.setBounds(1, 15, 60, 30);
		lbl_temp.setCursor(PocMain.handc);
		lbl_temp.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl_temp.setLayoutData(new GridData(SWT.FILL , SWT.CENTER, true,true ));
		lbl_temp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				chartView( idx );
			}
		});


		lbl.setFont(SWTResourceManager.getFont( "Microsoft Sans Serif", 10, SWT.BOLD));
		lbl.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		lbl.setText(id);
		lbl.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lbl.setLayoutData(new GridData(SWT.FILL , SWT.CENTER, true,true ));

	}

	private void chartView( int sno) {

		PocMain.delWidget(PocMain.cur_comp);
		PocMain.cur_comp.setLayout(new FillLayout());
		new RealChart(PocMain.cur_comp, SWT.NONE, sno);
		PocMain.cur_comp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		PocMain.cur_comp.setSize(PocMain.cur_comp.getParent().getSize());
		PocMain.cur_comp.getParent().layout();
		PocMain.cur_comp.setToolTipText("Activechart");
	}

	public void setImage(int x) {
		if (x == 2) {
			this.setBackgroundImage(img_active);
		} else if (x == 1) {
			this.setBackgroundImage(img_low);
		} else {
			this.setBackgroundImage(img_inactive);
		}
	}
	public void setTemp(String temp) {
		this.temp = temp;
		lbl_temp.setText(temp);
		lbl_temp.requestLayout();
	}


	public void setId(String id) {
		this.id = id;
		lbl.setText(id);
	}


	public void setIdx(int idx) {
		this.idx = idx;
	}

}