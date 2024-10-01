# Parcel Loading

Консольное приложение для погрузки посылок в грузовики. Имеет дополнительный функционал. Например, получение погруженного грузовика из json файла, подсчёт посылок и т.п.

## Содержание

- [Начало работы](#начало-работы)
- [Использование](#использование)
- [Тестирование](#тестирование)

## Начало работы

1. Установите JDK 17.
2. Установите Maven.

3. Клонируйте репозиторий себе на устройство:

```sh
$ git clone https://github.com/supervoid13/parcel-loading.git
```

2. Откройте проект в любой удобной для Вас среде разработки, например, IntelliJ IDEA.

3. Запустите метод `main` в `ru.liga.loading.ParcelLoadingApplication.java`.

## Использование

(Файлы для ручного тестирования находятся по пути `src/test/resources/manualtest`)

Выберите действие, которое хотите выполнить (введите в консоль цифру):

```sh
1 - load parcels, 2 - specify parcels
```

### 1. Погрузка посылок в грузовики

- Введите путь к файлу:

```sh
Enter file path:
```

- Введите метод погрузки:

```sh
Enter mode (simple - 1, effective - 2, uniform - 3):
```

#### 1.1. Простая погрузка (1 посылка в 1 грузовик)

На экране появится вывод, например:

```sh\
+      +
+      +
+      +
+999   +
+999   +
+999   +
++++++++

+      +
+      +
+      +
+      +
+666   +
+666   +
++++++++
```

#### 1.2. Эффективная погрузка (использование всей вместимости грузовиков)

На экране появится вывод, например:

```sh
+      +
+1     +
+999333+
+999666+
+999666+
+555551+
++++++++
```

#### 1.3. Равномерная погрузка

- Введи количество грузовиков:

```sh
Enter amount of trucks to load:
```

На экране появится вывод, например:

```sh
+      +
+      +
+      +
+      +
+      +
+55555 +
++++++++

+      +
+      +
+      +
+      +
+      +
+55555 +
++++++++
```

### 2. Подсчёт посылок из файла с грузовиками

- Введите путь к файлу:

```sh
Enter truck json file path:
```

На экране появится вывод, например:

```sh
Amount of parcels with rate of 1 - 2
Amount of parcels with rate of 3 - 2
Amount of parcels with rate of 5 - 2
Amount of parcels with rate of 6 - 1
Amount of parcels with rate of 9 - 1
Truck's body
+55555 +
+1333  +
+999333+
+999666+
+999666+
+555551+
++++++++
```

## Тестирование

Проект покрыт юнит-тестами JUnit 5. Для их запуска выполните команду:

```sh
$ mvn test
```
