package org.springframework.boot.mustache.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.mustache.SpringTemplateLoader;
import org.springframework.boot.mustache.autoconfigure.MustacheAutoConfiguration;
import org.springframework.boot.mustache.web.ApplicationTests.Application;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port:0")
@WebAppConfiguration
public class ApplicationTests {

	@Autowired
	private EmbeddedWebApplicationContext context;
	private int port;

	@Before
	public void init() {
		port = context.getEmbeddedServletContainer().getPort();
	}

	@Test
	public void contextLoads() {
		String source = "Hello {{arg}}!";
		Template tmpl = Mustache.compiler().compile(source);
		Map<String, String> context = new HashMap<String, String>();
		context.put("arg", "world");
		assertEquals("Hello world!", tmpl.execute(context)); // returns "Hello world!"
	}

	@Test
	public void testHomePage() throws Exception {
		String body = new TestRestTemplate().getForObject("http://localhost:" + port,
				String.class);
		assertTrue(body.contains("Hello World"));
	}

	@Test
	public void testPartialPage() throws Exception {
		String body = new TestRestTemplate().getForObject("http://localhost:" + port
				+ "/partial", String.class);
		assertTrue(body.contains("Hello World"));
	}

	@Configuration
	@EnableAutoConfiguration(exclude=MustacheAutoConfiguration.class)
	@Controller
	public static class Application {

		@RequestMapping("/")
		public String home(Map<String, Object> model) {
			model.put("time", new Date());
			model.put("message", "Hello World");
			model.put("title", "Hello App");
			return "home";
		}

		@RequestMapping("/partial")
		public String layout(Map<String, Object> model) {
			model.put("time", new Date());
			model.put("message", "Hello World");
			model.put("title", "Hello App");
			return "partial";
		}

		@Bean
		public MustacheViewResolver viewResolver() {
			MustacheViewResolver resolver = new MustacheViewResolver();
			resolver.setPrefix("classpath:/templates/");
			resolver.setSuffix(".html");
			resolver.setCompiler(Mustache.compiler().withLoader(
					new SpringTemplateLoader("classpath:/templates/", ".html")));
			return resolver;
		}

		public static void main(String[] args) {
			SpringApplication.run(Application.class, args);
		}

	}

}
