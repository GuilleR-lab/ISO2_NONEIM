package com.example.backend.dto;

public class AuthDTO {
    
    public static class LoginRequest {
        private String username;
        private String email;
        private String password;

        //gets and sets
        public String getUsername(){
            return username;
        }

        public void setUsername(String username){
            this.username = username;
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

    //Misma que usuario tal vez eliminar...
    public static class RegisterRequest {
        private String username;
        private String surname;
        private String email;
        private String address;
        private String password;

        //gets and sets
        public String getUsername(){
            return username;
        }

        public void setUsername(String username){
            this.username = username;
        }

        public String getSurname(){
            return surname;
        }

        public void setSurname(String surname){
            this.surname = surname;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
