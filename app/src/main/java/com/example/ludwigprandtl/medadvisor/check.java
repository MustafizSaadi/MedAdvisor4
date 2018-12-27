package com.example.ludwigprandtl.medadvisor;

public class check  {
    String disease;
    int val;
    check(String disease,int val){
        this.disease=disease;
        this.val=val;
    }
    String getDisease(){
        return disease;
    }
    int getVal(){
        return val;
    }
}
