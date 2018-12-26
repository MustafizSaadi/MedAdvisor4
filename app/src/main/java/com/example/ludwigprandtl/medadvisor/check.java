package com.example.ludwigprandtl.medadvisor;

public class check implements Comparable<check> {
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

    @Override
    public int compareTo(check o) {
        if(this.val<o.val) return -1;
        else if(this.val>o.val) return 1;
        else
            return 0;
    }
}
