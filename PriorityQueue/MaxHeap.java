package PriorityQueue;
import java.util.ArrayList;


public class MaxHeap<T extends Comparable> implements PriorityQueueInterface<T> {
	class Node<T extends Comparable> implements Comparable<Node>
	{
		T element;
		int cnt;
		Node(T ele,int k)
		{
			this.element=ele;
			this.cnt=k;
		}
		public int compareTo(Node ele1)
		{
			if(this.element.compareTo((T)ele1.element)==0)
				return ele1.cnt-this.cnt;
			else
				return this.element.compareTo((T)ele1.element);
		}

	}
	ArrayList<Node<T>> pq=new ArrayList();
    public int heapsize()
    {
        return pq.size();
    }
	int count=0;
    @Override
    public void insert(T element) {


    	if(pq.size()==0)
    		pq.add(new Node(element,count));
    	else
    	{
    		pq.add(new Node(element,count));
    		int index=pq.size()-1;
    		while(index>=1)
    		{
    			int parent=(index-1)/2;
    			if(pq.get(index).compareTo(pq.get(parent))<=0)
    				break;
    			else
    			{
    				Node<T> swapr=pq.get(index);
    				pq.set(index,pq.get(parent));
    				pq.set(parent,swapr);
    				index=parent;
    			}
    		}
    	}
    	count++;

    }

    @Override
    public T extractMax() {
    	if(pq.size()==0)
    		return null;
    	else{
    	Node<T> max=pq.get(0);
    	if(pq.size()==1)
    		pq.remove(0);
    	else
    	{
    		Node<T> replace=pq.remove(pq.size()-1);
    		int parent=0;
    		pq.set(0,replace);
    		while(true)
    		{
    			int child1=2*parent+1;
    			int child2=2*parent+2;
    			if(child2>=pq.size())
    			{
    				if(child1>=pq.size())
    					break;
    				else
    				{
    					if(pq.get(child1).compareTo(pq.get(parent))>=0)
    					{
    						pq.set(child1,pq.set(parent,pq.get(child1)));
    						parent=child1;
    					}
    					else
    						break;
    				}
    			}
    			else
    			{
    				if(pq.get(child1).compareTo(pq.get(child2))>=0)
    				{
    					if(pq.get(child1).compareTo(pq.get(parent))>=0)
    					{
    						pq.set(child1,pq.set(parent,pq.get(child1)));
    						parent=child1;
    					}
    					else
    						break;
    				}
    				else
    				{
    					if(pq.get(child2).compareTo(pq.get(parent))>=0)
    					{
    						pq.set(child2,pq.set(parent,pq.get(child2)));
    						parent=child2;
    					}
    					else
    						break;
    				}
    			}

    		}
    	}
        return max.element;
    }
    }

}