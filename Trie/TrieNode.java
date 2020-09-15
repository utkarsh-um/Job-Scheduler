package Trie;


import Util.NodeInterface;


public class TrieNode<T> implements NodeInterface<T> {
	boolean endofword;
	TrieNode[] alpha;
	T value;
	int level;
	char data;
	public TrieNode(char data)
	{
		this.data=data;
		endofword=false;
		level=0;
		alpha=new TrieNode[128];
	}

    @Override
    public T getValue() {
        return value;
    }


}