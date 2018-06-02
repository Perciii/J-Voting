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
	static Shell shellM = new Shell (display);
	static Button edit = new Button(shell, SWT.PUSH);
	static Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	static Button save = new Button(shell, SWT.PUSH);
	static Integer modifVoter = null;
	static Integer alter1 = null;
	static Integer alter2 = null;
	static ProfileBuilder profileBuilder;
	static boolean modif = false;
	
	public static ProfileBuilder tableDisplay(String[] args1) throws IOException {
		LOGGER.debug("tableDisplay");
		Preconditions.checkNotNull(args1[0]);
		String arg = args1[0];//arg is the file path
		ReadProfile rp = new ReadProfile();
		try(InputStream is = new ProfileGUI().getClass().getResourceAsStream(arg)){
			ProfileI profileI = rp.createProfileFromStream(is);
			ProfileBuilder profileBuilder = new ProfileBuilder(profileI);

			if(profileI.isComplete() && profileI.isStrict()) {
				StrictProfile strictProfile = profileBuilder.createStrictProfile();
				Iterable<Voter> allVoters = strictProfile.getAllVoters();
				int i = 0,nbAlternatives = strictProfile.getNbAlternatives();
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
					for(Voter v : allVoters){
						alternatives.add(strictProfile.getPreference(v).getAlternative(i).toString());
						//add method to get ieme alternative of each voter in StrictProfile
					}
					TableItem item = new TableItem (table, SWT.NONE);
					item.setText(alternatives.toArray(new String[nbAlternatives]));	
					alternatives = new ArrayList<>();
				}
				for (i = 0 ; i < titles.size() ; i++) {
					table.getColumn(i).pack();
				}
				edit.setText("Edit");
				save.setText("Save");
				shell.setSize(300, 300);
				shell.setText("EditSOC");
				shell.open();
			}
			return profileBuilder;
		}
	}

	public static void main (String [] args) throws IOException {
		LOGGER.debug("Main");
		profileBuilder = tableDisplay(args);
		modif(args);
		boolean save = false;
		save = save(args);
	/*	if(modif&&!save) {
			profileBuilder = tableDisplay(args);
		}*/
	}

	public static void modif(String[] args) throws IOException {
		LOGGER.debug("modif");
		StrictProfile strictProfile = profileBuilder.createStrictProfile();
		int nbAlternatives = strictProfile.getNbAlternatives();

		edit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				edit();
				List<Set<Alternative>> list3 = new ArrayList<>();
				Iterable<Voter> allV = strictProfile.getAllVoters();
				Voter voter = new Voter(modifVoter);
				for(Voter v : allV){
					System.out.println("voter " +v.getId());
					if(v.equals(voter)) {
						System.out.println("true ");
						for(int l=0;l<nbAlternatives;l++) {
							if ((strictProfile.getPreference(v).getAlternative(l)).equals(new Alternative(alter1))) {
								System.out.println("alter1");
								Set<Alternative> s1 = new HashSet<>();
								s1.add(new Alternative(alter2));
								list3.add(s1);
							}
							else if ((strictProfile.getPreference(v).getAlternative(l)).equals(new Alternative(alter2))) {
								System.out.println("alter2");
								Set<Alternative> s1 = new HashSet<>();
								s1.add(new Alternative(alter1));
								list3.add(s1);
							}
							else {
								Set<Alternative> s1 = new HashSet<>();
								s1.add(strictProfile.getPreference(v).getAlternative(l));
								list3.add(s1);
							}
							System.out.println("list3 " +list3);
						}
					}
				}
				Preference pref3 = new Preference(list3);
				//System.out.println(pref3);
				profileBuilder.addVote(new Voter(modifVoter), pref3);
				StrictProfile sp = profileBuilder.createStrictProfile();
				/*Iterable<Voter> allVoters = sp.getAllVoters();
				for(Voter v : allVoters) {
					LOGGER.debug("Preference Voter {} : {}", v, sp.getPreference(v).toString());
				}*/
				try {
					//new File(getClass().getResource(args[0]).toURI())
					URL resourceUrl = getClass().getResource(args[0]);
					File file = new File(resourceUrl.toURI());
					try(OutputStream outputStream = new FileOutputStream(file);){
						sp.writeToSOC(outputStream);
						//System.out.println("writeToSOC args"+args[0]);
					//	display.dispose();
						modif = true;
					} catch (IOException ioe) {
						/*MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
						dialog.setText("IOException");
						dialog.setMessage("Error when opening Stream : " + ioe);
						dialog.open();*/
					}
				} catch (IllegalArgumentException iae) {
					/*	MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
							dialog.setText("Illegal Argument Exception");
							dialog.setMessage("New profile is not in SOC format : " + iae);
							dialog.open();*/
				} catch (URISyntaxException uriSE) {
					throw new IllegalStateException(uriSE);
				}
			}
		});

	}


	public static boolean save(String[] args) {
		LOGGER.debug("save");
		save.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					StrictProfile sp = profileBuilder.createStrictProfile();
					/*for(int i=0;i<modif;i++) {
						Preference pref3 = new Preference(globalList.get(i));
						profileBuilder.addVote(globalVoter.get(i), pref3);
					}
					Iterable<Voter> allVoters = sp.getAllVoters();
					for(Voter v : allVoters) {
						LOGGER.debug("Preference Voter {} : {}", v, sp.getPreference(v).toString());
					}
					 */
					// new File(getClass().getResource(args[0]).toURI())
					URL resourceUrl = getClass().getResource(args[0]);
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
				} catch (IllegalArgumentException iae) {
					/*	MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
							dialog.setText("Illegal Argument Exception");
							dialog.setMessage("New profile is not in SOC format : " + iae);
							dialog.open();*/
				} catch (URISyntaxException uriSE) {
					throw new IllegalStateException(uriSE);
				}
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
			/*	else {
				throw new IllegalArgumentException("Le profil spécifié n'est pas en format SOC");
			}*/
		}
		display.dispose();
		return true;
	}


	public static void edit() {
		LOGGER.debug("edit");
		final Shell shell = new Shell(shellM, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
		shell.setText("Edit");
		shell.setLayout(new GridLayout(2, true));
		Label label = new Label(shell, SWT.NULL);
		label.setText("Which voter do you want to change ?");
		final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		Label label1 = new Label(shell, SWT.NULL);
		label1.setText("Replace this alternative ");
		final Text text1 = new Text(shell, SWT.SINGLE | SWT.BORDER);
		Label label2 = new Label(shell, SWT.NULL);
		label2.setText("with");
		final Text text2 = new Text(shell, SWT.SINGLE | SWT.BORDER);
		final Button buttonOK = new Button(shell, SWT.PUSH);
		buttonOK.setText("Ok");
		buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		Button buttonCancel = new Button(shell, SWT.PUSH);
		buttonCancel.setText("Cancel");
		text.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					modifVoter = Integer.parseInt(text.getText());
					buttonOK.setEnabled(true);
				} catch (Exception e) {
					buttonOK.setEnabled(false);
				}
			}
		});
		text1.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					alter1 = Integer.parseInt(text1.getText());
					buttonOK.setEnabled(true);
				} catch (Exception e) {
					buttonOK.setEnabled(false);
				}
			}
		});
		text2.addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				try {
					alter2 = Integer.parseInt(text2.getText());
					buttonOK.setEnabled(true);
				} catch (Exception e) {
					buttonOK.setEnabled(false);
				}
			}
		});
		buttonOK.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
		buttonCancel.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
		shell.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event event) {
				if(event.detail == SWT.TRAVERSE_ESCAPE)
					event.doit = false;
			}
		});
		text.setText("");
		shell.pack();
		shell.open();
		Display display = shellM.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		System.out.println("modifVoter : "+modifVoter);
		System.out.println("alter1 : "+alter1);
		System.out.println("alter2 : "+alter2);
	}
}

