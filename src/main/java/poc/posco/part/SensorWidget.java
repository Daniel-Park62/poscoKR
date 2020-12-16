package poc.posco.part;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

@SuppressWarnings("serial")
public class SensorWidget extends Composite {

	final int w = 80, h = 80 ;

	Image image2 = PocMain.resize( new Image( null, "images/icon_active.png"), w, h);
	String sloc ;
	int idx ;
	
	public SensorWidget getDrag() { return this; }
	
	public SensorWidget(Composite parent, int idx, String text) {
		super(parent, SWT.NONE);
		this.setSize(w, h);
		this.setBackgroundImage(image2);
		this.sloc = text ;
		this.idx  = idx ;
		this.setLayout(new GridLayout(1,false));
		
		Label lbl = new Label(this, SWT.CENTER);
		lbl.setFont(SWTResourceManager.getFont( "¸¼Àº °íµñ", 18, SWT.BOLD));
		lbl.setForeground(SWTResourceManager.getColor(SWT.COLOR_YELLOW));
		lbl.setText(text);
//		lbl.setBounds(0, 15, 60, 30);
		lbl.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		lbl.setCursor(PocMain.handc);
		lbl.setLayoutData(new GridData(SWT.FILL , SWT.CENTER, true,true ));
		try {
			lbl.requestLayout();	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		DragSource dragSource = new DragSource(lbl, DND.DROP_MOVE);
		Transfer[] types = new Transfer[] { LocalSelectionTransfer.getTransfer()};
		dragSource.setTransfer(types);
		dragSource.addDragListener(new DragSourceListener() {
			@Override
            public void dragStart(DragSourceEvent event) {
				event.doit = true ;
            }

			@Override
			public void dragSetData(DragSourceEvent event) {
				SensorPos.dragWidget =  getDrag();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				// TODO Auto-generated method stub
			
			}
        });
	}
	
	public String getSloc() {
		return this.sloc ;
	}

	public int getIdx() {
		return idx;
	}

}
