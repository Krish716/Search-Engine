package WordSearch;

import java.util.HashMap;

public class Trie<tree> {
	private Node<tree> root;
	public int size;
	
	public Trie() {
		this.root = new Node<tree>();
		this.size = 0;
	}
	//insert key and values in trie
	public void insert(String s, tree value) {
		HashMap<Character, Node<tree>> children = this.root.child;
		Node<tree> node = null;
		
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			
			if (children.containsKey(c)) {
				node = children.get(c);
			} else {
				node = new Node<tree>(c);
				children.put(c,node);
			}
			
			if (i==s.length()-1) // is the end of the word
				node.value = value;
			
			children = node.child;
		}
		this.size += 1;
	}
	
	// when a word is searched and if the characters of the word are present it returns the url which has the word else returns null 
	 
	public tree search_word(String s) {
		HashMap<Character, Node<tree>> children = this.root.child;
		Node<tree> node = null;
		tree ans = null;
		
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			
			if (children.containsKey(c)) {
				node = children.get(c);
			} else {
				return null;
			}
			if (i == s.length()-1) {
				ans = node.value;
			}
			children = node.child;
		}
		return ans; // page index gets returned if exists else null
	}

}