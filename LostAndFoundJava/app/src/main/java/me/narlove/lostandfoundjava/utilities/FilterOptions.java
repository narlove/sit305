package me.narlove.lostandfoundjava.utilities;

import java.util.ArrayList;
import java.util.Arrays;

public class FilterOptions {
    private boolean isEnabled;
    private int radius;

    public FilterOptions(boolean isEnabled, int radius) {
        this.isEnabled = isEnabled;
        this.radius = radius;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (radius < 100 || radius > 100000) throw new IllegalArgumentException("radius needs to be within bounds 100-10000");
        this.radius = radius;
    }
}
