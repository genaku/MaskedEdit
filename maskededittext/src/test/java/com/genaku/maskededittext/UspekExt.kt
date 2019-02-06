package com.genaku.maskededittext

import org.junit.Assert

infix fun <T> T.eqq(expected: T) =  Assert.assertEquals(expected, this)
infix fun <T> T.notEq(expected: T) =  Assert.assertNotEquals(expected, this)