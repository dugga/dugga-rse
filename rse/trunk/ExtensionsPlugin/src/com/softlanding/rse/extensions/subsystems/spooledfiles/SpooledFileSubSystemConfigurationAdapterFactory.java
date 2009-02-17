package com.softlanding.rse.extensions.subsystems.spooledfiles;

	import org.eclipse.core.runtime.IAdapterFactory;
	import org.eclipse.core.runtime.IAdapterManager;
	import org.eclipse.rse.ui.subsystems.ISubSystemConfigurationAdapter;

	/**
	 * @see IAdapterFactory
	 */
	public class SpooledFileSubSystemConfigurationAdapterFactory implements
	                IAdapterFactory {

	        private ISubSystemConfigurationAdapter ssConfigAdapter = new SpooledFileSubSystemConfigurationAdapter();

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
	                manager.registerAdapters(this, SpooledFileSubSystemFactory.class);
	        }

	        /**
	         * @see IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	         */
	        public Object getAdapter(Object adaptableObject, Class adapterType)
	        {
	            Object adapter = null;
	            if (adaptableObject instanceof SpooledFileSubSystemFactory)
	                adapter = ssConfigAdapter;

	                return adapter;
	        }

	}
