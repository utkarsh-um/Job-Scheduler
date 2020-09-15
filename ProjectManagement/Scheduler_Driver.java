package ProjectManagement;


import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import PriorityQueue.PriorityQueueDriverCode;
import PriorityQueue.*;
import Trie.*;
import RedBlack.*;
import java.util.List;

public class Scheduler_Driver extends Thread implements SchedulerInterface {
	Trie<User> users_trie;
	Trie<Project> projects_trie;
	MaxHeap<Job> jobs_heap;
	int globaltime;
	RBTree<String,Job> alljobs;
	ArrayList<Job> unfinishedjobs;
    ArrayList<Job> finishedjobs;
    ArrayList<Job> allunfinished;
    int projectcount=0;
    int jobcount=0;
Scheduler_Driver()
    {
    projects_trie=new Trie<Project>();
    jobs_heap=new MaxHeap<Job>();
    users_trie=new Trie<User>();
    globaltime=0;
    unfinishedjobs=new ArrayList<Job>();
    alljobs=new RBTree<String,Job>();
    finishedjobs=new ArrayList<Job>();
    allunfinished=new ArrayList<Job>();
}


    public static void main(String[] args) throws IOException {
//

        Scheduler_Driver scheduler_driver = new Scheduler_Driver();
        File file;
        if (args.length == 0) {
            URL url = Scheduler_Driver.class.getResource("INP");
            file = new File(url.getPath());
        } else {
            file = new File(args[0]);
        }

        scheduler_driver.execute(file);
    }

    public void execute(File commandFile) throws IOException {


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(commandFile));

