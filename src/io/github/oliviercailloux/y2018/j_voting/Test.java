package io.github.oliviercailloux.y2018.j_voting;

import java.io.IOException;
import java.util.*;

// taille : System.out.println(((LinkedHashSet<Alternative>) preferences).size());
// contains : ((LinkedHashSet) this.preferences).contains(i)

public class Test {

	public static void main (String[] args) throws IOException {
		Iterable<Alternative> preferences= new LinkedHashSet<>();
		Alternative i = new Alternative(1);
		Alternative j = new Alternative(5);
		Alternative k = new Alternative(9);
		Alternative l = new Alternative(1);
		Alternative m = new Alternative(8);
		((LinkedHashSet<Alternative>) preferences).add(i);
		((LinkedHashSet<Alternative>) preferences).add(j);
		((LinkedHashSet<Alternative>) preferences).add(k);
		((LinkedHashSet<Alternative>) preferences).add(l);
		((LinkedHashSet<Alternative>) preferences).add(m);
		((LinkedHashSet<Alternative>) preferences).add(j);
		Voter v =new Voter(1);
		StrictPreference SPreference = new StrictPreference(preferences);
		StrictProfile SProfile = new StrictProfile();
		SProfile.addProfile(v,SPreference);
		SProfile.addProfile(v,SPreference);
		List<Alternative> a=new ArrayList<Alternative>();
		a.add(i);
		a.add(j);
		a.add(k);
		a.add(l);
		a.add(m);
		toSOC soc= new toSOC();
		String s=soc.ToSOC(a,SProfile);
		System.out.println(s);
		
	}
	
	
}
