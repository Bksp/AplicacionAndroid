# Registro Multimedia de Equipo — Guía del Proyecto, Entorno y Flujo de Git

Una aplicación móvil nativa para Android diseñada para la recolección, gestión y visualización dinámica de la información de los integrantes de un equipo de trabajo.

Este repositorio implementa una arquitectura **MVVM + Clean Architecture (por Capas)** en **Java 100%** para el desarrollo colaborativo. Para evitar conflictos de código y asegurar la estabilidad del sistema, todo el flujo de trabajo, la configuración del entorno y las reglas de auditoría se rigen bajo los siguientes protocolos obligatorios.

---

## 1. Características Principales

- **Formulario Interactivo:** Captura de datos utilizando una amplia gama de controles de UI (Spinner, CheckBox, RadioGroup, RatingBar) dentro de un `ScrollView`.
- **Seguimiento de Progreso:** Visualización del avance matemático mediante un `ProgressBar`.
- **Integración Multimedia:** Capacidad para grabar audio (micrófono) y reproducir notas de voz localmente en estricto formato `.m4a` con códec AAC, acompañada de un componente visual de ondas sonoras (`VisualizarOndas`).
- **Listado Dinámico:** Renderizado en tiempo real de los perfiles guardados utilizando un `RecyclerView` con tarjetas (`CardView`).
- **Persistencia de Datos:** Almacenamiento local mediante `SharedPreferences`, inicializado globalmente desde `MainApplication`.
- **Gestión de Permisos:** Solicitud dinámica de permisos en tiempo de ejecución para el acceso al micrófono (`RECORD_AUDIO`) mediante `MicPermissionHelper`.
- **Suite de Pruebas Unitarias:** Cobertura de la lógica de negocio y ViewModels utilizando `JUnit` y `androidx.arch.core:core-testing`.

---

## 2. Requisitos de Entorno y Stack Tecnológico

Para garantizar la compatibilidad exacta con la rúbrica docente y evitar errores de compilación, todo el equipo debe utilizar estrictamente las siguientes versiones:

- **IDE Requerido:** Android Studio (Panda 1 | 2025.3.1 Patch 1 o superior).
- **Lenguaje:** **Java 11** (Strictly No Kotlin / No Jetpack Compose).
- **SDK Mínimo (`minSdk`):** API 26 (Android 8.0 Oreo).
- **SDK Objetivo y Compilación (`targetSdk` / `compileSdk`):** API 36.
- **Sistema de Construcción:** Gradle 8.x con AGP (Android Gradle Plugin) 8.1.1+.
- **Gradle JDK:** JDK 11 o superior (Asegurar que el IDE apunte a este JDK).

### Dependencias del Proyecto (`app/build.gradle.kts`)

El proyecto integra las siguientes librerías de AndroidX, Material Components y Testing:

```kotlin
implementation("androidx.appcompat:appcompat:1.7.1")
implementation("com.google.android.material:material:1.13.0")
implementation("androidx.constraintlayout:constraintlayout:2.1.4")
implementation("androidx.recyclerview:recyclerview:1.4.0")
implementation("androidx.cardview:cardview:1.0.0")

// Unit Testing
testImplementation("junit:junit:4.13.2")
testImplementation("androidx.arch.core:core-testing:2.2.0")
```

---

## 3. Arquitectura del Proyecto (MVVM + Clean Architecture por Capas)

El código está estructurado bajo el patrón **MVVM + Clean Architecture** dividido en 3 capas fundamentales desacopladas:

