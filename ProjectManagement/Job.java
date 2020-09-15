package ProjectManagement;

public class Job implements Comparable<Job>,JobReport_ {
	String name;Project project;User user;int runtime;int completedtime;int arrival_time;int priority;
	boolean status;int count;
	Job(String name,Project project,User user,int runtime,int arr,int count)
	{
		this.name=name;
		this.project=project;
		this.user=user;
		this.runtime=runtime;
		this.status=false;
		this.completedtime=-1;
        this.arrival_time=arr;
        this.priority=this.project.priority;
        this.count=count;
	}

    @Override
    public int compareTo(Job job) {
    	if( this.priority!=job.priority)
            return this.priority-job.priority;
        else
            return job.count-this.count;
    }
    public String toString()
    {
    	String jobstatus="";
    	if(this.status)
    		jobstatus="COMPLETED";
    	else
    		jobstatus="REQUESTED";
    	String timer="";
    	if(this.completedtime==-1)
    		timer="null"; 
    	else
    		timer=Integer.toString(this.completedtime);
    	return "Job{user=\'"+this.user.name+"\'"+", project="+"\'"+this.project.name+"\'"+", jobstatus="+jobstatus+", execution_time="+this.runtime+", end_time="+timer+", name="+"\'"+this.name+"\'"+"}";
    }
    public int compareWith(Job job1)
    {
        return this.completedtime-job1.completedtime;
    }
    public int compareWith1(Job job1)
    {
    	if(this.project.compareTo(job1.project)==0)
    		return this.count-job1.count;
    	else
    		return this.project.compareTo(job1.project);
    }

    public String user() { return this.user.name; }

    public String project_name()  { return this.project.name; }

    public int budget()  { return this.project.budget; }

    public int arrival_time()  { return this.arrival_time; }

   public int completion_time() { return this.completedtime; }
}