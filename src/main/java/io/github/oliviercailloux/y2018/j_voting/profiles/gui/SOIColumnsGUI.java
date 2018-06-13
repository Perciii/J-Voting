package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.slf4j.*;

import com.google.common.base.Preconditions;

import io.github.oliviercailloux.y2018.j_voting.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.*;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.*;

public class SOIColumnsGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCColumnsGUI.class.getName());

	static List<List<Set<Alternative>>> globalList = new ArrayList<>();//list of every list<set<alternative>> after modification
	static List<Voter> globalVoter = new ArrayList<>();//list of every modified voter

	final static Display display = Display.getDefault();
	final static Shell mainShell = new Shell (display, SWT.CLOSE);
	static Button edit = new Button(mainShell, SWT.PUSH);
	static Table table = new Table (mainShell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	static Integer voterToModify = null;
	public static StrictPreference newpref;
	static ProfileBuilder profileBuilder;
	static boolean modif = false;
	

	public static ProfileBuilder tableDisplaySOI(String[] args) throws IOException {
		LOGGER.debug("tableDisplay");
		Preconditions.checkNotNull(args[0]);

		String arg = args[0];//arg is the file path
		ReadProfile rp = new ReadProfile();
		try(FileInputStream is = new FileInputStream(arg)){
			ProfileI profileI = rp.createProfileFromStream(is);
			profileBuilder = new ProfileBuilder(profileI);

			if(profileI.isStrict()) {

				//table layout handling
				mainShell.setLayout(new GridLayout());
				table.setLinesVisible (true);
				table.setHeaderVisible (true);
				GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
				data.heightHint = 300;
				data.widthHint = 1000;
				table.setLayoutData(data);

				createColumns();
				
				populateRowsSOI();
				
				List<String> columnTitles = createColumns();
				
				for (int i = 0 ; i < columnTitles.size() ; i++) {
					table.getColumn(i).pack(); // resize automatically the column
				}

				edit.setText("Edit");
				edit.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						editPreference(arg); //open edit modal
					}
				});
				
				mainShell.setSize(300, 300);
				mainShell.setText("EditSOC");
				mainShell.pack();
				mainShell.open();

				while (!display.isDisposed()) {
					if (!display.readAndDispatch()) display.sleep();
				}
			}
			return profileBuilder; // return profileBuilder containing the profile get in the read file
		}
	}
	
	public static List<String> createColumns() {
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();//if profile get from file is SOI, create a StrictProfile from it

		Iterable<Voter> allVoters = strictProfile.getAllVoters(); //get voters from profile
		
		int i = 0; 
		
		//COLUMNS
		List<String> titles = new ArrayList<>();
		for(Voter v : allVoters){
			titles.add("Voter " + v.getId());
			i++;
		}
		for (i = 0 ; i < titles.size() ; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles.get(i));
		}
		
		return titles;
	}

	public static void populateRowsSOI() {
		//ROWS
		StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		List<String> alternatives = new ArrayList<>();
		
		int nbAlternatives = strictProfile.getMaxSizeOfPreference();

		for(int i = 0 ; i < nbAlternatives ; i++){
			/*List <Alternative> ithAlternatives = strictProfile.getIthAlternatives(i); // get ith alternative of each voter
			for(Alternative alt : ithAlternatives) {
				alternatives.add(alt.toString()); // convert alternatives in the list to strings
			}*/

			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(strictProfile.getIthAlternativesStrings(i).toArray(new String[nbAlternatives]));	// create a row with ith alternatives
			alternatives.clear(); // empty the list
		}
	}

	public static void editPreference(String arg) {
		LOGGER.debug("edit");

		final Shell modalShell = new Shell(display, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
		modalShell.setText("Edit");
		modalShell.setLayout(new GridLayout(2, true));

		Label label = new Label(modalShell, SWT.NULL);
		label.setText("Which voter do you want to change ?");

		final Text text = new Text(modalShell, SWT.SINGLE | SWT.BORDER);
		
		Label labelPref = new Label(modalShell, SWT.NULL);
		labelPref.setText("Choose the preference :");

		final Text textPref = new Text(modalShell, SWT.SINGLE | SWT.BORDER);
		
		final Button save = new Button(modalShell, SWT.PUSH);
		save.setText("Save");
		save.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		Button buttonCancel = new Button(modalShell, SWT.PUSH);
		buttonCancel.setText("Cancel");

		modalShell.pack();
		modalShell.open();

		text.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					voterToModify = Integer.parseInt(text.getText());
					save.setEnabled(true);
					modif = true;
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : " + iae);
					save.setEnabled(false);
				}
			}
		});

		textPref.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					newpref = new ReadProfile().getPreferences(textPref.getText());
					save.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : {} ", iae);
					save.setEnabled(false);
				}
			}
		});


		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				LOGGER.debug("voterToModify : {} ", voterToModify);
				LOGGER.debug("pref : {} ", newpref);
				modifSOI();
				save(arg);
				modalShell.dispose();
				table.removeAll();
				populateRowsSOI();
				table.setRedraw(true);
			}
		});

		buttonCancel.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				modalShell.dispose();
			}
		});

		modalShell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					modalShell.dispose();
				event.doit = false;
			}
		});

	}

	public static void modifSOI() {
		LOGGER.debug("modif");
		//StrictProfileI strictProfile = profileBuilder.createStrictProfileI();
		Voter voter = new Voter(voterToModify);
		LOGGER.debug("New preference for voter v {} : {}", voter ,  newpref);
		profileBuilder.addVote(new Voter(voterToModify), newpref);// change preference for this Voter in global ProfileBuilder
	}



	public static void save(String outputFile) {
		LOGGER.debug("save");

		StrictProfileI sp = profileBuilder.createStrictProfileI();
		File file = new File(outputFile);
		try(OutputStream outputStream = new FileOutputStream(file);){
			sp.writeToSOI(outputStream);
		} catch (IOException ioe) {
			MessageBox dialog = new MessageBox(mainShell, SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("IOException");
			dialog.setMessage("Error when opening Stream : " + ioe);
			dialog.open();
		}
}

	public static void main (String [] args) throws IOException {
		LOGGER.debug("Main");
		profileBuilder = tableDisplaySOI(args);
	}

}

