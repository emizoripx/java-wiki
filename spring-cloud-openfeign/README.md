# Spring Cloud OpenFeign

## Caracteristicas
- Feign es un cliente REST declarativo
- Facilita la escritura de clientes de servicios web
- Tiene soporte de anotaciones conectable, incluidas anotaciones Feign y anotaciones JAX-RS
- Admite codificadores y decodificadores conectables
- Spring Cloud agrega soporte para anotaciones Spring MVC

> Una gran ventaja de usar Feign es que no tenemos que escribir ningún código para llamar al servicio, aparte de una definición de interfaz.

## Dependencias
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
Además, necesitaremos agregar las dependencias de spring-cloud :
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## FeignClient
- A continuación, debemos agregar `@EnableFeignClients` a nuestra clase principal:

```java
package com.mcalvaro.springcloudopenfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringCloudOpenfeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudOpenfeignApplication.class, args);
	}

}

```

Con esta anotación, habilitamos el escaneo de componentes en busca de interfaces que declaren que son clientes de Feign.
Luego declaramos un cliente Feign usando la anotación `@FeignClient` :

```java
package com.mcalvaro.springcloudopenfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "JsonClient", url = "ttps://my-json-server.typicode.com")
public interface IMyJsonClient {
 
}

```
- Además, dado que esta interfaz es un cliente de Feign, podemos usar las anotaciones de Spring Web para declarar las API a las que queremos comunicarnos.

```java

    @GetMapping("posts")
    public ResponseEntity<List<PostDto>> getAllPosts() {

        return ResponseEntity.ok(myJsonClient.getAllPosts());
    }
```

## Configuración
Ahora bien, es muy importante comprender que cada cliente de Feign se compone de un conjunto de componentes personalizables.

Spring Cloud crea un nuevo conjunto predeterminado bajo demanda para cada cliente nombrado utilizando la clase FeignClientsConfiguration que podemos personalizar como se explica en la siguiente sección.

- Creamos una clase con la anotación `@Configuration`

```java
package com.mcalvaro.springcloudopenfeign.feign.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MyJsonClientConfiguration {
 
}

```

- Lo pasamos `@FeignClient`
```java
package com.mcalvaro.springcloudopenfeign.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.mcalvaro.springcloudopenfeign.dto.PostDto;
import com.mcalvaro.springcloudopenfeign.feign.config.MyJsonClientConfiguration;

@FeignClient(value = "JsonClient", url = "https://my-json-server.typicode.com", configuration = MyJsonClientConfiguration.class)
public interface IMyJsonClient {
 
    @GetMapping("typicode/demo/posts")
    List<PostDto> getAllPosts();
}

```
**Spring Cloud OpenFeign proporciona los siguientes beans de forma predeterminada**

- `Decoder` ResponseEntityDecoder , que envuelve SpringDecoder , utilizado para decodificar la respuesta
- `Encoder` SpringEncoder  se utiliza para codificar RequestBody.
- `Logger` Slf4jLogger es el registrador predeterminado utilizado por Feign.
- `Contract` SpringMvcContract , que proporciona procesamiento de anotaciones.
- `Feign.Builder` HystrixFeign.Builder se utiliza para construir los componentes.
- `Client` LoadBalancerFeignClient o cliente Feign predeterminado.

## Configuración de Beans Personalizados
Si queremos personalizar uno o más de estos beans , podemos anularlos creando una clase de Configuración , que luego agregamos a la anotación FeignClient :

```java
package com.mcalvaro.springcloudopenfeign.feign.config;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyJsonClientConfiguration {
 
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}

```

En este ejemplo, le decimos a Feign que use OkHttpClient en lugar del predeterminado para admitir HTTP/2.

Spring Cloud OpenFeign no proporciona los siguientes beans de forma predeterminada para fingir, pero aún así busca beans de estos tipos en el contexto de la aplicación para crear el cliente fingido:
- `Logger.Level`
- `Retryer`
- `ErrorDecoder`
- `Request.Options`
- `Collection<RequestInterceptor>`
- `SetterFactory`
- `QueryMapEncoder`
- `Capability`

## Interceptores
Agregar interceptores es otra característica útil proporcionada por Feign.

Los interceptores pueden realizar una variedad de tareas implícitas, desde autenticación hasta logs, para cada solicitud/respuesta HTTP.

```java
    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {
            requestTemplate.header("api-key", "Sxmvjafoairiqwhrpltureancmaqyreqq");
            requestTemplate.header("Accept", "application/json");
        };
    }

```
### Usando BasicAuthRequestInterceptor
Alternativamente, podemos usar la clase BasicAuthRequestInterceptor que proporciona Spring Cloud OpenFeign:
```java
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("username", "password");
    }

```
 Ahora todas las solicitudes contendrán el encabezado de autenticación básico.

## Soporte Hystrix
Feign es compatible con Hystrix , por lo que si lo hemos habilitado, podemos implementar el patrón fallback
Con el patrón fallback, cuando falla una llamada de servicio remoto, en lugar de generar una excepción, el consumidor del servicio ejecutará una ruta de código alternativa para intentar llevar a cabo la acción por otros medios.
Para lograr el objetivo, necesitamos habilitar Hystrix agregando feign.hystrix.enabled=true en el archivo de propiedades.
Esto nos permite implementar métodos alternativos que se llaman cuando falla el servicio:

```java
@Component
public class MyJsonClientFallback implements IMyJsonClient {

    private final Logger logger = LoggerFactory.getLogger(MyJsonClientFallback.class);

    @Override
    public List<PostDto> getAllPosts() {

        // TODO: Habilitar hytrix
        return Collections.emptyList();
    }

}

```
## Logging
Para cada cliente de Feign, se crea un logger de forma predeterminada.

Para habilitar el log, debemos declararlo en el archivo application.propertie usando el nombre del paquete de las interfaces del cliente:
```
logging.level.com.baeldung.cloud.openfeign.client: DEBUG
```
O, si queremos habilitar el registro sólo para un cliente particular en un paquete, podemos usar el nombre completo de la clase:
```
logging.level.com.mcalvaro.cloud.openfeign.client.IMyJsonClient: DEBUG
```

Tenga en cuenta que el registro de Feign responde solo al nivel DEBUG .

El Logger.Level que podemos configurar por cliente indica cuánto registrar:
```java
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
```
Hay cuatro niveles de registro para elegir:
- `NONE` sin registro, que es el valor predeterminado
- `BASIC` registra solo el método de solicitud, la URL y el estado de la respuesta
- `HEADERS` registra la información básica junto con los encabezados de solicitud y respuesta.
- `FULL` registre el cuerpo, los encabezados y los metadatos tanto para la solicitud como para la respuesta

## Manejo de errores
El controlador de errores predeterminado de Feign, ErrorDecoder.default , siempre arroja una FeignException .

Ahora bien, este comportamiento no siempre es el más útil. Entonces, para personalizar la excepción lanzada, podemos usar un CustomErrorDecoder :

```java
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String method, Response response) {

        switch (response.status()) {

            case 400:
                return new BadRequestException("Bad Request");
            case 404:
                return new NotFoundException("Not Found");
            case 500:
                return new Exception("Internal Server Error");
            default:
                return new Exception();

        }

    }

}

```

Luego, como hicimos anteriormente, tenemos que reemplazar el ErrorDecoder predeterminado agregando un bean a la clase de Configuración :
```java
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

```
