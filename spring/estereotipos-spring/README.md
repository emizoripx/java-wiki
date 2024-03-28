# Estereotipos de Spring
## @Component
Esta anotación se usa en clases para indicar un componente Spring. La anotación @Component marca la clase Java como un bean o componente para que el mecanismo de exploración de componentes de Spring pueda agregarla al contexto de la aplicación.

## @Controller
La anotación @Controller se usa para indicar que la clase es un controlador Spring. Esta anotación se puede utilizar para identificar controladores para Spring MVC o Spring WebFlux.

## @Service
Esta anotación se usa en una clase. @Service marca una clase Java que realiza algún servicio, como ejecutar lógica de negocios, realizar cálculos y llamar a API externas. Esta anotación es una forma especializada de la anotación @Component destinada a ser utilizada en la capa de servicio.

## @Repository
Esta anotación se utiliza en clases Java que acceden directamente a la base de datos. La anotación @Repository funciona como un marcador para cualquier clase que cumpla la función de repositorio u Objeto de acceso a datos.

Esta anotación tiene una función de traducción automática. Por ejemplo, cuando ocurre una excepción en el @Repository, hay un controlador para esa excepción y no es necesario agregar un bloque try-catch.

