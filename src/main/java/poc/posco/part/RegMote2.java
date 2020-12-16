package poc.posco.part;

import java.util.ArrayList;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import poc.posco.model.MoteStatus;

public class RegMote2 {

	private static class ContentProvider implements IStructuredContentProvider {
		/**
		 * 
		 */
		@Override
		public Object[] getElements(Object input) {
			//return new Object[0];
			ArrayList<MoteStatus> arrayList = (ArrayList<MoteStatus>)input;
			return arrayList.toArray();
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
	
//	Bundle bundle = FrameworkUtil.getBundle(this.getClass());
    
//    URL url8 = FileLocator.find(bundle, new Path("images/slice_page5.png"), null);
    Image slice_page = new Image(Display.getCurrent(), "images/slice_page5.png");
    
    Label lblApActive;
    Label lblApInactive;
    Label lblTagActive;
    Label lblTagInactive;
    Label lblAlertActive;
    Label lblAlertInactive;
    Label lblDate, lblTime;
    
    private Table table;
    private TableViewer tableViewer;
    ArrayList<MoteStatus> tempList ;
    public static final String[] PROPS = { "", "", "","", "DESC","TYPE","SPARE","BATTDT" };
    
	public RegMote2(Composite parent, int style) {

		ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginTop = 20;
		gl_parent.horizontalSpacing = 0;
		gl_parent.marginWidth = 0;
		gl_parent.marginHeight = 0;
		gl_parent.verticalSpacing = 0;
		parent.setLayout(gl_parent);
		parent.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginWidth = 0;
		gl_composite.marginHeight = 0;
		gl_composite.marginBottom = 10;
		gl_composite.horizontalSpacing = 0;
		gl_composite.verticalSpacing = 0;
		composite.setLayout(gl_composite);
		composite.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		
		Composite composite_15 = new Composite(composite, SWT.NONE);
		GridData gd_composite_15 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_15.heightHint = 120;
		composite_15.setLayoutData(gd_composite_15);
		composite_15.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		Label lblNewLabel_4 = new Label(composite_15, SWT.NONE);
		lblNewLabel_4.setBounds(0, 0, 1647, 100);
		lblNewLabel_4.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));
		lblNewLabel_4.setImage(slice_page);
/*
		RegMote.addKeyListener(new keyAdapter() { 
			public void KeyPressed(KeyEvent e) {
				if ( e.keyCode == SWT.F15 ) refreshSensorList();  
			}
		});
*/		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(1, false);
		gl_composite_3.marginRight = 15;
		gl_composite_3.marginLeft = 15;
		gl_composite_3.marginBottom = 20 ;
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_3.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		Composite buttonContainer = new Composite(composite, SWT.NORMAL);
		GridLayout gl_layout = new GridLayout(2, false);
		buttonContainer.setLayout(gl_layout);
		buttonContainer.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, false, 2, 1));
		buttonContainer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TRANSPARENT));

		{
			MessageBox dialog =  new MessageBox(parent.getShell() , SWT.ICON_INFORMATION | SWT.CLOSE);
			dialog.setText("Save Info");
			dialog.setMessage("Saved it!.");

				// open dialog and await user selection
				
			Button b = new Button(buttonContainer, SWT.ON_TOP);
			b.setFont(new Font(null, "Consolas", 16, SWT.NORMAL));
			b.setText(" SAVE ");
			b.setSize(140, 50);
			b.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					em.getTransaction().begin();
					for (MoteStatus m : tempList) {
						em.merge(m) ;
					}
					em.getTransaction().commit();
					dialog.open();
				}
			}); 
		}
		{
			Button b = new Button(buttonContainer, SWT.PUSH);
			b.setFont(new Font(null, "Consolas", 16, SWT.NORMAL));
			b.setText(" CANCEL ");
			b.setSize(140, 50);
			b.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					refreshSensorList();
				}
			}); 
		}
		
		tableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION );
		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setFont(new Font(null,"Calibri", 14, SWT.NORMAL));
		table.setHeaderBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));

		tableViewer.setUseHashlookup(true); 


		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn1 = tableViewerColumn_1.getColumn();
		tblclmnNewColumn1.setAlignment(SWT.CENTER);
		tblclmnNewColumn1.setWidth(80);
		tblclmnNewColumn1.setText("SEQ");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn3 = tableViewerColumn_3.getColumn();
		tblclmnNewColumn3.setAlignment(SWT.CENTER);
		tblclmnNewColumn3.setWidth(120);
		tblclmnNewColumn3.setText("Status");

		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn5 = tableViewerColumn_5.getColumn();
		tblclmnNewColumn5.setAlignment(SWT.CENTER);
		tblclmnNewColumn5.setWidth(250);
		tblclmnNewColumn5.setText("Mac Address");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn2 = tableViewerColumn_2.getColumn();
		tblclmnNewColumn2.setAlignment(SWT.CENTER);
		tblclmnNewColumn2.setWidth(100);
		tblclmnNewColumn2.setText("Sensor ID");


		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn4 = tableViewerColumn_4.getColumn();
		tblclmnNewColumn4.setAlignment(SWT.NONE);
		tblclmnNewColumn4.setWidth(300);
		tblclmnNewColumn4.setText("Description");

		TableViewerColumn tableViewerColumn_6 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn6 = tableViewerColumn_6.getColumn();
		tblclmnNewColumn6.setAlignment(SWT.CENTER);
		tblclmnNewColumn6.setWidth(70);
		tblclmnNewColumn6.setText("Type");

		TableViewerColumn tableViewerColumn_7 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn7 = tableViewerColumn_7.getColumn();
		tblclmnNewColumn7.setAlignment(SWT.CENTER);
		tblclmnNewColumn7.setWidth(70);
		tblclmnNewColumn7.setText("Spare");

		TableViewerColumn tableViewerColumn_8 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNewColumn8 = tableViewerColumn_8.getColumn();
		tblclmnNewColumn8.setAlignment(SWT.CENTER);
		tblclmnNewColumn8.setWidth(250);
		tblclmnNewColumn8.setText("Battery REP. DT");

		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new MoteLabelProvider());
		tableViewer.setInput(tempList);
		tableViewer.setColumnProperties(PROPS);
		
		CellEditor[] editors = new CellEditor[8];
