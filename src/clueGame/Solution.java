package clueGame;

public class Solution {
	public String person;
	public String room;
	public String weapon;
	
	public Solution(String p, String r, String w){
		person = p;
		room = r;
		weapon = w;
	}
	
	public boolean isSame(Solution s) {
		return person == s.person && room == s.room && weapon == s.weapon;
	}
}
