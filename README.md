# Registro Multimedia de Equipo — Guía del Proyecto, Entorno y Flujo de Git

Una aplicación móvil nativa para Android diseñada para la recolección, gestión y visualización dinámica de la información de los integrantes de un equipo de trabajo.

Este repositorio implementa una arquitectura **MVVM estricta** en Java 100% para el desarrollo colaborativo de 20 personas. Para evitar conflictos de código y asegurar la estabilidad del sistema, todo el flujo de trabajo, la configuración del entorno y las reglas de auditoría se rigen bajo los siguientes protocolos obligatorios.

---

## 1. Características Principales

- **Formulario Interactivo:** Captura de datos utilizando una amplia gama de controles de UI (Spinner, CheckBox, RadioGroup, RatingBar) dentro de un `ScrollView`.
- **Seguimiento de Progreso:** Visualización del avance matemático mediante un `ProgressBar`.
- **Integración Multimedia:** Capacidad para grabar audio (micrófono) y reproducir notas de voz localmente en estricto formato `.m4a` con códec AAC.
- **Listado Dinámico:** Renderizado en tiempo real de los perfiles guardados utilizando un `RecyclerView` con tarjetas (`CardView`).
- **Gestión de Permisos:** Solicitud dinámica de permisos en tiempo de ejecución para el acceso al micrófono (`RECORD_AUDIO`).

---

## 2. Requisitos de Entorno y Stack Tecnológico

Para garantizar la compatibilidad exacta con la rúbrica docente y evitar errores de compilación, todo el equipo debe utilizar estrictamente las siguientes versiones:

- **IDE Requerido:** Android Studio (Panda 1 | 2025.3.1 Patch 1 o superior).
- **Lenguaje:** **Java 11** (Strictly No Kotlin / No Jetpack Compose).
- **SDK Mínimo (`minSdk`):** API 24 (Android 7.0 Nougat).
- **SDK Objetivo y Compilación (`targetSdk` / `compileSdk`):** API 34 o superior.
- **Sistema de Construcción:** Gradle 8.x con AGP (Android Gradle Plugin) 8.1.1+.
- **Gradle JDK:** JDK 11 o superior (Asegurar que el IDE apunte a este JDK).

### Dependencias del Proyecto (`app/build.gradle.kts`)

El proyecto integra por defecto las siguientes librerías de AndroidX y Material Components:

```kotlin
implementation("androidx.appcompat:appcompat:1.6.1")
implementation("com.google.android.material:material:1.13.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")
implementation("androidx.recyclerview:recyclerview:1.4.0")
implementation("androidx.cardview:cardview:1.0.0")
implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
```

---

## 3. Arquitectura del Proyecto (MVVM por Capas)

El código está estructurado bajo el patrón **MVVM** dividido en 3 capas fundamentales para aislar el trabajo de los distintos equipos:

- **Domain (Dominio):** Representa la capa de datos puros. Contiene el Modelo (`Member.java`) que encapsula los atributos, y las clases con lógicas de validación. No tiene dependencias de Android.
- **Presentation (Presentación):** Responsable de la UI y el estado. Contiene la `MainActivity`, archivos XML, adaptadores (`MemberAdapter.java`) y el **ViewModel**. La UI solo observa cambios, mientras que el ViewModel procesa la lógica reactiva sin importar clases visuales (`android.view.*`).
- **Data (Infraestructura y Core):** Encargada del manejo de hardware (`MediaRecorder`, `MediaPlayer`), la solicitud de permisos y el almacenamiento local de la app.

---

## 4. Estructura de Ramas

El repositorio está protegido. Nadie trabaja ni sube código directamente sobre las ramas de integración o producción.

| Rama Base / Capa    | Propósito                                                                                                                |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| `main`              | **Producción.** Intocable. Solo recibe el código final para la entrega.                                                  |
| `develop`           | **Integración.** Rama de ensamblaje controlada por el equipo Core. Recibe los Pull Requests.                             |
| `presentation/base` | **Capa de Presentación.** Rama base para la UI, maquetación XML, adaptadores (`MemberAdapter`) y ViewModels.             |
| `domain/base`       | **Capa de Dominio.** Rama base para entidades del modelo (`Member.java`) y lógicas de validación.                        |
| `data/base`         | **Capa de Datos.** Rama base para gestión de hardware (`MediaRecorder`, `MediaPlayer`), permisos y almacenamiento local. |

### Nomenclatura de Ramas de Trabajo (Feature Branches)

Todo desarrollador debe crear su rama desde la rama base de la capa asignada o desde `develop`:

- `presentation/<nombre-tarea>` (Ej: `presentation/base`, `presentation/viewmodels`)
- `domain/<nombre-tarea>` (Ej: `domain/base`, `domain/entidad-member`)
- `data/<nombre-tarea>` (Ej: `data/base`, `data/media-recorder`)

---

## 5. Instalación, Ejecución y Flujo de Trabajo (Git)

### Clonar y Levantar el Proyecto

1. Clona este repositorio:

```bash
git clone https://github.com/Bksp/AplicacionAndroid.git
cd AplicacionAndroid
```

2. Compilar la APK de depuración (CLI): `./gradlew assembleDebug`
3. Instalar en el emulador (CLI): `./gradlew installDebug`

### Procedimiento Obligatorio para Desarrollar

1. Asegúrate de estar sincronizado con la rama de integración:

```bash
git checkout develop
git pull origin develop
```

2. Crea tu rama de trabajo según tu asignación en el Kanban:

```bash
git checkout -b <capa>/<nombre-tarea>
```

3. Realiza tus cambios, compila localmente y haz commit (ver estándar abajo).
4. Sube tu rama al servidor:

```bash
git push origin <capa>/<nombre-tarea>
```

5. Abre un **Pull Request (PR)** en GitHub directed towards `develop`.

---

## 6. Estándar de Commits

Los mensajes deben seguir una nomenclatura formal y trazable basada en _Conventional Commits_:

- **`feat:`** (Nueva característica) -> _Ej: feat: añade validación de campos vacíos en MemberViewModel_
- **`fix:`** (Corrección de error) -> _Ej: fix: corrige fuga de memoria en MediaPlayer al destruir Activity_
- **`ui:`** (Cambios visuales) -> _Ej: ui: actualiza márgenes y colores del CardView de integrantes_

---

## 7. Plantilla Obligatoria para Pull Requests (PR)

Al abrir un PR hacia `develop`, debes rellenar la siguiente estructura en la descripción:

```markdown
## Qué hace

(Descripción concisa en un máximo de 2 líneas de los cambios implementados)

## Tarjeta del Kanban resuelta

(Ejemplo: Resuelve tarea "Diseño de la Tarjeta de Member")

## Capa de Arquitectura

- [ ] Domain (`domain/`)
- [ ] Presentation (`presentation/`)
- [ ] Data (`data/`)
```

---

## 8. Criterios de Rechazo y Auditoría

El equipo de Integración (Alessy, Sofi, Sebita, Diogo) revisará todo el código. Se aplicará **rechazo automático** a cualquier PR que incumpla lo siguiente:

1. **Infracción MVVM:** Importar widgets (`android.widget.*`, `android.view.*`) dentro de los ViewModels.
2. **Violación de Capas:** Alterar archivos que no corresponden a la etiqueta (`presentation`, `domain`, `data`) de la tarea asignada.
3. **Conflictos con `develop`:** Todo PR que arroje conflictos pendientes será devuelto para que el desarrollador realice el `git pull origin develop` y los resuelva localmente.
4. **Mal uso de Git:** Commits genéricos ("arreglos", "commit") o intentos de fusión directa sin pasar por revisión.
