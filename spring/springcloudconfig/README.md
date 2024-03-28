# Spring Cloud Config

Spring Cloud Config proporciona soporte del lado del servidor y del lado del cliente para la configuración externalizada en un sistema distribuido.
Los conceptos tanto en el cliente como en el servidor se asignan de manera idéntica a las abstracciones Spring Environment y PropertySource, por lo que encajan muy bien con las aplicaciones Spring pero se pueden usar con cualquier aplicación que se ejecute en cualquier idioma.
A medida que una aplicación avanza por el proceso de implementación desde el desarrollo hasta la prueba y la producción, puede administrar la configuración entre esos entornos y estar seguro de que las aplicaciones tienen todo lo que necesitan para ejecutarse cuando se migran.
La implementación predeterminada del backend servidor utiliza git, por lo que admite fácilmente versiones etiquetadas de entornos de configuración, además de ser accesible a una amplia gama de herramientas para administrar el contenido.
Es fácil agregar implementaciones alternativas y conectarlas con la configuración de Spring.

## Inicio

### Servidor
La estrategia predeterminada para localizar fuentes de propiedades es clonar un repositorio git (en spring.cloud.config.server.git.uri) y usarlo para inicializar una mini SpringApplication. El entorno de la miniaplicación se utiliza para enumerar fuentes de propiedades y publicarlas en un punto final JSON.

El servidor es el que nos proveera de las configuraciones disponibles en cloud.

application.yml:
```yaml
server:
  port: 8888
spring:
  application:
    name: foo
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
```
Dependencias en pom.xml
```xml
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-server</artifactId>
		</dependency>
```
 SpringCloudConfigDemoServerApplication.java:
```java
package com.teamqr.springcloudconfigdemoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigDemoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigDemoServerApplication.class, args);
    }

}

```

El servicio HTTP tiene recursos de la siguiente forma:
```text
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```
ejemplo:

```commandline
curl localhost:8888/foo/development
curl localhost:8888/foo/development/master
curl localhost:8888/foo/development,db/master
curl localhost:8888/foo-development.yml
curl localhost:8888/foo-db.properties
curl localhost:8888/master/foo-db.properties
```

donde la application se inyecta como spring.config.name en SpringApplication (lo que normalmente es una application en una aplicación Spring Boot normal), el profile es el perfil activo (o una lista de propiedades separadas por comas) y  <b>label</b> es una etiqueta git opcional ( El valor predeterminado es master or main.)

### Cliente

El cliente se conectara al servidor para obtener las configuraciones que se requiere en la aplicación, para este caso en application.yml debe ir el siguiente contenido:
```yaml
server:
  port: 8880
spring:
  application:
    name: foo
  profiles:
    active: dev
  config:
    import: optional:configserver:http://root:s3cr3t@localhost:8888
```
Dependencias en pom.xml
```xml
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-config-client</artifactId>
		</dependency>
```
SpringCloudConfigDemoClientApplication.java:
```java
package com.teamqr.springcloudconfigdemoclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringCloudConfigDemoClientApplication {
    
    @Value("${bar:default2}")
    private String value2;

    @RequestMapping("/view")
    public String pruebaValues() {
        return String.format("Value 2 es \"%s\"", value2 );
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigDemoClientApplication.class, args);
    }

}

```

## Spring Cloud Config Server
Spring Cloud Config Server proporciona una API basada en recursos HTTP para configuración externa (pares nombre-valor o contenido YAML equivalente). El servidor se puede integrar en una aplicación Spring Boot mediante la anotación @EnableConfigServer. En consecuencia, la siguiente aplicación es un servidor de configuración:

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigServer {
  public static void main(String[] args) {
    SpringApplication.run(ConfigServer.class, args);
  }
}
```

Como todas las aplicaciones Spring Boot, se ejecuta en el puerto 8080 de forma predeterminada, pero puedes cambiarlo al puerto más convencional 8888 de varias maneras. La más sencilla, que también establece un repositorio de configuración predeterminado, es iniciarlo con spring.config.name=configserver (hay un configserver.yml en el archivo jar del Config Server). Otra es utilizar sus propias propiedades de aplicación, como se muestra en el siguiente ejemplo:

```properties
server.port: 8888
spring.cloud.config.server.git.uri: file://${user.home}/config-repo
```
donde ${user.home}/config-repo es un repositorio git que contiene archivos YAML y de propiedades.

> #### Nota:
> En Windows, necesita un "/" adicional en la URL del archivo si es absoluto con un prefijo de unidad (por ejemplo, file:///${user.home}/config-repo).

> #### Advertencia:
> La clonación inicial de su repositorio de configuración puede ser rápida y eficiente si solo guarda archivos de texto en él. Si almacena archivos binarios, especialmente los grandes, puede experimentar retrasos en la primera solicitud de configuración o encontrar errores de falta de memoria en el servidor.
 

## Repositorio de entorno

¿Dónde debería almacenar los datos de configuración del Config Server? La estrategia que gobierna este comportamiento es EnvironmentRepository, que sirve a los objetos de Environment. Este entorno es una copia superficial del dominio del entorno Spring (incluido propertySources como característica principal). Los recursos del Medio Ambiente están parametrizados por tres variables:

- {aplicación}, que se asigna a spring.application.name en el lado del cliente.
- {profile}, que se asigna a spring.profiles.active en el cliente (lista separada por comas).
- {label}, que es una característica del lado del servidor que etiqueta un conjunto "versionado" de archivos de configuración.

Las implementaciones de repositorio generalmente se comportan como una aplicación Spring Boot, cargando archivos de configuración desde spring.config.name igual al parámetro {application} y spring.profiles.active igual al parámetro {profiles}. Las reglas de precedencia para los perfiles también son las mismas que en una aplicación Spring Boot normal: los perfiles activos tienen prioridad sobre los predeterminados y, si hay varios perfiles, el último gana (similar a agregar entradas a un mapa).

### Git Backend
La implementación predeterminada de EnvironmentRepository utiliza un backend Git, que es muy conveniente para administrar actualizaciones y entornos físicos y para auditar cambios. Para cambiar la ubicación del repositorio, puede configurar la propiedad de configuración spring.cloud.config.server.git.uri en Config Server (por ejemplo, en application.yml). Si lo configura con un prefijo file:, debería funcionar desde un repositorio local para que pueda comenzar rápida y fácilmente sin un servidor. Sin embargo, en ese caso, el servidor opera directamente en el repositorio local sin clonarlo (no importa si no está vacío porque el Config Server nunca realiza cambios en el repositorio "remoto"). Para ampliar el Config Server y hacerlo altamente disponible, necesita que todas las instancias del servidor apunten al mismo repositorio, de modo que solo funcione un sistema de archivos compartido. Incluso en ese caso, es mejor usar el protocolo ssh: para un repositorio de sistema de archivos compartido, de modo que el servidor pueda clonarlo y usar una copia de trabajo local como caché.

Esta implementación de repositorio asigna el parámetro {label} del recurso HTTP a una etiqueta git (commit id, branch name, o tag). Si la rama de git o el nombre de la etiqueta contiene una barra diagonal (/), entonces la etiqueta en la URL HTTP debe especificarse con la cadena especial (_) (para evitar ambigüedades con otras rutas URL). Por ejemplo, si la etiqueta es foo/bar, reemplazar la barra diagonal dará como resultado la siguiente etiqueta: foo(_)bar. La inclusión de la cadena especial (_) también se puede aplicar al parámetro {aplicación}. Si utiliza un cliente de línea de comandos como curl, tenga cuidado con los corchetes en la URL; debe escaparlos del shell con comillas simples ('').

## Saltarse la validación del certificado SSL
La validación del servidor de configuración del certificado SSL del servidor Git se puede deshabilitar estableciendo la propiedad git.skipSslValidation en verdadero (el valor predeterminado es falso).

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          skipSslValidation: true
```

