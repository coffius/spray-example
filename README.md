Пример использования Spray + Slick.
Для тестов используется Spec2 + Mockito

Запуск:
 - восстановить БД: pg_restore -d spray-example ./sql.dump
 - указать адрес, порт и имя БД:
    указать данных в файле `build.sbt` в строке `javaOptions := Seq("-Ddb.host=localhost", "-Ddb.port=5432", "-Ddb.name=spray-example")`
 - запустить приложение: sbt run