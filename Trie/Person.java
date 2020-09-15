package Trie;

public class Person {
	String name,phone_number;
    public Person(String name, String phone_number) {
    	this.name=name;
    	this.phone_number=phone_number;
    }

    public String getName() {
        return this.name;
    }
    public String toString()
    {
    	String z="[Name: "+this.name+", Phone="+this.phone_number+"]" ;
    	return z;
    }
}