### Setting HTTP Connection Timeout

Puede configurar el tiempo, en segundos, que el servidor de configuración esperará para adquirir una conexión HTTP. Utilice la propiedad git.timeout (el valor predeterminado es 5).

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          timeout: 4
```

### Placeholders en Git URI

Spring Cloud Config Server admite una URL de repositorio git con marcadores de posición para {application} y {profile} (y {label} si lo necesita, pero recuerde que la etiqueta se aplica como una etiqueta git de todos modos). Por lo tanto, puede admitir una política de "un repositorio por aplicación" utilizando una estructura similar a la siguiente:
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/teamqr/{application}

```
También puede admitir una política de "un repositorio por perfil" utilizando un patrón similar pero con {profile}.

Además, el uso de la cadena especial "(_)" dentro de los parámetros de {aplicación} puede habilitar la compatibilidad con varias organizaciones, como se muestra en el siguiente ejemplo:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/{application}

```

### Coincidencia de patrones y repositorios múltiples

Spring Cloud Config también incluye soporte para requisitos más complejos con coincidencia de patrones en la aplicación y el nombre del perfil. El formato del patrón es una lista separada por comas de nombres de {aplicación}/{perfil} con comodines (tenga en cuenta que es posible que sea necesario citar un patrón que comienza con un comodín), como se muestra en el siguiente ejemplo:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          repos:
            simple: file://${user.home}/repos/simple
            special:
              pattern: special*/dev*,*special*/dev*
              uri: file://${user.home}/repos/special
            local:
              pattern: local*
              uri: file://${user.home}/repos/local
```

Si {application}/{profile} no coincide con ninguno de los patrones, utiliza el URI predeterminado definido en spring.cloud.config.server.git.uri. En el ejemplo anterior, para el repositorio "simple", el patrón es simple/* (solo coincide con una aplicación denominada simple en todos los perfiles). El repositorio "local" coincide con todos los nombres de aplicaciones que comienzan con local en todos los perfiles (el sufijo /* se agrega automáticamente a cualquier patrón que no tenga un comparador de perfiles).
> #### Nota:
> El atajo de “una sola línea” utilizado en el ejemplo “simple” sólo se puede utilizar si la única propiedad que se establecerá es el URI. Si necesita configurar algo más (credenciales, patrón, etc.), debe utilizar el formulario completo.

La propiedad de patrón en el repositorio es en realidad una matriz, por lo que puede usar una matriz YAML (o sufijos [0], [1], etc. en archivos de propiedades) para vincular múltiples patrones. Es posible que deba hacerlo si va a ejecutar aplicaciones con múltiples perfiles, como se muestra en el siguiente ejemplo:
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/simple/config-repo
          repos:
            development:
              pattern:
                - '*/development'
                - '*/staging'
              uri: file://${user.home}/repos/development
            staging:
              pattern:
                - '*/qa'
                - '*/production'
              uri: file://${user.home}/repos/staging
```
> #### Nota:
> Spring Cloud guesses that a pattern containing a profile that does not end in * implies that you actually want to match a list of profiles starting with this pattern (so */staging is a shortcut for ["*/staging", "*/staging,*"], and so on). This is common where, for instance, you need to run applications in the “development” profile locally but also the “cloud” profile remotely.

Opcionalmente, cada repositorio también puede almacenar archivos de configuración en subdirectorios, y los patrones para buscar esos directorios se pueden especificar como rutas de búsqueda. El siguiente ejemplo muestra un archivo de configuración en el nivel superior:
```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          search-paths:
            - foo
            - bar*
