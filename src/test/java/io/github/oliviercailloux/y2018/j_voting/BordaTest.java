package io.github.oliviercailloux.y2018.j_voting;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.collect.*;


public class BordaTest {

	
	@Test
	public void testgetSocietyStrictPreference(){
		ProfileI prof = ImmutableProfileITest.createIPIToTest();
		
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		List<Set<Alternative>> list1 = new ArrayList<>();
		
		Set<Alternative> s1 = new HashSet<>();
		Set<Alternative> s2 = new HashSet<>();
		
		s1.add(a1);
		s1.add(a2);
		s2.add(a3);
		
		list1.add(s1);
		list1.add(s2);		
		Preference pref1 = new Preference(list1);
		assertEquals(new Borda().getSocietyStrictPreference(prof),pref1);
	}
	
	//test getscores ? => add a getMultiSet to Borda

	
	@Test
	public void testgetMax(){
		HashMultiset<Alternative> listScores = HashMultiset.create();
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		listScores.add(a1,6);
		listScores.add(a2,5);
		listScores.add(a3,7);
		Set<Alternative> set = new HashSet<>();
		set.add(a3);
		assertTrue(Preference.alternativeSetEqual(new Borda().getMax(listScores),set));
	}
	
	
	@Test
	public void testEquals(){
		
		HashMultiset<Alternative> mset1 = HashMultiset.create();
		HashMultiset<Alternative> mset2 = HashMultiset.create();
		HashMultiset<Alternative> mset3 = HashMultiset.create();
		
		Alternative a1 = new Alternative(1);
		Alternative a2 = new Alternative(2);
		Alternative a3 = new Alternative(3);
		
		mset1.add(a1,2);
		mset1.add(a2,1);
		mset1.add(a3,5);
		
		mset2.add(a1,2);
		mset2.add(a2,1);
		mset2.add(a3,5);
		
		mset3.add(a1,1);
		mset3.add(a2,2);
		mset3.add(a3,3);
		
		
		Borda b1 = new Borda(mset1);
		Borda b2 = new Borda(mset2);
		Borda b3 = new Borda(mset3);
		
		assertTrue(b1.equals(b2));
		assertTrue(!(b1.equals(b3)));
		
	}
	

}


