package com.example.gruppe11_cdio.Objects;

/*
Mikkel Danielsen, s183913
Frederik Koefoed, s195463
Muhammad Talha, s195475
Volkan Isik, s180103
Lasse Strunge, s19548
Mark Mortensen, s174881
 */

public class MoveDTO {
    private boolean correct;
    private String msg;

    public MoveDTO(boolean correct, String msg) {
        this.correct = correct;
        this.msg = msg;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
