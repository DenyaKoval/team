package com.epam.rd.spring2019.container;

class BeanConstructorParameter {
    final Type type;
    final String name;
    final String value;

    BeanConstructorParameter(Type type, String name, String value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    // REF - reference to another BEAN
    // VAL - primitive value or String
    enum Type {
        REF, VAL
    }
}
