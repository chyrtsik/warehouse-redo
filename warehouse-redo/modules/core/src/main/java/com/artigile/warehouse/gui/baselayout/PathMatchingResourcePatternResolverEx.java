package com.artigile.warehouse.gui.baselayout;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

public class PathMatchingResourcePatternResolverEx extends PathMatchingResourcePatternResolver {

    public PathMatchingResourcePatternResolverEx(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public PathMatchingResourcePatternResolverEx(ClassLoader classLoader) {
        super(classLoader);
    }

    public PathMatchingResourcePatternResolverEx() {
        super();
    }

    @Override
    protected boolean isJarResource(Resource resource) throws IOException {
        return super.isJarResource(resource) || "nbjcl".equals(resource.getURL().getProtocol());
    }
}