```
En el ejemplo anterior, el servidor busca archivos de configuración en el nivel superior y en el subdirectorio foo/ y también en cualquier subdirectorio cuyo nombre comience con bar.

De forma predeterminada, el servidor clona repositorios remotos cuando se solicita la configuración por primera vez. El servidor se puede configurar para clonar los repositorios al inicio, como se muestra en el siguiente ejemplo de nivel superior:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://git/common/config-repo.git
          repos:
            team-a:
                pattern: team-a-*
                cloneOnStart: true
                uri: https://git/team-a/config-repo.git
            team-b:
                pattern: team-b-*
                cloneOnStart: false
                uri: https://git/team-b/config-repo.git
            team-c:
                pattern: team-c-*
                uri: https://git/team-a/config-repo.git
```
En el ejemplo anterior, el servidor clona el repositorio de configuración del equipo a al inicio, antes de aceptar cualquier solicitud. Todos los demás repositorios no se clonan hasta que se solicita la configuración del repositorio.

> #### Nota:
> Configurar un repositorio para que se clone cuando se inicia Config Server puede ayudar a identificar rápidamente una fuente de configuración mal configurada (como un URI de repositorio no válido), mientras se inicia Config Server. Con cloneOnStart no habilitado para una fuente de configuración, Config Server puede iniciarse exitosamente con una fuente de configuración mal configurada o no válida y no detectar un error hasta que una aplicación solicite la configuración de esa fuente de configuración.

### Autenticación
Para utilizar la autenticación básica HTTP en el repositorio remoto, agregue las propiedades de nombre de usuario y contraseña por separado (no en la URL), como se muestra en el siguiente ejemplo:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          username: trolley
          password: strongpassword
```

## Authentication con AWS CodeCommit
Spring Cloud Config Server también admite la autenticación AWS CodeCommit. AWS CodeCommit utiliza un asistente de autenticación cuando utiliza Git desde la línea de comandos. Este asistente no se utiliza con la biblioteca JGit, por lo que se crea un JGit CredentialProvider para AWS CodeCommit si el URI de Git coincide con el patrón de AWS CodeCommit. Los URI de AWS CodeCommit siguen este patrón:
```yaml
uri: https://git-codecommit.${AWS_REGION}.amazonaws.com/v1/repos/${repo}

```

### Configuración de Git SSH usando propiedades
De forma predeterminada, la biblioteca JGit utilizada por Spring Cloud Config Server usa archivos de configuración SSH como ~/.ssh/known_hosts y /etc/ssh/ssh_config cuando se conecta a repositorios Git mediante un URI SSH. En entornos de nube como Cloud Foundry, el sistema de archivos local puede ser efímero o no ser fácilmente accesible. Para esos casos, la configuración SSH se puede establecer mediante propiedades de Java. Para activar la configuración SSH basada en propiedades, la propiedad spring.cloud.config.server.git.ignoreLocalSshSettings debe establecerse en verdadero, como se muestra en el siguiente ejemplo:

```yaml
  spring:
    cloud:
      config:
        server:
          git:
            uri: git@gitserver.com:team/repo1.git
            ignoreLocalSshSettings: true
            hostKey: someHostKey
            hostKeyAlgorithm: ssh-rsa
            privateKey: |
                         -----BEGIN RSA PRIVATE KEY-----
                         MIIEpgIBAAKCAQEAx4UbaDzY5xjW6hc9jwN0mX33XpTDVW9WqHp5AKaRbtAC3DqX
                         IXFMPgw3K45jxRb93f8tv9vL3rD9CUG1Gv4FM+o7ds7FRES5RTjv2RT/JVNJCoqF
                         ol8+ngLqRZCyBtQN7zYByWMRirPGoDUqdPYrj2yq+ObBBNhg5N+hOwKjjpzdj2Ud
                         1l7R+wxIqmJo1IYyy16xS8WsjyQuyC0lL456qkd5BDZ0Ag8j2X9H9D5220Ln7s9i
                         oezTipXipS7p7Jekf3Ywx6abJwOmB0rX79dV4qiNcGgzATnG1PkXxqt76VhcGa0W
                         DDVHEEYGbSQ6hIGSh0I7BQun0aLRZojfE3gqHQIDAQABAoIBAQCZmGrk8BK6tXCd
                         fY6yTiKxFzwb38IQP0ojIUWNrq0+9Xt+NsypviLHkXfXXCKKU4zUHeIGVRq5MN9b
                         BO56/RrcQHHOoJdUWuOV2qMqJvPUtC0CpGkD+valhfD75MxoXU7s3FK7yjxy3rsG
                         EmfA6tHV8/4a5umo5TqSd2YTm5B19AhRqiuUVI1wTB41DjULUGiMYrnYrhzQlVvj
                         5MjnKTlYu3V8PoYDfv1GmxPPh6vlpafXEeEYN8VB97e5x3DGHjZ5UrurAmTLTdO8
                         +AahyoKsIY612TkkQthJlt7FJAwnCGMgY6podzzvzICLFmmTXYiZ/28I4BX/mOSe
                         pZVnfRixAoGBAO6Uiwt40/PKs53mCEWngslSCsh9oGAaLTf/XdvMns5VmuyyAyKG
                         ti8Ol5wqBMi4GIUzjbgUvSUt+IowIrG3f5tN85wpjQ1UGVcpTnl5Qo9xaS1PFScQ
                         xrtWZ9eNj2TsIAMp/svJsyGG3OibxfnuAIpSXNQiJPwRlW3irzpGgVx/AoGBANYW
                         dnhshUcEHMJi3aXwR12OTDnaLoanVGLwLnkqLSYUZA7ZegpKq90UAuBdcEfgdpyi
                         PhKpeaeIiAaNnFo8m9aoTKr+7I6/uMTlwrVnfrsVTZv3orxjwQV20YIBCVRKD1uX
                         VhE0ozPZxwwKSPAFocpyWpGHGreGF1AIYBE9UBtjAoGBAI8bfPgJpyFyMiGBjO6z
                         FwlJc/xlFqDusrcHL7abW5qq0L4v3R+FrJw3ZYufzLTVcKfdj6GelwJJO+8wBm+R
                         gTKYJItEhT48duLIfTDyIpHGVm9+I1MGhh5zKuCqIhxIYr9jHloBB7kRm0rPvYY4
                         VAykcNgyDvtAVODP+4m6JvhjAoGBALbtTqErKN47V0+JJpapLnF0KxGrqeGIjIRV
                         cYA6V4WYGr7NeIfesecfOC356PyhgPfpcVyEztwlvwTKb3RzIT1TZN8fH4YBr6Ee
                         KTbTjefRFhVUjQqnucAvfGi29f+9oE3Ei9f7wA+H35ocF6JvTYUsHNMIO/3gZ38N
                         CPjyCMa9AoGBAMhsITNe3QcbsXAbdUR00dDsIFVROzyFJ2m40i4KCRM35bC/BIBs
                         q0TY3we+ERB40U8Z2BvU61QuwaunJ2+uGadHo58VSVdggqAo0BSkH58innKKt96J
                         69pcVH/4rmLbXdcmNYGm6iu+MlPQk4BUZknHSmVHIFdJ0EPupVaQ8RHT
                         -----END RSA PRIVATE KEY-----
