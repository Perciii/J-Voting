package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.*;
import org.eclipse.swt.layout.*;

/**
 * Home GUI allowing to load a profile and then select how to display it
 */
public class MainGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class.getName());
	
	protected final static Display display = Display.getDefault();
	protected final static Shell mainShell = new Shell (display, SWT.CLOSE);
	protected final static Label label = new Label(mainShell, SWT.CENTER);
	protected final static Button selectFileToRead = new Button (mainShell, SWT.PUSH);
	protected final static Button columnsGUI = new Button (mainShell, SWT.PUSH);
	protected final static Button columnsSOIGUI = new Button (mainShell, SWT.PUSH);
	protected final static Button rowsGUI = new Button (mainShell, SWT.PUSH);
	protected final static Button rowsSOIGUI = new Button (mainShell, SWT.PUSH);
	protected final static Button wrappedColumnsGUI = new Button (mainShell, SWT.PUSH);
	protected final static Button wrappedSOIColumnsGUI = new Button (mainShell, SWT.PUSH);
	
	protected static String fileToRead = "";
	protected static String[] profileToRead = new String[1];
	
	public void displayGUI() {
		LOGGER.debug("displayGUI :");
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.verticalSpacing = 10;
		gridLayout.numColumns = 3;
		mainShell.setLayout(gridLayout);
		
		//file chooser
		FileDialog fileChooser = new FileDialog(mainShell, SWT.OPEN);
		fileChooser.setFilterExtensions(new String [] {"*.soc", "*.soi"});
		
		Font boldFont = new Font( label.getDisplay(), new FontData( "Arial", 20, SWT.BOLD ));
		label.setFont(boldFont);
		
		label.setText("Profile Editing");
		selectFileToRead.setText("Select a profile");
		columnsGUI.setText("Columns : Voters");
		rowsGUI.setText("Rows : Voters");
		wrappedColumnsGUI.setText("Columns : Voters wrapped");
		columnsSOIGUI.setText("SOI - Columns : Voters");
		rowsSOIGUI.setText("SOI - Rows : Voters");
		wrappedSOIColumnsGUI.setText("SOI - Columns : Voters wrapped");
		
		GridData gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.FILL;
		label.setLayoutData(gridData);
		
		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 3;
		selectFileToRead.setLayoutData(gridData);
		
		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		columnsGUI.setLayoutData(gridData);
		
		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		rowsGUI.setLayoutData(gridData);
		
		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		wrappedColumnsGUI.setLayoutData(gridData);		

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		columnsSOIGUI.setLayoutData(gridData);
		
		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		rowsSOIGUI.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		wrappedSOIColumnsGUI.setLayoutData(gridData);	
		
		if(fileToRead == "") {
			mainShell.setText("Profile editing - No profile loaded");
		} else {
			mainShell.setText("Profile editing - " + fileToRead);
		}

		//pack elements to reduce size to the smallest
		mainShell.pack();
		
		//center shell in your primary monitor
		Rectangle screenSize = display.getPrimaryMonitor().getBounds();
		mainShell.setLocation((screenSize.width - mainShell.getBounds().width) / 2, (screenSize.height - mainShell.getBounds().height) / 2);
		
		//open the shell
		mainShell.open ();
		
		selectFileToRead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileToRead = fileChooser.open();
				
				//shell text
				if(fileToRead == "") {
					mainShell.setText("Profile editing - No profile loaded");
				} else {
					mainShell.setText("Profile editing - " + fileToRead);
					profileToRead[0] = fileToRead;
				}
				
				//title text
				if(fileToRead == "") {
					label.setText("Profile editing");
				} else {
					String fileExtension = fileToRead.substring(fileToRead.length() - 3);
					System.out.println(fileExtension);
					if(fileExtension.equals("soc")) {
						label.setText("SOC Profile editing");
					} else {//if extension is soi
						label.setText("SOI Profile editing");
					}
					
				}
			}
		});
		
		columnsGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOCColumnsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening Columns GUI : {}", ioe);
				}
			}
		});

		columnsSOIGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOIColumnsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening Columns GUI : {}", ioe);
				}
			}
		});
		
		rowsGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOCRowsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening Rows GUI : {}", ioe);
				}
			}
		});
		
		rowsSOIGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOIRowsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening rows GUI : {}", ioe);
				}
			}
		});
		
		wrappedColumnsGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOCWrappedColumnsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening wrapped GUI : {}", ioe);
				}
			}
		});
		
		wrappedSOIColumnsGUI.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SOIWrappedColumnsGUI.main(profileToRead);
				} catch (IOException ioe) {
					LOGGER.debug("IOException when opening wrapped GUI : {}", ioe);
				}
			}
		});
		
		while (!mainShell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	public static void main(String[] args) {
		MainGUI mainGUI = new MainGUI();
		mainGUI.displayGUI();
	}

}