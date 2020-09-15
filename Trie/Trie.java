package Trie;
import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;

public class Trie<T> implements TrieInterface {
    TrieNode root;
    public ArrayList<T> allObjectsarr;
    public Trie()
    {
        root=new TrieNode(' ');
        root.endofword=true;
        allObjectsarr=new ArrayList<T>();
    }

    @Override
    public boolean delete(String word) {
        boolean found=false;
        TrieNode curr=root,parent=root,real=root;
        int i=-1,k=0;
        while(i<word.length())
        {
            if(i<word.length()-1&&curr.alpha[(int)word.charAt(i+1)]==null)
              {break;}
            else
            {
                int c=0;
                for(int j=0;j<128;j++)
                {
                    if(curr.alpha[j]!=null)
                        c++;
                }
                if(c==1 || (i==word.length()-1 && c==0))
                {
                    k++;
                }
                else
                    k=0;
                parent =curr;
                if(i<word.length()-1)
                curr=curr.alpha[(int)word.charAt(i+1)];
            }
               i++;            
        }
        if(i==word.length()&&curr.endofword==true)
            {
                found = true;
                curr.endofword=false;
                if(k>0)
                {
                    TrieNode curr1=root;
                    if(word.length()-k<=0)
                        root.alpha[(int)word.charAt(0)]=null;
                    else{
                        int m=0;
                    for(m=0;m<word.length()-k;m++)
                    {
                        curr1=curr1.alpha[(int)word.charAt(m)];
                    }
                    curr1.alpha[(int)word.charAt(m)]=null;
                    
                }

                }

            }
        
        return found;
    }

    @Override
    public TrieNode search(String word) {
        TrieNode curr=root;
        TrieNode rtn=null;
        boolean found = false;
        int i;
        for(i=0;i<word.length();i++)
        {
            if(curr.alpha[(int)word.charAt(i)]==null)
                {break;}
            else
            {
                curr=curr.alpha[(int)word.charAt(i)];
            }
        }
        if(i==word.length() && curr.endofword==true)
            found = true;
        if(found==true)
            return curr;
        else
        return null;
    }

    @Override
    public TrieNode startsWith(String prefix) {
        TrieNode curr=root;
        int k=0,i=0;
        while(i<prefix.length())
        {   
            if(curr.alpha[(int)prefix.charAt(i)]==null)
                break;
            else
            {
                curr=curr.alpha[(int)prefix.charAt(i)];
                k++;
            }
            i++;
        }

        if(i==k && k!=0)
        {
            return curr;}
        else 
            {
            return null;
        }
    }
    @Override
    public void printTrie(TrieNode trieNode) {
        
        if(trieNode.endofword==true)
            System.out.println(trieNode.getValue());
       for(int i=0;i<128;i++)
       {
            if(trieNode.alpha[i]!=null)
                printTrie(trieNode.alpha[i]);
       }
    }

    @Override
    public boolean insert(String word, Object value) {
        if(root.endofword==true)
        {
            root.alpha[(int)word.charAt(0)]=new TrieNode(word.charAt(0));
            TrieNode curr=root.alpha[(int)word.charAt(0)];
            curr.level=1;
            for(int i=1;i<word.length();i++)
            {
                curr.alpha[(int)word.charAt(i)]=new TrieNode(word.charAt(i));
                curr=curr.alpha[(int)word.charAt(i)];
                curr.level=i+1;
            }
            if(curr.endofword==true)
                return false;
            else
            {
            curr.value=value;
            curr.endofword=true;
            root.endofword=false;
            allObjectsarr.add((T)value);
        }
        }
        else
        {
            TrieNode curr=root;
            for(int i=0;i<word.length();i++)
            {
                if(curr.alpha[(int)word.charAt(i)]==null)
                {
                    curr.alpha[(int)word.charAt(i)]=new TrieNode(word.charAt(i));
                    curr=curr.alpha[(int)word.charAt(i)];
                    curr.level=i+1;
                }
                else
                {
                    curr=curr.alpha[(int)word.charAt(i)];
                }
            }
            if(curr.endofword==true)
                return false;
            else
            {
            curr.endofword=true;
            curr.value=value;
            allObjectsarr.add((T)value);
            return true;
        }
        }

        return true;
    }

    @Override
    public void printLevel(int level) {
        Queue<TrieNode> q1=new LinkedList<TrieNode>();
        int c=0;
        int curr_level=1;
        ArrayList<Character> a1=new ArrayList<Character>();
        for(int i=0;i<128;i++)
        {
            if(root.alpha[i]!=null)
            {
                c++;
                q1.add(root.alpha[i]);
            }
        }
        while(c>0)
        {
            TrieNode t1=q1.remove();
            c--;
            if(t1.level==level)
                a1.add(t1.data);
            if(t1.level>level)
                break;
            
            for(int i=0;i<128;i++)
        {

            if(t1.alpha[i]!=null)
            {
                c++;
                q1.add(t1.alpha[i]);
            }
        }
        }
                            
                a1.sort(null);
                System.out.print("Level "+level+": ");
                if(a1.size()>=1){
                for(int k=0;k<a1.size()-1;k++)
                    {
                        char ch=a1.get(k);
                        if(ch!=' ')
                            System.out.print(ch+",");

                    }
                    System.out.println(a1.get(a1.size()-1));}
                    else
                        System.out.println();
                




    }

    @Override
    public void print() {
        System.out.println("-------------");
        System.out.println("Printing Trie");
        Queue<TrieNode> q1=new LinkedList<TrieNode>();
        int c=0;
        int curr_level=1;
        ArrayList<Character> a1=new ArrayList<Character>();
        for(int i=0;i<128;i++)
        {
            if(root.alpha[i]!=null)
            {
                c++;
                q1.add(root.alpha[i]);
            }
        }
        while(c>0)
        {
            TrieNode t1=q1.remove();
            c--;
            if(t1.level==curr_level)
                a1.add(t1.data);
            else
            {
                
                a1.sort(null);
                System.out.print("Level "+curr_level+": ");
                if(a1.size()>=1){
                for(int k=0;k<a1.size()-1;k++)
                    {
                        char ch=a1.get(k);
                        if(ch!=' ')
                            System.out.print(ch+",");

                    }
                    System.out.println(a1.get(a1.size()-1));}
                    else
                        System.out.println();
                a1=new ArrayList<Character>();
                curr_level=t1.level;
                a1.add(t1.data);
            }
            for(int i=0;i<128;i++)
        {

            if(t1.alpha[i]!=null)
            {
                c++;
                q1.add(t1.alpha[i]);
            }
        }
        }
        System.out.print("Level "+curr_level+": ");
        a1.sort(null);
                if(a1.size()>=1){
                for(int k=0;k<a1.size()-1;k++)
                    {
                        char ch=a1.get(k);
                        if(ch!=' ')
                            System.out.print(ch+",");
                    }
                    System.out.println(a1.get(a1.size()-1));}
                else
                        System.out.println();
        System.out.println("Level "+(curr_level+1)+": ");
        System.out.println("-------------");
    }


ArrayList<T> arr1=new ArrayList<T>();
public void allObjectshelp(TrieNode trieNode) {
        if(trieNode.endofword==true)
            arr1.add((T)trieNode.getValue());
       for(int i=0;i<128;i++)
       {
     
            if(trieNode.alpha[i]!=null)
                allObjectshelp(trieNode.alpha[i]);
       }
    }
public ArrayList<T> allObjects()
{
    allObjectshelp(root);
    return arr1;
}





}





