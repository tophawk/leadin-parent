package org.leadin.analyze;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class SpringAppTests {
    @Autowired
    //private HelloService helloService;

    @Test
    public void testSayHello() {
        Assert.assertEquals("ssss12321231ssssHello world345dddddd3345355555!", null);
    }
}