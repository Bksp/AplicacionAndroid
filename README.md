# Registro Multimedia — Guía del Proyecto, Entorno y Flujo de Git

Este repositorio implementa una arquitectura **MVVM estricta** en Java 100% para el desarrollo colaborativo de 20 personas divididas en 5 grupos. Para evitar conflictos de código y asegurar la estabilidad del sistema, todo el flujo de trabajo, la configuración del entorno y las reglas de auditoría se rigen bajo los siguientes protocolos obligatorios.

---

## 1. Descripción General del Proyecto

**Registro Multimedia** es una aplicación nativa para Android desarrollada en Java 100% siguiendo una arquitectura **MVVM (Model-View-ViewModel) estricta**. La aplicación está diseñada para ser construida de forma colaborativa entre 20 desarrolladores divididos en 5 equipos de trabajo (G1 a G5).

### Funcionalidades Principales y Módulos:
1. **Formulario de Registro de Integrantes (G2):** Captura de datos personales (nombre, rol, correo) con validación reactiva de errores mediante `FormViewModel`, `FormBinder` y `FormError`.
2. **Indicadores y Métricas del Sistema (G3):** Monitor en tiempo real del total de integrantes registrados y audios capturados expuesto vía `IndicatorsViewModel` e `IndicatorsBinder`.
3. **Módulo de Audio Multimedia (G4):** Captura y reproducción de notas de voz haciendo uso de `MediaRecorder` y `MediaPlayer` a través de `AudioRecorder`, `AudioViewModel` y `AudioBinder`, gestionando permisos de micrófono en runtime con `MicPermissionHelper`.
4. **Visualización e Interfaz Gráfica (G5):** Presentación del listado de miembros mediante `RecyclerView` (`MemberAdapter`), tarjetas personalizadas (`CardView`), gráficos vectoriales (`ic_avatar.xml`), `MockData` y recursos de UI estandarizados (`strings.xml`, `colors.xml`, `dimens.xml`, `themes.xml`).
5. **Orquestación Central (G1):** `MainActivity` como View raíz que conecta todos los componentes e instancializa los ViewModels y Binders sin albergar lógica de negocio.

---

## 2. Requisitos de Entorno y Stack Tecnológico

Para garantizar la compatibilidad exacta con la rúbrica docente y evitar errores de compilación, todo el equipo debe utilizar estrictamente las siguientes versiones:

* **IDE Recomendado:** Android Studio (Ladybug / Jellyfish / Iguana o superior).
* **Lenguaje:** **Java 11** (Strictly No Kotlin / No Jetpack Compose).
* **SDK Mínimo (`minSdk`):** API 24 (Android 7.0 Nougat).
* **SDK Objetivo y Compilación (`targetSdk` / `compileSdk`):** API 34 o superior.
* **Sistema de Construcción:** Gradle 8.x con AGP (Android Gradle Plugin) 8.1.1+ (Se requiere que el Gradle JDK de Android Studio apunte a un **JDK 11 o superior** compatible con tu versión local de Gradle).

### Dependencias Principales (`app/build.gradle`)
El proyecto integra por defecto las siguientes librerías de AndroidX y Material Components:
```groovy
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.9.0'
implementation 'androidx.recyclerview:recyclerview:1.3.1'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.lifecycle:lifecycle-viewmodel:2.6.2'
implementation 'androidx.lifecycle:lifecycle-livedata:2.6.2'
```

---

## 3. Procedimiento para Clonar, Levantar y Ejecutar el Proyecto

