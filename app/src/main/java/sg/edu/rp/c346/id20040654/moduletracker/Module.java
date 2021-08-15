package sg.edu.rp.c346.id20040654.moduletracker;

import java.io.Serializable;

public class Module implements Serializable {
    private int moduleId, breakdown;
    private String code, name;

    public Module(int moduleId, int breakdown, String code, String name) {
        this.moduleId = moduleId;
        this.breakdown = breakdown;
        this.code = code;
        this.name = name;
    }

    public Module(int breakdown, String code, String name) {
        moduleId = -1;
        this.breakdown = breakdown;
        this.code = code;
        this.name = name;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public int getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(int breakdown) {
        this.breakdown = breakdown;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String display = String.format("%s\n%s", code, name);
        return display;
    }
}
