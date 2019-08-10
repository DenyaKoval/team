package com.epam.rd.spring2019.container;

import com.epam.rd.spring2019.container.exception.ContainerException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Sorter {
    private List<BeanDefinition> beanInitializingList = new ArrayList<>();

    Sorter(List<BeanDefinition> beanDefinitionList) {
        Map<String, BeanDefinition> appUnresolvedComponents = beanDefinitionList.stream()
                .collect(Collectors.toMap(beanDefinition -> beanDefinition.beanName, beanDefinition -> beanDefinition));

        while (appUnresolvedComponents.size() != 0) {
            int checkSize = appUnresolvedComponents.size();
            Map<String, BeanDefinition> copyAppComponents = new HashMap<>(appUnresolvedComponents);
            for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : copyAppComponents.entrySet()) {
                int dependenciesCount = 0;
                for (BeanConstructorParameter constructorParameter : beanDefinitionEntry.getValue().beanConstructorParameters) {
                    if (constructorParameter.type.equals(BeanConstructorParameter.Type.REF) &&
                            appUnresolvedComponents.containsKey(constructorParameter.value)) {
                        dependenciesCount++;
                    }
                }
                if (dependenciesCount == 0) {
                    beanInitializingList.add(beanDefinitionEntry.getValue());
                    appUnresolvedComponents.remove(beanDefinitionEntry.getKey());
                }
            }
            if (checkSize == appUnresolvedComponents.size()) {
                throw new ContainerException("Cyclic dependencies occurred");
            }
        }
    }

    List<BeanDefinition> getBeanInitializingList() {
        return beanInitializingList;
    }
}
