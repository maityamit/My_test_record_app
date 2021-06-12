package mytestbyamitmaity.example.mytestapp;

public class ExamClass {


    private String Title, ID;

    public ExamClass() {

    }

    public ExamClass(String Tttle, String ID) {
        this.Title = Tttle;
        this.ID = ID;


    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
