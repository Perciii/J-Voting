package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import java.io.*;

public class toSOC {
	
	private String profil;
	
	public String ToSOC(List<Alternative> Alternatives, StrictProfile p) throws IOException{
		String s="";//premiere partie : nb alternatives, alternative et nb votants ...
		String strp="";// deuxieme partie : nb pers et profils
		List<StrictPreference> pref;
		//Comparator<StrictPreference> c;
		int nbVoters=0,sumVote=0,nbProfile=0,i=0,j=0;
		pref=p.getPreferences();//recupere list des choix de vote
		s=s+Integer.toString(Alternatives.size())+"\n";//nb alternatives
		for(i=0;i<Alternatives.size();i++){//id alternatives
			s+=Alternatives.get(i)+"\n";
		}
		//sort(pref,c);
		for(j=0;j<pref.size();j++){
			strp=strp+"1";
		    strp=strp+pref.get(j);
		    strp=strp+"\n";
		}
		nbVoters=j;
		sumVote=nbVoters;
		nbProfile=nbVoters;
		s=s+nbVoters+", "+sumVote+", "+nbProfile+"\n";
		profil=s+strp;
		BufferedWriter bw = new BufferedWriter(new FileWriter("profil.soc"));
        PrintWriter pWriter = new PrintWriter(bw);
        pWriter.print(profil);
        pWriter.close() ;
		return profil;
	}
}
