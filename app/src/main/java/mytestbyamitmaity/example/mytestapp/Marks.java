package mytestbyamitmaity.example.mytestapp;

public class Marks {

    private String Title, OM, FM, Date;

    public Marks() {

    }

    public Marks(String Tttle, String OM, String FM, String Date) {
        this.Title = Tttle;
        this.OM = OM;
        this.FM = FM;
        this.Date = Date;


    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getOM() {
        return OM;
    }

    public void setOM(String OM) {
        this.OM = OM;
    }

    public String getFM() {
        return FM;
    }

    public void setFM(String FM) {
        this.FM = FM;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
