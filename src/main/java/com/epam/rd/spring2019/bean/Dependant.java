package com.epam.rd.spring2019.bean;

import java.util.Objects;

public class Dependant {
    final Module module;
    final String name;
    final double intVal;

    public Dependant(Module module, String name, double intVal) {
        this.module = module;
        this.name = name;
        this.intVal = intVal;
    }

    @Override
    public String toString() {
        return "Dependant{" +
                "module=" + module +
                ", name='" + name + '\'' +
                ", intVal=" + intVal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependant dependant = (Dependant) o;
        return Double.compare(dependant.intVal, intVal) == 0 &&
                module.equals(dependant.module) &&
                name.equals(dependant.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, name, intVal);
    }
}
