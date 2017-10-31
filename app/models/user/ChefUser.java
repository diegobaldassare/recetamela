package models.user;

import models.News;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "ChefUser")
public class ChefUser extends PremiumUser {

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "author")
    private List<News> news;

    public ChefUser() {}

    public ChefUser(String name, String lastName, String email, String profilePic) {
        super(name, lastName, email, profilePic);
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
