# Job-Scheduler
Data Structures &amp; Algorithms Course, IIT Delhi
# Problem Statement : 
Part 1: https://drive.google.com/file/d/1isR2QyLtPSyhbJnxVsdQIO4I-zQikKdZ/view?usp=sharing

Part 2: https://drive.google.com/file/d/1n46W59G2WYRjzpHH5ARBDe8I15FmHPQR/view?usp=sharing


# Report for my implementation :
timed_top_consumer(): From the users trie an ArrayList which contains all the inserted objects is extracted and sorted through ArrayList.sort() by overriding compareTo . Time Complexity is O(nlogn) n is number of users

timed_flush():  All jobs in heap are extracted and stored in another heap if waittime is less than required or the budget is less otherwise job is executed. Worst Case Time Complexity is O(nlogn) n is number of jobs in heap.

handle_new_priority():From a previously mantained ArrayList allunfinished(which stores all the allunfinished jobs) all those jobs which have priority greater than the input are stored a returned in a list. Time Complexity is O(total number of unfinished jobs)

handle new_user(): From the users trie the user with given name is extracted and all the jobs of the user are iterated through and required jobs are inserted in the returned list. Time Complexity is O(user_name_length+number of jobs of the user)
handle new_project(): From the projects trie the user with given name is extracted and all the jobs of the project are iterated through and required jobs are inserted in the returned list. Time Complexity is O(project_name_length+number of jobs of the project)
handle_new_projectuser():From the users trie the user with given name is extracted and all the jobs of the user are iterated through and required jobs are inserted in the returned list which have the same project. Time Complexity is O(user_name_length+project_name_length+number of jobs of the user)

all other timed functions are the counterparts of their untimed functions and only difference is they are without the print statements.

The provided test cases work on my code.

NOTE :  I am not printing priority and id in print_stats as it is not necessary as per the instructions.

---------------------------------------------------------------------------------------------------------------------------------------------------------
				README Of Assignment4 (Implementation of Trie, RedBlack Tree, Heap)
---------------------------------------------------------------------------------------------------------------------------------------------------------
TrieNode:
Attributes: value(stores the object),level(stores the level),data(stores the character),endofword(marks whether a word ends here or not),alpha[](array of 128 characters)
NOTE:As according to previous assignment details characters were in the range 0-127, in accordance with that I have used array of 128 length, Because by the time new details came by, my Trie class was over so it was difficut to change that to 33-126 at every place. Although it does not create any differnce to my program.



Trie:
insert(): Traversed down the string and added the characters already not present in the trie and marked the last character as endofword. If it was already endofword implies word was already in the trie. Time Complexity is O(max_string_length);
search(): Traversed down the string. If all characters are found and last is endofword then true else false;  Time Complexity: O(max_string_length);
startswith()/printTrie: Implemented by traversing down to the all endofwords by recursion. And printing the value stored their.	Worst Time Complexity:O(no.of_nodes*128) (same as dfs)

print,printlevel: implemented using bfs for print bfs is done on all levels and for printlevel bfs is done till the given level; Time Complexity =O(no. of nodes*128)(same as bfs)

delete():Found the maximum element from below for which all the elements belows does not have more than 1 children and further removing all these refrences and als removing endofword mark.Time Complexity:O(string_length*128) By checking all the corresponding children of all the characters of string.


MaxHeap:
To implement maxheap using fifo for same priorities: Priorities have been modified in such a way that follows the order of (i)original priority (ii)fifo:
So a new Node which is pair <T,int> is made which stores the original object as T and globalcount in int: globalcount is the count which marks how many total insertions took place before this element's insertion: So modified priority is like (i) Original priority if they are different (ii)else the one which has less globalcount is first in FIFO order. In my Node this globalcount is named as cnt.
MaxHeap implemented using ArrayList
Parent=(child-1)/2
child=2*Pareent+1||2
Insert: By inserting at the last position in arraylist and then bubbling up by comparing with parent. T(n)=T((n-1)/2)+O(1) where T(n) is time required to move the element to required place from index n. T(1)=O(1). hence time complexity is O(logn)
ExtractMax: Firstly removed the topmost element of the arraylist and then moving the last element to that position and then bubbling down by comaring with children till all settles. Similar time complexity analysis as insert instead of going back in the array we are moving ahead in the array same steps. SO timecomplexity O(logn)
Here maxheap property that every node is greater than its subtrees or its children is followed in accordance with modified priority hence maitaining FIFO for same priority as well.



REDBLACKTREE:
RedBlackNode: name(key),list,colour(stores the colour whether red or black in the constructor initialized as 'r'),parent,right,left.

Search: Normal search for a BST by comparing keys hence worst time complexity is here O(max_height) and for RedBlackTree max_height is O(logn) so search is O(logn)

Insert: Firstly root is inserted and marked as 'b'.Then all further insertions are like (i) parent is black (ii)parent is red (a)uncle is null\black (b)uncle is red
(i)Directly inserted the element at the position.
(ii)(a)Restructuring/rotation as done by restructure() function
(ii)(b)recolouring and then recurring up the tree to solve conflicts above
Worst Case Time Complexity of insertion:sum of (finding the position, restructuring, recolouring and recurring up)
Finding is O(logn) {because of height is  O(logn)}
Restructuring is O(1) and atmost 2 resturctrure so overall O(1)
Recolouring and recurring is O(1) per recolouring and O(logn) recolourings by recurring up so overall O(logn) {because of height is  O(logn)}
So worst case insertion is O(logn).

ProjectManagement:
Project:name,priority,budget
Job:name,project,user,runtime,completedtime,status; compareTo uses priority of the corresponding projects
User:name;

Main:
 Trie<Project> projects_trie : the trie string all the projects
    MaxHeap<Job> jobs_heap : the main heap storing all jobs which have neither been completed nor discarded also includes jobs added again after budget update.
    Trie<User> users_trie : stores all the users
    int globaltime : stores the globaltime.
    RBTree<String,Job> alljobs : stores all the jobs
    ArrayList<Job> unfinishedjobs : stores unfinishedjobs that have been discarded
    ArrayList<Job> finishedjobs : stores finished jobs.

schedule(): runs till (i) jobs_heap is emptied or (ii) a project is executed:
		If a job is unable to execute is removed from the heap and moved to unfinished jobs
		If a job is executed it is moved to finishedjobs after removal. Status is changed accordingly.
		Worst Case Time Complexity:O(n)
handle_empty_line: runs schedule() once Worst case Time complexity O(n)
run_to_completion:runs schedule till heap is empty : Time Complexity is O(n) that is till heap is empty.
(n is no. of jobs)

handle_query: It is done by searching in the alljobs RedBlackTree if found then check its status and print accordingly else Not Found Because RBTree search worst case time complexity is O(logn)  n is no. of jobs(all jobs)

handle_user/hande_project/handle_job : Trivial, added into respective data structures.

printstats: Trivially handled by printing from respective data structures. For unfinished jobs printing order is Priority then FIFO


handle_add: When a budget is added all the jobs from the jobs_heap is removed and stored in arraylist. And then from the unfinished jobs arraylist all jobs of this project is added to heap(jobs_heap) and then those stored in the arraylist in the above step is moved back to heap. Hence maintaning FIFO for same priority Worst Case Time complexity is O(nlogn).


All other classes like Student, Person are trivial.

