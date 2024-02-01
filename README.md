##Comandos git

git init

Inicializa un nuevo repositorio de Git en el directorio actual. Se usa al comenzar un proyecto nuevo o para convertir un proyecto existente en un repositorio de Git.
git clone [url]

Crea una copia local de un repositorio que ya existe en línea. La URL es la dirección del repositorio remoto.
git status

Muestra el estado del repositorio actual incluyendo cambios que han sido añadidos al área de staging (preparados para commit) y cambios que no están preparados.
git add [archivo]

Añade los cambios en el archivo especificado al área de staging, preparándolos para ser incluidos en el próximo commit. Usar git add . añade todos los cambios actuales.
git commit -m "mensaje"

Guarda los cambios que están en el área de staging en el repositorio local. El mensaje debe ser una descripción breve de los cambios realizados.
git push [remote] [branch]

Envía los commits del repositorio local a la rama especificada del repositorio remoto. Por lo general, remote es origin (el repositorio desde el que se clonó) y branch es la rama a la que se empujan los cambios.
git pull [remote] [branch]

Actualiza el repositorio local con los cambios más recientes del repositorio remoto especificado y la rama. Esto es una combinación de git fetch y git merge.
git branch [nombre-rama]

Crea una nueva rama en el repositorio. No cambia automáticamente a la nueva rama.
git checkout [nombre-rama]

Cambia a la rama especificada. Si se usa junto con -b, crea una nueva rama y luego cambia a ella.
git merge [nombre-rama]

Fusiona los cambios de la rama especificada en la rama actual.
git log

Muestra el historial de commits en la rama actual, incluyendo el autor, la fecha y el mensaje de commit.
git diff

Muestra las diferencias entre archivos o ramas que no han sido añadidos al área de staging.
git fetch [remote]

Descarga los cambios más recientes del repositorio remoto, pero no los integra en el repositorio local.
git reset [archivo]

Quita un archivo del área de staging sin perder los cambios realizados en él.
git remote add [nombre-remote] [url]

Añade un nuevo repositorio remoto con el nombre y la URL especificados.


# controla_tus_habitos
Proyecto para la asignatura de Desarrollo Movil que consiste en una aplicacion de tracking de tus habitos
