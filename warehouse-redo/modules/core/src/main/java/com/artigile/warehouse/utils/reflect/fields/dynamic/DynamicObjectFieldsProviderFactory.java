package com.artigile.warehouse.utils.reflect.fields.dynamic;

import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory of providers to access dynamic objects fields. Providers are registered on runtime.
 * Implemented as singleton.
 *
 * @author Aliaksandr.Chyrtsik, 18.12.12
 */
public class DynamicObjectFieldsProviderFactory {
    private static DynamicObjectFieldsProviderFactory instance = new DynamicObjectFieldsProviderFactory();

    private final Map<String, DynamicObjectFieldsProvider> providers = new ConcurrentHashMap<String, DynamicObjectFieldsProvider>();

    private DynamicObjectFieldsProviderFactory(){
    }

    public static DynamicObjectFieldsProviderFactory getInstance() {
        return instance;
    }

    /**
     * Register provider to access dynamic fields.
     *
     * @param providerName name of providers used in expressions.
     * @param provider provider instance. Providers pool is shared. So that providers are supposed to provide thread
     * safe implementations.
     */
    public void registerProvider(String providerName, DynamicObjectFieldsProvider provider) {
        synchronized (providers){
            if (providers.containsKey(providerName)){
                throw new IllegalArgumentException(MessageFormat.format("Provider with name \"{0}\" is already registered.", providerName));
            }
            providers.put(providerName, provider);
        }
    }

    /**
     * Retrieve provider with given name.
     *
     * @param providerName provider name.
     * @return instance of provider of null if no such provider was found.
     */
    public DynamicObjectFieldsProvider createProvider(String providerName) {
        return providers.get(providerName);
    }
}
