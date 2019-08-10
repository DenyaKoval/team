package com.epam.rd.spring2019.container;

import java.util.List;

class BeanDefinition {
    final Class<?> clazz;
    final String beanName;
    final List<BeanConstructorParameter> beanConstructorParameters;

    BeanDefinition(Class<?> clazz, String beanName, List<BeanConstructorParameter> beanConstructorParameters) {
        this.clazz = clazz;
        this.beanName = beanName;
        this.beanConstructorParameters = beanConstructorParameters;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "clazz=" + clazz +
                ", beanName='" + beanName + '\'' +
                ", beanConstructorParameters=" + beanConstructorParameters +
                '}';
    }
}
