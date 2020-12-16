package poc.posco.part;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import poc.posco.model.MoteConfig;

public class PocMain extends ApplicationWindow {

    public static EntityManagerFactory emf ; //= Persistence.createEntityManagerFactory("poc1");
    public static MoteConfig MOTECNF = MoteConfig.getInstance() ;
    final public static Cursor handc = SWTResourceManager.getCursor( SWT.CURSOR_HAND);
    final public static Cursor busyc = SWTResourceManager.getCursor( SWT.CURSOR_WAIT);

	public static Composite cur_comp ;
	/**
	 * Create the application window.
	 */
	public PocMain() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();

		String dbip = System.getenv("DBIP") ;  // 林家:port
        if (dbip == null) dbip = "localhost" ;
        String dbport = System.getenv("DBPORT") ;  // 林家:port
        if (dbport == null) dbport = "3306" ;
        
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.url", "jdbc:mariadb://" + dbip + ":" + dbport + "/poc1");
        properties.put("javax.persistence.jdbc.user", "pocusr");
        properties.put("javax.persistence.jdbc.password", "dawinit1"); 
        properties.put("javax.persistence.jdbc.driver","org.mariadb.jdbc.Driver") ;
        properties.put("eclipselink.target-database","MySQL");
//        
        emf = Persistence.createEntityManagerFactory("poc1", properties) ;
		
//		addStatusLine();
	}

	public static void delWidget(Composite parent) {
		 
	    for (Control kid : parent.getChildren()) {
	    	try {
		        kid.dispose();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
//		Composite container = new Composite(parent, SWT.NONE);
		Shell shell = getShell() ;
		shell.setText("POSCO 力碍 KR IMPELLER");
		shell.setBounds(5, 5, 1800, 1000);
//		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL);
		sashForm.setSashWidth(5);
		Composite comp1_1 = new Composite(sashForm, SWT.NONE) ;
		comp1_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_TRANSPARENT));
		comp1_1.setLayout(new GridLayout(1, true));
		GridData gd_1 = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_1.minimumWidth = 300; 
		gd_1.widthHint = 300;
		
//		comp1_1.setLayoutData(gd_1); 
		new MainMenu(comp1_1);
		comp1_1.pack();
		 
		Composite comp1_2 = new Composite(sashForm, SWT.NONE);
		comp1_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		comp1_2.setLayout(new FillLayout());
//		new RealTime(comp1_2, SWT.NONE);
//		new RegMote(comp1_2, SWT.NONE);
		new DashBoard(comp1_2, SWT.NONE);
//		new RealChart(comp1_2, SWT.NONE);
//		new SensorPos(comp1_2, SWT.NONE);

		sashForm.setWeights(new int[] {20,80});
		cur_comp = comp1_2 ;
		return parent;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
//		MenuManager menu1 = new MenuManager("DashBoard");
//		menu1.add(act_dash);
//		menuManager.add(menu1) ;
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			PocMain window = new PocMain();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public static Image resize(Image image, int width, int height) {
		  Image scaled = new Image(Display.getCurrent(), width, height);
		  GC gc = new GC(scaled);
		  gc.setAntialias(SWT.ON);
		  gc.setInterpolation(SWT.HIGH);
		  gc.drawImage(image, 0, 0,image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		  gc.dispose();
		  image.dispose(); // don't forget about me!
		  return scaled;
	}
    public static void exportTable(TableViewer tableViewer, int si)  {
        // TODO: add logic to ask user for the file location

    	String[] ext = { "csv" }  ;
    	   final FileDialog dlg = new FileDialog ( Display.getDefault().getActiveShell() , SWT.APPLICATION_MODAL | SWT.SAVE );
    	    dlg.setFileName("sensorData");
    	    dlg.setFilterExtensions ( ext );
    	    dlg.setOverwrite ( true );
    	    dlg.setText ( "历厘颇老疙 急琶" );

    	    String fileName = dlg.open ();
    	    if ( fileName == null )
    	    {
    	        return ;
    	    }
    	    if (!fileName.matches("\\.csv$") ) fileName += ".csv" ;
//      File  file = new File(fileName + "." + ext[ dlg.getFilterIndex() ] );
    	    
    	    
//        BufferedWriter bw = new BufferedWriter(osw);
        
        try {
    		FileOutputStream fos = new FileOutputStream(fileName );
    		OutputStreamWriter osw = new OutputStreamWriter(fos, "MS949");
        	BufferedWriter writer = new BufferedWriter(new BufferedWriter(osw)) ;
            final Table table = tableViewer.getTable();
            final int[] columnOrder = table.getColumnOrder();
 
            for(int columnOrderIndex = si; columnOrderIndex < columnOrder.length; 
                    columnOrderIndex++) {
                int columnIndex = columnOrder[columnOrderIndex];
                TableColumn tableColumn = table.getColumn(columnIndex);
                if (tableColumn.getText().equals("ID")) 
                	writer.write("SID");
                else
                    writer.write(tableColumn.getText());
                if ( columnOrderIndex+1 != columnOrder.length ) writer.write(",");
            }
            writer.write("\r\n");
            
            final int itemCount = table.getItemCount();
            for(int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                TableItem item = table.getItem(itemIndex);
                
                for(int columnOrderIndex = si; 
                        columnOrderIndex < columnOrder.length; 
                        columnOrderIndex++) {
                    int columnIndex = columnOrder[columnOrderIndex];
                    writer.write(item.getText(columnIndex));
                    if ( columnOrderIndex+1 != columnOrder.length ) writer.write(",");
                }
                writer.write("\r\n");
            }
            writer.close();
        } catch(IOException ioe) {
            // TODO: add logic to inform the user of the problem
            System.err.println("trouble exporting table data to file");
            ioe.printStackTrace();
		}
        
    }
	
}
