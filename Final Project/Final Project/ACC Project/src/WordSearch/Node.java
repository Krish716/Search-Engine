package WordSearch;

 

import java.util.HashMap;

 

public class Node<tree> {
    char ch;
    tree value;
    HashMap<Character, Node<tree>> child;
    
    public Node() {
        this.child = new HashMap<Character, Node<tree>>();
    }
    
    public Node(char key) {
        this.ch = key;
        this.child = new HashMap<Character, Node<tree>>();
    }
}
 