package poc.posco.part;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

@SuppressWarnings("serial")
public class RepeatWidget extends Composite {

	Image img_active = SWTResourceManager.getImage("images/moteicon_active.png");
	Image img_inactive = SWTResourceManager.getImage("images/moteicon_inactive.png");
	Image img_low = SWTResourceManager.getImage("images/moteicon_lowbattery.png");
	
	String sloc ;
	String id ;
	int idx ;
	Label lbl = new Label(this, SWT.NONE);
	
	public RepeatWidget(Composite parent, String id, int x, int y) {
		super(parent, SWT.NONE);
		this.setSize(66, 46);
		this.setBackgroundImage(img_active);
		this.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		this.id  = id ;
		this.setLocate(x, y);
		this.setLayout(new GridLayout(1,true));
		lbl.setFont(SWTResourceManager.getFont( "¸¼Àº °íµñ", 14, SWT.BOLD));
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lbl.setText(id);
//		lbl.setBounds(10, 10, 40, 30);
		lbl.setLayoutData(new GridData(SWT.CENTER , SWT.CENTER, true,true ));
		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
	}
	
	public void setLocate(int x, int y ) {
		this.setLocation(x , y );
	}
	public void setImage(int x) {
		if (x == 2) {
			this.setBackgroundImage(img_active);
		} else if (x == 1) {
			this.setBackgroundImage(img_low);
		} else  {
			this.setBackgroundImage(img_inactive);
		}
	}

	public void setId(String id) {
		this.id = id;
		lbl.setText(id);
		lbl.requestLayout();
	}


	public int getIdx() {
		return idx;
	}

}