```

### Eliminar ramas sin seguimiento en repositorios Git

Como Spring Cloud Config Server tiene un clon del repositorio git remoto después de verificar la rama en el repositorio local (por ejemplo, buscar propiedades por etiqueta), mantendrá esta rama para siempre o hasta el próximo reinicio del servidor (lo que crea un nuevo repositorio local). Por lo tanto, podría darse el caso de que se elimine la rama remota pero la copia local todavía esté disponible para recuperarse. Y si el servicio de cliente Spring Cloud Config Server comienza con --spring.cloud.config.label=deletedRemoteBranch,master, obtendrá propiedades de la rama local eliminadoRemoteBranch, pero no de master.

Para mantener las ramas del repositorio local limpias y actualizadas, se puede configurar la propiedad deleteUntrackedBranches. Hará que Spring Cloud Config Server fuerce la eliminación de ramas sin seguimiento del repositorio local. Ejemplo:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/spring-cloud-samples/config-repo
          deleteUntrackedBranches: true
```

### Frecuencia de actualización de Git
Puede controlar la frecuencia con la que el servidor de configuración obtendrá datos de configuración actualizados de su backend de Git utilizando spring.cloud.config.server.git.refreshRate. El valor de esta propiedad se especifica en segundos. De forma predeterminada, el valor es 0, lo que significa que el servidor de configuración obtendrá la configuración actualizada del repositorio de Git cada vez que se solicite.

### Etiqueta por defecto

La etiqueta predeterminada utilizada para Git es main. Si no configura spring.cloud.config.server.git.defaultLabel y no existe una rama llamada main, el servidor de configuración también intentará verificar una rama llamada master de manera predeterminada. Si desea deshabilitar el comportamiento de la rama alternativa, puede configurar spring.cloud.config.server.git.tryMasterBranch en falso.

### Vault Backend
Spring Cloud Config Server también admite Vault como backend.

> Vault es una herramienta para acceder a secretos de forma segura. Un secreto es cualquier cosa a la que desee controlar estrictamente el acceso, como claves API, contraseñas, certificados y otra información confidencial. Vault proporciona una interfaz unificada para cualquier secreto al mismo tiempo que proporciona un estricto control de acceso y registra un registro de auditoría detallado.

By default, Spring Cloud Config Server uses Token based Authentication to fetch config from Vault. Vault also supports additional authentication methods like AppRole, LDAP, JWT, CloudFoundry, Kubernetes Auth. In order to use any authentication method other than TOKEN or the X-Config-Token header, we need to have Spring Vault Core on the classpath so that Config Server can delegate authentication to that library. Please add the below dependencies to your Config Server App.
```xml
Maven (pom.xml)

<dependencies>
	<dependency>
		<groupId>org.springframework.vault</groupId>
		<artifactId>spring-vault-core</artifactId>
	</dependency>
</dependencies>
```
```groovy
Gradle (build.gradle)

dependencies {
implementation "org.springframework.vault:spring-vault-core"
}
```

### Compartir configuración con todas las aplicaciones

- File Based Repositories
- Vault Server
- CredHub Server
- AWS Secrets Manager
- AWS Parameter Store

#### File Based Repositories
Con los repositorios basados en archivos (git, svn y nativos), los recursos con nombres de archivos en aplicación* (application.properties, application.yml, application-*.properties, etc.) se comparten entre todas las aplicaciones cliente. Puede utilizar recursos con estos nombres de archivos para configurar los valores predeterminados globales y hacer que los archivos específicos de la aplicación los anulen según sea necesario.

La función de anulación de propiedades también se puede utilizar para establecer valores predeterminados globales, y las aplicaciones de marcadores de posición pueden anularlos localmente.

