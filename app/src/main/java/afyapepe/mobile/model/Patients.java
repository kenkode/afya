package afyapepe.mobile.model;

/**
 * Created by Elisha on 7/24/2017.
 */

import java.util.ArrayList;

public class Patients {
    private String name;
    private String thumbnailUrl;
    private String date;
    private String gender;
    private String status;

    public String getCheckPage() {
        return checkPage;
    }

    public void setCheckPage(String checkPage) {
        this.checkPage = checkPage;
    }

    private String checkPage;
    private int age,id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Patients() {

    }

    public Patients(String name, String thumbnailUrl, String date, String gender,
                 int age,int id) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.date = date;
        this.age = age;
        this.gender = gender;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
