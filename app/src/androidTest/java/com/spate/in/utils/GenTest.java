package com.spate.in.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ansal on 4/21/17.
 */
public class GenTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void convertTime24To12format() throws Exception {
        String str = Gen.convertTime24To12format("22:00");
        assertEquals(str, "10:00 PM");
    }

    @Test
    public void convertTime12To24format() throws Exception {
        String str = Gen.convertTime12To24format("10:20 am");
        assertEquals(str, "10:20");
    }

}