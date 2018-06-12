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

public class SOCWrappedColumnsGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(SOCWrappedColumnsGUI.class.getName());

	static List<List<Set<Alternative>>> globalList = new ArrayList<>();//list of every list<set<alternative>> after modification
	static List<Voter> globalVoter = new ArrayList<>();//list of every modified voter

	final static Display display = Display.getDefault();
	final static Shell mainShell = new Shell (display, SWT.CLOSE);
	static Button edit = new Button(mainShell, SWT.PUSH);
	static Table table = new Table (mainShell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	static Integer voterToModify = null;
	static Integer alternative1 = null;
	static Integer alternative2 = null;
	static ProfileBuilder profileBuilder;
	static boolean modif = false;
	
	//TODO: change everything so that the GUI is the same as SOCColumnsGUI but Voters with the same preference are displayed in a unique column which title is the list of voters for this pref
	
	public static ProfileBuilder tableDisplay(String[] args) throws IOException {
		LOGGER.debug("tableDisplay");
		Preconditions.checkNotNull(args[0]);

		String arg = args[0];//arg is the file path
		ReadProfile rp = new ReadProfile();
		//try(InputStream is = new SOCColumnsGUI().getClass().getResourceAsStream(arg)){
		try(FileInputStream is = new FileInputStream(arg)){
			ProfileI profileI = rp.createProfileFromStream(is);
			profileBuilder = new ProfileBuilder(profileI);

			if(profileI.isComplete() && profileI.isStrict()) {

				//table layout handling
				mainShell.setLayout(new GridLayout());
				table.setLinesVisible (true);
				table.setHeaderVisible (true);
				GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
				data.heightHint = 300;
				data.widthHint = 1000;
				table.setLayoutData(data);

				createColumns();
				
				populateRows();
				
				List<String> columnTitles = createColumns();
				
				for (int i = 0 ; i < columnTitles.size() ; i++) {
					table.getColumn(i).pack(); // resize automatically the column
				}

				edit.setText("Edit");
				edit.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						edit(arg); //open edit modal
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
		StrictProfile strictProfile = profileBuilder.createStrictProfile();//if profile get from file is SOC, create a StrictProfile from it

		//Iterable<Voter> allVoters = strictProfile.getAllVoters(); //get voters from profile
		int nbUniquePreferences = strictProfile.getNbUniquePreferences();
		
		Set<Preference> uniquePreferences = strictProfile.getUniquePreferences();
		
		//COLUMNS
		List<String> titles = new ArrayList<>();
		for(Preference p : uniquePreferences){
			int nbVoters = strictProfile.getNbVoterForPreference(p);
			String voterOrVoters = (nbVoters > 1) ? " voters" : " voter";
			
			titles.add(nbVoters + voterOrVoters);
		}
		for (int i = 0 ; i < titles.size() ; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText(titles.get(i));
		}
		
		return titles;
	}

	public static void populateRows() {
		//ROWS
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		List<String> alternatives = new ArrayList<>();
		
		int nbAlternatives = strictProfile.getNbAlternatives();//nb of rows

		for(int i = 0 ; i < nbAlternatives ; i++){
			List <Alternative> ithAlternatives = strictProfile.getIthAlternatives(i); // get ith alternative of each voter
			for(Alternative alt : ithAlternatives) {
				alternatives.add(alt.toString()); // convert alternatives in the list to strings
			}

			TableItem item = new TableItem (table, SWT.NONE);
			item.setText(alternatives.toArray(new String[nbAlternatives]));	// create a row with ith alternatives
			alternatives.clear(); // empty the list
		}
	}

	public static void edit(String arg) {
		LOGGER.debug("edit");

		final Shell modalShell = new Shell(display, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.CLOSE);
		modalShell.setText("Edit");
		modalShell.setLayout(new GridLayout(2, true));

		Label label = new Label(modalShell, SWT.NULL);
		label.setText("Which voter do you want to change ?");

		final Text text = new Text(modalShell, SWT.SINGLE | SWT.BORDER);

		Label label1 = new Label(modalShell, SWT.NULL);
		label1.setText("Replace this alternative ");

		final Text text1 = new Text(modalShell, SWT.SINGLE | SWT.BORDER);

		Label label2 = new Label(modalShell, SWT.NULL);
		label2.setText("with");

		final Text text2 = new Text(modalShell, SWT.SINGLE | SWT.BORDER);

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

		text1.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					alternative1 = Integer.parseInt(text1.getText());
					save.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : {} ", iae);
					save.setEnabled(false);
				}
			}
		});

		text2.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					alternative2 = Integer.parseInt(text2.getText());
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
				LOGGER.debug("alternative1 : {} ", alternative1);
				LOGGER.debug("alternative2 : {}", alternative2);
				modif();
				save(arg);
				modalShell.dispose();
				table.removeAll();
				populateRows();
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

	public static void modif() {
		LOGGER.debug("modif");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		int nbAlternatives = strictProfile.getNbAlternatives();

		List<Alternative> list3 = new ArrayList<>();

		Voter voter = new Voter(voterToModify);

		LOGGER.debug("Voter : {}", voter.getId());
		for(int rank = 0 ; rank < nbAlternatives ; rank++) { // browse alternatives
			Alternative alternativeAtThisRank = (strictProfile.getPreference(voter).getAlternative(rank));
			if (alternativeAtThisRank.equals(new Alternative(alternative1))) { // if tested alternative = alternative to replace
				LOGGER.debug("alternative1 rank : {}", rank);
				list3.add(new Alternative(alternative2)); // replace it with the replacing one
			}
			else if (alternativeAtThisRank.equals(new Alternative(alternative2))) { // tested alternative = replacing alternative
				LOGGER.debug("alternative2 rank : {}", rank);
				list3.add(new Alternative(alternative1)); // replace it with the replaced one
			}
			else { // if tested alternative != replaced or replacing one
				list3.add(alternativeAtThisRank); // add it to its original rank
			}
		}
		// now the two alternatives are switched
		StrictPreference newPreference = new StrictPreference(list3);
		LOGGER.debug("New preference for voter v {} : {}", voter ,  newPreference.toString());

		profileBuilder.addVote(new Voter(voterToModify), newPreference);// change preference for this Voter in global ProfileBuilder

	}


	public static void save(String outputFile) {
		LOGGER.debug("save");

		StrictProfile sp = profileBuilder.createStrictProfile();
		File file = new File(outputFile);
		try(OutputStream outputStream = new FileOutputStream(file);){
			sp.writeToSOC(outputStream);
		} catch (IOException ioe) {
			MessageBox dialog = new MessageBox(mainShell, SWT.ICON_QUESTION | SWT.OK);
			dialog.setText("IOException");
			dialog.setMessage("Error when opening Stream : " + ioe);
			dialog.open();
		}
}

	public static void main (String [] args) throws IOException {
		LOGGER.debug("Main");
		//String[] s = {"profiletest.soc", ""};
		profileBuilder = tableDisplay(args);
	}

}

