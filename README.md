# Aplicacion de Gestión de Inquilinos
Esta aplicación Android está diseñada para gestionar alquileres temporales, permitiendo a los propietarios llevar un control detallado de sus inquilinos, pagos y fechas importantes.

## Registro de nuevos inquilinos con información detallada:
  - Nombre
  - Teléfono
  - Fecha de inicio
  - Duración del alquiler
  - Precio por día
  - Monto pagado

## Sistema de Pagos
  - Control de pagos realizados
  - Cálculo automático del monto restante
  - Función de agregar pagos parciales

## Calendario Interactivo
  - visualizacion de:
    - Fechas de entrada y salida
    - Periodos de ocupacion

## Comunicación con Inquilinos
  - Integración con WhatsApp para contacto directo
  - Acceso rápido al chat mediante botón dedicado

## Tecnologías Utilizadas
  - Kotlin
  - Android SDK
  - ViewBinding
  - Coroutines
  - MVVM Architecture
  - Room Database
  - Dagger Hilt (Dependency Injection)

## Estructura del Proyecto
  - UI: Contiene las vistas y ViewModels
  - Data: Modelos y lógica de datos
  - DI: Configuración de inyección de dependencias

## Requisitos
  - Android 10.0 o superior
  - Conexión a Internet para funciones de WhatsApp