### Paso A: Desde Android Studio (Recomendado)

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd AplicacionAndroid
   ```
2. **Abrir en Android Studio:**
   - Selecciona **File -> Open...** (o *Open* en la pantalla de bienvenida).
   - Selecciona la carpeta raíz del proyecto (`AplicacionAndroid`).
3. **Sincronizar dependencias de Gradle:**
   - Si no inicia automáticamente, haz clic en el icono de elefante **"Sync Project with Gradle Files"** en la barra superior derecha.
4. **Configurar Emulador o Dispositivo Físico:**
   - Abre el **Device Manager** en Android Studio.
   - Crea o inicia un Virtual Device (AVD) con **Android 7.0 (API 24)** o superior.
   - Si usas un dispositivo físico, activa la *Depuración por USB* (USB Debugging).
5. **Compilar y Ejecutar:**
   - Presiona el botón verde de **Run 'app'** (`Shift + F10`) en la barra de herramientas.

### Paso B: Desde la Línea de Comandos (CLI)

Si dispones de Android SDK configurado en tu terminal:

1. **Compilar la APK de depuración:**
   ```bash
   ./gradlew assembleDebug
   ```
2. **Instalar e iniciar en el emulador o dispositivo conectado:**
   ```bash
   ./gradlew installDebug
   ```

---

## 4. Estructura de Ramas

El desarrollo se aísla mediante *Feature Branches* por grupo. Nadie trabaja directamente sobre `main`.

| Rama | Responsable | Propósito y Clases Clave |
| --- | --- | --- |
| `main` | Integrador (Alessy) | Rama protegida de producción. Requiere Pull Request y aprobación. |
| `feat/g2-controles` | Grupo 2 | Lógica de formularios y validaciones (`FormViewModel`, `FormBinder`, `FormError`). |
| `feat/g3-indicadores` | Grupo 3 | Manejo de contadores y progreso visual (`IndicatorsViewModel`, `IndicatorsBinder`). |
| `feat/g4-audio` | Grupo 4 | Gestión de hardware y permisos de audio (`AudioRecorder`, `AudioViewModel`, `AudioBinder`, `MicPermissionHelper`). |
| `feat/g5-diseno` | Grupo 5 | Estructura XML (`activity_main.xml`, `item_member.xml`), recursos (`res/`) y adaptador (`MemberAdapter`, `MembersViewModel`, `MockData`). |

*Nota para los sublíderes:* Dentro de la rama de su grupo, pueden coordinar sub-ramas internas (ej. `feat/g2-spinner`), pero la rama oficial que abrirá el Pull Request hacia `main` debe ser la asignada en la tabla anterior.

---

## 5. Procedimiento para Colaborar y Hacer Push / Pull Request

Cada sublíder de grupo es responsable de gestionar los cambios de su equipo antes de enviarlos a revisión:

1. **Actualiza tu rama local antes de enviar cambios:**
   ```bash
   git checkout feat/gX-tu-grupo
   git pull origin feat/gX-tu-grupo
   ```
2. **Sincroniza con los últimos cambios de `main` (Prevención de conflictos):**
   ```bash
   git fetch origin main
   git merge origin/main
   ```
   *(Si surgen conflictos en este punto, el grupo responsable debe resolverlos localmente y probar que compile antes de continuar).*
3. **Sube tus cambios a tu rama remota:**
   ```bash
   git push origin feat/gX-tu-grupo
   ```
4. **Abre un Pull Request (PR):**
   - Dirígete a GitHub, ve a la pestaña **Pull Requests** y haz clic en *New Pull Request*.
   - Selecciona `base: main` y `compare: feat/gX-tu-grupo`.
   - Utiliza obligatoriamente la **Plantilla de PR** descrita en la sección 7 de este documento.

---

## 6. Formato y Estándar de Commits

Para mantener un historial limpio, auditable y fácil de rastrear por parte de la docencia, los mensajes de commit deben seguir strictly el siguiente formato:

```text
Gx: [verbo de acción en imperativo] breve descripción técnica
```

### Ejemplos válidos:
- `G2: añade validación de campos vacíos en FormViewModel`
- `G4: implementa permisos de micrófono y configuración de AudioRecorder`
- `G5: actualiza layout principal y agrega tarjeta de integrante`

**Regla de oro:** No se aceptarán commits con mensajes genéricos o vacíos (ej. "cambios", "arreglo", "commit final"). Cada commit debe reflejar una unidad lógica de trabajo trazable al grupo correspondiente.

---

## 7. Plantilla Obligatoria para Pull Requests (PR)

Al abrir un PR hacia `main`, el sublíder debe rellenar la siguiente estructura en la descripción:

```markdown
## Qué hace
(Descripción concisa en un máximo de 2 líneas de los cambios implementados)

## Ítems del contrato / RF que cubre
- [ ] RF-XX: ...

## Capa de Arquitectura MVVM que toca
- [ ] Model (`model/`, `data/`)
- [ ] ViewModel (`viewmodel/`)
- [ ] View (`ui/`, XML, Adapters)

## Archivos modificados fuera del alcance de mi grupo
(Indicar "Ninguno" o detallar el motivo justificado si se tocó MainActivity u otro archivo)

## Cómo probarlo
1. Pasos claros para verificar la funcionalidad en el emulador o dispositivo físico.

## Captura o evidencia
(Opcional, recomendado para cambios visuales de G5)
```

---

## 8. Criterios de Rechazo y Protocolo de Auditoría (Pull Request)

El Integrador (Alessy) actúa como guardián estricto del repositorio y aplicará un rechazo automático a cualquier Pull Request que incumpla cualquiera de los siguientes lineamientos técnicos y contractuales:

1. **Infracción del Patrón MVVM (UI en ViewModels):**
   - Quedan prohibidas las importaciones de `android.widget.*`, `android.view.*`, referencias a `R.id.*`, `R.string.*` o el uso de contextos visuales/Activities dentro de la capa `viewmodel/`.
   - Todo cambio que rompa el desacoplamiento reactivo mediante `LiveData` será rechazado de inmediato.

2. **Violación de la Matriz de Aislamiento (Archivos Ajenos):**
   - Ningún grupo tiene permitido modificar archivos pertenecientes a la columna arquitectónica de otro equipo (según la matriz de propiedad asignada).
   - Cualquier alteración no autorizada en archivos que no correspondan a su rama provocará el cierre fulminante del PR.

3. **Conflictos Activos con `main`:**
   - El Integrador **no** resolverá los conflictos de código de los grupos.
   - Todo PR que arroje conflictos pendientes de fusión con la rama principal será devuelto inmediatamente al sublíder para que su equipo realice el `git pull` y la resolución local obligatoria.

4. **Incumplimiento del Estándar de Commits:**
   - Se rechazarán todos los PRs cuyos historiales de commits no cumplan rigurosamente con la nomenclatura formal `Gx: [acción] descripción`. No se tolerarán mensajes ambiguos, vacíos o que oculten la trazabilidad del trabajo colaborativo exigido por la rúbrica docente.

5. **Omisión de Pruebas o Datos de Validación:**
   - Todo PR debe incluir explícitamente en su descripción la metodología de prueba funcional para verificar que el módulo compila y opera sin romper los flujos anteriores (como el registro de integrantes o la captura de audio).
