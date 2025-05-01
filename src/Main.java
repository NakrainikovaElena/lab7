public class Main {
    public static void main(String[] args) {
        Persona student = new Student();
        Persona professor = new Professor();
    }
}

interface Persona {
    void accept(Visitor visitor);
}

interface Visitor {
    void visitStudent(Student student);
    void visitProfessor(Professor professor);
}

class Student implements Persona {
    @Override
    public void accept(Visitor visitor) {
        visitor.visitStudent(this);
    }

    public void takeExam() { System.out.println("Студент сдает экзамен."); }
    public void relax() { System.out.println("Студент отдыхает."); }
}

class Professor implements Persona {
    @Override
    public void accept(Visitor visitor) {
        visitor.visitProfessor(this);
    }

    public void conductExam() { System.out.println("Профессор принимает экзамен."); }
    public void goOnVacation() { System.out.println("Профессор идет в отпуск."); }
}