#### Vault Server
Cuando utiliza Vault como backend, puede compartir la configuración con todas las aplicaciones colocando la configuración en secreto/aplicación. Por ejemplo, si ejecuta el siguiente comando de Vault, todas las aplicaciones que utilicen el servidor de configuración tendrán las propiedades foo y baz disponibles:
```commandline
vault write secret/application foo=bar baz=bam
```

#### CredHub Server
Cuando usa CredHub como backend, puede compartir la configuración con todas las aplicaciones colocando la configuración en /aplicación/ o colocándola en el perfil predeterminado de la aplicación. Por ejemplo, si ejecuta el siguiente comando CredHub, todas las aplicaciones que utilicen el servidor de configuración tendrán las propiedades compartidas.color1 y compartida.color2 disponibles:
```commandline
credhub set --name "/application/profile/master/shared" --type=json
value: {"shared.color1": "blue", "shared.color": "red"}
```

#### AWS Secrets Manager
Cuando utiliza AWS Secrets Manager como backend, puede compartir la configuración con todas las aplicaciones colocando la configuración en /application/ o colocándola en el perfil predeterminado de la aplicación. Por ejemplo, si agrega secretos con las siguientes claves, todas las aplicaciones que utilicen el servidor de configuración tendrán las propiedades compartidas.foo y compartidas.bar disponibles:
```
secret name = /secret/application/
```
```
secret value =
{
 shared.foo: foo,
 shared.bar: bar
}
```

Utilice la propiedad spring.cloud.config.server.aws-secretsmanager.default-label para establecer la etiqueta predeterminada. Si la propiedad no está definida, el backend utiliza AWSCURRENT como etiqueta provisional.

```yaml
spring:
  profiles:
    active: aws-secretsmanager
  cloud:
    config:
      server:
        aws-secretsmanager:
          region: us-east-1
          default-label: 1.0.0
```

#### AWS Parameter Store
Cuando utiliza AWS Parameter Store como backend, puede compartir la configuración con todas las aplicaciones colocando propiedades dentro de la jerarquía /application.

#### JDBC Backend
Spring Cloud Config Server admite JDBC (base de datos relacional) como backend para las propiedades de configuración. Puede habilitar esta característica agregando spring-boot-starter-data-jdbc al classpath y usando el perfil jdbc o agregando un bean de tipo JdbcEnvironmentRepository. Si incluye las dependencias correctas en el classpath (consulte la guía del usuario para obtener más detalles al respecto), Spring Boot configura una fuente de datos.
Por ejemplo:
application.properties
```properties
server.port=8888
management.endpoints.web.exposure.include=*

spring.datasource.url=jdbc:mysql://localhost:3306/configdb
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQLDialect
spring.jpa.show-sql=true
```
bootstrap.yml
```yaml
spring:
  application:
    name: config-server-with-jdbc
  profiles:
    active: jdbc
  cloud:
    config:
      server:
        jdbc:
          sql: SELECT PROP_KEY, VALUE from PROPERTIES where APPLICATION=? and PROFILE=? and LABEL=?
          order: 1
```
Tabla
```text
create table PROPERTIES (
  id serial primary key, 
  CREATED_ON timestamp ,
  APPLICATION text, 
  PROFILE text, 
  LABEL text, 
  PROP_KEY text, 
  VALUE text
 );
```

#### Redis Backend

Spring Cloud Config Server admite Redis como backend para las propiedades de configuración. Puede habilitar esta función agregando una dependencia a Spring Data Redis.
dependencia para pom.xml
```xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-redis</artifactId>
	</dependency>
</dependencies>
```
La siguiente configuración utiliza Spring Data RedisTemplate para acceder a Redis. Podemos usar las propiedades spring.redis.* para anular la configuración de conexión predeterminada.
```yaml
spring:
  profiles:
    active: redis
  redis:
    host: redis
    port: 16379
```
#### AWS S3 Backend
Spring Cloud Config Server admite AWS S3 como backend para las propiedades de configuración. Puede habilitar esta característica agregando una dependencia al AWS Java SDK para Amazon S3.

```xml
<dependencies>
	<dependency>
		<groupId>software.amazon.awssdk</groupId>
		<artifactId>s3</artifactId>
	</dependency>
</dependencies>
```
La siguiente configuración utiliza el cliente AWS S3 para acceder a los archivos de configuración. Podemos usar las propiedades spring.cloud.config.server.awss3.* para seleccionar el depósito donde se almacena su configuración.
```yaml
spring:
  profiles:
    active: awss3
  cloud:
    config:
      server:
        awss3:
          region: us-east-1
          bucket: bucket1
```

#### AWS Parameter Store Backend
Spring Cloud Config Server admite AWS Parameter Store como backend para las propiedades de configuración. Puede habilitar esta característica agregando una dependencia al AWS Java SDK para SSM.

Dependencia para pom
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>ssm</artifactId>
</dependency>
```

La siguiente configuración utiliza el cliente AWS SSM para acceder a los parámetros.
```yaml
spring:
  profiles:
    active: awsparamstore
  cloud:
    config:
      server:
        awsparamstore:
          region: us-east-1
          endpoint: https://ssm.us-east-1.amazonaws.com
          origin: aws:parameter:
          prefix: /config/service
          profile-separator: _
          recursive: true
          decrypt-values: true
          max-results: 5
```

#### AWS Secrets Manager Backend

Spring Cloud Config Server admite AWS Secrets Manager como backend para las propiedades de configuración. Puede habilitar esta característica agregando una dependencia a AWS Java SDK para Secrets Manager.

Dependias en pom
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>secretsmanager</artifactId>
</dependency>
```
La siguiente configuración utiliza el cliente de AWS Secrets Manager para acceder a los secretos.