            String st;
            while ((st = br.readLine()) != null) {
                String[] cmd = st.split(" ");
                if (cmd.length == 0) {
                    System.err.println("Error parsing: " + st);
                    return;
                }
                String project_name, user_name;
                Integer start_time, end_time;

                long qstart_time, qend_time;
                switch (cmd[0]) {

                    case "PROJECT":
                        handle_project(cmd);
                        break;
                    case "JOB":
                        handle_job(cmd);
                        break;
                    case "USER":
                        handle_user(cmd[1]);
                        break;
                    case "QUERY":
                        handle_query(cmd[1]);
                        break;
                    case "": // HANDLE EMPTY LINE
                        handle_empty_line();
                        break;
                    case "ADD":
                        handle_add(cmd);
                        break;
                    //--------- New Queries
                    case "NEW_PROJECT":
                    case "NEW_USER":
                    case "NEW_PROJECTUSER":
                    case "NEW_PRIORITY":
                        timed_report(cmd);
                        break;
                    case "NEW_TOP":
                    System.out.println("Top query");
                        qstart_time = System.nanoTime();
                        timed_top_consumer(Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                    case "NEW_FLUSH":
                    System.out.println("Flush query");
                        qstart_time = System.nanoTime();
                        timed_flush( Integer.parseInt(cmd[1]));
                        qend_time = System.nanoTime();
                        System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                        break;
                        default:
                        System.err.println("Unknown command: " + cmd[0]);
                }

            }

            run_to_completion();
            print_stats();

        } catch (FileNotFoundException e) {
            System.err.println("Input file Not found. " + commandFile.getAbsolutePath());
        } catch (NullPointerException ne) {
            ne.printStackTrace();

        }
    }

    @Override
    public ArrayList<JobReport_> timed_report(String[] cmd) {
        long qstart_time, qend_time;
        ArrayList<JobReport_> res = null;
        switch (cmd[0]) {
            case "NEW_PROJECT":
            System.out.println("Project query");
                qstart_time = System.nanoTime();
                res = handle_new_project(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_USER":
            	System.out.println("User query");
                qstart_time = System.nanoTime();
                res = handle_new_user(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));

                break;
            case "NEW_PROJECTUSER":
            System.out.println("Project User query");
                qstart_time = System.nanoTime();
                res = handle_new_projectuser(cmd);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
            case "NEW_PRIORITY":
            System.out.println("Priority query");
                qstart_time = System.nanoTime();
                res = handle_new_priority(cmd[1]);
                qend_time = System.nanoTime();
                System.out.println("Time elapsed (ns): " + (qend_time - qstart_time));
                break;
        }

        return res;
    }
    @Override
    public ArrayList<UserReport_> timed_top_consumer(int top) {
        ArrayList<UserReport_> arr1 =new ArrayList<UserReport_>();
        ArrayList<User> arrhelp=users_trie.allObjectsarr;
        arrhelp.sort(null);
       
        int k=0;
        while(k<top && k<arrhelp.size())
       {
          arr1.add((UserReport_)arrhelp.get(k));
         k++;
        }
        return arr1;
    }



    @Override
    public void timed_flush(int waittime) {
        int gt=globaltime;
        MaxHeap<Job> highjob=new MaxHeap<Job>();
        while(jobs_heap.heapsize()>0)
        {
            Job job1=jobs_heap.extractMax();
            if(gt-job1.arrival_time>=waittime)
            {
             if(job1.runtime>job1.project.budget)
             {
                highjob.insert(job1);
             }
              else
             {
            job1.project.budget-=job1.runtime;
            globaltime+=job1.runtime;
            job1.completedtime=globaltime;
            job1.status=true;
            job1.user.latest_job=globaltime;
            job1.user.consumed+=job1.runtime;
            finishedjobs.add(job1);
            for(int i=0;i<allunfinished.size();i++)
            {
                Job j1=allunfinished.get(i);
                if(j1==job1)
                {
                    allunfinished.remove(i);
                    break;
                }
            }
            }
        }
        else
        {
            highjob.insert(job1);
        }
    }
    jobs_heap=highjob;
}
    

    private ArrayList<JobReport_> handle_new_priority(String s) {
        int pr=Integer.parseInt(s);
        ArrayList<JobReport_> arr1=new ArrayList<JobReport_>();
        for(int i=0;i<allunfinished.size();i++)
        {   Job j1=allunfinished.get(i);
            if(j1.priority>=pr)
                arr1.add(j1);
        }
        return arr1;
    }

    private ArrayList<JobReport_> handle_new_projectuser(String[] cmd) {
    	ArrayList<JobReport_> arr1=new ArrayList<JobReport_>();
        ArrayList<Job> finarr=new ArrayList<Job>();
        ArrayList<Job> unarr=new ArrayList<Job>();
        TrieNode usr=users_trie.search(cmd[2]);
        TrieNode proj=projects_trie.search(cmd[1]);
        int t2=Integer.parseInt(cmd[3]);
        int t3=Integer.parseInt(cmd[4]);
        if(usr!=null && proj!=null)
        {
            Project project1=(Project)proj.getValue();
            User user1=(User)usr.getValue();
            for(int i=0;i<user1.jobs.size();i++)
            {   
                Job j1=user1.jobs.get(i);
                int t1=j1.arrival_time;
                if(t1>=t2 && t1<=t3 && j1.project==project1)
                    if(j1.status==true)
                    finarr.add(j1);
                else
                    unarr.add(j1);
                        
            }
        } 
        mergeSortJ(finarr,0,finarr.size()-1);  
        for(int i=0;i<finarr.size();i++)
            arr1.add((JobReport_)finarr.get(i));
        for(int i=0;i<unarr.size();i++)
            arr1.add((JobReport_)unarr.get(i));
        return arr1;
    }

    private ArrayList<JobReport_> handle_new_user(String[] cmd) {
        ArrayList<JobReport_> arr1=new ArrayList<JobReport_>();
    	TrieNode proj=users_trie.search(cmd[1]);
    	if(proj!=null)
    	{
    		User user1=(User)proj.getValue();
            int t2=Integer.parseInt(cmd[2]);
            int t3=Integer.parseInt(cmd[3]);
    		for(int i=0;i<user1.jobs.size();i++)
    		{
    			Job j1=user1.jobs.get(i);
    			int t1=j1.arrival_time;
    			if(t1>=t2 && t1<=t3)
    				arr1.add(j1);
    		}
    	}
        return arr1;
    }

    private ArrayList<JobReport_> handle_new_project(String[] cmd) {
        ArrayList<JobReport_> arr1=new ArrayList<JobReport_>();
        TrieNode proj=projects_trie.search(cmd[1]);
        if(proj!=null)
        {
            Project project1=(Project)proj.getValue();
            int t2=Integer.parseInt(cmd[2]);
            int t3=Integer.parseInt(cmd[3]);
            for(int i=0;i<project1.jobs.size();i++)
            {
                Job j1=project1.jobs.get(i);
                int t1=j1.arrival_time;
                if(t1>=t2 && t1<=t3)
                    arr1.add(j1);
            }
        }
        return arr1;
    }




    public void schedule() {
            execute_a_job();
    }

    public void run_to_completion() {
        if(jobs_heap.heapsize()==0)
        {
            System.out.println("Running code");
         System.out.println("Remaining jobs: "+jobs_heap.heapsize());
           System.out.println("System execution completed");
        }
        
        while(jobs_heap.heapsize()>0)
        {System.out.println("Running code");
         System.out.println("Remaining jobs: "+jobs_heap.heapsize());
         schedule();
           System.out.println("System execution completed");
               
      }
    }

    public void print_stats() {
    	System.out.println("--------------STATS---------------");
            System.out.println("Total jobs done: "+finishedjobs.size());
            while(finishedjobs.size()>0)
                System.out.println(finishedjobs.remove(0));
            System.out.println("------------------------");
            System.out.println("Unfinished jobs: ");
            int v=unfinishedjobs.size();
           mergeSortJ1(unfinishedjobs,0,v-1);
           int z=0;
           while(z<v){
                System.out.println(unfinishedjobs.get(z));
                z++;
                          }
            System.out.println("Total unfinished jobs: "+v);
            System.out.println("--------------STATS DONE---------------");
    }

    public void handle_add(String[] cmd) {
        System.out.println("ADDING Budget");
        TrieNode<Project> proj=projects_trie.search(cmd[1]);
        if(proj!=null)
        {Project project1=(Project)proj.getValue();
            project1.budget+=Integer.parseInt(cmd[2]);
            //ArrayList<Job> arr=new ArrayList<Job>();
            /*while(jobs_heap.heapsize()>0)
            {
                arr.add(jobs_heap.extractMax());
            }*/
            int i=0;
            while(i<unfinishedjobs.size())
            {
                if(unfinishedjobs.get(i).project==project1)
                    jobs_heap.insert(unfinishedjobs.remove(i));
                else
                    i++;
            }
        }
        else
            {
                
                return;
            }
    }

    public void handle_empty_line() {
       System.out.println("Running code");
        System.out.println("Remaining jobs: "+jobs_heap.heapsize());
        schedule();
        System.out.println("Execution cycle completed");
    }


    public void handle_query(String key) {
        System.out.println("Querying");
        List<Job> jb1=alljobs.search(key).getValues();
        if(jb1!=null)
        {
            if(jb1.get(0).status)
                System.out.println(jb1.get(0).name+": COMPLETED");
            else
                System.out.println(jb1.get(0).name+": NOT FINISHED");
        }
    else
        System.out.println(key+": NO SUCH JOB");
    }

    public void handle_user(String name) {
    	System.out.println("Creating user");
        User user1=new User(name);
        users_trie.insert(name,user1);
    }

    public void handle_job(String[] cmd) {
        System.out.println("Creating job");
        TrieNode proj=projects_trie.search(cmd[2]);
        Project project1;
        if(proj!=null)
        project1=(Project)proj.getValue();
        else
            {
                System.out.println("No such project exists. "+cmd[2]);
                return;
            }
        TrieNode usr=users_trie.search(cmd[3]);
        User user1;
         if(usr!=null)
        user1=(User)usr.getValue();
        else
           { System.out.println("No such user exists: "+cmd[3]);
       return;}
       	jobcount++;
        Job job1=new Job(cmd[1],project1,user1,Integer.parseInt(cmd[4]),globaltime,jobcount);
        user1.jobs.add(job1);
        project1.jobs.add(job1);
        jobs_heap.insert(job1);
        alljobs.insert(cmd[1],job1);
        allunfinished.add(job1);
    }

    public void handle_project(String[] cmd) {
    	System.out.println("Creating project");
    	projectcount++;
        Project project1=new Project(cmd[1],Integer.parseInt(cmd[2]),Integer.parseInt(cmd[3]),projectcount);
        projects_trie.insert(cmd[1],project1);
    }

    public void execute_a_job() {
        while(jobs_heap.heapsize()>0)
        {
        Job job1=jobs_heap.extractMax();
        System.out.println("Executing: "+job1.name+" from: "+job1.project.name);
        if(job1.runtime>job1.project.budget)
        {
            unfinishedjobs.add(job1);
            System.out.println("Un-sufficient budget.");
            //System.out.println("Execution cycle completed");
        }
        else
        {
            job1.project.budget-=job1.runtime;
            globaltime+=job1.runtime;
            job1.completedtime=globaltime;
            job1.status=true;
            job1.user.latest_job=globaltime;
            job1.user.consumed+=job1.runtime;
            System.out.println("Project: "+job1.project.name+" budget remaining: "+job1.project.budget);
            finishedjobs.add(job1);
            for(int i=0;i<allunfinished.size();i++)
            {
                Job j1=allunfinished.get(i);
                if(j1==job1)
                {
                    allunfinished.remove(i);
                    break;
                }
            }
            
            break;
        }
    }
    }


        void mergeSort(ArrayList<User> arrhelp,int f,int l)
        {
            if(f<l)
            {   int mid=(f+l)/2;
                mergeSort(arrhelp,f,mid);
                mergeSort(arrhelp,mid+1,l);
                merge_arr(arrhelp,f,mid,l);
            }

        }
        void merge_arr(ArrayList<User> arrhelp,int f,int mid,int l)
        {
            ArrayList<User> helper=new ArrayList<User>();
            int i=f,j=mid+1;
            while(i<mid+1&&j<l+1)
            {
                User usr1=arrhelp.get(i);
                User usr2=arrhelp.get(j);
                if(usr1.compareWith(usr2)>0)
                   { helper.add(usr1);
                    i++;}
                else
                    {helper.add(usr2);
                        j++;}
            }
            if(i<mid+1)
            {
                while(i<mid+1)
                {
                    helper.add(arrhelp.get(i));
                    i++;
                }
            }
            if(j<l+1)
            {
                while(j<l+1)
                {
                    helper.add(arrhelp.get(j));
                    j++;
                }
            }
            for(int k=f;k<=l;k++)
                arrhelp.set(k,helper.get(k-f));
        }


        void mergeSortJ(ArrayList<Job> arrhelp,int f,int l)
        {
            if(f<l)
            {   int mid=(f+l)/2;
                mergeSortJ(arrhelp,f,mid);
                mergeSortJ(arrhelp,mid+1,l);
                merge_arrJ(arrhelp,f,mid,l);
            }

        }
        void merge_arrJ(ArrayList<Job> arrhelp,int f,int mid,int l)
        {
            ArrayList<Job> helper=new ArrayList<Job>();
            int i=f,j=mid+1;
            while(i<mid+1&&j<l+1)
            {
                Job usr1=arrhelp.get(i);
                Job usr2=arrhelp.get(j);
                if(usr1.compareWith(usr2)<0)
                   { helper.add(usr1);
                    i++;}
                else
                    {helper.add(usr2);
                        j++;}
            }
            if(i<mid+1)
            {
                while(i<mid+1)
                {
                    helper.add(arrhelp.get(i));
                    i++;
                }
            }
            if(j<l+1)
            {
                while(j<l+1)
                {
                    helper.add(arrhelp.get(j));
                    j++;
                }
            }
            for(int k=f;k<=l;k++)
                arrhelp.set(k,helper.get(k-f));
        }


        void mergeSortJ1(ArrayList<Job> arrhelp,int f,int l)
        {
            if(f<l)
            {   int mid=(f+l)/2;
                mergeSortJ1(arrhelp,f,mid);
                mergeSortJ1(arrhelp,mid+1,l);
                merge_arrJ1(arrhelp,f,mid,l);
            }

        }
        void merge_arrJ1(ArrayList<Job> arrhelp,int f,int mid,int l)
        {
            ArrayList<Job> helper=new ArrayList<Job>();
            int i=f,j=mid+1;
            while(i<mid+1&&j<l+1)
            {
                Job usr1=arrhelp.get(i);
                Job usr2=arrhelp.get(j);
                if(usr1.compareWith1(usr2)<0)
                   { helper.add(usr1);
                    i++;}
                else
                    {helper.add(usr2);
                        j++;}
            }
            if(i<mid+1)
            {
                while(i<mid+1)
                {
                    helper.add(arrhelp.get(i));
                    i++;
                }
            }
            if(j<l+1)
            {
                while(j<l+1)
                {
                    helper.add(arrhelp.get(j));
                    j++;
                }
            }
            for(int k=f;k<=l;k++)
                arrhelp.set(k,helper.get(k-f));
        }

        public void timed_handle_user(String name) {
        User user1=new User(name);
        users_trie.insert(name,user1);
    }
    public void timed_handle_project(String[] cmd) {
    	projectcount++;
        Project project1=new Project(cmd[1],Integer.parseInt(cmd[2]),Integer.parseInt(cmd[3]),projectcount);
        projects_trie.insert(cmd[1],project1);
    }


  public void timed_handle_job(String[] cmd) {
        TrieNode proj=projects_trie.search(cmd[2]);
        Project project1;
        if(proj!=null)
        project1=(Project)proj.getValue();
        else
            {
                return;
            }
        TrieNode usr=users_trie.search(cmd[3]);
        User user1;
         if(usr!=null)
        user1=(User)usr.getValue();
        else
           { 
       return;}
       	jobcount++;
        Job job1=new Job(cmd[1],project1,user1,Integer.parseInt(cmd[4]),globaltime,jobcount);
        user1.jobs.add(job1);
        project1.jobs.add(job1);
        jobs_heap.insert(job1);
        alljobs.insert(cmd[1],job1);
        allunfinished.add(job1);
    }
       public void timed_run_to_completion() {
        
        while(jobs_heap.heapsize()>0)
        {
        while(jobs_heap.heapsize()>0)
        {
        Job job1=jobs_heap.extractMax();
        if(job1.runtime>job1.project.budget)
        {
            unfinishedjobs.add(job1);
        }
        else
        {
            job1.project.budget-=job1.runtime;
            globaltime+=job1.runtime;
            job1.completedtime=globaltime;
            job1.status=true;
            job1.user.latest_job=globaltime;
            job1.user.consumed+=job1.runtime;
            finishedjobs.add(job1);
            for(int i=0;i<allunfinished.size();i++)
            {
                Job j1=allunfinished.get(i);
                if(j1==job1)
                {
                    allunfinished.remove(i);
                    break;
                }
            }
            
            break;
        }
    }
               
      }
    }



}
