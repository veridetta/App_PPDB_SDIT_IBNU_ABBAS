package com.example.appppdbsditibnuabbas.Model;

public class Users {
    String nama, nis, email, password, kombin;
    public Users(){
    }

    public String getKombin() {
        return kombin;
    }

    public void setKombin(String kombin) {
        this.kombin = kombin;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
