package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Home GUI allowing to load a profile and then select how to display it
 */
public class MainGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(MainGUI.class.getName());

	protected final static Display display = Display.getDefault();
	protected final static Shell mainShell = new Shell(display, SWT.CLOSE | SWT.RESIZE);
	protected GridData gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
	protected final static Label label = new Label(mainShell, SWT.CENTER);
	protected final static Button selectFileToReadButton = new Button(mainShell, SWT.PUSH);
	protected static Button socColumnsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button soicolumnsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button socRowsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button soiRowsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button socWrappedColumnsGUIButton = new Button(mainShell, SWT.PUSH);
	protected static Button soiWrappedColumnsGUIButton = new Button(mainShell, SWT.PUSH);

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
		selectFileToReadButton.setText("Select a profile");

		gridData.horizontalSpan = 3;
		gridData.horizontalAlignment = SWT.FILL;
		label.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 3;
		selectFileToReadButton.setLayoutData(gridData);

		createSOCButtons();
		createSOIButtons();

		if (fileToRead == "") {
			mainShell.setText("Profile editing - No profile loaded");
			// remove SOI buttons
			soicolumnsGUIButton.setVisible(false);
			soiRowsGUIButton.setVisible(false);
			soiWrappedColumnsGUIButton.setVisible(false);
			GridData data = (GridData) soicolumnsGUIButton.getLayoutData();
			data.exclude = true;
			data = (GridData) soiRowsGUIButton.getLayoutData();
			data.exclude = true;
			data = (GridData) soiWrappedColumnsGUIButton.getLayoutData();
			data.exclude = true;

			// remove SOC buttons
			socColumnsGUIButton.setVisible(false);
			socRowsGUIButton.setVisible(false);
			socWrappedColumnsGUIButton.setVisible(false);
			data = (GridData) socColumnsGUIButton.getLayoutData();
			data.exclude = true;
			data = (GridData) socRowsGUIButton.getLayoutData();
			data.exclude = true;
			data = (GridData) socWrappedColumnsGUIButton.getLayoutData();
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

		selectFileToReadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fileToRead = fileChooser.open();

				// shell text
				if (fileToRead == null || fileToRead.isEmpty()) {
					mainShell.setText("Profile editing - No profile loaded");
					label.setText("Profile editing");
				} else {
					fileExtension = fileToRead.substring(fileToRead.length() - 3);
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

	/**
	 * Instantiates and set characteristics of SOC buttons with their specific
	 * behaviors.
	 */
	public void createSOCButtons() {
		socColumnsGUIButton.setText("Columns : Voters");
		socRowsGUIButton.setText("Rows : Voters");
		socWrappedColumnsGUIButton.setText("Columns : Voters wrapped");

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		socColumnsGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		socRowsGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		socWrappedColumnsGUIButton.setLayoutData(gridData);

		// listeners
		socColumnsGUIButton.addSelectionListener(new SelectionAdapter() {
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

		socRowsGUIButton.addSelectionListener(new SelectionAdapter() {
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

		socWrappedColumnsGUIButton.addSelectionListener(new SelectionAdapter() {
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

	/**
	 * Instantiates and set characteristics of SOI buttons with their specific
	 * behaviors.
	 */
	public void createSOIButtons() {
		soicolumnsGUIButton.setText("Columns : Voters");
		soiRowsGUIButton.setText("Rows : Voters");
		soiWrappedColumnsGUIButton.setText("Columns : Voters wrapped");

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		soicolumnsGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		soiRowsGUIButton.setLayoutData(gridData);

		gridData = new GridData(GridData.CENTER, GridData.CENTER, true, false);
		gridData.horizontalSpan = 1;
		soiWrappedColumnsGUIButton.setLayoutData(gridData);

		// listeners
		soicolumnsGUIButton.addSelectionListener(new SelectionAdapter() {
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

		soiRowsGUIButton.addSelectionListener(new SelectionAdapter() {
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

		soiWrappedColumnsGUIButton.addSelectionListener(new SelectionAdapter() {
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

	/**
	 * Displays SOC buttons and hides SOI ones
	 */
	public void displaySOCButtons() {
		LOGGER.debug("displaySOCButtons : ");

		// remove SOI buttons
		soicolumnsGUIButton.setVisible(false);
		soiRowsGUIButton.setVisible(false);
		soiWrappedColumnsGUIButton.setVisible(false);
		GridData data = (GridData) soicolumnsGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) soiRowsGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) soiWrappedColumnsGUIButton.getLayoutData();
		data.exclude = true;

		// add SOC buttons
		socColumnsGUIButton.setVisible(true);
		socRowsGUIButton.setVisible(true);
		socWrappedColumnsGUIButton.setVisible(true);
		data = (GridData) socColumnsGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) socRowsGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) socWrappedColumnsGUIButton.getLayoutData();
		data.exclude = false;
	}

	/**
	 * Displays SOI buttons and hides SOC ones
	 */
	public void displaySOIButtons() {
		LOGGER.debug("displaySOIButtons : ");

		// remove SOC buttons
		socColumnsGUIButton.setVisible(false);
		socRowsGUIButton.setVisible(false);
		socWrappedColumnsGUIButton.setVisible(false);
		GridData data = (GridData) socColumnsGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) socRowsGUIButton.getLayoutData();
		data.exclude = true;
		data = (GridData) socWrappedColumnsGUIButton.getLayoutData();
		data.exclude = true;

		// add SOI buttons
		soicolumnsGUIButton.setVisible(true);
		soiRowsGUIButton.setVisible(true);
		soiWrappedColumnsGUIButton.setVisible(true);
		data = (GridData) soicolumnsGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) soiRowsGUIButton.getLayoutData();
		data.exclude = false;
		data = (GridData) soiWrappedColumnsGUIButton.getLayoutData();
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
		new MainGUI().displayGUI();
	}

}