//	    editors[0] = new TextCellEditor(table);
//	    editors[1] = new TextCellEditor(table);
//	    editors[2] = new TextCellEditor(table);
//	    editors[3] = new TextCellEditor(table);
	    editors[4] = new TextCellEditor(table);
	    editors[5] = new TextCellEditor(table);
	    editors[6] = new TextCellEditor(table);
	    editors[7] = new TextCellEditor(table);
	    
	    

	    // Set the editors, cell modifier, and column properties
	    tableViewer.setCellModifier(new MoteCellModifier(tableViewer));
	    tableViewer.setCellEditors(editors);
		
		refreshSensorList();
	}

	@PreDestroy
	public void preDestroy() {
		
	}


//	@SuppressWarnings("unchecked")
	public void refreshSensorList() {
	    EntityManager em = PocMain.emf.createEntityManager();
		tempList = new ArrayList<MoteStatus>();

		em.clear();
		em.getEntityManagerFactory().getCache().evictAll();
        
        TypedQuery<MoteStatus> qMotes = em.createQuery("select t from MoteStatus t order by t.seq ", MoteStatus.class);

        qMotes.getResultList().stream().forEach( t -> tempList.add(t));
        
		em.close();
		// 화면 업데이트

		tableViewer.setInput(tempList);
		tableViewer.refresh();

//		}

	}

	private static class MoteLabelProvider implements ITableLabelProvider {
	  /**
	   * Returns the image
	   * 
	   * @param element
	   *            the element
	   * @param columnIndex
	   *            the column index
	   * @return Image
	   */
	
	String[] statusNm = {"Inactive","Sleep","Active"} ;
	  public Image getColumnImage(Object element, int columnIndex) {
	    return null;
	  }

	  /**
	   * Returns the column text
	   * 
	   * @param element
	   *            the element
	   * @param columnIndex
	   *            the column index
	   * @return String
	   */
	  public String getColumnText(Object element, int columnIndex) {
		  MoteStatus mote = (MoteStatus) element;
	    switch (columnIndex) {
	    case 0:
	      return mote.getSeq()+"";
	    case 1:
	      return statusNm[mote.getAct()];
	    case 2:
		      return mote.getMac();
	    case 3:
		      return mote.getDispNm();
	    case 4:
		      return mote.getDesc();
	    case 5:
		      return mote.getGubun() ;
	    case 6:
		      return mote.getSpare() ;
	    case 7:
		      return mote.getBattDt().length() < 8 ? "" : mote.getBattDt().substring(0, 4) + "-" + mote.getBattDt().substring(4, 6) + "-" + mote.getBattDt().substring(6) ;
	    }
	    return null;
	  }

	  /**
	   * Adds a listener
	   * 
	   * @param listener
	   *            the listener
	   */
	  public void addListener(ILabelProviderListener listener) {
	    // Ignore it
	  }

	  /**
	   * Disposes any created resources
	   */
	  public void dispose() {
	    // Nothing to dispose
	  }

	  /**
	   * Returns whether altering this property on this element will affect the
	   * label
	   * 
	   * @param element
	   *            the element
	   * @param property
	   *            the property
	   * @return boolean
	   */
	  public boolean isLabelProperty(Object element, String property) {
	    return false;
	  }

	  /**
	   * Removes a listener
	   * 
	   * @param listener
	   *            the listener
	   */
	  public void removeListener(ILabelProviderListener listener) {
	    // Ignore
	  }
	}

	private class MoteCellModifier implements ICellModifier {
		  private Viewer viewer;

		  public MoteCellModifier(Viewer viewer) {
		    this.viewer = viewer;
		  }

		  /**
		   * Returns whether the property can be modified
		   * 
		   * @param element
		   *            the element
		   * @param property
		   *            the property
		   * @return boolean
		   */
		  public boolean canModify(Object element, String property) {
		    // Allow editing of all values
			    if (property.equals("DESC") )
				      return true;
				    else if (property.equals("TYPE") || property.equals("SID"))
				      return true;
				    else if (property.equals("SPARE") || property.equals("BATTDT"))
				      return true;
				    else
				      return false;
		  }

		  /**
		   * Returns the value for the property
		   * 
		   * @param element
		   *            the element
		   * @param property
		   *            the property
		   * @return Object
		   */
		  public Object getValue(Object element, String property) {
		    MoteStatus m = (MoteStatus) element;
		    if (property.equals("DESC"))
		      return m.getDesc();
		    else if (property.equals("SID"))
			      return m.getSensorNo()+"";
		    else if (property.equals("TYPE"))
			      return m.getGubun();
		    else if (property.equals("SPARE"))
			      return m.getSpare();
		    else if (property.equals("BATTDT"))
			      return m.getBattDt() == null ? "" : m.getBattDt() ;
		    else
		      return null;
		  }

		  /**
		   * Modifies the element
		   * 
		   * @param element
		   *            the element
		   * @param property
		   *            the property
		   * @param value
		   *            the value
		   */
		  public void modify(Object element, String property, Object value) {
		    if (element instanceof Item)
		      element = ((Item) element).getData();

		    MoteStatus m = (MoteStatus) element;
		    if (property.equals("DESC"))
		      m.setDesc((String) value);
		    else if (property.equals("SID"))
			  m.setSensorNo(Integer.valueOf((String) value) );
		    else if (property.equals("TYPE"))
			  m.setGubun((String) value);
		    else if (property.equals("SPARE"))
			  m.setSpare((String) value);
		    else if (property.equals("BATTDT"))
			  m.setBattDt( value != null ? (String)value : ""   );

		    // Force the viewer to refresh
		    viewer.refresh();
		  }
		}

}