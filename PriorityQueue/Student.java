
package PriorityQueue;

public class Student implements Comparable<Student> {
    private String name;
    private Integer marks;

    public Student(String trim, int parseInt) {
        this.marks=parseInt;
        this.name=trim;

    }


    @Override
    public int compareTo(Student student) {
        return marks-student.getmarks();
    }

    public String getName() {
        return name;
    }
    public int getmarks()
    {
        return marks;
    }
    public String toString()
    {
        return "Student{name="+"\'" + this.name + "\'"+", marks=" + this.marks + "}";
    }
}