```
app/src/main/java/com/example/registromultimedia/
├── domain/                    # Capa de Dominio (Casos de Uso e Interfaces)
│   ├── MemberRepository.java
│   ├── AudioRepository.java
│   ├── MemberRepositoryProvider.java
│   ├── AudioRepositoryProvider.java
│   └── usecase/
│       ├── AddMemberUseCase.java
│       ├── GetMembersUseCase.java
│       ├── ValidateMemberFormUseCase.java
│       ├── RecordAudioUseCase.java
│       ├── PlayAudioUseCase.java
│       └── ManageAudioUseCase.java
├── data/                      # Capa de Datos e Infraestructura
│   ├── MemberRepositoryImpl.java
│   ├── AudioRepositoryImpl.java
│   ├── AudioRecorder.java
│   ├── AudioPlayerManager.java
│   └── MicPermissionHelper.java
├── viewmodel/                 # Capa de Presentación (Estado y Lógica Reactiva)
│   ├── MembersViewModel.java
│   ├── FormViewModel.java
│   ├── AudioViewModel.java
│   └── IndicatorsViewModel.java
├── ui/                        # Capa de UI (Binders, Adapters y Vistas Personalizadas)
│   ├── FormBinder.java
│   ├── AudioBinder.java
│   ├── IndicatorsBinder.java
│   ├── MemberAdapter.java
│   └── VisualizarOndas.java
└── model/                     # Entidades y Estados de UI
    ├── Member.java
    ├── FormError.java
    └── AudioState.java
```

### Descripción de Capas:

1. **Domain (Dominio):**
   - Encapsula las reglas de negocio puras.
   - Contiene los **Casos de Uso** (`AddMemberUseCase`, `GetMembersUseCase`, `ValidateMemberFormUseCase`, `RecordAudioUseCase`, `PlayAudioUseCase`, `ManageAudioUseCase`).
   - Define las interfaces de repositorio (`MemberRepository`, `AudioRepository`) y proveedores tipo Registry/Service Locator (`MemberRepositoryProvider`, `AudioRepositoryProvider`).
   - Libre de dependencias del framework de Android.

2. **Data (Infraestructura y Datos):**
   - Implementa las interfaces de dominio (`MemberRepositoryImpl`, `AudioRepositoryImpl`).
   - `MemberRepositoryImpl` gestiona la persistencia local con `SharedPreferences`.
   - Encapsula la interacción con hardware (`AudioRecorder`, `AudioPlayerManager`, `MicPermissionHelper`).

3. **Presentation & UI (Presentación y Vistas):**
   - **ViewModels:** `MembersViewModel`, `FormViewModel`, `AudioViewModel`, `IndicatorsViewModel`. Consumen casos de uso y exponen LiveData reactivo a la UI. No importan clases visuales (`android.view.*`).
   - **Activities / Views:** `MainActivity`, `RegistroActivity`, `DashboardActivity`, `ReproductorActivity`, `SplashActivity`.
   - **Binders y Adaptadores:** `FormBinder`, `AudioBinder`, `IndicatorsBinder`, `MemberAdapter`.
   - **Custom Views:** `VisualizarOndas` para el gráfico interactivo de la señal de audio.

4. **Application Global (`MainApplication`):**
   - Hereda de `android.app.Application` e inicializa el contexto global del repositorio (`MemberRepositoryProvider.setInstance(...)`) para habilitar la persistencia continua de datos al arrancar la app.

---

## 4. Estructura de Ramas

El repositorio está protegido. Nadie trabaja ni sube código directamente sobre las ramas de integración o producción.

| Rama Base / Capa    | Propósito                                                                                                                |
| ------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| `main`              | **Producción.** Intocable. Solo recibe el código final para la entrega.                                                  |
| `develop`           | **Integración.** Rama de ensamblaje controlada por el equipo Core. Recibe los Pull Requests.                             |
| `presentation/base` | **Capa de Presentación.** Rama base para la UI, maquetación XML, adaptadores (`MemberAdapter`) y ViewModels.             |
| `domain/base`       | **Capa de Dominio.** Rama base para entidades del modelo (`Member.java`), contratos y casos de uso (`usecase/`).         |
| `data/base`         | **Capa de Datos.** Rama base para gestión de hardware (`MediaRecorder`, `MediaPlayer`), permisos y almacenamiento local. |

> **Nota sobre la Persistencia:** La implementación `MemberRepositoryImpl` utiliza **SharedPreferences**, asegurando que los registros de miembros persistan al reiniciar la aplicación. Esta persistencia se configura automáticamente desde `MainApplication`.

