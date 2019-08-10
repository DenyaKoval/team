package com.epam.rd.spring2019.container;

import com.epam.rd.spring2019.bean.Dependant;
import com.epam.rd.spring2019.bean.Module;
import com.epam.rd.spring2019.container.exception.ContainerException;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

public class ApplicationCtxTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCyclicDependency() {
        // GIVEN
        String iniPath = "cyclic-context.ini";
        expectedException.expect(ContainerException.class);
        expectedException.expectMessage("Cyclic dependencies occurred");

        // WHEN
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(iniPath).getFile());
        ApplicationCtx ctx = new ApplicationCtx(file.getAbsolutePath());
    }

    @Test
    public void testGetBean() {
        // GIVEN
        String iniPath = "correct-context.ini";
        Module expectedModule = new Module("SimpleModule", "ABC");
        Dependant expectedDependant = new Dependant(expectedModule, "Dependant_NAME", 200.5);

        // WHEN
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(iniPath).getFile());
        ApplicationCtx ctx = new ApplicationCtx(file.getAbsolutePath());
        Module module = ctx.getBean("module", Module.class);
        Dependant dependant = ctx.getBean("dependant", Dependant.class);

        // THEN
        Assert.assertThat(module, Matchers.is(expectedModule));
        Assert.assertThat(dependant, Matchers.is(expectedDependant));
    }

}