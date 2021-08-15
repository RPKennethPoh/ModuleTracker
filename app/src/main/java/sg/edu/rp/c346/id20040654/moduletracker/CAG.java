package sg.edu.rp.c346.id20040654.moduletracker;

import java.io.Serializable;

public class CAG extends Module implements Serializable {
    private int cagId;
    private float aks, sdl, col;

    public CAG(int moduleId, int breakdown, String code, String name, int cagId, float aks, float sdl, float col) {
        super(moduleId, breakdown, code, name);
        this.cagId = cagId;
        this.aks = aks;
        this.sdl = sdl;
        this.col = col;
    }

    public CAG(int moduleId, int breakdown, String code, String name, float aks, float sdl, float col) {
        super(moduleId, breakdown, code, name);
        cagId = -1;
        this.aks = aks;
        this.sdl = sdl;
        this.col = col;
    }

    public int getCagId() {
        return cagId;
    }

    public void setCagId(int cagId) {
        this.cagId = cagId;
    }

    public float getAks() {
        return aks;
    }

    public void setAks(float aks) {
        this.aks = aks;
    }

    public float getSdl() {
        return sdl;
    }

    public void setSdl(float sdl) {
        this.sdl = sdl;
    }

    public float getCol() {
        return col;
    }

    public void setCol(float col) {
        this.col = col;
    }

    @Override
    public String toString() {
        String display = String.format("AKS: %.0f | SDL: %.0f | COL: %.0f\n", aks, sdl, col);
        int breakdown = getBreakdown();
        double total = 0;
        String grade = "";
        if(breakdown == 50) {
            total = (aks/4 * 0.5 * 100) + (sdl/4 * 0.25 * 100) + (col/4 * 0.25 * 100);
        } else {
            total = (aks/4 * 0.6 * 100) + (sdl/4 * 0.2 * 100) + (col/4 * 0.2 * 100);
        }

        if(total >= 80 && total <= 100) {
            grade = "A";
        } else if (total >= 70 && total < 80) {
            grade = "B";
        } else if (total >= 60 && total < 70) {
            grade = "C";
        } else if (total >= 50 && total < 60) {
            grade = "D";
        } else {
            grade = "F";
        }
        display += String.format("Grade: %s (%.2f%%)", grade, total);
        return display;
    }
}
