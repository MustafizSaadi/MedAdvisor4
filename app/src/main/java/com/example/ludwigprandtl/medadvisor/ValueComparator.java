package com.example.ludwigprandtl.medadvisor;

import java.util.Comparator;

public class ValueComparator implements Comparator<check> {
    @Override
    public int compare(check o1, check o2) {
        if(o2.getVal()>o1.getVal()) return 1;
        else if(o2.getVal()<o1.getVal()) return -1;
        else return 0;
    }
}