```yaml
spring:
  profiles:
  	active: awssecretsmanager
  cloud:
    config:
      server:
        aws-secretsmanager:
          region: us-east-1
          endpoint: https://us-east-1.console.aws.amazon.com/
          origin: aws:secrets:
          prefix: /secret/foo
          profileSeparator: _
```

#### Repositorios de entornos compuestos
En algunos escenarios, es posible que desee extraer datos de configuración de múltiples repositorios de entorno. Para hacerlo, puede habilitar el perfil compuesto en las propiedades de la aplicación de su servidor de configuración o en el archivo YAML. Si, por ejemplo, desea extraer datos de configuración de un repositorio de Subversion, así como de dos repositorios de Git, puede establecer las siguientes propiedades para su servidor de configuración:
```yaml
spring:
  profiles:
    active: composite
  cloud:
    config:
      server:
        composite:
        -
          type: svn
          uri: file:///path/to/svn/repo
        -
          type: git
          uri: file:///path/to/rex/git/repo
        -
          type: git
          uri: file:///path/to/walter/git/repo
```

Si desea extraer datos de configuración solo de repositorios que sean de distintos tipos, puede habilitar los perfiles correspondientes, en lugar del perfil compuesto, en las propiedades de la aplicación de su servidor de configuración o en el archivo YAML. Si, por ejemplo, desea extraer datos de configuración de un único repositorio Git y un único servidor HashiCorp Vault, puede establecer las siguientes propiedades para su servidor de configuración:
```yaml
spring:
  profiles:
    active: git, vault
  cloud:
    config:
      server:
        git:
          uri: file:///path/to/git/repo
          order: 2
        vault:
          host: 127.0.0.1
          port: 8200
          order: 1
```


#### Anulaciones de propiedades

El servidor de configuración tiene una función de "overrides" que permite al operador proporcionar propiedades de configuración a todas las aplicaciones. La aplicación no puede cambiar accidentalmente las propiedades anuladas con los ganchos Spring Boot normales. Para declarar anulaciones, agregue un mapa de pares nombre-valor a spring.cloud.config.server.overrides, como se muestra en el siguiente ejemplo:

```yaml
spring:
  cloud:
    config:
      server:
        overrides:
          foo: bar
```
Los ejemplos anteriores hacen que todas las aplicaciones que son clientes de configuración lean foo=bar, independientemente de su propia configuración.

## Indicador de salud
Config Server viene con un indicador de estado que verifica si el EnvironmentRepository configurado está funcionando. De forma predeterminada, solicita a EnvironmentRepository una aplicación denominada aplicación, el perfil predeterminado y la etiqueta predeterminada proporcionada por la implementación de EnvironmentRepository.

Puede configurar el indicador de estado para verificar más aplicaciones junto con perfiles y etiquetas personalizados, como se muestra en el siguiente ejemplo:
```yaml
spring:
  cloud:
    config:
      server:
        health:
          repositories:
            myservice:
              label: mylabel
            myservice-dev:
              name: myservice
              profiles: development
```
Puede desactivar el indicador de estado configurando management.health.config.enabled=false.

Además, puede proporcionar su propio estado de inactividad personalizado configurando la propiedad spring.cloud.config.server.health.down-health-status (valorada en "DOWN" de forma predeterminada).

## Seguridad
Puede proteger su Config Server de cualquier forma que tenga sentido para usted (desde seguridad de red física hasta tokens portadores de OAuth2), porque Spring Security y Spring Boot ofrecen soporte para muchas disposiciones de seguridad.

Para utilizar la seguridad HTTP básica configurada por Spring Boot de forma predeterminada, incluya Spring Security en la ruta de clase (por ejemplo, a través de spring-boot-starter-security). El valor predeterminado es un nombre de usuario de usuario y una contraseña generada aleatoriamente. Una contraseña aleatoria no es útil en la práctica, por lo que le recomendamos configurar la contraseña (estableciendo spring.security.user.password) y cifrarla (consulte las instrucciones a continuación sobre cómo hacerlo).

## Cifrado y descifrado

Si las fuentes de propiedades remotas contienen contenido cifrado (valores que comienzan con {cipher}), se descifran antes de enviarlos a los clientes a través de HTTP. La principal ventaja de esta configuración es que los valores de las propiedades no necesitan estar en texto plano cuando están "en reposo" (por ejemplo, en un repositorio git). Si un valor no se puede descifrar, se elimina del origen de la propiedad y se agrega una propiedad adicional con la misma clave pero con el prefijo no válido y un valor que significa "no aplicable" (normalmente <n/a>). Esto es en gran medida para evitar que el texto cifrado se utilice como contraseña y se filtre accidentalmente.

Si configura un repositorio de configuración remoto para aplicaciones cliente de configuración, es posible que contenga un archivo application.yml similar al siguiente:
```yaml
spring:
  datasource:
    username: dbuser
    password: '{cipher}FKSAJDFGYOS8F7GLHAKERGFHLSAJ'
```
Los valores cifrados en el archivo application.properties no deben estar entre comillas. De lo contrario, el valor no se descifra. El siguiente ejemplo muestra valores que funcionarían:
```properties
spring.datasource.username: dbuser
spring.datasource.password: {cipher}FKSAJDFGYOS8F7GLHAKERGFHLSAJ
```
Puede enviar este texto sin formato de forma segura a un repositorio git compartido y la contraseña secreta permanecerá protegida.

El servidor también expone los puntos finales /encrypt y /decrypt (suponiendo que estén protegidos y solo accedan a ellos agentes autorizados). Si edita un archivo de configuración remoto, puede usar el servidor de configuración para cifrar valores mediante la publicación en el punto final /encrypt, como se muestra en el siguiente ejemplo:

