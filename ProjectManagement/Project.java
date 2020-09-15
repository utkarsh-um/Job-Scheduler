package ProjectManagement;
import java.util.ArrayList;

public class Project {
	String name;
	int priority;
	int budget;
	ArrayList<Job> jobs;
	int count;
	Project(String name,int priority,int budget,int count)
	{
		this.name=name;
		this.priority=priority;
		this.budget=budget;
		this.count=count;
		jobs=new ArrayList<Job>();
	}
	public int compareTo(Project project)
	{
		if(this.priority!=project.priority)
			return project.priority-this.priority;
		else
			return this.count-project.count;
	}
}
