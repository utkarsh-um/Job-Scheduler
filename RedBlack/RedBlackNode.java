package RedBlack;

import Util.RBNodeInterface;

import java.util.List;
import java.util.ArrayList;

public class RedBlackNode<T extends Comparable, E> implements RBNodeInterface<E> {
	T name;
	List<E> list;
	char colour;
	RedBlackNode<T,E> left,right,parent;
	public RedBlackNode(T name)
	{
		this.name=name;
		list=new ArrayList<E>();
		colour='r';
		left=null;
		right = null;
		parent=null;
	}

    @Override
    public E getValue() {
        return null;
    }

    @Override
    public List<E> getValues() {
    	if(this.name!=null)
    		return this.list;
    	else
    		
    		return null;
    	
}}