Proyecto: Sistema de Alquiler de Viviendas/NONEIM
=================================================

Descripción General
-------------------
Este proyecto consiste en el desarrollo de una plataforma web similar a Airbnb, donde propietarios pueden publicar inmuebles (viviendas completas o habitaciones) y los inquilinos pueden buscar, reservar y pagar alojamientos.

Objetivo
--------
Crear un sistema que permita la interacción entre propietarios e inquilinos para facilitar el alquiler de viviendas o habitaciones de forma segura y eficiente.

Roles del Sistema
-----------------
- Propietario: Publica propiedades, gestiona disponibilidad y confirma reservas.
- Inquilino: Busca alojamientos, realiza reservas y pagos.

Funcionalidades Principales
---------------------------
- Registro y autenticación de usuarios (propietarios e inquilinos).
- Alta de propiedades por parte de propietarios.
- Búsqueda de inmuebles con filtros avanzados:
  - Reserva inmediata
  - Comodidades específicas
  - Política de cancelación
- Lista de deseos para usuarios registrados.
- Reserva inmediata o mediante solicitud.
- Pagos por tarjeta de crédito, débito o PayPal.
- Notificaciones de confirmación o rechazo de reservas.

Políticas de Cancelación
------------------------
- NO_REEMBOLSABLE
- REEMBOLSABLE
- REEMBOLSABLE_50_PER

Arquitectura del Sistema
------------------------
El sistema se estructura en capas:

1. Vista (UI):
   - Ventanas de registro, alta de inmuebles, búsqueda, reservas, confirmación y pago.

2. Controladores:
   - Gestores para usuarios, inmuebles, búsquedas, reservas y notificaciones.

3. Dominio:
   - Clases principales: Usuario, Propietario, Inquilino, Inmueble, Reserva, SolicitudReserva, Pago, ListaDeseos.

4. Persistencia:
   - DAOs para cada entidad con operaciones CRUD (select, insert, update, delete).

Tecnologías usadas
------------------
- Lenguaje de programación: Java 
- Base de datos: H2
- Frameworks: Spring Boot y react.js
- Frontend: HTML

Planificación Sprints
---------------------
Para el desarrollo del proyecto se emplea una combinación de la metodología Scrum con un panel Kanban para el seguimiento de tareas.
Cada sprint tiene una duración de una semana.

1. Sprint 0 – Preparación inicial

   - Objetivo: establecer la base del proyecto y preparar el entorno de desarrollo.
   - Tareas realizadas:

      - Creación del backend y frontend básicos sobre los que se iterará.

      - Configuración del repositorio y del panel Kanban.

      - Definición inicial del backlog del producto.


2. Sprint 1 – Requisitos y configuración

   - Objetivo: definir requisitos funcionales y preparar la infraestructura del proyecto.
   - Tareas realizadas:

      - Recopilación de requisitos funcionales y no funcionales.

      - Creación del plan de gestión de la configuración.

      - Generación de documentación en el directorio /docs.

      - Diseño e implementación de la base de datos.

      - Inicio de la modificación de los formularios de login y registro en el frontend.


3. Sprint 2 – Desarrollo del backend y mejoras en frontend

   - Objetivo: avanzar en la implementación de la lógica del backend y conectar con el frontend.
   - Tareas realizadas:

      - Creación de las entidades necesarias en el backend.

      - Implementación y mejora de los formularios del frontend.

      - Modificación y extensión de la API REST de usuarios.
