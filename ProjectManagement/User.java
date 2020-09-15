package ProjectManagement;
import java.util.ArrayList;
public class User implements Comparable<User>,UserReport_ {
	String name;
	int consumed;
	ArrayList<Job> jobs;
	int latest_job;
	User(String name)
	{
		this.name=name;
		this.consumed=0;
		jobs=new ArrayList<Job>();
		latest_job=-1;
	}
    @Override
    public int compareTo(User user) {
    	if(this.consumed==user.consumed)
    	{
    			return this.latest_job-user.latest_job;
    	}
    	else
    		return user.consumed-this.consumed;
    }


    public int compareWith(User user)
    {
    	if(this.consumed>user.consumed)
    		return 1;
    	else if(this.consumed==user.consumed)
    	{
    		if(this.latest_job<=user.latest_job)
    			return 1;
    		else
    			return -1;
    	}
    	else
    		return -1;
    }
    public String user()    { return this.name; }

  public int consumed() { return this.consumed; }

  public String toString()
  {
  	return "User{name="+'\''+this.name+'\''+", usage="+this.consumed+"}";
  }


}
