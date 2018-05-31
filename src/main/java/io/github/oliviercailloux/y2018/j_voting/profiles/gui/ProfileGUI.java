package io.github.oliviercailloux.y2018.j_voting.profiles.gui;

import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import io.github.oliviercailloux.y2018.j_voting.profiles.StrictProfile;
import io.github.oliviercailloux.y2018.j_voting.Voter;
import io.github.oliviercailloux.y2018.j_voting.Preference;

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
	
	// 1)
	public class ProfileModel {
		public Map<Voter, Preference> strictProfile;
		public ProfileModel(StrictProfile strictProfile) throws IllegalArgumentException{
			if(!(strictProfile.isStrict() && strictProfile.isComplete())){
				throw new IllegalArgumentException("Profile is not in SOC format");
			}
			this.strictProfile = strictProfile.getProfile();
		}
	}
	
	
	// 2)
	private Map<Voter, Preference> createModel(StrictProfile strictProfile) {

		return new ProfileModel(strictProfile).strictProfile;
	}
	
	
	// 3)
	public ProfileGUI(Shell shell, StrictProfile strictProfile) {
		final TableViewer v = new TableViewer(shell);
		v.setLabelProvider(new LabelProvider());
		// for demonstration purposes use custom content provider
		// alternatively you could use ArrayContentProvider.getInstance()
		v.setContentProvider(ArrayContentProvider.getInstance());
		Map<Voter, Preference> model = createModel(strictProfile);
		v.setInput(model);
		v.getTable().setLinesVisible(true);
	}
	
	
	// 4)
	
	
	
	// 5)
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		//new ProfileGUI(shell, strictProfile);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();

	}
}
