package com.softlanding.rse.extensions.messages;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.rse.ui.subsystems.ISubSystemConfigurationAdapter;

/**
 * @see IAdapterFactory
 */
public class QueuedMessageSubSystemConfigurationAdapterFactory implements
                IAdapterFactory {

        private ISubSystemConfigurationAdapter ssConfigAdapter = new QueuedMessageSubSystemConfigurationAdapter();

        /**
         * @see IAdapterFactory#getAdapterList()
         */
        public Class[] getAdapterList()
        {
            return new Class[] {ISubSystemConfigurationAdapter.class};
        }

        /**
         * Called by our plugin's startup method to register our adaptable object types
         * with the platform. We prefer to do it here to isolate/encapsulate all factory
         * logic in this one place.
         * @param manager Platform adapter manager
         */
        public void registerWithManager(IAdapterManager manager)
        {
                manager.registerAdapters(this, QueuedMessageSubSystemFactory.class);
        }

        /**
         * @see IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
         */
        public Object getAdapter(Object adaptableObject, Class adapterType)
        {
            Object adapter = null;
            if (adaptableObject instanceof QueuedMessageSubSystemFactory)
                adapter = ssConfigAdapter;

                return adapter;
        }

}