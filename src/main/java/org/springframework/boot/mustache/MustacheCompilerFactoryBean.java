/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.mustache;

import org.springframework.beans.factory.FactoryBean;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.Collector;
import com.samskivert.mustache.Mustache.Compiler;
import com.samskivert.mustache.Mustache.Escaper;
import com.samskivert.mustache.Mustache.Formatter;
import com.samskivert.mustache.Mustache.TemplateLoader;

/**
 * Factory for a Mustache compiler with custom strategies. For building a
 * <code>@Bean</code> definition in Java it probably doesn't help to use this factory
 * since the underlying fluent API is actually richer.
 * 
 * @see MustacheResourceTemplateLoader
 * 
 * @author Dave Syer
 *
 */
public class MustacheCompilerFactoryBean implements FactoryBean<Mustache.Compiler> {

	private String delims;
	private TemplateLoader templateLoader;
	private Formatter formatter;
	private Escaper escaper;
	private Collector collector;
	private Compiler compiler;

	public void setDelims(String delims) {
		this.delims = delims;
	}

	public void setTemplateLoader(TemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	public void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

	public void setEscaper(Escaper escaper) {
		this.escaper = escaper;
	}

	public void setCollector(Collector collector) {
		this.collector = collector;
	}

	@Override
	public Mustache.Compiler getObject() throws Exception {
		compiler = Mustache.compiler();
		if (delims != null) {
			compiler = compiler.withDelims(delims);
		}
		if (templateLoader != null) {
			compiler = compiler.withLoader(templateLoader);
		}
		if (formatter != null) {
			compiler = compiler.withFormatter(formatter);
		}
		if (escaper != null) {
			compiler = compiler.withEscaper(escaper);
		}
		if (collector != null) {
			compiler = compiler.withCollector(collector);
		}
		return compiler;
	}

	@Override
	public Class<?> getObjectType() {
		return Mustache.Compiler.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
