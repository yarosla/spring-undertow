Spring MVC REST Sample using Undertow Embedded Server
=====================================================

Minimalistic scaffolding for a small portable web app.

Quick start:

    gradle clean fatJar
    java -jar build/libs/spring-undertow-1.0.jar
    
Then point your browser to `http://localhost:8080/`. This should redirect 
to `http://localhost:8080/myapp/static/hello.html`, which will display simple form
that uses REST call to display 'hello' message.

How it works:
- `UndertowMain` class defines constants like `LISTENER_PORT`, etc.
- Undertow server is configured and launched from `UndertowMain.main()`
- Spring context initialized in `UndertowMain.createSpringWebAppContext()`
- properties loaded from `resources/app.properties` file
- Spring `@Configuration`s are in `ys.rest.config` package
- `@RestController`s are in `ys.rest.handlers` package
- JSON response generation using Jackson library
- static content is served from `resources/static` folder
- Spring MVC dispatcher servlet is attached in `UndertowMain.createDispatcherServlet`
- application is packaged into single jar (11Mb) to simplify delivery
