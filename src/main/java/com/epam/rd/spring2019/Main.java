package com.epam.rd.spring2019;

import com.epam.rd.spring2019.bean.Dependant;
import com.epam.rd.spring2019.bean.Module;
import com.epam.rd.spring2019.container.ApplicationCtx;

public class Main {
    public static void main(String[] args) {
        ApplicationCtx appContext = new ApplicationCtx("di-container/src/main/resources/context.ini");

        Module moduleBean = appContext.getBean("module", Module.class);
        Dependant dependantBean = appContext.getBean("dependant", Dependant.class);

        System.out.println(moduleBean);
        System.out.println(dependantBean);
    }
}
