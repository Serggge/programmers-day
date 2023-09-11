package dto;

import java.io.Serializable;

public class Answer implements Serializable {

    private String password;

    public Answer(String password) {
        this.password = password;
    }
}
