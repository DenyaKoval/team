package com.epam.rd.spring2019.bean;

import java.util.Objects;

public class Module {
    final String moduleName;
    final String count;

    public Module(String moduleName, String count) {
        this.moduleName = moduleName;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Module{" +
                "moduleName='" + moduleName + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return moduleName.equals(module.moduleName) &&
                count.equals(module.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleName, count);
    }
}