```commandline
curl localhost:8888/encrypt -s -d mysecret
```
## Gestión de claves
El Config Server puede utilizar una clave simétrica (compartida) o una asimétrica (par de claves RSA). La opción asimétrica es superior en términos de seguridad, pero a menudo es más conveniente usar una clave simétrica ya que es un valor de propiedad único para configurar en application.properties.

Para configurar una clave simétrica, debe establecer encrypt.key en una cadena secreta (o usar la variable de entorno ENCRYPT_KEY para mantenerla fuera de los archivos de configuración de texto sin formato).

> #### Nota
> Si incluye spring-cloud-starter-bootstrap en el classpath o establece spring.cloud.bootstrap.enabled=true como propiedad del sistema, deberá configurar encrypt.key en bootstrap.properties.
> 
> No puede configurar una clave asimétrica usando encrypt.key.

Para configurar una clave asimétrica, utilice un almacén de claves (por ejemplo, el creado por la utilidad keytool que viene con el JDK). Las propiedades del almacén de claves son encrypt.keyStore.* con * igual a:

|Propiedad   	| Descripción   	                        |
|---	|----------------------------------------|
|encrypt.keyStore.location   	| Contiene una ubicación del recurso   	 |
|encrypt.keyStore.password   	| Contiene la contraseña que desbloquea el almacén de claves.	                                      |
|encrypt.keyStore.alias   	| Identifica qué clave de la tienda utilizar	                                      |
|encrypt.keyStore.type   	| El tipo de KeyStore que se va a crear. El valor predeterminado es jks.	                                      |

## Crear un almacén de claves para pruebas
Para crear un almacén de claves para realizar pruebas, puede utilizar un comando similar al siguiente:
```commandline
keytool -genkeypair -alias mytestkey -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=US" -keypass changeme -keystore server.jks -storepass letmein
```

Coloque el archivo server.jks en el classpath (por ejemplo) y luego, en su bootstrap.yml, para el servidor de configuración, cree las siguientes configuraciones:
```yaml
encrypt:
  keyStore:
    location: classpath:/server.jks
    password: letmein
    alias: mytestkey
    secret: changeme
```

## Sirviendo propiedades cifradas

A veces desea que los clientes descifren la configuración localmente, en lugar de hacerlo en el servidor. En ese caso, si proporciona la configuración encrypt.* para localizar una clave, aún puede tener los puntos finales /encrypt y /decrypt, pero debe desactivar explícitamente el descifrado de las propiedades salientes colocando spring.cloud.config.server. encrypt.enabled=false en bootstrap.[yml|properties]. Si no le importan los puntos finales, debería funcionar si no configura la clave o el indicador habilitado.

## Notificaciones push y Spring Cloud Bus

Muchos proveedores de repositorios de código fuente (como Github, Gitlab, Gitea, Gitee, Gogs o Bitbucket) le notifican los cambios en un repositorio a través de un webhook. Puede configurar el webhook a través de la interfaz de usuario del proveedor como una URL y un conjunto de eventos en los que esté interesado. Por ejemplo, Github usa un POST para el webhook con un cuerpo JSON que contiene una lista de confirmaciones y un encabezado (X-Github-Event) configurado para enviar. Si agrega una dependencia en la biblioteca spring-cloud-config-monitor y activa Spring Cloud Bus en su servidor de configuración, entonces se habilita un punto final /monitor.

Cuando se activa el webhook, el servidor de configuración envía un RefreshRemoteApplicationEvent dirigido a las aplicaciones que cree que podrían haber cambiado. La detección de cambios se puede diseñar estratégicamente. Sin embargo, de forma predeterminada, busca cambios en los archivos que coinciden con el nombre de la aplicación (por ejemplo, foo.properties está dirigido a la aplicación foo, mientras que application.properties está dirigido a todas las aplicaciones). La estrategia a utilizar cuando desee anular el comportamiento es PropertyPathNotificationExtractor, que acepta los encabezados y el cuerpo de la solicitud como parámetros y devuelve una lista de rutas de archivo que cambiaron.

La configuración predeterminada funciona de inmediato con Github, Gitlab, Gitea, Gitee, Gogs o Bitbucket. Además de las notificaciones JSON de Github, Gitlab, Gitee o Bitbucket, puede activar una notificación de cambio PUBLICANDO en /monitor con parámetros de cuerpo codificados en forma en el patrón de ruta={aplicación}. Al hacerlo, se transmite a aplicaciones que coinciden con el patrón {application} (que puede contener comodines).

# Spring Cloud Config Client
Una aplicación Spring Boot puede aprovechar inmediatamente Spring Config Server (u otras fuentes de propiedades externas proporcionadas por el desarrollador de la aplicación). También recoge algunas características útiles adicionales relacionadas con eventos de cambio de entorno.

## Importación de datos de configuración de Spring Boot

Spring Boot 2.4 introdujo una nueva forma de importar datos de configuración a través de la propiedad spring.config.import. Esta es ahora la forma predeterminada de vincularse al Config Server.

Para conectarse opcionalmente al servidor de configuración, configure lo siguiente en application.properties:

```properties
spring.config.import=optional:configserver:[url config server]
```
Esto se conectará al servidor de configuración en la ubicación predeterminada de "http://localhost:8888". La eliminación del prefijo opcional: hará que el Config Client falle si no puede conectarse al Config Server.

