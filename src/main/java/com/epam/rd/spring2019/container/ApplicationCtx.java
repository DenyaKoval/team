package com.epam.rd.spring2019.container;

import com.epam.rd.spring2019.container.exception.ContainerException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationCtx {
    private Map<String, Object> context = new HashMap<>();

    public ApplicationCtx(String iniPath) {
        IniLoader iniLoader = new IniLoader();
        List<BeanDefinition> beanDefinitionList = iniLoader.readBeanDefinitions(iniPath);
        Sorter sorter = new Sorter(beanDefinitionList);
        beanDefinitionList = sorter.getBeanInitializingList();
        instantiateBeans(beanDefinitionList);
    }

    private void instantiateBeans(List<BeanDefinition> beanDefinitionList) {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            Constructor beanConstructor = extractConstructor(beanDefinition);
            List<Object> constructorParameters = new ArrayList<>();

            Map<String, BeanConstructorParameter> constructorParameterMap =
                    getConstructorParameterMap(beanDefinition.beanConstructorParameters);

            for (Parameter parameter : beanConstructor.getParameters()) {
                processParameter(constructorParameterMap, parameter, constructorParameters);
            }

            Object[] arr = constructorParameters.toArray();
            try {
                context.put(beanDefinition.beanName, beanConstructor.newInstance(arr));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ContainerException("Can't instantiate bean: " + beanDefinition.beanName);
            }
        }
    }

    private void processParameter(Map<String, BeanConstructorParameter> constructorParameterMap,
                                  Parameter parameter,
                                  List<Object> constructorParameters) {
        BeanConstructorParameter beanConstructorParameter = constructorParameterMap.get(parameter.getName());
        if (beanConstructorParameter.type.equals(BeanConstructorParameter.Type.VAL)) {
            if (parameter.getType().isPrimitive()) {
                addPrimitiveParameter(constructorParameters, parameter, beanConstructorParameter.value);
            } else {
                if (parameter.getType().equals(String.class)) {
                    constructorParameters.add(beanConstructorParameter.value);
                }
            }
        } else {
            Object constructorParameter = context.get(beanConstructorParameter.value);
            constructorParameters.add(constructorParameter);
        }
    }

    private Map<String, BeanConstructorParameter> getConstructorParameterMap(List<BeanConstructorParameter> beanConstructorParameters) {
        return beanConstructorParameters.stream()
                .collect(Collectors.toMap(beanConstructorParameter -> beanConstructorParameter.name,
                        beanConstructorParameter -> beanConstructorParameter));
    }

    private void addPrimitiveParameter(List<Object> constructorParameters, Parameter parameter, String value) {
        switch (parameter.getType().getTypeName()) {
            case "byte":
                constructorParameters.add(Byte.valueOf(value));
                break;
            case "short":
                constructorParameters.add(Short.valueOf(value));
                break;
            case "int":
                constructorParameters.add(Integer.valueOf(value));
                break;
            case "long":
                constructorParameters.add(Long.valueOf(value));
                break;
            case "float":
                constructorParameters.add(Float.valueOf(value));
                break;
            case "double":
                constructorParameters.add(Double.valueOf(value));
                break;
            case "boolean":
                constructorParameters.add(Boolean.valueOf(value));
                break;
            default:
                throw new ContainerException("Illegal primitive type name");
        }
    }

    private Constructor extractConstructor(BeanDefinition beanDefinition) {
        Constructor<?>[] constructors = beanDefinition.clazz.getConstructors();
        if (constructors.length != 1) {
            throw new ContainerException("Unsupported bean class: bean class can only have one public constructor");
        }
        return constructors[0];
    }

    public <T> T getBean(String beanName, Class<T> clazz) {
        return clazz.cast(context.get(beanName));
    }
}
