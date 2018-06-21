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
	protected final static Shell mainShell = new Shell(display, SWT.CLOSE);
	protected GridData gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
	protected final static Label label = new Label(mainShell, SWT.CENTER);
	protected final static Button selectFileToRead = new Button(mainShell, SWT.PUSH);
	protected static Button columnsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button columnsSOIGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button rowsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button rowsSOIGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button wrappedColumnsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button wrappedColumnsSOIGUIButton = new Button(mainShell, SWT.PUSH);

	protected static String fileToRead = "";
	protected static String fileExtension = "";
	protected static String[] profileToRead = new String[1];

	public void displayGUI() {
		LOGGER.debug("displayGUI :");
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginTop = 10;
		gridLayout.verticalSpacing = 10;
		gridLayout.numColumns = 3;
		mainShell.setLayout(gridLayout);

		// file chooser
		FileDialog fileChooser = new FileDialog(mainShell, SWT.OPEN);
		fileChooser.setFilterExtensions(new String[] { "*.soc", "*.soi" });

		Font boldFont = new Font(label.getDisplay(), new FontData("Arial", 20, SWT.BOLD));
		label.setFont(boldFont);

		label.setText("Profile Editing");
		selectFileToRead.setText("Select a profile");

		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.FILL;
		label.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 3;
		selectFileToRead.setLayoutData(gridData);

		createSOCButtons();
		createSOIButtons();
		
		if (fileToRead == "") {
			mainShell.setText("Profile editing - No profile loaded");
			// remove SOI buttons
			columnsSOIGUIButton.setVisible(false);
			rowsSOIGUIButton.setVisible(false);
			wrappedColumnsSOIGUIButton.setVisible(false);
			GridData data = (GridData) columnsSOIGUIButton.getLayoutData();
			System.out.println(data);
			data.exclude = true;
			data = (GridData) rowsSOIGUIButton.getLayoutData();
			data.exclude = true;
			data = (GridData) wrappedColumnsSOIGUIButton.getLayoutData();
			data.exclude = true;

			// remove SOC buttons
			columnsGUIButton.setVisible(false);
			rowsGUIButton.setVisible(false);
			wrappedColumnsGUIButton.setVisible(false);
			data = (GridData) columnsGUIButton.getLayoutData();
			data.exclude = true;
			data = (GridData) rowsGUIButton.getLayoutData();
			data.exclude = true;
			data = (GridData) wrappedColumnsGUIButton.getLayoutData();
			data.exclude = true;
		} else {
			mainShell.setText("Profile editing - " + fileToRead);
		}

		// pack elements to reduce size to the smallest
		mainShell.pack();

		// center shell in your primary monitor
		Rectangle screenSize = display.getPrimaryMonitor().getBounds();
		mainShell.setLocation((screenSize.width - mainShell.getBounds().width) / 2,
				(screenSize.height - mainShell.getBounds().height) / 2);

		// open the shell
		mainShell.open();

		selectFileToRead.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileToRead = fileChooser.open();

				// shell text
				if (fileToRead == null || fileToRead.isEmpty()) {
					mainShell.setText("Profile editing - No profile loaded");
					label.setText("Profile editing");
				} else {
					fileExtension = fileToRead.substring(fileToRead.length() - 3);
					System.out.println(fileExtension);
					if (fileExtension.equals("soc")) {
						label.setText("SOC Profile editing");
						displaySOCButtons();
						mainShell.pack();
						mainShell.layout();
					} else {// if extension is soi
						label.setText("SOI Profile editing");
						displaySOIButtons();
						mainShell.pack();
						mainShell.layout();
					}

					mainShell.setText("Profile editing - " + fileToRead);
					profileToRead[0] = fileToRead;
				}
			}
		});

		while (!mainShell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	public void createSOCButtons() {
		columnsGUIButton.setText("Columns SOC : Voters");
		rowsGUIButton.setText("Rows : Voters");
		wrappedColumnsGUIButton.setText("Columns : Voters wrapped");

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		columnsGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		rowsGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		wrappedColumnsGUIButton.setLayoutData(gridData);

		// listeners
		columnsGUIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileToRead == null || fileToRead.isEmpty()) {
					displayMessageNoFileLoaded();
				} else {
					try {
						new SOCColumnsGUI().displayProfileWindow(profileToRead);
					} catch (IOException ioe) {
						LOGGER.debug("IOException when opening Columns GUI : {}", ioe);
					}
				}
			}
		});

		rowsGUIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileToRead == null || fileToRead.isEmpty()) {
					displayMessageNoFileLoaded();
				} else {
					try {
						new SOCRowsGUI().displayProfileWindow(profileToRead);
					} catch (IOException ioe) {
						LOGGER.debug("IOException when opening Rows GUI : {}", ioe);
					}
				}
			}
		});

		wrappedColumnsGUIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileToRead == null || fileToRead.isEmpty()) {
					displayMessageNoFileLoaded();
				} else {
					try {
						new SOCWrappedColumnsGUI().displayProfileWindow(profileToRead);
					} catch (IOException ioe) {
						LOGGER.debug("IOException when opening wrapped GUI : {}", ioe);
					}
				}
			}
		});
	}
	
	public void createSOIButtons() {
		columnsSOIGUIButton.setText("Columns SOI : Voters");
		rowsSOIGUIButton.setText("Rows : Voters");
		wrappedColumnsSOIGUIButton.setText("Columns : Voters wrapped");

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		columnsSOIGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		rowsSOIGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		wrappedColumnsSOIGUIButton.setLayoutData(gridData);

		// listeners
		columnsSOIGUIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileToRead == null || fileToRead.isEmpty()) {
					displayMessageNoFileLoaded();
				} else {
					try {
						new SOIColumnsGUI().displayProfileWindow(profileToRead);
					} catch (IOException ioe) {
						LOGGER.debug("IOException when opening Columns SOI GUI : {}", ioe);
					}
				}
			}
		});

		rowsSOIGUIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileToRead == null || fileToRead.isEmpty()) {
					displayMessageNoFileLoaded();
				} else {
					try {
						new SOIRowsGUI().displayProfileWindow(profileToRead);
					} catch (IOException ioe) {
						LOGGER.debug("IOException when opening Rows SOI GUI : {}", ioe);
					}
				}
			}
		});

		wrappedColumnsSOIGUIButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (fileToRead == null || fileToRead.isEmpty()) {
					displayMessageNoFileLoaded();
				} else {
					try {
						new SOIWrappedColumnsGUI().displayProfileWindow(profileToRead);
					} catch (IOException ioe) {
						LOGGER.debug("IOException when opening Wrapped Columns SOI GUI : {}", ioe);
					}
				}
			}
		});
	}

	public void displaySOCButtons() {
		LOGGER.debug("displaySOCButtons : ");

		// remove SOI buttons
		columnsSOIGUIButton.setVisible(false);
		rowsSOIGUIButton.setVisible(false);
		wrappedColumnsSOIGUIButton.setVisible(false);
		GridData data = (GridData) columnsSOIGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) rowsSOIGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) wrappedColumnsSOIGUIButton.getLayoutData();
		data.exclude = true;

		// add SOC buttons
		columnsGUIButton.setVisible(true);
		rowsGUIButton.setVisible(true);
		wrappedColumnsGUIButton.setVisible(true);
		data = (GridData) columnsGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) rowsGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) wrappedColumnsGUIButton.getLayoutData();
		data.exclude = false;
	}

	public void displaySOIButtons() {
		LOGGER.debug("displaySOIButtons : ");

		// remove SOC buttons
		columnsGUIButton.setVisible(false);
		rowsGUIButton.setVisible(false);
		wrappedColumnsGUIButton.setVisible(false);
		GridData data = (GridData) columnsGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) rowsGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) wrappedColumnsGUIButton.getLayoutData();
		data.exclude = true;

		// add SOI buttons
		columnsSOIGUIButton.setVisible(true);
		rowsSOIGUIButton.setVisible(true);
		wrappedColumnsSOIGUIButton.setVisible(true);
		data = (GridData) columnsSOIGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) rowsSOIGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) wrappedColumnsSOIGUIButton.getLayoutData();
		data.exclude = false;
	}

	public void displayMessageNoFileLoaded() {
		LOGGER.debug("displayMessageNoFileLoaded : ");
		MessageBox messageBox = new MessageBox(mainShell, SWT.OK);
		messageBox.setText("Warning");
		messageBox.setMessage("No profile loaded !");
		messageBox.open();
	}

	public static void main(String[] args) {
		MainGUI mainGUI = new MainGUI();
		mainGUI.displayGUI();
	}

}