Spring Boot Config Data resuelve la configuración en un proceso de dos pasos. Primero carga toda la configuración usando el perfil predeterminado. Esto permite que Spring Boot recopile toda la configuración que puede activar cualquier perfil adicional. Una vez que haya recopilado todos los perfiles activados, cargará cualquier configuración adicional para los perfiles activos. Debido a esto, es posible que vea que se realizan varias solicitudes al servidor de configuración de Spring Cloud para recuperar la configuración. Esto es normal y es un efecto secundario de cómo Spring Boot carga la configuración cuando se usa spring.config.import. En versiones anteriores de Spring Cloud Config, solo se realizaba una solicitud, pero esto significaba que no se podían activar perfiles desde la configuración proveniente del servidor de configuración. La solicitud adicional con solo el perfil "predeterminado" ahora lo hace posible.

## Configurar el primer arranque

Para utilizar la forma de arranque heredada de conectarse a Config Server, el arranque debe habilitarse mediante una propiedad o el iniciador spring-cloud-starter-bootstrap. La propiedad es spring.cloud.bootstrap.enabled=true. Debe establecerse como una propiedad del sistema o una variable de entorno. Una vez que se haya habilitado el arranque, cualquier aplicación con Spring Cloud Config Client en el classpath se conectará al Config Server de la siguiente manera: cuando se inicia un cliente de configuración, se vincula al Config Server (a través de la propiedad de configuración de arranque spring.cloud.config.uri) y inicializa Spring Environment con fuentes de propiedades remotas.

El resultado neto de este comportamiento es que todas las aplicaciones cliente que quieran consumir el servidor de configuración necesitan un bootstrap.yml (o una variable de entorno) con la dirección del servidor configurada en spring.cloud.config.uri (el valor predeterminado es "http:/ /localhost:8888").

dependencia en pom
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```
archivo application.yml
```yaml
spring:
  cloud:
    bootstrap:
      enabled: true
```

archivo bootstrap.yml
```yaml
spring:
  application:
    name: springcloudconfigdemoclient
  profiles:
    active: dev
  cloud:
    config:
      uri: http://root:s3cr3t@localhost:8888
    bootstrap:
      enabled: true
```

## Primera búsqueda de descubrimiento

Si utiliza una implementación de DiscoveryClient, como Spring Cloud Netflix y Eureka Service Discovery o Spring Cloud Consul, puede hacer que el Config Server se registre con Discovery Service.

Si prefiere utilizar DiscoveryClient para ubicar el servidor de configuración, puede hacerlo configurando spring.cloud.config.discovery.enabled=true (el valor predeterminado es falso). Por ejemplo, con Spring Cloud Netflix, debe definir la dirección del servidor Eureka (por ejemplo, en eureka.client.serviceUrl.defaultZone). El precio por utilizar esta opción es un viaje extra de ida y vuelta a la red al inicio, para localizar el alta del servicio. El beneficio es que, siempre que el Servicio de descubrimiento sea un punto fijo, el Servidor de configuración puede cambiar sus coordenadas. El ID de servicio predeterminado es configserver, pero puede cambiarlo en el cliente configurando spring.cloud.config.discovery.serviceId (y en el servidor, de la forma habitual para un servicio, como configurando spring.application.name) .

Todas las implementaciones del cliente de descubrimiento admiten algún tipo de mapa de metadatos (por ejemplo, tenemos eureka.instance.metadataMap para Eureka). Es posible que sea necesario configurar algunas propiedades adicionales del servidor de configuración en sus metadatos de registro de servicio para que los clientes puedan conectarse correctamente. Si el servidor de configuración está protegido con HTTP básico, puede configurar las credenciales como usuario y contraseña. Además, si el servidor de configuración tiene una ruta de contexto, puede configurar configPath. Por ejemplo, el siguiente archivo YAML es para un servidor de configuración que es un cliente Eureka:

```yaml
eureka:
  instance:
    ...
    metadataMap:
      user: osufhalskjrtl
      password: lviuhlszvaorhvlo5847
      configPath: /config
```

## El cliente de configuración falla

En algunos casos, es posible que falle el inicio de un servicio si no puede conectarse al servidor de configuración. Si este es el comportamiento deseado, establezca la propiedad de configuración de arranque spring.cloud.config.fail-fast=true para que el cliente se detenga con una excepción.

## Reintentar el cliente de configuración con spring.config.import

Reintentar funciona con la declaración Spring Boot spring.config.import y las propiedades normales funcionan. Sin embargo, si la declaración de importación está en un perfil, como application-prod.properties, necesitará una forma diferente de configurar el reintento. La configuración debe colocarse como parámetros de URL en la declaración de importación.

```properties
spring.config.import=configserver:http://root:s3cr3t@localhost:8888?fail-fast=true&max-attempts=10&max-interval=1500&multiplier=1.2&initial-interval=1100"
```

## Vault
Cuando utiliza Vault como backend para su servidor de configuración, el cliente debe proporcionar un token para que el servidor recupere valores de Vault. Este token se puede proporcionar dentro del cliente configurando spring.cloud.config.token en bootstrap.yml, como se muestra en el siguiente ejemplo:
```yaml
spring:
  cloud:
    config:
      token: YourVaultToken
```
## Seguridad

Si utiliza seguridad HTTP básica en el servidor, los clientes necesitan saber la contraseña (y el nombre de usuario si no es el predeterminado). Puede especificar el nombre de usuario y la contraseña a través del URI del servidor de configuración o mediante propiedades separadas de nombre de usuario y contraseña, como se muestra en el siguiente ejemplo:
```yaml
spring:
  cloud:
    config:
     uri: http://root:s3cr3t@localhost:8888
```
otra alternativa
```yaml
spring:
  cloud:
    config:
     uri: http://localhost:8888
     username: user
     password: secret
```