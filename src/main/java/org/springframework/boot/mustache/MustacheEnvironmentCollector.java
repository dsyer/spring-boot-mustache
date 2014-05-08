package org.springframework.boot.mustache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.VariableFetcher;

public class MustacheEnvironmentCollector extends DefaultCollector implements
		EnvironmentAware {

	private ConfigurableEnvironment environment;
	private Map<String, Object> target;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
		target = new HashMap<String, Object>();
		new RelaxedDataBinder(target).bind(new PropertySourcesPropertyValues(
				this.environment.getPropertySources()));
	}

	@Override
	public Mustache.VariableFetcher createFetcher(Object ctx, String name) {
		VariableFetcher fetcher = super.createFetcher(ctx, name);
		if (fetcher != null) {
			return fetcher;
		}
		if (environment.containsProperty(name)) {
			return new VariableFetcher() {

				@Override
				public Object get(Object ctx, String name) throws Exception {
					return environment.getProperty(name);
				}

			};
		}
		if (target.containsKey(name)) {
			return new VariableFetcher() {

				@Override
				public Object get(Object ctx, String name) throws Exception {
					return target.get(name);
				}

			};
		}
		return null;
	}

}
