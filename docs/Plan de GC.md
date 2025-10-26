# Plan de Gestión de la Configuración (GC)

## 1. Introducción

### 1.1 Propósito del plan
El propósito de este Plan de Gestión de la Configuración (GC) es definir los procedimientos, responsabilidades, herramientas y actividades necesarias para identificar, controlar, auditar y mantener la integridad de los elementos de configuración del proyecto **ISO2_NONEIM – Sistema de Alquiler de Viviendas**.  
Este plan garantiza que todos los artefactos del sistema se gestionen adecuadamente durante el ciclo de vida del desarrollo, asegurando trazabilidad, control de versiones y coherencia entre componentes de software y documentación.

---

### 1.2 Alcance del plan
El plan abarca todos los componentes del proyecto que estén sujetos a control de configuración, incluyendo:
- Código fuente del backend (API REST en Spring Boot).  
- Código fuente del frontend (interfaz web React).  
- Archivos de configuración del entorno (por ejemplo, `application.properties`, `.env`, `package.json`).  
- Base de datos H2 y su esquema (`schema.sql`).  
- Documentación técnica y funcional (manuales, diagramas UML, plan de pruebas, etc.).  
- Ficheros de despliegue y dependencias de compilación (Maven y npm).

---

### 1.3 Relación con la organización y con otros proyectos
Este plan se integra dentro del marco metodológico de la asignatura **Ingeniería del Software II**, y se coordina con el resto de entregables del proyecto académico.  
No se prevé la interacción con otros proyectos externos, salvo la integración entre módulos del propio sistema (frontend y backend).  

---

### 1.4 Términos claves

| Término | Definición |
|----------|-------------|
| **Elemento de configuración (EC)** | Cualquier componente sujeto a control de versiones (código, documentación, datos, etc.). |
| **Versión** | Estado identificado de un EC en un momento determinado. |
| **Repositorio** | Espacio donde se almacenan y gestionan los EC (en este caso, GitHub). |
| **Baseline (línea base)** | Conjunto aprobado de EC que sirve como referencia para posteriores desarrollos. |
| **Branch (rama)** | Línea de desarrollo independiente dentro del repositorio Git. |
| **Merge** | Integración de cambios de una rama en otra. |

---

### 1.5 Referencias
- ISO/IEC/IEEE 12207:2017 — *Systems and software engineering — Software life cycle processes.*  
- IEEE Std 828-2012 — *Configuration Management in Systems and Software Engineering.*  
- Guía docente de Ingeniería del Software II (UCLM).  
- Repositorio GitHub: [https://github.com/GuilleR-lab/ISO2_NONEIM](https://github.com/GuilleR-lab/ISO2_NONEIM).

---

## 2. Criterios para la identificación de los elementos de configuración a los cuales la GC será aplicada

Se aplicará la gestión de configuración a todos los elementos que cumplan al menos uno de los siguientes criterios:
- Impacten directamente en la funcionalidad del sistema.  
- Sean necesarios para la correcta compilación, ejecución o despliegue.  
- Forman parte de la documentación oficial del proyecto.  
- Requieran trazabilidad o control de versiones.  

Los principales **elementos de configuración identificados** son:
- Código fuente (`/backend/src/main/java`, `/frontend/src`)  
- Ficheros de configuración (`application.properties`, `.env`, `pom.xml`, `package.json`)  
- Archivos de documentación (`/docs`, `/README.md`)  
- Scripts de base de datos (`schema.sql`, `data.sql`)

---

## 3. Limitaciones y suposiciones que afectan al plan

- Se asume que todos los integrantes del equipo disponen de acceso al repositorio GitHub.  
- Las herramientas utilizadas (Git, VS Code y Eclipse, npm, Maven) son compartidas y actualizadas por cada miembro.  
- No se contemplan integraciones con sistemas externos de CI/CD (como Jenkins o GitHub Actions).  
- La GC se limita al entorno de desarrollo local y a GitHub como único repositorio oficial.

---

## 4. Responsabilidades y autoridades del Plan

| Rol | Responsabilidades |
|------|--------------------|
| **Responsable de GC** | Supervisar la aplicación del plan, aprobar cambios en la configuración y mantener la trazabilidad. |
| **Desarrolladores** | Aplicar los procedimientos de control de versiones y comunicar cambios significativos. |
| **Revisor/a de configuración** | Validar la correcta implementación de las políticas de GC y participar en revisiones de líneas base. |
| **Product Owner / Coordinador** | Aprobar versiones estables del software y validar la entrega final. |

---

## 5. Organización del proyecto

El proyecto se estructura en dos subsistemas principales:
1. **Backend:** desarrollado con Spring Boot y Maven (Java 21), que implementa la API REST y la lógica de negocio.  
2. **Frontend:** desarrollado con React y npm, encargado de la interfaz web y consumo del backend.  

Cada módulo tiene su propio conjunto de elementos de configuración bajo un mismo repositorio Git, organizado en ramas *feature*, *develop* y *main*.

---

## 6. Responsabilidades de la GC

- Mantener la integridad de todos los EC identificados.  
- Asegurar la trazabilidad entre requisitos, código y entregables.  
- Controlar versiones mediante Git (ramas, merges y commits).  
- Definir y aplicar líneas base (releases funcionales).  
- Garantizar que las versiones aprobadas estén debidamente etiquetadas y documentadas.  

---

## 7. Políticas aplicables, directivas y procedimientos

- El repositorio oficial es **GitHub – ISO2_NONEIM**.  
- Las ramas principales son:
  - `main` → versión estable.  
  - `develop` → integración de nuevas funcionalidades.  
  - `feature/...` → desarrollo de características específicas.  
- Todo commit debe incluir un mensaje descriptivo y asociarse a una tarea del tablero Kanban.  
- Las solicitudes de integración (pull requests) deben revisarse antes de ser aceptadas.  
- Las versiones aprobadas se etiquetarán con el formato `vX.Y.Z` (por ejemplo, `v1.0.0`).

---

## 8. Actividades planificadas, agenda y recursos

### 8.1 Agendas de la GC

| Actividad | Frecuencia | Responsable |
|------------|-------------|--------------|
| Revisión de configuración (auditoría interna) | Cada entrega parcial | Responsable de GC |
| Creación de ramas *feature* | Al iniciar una nueva tarea | Desarrolladores |
| Revisión de merges / pull requests | Al finalizar la tarea | Revisor de GC |
| Creación de línea base | Al cierre de una iteración o sprint | Coordinador |
| Actualización del plan de GC | Cuando se modifique la estructura del proyecto | Responsable de GC |

---

### 8.2 Recursos de la GC

**Herramientas:**
- Git y GitHub para control de versiones.  
- Visual Studio Code y Eclipse como entornos de desarrollo.  
- GitHub Projects para Kanban y seguimiento.  

**Recursos humanos:**
- Todo el equipo de desarrollo.  
- Un responsable de configuración designado.  

---

## 9. Mantenimiento del Plan de GC

Este plan será revisado y actualizado en cada iteración o cuando:
- Se incorporen nuevos módulos o componentes al proyecto.  
- Se modifiquen las herramientas o el flujo de trabajo de GC.  
- Se detecten desviaciones entre lo planificado y lo ejecutado.  

Las versiones del plan se documentarán mediante numeración incremental (`v1.0`, `v1.1`, etc.) y se almacenarán en el repositorio bajo `/docs/configuration_management/`.
