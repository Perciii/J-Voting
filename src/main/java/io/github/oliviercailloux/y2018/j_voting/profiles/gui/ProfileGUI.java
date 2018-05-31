package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import static org.eclipse.swt.events.SelectionListener.*;

import io.github.oliviercailloux.y2018.j_voting.profiles.ImmutableStrictProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.ImmutableStrictProfileTest;
import io.github.oliviercailloux.y2018.j_voting.profiles.ProfileI;
import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ProfileBuilder;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.ReadProfile;
import io.github.oliviercailloux.y2018.j_voting.profiles.management.StrictProfileBuilder;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.Alternative;
import io.github.oliviercailloux.y2018.j_voting.Preference;
import io.github.oliviercailloux.y2018.j_voting.StrictPreference;

public class ProfileGUI {
	/* 1) 
	 * get a StrictProfile (which one ? how ?) --> check that it is SOC :
	 *	if(!(strictProfile.isStrict() && strictProfile.isComplete()){
	 *		throw new IllegalArgumentException("Profile is not in SOC format");
	 *	}
	 * 2)
	 * StrictProfile --> Map ? : 
	 * Map<Voter, Preference> profile = strictProfile.getProfile()
	 * 
	 * 3)
	 * create table with size :
	 * 		width : strictProfile.getNbVoters();
	 *  		length : strictProfile.getProfile().get(1).size();
	 *
	 * 4)
	 * populate table with map :
	 * 		NavigableSet<Voter>  voters = strictProfile.getAllVoters(); --> first row of data
	 * 		for(Voter voter : voters){
	 * 			createColumn(voter) --> populate it with getPreference(voter);
	 * 		}
	 *
	 * 5)
	 * create "window" : 
	 *
	 * public static void main(String[] args) {
	 *	Display display = new Display();
	 *	Shell shell = new Shell(display);
	 *	shell.setLayout(new FillLayout());
	 *	new ProfileTableViewer(shell);
	 *	shell.open();
	 *
	 *	while (!shell.isDisposed()) {
	 *		if (!display.readAndDispatch())
	 *			display.sleep();
	 *	}
	 *
	 *	display.dispose();
	 *
	 * }
	 * 
	 */
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileGUI.class.getName());

	public static void main (String [] args) throws IOException {
		Preconditions.checkNotNull(args[0]);
		String arg = args[0];
		LOGGER.debug("Arg : {}", arg);
		

		ReadProfile rp = new ReadProfile();
		InputStream is = new ProfileGUI().getClass().getResourceAsStream(arg);
		ProfileI profileI = rp.createProfileFromStream(is);
		
		ProfileBuilder profileBuilder = new ProfileBuilder(profileI);
		
		
	
		if(profileI.isComplete() && profileI.isStrict()) {
			StrictProfile strictProfile = profileBuilder.createStrictProfile();
	
	
			Display display = new Display ();
			Shell shell = new Shell (display);
			shell.setLayout(new GridLayout());
			Table table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
			table.setLinesVisible (true);
			table.setHeaderVisible (true);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			data.heightHint = 200;
			table.setLayoutData(data);
			String[] titles = new String[strictProfile.getNbVoters()];
			int i = 0;
			Iterable<Voter> allVoters = strictProfile.getAllVoters();
			for(Voter v : allVoters){
				titles[i] = "Voter "+v.getId();
				i++;
			}
	
	
	
			for (i=0; i<titles.length; i++) {
				TableColumn column = new TableColumn (table, SWT.NONE);
				column.setText (titles[i]);
			}
	
	
	
			int nbAlternatives = strictProfile.getNbAlternatives();
			List<String> alternatives = new ArrayList<>();
			for(i=0;i<nbAlternatives;i++){
				for(Voter v : allVoters){
					alternatives.add(strictProfile.getPreference(v).getAlternative(i).toString());
					//add method to get ieme alternative of each voter in StrictProfile
				}
				
				TableItem item = new TableItem (table, SWT.NONE);
				item.setText(alternatives.toArray(new String[nbAlternatives]));	
				alternatives = new ArrayList<>();
			}
	
			for (i=0; i<titles.length; i++) {
				table.getColumn(i).pack ();
			}
	
	
			Button button = new Button(shell, SWT.PUSH);
			button.setText("Save");
			shell.setSize(300, 300);
			shell.open();
	
	
	
			//System.out.println("old "+strictProfile);
	
	
			final TableEditor editor = new TableEditor (table);
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			table.addListener (SWT.MouseDown, event -> {
				Rectangle clientArea = table.getClientArea ();
				Point pt = new Point (event.x, event.y);
				int index = table.getTopIndex ();
				while (index < table.getItemCount ()) {
					boolean visible = false;
					final TableItem item = table.getItem (index);
					for (int j=0; j<table.getColumnCount (); j++) {
						Rectangle rect = item.getBounds (j);
						if (rect.contains (pt)) {
							final int column = j;
							//System.out.println(index);
							final Text text = new Text (table, SWT.NONE);
	
							Listener textListener = e -> {
	
								List<Set<Alternative>> list3 = new ArrayList<>();
								Set<Alternative> s1 = new HashSet<>();
								
								int c = 0, ligne = Integer.parseInt(item.getText()), l=0;
								System.out.println("ligne "+ligne);
								Voter currentVoter = new Voter(1);
								for(Voter v : allVoters){
									if(c == column) {
										currentVoter = v;
										if(l == ligne) {
											System.out.println("text hashcode "+ Integer.parseInt(text.getText()));
											s1.add(new Alternative(Integer.parseInt(text.getText())));
	
	
										}
	
										else {
	
											System.out.println("alternative "+ (strictProfile.getPreference(v).getAlternative(ligne-1)));
											s1.add(strictProfile.getPreference(v).getAlternative(ligne-1));
	
										}
										l++;
	
									}
									c++;
	
								}
	
	
								list3.add(s1);
								Preference pref3 = new Preference(list3);
								profileBuilder.addVote(currentVoter, pref3);
	
	
								
	
								switch (e.type) {
								case SWT.FocusOut:
									item.setText (column, text.getText ());
	
									text.dispose ();
									break;
								case SWT.Traverse:
									switch (e.detail) {
									case SWT.TRAVERSE_RETURN:
										item.setText (column, text.getText ());
										//FALL THROUGH
									case SWT.TRAVERSE_ESCAPE:
										text.dispose ();
										e.doit = false;
									}
									break;
								}
							};
							text.addListener (SWT.FocusOut, textListener);
							text.addListener (SWT.Traverse, textListener);
							editor.setEditor (text, item, j);
							text.setText (item.getText (j));
							text.selectAll ();
							text.setFocus ();
							return;
						}
						if (!visible && rect.intersects (clientArea)) {
							visible = true;
						}
					}
					if (!visible) return;
					index++;
				}
			});
	
	
			button.addSelectionListener(new SelectionAdapter() {
	
				public void widgetSelected(SelectionEvent e) {
					
					ProfileI prof = profileBuilder.createProfileI();
					prof = prof.restrictProfile();
					
					if(prof.isComplete() && prof.isStrict()) {
						try(OutputStream outputStream = new FileOutputStream(args[0])){
							((StrictProfile) prof).writeToSOC(outputStream);
						} catch (IOException ioe) {
							MessageBox dialog = new MessageBox(shell, SWT.ICON_QUESTION | SWT.OK);
								dialog.setText("IOException");
								dialog.setMessage("Error when opening Stream : " + ioe);
								dialog.open();
						}
					
					}
				}
			});
	
	
			shell.pack ();
			shell.open ();
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			display.dispose ();
		} else {
			throw new IllegalArgumentException("Le profil spécifié n'est pas en format SOC");
		}

	}

}
