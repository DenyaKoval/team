package com.epam.rd.spring2019.container;

import com.epam.rd.spring2019.container.exception.ContainerException;
import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class IniLoader {

    List<BeanDefinition> readBeanDefinitions(String containerDescriptionIni) {
        Wini ini = loadIniFile(containerDescriptionIni);
        return getBeanDefinitionListFromIni(ini);
    }

    private Wini loadIniFile(String contextIni) {
        try {
            return new Wini(new File(contextIni));
        } catch (IOException e) {
            throw  new ContainerException("Can't process *.ini file: " + contextIni);
        }
    }

    private List<BeanDefinition> getBeanDefinitionListFromIni(Wini ini) {
        List<BeanDefinition> beanDefinitionsList = new ArrayList<>();

        for (Map.Entry<String, Profile.Section> section : ini.entrySet()) {
            String[] sectionPair = section.getKey().split(":");
            if (sectionPair.length != 2) {
                throw new ContainerException("Wrong syntax at *.ini file");
            }

            String className = sectionPair[0];
            String beanName = sectionPair[1];

            Class<?> beanClazz;
            try {
                beanClazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new ContainerException("Can't load class: " + className);
            }

            List<BeanConstructorParameter> constructorParameters = new ArrayList<>();
            for (Map.Entry<String, String> parameterEntry : section.getValue().entrySet()) {
                String paramName = parameterEntry.getKey();
                String[] pair = parameterEntry.getValue().split(":");
                String paramType = pair[0].toUpperCase();
                String paramValue = pair[1];
                constructorParameters.add(new BeanConstructorParameter(
                        BeanConstructorParameter.Type.valueOf(paramType), paramName, paramValue));
            }
            beanDefinitionsList.add(new BeanDefinition(beanClazz, beanName, constructorParameters));
        }

        return beanDefinitionsList;
    }
}
