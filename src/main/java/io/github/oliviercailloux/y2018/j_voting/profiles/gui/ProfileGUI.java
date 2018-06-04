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

public class ProfileGUI {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileGUI.class.getName());

	//	private static int oldInt =0;//value before modification
	//private static int modif = 0;//value after modification
	static List<List<Set<Alternative>>> globalList = new ArrayList<>();//list of every list<set<alternative>> after modification
	static List<Voter> globalVoter = new ArrayList<>();//list of every modified voter

	static Display display = new Display ();
	static Shell shell = new Shell (display);
	static Shell mainShell = new Shell (display);
	static Button edit = new Button(shell, SWT.PUSH);
	static Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	static Button save = new Button(shell, SWT.PUSH);
	static Integer voterToModify = null;
	static Integer alternative1 = null;
	static Integer alternative2 = null;
	static ProfileBuilder profileBuilder;
	static boolean modif = false;
	
	public static ProfileBuilder tableDisplay(String[] args) throws IOException {
		LOGGER.debug("tableDisplay");
		Preconditions.checkNotNull(args[0]);
		
		String arg = args[0];//arg is the file path
		ReadProfile rp = new ReadProfile();
		
		try(InputStream is = new ProfileGUI().getClass().getResourceAsStream(arg)){
			ProfileI profileI = rp.createProfileFromStream(is);
			profileBuilder = new ProfileBuilder(profileI);

			if(profileI.isComplete() && profileI.isStrict()) {
				StrictProfile strictProfile = profileBuilder.createStrictProfile();//if profile get from file is SOC, create a StrictProfile from it
				
				Iterable<Voter> allVoters = strictProfile.getAllVoters(); //get voters from profile
				int i = 0, nbAlternatives = strictProfile.getNbAlternatives(); 
				
				//table layout handling
				shell.setLayout(new GridLayout());
				table.setLinesVisible (true);
				table.setHeaderVisible (true);
				GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
				data.heightHint = 300;
				data.widthHint = 1000;
				table.setLayoutData(data);

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

				//ROWS
				List<String> alternatives = new ArrayList<>();
				
				for(i = 0 ; i < nbAlternatives ; i++){
					List <Alternative> ithAlternatives = strictProfile.getIthAlternatives(i); // get ith alternative of each voter
					for(Alternative alt : ithAlternatives) {
						alternatives.add(alt.toString()); // convert alternatives in the list to strings
					}
					
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText(alternatives.toArray(new String[nbAlternatives]));	// create a row with ith alternatives
					alternatives.clear(); // empty the list
				}
				for (i = 0 ; i < titles.size() ; i++) {
					table.getColumn(i).pack(); // resize automatically the column
				}
				
				edit.setText("Edit");
				edit.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						edit();
					}
				});
				
				save.setText("Save");
				save.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						save(arg);
					}
				});
				shell.setSize(300, 300);
				shell.setText("EditSOC");
				shell.open();
			}
			return profileBuilder; // return profileBuilder containing the profile get in the read file
		}
	}


	public static void edit() {
		LOGGER.debug("edit");
		final Shell modal = new Shell(mainShell, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		modal.setText("Edit");
		modal.setLayout(new GridLayout(2, true));
		Label label = new Label(modal, SWT.NULL);
		label.setText("Which voter do you want to change ?");
		final Text text = new Text(modal, SWT.SINGLE | SWT.BORDER);
		Label label1 = new Label(modal, SWT.NULL);
		label1.setText("Replace this alternative ");
		final Text text1 = new Text(modal, SWT.SINGLE | SWT.BORDER);
		Label label2 = new Label(modal, SWT.NULL);
		label2.setText("with");
		final Text text2 = new Text(modal, SWT.SINGLE | SWT.BORDER);
		final Button buttonOK = new Button(modal, SWT.PUSH);
		buttonOK.setText("Ok");
		buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		Button buttonCancel = new Button(modal, SWT.PUSH);
		buttonCancel.setText("Cancel");
		
		text.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					voterToModify = Integer.parseInt(text.getText());
					buttonOK.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : " + iae);
					buttonOK.setEnabled(false);
				}
			}
		});
		text1.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					alternative1 = Integer.parseInt(text1.getText());
					buttonOK.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : " + iae);
					buttonOK.setEnabled(false);
				}
			}
		});
		text2.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				try {
					alternative2 = Integer.parseInt(text2.getText());
					buttonOK.setEnabled(true);
				} catch (IllegalArgumentException iae) {
					LOGGER.debug("Illegal Argument Exception : " + iae);
					buttonOK.setEnabled(false);
				}
			}
		});
		buttonOK.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				modal.dispose();
				modif();
			}
		});
		buttonCancel.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				modal.dispose();
			}
		});
		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
			}
		});
		text.setText("");
		modal.pack();
		modal.open();
		Display display = mainShell.getDisplay();
		while (!modal.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		LOGGER.debug("voterToModify : " + voterToModify);
		LOGGER.debug("alternative1 : " + alternative1);
		LOGGER.debug("alternative2 : " + alternative2);
	}

	public static void modif() {
		LOGGER.debug("modif");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		int nbAlternatives = strictProfile.getNbAlternatives();

		
		
			List<Alternative> list3 = new ArrayList<>();
			
			Voter voter = new Voter(voterToModify);
			
			LOGGER.debug("Voter : " + voter.getId());
			for(int rank = 0 ; rank < nbAlternatives ; rank++) { // browse alternatives
				Alternative alternativeAtThisRank = (strictProfile.getPreference(voter).getAlternative(rank));
				if (alternativeAtThisRank.equals(new Alternative(alternative1))) { // if tested alternative = alternative to replace
					LOGGER.debug("alternative1 rank : " + rank);
					list3.add(new Alternative(alternative2)); // replace it with the replacing one
				}
				else if (alternativeAtThisRank.equals(new Alternative(alternative2))) { // tested alternative = replacing alternative
					LOGGER.debug("alternative2 rank : " + rank);
					list3.add(new Alternative(alternative1)); // replace it with the replaced one
				}
				else { // if tested alternative != replaced or replacing one
					list3.add(alternativeAtThisRank); // add it to its original rank
				}
			}
			// now the two alternatives are switched
			
			StrictPreference newPreference = new StrictPreference(list3);
			LOGGER.debug("New preference for voter v" + voter + " : " + newPreference.toString());
			profileBuilder.addVote(new Voter(voterToModify), newPreference);// change preference for this Voter in global ProfileBuilder
	}


	public static boolean save(String outputFile) {
		LOGGER.debug("save");
		
				try {
					StrictProfile sp = profileBuilder.createStrictProfile();
					
					URL resourceUrl = ProfileGUI.class.getResource(outputFile);
					File file = new File(resourceUrl.toURI());
					try(OutputStream outputStream = new FileOutputStream(file);){
						sp.writeToSOC(outputStream);
						display.dispose();
					} catch (IOException ioe) {
						MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("IOException");
						dialog.setMessage("Error when opening Stream : " + ioe);
						dialog.open();
					}
				} catch (URISyntaxException uriSE) {
					throw new IllegalStateException(uriSE);
				}
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
		return true;
	}

	public static void main (String [] args) throws IOException {
		LOGGER.debug("Main");
		profileBuilder = tableDisplay(args);
		
		/*
		modif();
		boolean save = false;
		save = save(args);
		if(modif&&!save) {
			profileBuilder = tableDisplay(args);
		}*/
	}
}

