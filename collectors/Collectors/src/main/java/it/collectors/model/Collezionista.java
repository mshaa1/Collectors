package it.collectors.model;

public class Collezionista {

    private final Integer id;

    private final String email;

    private final String nickname;

    public Collezionista(Integer id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

}
