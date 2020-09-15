package RedBlack;

public class RBTree<T extends Comparable, E> implements RBTreeInterface<T, E>  {
	RedBlackNode root;
	public RBTree()
	{
		root=null;
	}
    @Override
    public void insert(T key, E value) {
    	RedBlackNode<T,E> explorer=this.search(key);
    	if(explorer.getValues()!=null)
    	{
    		explorer.list.add(value);
    	}
    	else
    	{
    		if(root==null)
    		{
    			root=new RedBlackNode(key);
    			root.colour='b';
    			root.list.add(value);
    		}
    		else
    		{
    			RedBlackNode curr=root;
    			char ch='l';
    			boolean terminate=false;
    			while(!terminate)
    			{
    				if(curr.name.compareTo(key)>0)
    				{
    					if(curr.left==null)
    					{
    						ch='l';
    						terminate=true;
    					}
    					else
    						curr=curr.left;
    				}
    				if(curr.name.compareTo(key)<0)
    				{
    					if(curr.right==null)
    					{
    						ch='r';
    						terminate=true;
    					}
    					else
    						curr=curr.right;
    				}
    			}
    			if(curr.colour=='b')
    			{
    				RedBlackNode new1=new RedBlackNode(key);
    				new1.list.add(value);
    				if(ch=='l')
    					{
    						curr.left=new1;
    						curr.left.parent=curr;
    					}
    					if(ch=='r')
    					{
    						curr.right=new1;
    						curr.right.parent=curr;
    					}
    			}
    			else
    			{

    				RedBlackNode new1=new RedBlackNode(key);
    				new1.list.add(value);
    				RedBlackNode parent=curr.parent;
    				RedBlackNode uncle;
    				char pos;
    				if(parent.left==curr)
    				{
    					pos='l';
    					uncle=parent.right;
    				}
    				else
    				{
    					pos='r';
    					uncle=parent.left;
    				}

    				if(uncle==null)
    				{

    					restructure(parent,pos,curr,ch,new1,parent.parent);
    				}
    				else
    				{
    					if(ch=='l')
    						{new1.parent=curr;
    							curr.left=new1;
    							
    						}
    					else
    					{new1.parent=curr;
    						curr.right=new1;
    						
    					}
    						while(true)
    						{
    							parent=new1.parent;
    							if(parent.colour=='b')
    								break;
    							else if(uncle==null||uncle.colour=='b')
    							{
    								char ch1='l',pos1='l';
    								if(new1.parent.parent.left==new1.parent)
    									pos1='l';
    								else
    									pos1='r';
    								if(new1.parent.left==new1)
    									ch1='l';
    								else
    									ch1='r';
    								restructure(new1.parent.parent,pos1,new1.parent,ch1,new1,new1.parent.parent.parent);
    								break;
    							}
    							else
    							{
    								if(parent.parent==root)
    							{	parent.colour='b';
    									uncle.colour='b';
    									break;
    								}
    									else
    									{
    										parent.colour='b';
    										parent.parent.colour='r';
    										uncle.colour='b';
    										new1=parent.parent;
    										parent=new1.parent;
    										if(parent.colour=='b')
    											break;
    										else
    										{
    											if(parent.parent.left==parent)
    												uncle=parent.parent.right;
    											else
    												uncle=parent.parent.left;
    										}
    									}
    									

    							}
    						}
    				}
    			}
    		}
    	}

    }

    void restructure(RedBlackNode parent,char pos,RedBlackNode curr,char ch,RedBlackNode new1,RedBlackNode real)
    {
    	
    	RedBlackNode high,low,medium;
    	if(pos=='l'&&ch=='r')
    	{
    		high=parent;
    		medium=new1;
    		low=curr;
    		low.right=medium.left;
    		
    		if(low.right!=null)
    			
    		low.right.parent=low;
    		high.left=medium.right;
    		if(high.left!=null)
    		high.left.parent=high;
    	}
    	else if(pos=='l'&&ch=='l')
    		{
    		high=parent;
    		medium=curr;
    		low=new1;
    		high.left=medium.right;
    		if(high.left!=null)
    		high.left.parent=high;
    	}
    	else if(pos=='r'&&ch=='r')
    	{
    		high=new1;
    		medium=curr;
    		low=parent;
    		low.right=medium.left;
    		if(low.right!=null)
    		low.right.parent=low;
    	}
    	else
    	{
    		high=curr;
    		medium=new1;
    		low=parent;
    		low.right=medium.left;
    		if(low.right!=null)
    		low.right.parent=low;
    		high.left=medium.right;
    		if(high.left!=null)
    		high.left.parent=high;
    	}
    	low.colour='r';
    	high.colour='r';
    	low.parent=medium;
    	high.parent=medium;
    	medium.left=low;
    	medium.right=high;
    	medium.colour='b';
    	medium.parent=real;
    	if(real!=null)
    	{
    		if(real.left==parent)
    			real.left=medium;
    		else
    			real.right=medium;
    	}
    	else
    {		root=medium;
    	root.parent=null;}
    }








    @Override
    public RedBlackNode<T, E> search(T key) {
    	
    	RedBlackNode curr=root;
    	boolean found=false;
    	while(!found & curr!=null)
    	{	
    		if(curr.name.compareTo(key)==0)
    			found=true;
    		else if(curr.name.compareTo(key)<0)
    			curr=curr.right;
    		else
    			curr=curr.left;
    	}
    	if(found)
    		return curr;
    	else
            return new RedBlackNode<T,E>(null);
    }
    



}