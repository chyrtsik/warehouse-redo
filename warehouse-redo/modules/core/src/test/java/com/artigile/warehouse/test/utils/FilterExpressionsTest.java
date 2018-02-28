package com.artigile.warehouse.test.utils;

import com.artigile.swingx.filter.DefaultWildcardSupport;
import com.artigile.swingx.filter.WildcardSupport;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for important utilities used in table reports data filtering.
 * @author Aliaksandr.Chyrtsik, 11.07.11
 */
public class FilterExpressionsTest {
    @Test
    public void testDefaultWildcardConvert(){
        WildcardSupport wildcardSupport = new DefaultWildcardSupport();
        Assert.assertEquals("ab.*cd", wildcardSupport.convert("ab*cd"));
        Assert.assertEquals("ab.+cd", wildcardSupport.convert("ab+cd"));
        Assert.assertEquals("ab.cd", wildcardSupport.convert("ab?cd"));
        Assert.assertEquals("ab \\(cd\\)", wildcardSupport.convert("ab (cd)"));
        Assert.assertEquals("ab \\[cd\\]", wildcardSupport.convert("ab [cd]"));
        Assert.assertEquals("ab \\{cd\\}", wildcardSupport.convert("ab {cd}"));
        Assert.assertEquals("ab \\.cd\\.", wildcardSupport.convert("ab .cd."));
        Assert.assertEquals("ab \\\\cd\\\\", wildcardSupport.convert("ab \\cd\\"));
        Assert.assertEquals("ab \\^cd\\^", wildcardSupport.convert("ab ^cd^"));
        Assert.assertEquals("ab \\$cd\\$", wildcardSupport.convert("ab $cd$"));
        Assert.assertEquals("ab \\*cd\\*", wildcardSupport.convert("ab \\*cd\\*"));
        Assert.assertEquals("ab \\?cd\\?", wildcardSupport.convert("ab \\?cd\\?"));
        Assert.assertEquals("ab \\+cd\\+", wildcardSupport.convert("ab \\+cd\\+"));
        Assert.assertEquals("ab \\|cd\\|", wildcardSupport.convert("ab |cd|"));
        Assert.assertEquals("\\(\\)\\{\\}\\[\\]\\.\\^\\$\\*\\?\\+\\|", wildcardSupport.convert("(){}[].^$\\*\\?\\+|"));
        Assert.assertEquals("\\(\\)\\{\\}\\[\\]\\.\\^\\$.*..+\\|", wildcardSupport.convert("(){}[].^$*?+|"));
    }
}
