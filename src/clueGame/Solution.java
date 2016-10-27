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
		return person.contains(s.person) && room.contains(s.room) && weapon.contains(s.weapon);
	}
}
