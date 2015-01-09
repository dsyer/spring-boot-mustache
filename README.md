> Note: this code has moved to Spring Boot as of 1.2.2

Spring Boot autoconfig support for JMustache (logic-less
handlebar style templates).

Currently supported: rendering templates in a standalone 
app, and via a Spring MVC `View` and `ViewResolver`.

Example standalone usage:

```java
@EnableAutoConfiguration
@Configuration
public class Application implements CommandLineRunner {

	@Autowired
	private Mustache.Compiler compiler;

	public void run(String... args) {
		System.out.println(compiler.compile("Hello: {{world}}").execute(
				Collections.singletonMap("world", "World")));
	}
    
}
```

Example webapp, with template (in `classpath:/templates/home.html`):

```html
<html>
<head>
<title>{{title}}</title>
</head>
<body>
	<h2>A Message</h2>
	<div>{{message}} at {{time}}</div>
</body>
</html>
```

application code:

```java
@Configuration
@EnableAutoConfiguration
@Controller
public static class Application {

	@RequestMapping("/")
	public String home(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", "Hello World");
		model.put("title", "Hello App");
		return "home";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```

Run the app and then load the HTML page at http://localhost:8080.