### Nomenclatura de Ramas de Trabajo (Feature Branches)

Todo desarrollador debe crear su rama desde la rama base de la capa asignada o desde `develop`:

- `presentation/<nombre-tarea>` (Ej: `presentation/viewmodels`, `presentation/form-binder`)
- `domain/<nombre-tarea>` (Ej: `domain/usecases`, `domain/contracts`)
- `data/<nombre-tarea>` (Ej: `data/shared-preferences`, `data/audio-repository`)

---

## 5. Instalación, Ejecución y Pruebas

### Clonar y Levantar el Proyecto

1. Clona este repositorio:

```bash
git clone https://github.com/Bksp/AplicacionAndroid.git
cd AplicacionAndroid
```

2. Compilar la APK de depuración (CLI):

```bash
./gradlew assembleDebug
```

3. Instalar en el emulador (CLI):

```bash
./gradlew installDebug
```

### Ejecutar Pruebas Unitarias

Para verificar el correcto funcionamiento de los ViewModels y Casos de Uso:

```bash
./gradlew test
```

Los reportes de ejecuciones de pruebas se generan en `app/build/reports/tests/testDebugUnitTest/index.html`.

---

## 6. Procedimiento Obligatorio para Desarrollar (Git)

1. Asegúrate de estar sincronizado con la rama de integración:

```bash
git checkout develop
git pull origin develop
```

2. Crea tu rama de trabajo según tu asignación en el Kanban:

```bash
git checkout -b <capa>/<nombre-tarea>
```

3. Realiza tus cambios, ejecuta las pruebas unitarias (`./gradlew test`), compila localmente y haz commit.
4. Sube tu rama al servidor:

```bash
git push origin <capa>/<nombre-tarea>
```

5. Abre un **Pull Request (PR)** en GitHub dirigido hacia `develop`.

---

## 7. Estándar de Commits

Los mensajes deben seguir una nomenclatura formal y trazable basada en _Conventional Commits_:

- **`feat:`** (Nueva característica) -> _Ej: feat: añade ValidateMemberFormUseCase en la capa domain_
- **`fix:`** (Corrección de error) -> _Ej: fix: corrige liberación de recursos en AudioRepositoryImpl_
- **`ui:`** (Cambios visuales) -> _Ej: ui: actualiza márgenes y animación en VisualizarOndas_
- **`test:`** (Pruebas unitarias) -> _Ej: test: agrega pruebas de unidad para AudioViewModel_

---

## 8. Plantilla Obligatoria para Pull Requests (PR)

Al abrir un PR hacia `develop`, debes rellenar la siguiente estructura en la descripción:

```markdown
## Qué hace

(Descripción concisa en un máximo de 2 líneas de los cambios implementados)

## Tarjeta del Kanban resuelta

(Ejemplo: Resuelve tarea "Caso de Uso de Validación de Integrantes")

## Capa de Arquitectura

- [ ] Domain (`domain/`)
- [ ] Presentation (`presentation/`)
- [ ] Data (`data/`)
```

---

## 9. Criterios de Rechazo y Auditoría

El equipo de Integración revisará todo el código. Se aplicará **rechazo automático** a cualquier PR que incumpla lo siguiente:

1. **Infracción MVVM / Clean Architecture:** Importar widgets (`android.widget.*`, `android.view.*`) dentro de los ViewModels o la capa Domain.
2. **Violación de Capas:** Alterar archivos que no corresponden a la etiqueta (`presentation`, `domain`, `data`) de la tarea asignada.
3. **Fallo en Pruebas Unitarias:** PRs cuyos cambios rompan las pruebas (`./gradlew test`).
4. **Conflictos con `develop`:** Todo PR que arroje conflictos pendientes será devuelto para que el desarrollador realice `git pull origin develop` y los resuelva localmente.
5. **Mal uso de Git:** Commits genéricos ("arreglos", "commit") o intentos de fusión directa sin pasar por revisión.

---
