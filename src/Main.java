public class Main {
    public static void main(String[] args) {
        Persona student = new Student();
        Persona professor = new Professor();

        Visitor session = new Session();
        student.accept(session);
        professor.accept(session);

        System.out.println("---");

        Visitor vacations = new Vacations();
        student.accept(vacations);
        professor.accept(vacations);

        System.out.println("---");

        Visitor certification = new Certification();
        student.accept(certification);
        professor.accept(certification);
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
    public void getCertified() { System.out.println("Студент проходит аттестацию."); }
}

class Professor implements Persona {
    @Override
    public void accept(Visitor visitor) {
        visitor.visitProfessor(this);
    }

    public void conductExam() { System.out.println("Профессор принимает экзамен."); }
    public void goOnVacation() { System.out.println("Профессор идет в отпуск."); }
    public void carryOutCertification() { System.out.println("Профессор проводит аттестацию"); }
}

class Session implements Visitor {
    @Override
    public void visitStudent(Student student) {
        student.takeExam();
    }

    @Override
    public void visitProfessor(Professor professor) {
        professor.conductExam();
    }
}

class Vacations implements Visitor {
    @Override
    public void visitStudent(Student student) {
        student.relax();
    }

    @Override
    public void visitProfessor(Professor professor) {
        professor.goOnVacation();
    }
}

class Certification implements Visitor {
    @Override
    public void visitStudent(Student student) {
        student.getCertified();
    }

    @Override
    public void visitProfessor(Professor professor) {
        professor.carryOutCertification();
    }
}