# Java Records

## ¿Qué son los Java Records?

Son un tipo de datos especial para crear objetos inmutables.
- Introducidos en Java 14 y confirmados como característica permanente en Java 16.
- Permiten la creación de objetos inmutables de forma concisa.
- Diseñados para representar datos simples de una manera más compacta que las clases tradicionales.

## Propósito
Normalmente, escribimos clases para simplemente contener datos, resultados de consultas o información de un servicio.
- Facilitar la creación de objetos inmutables.
- Reducir la verbosidad del código al definir clases de datos.
- Mejorar la legibilidad y mantenibilidad del código.

## Características de los Java Records
- Atributos finales e inmutables.
- Métodos equals, hashCode y toString generados automáticamente.
- Sintaxis concisa y legible.
- No permiten herencia, pero pueden implementar interfaces.
- Útiles para DTOs y representación de datos simples.

### Ejemplo
```java
package com.mcalvaro.records;
 
public record CategoryRecord( int categoryId, String name) {
 
}

```
