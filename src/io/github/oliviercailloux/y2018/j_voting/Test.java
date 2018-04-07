package io.github.oliviercailloux.y2018.j_voting;

import java.io.IOException;
import java.util.*;

// taille : System.out.println(((LinkedHashSet<Alternative>) preferences).size());
// contains : ((LinkedHashSet) this.preferences).contains(i)

public class Test {

	public static void main (String[] args) throws IOException {
		Iterable<Alternative> preferences= new LinkedHashSet<Alternative>();
		Alternative i = new Alternative(1);
		Alternative j = new Alternative(2);
		Alternative k = new Alternative(3);
		Alternative l = new Alternative(4);
		((LinkedHashSet<Alternative>) preferences).add(i);
		((LinkedHashSet<Alternative>) preferences).add(j);
		((LinkedHashSet<Alternative>) preferences).add(k);
		((LinkedHashSet<Alternative>) preferences).add(l);
		((LinkedHashSet<Alternative>) preferences).add(k);
		Voter v =new Voter(1);
		Voter v2=new Voter(2);
		StrictPreference SPreference = new StrictPreference(preferences);
		StrictProfile SProfile = new StrictProfile();
		SProfile.addProfile(v,SPreference);
		SProfile.addProfile(v2,SPreference);
		
		String s=SProfile.toSOC();
		System.out.println("SOC profile :\n"+s);
		
	}
	
	
}
